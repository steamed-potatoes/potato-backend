package com.potato.domain.domain.member.repository;

import com.potato.domain.domain.member.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    Member findMemberByEmail(String email);

    Member findMemberById(Long memberId);

    List<Member> findAllByInIds(List<Long> memberIds);

}
