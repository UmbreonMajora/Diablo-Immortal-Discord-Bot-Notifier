-- VAULT
CREATE TABLE IF NOT EXISTS vault_times
(
    warn_range CHAR(11) NOT NULL,
    id int not null unique
);