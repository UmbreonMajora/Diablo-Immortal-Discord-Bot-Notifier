package net.purplegoose.didnb.events;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.data.ScheduledEventsSetting;
import net.purplegoose.didnb.database.DatabaseRequests;
import net.purplegoose.didnb.enums.GameEvent;
import net.purplegoose.didnb.enums.Language;
import net.purplegoose.didnb.exeption.StringSelectInteractionException;
import net.purplegoose.didnb.languages.LanguageController;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

@AllArgsConstructor
public class StringSelectInteraction extends ListenerAdapter {

    private final GuildsCache guildsCache;
    private final DatabaseRequests databaseRequests;

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        Guild guild = event.getGuild();
        if (isInDirectMessages(guild)) return;
        String guildID = guild.getId();
        Language language = guildsCache.getLanguageByGuildID(guildID);
        ScheduledEventsSetting seSetting = guildsCache.getClientGuildByID(guildID).getSeSetting();

        for (SelectOption selectedOption : event.getSelectedOptions()) {
            String fieldName = selectedOption.getValue() + "Enabled";
            boolean currentValue = updateSettingsValueAndGetCurrentValue(fieldName, seSetting);
            String selectedEvent = selectedOption.getValue();
            GameEvent gameEvent = getGameEvent(selectedEvent);
            String responseLanguageKey = currentValue ? "SE-DISABLED-FOR" : "SE-ENABLED-FOR";
            String responseMessage = LanguageController.getMessage(language, responseLanguageKey);
            String gameEventDisplayName = LanguageController.getMessage(language, gameEvent.languageKey);
            event.reply(String.format(responseMessage, gameEventDisplayName)).setEphemeral(true).queue();
            updateData(seSetting);
        }
    }

    private boolean updateSettingsValueAndGetCurrentValue(String fieldName, ScheduledEventsSetting seSetting) {
        try {
            Field field = seSetting.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            boolean currentValue = field.getBoolean(seSetting);
            field.setBoolean(seSetting, !currentValue);
            return currentValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new StringSelectInteractionException(e.getMessage(), e);
        }
    }

    private void updateData(ScheduledEventsSetting seSetting) {
        String guildID = seSetting.getGuildID();
        databaseRequests.updateScheduledEventsSettings(seSetting);
        guildsCache.getClientGuildByID(guildID).setSeSetting(seSetting);
    }

    private GameEvent getGameEvent(String selectedOption) {
        GameEvent ge = GameEvent.findGameEventByRawName(selectedOption.toLowerCase());
        if (ge == null)
            throw new StringSelectInteractionException(String.format("Failed to get GameEvent Object for %s.", selectedOption), null);
        return ge;
    }

    private boolean isInDirectMessages(Guild guild) {
        return guild == null;
    }

}
