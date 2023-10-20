package net.purplegoose.bot.di.events;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.purplegoose.bot.di.cache.ClientCache;
import net.purplegoose.bot.di.commands.NotificationCommand;
import net.purplegoose.bot.di.data.ClientGuild;
import net.purplegoose.bot.di.database.DatabaseRequests;
import net.purplegoose.didnb.utils.PermissionUtil;

@Slf4j
public class SlashCommandInteraction extends ListenerAdapter {
    private final ClientCache cache;
    private final DatabaseRequests databaseRequests;
    private final NotificationCommand notificationCommand;

    public SlashCommandInteraction(ClientCache cache, DatabaseRequests databaseRequests) {
        this.cache = cache;
        this.databaseRequests = databaseRequests;

        notificationCommand = new NotificationCommand(cache, databaseRequests);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Channel jdaChannel = event.getChannel();
        Guild jdaGuild = event.getGuild();
        if (!isExecutionValid(jdaChannel, jdaGuild)) {
            return;
        }

        assert jdaGuild != null : "jdaGuild was null.";
        String guildID = jdaGuild.getId();
        if (!doClientGuildExists(guildID)) {
            addClientGuild(guildID);
        }

        ClientGuild clientGuild = cache.getClientGuildByID(guildID);
        String botAdminRoleID = clientGuild.getBotAdminRoleID();
        Member member = event.getMember();
        if (!PermissionUtil.isUserPermitted(member, botAdminRoleID)) {
            log.info("{} tried to use a command without permissions.", event.getInteraction().getUser().getName());
            return;
        }

        event.deferReply().setEphemeral(true).queue();
        executeCommand(event);
    }

    private boolean isExecutionValid(Channel jdaChannel, Guild jdaGuild) {
        if (isPrivateChannel(jdaGuild)) {
            return false;
        }
        return isTextChannel(jdaChannel);
    }

    private boolean isPrivateChannel(Guild guild) {
        // Is null when sent in a private channel.
        return guild == null;
    }

    private boolean isTextChannel(Channel channel) {
        return channel instanceof TextChannel;
    }

    private boolean doClientGuildExists(String guildID) {
        return cache.doClientGuildExist(guildID);
    }

    private void addClientGuild(String guildID) {
        ClientGuild clientGuild = new ClientGuild(guildID);
        cache.addClientGuild(clientGuild);
        databaseRequests.insertClientGuild(clientGuild);
    }

    private void executeCommand(SlashCommandInteractionEvent event) {
        switch (event.getName().toLowerCase()) {
            case NotificationCommand.COMMAND -> notificationCommand.performCommand(event);
        }
    }
}