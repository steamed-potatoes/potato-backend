package com.potato.domain.board.admin;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.board.DeleteBoard;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DeleteAdminBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long backUpId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", nullable = false)
    private DeleteBoard board;

    @Column(nullable = false)
    private Long deleteAdministratorId;

    private String content;

    @Builder
    public DeleteAdminBoard(Long backUpId, Long administratorId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String content, Long deleteAdministratorId) {
        this.backUpId = backUpId;
        this.board = DeleteBoard.of(administratorId, title, startDateTime, endDateTime);
        this.content = content;
        this.deleteAdministratorId = deleteAdministratorId;
    }

    public static DeleteAdminBoard newBackupInstance(AdminBoard adminBoard, Long adminMemberId) {
        return DeleteAdminBoard.builder()
            .backUpId(adminBoard.getId())
            .administratorId(adminBoard.getAdministratorId())
            .title(adminBoard.getTitle())
            .startDateTime(adminBoard.getStartDateTime())
            .endDateTime(adminBoard.getEndDateTime())
            .content(adminBoard.getContent())
            .deleteAdministratorId(adminMemberId)
            .build();
    }

}
