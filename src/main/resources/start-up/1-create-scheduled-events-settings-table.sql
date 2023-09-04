CREATE TABLE IF NOT EXISTS scheduled_events_settings (
    guildID CHAR(20) PRIMARY KEY NOT NULL,
    ancient_arena TINYINT(1) DEFAULT 1 NOT NULL,
    ancient_nightmare TINYINT(1) DEFAULT 1 NOT NULL,
    assembly TINYINT(1) DEFAULT 1 NOT NULL,
    battlegrounds TINYINT(1) DEFAULT 1 NOT NULL,
    demon_gates TINYINT(1) DEFAULT 1 NOT NULL,
    haunted_carriage TINYINT(1) DEFAULT 1 NOT NULL,
    stormpoint TINYINT(1) DEFAULT 1 NOT NULL, -- aka onslaught
    shadow_lottery TINYINT(1) DEFAULT 1 NOT NULL,
    shadow_war TINYINT(1) DEFAULT 1 NOT NULL,
    accursed_tower TINYINT(1) DEFAULT 1 NOT NULL, -- aka tower of victory
    vault TINYINT(1) DEFAULT 1 NOT NULL,
    wrathborne_invasion TINYINT(1) DEFAULT 1 NOT NULL
);