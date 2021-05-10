package com.potato.domain.common.delete;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackUpInfo that = (BackUpInfo) o;
        return Objects.equals(backUpId, that.backUpId) && Objects.equals(backUpCreatedDateTime, that.backUpCreatedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backUpId, backUpCreatedDateTime);
    }

}
