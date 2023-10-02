UPDATE guilds
SET language              = ?,
    timezone              = ?,
    warn_messages_enabled = ?,
    event_messages_enabled= ?,
    admin_role_id         = ?,
    warn_time             = ?,
    daylight_time_enabled = ?,
    premium_server        = ?,
    auto_delete_enabled = ?,
    auto_delete_time = ?,
    embed_lead_time = ?
WHERE guildID = ?;