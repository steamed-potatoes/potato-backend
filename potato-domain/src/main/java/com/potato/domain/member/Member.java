package com.potato.domain.member;

import com.potato.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private MemberMajor major;

    @Enumerated(EnumType.STRING)
    private MemberProvider provider;

    @Builder
    public Member(String email, String name, String profileUrl, MemberMajor major, MemberProvider provider) {
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
        this.major = major;
        this.provider = provider;
    }

    public static Member newGoogleInstance(String email, String name, String profileUrl, MemberMajor major) {
        return Member.builder()
            .email(email)
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .provider(MemberProvider.GOOGLE)
            .build();
    }

}
