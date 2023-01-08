-- ANCIENT ARENA
CREATE TABLE ancient_arena_times
(
    warn_range CHAR(11) NOT NULL,
    warn_day   CHAR(10) NOT NULL
);
INSERT INTO ancient_arena_times (warn_range, warn_day)
VALUES ('21:15-21:30', 'Tuesday');
INSERT INTO ancient_arena_times (warn_range, warn_day)
VALUES ('21:15-21:30', 'Thursday');
INSERT INTO ancient_arena_times (warn_range, warn_day)
VALUES ('21:15-21:30', 'Saturday');
INSERT INTO ancient_arena_times (warn_range, warn_day)
VALUES ('21:15-21:30', 'Friday');