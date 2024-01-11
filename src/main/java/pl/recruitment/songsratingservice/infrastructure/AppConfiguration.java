package pl.recruitment.songsratingservice.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.recruitment.songsratingservice.domain.MonthlyAverageSongRatingRepository;
import pl.recruitment.songsratingservice.domain.SongRatingDataProvider;
import pl.recruitment.songsratingservice.domain.SongRatingRepository;
import pl.recruitment.songsratingservice.domain.TrendsGenerator;
import pl.recruitment.songsratingservice.domain.service.LocalDateService;
import pl.recruitment.songsratingservice.domain.service.SongRatingManagementService;
import pl.recruitment.songsratingservice.domain.service.SongRatingService;

@Configuration
class AppConfiguration {

  @Bean
  LocalDateService localDateService() {
    return new LocalDateService();
  }

  @Bean
  SongRatingService songRatingService(SongRatingRepository songRatingRepository, LocalDateService localDateService) {
    return new SongRatingService(localDateService, songRatingRepository);
  }

  @Bean
  SongRatingDataProvider songRatingDataProvider(LocalDateService localDateService,
      @Value("${cron.daily-import.file-location}") String fileLocation,
      @Value("${cron.daily-import.file-pattern}") String fileNamePattern) {

    return new TuneHeavenRatingProvider(fileLocation, fileNamePattern, localDateService);
  }

  @Bean
  SongRatingManagementService songRatingImportService(SongRatingService songRatingService,
      SongRatingDataProvider songRatingDataProvider, SongRatingRepository songRatingRepository,
      MonthlyAverageSongRatingRepository monthlyAverageSongRatingRepository,
      LocalDateService localDateService) {

    return new SongRatingManagementService(localDateService, songRatingService, songRatingDataProvider,
        songRatingRepository, monthlyAverageSongRatingRepository);
  }

  @Bean
  TrendsGenerator trendsGenerator(LocalDateService localDateService,
      @Value("${cron.monthly-file.file-location}") String fileLocation,
      @Value("${cron.monthly-file.trending-100-songs-file-pattern}") String trending100songsFilePattern,
      @Value("${cron.monthly-file.songs-loosing-file-pattern}") String songsLoosingFilePattern,
      SongRatingService songRatingService) {

    return new CsvTrendsGenerator(fileLocation, trending100songsFilePattern, songsLoosingFilePattern,
        songRatingService, localDateService);
  }

  @Bean
  SongRatingSchedulerService importSongRatingService(SongRatingManagementService songRatingImportService,
      TrendsGenerator trendsGenerator) {

    return new SongRatingSchedulerService(songRatingImportService, trendsGenerator);
  }

}
