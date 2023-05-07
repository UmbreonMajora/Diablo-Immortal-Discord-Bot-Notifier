INSERT INTO onslaught_times (warn_range, warn_day, id)
VALUES
('11:45-12:00', 'Sunday', 1),
('20:15-20:30', 'Sunday', 2),
('21:45-22:00', 'Sunday', 3)
on duplicate key update id=id;