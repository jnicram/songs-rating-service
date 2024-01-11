package pl.recruitment.songsratingservice.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record SongTrend(UUID songId, String songName, BigDecimal ratingThisMonth, BigDecimal ratingPrevMonth,
                        BigDecimal rating2monthsBack) {

}
