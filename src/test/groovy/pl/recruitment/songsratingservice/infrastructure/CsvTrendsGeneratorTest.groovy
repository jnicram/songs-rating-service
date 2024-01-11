package pl.recruitment.songsratingservice.infrastructure

import java.time.LocalDate
import pl.recruitment.songsratingservice.domain.model.SongTrend
import pl.recruitment.songsratingservice.domain.service.LocalDateService
import pl.recruitment.songsratingservice.domain.service.SongRatingService
import spock.lang.Specification
import spock.lang.Subject

class CsvTrendsGeneratorTest extends Specification {

  @Subject
  def trendsGenerator

  def localDateService = Mock(LocalDateService)
  def songRatingService = Mock(SongRatingService)
  def trendingSongsFile = new File("/tmp/trends.csv")
  def songsLoosingFile = new File("/tmp/songsLoosing.csv")

  def setup() {
    localDateService.getCurrentDate() >> LocalDate.of(2024, 1, 15)

    trendsGenerator = new CsvTrendsGenerator(
        "/tmp/",
        "trends.csv",
        "songsLoosing.csv",
        songRatingService,
        localDateService)
  }

  def cleanup() {
    trendingSongsFile.delete()
    songsLoosingFile.delete()
  }

  def "should generate trending100 songs"() {
    given: "trending 100 songs"
    def trending100Songs = []
    trending100Songs << new SongTrend(UUID.randomUUID(), "Song 1", new BigDecimal("3.5"), new BigDecimal("3.0"),
        new BigDecimal("2.5"))
    trending100Songs << new SongTrend(UUID.randomUUID(), "Song 2", new BigDecimal("4.5"), new BigDecimal("4.0"),
        new BigDecimal("3.5"))

    and: "trending 100 songs are returned by song rating service"
    songRatingService.getTrending100Songs() >> trending100Songs

    when: "generate trending 100 songs"
    trendsGenerator.generateTrending100songs()

    then: "trending 100 songs are written to csv"
    trendingSongsFile.exists()
    trendingSongsFile.text.contains("\"song_name\",\"song_uuid\",\"rating_this_month\",\"rating_previous_month\",\"rating_2months_back\"")
    trendingSongsFile.text.contains("Song 1")
    trendingSongsFile.text.contains("Song 2")
  }

  def "should generate loosing songs"() {
    given: "loosing songs"
    def loosingSongs = []
    loosingSongs << new SongTrend(UUID.randomUUID(), "Song 1", new BigDecimal("3.5"), new BigDecimal("3.0"),
        new BigDecimal("2.5"))
    loosingSongs << new SongTrend(UUID.randomUUID(), "Song 2", new BigDecimal("4.5"), new BigDecimal("4.0"),
        new BigDecimal("3.5"))

    and: "lossing songs are returned by song rating service"
    songRatingService.getSongsLoosing() >> loosingSongs.stream()

    when: "generate trending 100 songs"
    trendsGenerator.generateSongsLoosing()

    then: "loosing songs are written to csv"
    songsLoosingFile.exists()
    songsLoosingFile.text.contains("\"song_name\",\"song_uuid\",\"rating_this_month\",\"rating_previous_month\",\"rating_2months_back\"")
    songsLoosingFile.text.contains("Song 1")
    songsLoosingFile.text.contains("Song 2")
  }
}
