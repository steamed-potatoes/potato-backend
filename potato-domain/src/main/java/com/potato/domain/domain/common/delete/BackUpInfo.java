package com.potato.domain.domain.common.delete;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BackUpInfo {

    @Column(nullable = false)
    private Long backUpId;

    @Column(nullable = false)
    private LocalDateTime backUpCreatedDateTime;

    private BackUpInfo(Long backUpId, LocalDateTime backUpCreatedDateTime) {
        this.backUpId = backUpId;
        this.backUpCreatedDateTime = backUpCreatedDateTime;
    }

    public static BackUpInfo of(Long backUpId, LocalDateTime backUpCreatedDateTime) {
        return new BackUpInfo(backUpId, backUpCreatedDateTime);
    }

}
