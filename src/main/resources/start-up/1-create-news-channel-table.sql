CREATE TABLE IF NOT EXISTS news_channels
(
    channelid varchar(50) not null primary key,
    guildid varchar(50) not null,
    di_enabled tinyint(1) DEFAULT 0,
    hearthstone_enabled tinyint(1) DEFAULT 0,
    overwatch_enabled tinyint(1) Default 0,
    wow_web_enabled tinyint(1) DEFAULT 0,
    bnet_enabled tinyint(1) Default 0,
    cortez_enabled tinyint(1) default 0,
    d4_enabled tinyint(1) default 0,
    heroes_enabled tinyint(1) default 0,
    news_enabled tinyint(1) default 0,
    war_enabled tinyint(1) default 0,
    blizzcon_enabled tinyint(1) default 0
)