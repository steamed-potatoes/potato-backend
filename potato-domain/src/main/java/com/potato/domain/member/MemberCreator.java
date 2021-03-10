package com.potato.domain.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCreator {

    public static Member create(String email) {
        return Member.builder()
            .email(email)
            .name("테스트 유저")
            .profileUrl("http://profile.com")
            .provider(MemberProvider.GOOGLE)
            .major(MemberMajor.IT_ICT)
            .build();
    }

    public static Member create(String email, String name, String profileUrl, MemberMajor major) {
        return Member.builder()
            .email(email)
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .provider(MemberProvider.GOOGLE)
            .major(MemberMajor.IT_ICT)
            .build();
    }

}
