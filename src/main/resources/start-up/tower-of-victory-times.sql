INSERT INTO tower_of_victory_times (warn_range, warn_day, id)
VALUES ('19:45-20:00', 'Thursday', 1), ('19:45-20:00', 'Saturday', 2) on duplicate key update id=id;