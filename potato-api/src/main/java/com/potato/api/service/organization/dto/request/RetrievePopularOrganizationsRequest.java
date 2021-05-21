package com.potato.api.service.organization.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrievePopularOrganizationsRequest {

    @Min(1)
    @NotNull
    private int size;

    public static RetrievePopularOrganizationsRequest testInstance(int size) {
        return new RetrievePopularOrganizationsRequest(size);
    }

}
