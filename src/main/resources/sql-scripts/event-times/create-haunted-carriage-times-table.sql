-- HAUNTED CARRIAGE
CREATE TABLE haunted_carriage_times
(
    warn_range CHAR(11) NOT NULL,
    warn_day   CHAR(10) NOT NULL
);
INSERT INTO haunted_carriage_times (warn_range, warn_day)
VALUES ('11:45-12:00', 'Tuesday');
INSERT INTO haunted_carriage_times (warn_range, warn_day)
VALUES ('20:15-20:30', 'Tuesday');
INSERT INTO haunted_carriage_times (warn_range, warn_day)
VALUES ('21:45-22:00', 'Tuesday');
INSERT INTO haunted_carriage_times (warn_range, warn_day)
VALUES ('11:45-12:00', 'Saturday');
INSERT INTO haunted_carriage_times (warn_range, warn_day)
VALUES ('20:15-20:30', 'Saturday');
INSERT INTO haunted_carriage_times (warn_range, warn_day)
VALUES ('21:45-22:00', 'Saturday');