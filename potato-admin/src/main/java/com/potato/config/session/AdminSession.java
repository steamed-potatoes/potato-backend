package com.potato.config.session;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminSession implements Serializable {

    private final Long adminId;

    public static AdminSession of(Long memberId) {
        return new AdminSession(memberId);
    }

}
