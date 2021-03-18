package com.potato.domain.adminMember.repository;

import com.potato.domain.adminMember.AdminMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.potato.domain.adminMember.QAdminMember.adminMember;

@RequiredArgsConstructor
public class AdminMemberRepositoryImpl implements AdminMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public AdminMember findAdminByEmail(String email) {
        return queryFactory.selectFrom(adminMember)
            .where(
                adminMember.email.email.eq(email)
            )
            .fetchOne();
    }

    @Override
    public AdminMember findAdminById(Long adminMemberId) {
        return queryFactory.selectFrom(adminMember)
            .where(
                adminMember.id.eq(adminMemberId)
            )
            .fetchOne();
    }

}
