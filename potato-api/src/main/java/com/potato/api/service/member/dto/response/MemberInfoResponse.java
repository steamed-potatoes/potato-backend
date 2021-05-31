package com.potato.api.service.member.dto.response;

import com.potato.domain.domain.member.Member;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoResponse {

    private Long id;

    private String email;

    private String name;

    private String profileUrl;

    private MajorInfoResponse major;

    private Integer classNumber;

    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(member.getId(), member.getEmail(), member.getName(), member.getProfileUrl(), MajorInfoResponse.of(member.getMajor()), member.getClassNumber());
    }

    public static MemberInfoResponse deletedMember() {
        return new MemberInfoResponse(null, null, "삭제된 계정입니다", null, null, null);
    }

}
