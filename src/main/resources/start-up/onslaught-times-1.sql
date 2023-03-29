INSERT INTO onslaught_times (warn_range, warn_day, id)
VALUES ('11:45-12:00', 'Sunday', 1) on duplicate key update id=id;