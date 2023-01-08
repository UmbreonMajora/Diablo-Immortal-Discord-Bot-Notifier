-- STORM POINT
CREATE TABLE onslaught_times
(
    warn_range CHAR(11) NOT NULL,
    warn_day   CHAR(10) NOT NULL
);
INSERT INTO onslaught_times (warn_range, warn_day)
VALUES ('11:45-12:00', 'Sunday');
INSERT INTO onslaught_times (warn_range, warn_day)
VALUES ('20:15-20:30', 'Sunday');
INSERT INTO onslaught_times (warn_range, warn_day)
VALUES ('21:45-22:00', 'Sunday');