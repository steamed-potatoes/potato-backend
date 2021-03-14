package com.potato.domain.boardV2;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.boardV2.admin.AdminBoard;
import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.potato.domain.common.DateTimeInterval;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoardV2 extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String title;

    @Embedded
    private DateTimeInterval dateTimeInterval;

    @Builder
    private BoardV2(Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.memberId = memberId;
        this.title = title;
        this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
    }

    public static BoardV2 of(Long memberId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return BoardV2.builder()
            .memberId(memberId)
            .title(title)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .build();
    }

}
