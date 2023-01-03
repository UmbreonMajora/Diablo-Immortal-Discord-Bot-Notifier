-- ANCIENT NIGHTMARE
CREATE TABLE ancient_nightmare_times
(
    warn_range CHAR(11) NOT NULL,
    warn_day   CHAR(10) NOT NULL
);
INSERT INTO ancient_nightmare_times (warn_range, warn_day)
VALUES ('11:45-12:00', 'Wednesday');
INSERT INTO ancient_nightmare_times (warn_range, warn_day)
VALUES ('20:15-20:30', 'Wednesday');
INSERT INTO ancient_nightmare_times (warn_range, warn_day)
VALUES ('21:45-22:00', 'Wednesday');
INSERT INTO ancient_nightmare_times (warn_range, warn_day)
VALUES ('11:45-12:00', 'Friday');
INSERT INTO ancient_nightmare_times (warn_range, warn_day)
VALUES ('20:15-20:30', 'Friday');
INSERT INTO ancient_nightmare_times (warn_range, warn_day)
VALUES ('21:45-22:00', 'Friday');