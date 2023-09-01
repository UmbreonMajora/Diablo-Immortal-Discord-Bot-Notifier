INSERT INTO game_event_times
    (weekday, event_name, warn_start_time, warn_end_time, start_hour, start_minute, end_hour, end_minute, id)
VALUES
    ('tuesday', 'ancientarena', '21:15', '21:30', 21, 30, 21, 40, 1),
    ('thursday', 'ancientarena', '21:15', '21:30', 21, 30, 21, 40, 2),
    ('saturday', 'ancientarena', '21:15', '21:30', 21, 30, 21, 40, 3),
    ('sunday', 'ancientarena', '21:15', '21:30', 21, 30, 21, 40, 4),

    ('tuesday', 'hauntedcarriage', '11:45', '12:00', 12, 00, 12, 30, 5),
    ('tuesday', 'hauntedcarriage', '20:15', '20:30', 20, 30, 21, 00, 6),
    ('tuesday', 'hauntedcarriage', '21:45', '22:00', 22, 00, 22, 30, 7),
    ('saturday', 'hauntedcarriage', '11:45', '12:00', 12, 00, 12, 30, 8),
    ('saturday', 'hauntedcarriage', '20:15', '20:30', 20, 30, 21, 00, 9),
    ('saturday', 'hauntedcarriage', '21:45', '22:00', 22, 00, 22, 30, 10),

    ('thursday', 'towerofvictory', '19:45', '20:00', 20, 00, 21, 30, 11),
    ('saturday', 'towerofvictory', '19:45', '20:00', 20, 00, 21, 30, 12),

    ('everyday', 'wrathborneinvasion', '12:15', '12:30', 12, 30, 13, 00, 13),
    ('everyday', 'wrathborneinvasion', '20:45', '21:00', 21, 00, 21, 30, 14),

    ('thursday', 'shadowwar', '19:15', '19:30', 19, 30, 20, 00, 15),

    ('everyday', 'vault', '11:45', '12:00', 12, 00, 14, 00, 16),
    ('everyday', 'vault', '18:45', '19:00', 19, 00, 21, 00, 17),

    ('everyday', 'battlegrounds', '07:45', '08:00', 08, 00, 08, 30, 18),
    ('everyday', 'battlegrounds', '11:45', '12:00', 12, 00, 12, 30, 19),
    ('everyday', 'battlegrounds', '17:45', '18:00', 18, 00, 18, 30, 20),
    ('everyday', 'battlegrounds', '21:45', '22:00', 22, 00, 22, 30, 21),

    ('everyday', 'shadowlottery', '11:45', '12:00', 12, 00, 12, 30, 22),
    ('everyday', 'shadowlottery', '17:45', '18:00', 18, 00, 18, 30, 23),
    ('everyday', 'shadowlottery', '20:45', '21:00', 21, 00, 21, 30, 24),

    ('everyday', 'onslaught', '11:45', '12:00', 12, 00, 12, 15, 25),
    ('everyday', 'onslaught', '20:15', '20:30', 20, 30, 20, 45, 26),
    ('everyday', 'onslaught', '21:45', '22:00', 22, 00, 22, 15, 27),

    ('wednesday', 'ancientnightmare', '11:45', '12:00', 12, 00, 12, 15, 28),
    ('wednesday', 'ancientnightmare', '20:15', '20:30', 20, 30, 20, 45, 29),
    ('wednesday', 'ancientnightmare', '21:45', '21:00', 21, 00, 21, 15, 30),
    ('friday', 'ancientnightmare', '11:45', '12:00', 12, 00, 12, 15, 31),
    ('friday', 'ancientnightmare', '20:15', '20:30', 20, 30, 20, 45, 32),
    ('friday', 'ancientnightmare', '21:45', '21:00', 21, 00, 21, 15, 33),

    ('monday', 'assembly', '18:45', '19:00', 19, 00, 21, 00, 34),
    ('tuesday', 'assembly', '18:45', '19:00', 19, 00, 21, 00, 35),
    ('wednesday', 'assembly', '18:45', '19:00', 19, 00, 21, 00, 36),
    ('thursday', 'assembly', '18:45', '19:00', 19, 00, 21, 00, 37),
    ('friday', 'assembly', '18:45', '19:00', 19, 00, 21, 00, 38),
    ('saturday', 'assembly', '18:45', '19:00', 19, 00, 21, 00, 39),

    ('monday', 'demongates', '11:45', '12:00', 12, 00, 12, 30, 40),
    ('monday', 'demongates', '20:15', '20:30', 20, 30, 21, 00, 41),
    ('monday', 'demongates', '21:45', '22:00', 22, 00, 22, 30, 42),
    ('thursday', 'demongates', '11:45', '12:00', 12, 00, 12, 30, 43),
    ('thursday', 'demongates', '20:15', '20:30', 20, 30, 21, 00, 44),
    ('thursday', 'demongates', '21:45', '22:00', 22, 00, 22, 30, 45),
    ('sunday', 'demongates', '11:45', '12:00', 12, 00, 12, 30, 46),
    ('sunday', 'demongates', '20:15', '20:30', 20, 30, 21, 00, 47),
    ('sunday', 'demongates', '21:45', '22:00', 22, 00, 22, 30, 48)
on duplicate key update id=id;
