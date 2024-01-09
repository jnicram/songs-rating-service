package pl.recruitment.songsratingservice.infrastructure.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.recruitment.songsratingservice.domain.SongRatingRepository;
import pl.recruitment.songsratingservice.domain.service.SongRatingService;

@EnableScheduling
@Configuration
class ServiceConfiguration {

  @Bean
  SongRatingService songRatingService(SongRatingRepository songRatingRepository) {
    return new SongRatingService(songRatingRepository);
  }

  @Bean
  SongRatingSchedulerTasks importSongRatingService(SongRatingService songRatingService) {
    return new SongRatingSchedulerTasks(songRatingService);
  }
}
