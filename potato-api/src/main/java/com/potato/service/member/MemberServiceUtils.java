package com.potato.service.member;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberServiceUtils {

    public static void validateNonExistsMember(MemberRepository memberRepository, String email) {
        Member member = memberRepository.findMemberByEmail(email);
        if (member != null) {
            throw new IllegalArgumentException(String.format("이미 존재하는 회원 (%s) 입니다.", email));
        }
    }

}
