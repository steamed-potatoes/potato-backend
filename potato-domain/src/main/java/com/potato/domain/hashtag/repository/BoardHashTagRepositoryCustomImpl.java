package com.potato.domain.hashtag.repository;

import com.potato.domain.board.BoardType;
import com.potato.domain.hashtag.BoardHashTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.hashtag.QBoardHashTag.boardHashTag;

@RequiredArgsConstructor
public class BoardHashTagRepositoryCustomImpl implements BoardHashTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardHashTag> findAllByTypeAndBoardId(Long boardId, BoardType type) {
        return queryFactory.selectFrom(boardHashTag)
            .where(
                boardHashTag.boardId.eq(boardId),
                boardHashTag.type.eq(type)
            ).fetch();
    }

}
