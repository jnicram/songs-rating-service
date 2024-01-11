package pl.recruitment.songsratingservice.domain.model;

import java.util.UUID;
import lombok.Builder;

@Builder
public record SongRating(String songName, UUID songId, String artistName, UUID artistId, UUID userId, int reviewRating,
                         String genre) {

}
