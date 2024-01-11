package pl.recruitment.songsratingservice.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import pl.recruitment.songsratingservice.domain.model.SongTrend;

interface JpaSpringMonthlySongAverageRatingRepository extends Repository<SongMonthlyRatingEntity, UUID> {

  void save(SongMonthlyRatingEntity entity);

  @Query("SELECT e FROM SongMonthlyRatingEntity e "
      + "WHERE e.songId = :songId AND e.monthFor BETWEEN :since AND :until")
  List<SongMonthlyRatingEntity> findSongAverages(@Param("songId") UUID songId, @Param("since") LocalDate since,
      @Param("until") LocalDate until);

  @Query("SELECT new pl.recruitment.songsratingservice.domain.model.SongTrend("
      + "this_month.songId, this_month.songName,this_month.rating, prev_month.rating, two_months_back.rating)"
      + " FROM SongMonthlyRatingEntity this_month"
      + " LEFT JOIN SongMonthlyRatingEntity prev_month "
      + "   ON prev_month.songId = this_month.songId AND prev_month.monthFor = :prevMonth"
      + " LEFT JOIN SongMonthlyRatingEntity two_months_back "
      + "   ON two_months_back.songId = this_month.songId AND two_months_back.monthFor = :twoMonthsBack"
      + " WHERE this_month.monthFor = :thisMonth"
      + " ORDER BY (this_month.rating - COALESCE(prev_month.rating, 0)) DESC")
  List<SongTrend> getTrendingSongs(@Param("thisMonth") LocalDate thisMonth, @Param("prevMonth") LocalDate prevMonth,
      @Param("twoMonthsBack") LocalDate twoMonthsBack, Pageable pageable);

  @Query("SELECT new pl.recruitment.songsratingservice.domain.model.SongTrend("
      + "this_month.songId, this_month.songName,this_month.rating, prev_month.rating, two_months_back.rating)"
      + " FROM SongMonthlyRatingEntity this_month"
      + " LEFT JOIN SongMonthlyRatingEntity prev_month "
      + "   ON prev_month.songId = this_month.songId AND prev_month.monthFor = :prevMonth"
      + " LEFT JOIN SongMonthlyRatingEntity two_months_back "
      + "   ON two_months_back.songId = this_month.songId AND two_months_back.monthFor = :twoMonthsBack"
      + " WHERE this_month.monthFor = :thisMonth AND (COALESCE(prev_month.rating, 0) - this_month.rating) >= :avgDiff"
      + " ORDER BY (this_month.rating - COALESCE(prev_month.rating, 0)) ASC")
  Stream<SongTrend> getSongsLoosing(@Param("thisMonth") LocalDate thisMonth, @Param("prevMonth") LocalDate prevMonth,
      @Param("twoMonthsBack") LocalDate twoMonthsBack, @Param("avgDiff") double avgDiff);
}
