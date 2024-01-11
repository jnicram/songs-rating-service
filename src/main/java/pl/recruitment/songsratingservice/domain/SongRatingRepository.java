package pl.recruitment.songsratingservice.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;
import pl.recruitment.songsratingservice.domain.model.Song;
import pl.recruitment.songsratingservice.domain.model.SongRating;
import pl.recruitment.songsratingservice.domain.model.SongTrend;

public interface SongRatingRepository {

  List<Integer> findRatings(UUID songId, LocalDate since, LocalDate until);

  List<MonthlySongAverageRating> findSongAverages(UUID songId, LocalDate since, LocalDate until);

  Stream<Song> findSongsByMonth(LocalDate since, LocalDate until);

  List<SongTrend> getTrendingSongs(LocalDate thisMonth, LocalDate prevMonth, LocalDate twoMonthsBack, int limit);

  void save(SongRating songRating, LocalDate importedAt);

  Stream<SongTrend> getSongsLoosing(LocalDate with, LocalDate with1, LocalDate with2, double avgDiff);
}
