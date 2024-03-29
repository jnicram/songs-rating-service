package pl.recruitment.songsratingservice.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

  private int rating;

  @Column(length = 50)
  private String genre;

  private LocalDate importDay;
}
