CREATE TABLE song_rating
(
    id          UUID NOT NULL PRIMARY KEY,
    song_id     UUID,
    song_name   VARCHAR(255),
    artist_id   UUID,
    artist_name VARCHAR(255),
    user_id     UUID,
    rating      DOUBLE PRECISION NOT NULL,
    genre       VARCHAR(255),
    import_date TIMESTAMP(6) WITH TIME ZONE
);
