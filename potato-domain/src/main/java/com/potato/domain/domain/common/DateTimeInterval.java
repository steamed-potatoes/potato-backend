package com.potato.domain.domain.common;

import com.potato.common.exception.ErrorCode;
import com.potato.common.exception.model.ValidationException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@EqualsAndHashCode
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
            throw new ValidationException(String.format("시작 날짜(%s)가 종료 날짜(%s) 보다 이후일 수 없습니다", startDateTime, endDateTime), ErrorCode.VALIDATION_DATETIME_INTERVAL_EXCEPTION);
        }
    }

}
