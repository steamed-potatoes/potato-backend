package com.potato.domain.common;

import com.potato.domain.domain.common.DateTimeInterval;
import com.potato.common.exception.model.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeIntervalTest {

    @Test
    void 시작날짜가_종료날짜보다_이전인_경우_정상() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 3, 2, 0, 0);

        // when
        DateTimeInterval dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);

        // then
        assertThat(dateTimeInterval.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(dateTimeInterval.getEndDateTime()).isEqualTo(endDateTime);
    }

    @Test
    void 시작날짜가_종료날짜보다_이후일_경우_에러() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 3, 1, 0, 0);

        // when & then
        assertThatThrownBy(() -> DateTimeInterval.of(startDateTime, endDateTime)).isInstanceOf(ValidationException.class);
    }

    @Test
    void 시작날짜가_종료날짜와_같은경우_정상() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 3, 1, 0, 0);

        // when
        DateTimeInterval dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);

        // then
        assertThat(dateTimeInterval.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(dateTimeInterval.getEndDateTime()).isEqualTo(endDateTime);
    }

    @Test
    void DateTime_Interval_동등성비교_같은경우_true() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 5, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 3, 6, 0, 0, 0);

        DateTimeInterval dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);

        // when
        boolean result = DateTimeInterval.of(startDateTime, endDateTime).equals(dateTimeInterval);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void DateTime_Interval_동등성비교_다른경우_false() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 5, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 3, 6, 0, 0, 0);

        DateTimeInterval dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);

        // when
        boolean result = DateTimeInterval.of(startDateTime, LocalDateTime.of(2021, 3, 6, 0, 0, 1)).equals(dateTimeInterval);

        // then
        assertThat(result).isFalse();
    }

}
