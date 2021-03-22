package com.potato.service.member.dto.request;

import com.potato.domain.member.MemberMajor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor
public class UpdateMemberRequest {

    @NotBlank
    private String name;

    private String profileUrl;

    private MemberMajor major;

    private Integer classNumber;

    @Builder(builderMethodName = "testBuilder")
    public UpdateMemberRequest(@NotBlank String name, String profileUrl, MemberMajor major, Integer classNumber) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.major = major;
        this.classNumber = classNumber;
    }

}
