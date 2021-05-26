package com.potato.domain.domain.organization;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Test Helper Class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationCreator {

    public static Organization create(String subDomain) {
        return Organization.builder()
            .subDomain(subDomain)
            .name("조직")
            .category(OrganizationCategory.NON_APPROVED_CIRCLE)
            .build();
    }

    public static Organization create(String subDomain, OrganizationCategory category) {
        return Organization.builder()
            .subDomain(subDomain)
            .name("조직")
            .category(category)
            .build();
    }

    public static Organization create(String subDomain, String name, String description, String profileUrl, OrganizationCategory category) {
        return Organization.builder()
            .subDomain(subDomain)
            .name(name)
            .description(description)
            .profileUrl(profileUrl)
            .category(category)
            .build();
    }

    public static Organization createNonApproved(String subDomain, String name) {
        return Organization.builder()
            .subDomain(subDomain)
            .name(name)
            .description("비인준 동아리입니다")
            .category(OrganizationCategory.NON_APPROVED_CIRCLE)
            .build();
    }

    public static Organization createApproved(String subDomain, String name) {
        return Organization.builder()
            .subDomain(subDomain)
            .name(name)
            .description("인준 동아리입니다")
            .category(OrganizationCategory.APPROVED_CIRCLE)
            .build();
    }

}
