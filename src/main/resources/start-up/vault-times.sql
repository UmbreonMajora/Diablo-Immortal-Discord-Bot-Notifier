INSERT INTO vault_times (warn_range, warn_day, id)
VALUES
('11:45-12:00', 'Monday', 1),
('18:45-19:00', 'Monday', 2),
('11:45-12:00', 'Tuesday', 3),
('18:45-19:00', 'Tuesday', 4),
('11:45-12:00', 'Wednesday', 5),
('18:45-19:00', 'Wednesday', 6),
('11:45-12:00', 'Thursday', 7),
('18:45-19:00', 'Thursday', 8),
('11:45-12:00', 'Friday', 9),
('18:45-19:00', 'Friday', 10),
('11:45-12:00', 'Saturday', 11),
('18:45-19:00', 'Saturday', 12)
on duplicate key update id=id;