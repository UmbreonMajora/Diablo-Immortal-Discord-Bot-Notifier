CREATE TABLE IF NOT EXISTS news_channels
(
    channelid varchar(50) not null,
    guildid varchar(50) not null,
    di_enabled tinyint(1) DEFAULT 0,
    heartstone_enabled tinyint(1) DEFAULT 0
)