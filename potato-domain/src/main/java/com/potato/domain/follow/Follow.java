package com.potato.domain.follow;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.organization.Organization;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    private Follow(Organization organization, Long memberId) {
        this.memberId = memberId;
        this.organization = organization;
    }

    public static Follow newFollow(Organization organization, Long memberId) {
        return new Follow(organization, memberId);
    }

}
