package pl.recruitment.songsratingservice.domain.service

import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import pl.recruitment.songsratingservice.domain.SongRatingRepository
import pl.recruitment.songsratingservice.domain.exception.EntityNotFoundException
import pl.recruitment.songsratingservice.domain.model.MonthlySongAverageRating
import pl.recruitment.songsratingservice.domain.model.SongTrend
import spock.lang.Specification
import spock.lang.Subject

class SongRatingServiceTest extends Specification {

  @Subject
  def songRatingService

  def songRatingRepository = Mock(SongRatingRepository)
  def localDateService = Mock(LocalDateService)

  def today = LocalDate.now()

  def setup() {
    localDateService.getCurrentDate() >> today
    songRatingService = new SongRatingService(localDateService, songRatingRepository)
  }

  def "should calculated average rating for a song correctly"() {
    given:
    def songId = UUID.randomUUID()
    def since = LocalDate.of(2023, 1, 1)
    def until = LocalDate.of(2023, 1, 31)
    def ratings = [5, 4, 3, 5, 5]
    songRatingRepository.findRatings(songId, since, until) >> ratings

    when: "calculate average rating for a song"
    def result = songRatingService.getAverageRating(songId, since, until)

    then: "the average rating is calculated correctly"
    assert Math.round(result * 100) / 100.0 == 4.4
  }

  def "EntityNotFoundException is thrown when there are no ratings for a song in a given period"() {
    given:
    def songId = UUID.randomUUID()
    def since = LocalDate.of(2023, 1, 1)
    def until = LocalDate.of(2023, 1, 31)

    and: "no ratings for a particular song in a given period"
    def ratings = []
    songRatingRepository.findRatings(songId, since, until) >> ratings

    when: "calculate average rating for a song"
    songRatingService.getAverageRating(songId, since, until)

    then: "an EntityNotFoundException is thrown"
    thrown(EntityNotFoundException)
  }

  def "should return the average ratings for a song from last 3 months"() {
    given:
    def songId = UUID.randomUUID()
    def now = LocalDate.now()
    def threeMonthsBack = new MonthlySongAverageRating("", songId, now.minusMonths(3), 4.5)
    def twoMonthsBack = new MonthlySongAverageRating("", songId, now.minusMonths(2), 4.8)
    def prevMonth = new MonthlySongAverageRating("", songId, now.minusMonths(1), 4.2)

    and: "repository returns sample data"
    songRatingRepository.findSongAverages(songId, now.minusMonths(4).with(TemporalAdjusters.firstDayOfMonth()),
        now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())) >> [threeMonthsBack, twoMonthsBack, prevMonth]

    when: "invoking method"
    def result = songRatingService.getAverageLastThreeMonths(songId)

    then: "verify the result"
    result.size() == 3
    result.every { it instanceof MonthlySongAverageRating }
  }

  def "should return most trending songs"() {
    given:
    def expectedSongTrendList = [
        new SongTrend(UUID.randomUUID(), "song1", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE),
        new SongTrend(UUID.randomUUID(), "song2", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE)
    ]
    def today = LocalDate.now()
    def lastDayOfCurrentMonth = today.with(TemporalAdjusters.lastDayOfMonth())
    def lastDayOfPreviousMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())
    def lastDayOf2MonthsBack = today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth())

    songRatingRepository.getTrendingSongs(lastDayOfCurrentMonth, lastDayOfPreviousMonth, lastDayOf2MonthsBack, 100) >> expectedSongTrendList

    when: "invoking method"
    def result = songRatingService.getTrending100Songs()

    then: "verify the result"
    result == expectedSongTrendList
  }

  def "should return loosing songs"() {
    given:
    def song1 = new SongTrend(UUID.randomUUID(), "song1", new BigDecimal("2.5"), new BigDecimal("3.2"), new BigDecimal("3.5"))
    def song2 = new SongTrend(UUID.randomUUID(), "song2", new BigDecimal("1.8"), new BigDecimal("2.6"), new BigDecimal("2.8"))
    def expectedLoosingSongs = List.of(song1, song2)

    def today = LocalDate.now()
    def endOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth())
    def endOfLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())
    def endOfTwoMonthsAgo = today.minusMonths(2).with(TemporalAdjusters.lastDayOfMonth())
    def avgDiff = 0.4

    songRatingRepository.getSongsLoosing(endOfThisMonth, endOfLastMonth, endOfTwoMonthsAgo, avgDiff) >> expectedLoosingSongs.stream()

    when: "invoking method"
    def result = songRatingService.getSongsLoosing()

    then: "verify the result"
    result.toList() == expectedLoosingSongs
  }
}
