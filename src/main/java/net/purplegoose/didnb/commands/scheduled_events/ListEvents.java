package net.purplegoose.didnb.commands.scheduled_events;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.Command;
import net.purplegoose.didnb.data.ClientGuild;
import net.purplegoose.didnb.data.LoggingInformation;
import net.purplegoose.didnb.data.ScheduledEventsSetting;
import net.purplegoose.didnb.enums.GameEvent;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.languages.LanguageController;

import java.lang.reflect.Field;

@AllArgsConstructor
@CommandAnnotation(
        name = "se",
        usage = "/se",
        description = "This commands creates a dropdown menu where you can toggle the creation of scheduled events."
)
public class ListEvents extends Command {

    public static final String COMMAND = "se";
    private final GuildsCache guildsCache;

    @Override
    public void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getLanguageByGuildID(guildID);
        ClientGuild clientGuild = guildsCache.getClientGuildByID(guildID);
        ScheduledEventsSetting seSetting = clientGuild.getSeSetting();
        SelectMenu selectMenu = getSelectMenu(seSetting, language, logInfo);
        ActionRow actionRow = ActionRow.of(selectMenu);
        event.getHook().setEphemeral(true).sendMessage(LanguageController.getMessage(language, "CLICK-ON-SE-TO-TOGGLE")).addComponents(actionRow).queue();
        createUseLogEntry(logInfo, ListEvents.class);
    }

    private StringSelectMenu getSelectMenu(ScheduledEventsSetting seSetting, Language language, LoggingInformation logInfo) {
        StringSelectMenu.Builder builder = StringSelectMenu.create("menu:list_events");

        Class<?> clazz = seSetting.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName().replace("Enabled", "");

            if (!fieldName.equalsIgnoreCase("guildID") && !fieldName.contains("embed")) {
                try {
                    boolean value = (boolean) field.get(seSetting);
                    GameEvent gameEvent = GameEvent.findGameEventByRawName(fieldName.toLowerCase());
                    if (gameEvent == null) {
                        String error = String.format("Failed to get GameEvent enum for %s.", fieldName);
                        createErrorLogEntry(logInfo, ListEvents.class, error);
                    } else {
                        String gameEventDisplayName = LanguageController.getMessage(language, gameEvent.languageKey);
                        String enabledText = LanguageController.getMessage(language, "TEXT-ENABLED");
                        builder.addOption(gameEventDisplayName + "  -  " + enabledText + value, fieldName);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return builder.build();
    }
}
