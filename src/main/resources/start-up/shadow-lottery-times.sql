INSERT INTO shadow_lottery_times (warn_range, id)
VALUES
('11:45-12:00', 1),
('17:45-18:00', 2),
('20:45-21:00', 3)
on duplicate key update id=id;