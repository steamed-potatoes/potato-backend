package com.potato.domain.board.organization;

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
public class DeleteOrganizationBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long backUpId;

    @Column(nullable = false)
    private LocalDateTime backUpCreatedDateTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", nullable = false)
    private DeleteBoard board;

    @Column(nullable = false, length = 50)
    private String subDomain;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private OrganizationBoardType organizationBoardType;

    private Long deletedMemberId;

    private Long deletedAdminMemberId;

    private String content;

    private String imageUrl;

    @Builder
    public DeleteOrganizationBoard(Long backUpId, String subDomain, Long memberId, OrganizationBoardType organizationBoardType, String title,
                                   String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime backUpCreatedDateTime, Long deletedMemberId, Long deletedAdminMemberId) {
        this.backUpId = backUpId;
        this.board = DeleteBoard.of(memberId, title, startDateTime, endDateTime);
        this.subDomain = subDomain;
        this.organizationBoardType = organizationBoardType;
        this.content = content;
        this.imageUrl = imageUrl;
        this.backUpCreatedDateTime = backUpCreatedDateTime;
        this.deletedMemberId = deletedMemberId;
        this.deletedAdminMemberId = deletedAdminMemberId;
    }

    public static DeleteOrganizationBoard newBackUpInstance(OrganizationBoard organizationBoard, Long memberId) {
        return DeleteOrganizationBoard.builder()
            .backUpId(organizationBoard.getId())
            .deletedMemberId(memberId)
            .memberId(organizationBoard.getMemberId())
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

    public static DeleteOrganizationBoard newBackUpInstanceByAdmin(OrganizationBoard organizationBoard, Long adminMemberId) {
        return DeleteOrganizationBoard.builder()
            .backUpId(organizationBoard.getId())
            .deletedAdminMemberId(adminMemberId)
            .memberId(organizationBoard.getMemberId())
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
