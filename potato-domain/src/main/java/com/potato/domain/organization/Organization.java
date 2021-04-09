package com.potato.domain.organization;

import com.potato.domain.BaseTimeEntity;
import com.potato.exception.ConflictException;
import com.potato.exception.ForbiddenException;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private int followersCount;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrganizationMemberMapper> organizationMemberMapperList = new ArrayList<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrganizationFollower> organizationFollowerList = new ArrayList<>();

    @Builder
    public Organization(String subDomain, String name, String description, OrganizationCategory category, String profileUrl) {
        SubDomainValidator.validateSubDomain(subDomain);
        this.subDomain = subDomain;
        this.name = name;
        this.description = description;
        this.category = category;
        this.profileUrl = profileUrl;
        this.membersCount = 0;
        this.followersCount = 0;
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

    public void updateCategory(OrganizationCategory category) {
        this.category = category;
    }

    public void addAdmin(Long memberId) {
        OrganizationMemberMapper organizationMemberMapper = OrganizationMemberMapper.newAdmin(this, memberId);
        this.organizationMemberMapperList.add(organizationMemberMapper);
        this.membersCount++;
    }

    public void addUser(Long memberId) {
        OrganizationMemberMapper organizationMemberMapper = OrganizationMemberMapper.newUser(this, memberId);
        this.organizationMemberMapperList.add(organizationMemberMapper);
        this.membersCount++;
    }

    public void addPending(Long memberId) {
        validateIsMemberOrPendingInOrganization(memberId);
        OrganizationMemberMapper organizationMemberMapper = OrganizationMemberMapper.newPending(this, memberId);
        this.organizationMemberMapperList.add(organizationMemberMapper);
    }

    public void approvePendingMember(Long memberId) {
        OrganizationMemberMapper organizationMemberMapper = findPendingMember(memberId);
        organizationMemberMapper.approveToUser();
        this.membersCount++;
    }

    public void denyPendingMember(Long memberId) {
        OrganizationMemberMapper organizationMemberMapper = findPendingMember(memberId);
        organizationMemberMapperList.remove(organizationMemberMapper);
    }

    public void removeUser(Long memberId) {
        OrganizationMemberMapper organizationMemberMapper = findMember(memberId);
        organizationMemberMapper.validateCanRemove(memberId);
        organizationMemberMapperList.remove(organizationMemberMapper);
        this.membersCount--;
    }

    public void validateAdminMember(Long memberId) {
        if (!isAdmin(memberId)) {
            throw new ForbiddenException(String.format("멤버 (%s)는 그룹(%s)의 관리자가 아닙니다", memberId, subDomain), "그룹의 관리자만이 할 수 있습니다.");
        }
    }

    private boolean isAdmin(Long memberId) {
        return this.organizationMemberMapperList.stream()
            .anyMatch(organizationMemberMapper -> organizationMemberMapper.isAdmin(memberId));
    }

    private void validateIsMemberOrPendingInOrganization(Long memberId) {
        if (isMemberOrPendingInOrganization(memberId)) {
            throw new ConflictException(String.format("이미 그룹(%s)에 가입되거나 가입신청 한 유저(%s)입니다.", subDomain, memberId), "이미 그룹에 가입하거나 가입 신청하였습니다.");
        }
    }

    private boolean isMemberOrPendingInOrganization(Long memberId) {
        return this.organizationMemberMapperList.stream()
            .anyMatch(organizationMemberMapper -> organizationMemberMapper.isSameMember(memberId));
    }

    public void validateIsMemberInOrganization(Long memberId) {
        if (!isMemberInOrganization(memberId)) {
            throw new ForbiddenException(String.format("해당하는 멤버 (%s)는 그룹 (%s)의 소속이 아닙니다", memberId, this.subDomain), "회원은 그룹의 소속이 아닙니다.");
        }
    }

    private boolean isMemberInOrganization(Long memberId) {
        return this.organizationMemberMapperList.stream()
            .anyMatch(organizationMemberMapper -> organizationMemberMapper.isBelongToOrganization(memberId));
    }

    private OrganizationMemberMapper findPendingMember(Long memberId) {
        return organizationMemberMapperList.stream()
            .filter(mapper -> mapper.isPending(memberId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("해당하는 멤버 (%s)는 그룹 (%s)에 신청 한 상태가 아닙니다", memberId, subDomain)));
    }

    private OrganizationMemberMapper findMember(Long memberId) {
        return organizationMemberMapperList.stream()
            .filter(mapper -> mapper.isBelongToOrganization(memberId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("해당하는 멤버 (%s)는 그룹 (%s)의 소속 멤버가 아닙니다.", memberId, subDomain)));
    }

    public List<Long> getMemberIds() {
        return this.organizationMemberMapperList.stream()
            .map(OrganizationMemberMapper::getMemberId)
            .collect(Collectors.toList());
    }

    public OrganizationRole getRoleOfMember(Long memberId) {
        return findMember(memberId).getRole();
    }

    public void addFollow(Long memberId) {
        this.organizationFollowerList.add(OrganizationFollower.newFollow(this, memberId));
        this.followersCount++;
    }

    public void unFollow(Long memberId, Organization organization) {
        OrganizationFollower followMember = this.findFollowMember(memberId, organization);
        organizationFollowerList.remove(followMember);
        followersCount--;
    }

    public OrganizationFollower findFollowMember(Long memberId, Organization organization) {
        return this.organizationFollowerList.stream()
            .filter(mapper -> mapper.isSameMember(memberId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("해당하는 조직(%s)에 팔로우한 멤버(%s)가 없습니다.", subDomain, memberId)));
    }

    public List<Long> getFollowIds() {
        return this.organizationFollowerList.stream()
            .map(OrganizationFollower::getMemberId)
            .collect(Collectors.toList());
    }

}
