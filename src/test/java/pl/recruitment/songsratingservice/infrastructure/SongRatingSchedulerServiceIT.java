package pl.recruitment.songsratingservice.infrastructure;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.recruitment.songsratingservice.domain.TrendsGenerator;
import pl.recruitment.songsratingservice.domain.service.SongRatingManagementService;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("it")
public class SongRatingSchedulerServiceIT {

  @MockBean
  private SongRatingManagementService songRatingManagementService;

  @MockBean
  private TrendsGenerator trendsGenerator;

  SongRatingSchedulerService songRatingSchedulerService;

  @BeforeEach
  public void setup() {
    songRatingSchedulerService = new SongRatingSchedulerService(songRatingManagementService, trendsGenerator);

  }

  @Test
  public void testImportTuneHeavenRatings() {
    // when
    songRatingSchedulerService.importTuneHeavenRatings();

    // then
    verify(songRatingManagementService, times(1)).performImport();
  }

  @Test
  public void testGenerateSongsTrends() {
    // when
    songRatingSchedulerService.generateSongsTrends();

    // then
    verify(songRatingManagementService, times(1)).calculateAverages();
  }

  @Test
  public void testGenerateSongsTrendsFiles() {
    // when
    songRatingSchedulerService.generateSongsTrendsFiles();

    // then
    verify(trendsGenerator, times(1)).generateTrending100songs();
    verify(trendsGenerator, times(1)).generateSongsLoosing();
  }
}
