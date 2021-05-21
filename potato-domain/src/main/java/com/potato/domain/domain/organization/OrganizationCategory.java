package com.potato.domain.domain.organization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrganizationCategory {

    APPROVED_CIRCLE("인준 동아리"),
    NON_APPROVED_CIRCLE("비인준 동아리");

    private final String name;

}
