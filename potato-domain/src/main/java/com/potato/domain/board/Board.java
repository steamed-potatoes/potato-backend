package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.organization.OrganizationCategory;
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
    private Long organizationId;

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
    public Board(Visible visible, String title, String content, String imageUrl, Category category, Long memberId, Long organizationId) {
        this.visible = visible;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
        this.memberId = memberId;
        this.organizationId = organizationId;
    }

    public static Board newInstance(Visible visible, String title, String content, String imageUrl, Category category, Long memberId, Long organizationId) {
        return Board.builder()
            .visible(visible)
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .category(category)
            .memberId(memberId)
            .organizationId(organizationId)
            .build();
    }

    public void updateBoardInfo(Visible visible,String title, String content, String imageUrl, Category category) {
        this.visible = visible;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
    }

}
