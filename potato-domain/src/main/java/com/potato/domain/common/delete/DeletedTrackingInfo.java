package com.potato.domain.common.delete;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DeletedTrackingInfo {

    private Long deletedMemberId;

    private Long deletedAdminMemberId;

    private DeletedTrackingInfo(Long deletedMemberId, Long deletedAdminMemberId) {
        this.deletedMemberId = deletedMemberId;
        this.deletedAdminMemberId = deletedAdminMemberId;
    }

    public static DeletedTrackingInfo of(Long deletedMemberId, Long deletedAdminMemberId) {
        return new DeletedTrackingInfo(deletedMemberId, deletedAdminMemberId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeletedTrackingInfo that = (DeletedTrackingInfo) o;
        return Objects.equals(deletedMemberId, that.deletedMemberId) && Objects.equals(deletedAdminMemberId, that.deletedAdminMemberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deletedMemberId, deletedAdminMemberId);
    }

}
