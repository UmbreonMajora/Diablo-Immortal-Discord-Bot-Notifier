UPDATE guilds
SET language      = ?,
    timezone      = ?,
    event_headup  = ?,
    event_message = ?,
    adminroleid   = ?,
    headup_time   = ?
WHERE guildID = ?;