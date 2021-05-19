package com.potato.domain.board.repository;

import com.potato.domain.board.BoardImage;
import com.potato.domain.board.QBoardImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.board.QBoardImage.boardImage;

@RequiredArgsConstructor
public class BoardImageRepositoryCustomImpl implements BoardImageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardImage> findBoardImageByOrganizationBoardId(Long organizationBoardId) {
        return queryFactory.selectFrom(boardImage)
            .where(
                boardImage.boardId.eq(organizationBoardId)
            )
            .fetch();
    }

}
