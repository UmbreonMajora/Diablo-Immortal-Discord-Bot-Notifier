INSERT INTO haunted_carriage_times (warn_range, warn_day, id)
VALUES
('11:45-12:00', 'Tuesday', 1),
('20:15-20:30', 'Tuesday', 2),
('21:45-22:00', 'Tuesday', 3),
('11:45-12:00', 'Saturday', 4),
('20:15-20:30', 'Saturday', 5),
('21:45-22:00', 'Saturday', 6)
on duplicate key update id=id;