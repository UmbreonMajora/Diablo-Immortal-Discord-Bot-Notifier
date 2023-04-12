package net.purplegoose.didnb.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoggingInformation {

    String executor;
    String guildName;
    String guildID;
    String channelName;
    String channelID;

    public String[] get() {
        return new String[] {executor, guildName, guildID, channelName, channelID};
    }

}
