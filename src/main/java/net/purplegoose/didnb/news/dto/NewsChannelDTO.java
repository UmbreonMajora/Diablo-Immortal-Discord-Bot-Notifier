package net.purplegoose.didnb.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class NewsChannelDTO {

    private final String channelID;
    private final String guildID;
    private boolean diabloImmortalNewsEnabled = false;
    private boolean hearthStoneNewsEnabled = false;
    private boolean overwatchEnabled = false;
    private boolean wowWebEnabled = false;
    private boolean bnetEnabled = false;
    private boolean cortezEnabled = false;
    private boolean d4Enabled = false;
    private boolean heroesEnabled = false;
    private boolean newsEnabled = false;

    public NewsChannelDTO(String channelID, String guildID) {
        this.channelID = channelID;
        this.guildID = guildID;
    }
}