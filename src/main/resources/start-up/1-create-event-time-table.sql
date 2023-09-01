CREATE TABLE IF NOT EXISTS game_event_times (
    weekday VARCHAR(10) NOT NULL,
    event_name VARCHAR(20) NOT NULL,
    warn_start_time VARCHAR(5) NOT NULL,
    warn_end_time VARCHAR(5) NOT NULL,
    start_hour TINYINT(2) NOT NULL,
    start_minute TINYINT(2) NOT NULL,
    end_hour TINYINT(2) NOT NULL,
    end_minute TINYINT(2) NOT NULL,
    id INT NOT NULL UNIQUE
);




