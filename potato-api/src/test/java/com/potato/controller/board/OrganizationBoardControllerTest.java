package com.potato.controller.board;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardType;
import com.potato.domain.member.Member;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCreator;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.board.organization.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.board.organization.dto.request.RetrieveLatestBoardsRequest;
import com.potato.service.board.organization.dto.request.UpdateOrganizationBoardRequest;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.service.board.organization.dto.response.OrganizationBoardWithCreatorInfoResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
class OrganizationBoardControllerTest extends ControllerTestUtils {

    private OrganizationBoardMockMvc organizationBoardMockMvc;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @BeforeEach
    void setUp() {
        super.setup();
        organizationBoardMockMvc = new OrganizationBoardMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    void 그룹의_관리자가_새로운_그룹의_게시물을_등록하는_경우() throws Exception {
        //given
        String email = "tnswh2023@gmail.com";
        String token = memberMockMvc.getMockMemberToken(email);
        Member member = memberRepository.findMemberByEmail(email);

        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(member.getId());
        organizationRepository.save(organization);

        String title = "title";
        String content = "content";
        LocalDateTime startTime = LocalDateTime.of(2021, 3, 30, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2021, 3, 31, 0, 0);
        CreateOrganizationBoardRequest request = CreateOrganizationBoardRequest.testBuilder()
            .title(title)
            .content(content)
            .startDateTime(startTime)
            .endDateTime(endTime)
            .type(OrganizationBoardType.RECRUIT)
            .build();

        //when
        ApiResponse<OrganizationBoardInfoResponse> response = organizationBoardMockMvc.createOrganizationBoard(subDomain, request, token, 200);

        //then
        assertThat(response.getData().getTitle()).isEqualTo(title);
        assertThat(response.getData().getContent()).isEqualTo(content);
        assertThat(response.getData().getStartDateTime()).isEqualTo(startTime);
        assertThat(response.getData().getEndDateTime()).isEqualTo(endTime);
        assertThat(response.getData().getType()).isEqualTo(OrganizationBoardType.RECRUIT);
    }

    @Test
    void 특정그룹의_게시물을_조회하는_경우() throws Exception {
        //given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        String title = "title";
        OrganizationBoard board = OrganizationBoardCreator.create(subDomain, testMember.getId(), title, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(board);

        //when
        ApiResponse<OrganizationBoardWithCreatorInfoResponse> response = organizationBoardMockMvc.retrieveOrganizationBoard(board.getId(), 200);

        //then
        assertThat(response.getData().getBoard().getTitle()).isEqualTo(title);
        assertThat(response.getData().getCreator().getEmail()).isEqualTo(testMember.getEmail());
        assertThat(response.getData().getOrganization().getSubDomain()).isEqualTo(subDomain);
    }

    @Test
    void 그룹의_게시물을_스크롤_페이지네이션_기반_최신_3개_조회하는_경우() throws Exception {
        //given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        String title1 = "title1";
        String title2 = "title2";
        String title3 = "title3";
        OrganizationBoard board1 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title1, OrganizationBoardType.RECRUIT);
        OrganizationBoard board2 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title2, OrganizationBoardType.RECRUIT);
        OrganizationBoard board3 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title3, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.saveAll(Arrays.asList(board1, board2, board3));

        //when
        ApiResponse<List<OrganizationBoardInfoResponse>> response = organizationBoardMockMvc.retrieveLatestOrganizationBoardList(0, 3, 200);

        //then
        assertThat(response.getData().get(0).getTitle()).isEqualTo(title3);
        assertThat(response.getData().get(1).getTitle()).isEqualTo(title2);
        assertThat(response.getData().get(2).getTitle()).isEqualTo(title1);
    }

    @Test
    void 그룹의_게시물을_스크롤_페이지네이션_기반_최근게시글1개_있고_2개_조회하는_경우() throws Exception {
        //given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        String title1 = "title1";
        String title2 = "title2";
        String title3 = "title3";
        OrganizationBoard board1 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title1, OrganizationBoardType.RECRUIT);
        OrganizationBoard board2 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title2, OrganizationBoardType.RECRUIT);
        OrganizationBoard board3 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title3, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.saveAll(Arrays.asList(board1, board2, board3));

        //when
        ApiResponse<List<OrganizationBoardInfoResponse>> response = organizationBoardMockMvc.retrieveLatestOrganizationBoardList(board3.getId(), 2, 200);

        //then
        assertThat(response.getData().get(0).getTitle()).isEqualTo(title2);
        assertThat(response.getData().get(1).getTitle()).isEqualTo(title1);
    }

    @Test
    void 게시글이_없을_경우_빈배열_반환() throws Exception {
        //given

        //when
        ApiResponse<List<OrganizationBoardInfoResponse>> response = organizationBoardMockMvc.retrieveLatestOrganizationBoardList(0, 3, 200);

        //then
        assertThat(response.getData()).isEmpty();
    }

    @Test
    void 그룹_관리자가_그룹의_게시물을_수정하는_경우() throws Exception {
        //given
        String email = "tnswh2023@gmail.com";
        String token = memberMockMvc.getMockMemberToken(email);
        Member member = memberRepository.findMemberByEmail(email);

        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(member.getId());
        organizationRepository.save(organization);

        String title = "title";
        OrganizationBoard board = OrganizationBoardCreator.create(subDomain, testMember.getId(), title, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(board);

        String updateTitle = "updateTitle";
        UpdateOrganizationBoardRequest request = UpdateOrganizationBoardRequest.testBuilder()
            .organizationBoardId(board.getId())
            .title(updateTitle)
            .startDateTime(LocalDateTime.of(2021, 3, 30, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 31, 0, 0))
            .type(OrganizationBoardType.RECRUIT)
            .build();

        //when
        ApiResponse<OrganizationBoardInfoResponse> response = organizationBoardMockMvc.updateOrganizationBoard(subDomain, request, token, 200);

        //then
        assertThat(response.getData().getTitle()).isEqualTo(updateTitle);
    }

}