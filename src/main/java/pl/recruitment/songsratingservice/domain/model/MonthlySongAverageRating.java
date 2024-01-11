package pl.recruitment.songsratingservice.domain.model;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MonthlySongAverageRating(String songName, UUID songId, LocalDate period, Double avg) {

}
