package com.potato.domain.board.organization;

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

    @Column(nullable = false)
    private String title;

    private String content;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    private String imageUrl;

    @Column(nullable = false)
    private Long backUpId;

    private Long deletedMemberId;

    private Long deletedAdminMemberId;

    @Column(nullable = false)
    private LocalDateTime backUpCreatedDateTime;

    @Builder
    public DeleteOrganizationBoard(Long backUpId, String subDomain, Long memberId, OrganizationBoardCategory category, String title,
                                   String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime backUpCreatedDateTime, Long deletedMemberId, Long deletedAdminMemberId) {
        this.backUpId = backUpId;
        this.memberId = memberId;
        this.title = title;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.subDomain = subDomain;
        this.category = category;
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
            .category(organizationBoard.getCategory())
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
            .category(organizationBoard.getCategory())
            .title(organizationBoard.getTitle())
            .content(organizationBoard.getContent())
            .imageUrl(organizationBoard.getImageUrl())
            .startDateTime(organizationBoard.getStartDateTime())
            .endDateTime(organizationBoard.getEndDateTime())
            .backUpCreatedDateTime(organizationBoard.getCreatedDateTime())
            .build();
    }

    public LocalDateTime getStartDateTime() {
        return this.dateTimeInterval.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return this.dateTimeInterval.getEndDateTime();
    }

}
