package com.potato.config.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class MemberSession implements Serializable {

    private final Long memberId;

    public static MemberSession of(Long memberId) {
        return new MemberSession(memberId);
    }

}
