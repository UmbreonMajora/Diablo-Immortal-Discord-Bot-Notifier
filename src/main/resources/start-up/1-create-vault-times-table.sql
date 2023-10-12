-- VAULT
CREATE TABLE IF NOT EXISTS vault_times
(
    warn_range CHAR(11) NOT NULL,
    warn_day   CHAR(10) NOT NULL,
    id int not null unique
);