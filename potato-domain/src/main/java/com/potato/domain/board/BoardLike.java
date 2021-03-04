package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoardLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private Long memberId;

    private BoardLike(Board board, Long memberId) {
        this.board = board;
        this.memberId = memberId;
    }

    public static BoardLike of(Board board, Long memberId) {
        return new BoardLike(board, memberId);
    }

    public boolean isSameEntity(Long memberId) {
        return this.memberId.equals(memberId);
    }

}
