package pl.recruitment.songsratingservice.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

interface JpaSpringSongRatingRepository extends Repository<SongRatingEntity, UUID> {

  void save(SongRatingEntity entity);

  @Query("SELECT sre.rating FROM SongRatingEntity sre "
      + "WHERE sre.songId = :songId AND sre.importDay BETWEEN :since AND :until")
  List<Integer> findRatings(@Param("songId") UUID songId, @Param("since") LocalDate since,
      @Param("until") LocalDate until);

  @Query("SELECT sre FROM SongRatingEntity sre WHERE sre.importDay BETWEEN :since AND :until")
  Stream<SongRatingEntity> findAllByMonth(@Param("since") LocalDate since, @Param("until") LocalDate until);
}
