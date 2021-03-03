package com.potato.domain.member.repository;

import com.potato.domain.member.Member;

public interface MemberRepositoryCustom {

    Member findMemberByEmail(String email);

    Member findMemberById(Long memberId);
    
}
