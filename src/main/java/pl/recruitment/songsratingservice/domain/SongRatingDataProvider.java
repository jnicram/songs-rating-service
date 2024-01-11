package pl.recruitment.songsratingservice.domain;

import java.util.stream.Stream;
import pl.recruitment.songsratingservice.domain.model.SongRating;

public interface SongRatingDataProvider {

  Stream<SongRating> getRatings();
}
