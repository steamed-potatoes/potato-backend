package com.potato.domain.organization;

import com.potato.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class OrganizationFollower extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    private OrganizationFollower(Organization organization, Long memberId) {
        this.memberId = memberId;
        this.organization = organization;
    }

    public static OrganizationFollower newFollow(Organization organization, Long memberId) {
        return new OrganizationFollower(organization, memberId);
    }

    public boolean isSameMember(Long memberId, Organization organization) {
        return this.memberId.equals(memberId) && this.organization.equals(organization);
    }

}
