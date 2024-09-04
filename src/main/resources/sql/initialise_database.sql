DROP TABLE IF EXISTS wine;
--SPLIT
CREATE TABLE IF NOT EXISTS wines (
    type TEXT not null,
    UNIQUE name TEXT not null primary key,
    score int,
    year int,
    region TEXT,
    winery sql_variant,
    description TEXT,
    userRatings TEXT);
--SPLIT
DROP TABLE IF EXISTS winerys;
--SPLIT
CREATE TABLE IF NOT EXISTS winerys (
     name TEXT not null primary key,
     Lon int,
     Lat int,
     region TEXT not null);
--SPLIT