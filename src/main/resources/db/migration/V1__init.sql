CREATE TABLE song_rating
(
    id          UUID NOT NULL PRIMARY KEY,
    song_id     UUID,
    song_name   VARCHAR(255),
    artist_id   UUID,
    artist_name VARCHAR(255),
    user_id     UUID,
    rating      INTEGER NOT NULL,
    genre       VARCHAR(50),
    import_day  DATE NOT NULL,
    CONSTRAINT song_rating_uk UNIQUE (song_id, user_id, import_day)
);

CREATE TABLE song_monthly_rating
(
    id         UUID NOT NULL PRIMARY KEY,
    song_id    UUID,
    song_name  VARCHAR(255),
    rating     DECIMAL(3, 2) NOT NULL,
    month_for  DATE NOT NULL,
    CONSTRAINT song_monthly_rating_uk UNIQUE (song_id, month_for)
);
