package com.potato.domain.board.organization;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.board.DeleteBoard;
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
public class DeleteOrganizationBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long backUpId;

    @Column(nullable = false)
    private String subDomain;

    @Column(nullable = false)
    private OrganizationBoardType organizationBoardType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", nullable = false)
    private DeleteBoard board;

    private String content;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime backUpCreatedDateTime;

    @Builder
    public DeleteOrganizationBoard(Long backUpId, String subDomain, Long memberId, OrganizationBoardType organizationBoardType, String title,
                                   String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime backUpCreatedDateTime) {
        this.backUpId = backUpId;
        this.board = DeleteBoard.of(memberId, title, startDateTime, endDateTime);
        this.subDomain = subDomain;
        this.organizationBoardType = organizationBoardType;
        this.content = content;
        this.imageUrl = imageUrl;
        this.backUpCreatedDateTime = backUpCreatedDateTime;
    }

    public static DeleteOrganizationBoard newBackUpInstance(OrganizationBoard organizationBoard, Long memberId) {
        return DeleteOrganizationBoard.builder()
            .backUpId(organizationBoard.getId())
            .memberId(memberId)
            .subDomain(organizationBoard.getSubDomain())
            .organizationBoardType(organizationBoard.getType())
            .title(organizationBoard.getTitle())
            .content(organizationBoard.getContent())
            .imageUrl(organizationBoard.getImageUrl())
            .startDateTime(organizationBoard.getStartDateTime())
            .endDateTime(organizationBoard.getEndDateTime())
            .backUpCreatedDateTime(organizationBoard.getCreatedDateTime())
            .build();
    }

}
