package com.potato.domain.board.admin;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.board.Board;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AdminBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private Long administratorId;

    private String content;

    @Builder
    public AdminBoard(Long administratorId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String content) {
        this.administratorId = administratorId;
        this.board = Board.of(title, startDateTime, endDateTime);
        this.content = content;
    }

    public String getTitle() {
        return this.board.getTitle();
    }

    public LocalDateTime getStartDateTime() {
        return this.board.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return this.board.getEndDateTime();
    }

    public void updateInfo(String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.board = Board.of(title, startDateTime, endDateTime);
        this.content = content;
    }

    public DeleteAdminBoard delete(Long adminMemberId) {
        return DeleteAdminBoard.newBackupInstance(this, adminMemberId);
    }

}
