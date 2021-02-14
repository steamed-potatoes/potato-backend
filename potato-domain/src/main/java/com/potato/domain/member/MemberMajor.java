package com.potato.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberMajor {

    IT_ICT("IT학부, ICT융합학과"),
    IT_COMPUTER("IT학부, 컴퓨터공학과");

    private final String name;

}
