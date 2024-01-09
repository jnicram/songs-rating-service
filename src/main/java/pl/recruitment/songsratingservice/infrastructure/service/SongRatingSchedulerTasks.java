package pl.recruitment.songsratingservice.infrastructure.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import pl.recruitment.songsratingservice.domain.service.SongRatingService;

@Slf4j
@RequiredArgsConstructor
class SongRatingSchedulerTasks {

  private final SongRatingService songRatingService;

  @Scheduled(cron = "${cron-jobs.daily-import}")
  @SchedulerLock(name = "songRatingSchedulerTasks")
  void importTuneHeavenRatings() {
    log.info("Start daily song ratings import.");
  }

  @Scheduled(cron = "${cron-jobs.monthly-generation}")
  @SchedulerLock(name = "songRatingSchedulerTasks")
  void generateSongsTrends() {
    log.info("Last day of month. Generate songs trends.");
    generateTrending100songs();
    generateSongsLoosing();
  }

  private void generateSongsLoosing() {

  }

  private void generateTrending100songs() {

  }
}
