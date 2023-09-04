package net.purplegoose.didnb.commands.scheduled_events;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.purplegoose.didnb.annotations.CommandAnnotation;
import net.purplegoose.didnb.cache.GuildsCache;
import net.purplegoose.didnb.commands.Command;
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

    private final GuildsCache guildsCache;

    public static final String COMMAND = "se";

    private static final String ENABLED_MESSAGE = "Enabled: "; // todo: add this to languages file.
    private static final String CLICK_ON_SE_TO_TOGGLE = "Click on the event you want to toggle."; // todo: add this to languages file.

    @Override
    public void performCommand(SlashCommandInteractionEvent event, LoggingInformation logInfo) {
        String guildID = logInfo.getGuildID();
        Language language = guildsCache.getLanguageByGuildID(guildID);
        SelectMenu selectMenu = getSelectMenu(new ScheduledEventsSetting(guildID), language, logInfo);
        ActionRow actionRow = ActionRow.of(selectMenu);
        event.getHook().setEphemeral(true).sendMessage(CLICK_ON_SE_TO_TOGGLE).addComponents(actionRow).queue();
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
                    Object value = field.get(seSetting);
                    GameEvent gameEvent = GameEvent.findGameEventByRawName(fieldName.toLowerCase());
                    if (gameEvent == null) {
                        String error = String.format("Failed to get GameEvent enum for %s.", fieldName);
                        createErrorLogEntry(logInfo, ListEvents.class, error);
                    } else {
                        builder.addOption(LanguageController.getMessage(language, gameEvent.languageKey) + "  -  " + ENABLED_MESSAGE + value, fieldName);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return builder.build();
    }
}
