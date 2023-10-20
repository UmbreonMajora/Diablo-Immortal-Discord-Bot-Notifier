package net.purplegoose.bot.di.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.purplegoose.didnb.enums.GameEvent;

@AllArgsConstructor
@Data
public class ClientChannel {
    private final String guildID;
    private final String channelID;
    private final GameEvent gameEvent;
}