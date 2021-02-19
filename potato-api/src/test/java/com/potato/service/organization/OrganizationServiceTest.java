package com.potato.service.organization;

import com.potato.domain.organization.*;
import com.potato.exception.ConflictException;
import com.potato.exception.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.potato.service.organization.OrganizationServiceTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrganizationServiceTest extends MemberSetupTest {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMemberMapperRepository organizationMemberMapperRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationMemberMapperRepository.deleteAllInBatch();
        organizationRepository.deleteAllInBatch();
    }

    @Test
    void 새로운_조직을_생성한다() {
        // given
        String subDomain = "potato";
        String name = "찐 감자";
        String description = "개발 동아리 입니다";
        String profileUrl = "http://image.com";

        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain(subDomain)
            .name(name)
            .description(description)
            .profileUrl(profileUrl)
            .build();

        // when
        organizationService.createOrganization(request, memberId);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertOrganization(organizationList.get(0), subDomain, name, description, profileUrl);
    }

    @Test
    void 조직_생성시_서브_도메인이_중복되면_에러가_발생한다() {
        // given
        String subDomain = "potato";
        organizationRepository.save(OrganizationCreator.create(subDomain));

        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain(subDomain)
            .name("찐감자")
            .build();

        // when & then
        assertThatThrownBy(() -> {
            organizationService.createOrganization(request, memberId);
        }).isInstanceOf(ConflictException.class);
    }

    @Test
    void 조직을_생성하면_기본적으로_비인준_동아리가_된다() {
        // given
        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain("potato")
            .name("찐 감자")
            .build();

        // when
        organizationService.createOrganization(request, memberId);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertThat(organizationList.get(0).getCategory()).isEqualTo(OrganizationCategory.NON_APPROVED_CIRCLE);
    }

    @Test
    void 새로운_조직을_생성하면_자동으로_관리자가_되고_회원수가_1이된다() {
        // given
        CreateOrganizationRequest request = CreateOrganizationRequest.testBuilder()
            .subDomain("potato")
            .name("찐 감자")
            .build();

        // when
        organizationService.createOrganization(request, memberId);

        // then
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList.get(0).getMembersCount()).isEqualTo(1);

        List<OrganizationMemberMapper> organizationMemberMapperList = organizationMemberMapperRepository.findAll();
        assertThat(organizationList).hasSize(1);
        assertOrganizationMemberMapper(organizationMemberMapperList.get(0), memberId, OrganizationRole.ADMIN);
    }

    @Test
    void 서브도메인을_통해_특정_조직의_간단한_정보를_불러온다() {
        // given
        String subDomain = "potato";
        String name = "찐 감자";
        String description = "개발 동아리 입니다";
        String profileUrl = "http://image.com";
        OrganizationCategory category = OrganizationCategory.NON_APPROVED_CIRCLE;

        Organization organization = OrganizationCreator.create(subDomain, name, description, profileUrl, category);
        organizationRepository.save(organization);

        // when
        OrganizationInfoResponse response = organizationService.getSimpleOrganizationInfo(subDomain);

        // then
        assertOrganizationInfoResponse(response, organization.getId(), subDomain, name, description, profileUrl, category, organization.getMembersCount());
    }

    @Test
    void 특정_조직을_조회시_해당하는_서브도메인이_없는경우() {
        // when & then
        assertThatThrownBy(() -> {
            organizationService.getSimpleOrganizationInfo("empty");
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 조직_리스트를_불러온다() {
        // given
        organizationRepository.saveAll(Arrays.asList(
            OrganizationCreator.create("고예림"), OrganizationCreator.create("유순조"), OrganizationCreator.create("강승호")
        ));

        // when
        List<OrganizationInfoResponse> responses = organizationService.getOrganizationsInfo();

        // then
        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).getSubDomain()).isEqualTo("고예림");
        assertThat(responses.get(1).getSubDomain()).isEqualTo("유순조");
        assertThat(responses.get(2).getSubDomain()).isEqualTo("강승호");
    }

    @Test
    void 조직_리스트를_불러온다_아무_조직도_없다() {
        // when
        List<OrganizationInfoResponse> responses = organizationService.getOrganizationsInfo();

        // then
        assertThat(responses).isEmpty();
    }

}
