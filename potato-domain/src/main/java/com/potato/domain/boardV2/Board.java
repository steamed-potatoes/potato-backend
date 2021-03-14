package com.potato.domain.boardV2;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.common.DateTimeInterval;
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

    private Long memberId;

    private String title;

    private String content;

    private String imageUrl;

    @Embedded
    private DateTimeInterval dateTimeInterval;

}
