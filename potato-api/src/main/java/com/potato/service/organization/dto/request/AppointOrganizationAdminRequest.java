package com.potato.service.organization.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppointOrganizationAdminRequest {

    @NotNull
    private Long targetMemberId;

}
