CREATE TABLE IF NOT EXISTS reaction_role
(
    messageID VARCHAR(20) NOT NULL,
    guildID   VARCHAR(20) NOT NULL,
    roleID    VARCHAR(20) NOT NULL,
    emojiCode TEXT        NOT NULL,
    emojiType VARCHAR(20) NOT NULL
);