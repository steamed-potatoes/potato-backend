package com.potato.api.service.organization.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppointOrganizationAdminRequest {

    @NotNull
    private Long targetMemberId;

}
