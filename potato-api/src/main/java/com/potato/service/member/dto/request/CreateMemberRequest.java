package com.potato.service.member.dto.request;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberMajor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor
public class CreateMemberRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    private String profileUrl;

    private MemberMajor major;

    @Builder(builderMethodName = "testBuilder")
    public CreateMemberRequest(@NotBlank String email, @NotBlank String name, String profileUrl, MemberMajor major) {
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
        this.major = major;
    }

    public Member toEntity() {
        return Member.newGoogleInstance(this.email, this.name, this.profileUrl, this.major);
    }

}
