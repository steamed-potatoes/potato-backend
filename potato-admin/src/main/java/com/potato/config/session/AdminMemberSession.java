package com.potato.config.session;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminMemberSession implements Serializable {

    private final Long memberId;

    public static AdminMemberSession of(Long memberId) {
        return new AdminMemberSession(memberId);
    }

}
