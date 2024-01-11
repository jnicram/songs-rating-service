package pl.recruitment.songsratingservice.rest;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating;

record MonthAverageDto(@DateTimeFormat(pattern = "yyyyMM") LocalDate month, double avg) {

  public static MonthAverageDto from(MonthlySongAverageRating model) {
    return new MonthAverageDto(model.period(), model.avg());
  }
}
