package com.potato.domain.organization;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationCreator {

    public static Organization create(String subDomain) {
        return Organization.builder()
            .subDomain(subDomain)
            .name("조직")
            .category(OrganizationCategory.NON_APPROVED_CIRCLE)
            .build();
    }

}
