INSERT INTO assembly_times (warn_range, warn_day, id)
VALUES ('18:45-19:00', 'Monday', 1) on duplicate key update id=id;