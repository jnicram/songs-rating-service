package pl.recruitment.songsratingservice.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.recruitment.songsratingservice.domain.service.SongRatingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
class SongRatingEndpoint {

  private final SongRatingService songRatingService;

  @GetMapping("/{songId}/avg")
  public ResponseEntity<AverageDto> getAverageRating(@PathVariable UUID songId,
      @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate since,
      @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate until) {

    double avg = songRatingService.getAverageRating(songId, since, until);
    return ResponseEntity.ok(new AverageDto(avg));
  }

  @GetMapping("/{songId}/avg-three-months")
  public ResponseEntity<List<MonthAverageDto>> getAverageLastThreeMonths(@PathVariable UUID songId) {
    List<MonthAverageDto> monthlyAvg = songRatingService.getAverageLastThreeMonths(songId).stream()
        .map(MonthAverageDto::from)
        .toList();
    return ResponseEntity.ok(monthlyAvg);
  }
}
