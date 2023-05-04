INSERT INTO vault_times (warn_range, id)
VALUES
('11:45-12:00', 1),
('18:45-19:00', 2)
on duplicate key update id=id;