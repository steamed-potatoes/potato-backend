package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import com.potato.exception.ConflictException;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String subDomain;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Visible visible;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String imageUrl;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BoardLike> boardLikeList = new ArrayList<>();

    private int likesCount;

    @Builder
    public Board(String subDomain, Long memberId, Visible visible, String title, String content, String imageUrl, Category category) {
        this.memberId = memberId;
        this.subDomain = subDomain;
        this.visible = visible;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
        this.likesCount = 0;
    }

    public static Board newInstance(String subDomain, Long memberId, Visible visible, String title, String content, String imageUrl, Category category) {
        return Board.builder()
            .subDomain(subDomain)
            .memberId(memberId)
            .visible(visible)
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .category(category)
            .build();
    }

    public void updateBoardInfo(Visible visible, String title, String content, String imageUrl, Category category) {
        this.visible = visible;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void addLike(Long memberId) {
        if (hasAlreadyLike(memberId)) {
            throw new ConflictException(String.format("이미 멤버 (%s)는 게시물 (%s)에 좋아요를 눌렀습니다", memberId, this.id));
        }
        BoardLike boardLike = BoardLike.of(this, memberId);
        this.boardLikeList.add(boardLike);
        this.likesCount++;
    }

    private boolean hasAlreadyLike(Long memberId) {
        return this.boardLikeList.stream()
            .anyMatch(boardLike -> boardLike.isSameEntity(memberId));
    }

    public void cancelLike(Long memberId) {
        BoardLike boardLike = findLike(memberId);
        boardLikeList.remove(boardLike);
        this.likesCount--;
    }

    private BoardLike findLike(Long memberId) {
        return this.boardLikeList.stream()
            .filter(mapper -> mapper.isSameEntity(memberId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("멤버 (%s)는 게시물 (%s)에 좋아요를 누른 적이 없습니다", memberId, this.id)));
    }

    public boolean isPrivate() {
        return this.visible.equals(Visible.PRIVATE);
    }

}
