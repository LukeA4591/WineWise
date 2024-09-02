DROP TABLE IF EXISTS wine;
--SPLIT
CREATE TABLE IF NOT EXISTS wines (
     name TEXT not null primary key,
     type TEXT not null,
     vintage int not null,
     crit_score int,
     user_score int);
--SPLIT
DROP TABLE IF EXISTS winerys;
--SPLIT
CREATE TABLE IF NOT EXISTS winerys (
     name TEXT not null primary key,
     Lon int,
     Lat int,
     region TEXT not null);
--SPLIT