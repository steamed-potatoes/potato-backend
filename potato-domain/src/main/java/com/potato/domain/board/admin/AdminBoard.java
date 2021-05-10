package com.potato.domain.board.admin;

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
@Table(
    indexes = {
        @Index(name = "idx_admin_board_1", columnList = "startDateTime,endDateTime"),
    }
)
public class AdminBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long administratorId;

    @Column(nullable = false)
    private String title;

    private String content;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    @Builder
    public AdminBoard(Long administratorId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String content) {
        this.administratorId = administratorId;
        this.title = title;
        this.content = content;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
    }

    public LocalDateTime getStartDateTime() {
        return this.dateTimeInterval.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return this.dateTimeInterval.getEndDateTime();
    }

    public void updateInfo(String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.content = content;
    }

}
