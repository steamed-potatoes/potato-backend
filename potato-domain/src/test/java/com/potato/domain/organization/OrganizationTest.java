package com.potato.domain.organization;

import com.potato.domain.domain.organization.Organization;
import com.potato.common.exception.model.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrganizationTest {

    @Test
    void SubDomain은_영문자_숫자로_이루어져야한다() {
        // given
        String subDomain = "potato1";

        // when
        Organization organization = Organization.defaultInstance(subDomain, "이름", null, null);

        // then
        assertThat(organization.getSubDomain()).isEqualTo(subDomain);
    }

    @Test
    void SubDomain은_한글로_이루어질_수_없다() {
        // given
        String subDomain = "감자";

        // when & then
        assertThatThrownBy(() -> Organization.defaultInstance(subDomain, "이름", null, null)).isInstanceOf(ValidationException.class);
    }

}
