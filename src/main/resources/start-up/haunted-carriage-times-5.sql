INSERT INTO haunted_carriage_times (warn_range, warn_day, id)
VALUES ('20:15-20:30', 'Saturday', 5) on duplicate key update id=id;