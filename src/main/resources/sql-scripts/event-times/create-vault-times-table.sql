-- VAULT
CREATE TABLE vault_times
(
    warn_range CHAR(11) NOT NULL PRIMARY KEY
);
INSERT INTO vault_times (warn_range)
VALUES ('11:45-12:00');
INSERT INTO vault_times (warn_range)
VALUES ('18:45-19:00');