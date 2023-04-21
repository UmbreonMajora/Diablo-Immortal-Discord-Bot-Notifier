package net.purplegoose.didnb.cache;

import lombok.Getter;
import lombok.Setter;
import net.purplegoose.didnb.data.EventGameData;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class GameDataCache {

    private Set<EventGameData> wrathborneInvasionDataSet = new HashSet<>();
    private Set<EventGameData> ancientNightmareDataSet = new HashSet<>();
    private Set<EventGameData> hauntedCarriageDataSet = new HashSet<>();
    private Set<EventGameData> towerOfVictoryDataSet = new HashSet<>();
    private Set<EventGameData> shadowLotteryDataSet = new HashSet<>();
    private Set<EventGameData> ancientArenaDataSet = new HashSet<>();
    private Set<EventGameData> battlegroundDataSet = new HashSet<>();
    private Set<EventGameData> demonGatesDataSet = new HashSet<>();
    private Set<EventGameData> onSlaughtDataSet = new HashSet<>();
    private Set<EventGameData> vaultGameDataSet = new HashSet<>();
    private Set<EventGameData> assemblyDataSet = new HashSet<>();

}
