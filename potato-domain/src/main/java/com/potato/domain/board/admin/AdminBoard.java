package com.potato.domain.board.admin;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.board.BoardInfo;
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

    @Embedded
    private BoardInfo boardInfo;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    @Builder
    public AdminBoard(Long administratorId, String title, String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.administratorId = administratorId;
        this.boardInfo = BoardInfo.of(title, content);
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
    }

    public LocalDateTime getStartDateTime() {
        return this.dateTimeInterval.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return this.dateTimeInterval.getEndDateTime();
    }

    public void updateInfo(String title, String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.boardInfo = BoardInfo.of(title, content);
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
    }

    public String getTitle() {
        return boardInfo.getTitle();
    }

    public String getContent() {
        return boardInfo.getContent();
    }

}
