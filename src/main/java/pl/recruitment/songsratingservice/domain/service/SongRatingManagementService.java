package pl.recruitment.songsratingservice.domain.service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import pl.recruitment.songsratingservice.domain.MonthlyAverageSongRatingRepository;
import pl.recruitment.songsratingservice.domain.SongRatingDataProvider;
import pl.recruitment.songsratingservice.domain.SongRatingRepository;
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;
import pl.recruitment.songsratingservice.domain.model.Song;
import pl.recruitment.songsratingservice.domain.model.SongRating;

@RequiredArgsConstructor
public class SongRatingManagementService {

  private final LocalDateService localDateService;
  private final SongRatingService songRatingService;
  private final SongRatingDataProvider dataProvider;
  private final SongRatingRepository ratingRepository;
  private final MonthlyAverageSongRatingRepository monthlyAverageSongRatingRepository;

  @Transactional
  public void performImport() {
    LocalDate importedAt = localDateService.getCurrentDate();
    try (Stream<SongRating> ratings = dataProvider.getRatings()) {
      ratings.forEach(r -> ratingRepository.save(r, importedAt));
    }
  }

  @Transactional
  public void calculateAverages() {
    LocalDate today = localDateService.getCurrentDate();
    LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
    LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
    try (Stream<Song> songs = ratingRepository.findSongsByMonth(firstDayOfMonth, lastDayOfMonth)) {
      songs.forEach(sr -> {
        double avg = songRatingService.getAverageRating(sr.songId(), firstDayOfMonth, lastDayOfMonth);
        monthlyAverageSongRatingRepository.save(MonthlySongAverageRating.builder()
                .songId(sr.songId())
                .songName(sr.songName())
                .avg(avg)
                .period(lastDayOfMonth)
            .build());
      });
    }
  }

}
