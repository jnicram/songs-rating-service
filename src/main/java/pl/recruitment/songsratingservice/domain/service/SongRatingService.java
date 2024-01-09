package pl.recruitment.songsratingservice.domain.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import pl.recruitment.songsratingservice.domain.SongRatingRepository;
import pl.recruitment.songsratingservice.domain.exception.EntityNotFoundException;
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;

@RequiredArgsConstructor
public class SongRatingService {

  private final SongRatingRepository songRatingRepository;

  public double getAverageRating(UUID songId, Instant since, Instant until) {
    List<Double> ratings = songRatingRepository.findRatings(songId, since, until);
    return ratings.stream()
        .mapToDouble(x -> x)
        .average()
        .orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find ratings for song: %s", songId)));
  }

  public List<MonthlySongAverageRating> getAverageLastThreeMonths(UUID songId) {
    List<MonthlySongAverageRating> avgRatings = songRatingRepository.findSongLastThreeMonthsAverage(songId);
    if (avgRatings.isEmpty()) {
      throw new EntityNotFoundException(
          String.format("Cannot find average ratings from last 3 months for song: %s", songId));
    }
    return avgRatings;
  }
}
