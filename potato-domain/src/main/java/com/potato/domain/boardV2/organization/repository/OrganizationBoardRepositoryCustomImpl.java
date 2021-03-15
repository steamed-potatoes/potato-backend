package com.potato.domain.boardV2.organization.repository;

import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.potato.domain.boardV2.QBoardV2.boardV2;
import static com.potato.domain.boardV2.organization.QOrganizationBoard.organizationBoard;

@RequiredArgsConstructor
public class OrganizationBoardRepositoryCustomImpl implements OrganizationBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public OrganizationBoard findOrganizationBoardById(Long id) {
        return queryFactory.selectFrom(organizationBoard)
            .innerJoin(organizationBoard.board, boardV2).fetchJoin()
            .where(
                organizationBoard.id.eq(id)
            ).fetchOne();
    }

    @Override
    public OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain) {
        return queryFactory.selectFrom(organizationBoard)
            .innerJoin(organizationBoard.board, boardV2).fetchJoin()
            .where(
                organizationBoard.id.eq(organizationBoardId),
                organizationBoard.subDomain.eq(subDomain)
            ).fetchOne();
    }

}
