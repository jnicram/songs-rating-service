package pl.recruitment.songsratingservice.infrastructure.persistence;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import pl.recruitment.songsratingservice.domain.SongRatingRepository;
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;
import pl.recruitment.songsratingservice.domain.model.Song;
import pl.recruitment.songsratingservice.domain.model.SongRating;
import pl.recruitment.songsratingservice.domain.model.SongTrend;

@Repository
@RequiredArgsConstructor
class JpaSongRatingRepository implements SongRatingRepository {

  private final JpaSpringSongRatingRepository springSongRatingRepository;
  private final JpaSpringMonthlySongAverageRatingRepository springMonthlySongAverageRatingRepository;

  @Override
  public List<Integer> findRatings(UUID songId, LocalDate since, LocalDate until) {
    return springSongRatingRepository.findRatings(songId, since, until);
  }

  @Override
  public Stream<Song> findSongsByMonth(LocalDate since, LocalDate until) {
    return springSongRatingRepository.findAllByMonth(since, until)
        .map(entity -> Song.builder()
            .songName(entity.getSongName())
            .songId(entity.getSongId())
            .artistName(entity.getArtistName())
            .artistId(entity.getArtistId())
            .build())
        .distinct();
  }

  @Override
  public List<SongTrend> getTrendingSongs(LocalDate thisMonth, LocalDate prevMonth, LocalDate twoMonthsBack,
      int limit) {

    PageRequest pageable = PageRequest.of(0, limit);
    return springMonthlySongAverageRatingRepository.getTrendingSongs(thisMonth, prevMonth, twoMonthsBack, pageable);
  }

  @Override
  public List<MonthlySongAverageRating> findSongAverages(UUID songId, LocalDate since, LocalDate until) {
    return springMonthlySongAverageRatingRepository.findSongAverages(songId, since, until).stream()
        .map(entity -> MonthlySongAverageRating.builder()
            .songId(entity.getSongId())
            .songName(entity.getSongName())
            .avg(entity.getRating().doubleValue())
            .period(entity.getMonthFor())
            .build())
        .toList();
  }

  @Override
  public void save(SongRating songRating, LocalDate importedAt) {
    SongRatingEntity entity = SongRatingEntity.builder()
        .rating(songRating.reviewRating())
        .artistId(songRating.artistId())
        .artistName(songRating.artistName())
        .genre(songRating.genre())
        .songId(songRating.songId())
        .songName(songRating.songName())
        .userId(songRating.userId())
        .importDay(importedAt)
        .build();
    springSongRatingRepository.save(entity);
  }

  @Override
  public Stream<SongTrend> getSongsLoosing(LocalDate thisMonth, LocalDate prevMonth, LocalDate twoMonthsBack,
      double avgDiff) {

    return springMonthlySongAverageRatingRepository.getSongsLoosing(thisMonth, prevMonth, twoMonthsBack, avgDiff);
  }
}
