INSERT INTO onslaught_times (warn_range, warn_day, id)
VALUES ('20:15-20:30', 'Sunday', 2) on duplicate key update id=id;