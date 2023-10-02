INSERT INTO demon_gates_times (warn_range, warn_day, id)
VALUES
('11:45-12:00', 'Monday', 1),
('21:15-20:30', 'Monday', 2),
('21:45-22:00', 'Monday', 3),
('11:45-12:00', 'Thursday', 4),
('21:15-20:30', 'Thursday', 5),
('21:45-22:00', 'Thursday', 6),
('11:45-12:00', 'Monday', 7),
('21:15-20:30', 'Monday', 8),
('21:45-22:00', 'Monday', 9)
on duplicate key update id=id;