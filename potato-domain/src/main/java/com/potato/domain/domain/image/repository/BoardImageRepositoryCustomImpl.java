package com.potato.domain.domain.image.repository;

import com.potato.domain.domain.image.BoardImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.domain.image.QBoardImage.boardImage;


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
