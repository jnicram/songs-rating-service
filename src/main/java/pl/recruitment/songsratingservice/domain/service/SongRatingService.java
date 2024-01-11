package pl.recruitment.songsratingservice.domain.service;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import pl.recruitment.songsratingservice.domain.SongRatingRepository;
import pl.recruitment.songsratingservice.domain.exception.EntityNotFoundException;
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;
import pl.recruitment.songsratingservice.domain.model.SongTrend;

@RequiredArgsConstructor
public class SongRatingService {

  private static final double AVG_DIFF = 0.4;

  private final LocalDateService localDateService;
  private final SongRatingRepository ratingRepository;

  public double getAverageRating(UUID songId, LocalDate since, LocalDate until) {
    List<Integer> ratings = ratingRepository.findRatings(songId, since, until);
    double avg = ratings.stream()
        .mapToDouble(x -> x)
        .average()
        .orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find ratings for song: %s", songId)));
    return BigDecimal.valueOf(avg).setScale(2, HALF_UP).doubleValue();
  }

  public List<MonthlySongAverageRating> getAverageLastThreeMonths(UUID songId) {
    LocalDate currentDate = localDateService.getCurrentDate();
    LocalDate since = currentDate.minusMonths(4).with(TemporalAdjusters.firstDayOfMonth());
    LocalDate until = currentDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    return ratingRepository.findSongAverages(songId, since, until);
  }

  public List<SongTrend> getTrending100Songs() {
    LocalDate today = localDateService.getCurrentDate();
    return ratingRepository.getTrendingSongs(
        today.with(TemporalAdjusters.lastDayOfMonth()),
        today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()),
        today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth()),
        100);
  }

  public Stream<SongTrend> getSongsLoosing() {
    LocalDate today = localDateService.getCurrentDate();
    return ratingRepository.getSongsLoosing(
        today.with(TemporalAdjusters.lastDayOfMonth()),
        today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()),
        today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth()),
        AVG_DIFF);
  }
}
