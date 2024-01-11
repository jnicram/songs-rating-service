package pl.recruitment.songsratingservice.infrastructure.persistence;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.recruitment.songsratingservice.domain.MonthlyAverageSongRatingRepository;
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;

@Repository
@RequiredArgsConstructor
class JpaSongMonthlySongRatingRepository implements MonthlyAverageSongRatingRepository {

  private final JpaSpringMonthlySongAverageRatingRepository monthlySongAverageRatingRepository;

  @Override
  public void save(MonthlySongAverageRating songAverageRating) {
    SongMonthlyRatingEntity entity = SongMonthlyRatingEntity.builder()
        .songId(songAverageRating.songId())
        .songName(songAverageRating.songName())
        .rating(BigDecimal.valueOf(songAverageRating.avg()))
        .monthFor(songAverageRating.period())
        .build();
    monthlySongAverageRatingRepository.save(entity);
  }
}
