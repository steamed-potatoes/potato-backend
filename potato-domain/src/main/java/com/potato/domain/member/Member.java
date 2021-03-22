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

    @Embedded
    private Email email;

    @Column(nullable = false)
    private String name;

    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private MemberMajor major;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberProvider provider;

    @Embedded
    private ClassNumber classNumber;

    @Builder
    public Member(String email, String name, String profileUrl, MemberMajor major, MemberProvider provider, Integer classNumber) {
        this.email = Email.of(email);
        this.name = name;
        this.profileUrl = profileUrl;
        this.major = major;
        this.provider = provider;
        this.classNumber = ClassNumber.of(classNumber);
    }

    public static Member newGoogleInstance(String email, String name, String profileUrl, MemberMajor major, Integer classNumber) {
        return Member.builder()
            .email(email)
            .name(name)
            .profileUrl(profileUrl)
            .major(major)
            .provider(MemberProvider.GOOGLE)
            .classNumber(classNumber)
            .build();
    }

    public void updateMemberInfo(String name, String profileUrl, MemberMajor major, Integer classNumber) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.major = major;
        this.classNumber = ClassNumber.of(classNumber);
    }

    public String getEmail() {
        return this.email.getEmail();
    }

    public String getMajorName() {
        if (this.major == null) {
            return null;
        }
        return this.major.getName();
    }

    public Integer getClassNumber() {
        if (this.classNumber == null) {
            return null;
        }
        return this.classNumber.getClassNumber();
    }

}
