INSERT INTO ancient_arena_times (warn_range, warn_day, id)
VALUES
('21:15-21:30', 'Tuesday', 1),
('21:15-21:30', 'Thursday', 2),
('21:15-21:30', 'Saturday', 3),
('21:15-21:30', 'Sunday', 4)
on duplicate key update id=id;