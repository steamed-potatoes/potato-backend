package com.potato.service.organization;

import com.potato.domain.organization.*;
import com.potato.exception.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.organization.dto.response.OrganizationDetailInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.potato.service.organization.OrganizationServiceTestUtils.assertOrganizationInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrganizationRetrieveServiceTest extends MemberSetupTest {

    @Autowired
    private OrganizationRetrieveService organizationRetrieveService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationRepository.deleteAll();
    }

    private String subDomain = "potato";

    @Test
    void 서브도메인을_통해_특정_조직의_간단한_정보를_불러온다() {
        // given
        String name = "찐 감자";
        String description = "개발 동아리 입니다";
        String profileUrl = "http://image.com";
        OrganizationCategory category = OrganizationCategory.NON_APPROVED_CIRCLE;

        Organization organization = OrganizationCreator.create(subDomain, name, description, profileUrl, category);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        // when
        OrganizationDetailInfoResponse response = organizationRetrieveService.getDetailOrganizationInfo(subDomain);

        // then
        assertOrganizationInfoResponse(response, organization.getId(), subDomain, name, description, profileUrl, category, organization.getMembersCount());
    }

    @Test
    void 특정_그룹을_조회시_해당하는_그룹이_없는경우() {
        // when & then
        assertThatThrownBy(
            () -> organizationRetrieveService.getDetailOrganizationInfo(subDomain)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_리스트를_불러온다() {
        // given
        Organization organization1 = OrganizationCreator.create(subDomain);
        organization1.addAdmin(memberId);

        Organization organization2 = OrganizationCreator.create("subDomain2");
        organization2.addAdmin(memberId);

        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        // when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.getOrganizationsInfo();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getSubDomain()).isEqualTo(organization1.getSubDomain());
        assertThat(responses.get(1).getSubDomain()).isEqualTo(organization2.getSubDomain());
    }

    @Test
    void 조직_리스트를_불러온다_아무_조직도_없다() {
        // when
        List<OrganizationInfoResponse> responses = organizationRetrieveService.getOrganizationsInfo();

        // then
        assertThat(responses).isEmpty();
    }

    @Test
    void 내가_속한_조직의_리스트를_모두_불러온다() {
        // given
        Organization organization1 = OrganizationCreator.create(subDomain);
        organization1.addAdmin(memberId);

        String subDomain2 = "subDomain2";
        Organization organization2 = OrganizationCreator.create(subDomain2);
        organization2.addUser(memberId);

        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        // when
        List<OrganizationInfoResponse> organizationInfoResponses = organizationRetrieveService.getMyOrganizationsInfo(memberId);

        // then
        assertThat(organizationInfoResponses).hasSize(2);
        assertThat(organizationInfoResponses.get(0).getSubDomain()).isEqualTo(subDomain);
        assertThat(organizationInfoResponses.get(1).getSubDomain()).isEqualTo(subDomain2);
    }

    @Test
    void 내가_속한_조직의_리스트를_모두_불러올때_가입신청된_조직은_포함되지_않는다() {
        // given
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addPending(memberId);

        organizationRepository.save(organization);

        // when
        List<OrganizationInfoResponse> organizationInfoResponses = organizationRetrieveService.getMyOrganizationsInfo(memberId);

        // then
        assertThat(organizationInfoResponses).isEmpty();
    }

}
