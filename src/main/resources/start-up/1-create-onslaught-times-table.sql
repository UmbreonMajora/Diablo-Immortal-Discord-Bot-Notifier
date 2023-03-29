-- STORM POINT
CREATE TABLE IF NOT EXISTS onslaught_times
(
    warn_range CHAR(11) NOT NULL,
    warn_day   CHAR(10) NOT NULL,
    id int not null unique
);