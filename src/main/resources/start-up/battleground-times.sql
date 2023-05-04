INSERT INTO battleground_times (warn_range, id)
VALUES
('07:45-08:00', 1),
('11:45-12:00', 2),
('17:45-18:00', 3),
('21:45-22:00', 4)
on duplicate key update id=id;