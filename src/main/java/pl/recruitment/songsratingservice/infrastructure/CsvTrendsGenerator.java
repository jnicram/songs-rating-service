package pl.recruitment.songsratingservice.infrastructure;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pl.recruitment.songsratingservice.domain.TrendsGenerator;
import pl.recruitment.songsratingservice.domain.exception.SystemTechnicalException;
import pl.recruitment.songsratingservice.domain.model.SongTrend;
import pl.recruitment.songsratingservice.domain.service.LocalDateService;
import pl.recruitment.songsratingservice.domain.service.SongRatingService;

@Slf4j
@RequiredArgsConstructor
class CsvTrendsGenerator implements TrendsGenerator {

  private static final String[] CSV_HEADER = { "song_name", "song_uuid", "rating_this_month", "rating_previous_month",
      "rating_2months_back" };

  private final String fileLocation;
  private final String trending100songsFilePattern;
  private final String songsLoosingFilePattern;
  private final SongRatingService songRatingService;
  private final LocalDateService localDateService;

  @Override
  public void generateTrending100songs() {
    String filePath = getFilePath(trending100songsFilePattern);
    List<SongTrend> trending100Songs =  songRatingService.getTrending100Songs();
    try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, UTF_8))) {
      writer.writeNext(CSV_HEADER);
      trending100Songs.stream()
          .map(t -> new String[] {
              t.songName(),
              t.songId().toString(),
              ratingToString(t.ratingThisMonth()),
              ratingToString(t.ratingPrevMonth()),
              ratingToString(t.rating2monthsBack())})
          .forEach(writer::writeNext);
    } catch (IOException e) {
      log.error("Cannot generate trending 100 songs csv!", e);
      throw new SystemTechnicalException(e.getMessage(), e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public void generateSongsLoosing() {
    String filePath = getFilePath(songsLoosingFilePattern);
    try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, UTF_8))) {
      writer.writeNext(CSV_HEADER);
      try (Stream<SongTrend> songsLoosing =  songRatingService.getSongsLoosing()) {
        songsLoosing
            .map(t -> new String[] {
                t.songName(),
                t.songId().toString(),
                ratingToString(t.ratingThisMonth()),
                ratingToString(t.ratingPrevMonth()),
                ratingToString(t.rating2monthsBack())})
            .forEach(writer::writeNext);
      }
    } catch (IOException e) {
      log.error("Cannot generate songs loosing csv!", e);
      throw new SystemTechnicalException(e.getMessage(), e);
    }
  }

  private String getFilePath(String pattern) {
    String currentDate = localDateService.getCurrentDate().format(DateTimeFormatter.ofPattern("yyyyMM"));
    String fileName = pattern.replace("yyyyMM", currentDate);
    return String.format("%s%s", fileLocation, fileName);
  }

  private static String ratingToString(BigDecimal rating) {
    return rating == null ? "" : rating.toString();
  }
}
