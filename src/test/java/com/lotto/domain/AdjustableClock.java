package com.lotto.domain;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AdjustableClock extends Clock {

    private final ZoneId zone;
    protected Instant instant;

    public AdjustableClock(ZoneId zone, Instant initialInstant) {
        this.zone = zone;
        this.instant = initialInstant;
    }

    public static AdjustableClock ofLocalDataAndLocalTime(LocalDate date, LocalTime time, ZoneId zone) {
        ZonedDateTime zonedDateTime = createZoneDataTime(date, time, zone);
        return new AdjustableClock(zone, zonedDateTime.toInstant());
    }

    private static ZonedDateTime createZoneDataTime(final LocalDate date, final LocalTime time, final ZoneId zone) {
        return ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                time.getHour(), time.getMinute(), time.getSecond(), time.getNano(), zone);
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        if(zone.equals(this.zone)) {
            return this;
        }
        return new AdjustableClock(zone, instant);
    }

    @Override
    public long millis() {
        return instant().toEpochMilli();
    }

    @Override
    public Instant instant() {
        return instant;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof AdjustableClock other
                && instant.equals(other.instant)
                && zone.equals(other.zone);
    }

    @Override
    public int hashCode() {
        return instant.hashCode() + zone.hashCode();
    }
    @Override
    public String toString() {
        return "AdjustableClock" + instant + "," + zone + "]";
    }

    public void advanceInTimeBy(Duration clockOffset) {this.instant = instant.plus(clockOffset);}

    public void plusDays(int days) {
        Duration offset = Duration.ofDays(days);
        advanceInTimeBy(offset);
    }

    public void plusDaysAndMinutes(int days, int minutes) {
        Duration offset = Duration.ofDays(days);
        advanceInTimeBy(offset);
        Duration minutesDuration = Duration.ofMinutes(minutes);
        advanceInTimeBy(minutesDuration);
    }

    public void setClockToLocalDataTime(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = createZoneDataTime(localDateTime.toLocalDate(), localDateTime.toLocalTime(), zone);
        this.instant = zonedDateTime.toInstant();
    }



}
