package com.potato.api.service.member.dto.response;

import com.potato.domain.domain.member.MemberMajor;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MajorInfoResponse {

    private MemberMajor majorCode;

    private String department;

    private String major;

    public static MajorInfoResponse of(MemberMajor major) {
        return new MajorInfoResponse(major, major.getDepartment(), major.getName());
    }

}
