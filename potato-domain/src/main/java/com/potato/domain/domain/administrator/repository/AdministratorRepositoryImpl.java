package com.potato.domain.domain.administrator.repository;

import com.potato.domain.domain.administrator.Administrator;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.potato.domain.domain.administrator.QAdministrator.administrator;

@RequiredArgsConstructor
public class AdministratorRepositoryImpl implements AdministratorRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Administrator findAdminByEmail(String email) {
        return queryFactory.selectFrom(administrator)
            .where(
                administrator.email.email.eq(email)
            )
            .fetchOne();
    }

    @Override
    public Administrator findAdminById(Long adminMemberId) {
        return queryFactory.selectFrom(administrator)
            .where(
                administrator.id.eq(adminMemberId)
            )
            .fetchOne();
    }

}
