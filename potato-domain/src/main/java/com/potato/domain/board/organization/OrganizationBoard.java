package com.potato.domain.board.organization;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.board.Board;
import com.potato.exception.ConflictException;
import com.potato.exception.NotFoundException;
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
@Table(
    indexes = {
        @Index(name = "idx_organization_board_1", columnList = "subDomain")
    }
)
@Entity
public class OrganizationBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subDomain;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private Long memberId;

    private String content;

    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganizationBoardType type;

    @OneToMany(mappedBy = "organizationBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrganizationBoardLike> organizationBoardLikeList = new ArrayList<>();

    private int likesCount;

    @Builder
    public OrganizationBoard(String subDomain, Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String content, String imageUrl, OrganizationBoardType type) {
        this.subDomain = subDomain;
        this.memberId = memberId;
        this.board = Board.of(title, startDateTime, endDateTime);
        this.content = content;
        this.imageUrl = imageUrl;
        this.type = type;
        this.likesCount = 0;
    }

    public void updateInfo(String title, String content, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, OrganizationBoardType type, Long memberId) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.type = type;
        this.memberId = memberId;
        this.board = Board.of(title, startDateTime, endDateTime);
    }

    public void addLike(Long memberId) {
        if (hasAlreadyLike(memberId)) {
            throw new ConflictException(String.format("이미 멤버 (%s)는 게시물 (%s)에 좋아요를 눌렀습니다", memberId, this.id), "이미 해당 게시물을 좋아하고 있습니다.");
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

    public String getTitle() {
        return this.board.getTitle();
    }

    public LocalDateTime getStartDateTime() {
        return this.board.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return this.board.getEndDateTime();
    }

}
