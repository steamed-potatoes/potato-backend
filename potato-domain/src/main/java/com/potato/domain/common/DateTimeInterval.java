package com.potato.domain.common;

import com.potato.exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DateTimeInterval {

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private DateTimeInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static DateTimeInterval of(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        validateDateTime(startDateTime, endDateTime);
        return new DateTimeInterval(startDateTime, endDateTime);
    }

    private static void validateDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new ValidationException(String.format("시작 날짜(%s)가 종료 날짜(%s) 보다 이후일 수 없습니다", startDateTime, endDateTime), "시작날짜보다 종료날짜가 이후일 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeInterval that = (DateTimeInterval) o;
        return Objects.equals(startDateTime, that.startDateTime) && Objects.equals(endDateTime, that.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDateTime, endDateTime);
    }

}
