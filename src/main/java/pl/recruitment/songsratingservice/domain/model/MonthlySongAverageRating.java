package pl.recruitment.songsratingservice.domain.model;

import java.time.Instant;

public record MonthlySongAverageRating(Instant period, Double avg) {

}
