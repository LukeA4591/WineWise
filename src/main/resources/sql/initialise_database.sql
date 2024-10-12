DROP TABLE IF EXISTS wineries;
--SPLIT
CREATE TABLE IF NOT EXISTS wineries (
    wineryName TEXT not null primary key,
    longitude REAL,
    latitude REAL);
--SPLIT
DROP TABLE IF EXISTS wines;
--SPLIT
CREATE TABLE IF NOT EXISTS wines (
    wineID integer not null primary key AUTOINCREMENT,
    type TEXT,
    name TEXT not null,
    winery TEXT not null,
    vintage int not null,
    score INTEGER DEFAULT null,
    region TEXT,
    description TEXT,
    FOREIGN KEY (winery) REFERENCES wineries(wineryName) ON DELETE CASCADE,
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
    FOREIGN KEY (wine) REFERENCES wines ON DELETE CASCADE);

