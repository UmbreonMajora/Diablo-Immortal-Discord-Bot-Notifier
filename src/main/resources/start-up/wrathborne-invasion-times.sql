INSERT INTO wrathborne_invasion_times (warn_range, id)
VALUES
('12:15-12:30', 1),
('20:45-21:00', 2)
on duplicate key update id=id;