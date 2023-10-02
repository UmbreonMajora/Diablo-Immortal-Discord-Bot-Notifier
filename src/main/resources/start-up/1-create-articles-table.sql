CREATE TABLE IF NOT EXISTS articles
(
    id int not null primary key,
    url TEXT NOT NULL,
    category VARCHAR(50) NOT NULL
)