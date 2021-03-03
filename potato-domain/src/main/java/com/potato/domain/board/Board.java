package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Builder
    public Board(String subDomain, Long memberId, Visible visible, String title, String content, String imageUrl, Category category) {
        this.memberId = memberId;
        this.subDomain = subDomain;
        this.visible = visible;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
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

}
