package com.potato.domain.follow;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.member.Member;

import javax.persistence.*;

@Entity
public class Follow extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Member follow;

    private String subDomain;

    private Boolean isDeleted;

}
