DROP TABLE IF EXISTS FILM_LIKES;
DROP TABLE IF EXISTS FILM_GENRES;
DROP TABLE IF EXISTS FILMS;
DROP TABLE IF EXISTS MPA_RATINGS;
DROP TABLE IF EXISTS GENRES;
DROP TABLE IF EXISTS FRIENDSHIP;
DROP TABLE IF EXISTS USERS;

CREATE TABLE IF NOT EXISTS USERS (
    USER_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    EMAIL VARCHAR(255) NOT NULL UNIQUE,
    LOGIN VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    BIRTHDAY DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP (
    USER_ID BIGINT NOT NULL REFERENCES USERS (USER_ID),
    FRIEND_ID BIGINT NOT NULL REFERENCES USERS (USER_ID)
);

CREATE TABLE IF NOT EXISTS GENRES (
    GENRE_ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS MPA_RATINGS (
    MPA_ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS FILMS (
    FILM_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(255) NOT NULL,
    RELEASE_DATE DATE NOT NULL,
    DURATION INT NOT NULL,
    MPA_ID INT NOT NULL REFERENCES MPA_RATINGS (MPA_ID)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES (
    FILM_ID BIGINT NOT NULL REFERENCES FILMS (FILM_ID),
    GENRE_ID INT NOT NULL REFERENCES GENRES (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS FILM_LIKES (
    FILM_ID BIGINT NOT NULL REFERENCES FILMS (FILM_ID),
    LIKE_ID BIGINT NOT NULL REFERENCES USERS (USER_ID)
);