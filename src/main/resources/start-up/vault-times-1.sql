INSERT INTO vault_times (warn_range, id)
VALUES ('11:45-12:00', 1) on duplicate key update id=id;