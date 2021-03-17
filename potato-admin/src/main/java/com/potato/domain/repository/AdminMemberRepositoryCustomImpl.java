package com.potato.domain.repository;

import com.potato.domain.AdminMember;
import com.potato.domain.member.Email;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.potato.domain.QAdminMember.adminMember;

@RequiredArgsConstructor
public class AdminMemberRepositoryCustomImpl implements AdminMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public AdminMember findByEmail(String email) {
        return queryFactory.selectFrom(adminMember)
            .where(
                adminMember.email.email.eq(email)
            )
            .fetchOne();
    }

}
