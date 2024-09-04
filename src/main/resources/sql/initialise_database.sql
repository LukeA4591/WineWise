DROP TABLE IF EXISTS wines;
--SPLIT
CREATE TABLE IF NOT EXISTS wines (
    type TEXT,
    name TEXT not null,
    winery TEXT not null,
    vintage int not null,
    primary key (name, winery, vintage),
    score int,
    region TEXT,
    description TEXT);
--SPLIT
DROP TABLE IF EXISTS winerys;
--SPLIT
CREATE TABLE IF NOT EXISTS winerys (
     name TEXT not null primary key,
     Lon int,
     Lat int,
     region TEXT not null);
--SPLIT
