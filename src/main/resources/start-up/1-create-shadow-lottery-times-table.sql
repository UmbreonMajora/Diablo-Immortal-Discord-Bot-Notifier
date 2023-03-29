-- SHADOW LOTTERY
CREATE TABLE IF NOT EXISTS shadow_lottery_times
(
    warn_range CHAR(11) NOT NULL,
    id int not null unique
);