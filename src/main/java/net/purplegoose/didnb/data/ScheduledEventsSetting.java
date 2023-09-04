package net.purplegoose.didnb.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduledEventsSetting {

    public ScheduledEventsSetting(String guildID) {
        this.guildID = guildID;
    }

    private String guildID;
    private boolean ancientArenaEnabled;
    private boolean ancientNightmareEnabled;
    private boolean assemblyEnabled;
    private boolean battlegroundsEnabled;
    private boolean demonGatesEnabled;
    private boolean hauntedCarriageEnabled;
    private boolean stormpointEnabled;
    private boolean shadowLotteryEnabled;
    private boolean shadowWarEnabled;
    private boolean accursedTowerEnabled;
    private boolean vaultEnabled;
    private boolean wrathborneInvasionEnabled;

}
