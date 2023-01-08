-- ASSEMBLY
CREATE TABLE assembly_times
(
    warn_range CHAR(11) NOT NULL,
    warn_day   CHAR(10) NOT NULL
);
INSERT INTO assembly_times (warn_range, warn_day)
VALUES ('18:45-19:00', 'Monday');
INSERT INTO assembly_times (warn_range, warn_day)
VALUES ('18:45-19:00', 'Tuesday');
INSERT INTO assembly_times (warn_range, warn_day)
VALUES ('18:45-19:00', 'Wednesday');
INSERT INTO assembly_times (warn_range, warn_day)
VALUES ('18:45-19:00', 'Thursday');
INSERT INTO assembly_times (warn_range, warn_day)
VALUES ('18:45-19:00', 'Friday');
INSERT INTO assembly_times (warn_range, warn_day)
VALUES ('18:45-19:00', 'Saturday');