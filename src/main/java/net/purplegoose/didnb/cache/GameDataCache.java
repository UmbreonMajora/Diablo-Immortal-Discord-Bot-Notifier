package net.purplegoose.didnb.cache;

import net.purplegoose.didnb.data.EventGameData;

import java.util.HashSet;
import java.util.Set;

public class GameDataCache {

    private Set<EventGameData> wrathborneInvasionDataSet = new HashSet<>();
    private Set<EventGameData> ancientNightmareDataSet = new HashSet<>();
    private Set<EventGameData> hauntedCarriageDataSet = new HashSet<>();
    private Set<EventGameData> shadowLotteryDataSet = new HashSet<>();
    private Set<EventGameData> ancientArenaDataSet = new HashSet<>();
    private Set<EventGameData> battlegroundDataSet = new HashSet<>();
    private Set<EventGameData> demonGatesDataSet = new HashSet<>();
    private Set<EventGameData> onSlaughtDataSet = new HashSet<>();
    private Set<EventGameData> vaultGameDataSet = new HashSet<>();
    private Set<EventGameData> assemblyDataSet = new HashSet<>();

    public Set<EventGameData> getVaultGameDataSet() {
        return vaultGameDataSet;
    }

    public void setVaultGameDataSet(Set<EventGameData> vaultGameDataSet) {
        this.vaultGameDataSet = vaultGameDataSet;
    }

    public Set<EventGameData> getHauntedCarriageDataSet() {
        return hauntedCarriageDataSet;
    }

    public void setHauntedCarriageDataSet(Set<EventGameData> hauntedCarriageDataSet) {
        this.hauntedCarriageDataSet = hauntedCarriageDataSet;
    }

    public Set<EventGameData> getDemonGatesDataSet() {
        return demonGatesDataSet;
    }

    public void setDemonGatesDataSet(Set<EventGameData> demonGatesDataSet) {
        this.demonGatesDataSet = demonGatesDataSet;
    }

    public Set<EventGameData> getWrathborneInvasionDataSet() {
        return wrathborneInvasionDataSet;
    }

    public void setWrathborneInvasionDataSet(Set<EventGameData> wrathborneInvasionDataSet) {
        this.wrathborneInvasionDataSet = wrathborneInvasionDataSet;
    }

    public Set<EventGameData> getAncientNightmareDataSet() {
        return ancientNightmareDataSet;
    }

    public void setAncientNightmareDataSet(Set<EventGameData> ancientNightmareDataSet) {
        this.ancientNightmareDataSet = ancientNightmareDataSet;
    }

    public Set<EventGameData> getAncientArenaDataSet() {
        return ancientArenaDataSet;
    }

    public void setAncientArenaDataSet(Set<EventGameData> ancientArenaDataSet) {
        this.ancientArenaDataSet = ancientArenaDataSet;
    }

    public Set<EventGameData> getShadowLotteryDataSet() {
        return shadowLotteryDataSet;
    }

    public void setShadowLotteryDataSet(Set<EventGameData> shadowLotteryDataSet) {
        this.shadowLotteryDataSet = shadowLotteryDataSet;
    }

    public Set<EventGameData> getBattlegroundDataSet() {
        return battlegroundDataSet;
    }

    public void setBattlegroundDataSet(Set<EventGameData> battlegroundDataSet) {
        this.battlegroundDataSet = battlegroundDataSet;
    }

    public Set<EventGameData> getAssemblyDataSet() {
        return assemblyDataSet;
    }

    public void setAssemblyDataSet(Set<EventGameData> assemblyDataSet) {
        this.assemblyDataSet = assemblyDataSet;
    }

    public Set<EventGameData> getOnSlaughtDataSet() {
        return onSlaughtDataSet;
    }

    public void setOnSlaughtDataSet(Set<EventGameData> onSlaughtDataSet) {
        this.onSlaughtDataSet = onSlaughtDataSet;
    }
}
