package pl.recruitment.songsratingservice.infrastructure.persistence;


import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.recruitment.songsratingservice.domain.SongRatingRepository;
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;

@Repository
@RequiredArgsConstructor
class JpaSongRatingRepository implements SongRatingRepository {

  private final JpaSpringSongRatingRepository springSongRatingRepository;

  @Override
  public List<Double> findRatings(UUID songId, Instant since, Instant until) {
    return null;
  }

  @Override
  public List<MonthlySongAverageRating> findSongLastThreeMonthsAverage(UUID songId) {
    return null;
  }
}
