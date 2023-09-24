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

    public NewsChannelDTO(String channelID, String guildID) {
        this.channelID = channelID;
        this.guildID = guildID;
    }
}