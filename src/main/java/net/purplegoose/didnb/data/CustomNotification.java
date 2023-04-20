package net.purplegoose.didnb.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomNotification {

    private final String channelID;
    private String guildID;
    private String message;
    private String weekday;
    private String time;
    private String customMessageID;
    private boolean isRepeating;
    private boolean enabled;

}
