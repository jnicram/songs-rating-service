package pl.recruitment.songsratingservice.infrastructure;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import pl.recruitment.songsratingservice.domain.SongRatingDataProvider;
import pl.recruitment.songsratingservice.domain.exception.SystemTechnicalException;
import pl.recruitment.songsratingservice.domain.model.SongRating;
import pl.recruitment.songsratingservice.domain.service.LocalDateService;

@Slf4j
@RequiredArgsConstructor
class TuneHeavenRatingProvider implements SongRatingDataProvider {

  private final String fileLocation;
  private final String fileNamePattern;
  private final LocalDateService localDateService;

  @Override
  public Stream<SongRating> getRatings() {
    FileSystemResource fileSystemResource = new FileSystemResource(getImportFilePath());
    try (CSVReader csvReader = new CSVReaderBuilder(
        new InputStreamReader(fileSystemResource.getInputStream(), UTF_8)).withSkipLines(1).build()) {
      return csvReader.readAll().stream()
          .map(data -> new SongRating(data[0], UUID.fromString(data[1]), data[2], UUID.fromString(data[3]),
              UUID.fromString(data[4]), Integer.parseInt(data[5]), data[6]))
          .toList().stream();
    } catch (IOException | CsvException e) {
      log.error("Cannot import songs ratings from TuneHeaven service!", e);
      throw new SystemTechnicalException(e.getMessage(), e);
    }
  }

  private String getImportFilePath() {
    String currentDate = localDateService.getCurrentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String fileName = fileNamePattern.replace("yyyy-MM-dd", currentDate);
    return String.format("%s%s", fileLocation, fileName);
  }
}
