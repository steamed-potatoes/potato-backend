package com.potato.domain.domain.common.delete;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@EqualsAndHashCode
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

}
