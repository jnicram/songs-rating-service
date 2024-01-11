package pl.recruitment.songsratingservice.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import pl.recruitment.songsratingservice.domain.TrendsGenerator;
import pl.recruitment.songsratingservice.domain.service.SongRatingManagementService;

@Slf4j
@RequiredArgsConstructor
class SongRatingSchedulerService {

  private final SongRatingManagementService songRatingManagementService;
  private final TrendsGenerator trendsGenerator;

  @Scheduled(cron = "${cron.daily-import.expression}")
  @SchedulerLock(name = "songRatingSchedulerTasks")
  void importTuneHeavenRatings() {
    log.info("[START] Daily song ratings import.");
    songRatingManagementService.performImport();
    log.info("[END] Daily song ratings import.");
  }

  @Scheduled(cron = "${cron.monthly-generation.expression}")
  @SchedulerLock(name = "songRatingSchedulerTasks")
  void generateSongsTrends() {
    log.info("[START] Generate songs trends.");
    songRatingManagementService.calculateAverages();
    log.info("[END] Generate songs trends.");
  }

  @Scheduled(cron = "${cron.monthly-file.expression}")
  @SchedulerLock(name = "songRatingSchedulerTasks")
  void generateSongsTrendsFiles() {
    log.info("[START] Generate songs trends files.");
    trendsGenerator.generateTrending100songs();
    trendsGenerator.generateSongsLoosing();
    log.info("[END] Generate songs trends files.");
  }
}
