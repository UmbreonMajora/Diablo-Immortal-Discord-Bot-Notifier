-- SHADOW LOTTERY
CREATE TABLE shadow_lottery_times
(
    warn_range CHAR(11) NOT NULL PRIMARY KEY
);
INSERT INTO shadow_lottery_times (warn_range)
VALUES ('11:45-12:00');
INSERT INTO shadow_lottery_times (warn_range)
VALUES ('17:45-18:00');
INSERT INTO shadow_lottery_times (warn_range)
VALUES ('20:45-21:00');