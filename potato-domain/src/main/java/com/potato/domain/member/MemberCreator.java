package com.potato.domain.member;

public final class MemberCreator {

    public static Member create(String email) {
        return Member.builder()
            .email(email)
            .name("멤버")
            .provider(MemberProvider.GOOGLE)
            .build();
    }

}
