package pl.recruitment.songsratingservice.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@Entity
@Table(name = "song_rating")
class SongRatingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String songName;

  private UUID songId;

  private String artistName;

  private UUID artistId;

  private UUID userId;

  private double rating;

  private String genre;

  @CreatedDate
  private Instant importDate;
}
