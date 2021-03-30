package com.potato.service.member.dto.response;

import com.potato.domain.member.Member;
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

    private String major;

    private Integer classNumber;

    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(member.getId(), member.getEmail(), member.getName(), member.getProfileUrl(), member.getMajorName(), member.getClassNumber());
    }

}
