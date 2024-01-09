package pl.recruitment.songsratingservice.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;

public interface SongRatingRepository {

  List<Double> findRatings(UUID songId, Instant since, Instant until);

  List<MonthlySongAverageRating> findSongLastThreeMonthsAverage(UUID songId);
}
