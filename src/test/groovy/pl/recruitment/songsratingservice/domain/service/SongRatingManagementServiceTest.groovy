package pl.recruitment.songsratingservice.domain.service

import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.stream.Stream
import pl.recruitment.songsratingservice.domain.MonthlyAverageSongRatingRepository
import pl.recruitment.songsratingservice.domain.SongRatingDataProvider
import pl.recruitment.songsratingservice.domain.SongRatingRepository
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating
import pl.recruitment.songsratingservice.domain.model.Song
import pl.recruitment.songsratingservice.domain.model.SongRating
import spock.lang.Specification
import spock.lang.Subject

class SongRatingManagementServiceTest extends Specification {

  @Subject
  def songRatingManagementService

  def songRatingService = Mock(SongRatingService)
  def dataProvider = Mock(SongRatingDataProvider)
  def ratingRepository = Mock(SongRatingRepository)
  def monthlyAverageSongRatingRepository = Mock(MonthlyAverageSongRatingRepository)
  def localDateService = Mock(LocalDateService)

  def today = LocalDate.now()

  def setup() {
    localDateService.getCurrentDate() >> today
    songRatingManagementService = new SongRatingManagementService(
        localDateService,
        songRatingService,
        dataProvider,
        ratingRepository,
        monthlyAverageSongRatingRepository
    )
  }

  def "performImport should import all ratings from dataProvider and save them in repository"() {
    given:
    def songRating1 = new SongRating("song1", UUID.randomUUID(), "artist1", UUID.randomUUID(), UUID.randomUUID(), 5, "Pop")
    def songRating2 = new SongRating("song2", UUID.randomUUID(), "artist2", UUID.randomUUID(), UUID.randomUUID(), 3, "Rock")
    def ratings = Stream.of(songRating1, songRating2)
    dataProvider.getRatings() >> ratings

    when: "invoking method"
    songRatingManagementService.performImport()

    then: "ensure save method on ratingRepository is called for each SongRating"
    1 * ratingRepository.save(songRating1, today)
    1 * ratingRepository.save(songRating2, today)
  }

  def "calculateAverages should save song average in repository"() {
    given:
    def songId = UUID.randomUUID()
    def today = today
    def firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth())
    def lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth())
    def avg = 4.5
    def song = new Song("name", songId, "artistName", UUID.randomUUID())
    def songs = Stream.of(song)

    ratingRepository.findSongsByMonth(firstDayOfMonth, lastDayOfMonth) >> songs
    songRatingService.getAverageRating(songId, firstDayOfMonth, lastDayOfMonth) >> avg

    when:
    songRatingManagementService.calculateAverages()

    then:
    1 * monthlyAverageSongRatingRepository.save(
        MonthlySongAverageRating.builder()
            .songId(song.songId())
            .songName(song.songName())
            .avg(avg)
            .period(lastDayOfMonth)
            .build())
  }
}
