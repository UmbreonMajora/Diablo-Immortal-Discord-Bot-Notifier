package net.purplegoose.didnb.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomNotification {

    private final String channelID;
    private String guildID;
    private String message;
    private String weekday;
    private String time;
    private int customMessageID;
    private boolean isRepeating;
    private boolean enabled;

    public CustomNotification(String channelID, String guildID, String message, String weekday,
                              String time, int customMessageID, boolean isRepeating, boolean enabled) {
        this.channelID = channelID;
        this.guildID = guildID;
        this.message = message;
        this.weekday = weekday;
        this.time = time;
        this.customMessageID = customMessageID;
        this.isRepeating = isRepeating;
        this.enabled = enabled;
    }

    public CustomNotification(String channelID, String guildID, String message, String weekday,
                              String time, boolean isRepeating, boolean enabled) {
        this.channelID = channelID;
        this.guildID = guildID;
        this.message = message;
        this.weekday = weekday;
        this.time = time;
        this.isRepeating = isRepeating;
        this.enabled = enabled;
    }

}
