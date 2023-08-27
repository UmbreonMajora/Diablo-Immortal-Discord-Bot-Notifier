package net.purplegoose.didnb.gameevents;

import net.purplegoose.didnb.cache.GameDataCache;

public class GameEventBuilder {
    private GameDataCache gameDataCache;

    public GameEventBuilder withGameDataCache(GameDataCache gameDataCache) {
        this.gameDataCache = gameDataCache;
        return this;
    }

    public OnSlaughtEvent buildOnSlaughtEvent() {
        return new OnSlaughtEvent(gameDataCache);
    }

    public BattlegroundEvent buildBattlegroundEvent() {
        return new BattlegroundEvent(gameDataCache);
    }

    public TowerOfVictoryEvent buildTowerOfVictoryEvent() {
        return new TowerOfVictoryEvent(gameDataCache);
    }

    public VaultEvent buildVaultEvent() {
        return new VaultEvent(gameDataCache);
    }

    public AssemblyEvent buildAssemblyEvent() {
        return new AssemblyEvent(gameDataCache);
    }

    public AncientNightmareEvent buildAncientNightmareEvent() {
        return new AncientNightmareEvent(gameDataCache);
    }

    public ShadowWarEvent buildShadowWarEvent() {
        return new ShadowWarEvent(gameDataCache);
    }

    public DemonGatesEvent buildDemonGatesEvent() {
        return new DemonGatesEvent(gameDataCache);
    }

    public HauntedCarriageEvent buildHauntedCarriageEvent() {
        return new HauntedCarriageEvent(gameDataCache);
    }

    public AncientArenaEvent buildAncientArenaEvent() {
        return new AncientArenaEvent(gameDataCache);
    }

    public WrathborneInvasionEvent buildWrathborneInvasionEvent() {
        return new WrathborneInvasionEvent(gameDataCache);
    }

    public ShadowLotteryEvent buildShadowLotteryEvent() {
        return new ShadowLotteryEvent(gameDataCache);
    }
}
