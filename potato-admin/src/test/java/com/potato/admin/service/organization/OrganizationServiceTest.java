package com.potato.admin.service.organization;

import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationCreator;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.common.exception.model.NotFoundException;
import com.potato.admin.service.AdminSetupTest;
import com.potato.admin.service.organization.dto.request.UpdateCategoryRequest;
import com.potato.admin.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class OrganizationServiceTest extends AdminSetupTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationService organizationService;

    @AfterEach
    void cleanup() {
        super.cleanUp();
        organizationRepository.deleteAll();
    }

    @Test
    void 그룹_리스트들을_불러온다() {
        // given
        Organization organization1 = OrganizationCreator.create("subDomain1");
        Organization organization2 = OrganizationCreator.create("subDomain2");
        Organization organization3 = OrganizationCreator.create("subDomain3");
        organizationRepository.saveAll(Arrays.asList(organization1, organization2, organization3));

        // when
        List<OrganizationInfoResponse> responses = organizationService.retrieveOrganization();

        // then
        assertThat(responses).hasSize(3);
        assertOrganizationInfoResponse(responses.get(0), organization1.getId(), organization1.getSubDomain(), organization1.getName(),
            organization1.getDescription(), organization1.getProfileUrl(), organization1.getCategory(), organization1.getMembersCount());
        assertOrganizationInfoResponse(responses.get(1), organization2.getId(), organization2.getSubDomain(), organization2.getName(),
            organization2.getDescription(), organization2.getProfileUrl(), organization2.getCategory(), organization2.getMembersCount());
        assertOrganizationInfoResponse(responses.get(2), organization3.getId(), organization3.getSubDomain(), organization3.getName(),
            organization3.getDescription(), organization3.getProfileUrl(), organization3.getCategory(), organization3.getMembersCount());
    }

    @Test
    void 비인준_그룹을_인준그룹으로_변경해준다() {
        // given
        Organization organization = OrganizationCreator.create("potato", "감자", "설명", "https://profile.com", OrganizationCategory.NON_APPROVED_CIRCLE);
        organization.addAdmin(1L);
        organizationRepository.save(organization);

        UpdateCategoryRequest request = UpdateCategoryRequest.testInstance(OrganizationCategory.APPROVED_CIRCLE);

        // when
        organizationService.updateCategory(organization.getSubDomain(), request);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertThat(organizationList.get(0).getSubDomain()).isEqualTo(organization.getSubDomain());
        assertThat(organizationList.get(0).getCategory()).isEqualTo(OrganizationCategory.APPROVED_CIRCLE);
    }

    @Test
    void 비인준_그룹을_비인준그룹으로_변경요청하면_에러없이_유지된다() {
        // given
        Organization organization = OrganizationCreator.create("potato", "감자", "설명", "https://profile.com", OrganizationCategory.NON_APPROVED_CIRCLE);
        organization.addAdmin(1L);
        organizationRepository.save(organization);

        UpdateCategoryRequest request = UpdateCategoryRequest.testInstance(OrganizationCategory.NON_APPROVED_CIRCLE);

        // when
        organizationService.updateCategory(organization.getSubDomain(), request);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertThat(organizationList.get(0).getSubDomain()).isEqualTo(organization.getSubDomain());
        assertThat(organizationList.get(0).getCategory()).isEqualTo(OrganizationCategory.NON_APPROVED_CIRCLE);
    }

    @Test
    void 인준_그룹을_비인준그룹으로_변경한다() {
        // given
        Organization organization = OrganizationCreator.create("potato", "감자", "설명", "https://profile.com", OrganizationCategory.APPROVED_CIRCLE);
        organization.addAdmin(1L);
        organizationRepository.save(organization);

        UpdateCategoryRequest request = UpdateCategoryRequest.testInstance(OrganizationCategory.NON_APPROVED_CIRCLE);

        // when
        organizationService.updateCategory(organization.getSubDomain(), request);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertThat(organizationList.get(0).getSubDomain()).isEqualTo(organization.getSubDomain());
        assertThat(organizationList.get(0).getCategory()).isEqualTo(OrganizationCategory.NON_APPROVED_CIRCLE);
    }

    @Test
    void 그룹_카테고리_조회시_해당하는_그룹이_없을경우_에러발생() {
        // given
        UpdateCategoryRequest request = UpdateCategoryRequest.testInstance(OrganizationCategory.NON_APPROVED_CIRCLE);

        // when & then
        assertThatThrownBy(() -> organizationService.updateCategory("Empty", request)).isInstanceOf(NotFoundException.class);
    }

    private void assertOrganizationInfoResponse(OrganizationInfoResponse response, Long id, String subDomain, String name,
                                                String description, String profileUrl, OrganizationCategory category, int membersCount) {
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getSubDomain()).isEqualTo(subDomain);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getDescription()).isEqualTo(description);
        assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(response.getCategory()).isEqualTo(category);
        assertThat(response.getMembersCount()).isEqualTo(membersCount);
    }

}
