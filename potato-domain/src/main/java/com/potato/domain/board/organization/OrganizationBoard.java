package com.potato.domain.board.organization;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.board.BoardInfo;
import com.potato.domain.common.DateTimeInterval;
import com.potato.exception.model.ConflictException;
import com.potato.exception.model.NotFoundException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_organization_board_1", columnList = "subDomain"),
        @Index(name = "idx_organization_board_2", columnList = "likesCount"),
        @Index(name = "idx_organization_board_3", columnList = "category"),
        @Index(name = "idx_organization_board_4", columnList = "startDateTime,endDateTime")
    }
)
public class OrganizationBoard extends BaseTimeEntity {

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

    @OneToMany(mappedBy = "organizationBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrganizationBoardLike> organizationBoardLikeList = new ArrayList<>();

    @Builder
    public OrganizationBoard(String subDomain, Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String content, OrganizationBoardCategory category) {
        this.subDomain = subDomain;
        this.memberId = memberId;
        this.category = category;
        this.boardInfo = BoardInfo.of(title, content);
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
        this.likesCount = 0;
    }

    public void updateInfo(String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime, OrganizationBoardCategory category, Long memberId) {
        this.boardInfo = BoardInfo.of(title, content);
        this.category = category;
        this.memberId = memberId;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
    }

    public void addLike(Long memberId) {
        if (hasAlreadyLike(memberId)) {
            throw new ConflictException(String.format("이미 멤버 (%s)는 게시물 (%s)에 좋아요를 눌렀습니다", memberId, this.id));
        }
        OrganizationBoardLike like = OrganizationBoardLike.of(this, memberId);
        this.organizationBoardLikeList.add(like);
        this.likesCount++;
    }

    private boolean hasAlreadyLike(Long memberId) {
        return this.organizationBoardLikeList.stream()
            .anyMatch(boardLike -> boardLike.isSameEntity(memberId));
    }

    public void cancelLike(Long memberId) {
        OrganizationBoardLike like = findLike(memberId);
        this.organizationBoardLikeList.remove(like);
        this.likesCount--;
    }

    private OrganizationBoardLike findLike(Long memberId) {
        return this.organizationBoardLikeList.stream()
            .filter(mapper -> mapper.isSameEntity(memberId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("멤버 (%s)는 게시물 (%s)에 좋아요를 누른 적이 없습니다", memberId, this.id)));
    }

    public DeleteOrganizationBoard delete(Long memberId) {
        return DeleteOrganizationBoard.newBackUpInstance(this, memberId);
    }

    public DeleteOrganizationBoard deleteByAdmin(Long adminMemberId) {
        return DeleteOrganizationBoard.newBackUpInstanceByAdmin(this, adminMemberId);
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

}
