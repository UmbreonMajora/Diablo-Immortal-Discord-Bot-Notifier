UPDATE scheduled_events_settings
SET    ancient_arena = ?,
       ancient_nightmare = ?,
       assembly = ?,
       battlegrounds = ?,
       demon_gates = ?,
       haunted_carriage = ?,
       stormpoint = ?,
       shadow_lottery = ?,
       shadow_war = ?,
       accursed_tower = ?,
       vault = ?,
       wrathborne_invasion = ?
where  guildid = ?;