package com.potato.domain.boardV2.organization;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.boardV2.BoardV2;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrganizationBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subDomain;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardV2 board;

    private String content;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private OrganizationBoardType type;

    @Builder
    public OrganizationBoard(String subDomain, Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String content, String imageUrl, OrganizationBoardType type) {
        this.subDomain = subDomain;
        this.board = BoardV2.of(memberId, title, startDateTime, endDateTime);
        this.content = content;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public String getTitle() {
        return this.board.getTitle();
    }

    public Long getMemberId() {
        return this.board.getMemberId();
    }

    public LocalDateTime getStartDateTime() {
        return this.board.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return this.getEndDateTime();
    }

}
