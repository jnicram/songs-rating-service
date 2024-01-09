package pl.recruitment.songsratingservice.domain.model;

import java.util.UUID;

public record SongRating(String songName, UUID songId, String artistName, UUID artistId, UUID userId,
                         double reviewRating, String genre) {

}
