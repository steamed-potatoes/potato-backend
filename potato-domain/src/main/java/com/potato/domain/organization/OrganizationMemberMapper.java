package com.potato.domain.organization;

import com.potato.domain.BaseTimeEntity;
import com.potato.exception.ForbiddenException;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrganizationMemberMapper extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganizationRole role;

    private OrganizationMemberMapper(Organization organization, Long memberId, OrganizationRole role) {
        this.organization = organization;
        this.memberId = memberId;
        this.role = role;
    }

    static OrganizationMemberMapper newAdmin(Organization organization, Long memberId) {
        return new OrganizationMemberMapper(organization, memberId, OrganizationRole.ADMIN);
    }

    static OrganizationMemberMapper newUser(Organization organization, Long memberId) {
        return new OrganizationMemberMapper(organization, memberId, OrganizationRole.USER);
    }

    static OrganizationMemberMapper newPending(Organization organization, Long memberId) {
        return new OrganizationMemberMapper(organization, memberId, OrganizationRole.PENDING);
    }

    boolean isAdmin(Long memberId) {
        return this.memberId.equals(memberId) && this.role.equals(OrganizationRole.ADMIN);
    }

    boolean isUser(Long memberId) {
        return this.memberId.equals(memberId) && this.role.equals(OrganizationRole.USER);
    }

    boolean isPending(Long memberId) {
        return this.memberId.equals(memberId) && this.role.equals(OrganizationRole.PENDING);
    }

    boolean isBelongToOrganization(Long memberId) {
        return isAdmin(memberId) || isUser(memberId);
    }

    void approveToUser() {
        if (!isPending(memberId)) {
            throw new NotFoundException(String.format("멤버 (%s)는 조직 (%s)의 가입신청자가 아닙니다", memberId, organization.getSubDomain()), "회원은 조직에 가입하지 않았습니다.");
        }
        this.role = OrganizationRole.USER;
    }

    void validateCanRemove(Long memberId) {
        if (isAdmin(memberId)) {
            throw new ForbiddenException(String.format("그룹 (%s)의 관리자 (%s)는 그룹에서 탈퇴할 수 없습니다", this.organization.getSubDomain(), memberId), "관리자는 그룹에서 탈퇴할 수 없습니다.");
        }
    }

    boolean isSameMember(Long memberId) {
        return this.memberId.equals(memberId);
    }

    void appointAdmin() {
        this.role = OrganizationRole.ADMIN;
    }

    void disappointAdmin() {
        this.role = OrganizationRole.USER;
    }

}
