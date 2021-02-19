package com.potato.domain.organization;

import com.potato.domain.BaseTimeEntity;
import com.potato.exception.ForbiddenException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Organization extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subDomain;

    @Column(nullable = false)
    private String name;

    private String description;

    private int membersCount;

    @Enumerated(EnumType.STRING)
    private OrganizationCategory category;

    private String profileUrl;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrganizationMemberMapper> organizationMemberMapperList = new ArrayList<>();

    @Builder
    public Organization(String subDomain, String name, String description, OrganizationCategory category, String profileUrl) {
        this.subDomain = subDomain;
        this.name = name;
        this.description = description;
        this.category = category;
        this.profileUrl = profileUrl;
        this.membersCount = 0;
    }

    public static Organization defaultInstance(String subDomain, String name, String description, String profileUrl) {
        return Organization.builder()
            .subDomain(subDomain)
            .name(name)
            .description(description)
            .profileUrl(profileUrl)
            .category(OrganizationCategory.NON_APPROVED_CIRCLE)
            .build();
    }

    public void updateInfo(String name, String description, String profileUrl) {
        this.name = name;
        this.description = description;
        this.profileUrl = profileUrl;
    }

    public void validateAdminMember(Long memberId) {
        if (!isAdmin(memberId)) {
            throw new ForbiddenException(String.format("멤버 (%s)는 조직(%s)의 관리자가 아닙니다", memberId, subDomain));
        }
    }

    public void addAdmin(Long memberId) {
        OrganizationMemberMapper organizationMemberMapper = OrganizationMemberMapper.newAdmin(this, memberId);
        this.organizationMemberMapperList.add(organizationMemberMapper);
        this.membersCount++;
    }

    private boolean isAdmin(Long memberId) {
        return this.organizationMemberMapperList.stream()
            .anyMatch(organizationMemberMapper -> organizationMemberMapper.isAdmin(memberId));
    }

    public void addUser(Long memberId) {
        OrganizationMemberMapper organizationMemberMapper = OrganizationMemberMapper.newUser(this, memberId);
        this.organizationMemberMapperList.add(organizationMemberMapper);
        this.membersCount++;
    }

    public void validatePendingMember(Long memberId) {
        if (!isPending(memberId)) {
            throw new ForbiddenException(String.format("멤버 (%s)는 조직(%s)의 가입신청자가 아닙니다", memberId, subDomain));
        }
    }

    public boolean isPending(Long memberId) {
        return this.organizationMemberMapperList.stream()
            .anyMatch(organizationMemberMapper -> organizationMemberMapper.isPending(memberId));
    }

    public void updateRole(Long memberId) {
        OrganizationMemberMapper organizationMemberMapper = organizationMemberMapperList.stream()
            .filter(member -> memberId.equals(member.getMemberId()))
            .findAny()
            .orElse(null);
        organizationMemberMapper.updateRole();
    }

}
