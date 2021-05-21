package com.potato.admin.service.member.dto.response;

import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.member.MemberProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoResponse {

    private final Long id;

    private final String email;

    private final String name;

    private final String profileUrl;

    private final String major;

    private final MemberProvider provider;

    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(member.getId(), member.getEmail(), member.getName(),
            member.getProfileUrl(), member.getMajorName(), member.getProvider());
    }

}
