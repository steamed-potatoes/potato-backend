package com.potato.domain.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCreator {

    public static Member create(String email) {
        return Member.builder()
            .email(email)
            .name("멤버")
            .provider(MemberProvider.GOOGLE)
            .build();
    }

    public static Member create(String email, String name, String profileUrl, MemberMajor major) {
        return Member.builder()
            .email(email)
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .provider(MemberProvider.GOOGLE)
            .build();
    }

}
