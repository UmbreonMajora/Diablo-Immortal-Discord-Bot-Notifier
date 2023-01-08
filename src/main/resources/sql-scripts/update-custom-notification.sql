UPDATE custom_messages
SET message         = ?,
    day             = ?,
    time            = ?,
    message_repeat  = ?,
    message_enabled = ?
WHERE channelID = ?;
