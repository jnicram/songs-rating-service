package pl.recruitment.songsratingservice.infrastructure

import java.time.LocalDate
import pl.recruitment.songsratingservice.domain.exception.SystemTechnicalException
import pl.recruitment.songsratingservice.domain.model.SongRating
import pl.recruitment.songsratingservice.domain.service.LocalDateService
import spock.lang.Specification
import spock.lang.Subject

class TuneHeavenRatingProviderTest extends Specification {

  @Subject
  def tuneHeavenRatingProvider

  def localDateService = Mock(LocalDateService)

  def setup() {
    localDateService.getCurrentDate() >> LocalDate.of(2024, 1, 15)
  }

  def "getRatings expected behavior"() {
    given:
    tuneHeavenRatingProvider = new TuneHeavenRatingProvider("src/test/resources/", "importFile.csv", localDateService)

    when:
    def result = tuneHeavenRatingProvider.getRatings().toList()

    then:
    result.size() == 1
    result == [
        new SongRating("song name",
            UUID.fromString("4837498-ae56-4b78-acf4-0e4311860945"),
            "artist name",
            UUID.fromString("ef784938-ae56-4b78-acf4-0e4311860945"),
            UUID.fromString("4837498-ae56-4b78-acf4-0e4311860945"),
            4,
            "rock")
    ]
  }

  def "getRatings should throws SystemTechnicalException"() {
    given:
    tuneHeavenRatingProvider = new TuneHeavenRatingProvider("src/test/resources/", "nonExisting.csv", localDateService)

    when:
    tuneHeavenRatingProvider.getRatings()

    then:
    thrown(SystemTechnicalException)
  }
}
