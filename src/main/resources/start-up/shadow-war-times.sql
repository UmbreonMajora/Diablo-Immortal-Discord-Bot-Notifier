INSERT INTO
    shadow_war_times (warn_range, warn_day, id)
VALUES
    ('19:15-19:30', 'Thursday', 1),
    ('19:15-19:30', 'Saturday', 2)
on duplicate key update id=id;