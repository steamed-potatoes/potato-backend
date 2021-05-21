package com.potato.domain.domain.administrator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdministratorCreator {

    public static Administrator create(String email, String name) {
        return Administrator.builder()
            .email(email)
            .name(name)
            .build();
    }

}
