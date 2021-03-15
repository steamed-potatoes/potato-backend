package com.potato.domain.board.organization;

import com.potato.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrganizationBoardLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_board_id", nullable = false)
    private OrganizationBoard organizationBoard;

    @Column(nullable = false)
    private Long memberId;

    private OrganizationBoardLike(OrganizationBoard organizationBoard, Long memberId) {
        this.organizationBoard = organizationBoard;
        this.memberId = memberId;
    }

    public static OrganizationBoardLike of(OrganizationBoard organizationBoard, Long memberId) {
        return new OrganizationBoardLike(organizationBoard, memberId);
    }

    public boolean isSameEntity(Long memberId) {
        return this.memberId.equals(memberId);
    }

}
