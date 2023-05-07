INSERT INTO assembly_times (warn_range, warn_day, id)
VALUES
('18:45-19:00', 'Monday', 1),
('18:45-19:00', 'Tuesday', 2),
('18:45-19:00', 'Wednesday', 3),
('18:45-19:00', 'Thursday', 4),
('18:45-19:00', 'Friday', 5),
('18:45-19:00', 'Saturday', 6)
on duplicate key update id=id;