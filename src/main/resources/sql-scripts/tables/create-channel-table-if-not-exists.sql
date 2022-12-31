CREATE TABLE IF NOT EXISTS channel_notification
(
    textChannelID           VARCHAR(20)          NOT NULL PRIMARY KEY,
    guildID                 VARCHAR(20)          NOT NULL,
    roleID                  VARCHAR(20)          NOT NULL DEFAULT 'EVERYONE',
    message                 TINYINT(1) DEFAULT 1 NOT NULL,
    headUp                  TINYINT(1) DEFAULT 1 NOT NULL,
    ancientarena            TINYINT(1) DEFAULT 1 NOT NULL,
    ancientnightmare        TINYINT(1) DEFAULT 1 NOT NULL,
    assembly                TINYINT(1) DEFAULT 1 NOT NULL,
    battlegrounds           TINYINT(1) DEFAULT 1 NOT NULL,
    vault                   TINYINT(1) DEFAULT 1 NOT NULL,
    demongates              TINYINT(1) DEFAULT 1 NOT NULL,
    shadowlottery           TINYINT(1) DEFAULT 1 NOT NULL,
    hauntedcarriage         TINYINT(1) DEFAULT 1 NOT NULL,
    hauntedcarriageembed    TINYINT(1) DEFAULT 1 NOT NULL,
    demongatesembed         TINYINT(1) DEFAULT 1 NOT NULL,
    ancientnightmareembed   TINYINT(1) DEFAULT 1 NOT NULL,
    ancientarenaembed       TINYINT(1) DEFAULT 1 NOT NULL,
    wrathborneinvasion      TINYINT(1) DEFAULT 1 NOT NULL,
    onslaught_event_enabled TINYINT(1) DEFAULT 1 NOT NULL
);