package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.common.DateTimeInterval;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String title;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    @Builder
    private Board(Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.memberId = memberId;
        this.title = title;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
    }

    public static Board of(Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return Board.builder()
            .memberId(memberId)
            .title(title)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
    }

    public LocalDateTime getStartDateTime() {
        return this.dateTimeInterval.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return this.dateTimeInterval.getEndDateTime();
    }

}
