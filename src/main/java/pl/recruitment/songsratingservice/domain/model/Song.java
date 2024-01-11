package pl.recruitment.songsratingservice.domain.model;

import java.util.UUID;
import lombok.Builder;

@Builder
public record Song(String songName, UUID songId, String artistName, UUID artistId) {

}
