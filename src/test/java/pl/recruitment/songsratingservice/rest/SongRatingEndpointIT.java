package pl.recruitment.songsratingservice.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.recruitment.songsratingservice.domain.service.LocalDateService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class SongRatingEndpointIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LocalDateService localDateService;

  @BeforeEach
  void setUp() {
    LocalDate currentDate = LocalDate.of(2024, 1, 15);
    when(localDateService.getCurrentDate()).thenReturn(currentDate);
  }

  @Test
  void shouldReturn404ForNonExistingSong() throws Exception {
    // given
    UUID songId = UUID.randomUUID();
    LocalDate since = LocalDate.now().minusDays(10);
    LocalDate until = LocalDate.now();

    // when
    String url = String.format("/api/%s/avg?since=%s&until=%s", songId, since, until);

    // then
    mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnAverageForSong() throws Exception {
    // given
    UUID songId = UUID.fromString("97e06a5a-5d6d-4676-8a2b-25c5afc01213");
    LocalDate since = LocalDate.of(2024, 1, 1);
    LocalDate until = LocalDate.of(2024, 1, 31);
    double expectedAverage = 3.5;

    // when
    String url = String.format("/api/%s/avg?since=%s&until=%s", songId, since, until);

    // then
    mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.avg").value(expectedAverage));
  }

  @Test
  void shouldReturnEmptyListForNonExistingSong() throws Exception {
    // given
    UUID songId = UUID.randomUUID();

    // when
    String url = String.format("/api/%s/avg-three-months", songId);

    // then
    mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void getAverageLastThreeMonths() throws Exception {
    // given
    UUID songId = UUID.fromString("889d147f-a0c3-465a-a479-13a5a7fed46b");
    double avg202312 = 3.00;
    double avg202311 = 3.50;

    // when
    String url = String.format("/api/%s/avg-three-months", songId);

    // then
    mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].avg").value(avg202311))
        .andExpect(jsonPath("$.[1].avg").value(avg202312));
  }
}
