package com.potato.service.member.dto.response;

import com.potato.domain.member.MemberMajor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
