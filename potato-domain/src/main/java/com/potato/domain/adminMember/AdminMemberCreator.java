package com.potato.domain.adminMember;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminMemberCreator {

    public static AdminMember create(String email, String name) {
        return AdminMember.builder()
            .email(email)
            .name(name)
            .build();
    }

}
