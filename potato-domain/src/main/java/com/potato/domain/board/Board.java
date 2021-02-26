package com.potato.domain.board;

import com.potato.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Visible visible;

    @Column(nullable = false)
    private String title;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long organizationId;

}
