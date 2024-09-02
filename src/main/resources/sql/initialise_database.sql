DROP TABLE IF EXISTS wine;
--SPLIT
CREATE TABLE IF NOT EXISTS wine (
     name varchar() not null primary key,
     type varchar() not null,

    );
--SPLIT
DROP TABLE IF EXISTS users;
--SPLIT
CREATE TABLE IF NOT EXISTS users (
     id INTEGER PRIMARY KEY AUTOINCREMENT,
     username TEXT UNIQUE,
     password TEXT);
--SPLIT
INSERT INTO users (username, password) VALUES ('admin', 'admin');