CREATE TABLE IF NOT EXISTS channel_notification
(
    textChannelID         VARCHAR(20)          NOT NULL PRIMARY KEY,
    guildID               VARCHAR(20)          NOT NULL,
    roleID                VARCHAR(20)          NOT NULL DEFAULT 'EVERYONE',
    message               TINYINT(1) DEFAULT 0 NOT NULL,
    headUp                TINYINT(1) DEFAULT 0 NOT NULL,
    ancientarena          TINYINT(1) DEFAULT 0 NOT NULL,
    ancientnightmare      TINYINT(1) DEFAULT 0 NOT NULL,
    assembly              TINYINT(1) DEFAULT 0 NOT NULL,
    battlegrounds         TINYINT(1) DEFAULT 0 NOT NULL,
    defendvault           TINYINT(1) DEFAULT 0 NOT NULL,
    raidvault             TINYINT(1) DEFAULT 0 NOT NULL,
    demongates            TINYINT(1) DEFAULT 0 NOT NULL,
    shadowlottery         TINYINT(1) DEFAULT 0 NOT NULL,
    hauntedcarriage       TINYINT(1) DEFAULT 0 NOT NULL,
    hauntedcarriageembed  TINYINT(1) DEFAULT 0 NOT NULL,
    demongatesembed       TINYINT(1) DEFAULT 0 NOT NULL,
    ancientnightmareembed TINYINT(1) DEFAULT 0 NOT NULL,
    ancientarenaembed     TINYINT(1) DEFAULT 0 NOT NULL,
    wrathborneinvasion    TINYINT(1) DEFAULT 0 NOT NULL
)