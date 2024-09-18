DROP TABLE IF EXISTS wines;
--SPLIT
CREATE TABLE IF NOT EXISTS wines (
    wineID integer not null primary key AUTOINCREMENT,
    type TEXT,
    name TEXT not null,
    winery TEXT not null,
    vintage int not null,
    score int,
    region TEXT,
    description TEXT,
    UNIQUE (name, winery, vintage));
--SPLIT
DROP TABLE IF EXISTS reviews;
--SPLIT
CREATE TABLE IF NOT EXISTS reviews (
    reviewID integer not null primary key AUTOINCREMENT,
    wine integer,
    rating integer,
    description TEXT,
    reported BOOLEAN default false,
    FOREIGN KEY (wine) REFERENCES wines);

