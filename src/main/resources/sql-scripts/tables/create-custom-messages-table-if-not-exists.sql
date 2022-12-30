CREATE TABLE IF NOT EXISTS custom_messages
(
    guildID          VARCHAR(50)           NOT NULL,
    channelID        VARCHAR(50)           NOT NULL,
    message          VARCHAR(2000)         NOT NULL,
    day              VARCHAR(10)           NOT NULL,
    time             VARCHAR(10)           NOT NULL,
    message_repeat   TINYINT(1)            NOT NULL,
    message_id       int                   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    autodelete       TINYINT(2) DEFAULT 24 NOT NULL,
    autodelete_value TINYINT(1) DEFAULT 0  NOT NULL,
    message_enabled  TINYINT(1) DEFAULT 1  NOT NULL
)