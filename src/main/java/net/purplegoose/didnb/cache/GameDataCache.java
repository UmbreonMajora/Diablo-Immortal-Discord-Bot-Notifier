package net.purplegoose.didnb.cache;

import lombok.Getter;
import lombok.Setter;
import net.purplegoose.didnb.annotations.GameDataCacheSet;
import net.purplegoose.didnb.data.EventGameData;
import net.purplegoose.didnb.data.EventTime;

import java.util.HashSet;


@Getter
@Setter
public class GameDataCache {

    private HashSet<EventTime> gameDataCache = new HashSet<>();

    @GameDataCacheSet(tableName = "wrathborne_invasion_times", everyday = true)
    private HashSet<EventGameData> wrathborneInvasionDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "ancient_nightmare_times")
    private HashSet<EventGameData> ancientNightmareDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "haunted_carriage_times")
    private HashSet<EventGameData> hauntedCarriageDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "tower_of_victory_times")
    private HashSet<EventGameData> towerOfVictoryDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "shadow_lottery_times", everyday = true)
    private HashSet<EventGameData> shadowLotteryDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "ancient_arena_times")
    private HashSet<EventGameData> ancientArenaDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "battleground_times", everyday = true)
    private HashSet<EventGameData> battlegroundDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "demon_gates_times")
    private HashSet<EventGameData> demonGatesDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "onslaught_times")
    private HashSet<EventGameData> onSlaughtDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "vault_times", everyday = true)
    private HashSet<EventGameData> vaultGameDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "shadow_war_times", everyday = true)
    private HashSet<EventGameData> shadowWarDataSet = new HashSet<>();

    @GameDataCacheSet(tableName = "assembly_times")
    private HashSet<EventGameData> assemblyDataSet = new HashSet<>();

}
