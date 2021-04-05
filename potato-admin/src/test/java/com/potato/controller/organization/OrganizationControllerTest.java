package com.potato.controller.organization;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.domain.member.Member;
import com.potato.domain.member.MemberCreator;
import com.potato.domain.member.MemberRepository;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;
import com.potato.domain.organization.OrganizationCreator;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.dto.request.UpdateCategoryRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
public class OrganizationControllerTest extends ControllerTestUtils {

    private OrganizationMockMvc organizationMockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @BeforeEach
    void setUp() {
        super.setup();
        organizationMockMvc = new OrganizationMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        memberRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    void 그룹들_리스트_불러오는_경우() throws Exception {
        //given
        String token = administratorMockMvc.getMockAdminToken();

        String email1 = "tnswh1@naver.com";
        String email2 = "tnswh2@gmail.com";
        Member member1 = MemberCreator.create(email1);
        Member member2 = MemberCreator.create(email2);
        memberRepository.saveAll(Arrays.asList(member1, member2));

        String subDomain1 = "potato";
        Organization organization1 = OrganizationCreator.create(subDomain1);
        organization1.addAdmin(member1.getId());
        String subDomain2 = "potato2";
        Organization organization2 = OrganizationCreator.create(subDomain2);
        organization2.addAdmin(member1.getId());
        organizationRepository.saveAll(Arrays.asList(organization1, organization2));

        //when
        ApiResponse<List<OrganizationInfoResponse>> response = organizationMockMvc.retrieveOrganization(token, 200);

        //then
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getSubDomain()).isEqualTo(subDomain1);
        assertThat(response.getData().get(1).getSubDomain()).isEqualTo(subDomain2);
    }

    @Test
    void 비인준_그룹을_인준그룹으로_바꾼다() throws Exception {
        //given
        String token = administratorMockMvc.getMockAdminToken();

        Organization organization = OrganizationCreator.create("potato", "감자", "설명", "http://profile.com", OrganizationCategory.NON_APPROVED_CIRCLE);
        organization.addAdmin(1L);
        organizationRepository.save(organization);

        UpdateCategoryRequest request = UpdateCategoryRequest.testInstance(OrganizationCategory.NON_APPROVED_CIRCLE);

        //when
        ApiResponse<OrganizationInfoResponse> response = organizationMockMvc.changeCategoryToApproved(organization.getSubDomain(), request, token, 200);

        //then
        assertThat(response.getData().getSubDomain()).isEqualTo(organization.getSubDomain());
        assertThat(response.getData().getCategory()).isEqualTo(OrganizationCategory.APPROVED_CIRCLE);
    }

}