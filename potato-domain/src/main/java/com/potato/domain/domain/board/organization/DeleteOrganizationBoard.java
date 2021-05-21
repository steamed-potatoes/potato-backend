package com.potato.domain.domain.board.organization;

import com.potato.domain.domain.BaseTimeEntity;
import com.potato.domain.domain.board.BoardInfo;
import com.potato.domain.domain.common.delete.DeletedTrackingInfo;
import com.potato.domain.domain.common.delete.BackUpInfo;
import com.potato.domain.domain.common.DateTimeInterval;
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

    @Column(nullable = false, length = 50)
    private String subDomain;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private OrganizationBoardCategory category;

    @Embedded
    private BoardInfo boardInfo;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    private int likesCount;

    @Embedded
    private DeletedTrackingInfo deletedTrackingInfo;

    @Embedded
    private BackUpInfo backUpInfo;

    @Builder
    public DeleteOrganizationBoard(Long backUpId, String subDomain, Long memberId, OrganizationBoardCategory category, String title,
                                   String content, LocalDateTime startDateTime, LocalDateTime endDateTime, int likesCount,
                                   LocalDateTime backUpCreatedDateTime, Long deletedMemberId, Long deletedAdminMemberId) {
        this.memberId = memberId;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.subDomain = subDomain;
        this.category = category;
        this.boardInfo = BoardInfo.of(title, content);
        this.likesCount = likesCount;
        this.backUpInfo = BackUpInfo.of(backUpId, backUpCreatedDateTime);
        this.deletedTrackingInfo = DeletedTrackingInfo.of(deletedMemberId, deletedAdminMemberId);
    }

    public static DeleteOrganizationBoard newBackUpInstance(OrganizationBoard organizationBoard, Long memberId) {
        return DeleteOrganizationBoard.builder()
            .memberId(organizationBoard.getMemberId())
            .subDomain(organizationBoard.getSubDomain())
            .category(organizationBoard.getCategory())
            .title(organizationBoard.getTitle())
            .content(organizationBoard.getContent())
            .startDateTime(organizationBoard.getStartDateTime())
            .endDateTime(organizationBoard.getEndDateTime())
            .likesCount(builder().likesCount)
            .backUpCreatedDateTime(organizationBoard.getCreatedDateTime())
            .backUpId(organizationBoard.getId())
            .deletedMemberId(memberId)
            .build();
    }

    public static DeleteOrganizationBoard newBackUpInstanceByAdmin(OrganizationBoard organizationBoard, Long adminMemberId) {
        return DeleteOrganizationBoard.builder()
            .memberId(organizationBoard.getMemberId())
            .subDomain(organizationBoard.getSubDomain())
            .category(organizationBoard.getCategory())
            .title(organizationBoard.getTitle())
            .content(organizationBoard.getContent())
            .startDateTime(organizationBoard.getStartDateTime())
            .endDateTime(organizationBoard.getEndDateTime())
            .backUpId(organizationBoard.getId())
            .backUpCreatedDateTime(organizationBoard.getCreatedDateTime())
            .deletedAdminMemberId(adminMemberId)
            .build();
    }

    public LocalDateTime getStartDateTime() {
        return this.dateTimeInterval.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return this.dateTimeInterval.getEndDateTime();
    }

    public String getTitle() {
        return boardInfo.getTitle();
    }

    public String getContent() {
        return boardInfo.getContent();
    }

    public Long getBackUpId() {
        return backUpInfo.getBackUpId();
    }

    public Long getDeletedMemberId() {
        return deletedTrackingInfo.getDeletedMemberId();
    }

    public Long getDeletedAdminMemberId() {
        return deletedTrackingInfo.getDeletedAdminMemberId();
    }

}
