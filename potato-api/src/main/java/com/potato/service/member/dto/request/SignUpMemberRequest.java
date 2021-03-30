package com.potato.service.member.dto.request;

import com.potato.domain.member.Member;
import com.potato.domain.member.MemberMajor;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpMemberRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    private String profileUrl;

    private MemberMajor major;

    @NotNull
    private Integer classNumber;

    @Builder(builderMethodName = "testBuilder")
    public SignUpMemberRequest(@NotBlank String email, @NotBlank String name, String profileUrl, MemberMajor major, @NotBlank Integer classNumber) {
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
        this.major = major;
        this.classNumber = classNumber;
    }

    public Member toEntity() {
        return Member.newGoogleInstance(this.email, this.name, this.profileUrl, this.major, this.classNumber);
    }

}
