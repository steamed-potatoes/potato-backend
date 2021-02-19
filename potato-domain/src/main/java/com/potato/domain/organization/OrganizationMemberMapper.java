package com.potato.domain.organization;

import com.potato.domain.BaseTimeEntity;
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

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private OrganizationRole role;

    private OrganizationMemberMapper(Organization organization, Long memberId, OrganizationRole role) {
        this.organization = organization;
        this.memberId = memberId;
        this.role = role;
    }

    public static OrganizationMemberMapper newAdmin(Organization organization, Long memberId) {
        return new OrganizationMemberMapper(organization, memberId, OrganizationRole.ADMIN);
    }

    public boolean isAdmin(Long memberId) {
        return this.memberId.equals(memberId) && this.role.equals(OrganizationRole.ADMIN);
    }

    public static OrganizationMemberMapper newUser(Organization organization, Long memberId) {
        return new OrganizationMemberMapper(organization, memberId, OrganizationRole.USER);
    }

    public static OrganizationMemberMapper newPending(Organization organization, Long memberId) {
        return new OrganizationMemberMapper(organization, memberId, OrganizationRole.PENDING);
    }

    public boolean isPending(Long memberId) {
        return this.memberId.equals(memberId) && this.role.equals(OrganizationRole.PENDING);
    }

    public void approve() {
        this.role = OrganizationRole.USER;
    }

    public boolean isSameMember(Long memberId) {
        return this.memberId.equals(memberId);
    }

}
