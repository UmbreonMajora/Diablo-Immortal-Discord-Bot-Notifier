INSERT INTO ancient_nightmare_times (warn_range, warn_day, id)
VALUES
('11:45-12:00', 'Wednesday', 1),
('20:15-20:30', 'Wednesday', 2),
('21:45-22:00', 'Wednesday', 3),
('11:45-12:00', 'Friday', 4),
('20:15-20:30', 'Friday', 5),
('21:45-22:00', 'Friday', 6)
on duplicate key update id=id;