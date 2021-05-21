package com.potato.domain.domain.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Test Helper Class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCreator {

    public static Member create(String email) {
        return Member.builder()
            .email(email)
            .name("테스트 유저")
            .profileUrl("https://profile.com")
            .provider(MemberProvider.GOOGLE)
            .major(MemberMajor.IT_ICT)
            .classNumber(201610323)
            .build();
    }

    public static Member create(String email, String name, String profileUrl, MemberMajor major, Integer classNumber) {
        return Member.builder()
            .email(email)
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .provider(MemberProvider.GOOGLE)
            .major(MemberMajor.IT_ICT)
            .classNumber(classNumber)
            .build();
    }

}
