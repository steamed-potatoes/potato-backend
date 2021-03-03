package com.potato.service.member.dto.request;

import com.potato.domain.member.MemberMajor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UpdateMemberRequest {

    @NotBlank
    private String name;

    private String profileUrl;

    private MemberMajor major;

    @Builder(builderMethodName = "testBuilder")
    public UpdateMemberRequest(@NotBlank String name, String profileUrl, MemberMajor major) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.major = major;
    }

}
