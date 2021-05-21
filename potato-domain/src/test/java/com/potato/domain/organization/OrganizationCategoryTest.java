package com.potato.domain.organization;

import com.potato.domain.domain.organization.OrganizationCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrganizationCategoryTest {

    @Test
    void Organization의_이름이_반한된다() {
        // given
        OrganizationCategory organizationCategory = OrganizationCategory.NON_APPROVED_CIRCLE;

        // when
        String name = organizationCategory.getName();

        // then
        assertThat(name).isEqualTo("비인준 동아리");
    }

}
