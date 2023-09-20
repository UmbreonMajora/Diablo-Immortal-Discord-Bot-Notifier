INSERT INTO assembly_times (warn_range, warn_day, id)
VALUES
('18:45-19:00', 'Monday', 1),
('18:45-19:00', 'Tuesday', 2),
('18:45-19:00', 'Wednesday', 3),
('18:45-19:00', 'Thursday', 4),
('18:45-19:00', 'Friday', 5),
('18:45-19:00', 'Saturday', 6),
('11:45-12:00', 'Monday', 7),
('11:45-12:00', 'Tuesday', 8),
('11:45-12:00', 'Wednesday', 9),
('11:45-12:00', 'Thursday', 10),
('11:45-12:00', 'Friday', 11),
('11:45-12:00', 'Saturday', 12)
on duplicate key update id=id;