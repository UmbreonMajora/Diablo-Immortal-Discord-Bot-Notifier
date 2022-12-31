CREATE TABLE IF NOT EXISTS guilds
(
    guildID                CHAR(20) PRIMARY KEY NOT NULL,
    language               CHAR(5)  DEFAULT 'ENG',
    timezone               CHAR(10) DEFAULT 'GMT',
    warn_messages_enabled  TINYINT(1)   DEFAULT 1   NOT NULL,
    event_messages_enabled TINYINT(1)   DEFAULT 1   NOT NULL,
    admin_role_id          CHAR(20),
    warn_time              TINYINT(15)  DEFAULT 15  NOT NULL
);