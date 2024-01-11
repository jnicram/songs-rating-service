package pl.recruitment.songsratingservice.domain;

import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;

public interface MonthlyAverageSongRatingRepository {

  void save(MonthlySongAverageRating songAverageRating);
}
