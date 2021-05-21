package com.potato.api.service.member.dto.request;

import com.potato.domain.domain.member.MemberMajor;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateMemberRequest {

    @NotBlank
    private String name;

    private String profileUrl;

    private MemberMajor major;

    @NotNull
    private Integer classNumber;

    @Builder(builderMethodName = "testBuilder")
    public UpdateMemberRequest(@NotBlank String name, String profileUrl, MemberMajor major, Integer classNumber) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.major = major;
        this.classNumber = classNumber;
    }

}
