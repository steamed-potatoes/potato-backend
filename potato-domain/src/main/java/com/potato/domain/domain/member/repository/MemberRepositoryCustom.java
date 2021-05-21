package com.potato.domain.domain.member.repository;

import com.potato.domain.domain.member.Member;

public interface MemberRepositoryCustom {

    Member findMemberByEmail(String email);

    Member findMemberById(Long memberId);

}
