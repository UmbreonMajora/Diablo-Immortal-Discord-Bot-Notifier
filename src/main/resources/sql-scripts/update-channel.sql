UPDATE channel_notification
SET roleID                = ?,
    message               = ?,
    headUp                = ?,
    ancientarena          = ?,
    ancientnightmare      = ?,
    assembly              = ?,
    battlegrounds         = ?,
    vault                 = ?,
    demongates            = ?,
    shadowlottery         = ?,
    hauntedcarriage       = ?,
    demongatesembed       = ?,
    ancientnightmareembed = ?,
    hauntedcarriageembed  = ?,
    wrathborneinvasion    = ?
WHERE textChannelID = ?;