package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 삭제된 게시물들을 백업해두는 테이블
 * 수정되거나 삭제되면 안된다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DeletedBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long backUpId;

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

    private LocalDateTime backUpCreatedDateTime;

    @Builder
    public DeletedBoard(Long backUpId, Long memberId, String subDomain, Visible visible, Category category, String title, String content, String imageUrl, LocalDateTime backUpCreatedDateTime) {
        this.backUpId = backUpId;
        this.memberId = memberId;
        this.subDomain = subDomain;
        this.visible = visible;
        this.category = category;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.backUpCreatedDateTime = backUpCreatedDateTime;
    }

    public static DeletedBoard newBackUpInstance(Board board) {
        return DeletedBoard.builder()
            .backUpId(board.getId())
            .memberId(board.getMemberId())
            .subDomain(board.getSubDomain())
            .visible(board.getVisible())
            .category(board.getCategory())
            .title(board.getTitle())
            .content(board.getContent())
            .imageUrl(board.getImageUrl())
            .backUpCreatedDateTime(board.getCreatedDateTime())
            .build();
    }

}
