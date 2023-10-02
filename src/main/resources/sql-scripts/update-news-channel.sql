UPDATE news_channels
SET
di_enabled = ?,
hearthstone_enabled = ?,
overwatch_enabled = ?,
wow_web_enabled = ?,
bnet_enabled = ?,
cortez_enabled = ?,
d4_enabled = ?,
heroes_enabled = ?,
news_enabled = ?
WHERE channelid = ?;