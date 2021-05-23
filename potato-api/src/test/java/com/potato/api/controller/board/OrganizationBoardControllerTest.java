package com.potato.api.controller.board;

import com.potato.api.controller.ApiResponse;
import com.potato.api.controller.AbstractControllerTest;
import com.potato.api.controller.board.api.OrganizationBoardMockMvc;
import com.potato.api.service.board.organization.dto.request.CreateOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.request.DeleteOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.request.LikeOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.request.UpdateOrganizationBoardRequest;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.domain.hashtag.BoardHashTagRepository;
import com.potato.domain.domain.image.BoardImageRepository;
import com.potato.domain.domain.member.Member;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCreator;
import com.potato.domain.domain.organization.OrganizationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationBoardControllerTest extends AbstractControllerTest {

    private OrganizationBoardMockMvc organizationBoardMockMvc;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private BoardHashTagRepository boardHashTagRepository;

    @Autowired
    private BoardImageRepository boardImageRepository;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        organizationBoardMockMvc = new OrganizationBoardMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAll();
        organizationRepository.deleteAll();
        boardHashTagRepository.deleteAll();
        boardImageRepository.deleteAll();
    }

    @DisplayName("POST /api/v2/organization/board 200 OK")
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
            .type(OrganizationBoardCategory.RECRUIT)
            .hashTags(Arrays.asList("감자", "고구마"))
            .imageUrlList(Collections.singletonList("https://profile.com"))
            .build();

        //when
        ApiResponse<OrganizationBoardInfoResponse> response = organizationBoardMockMvc.createOrganizationBoard(subDomain, request, token, 200);

        //then
        assertThat(response.getData().getTitle()).isEqualTo(title);
        assertThat(response.getData().getContent()).isEqualTo(content);
        assertThat(response.getData().getStartDateTime()).isEqualTo(startTime);
        assertThat(response.getData().getEndDateTime()).isEqualTo(endTime);
        assertThat(response.getData().getType()).isEqualTo(OrganizationBoardCategory.RECRUIT);
    }

    @DisplayName("PUT /api/v2/organization/board")
    @Test
    void 그룹_관리자가_그룹의_게시물을_수정하는_경우() throws Exception {
        //given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        String title = "title";
        OrganizationBoard board = OrganizationBoardCreator.create(subDomain, testMember.getId(), title, OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(board);

        String updateTitle = "updateTitle";
        UpdateOrganizationBoardRequest request = UpdateOrganizationBoardRequest.testBuilder()
            .organizationBoardId(board.getId())
            .title(updateTitle)
            .startDateTime(LocalDateTime.of(2021, 3, 30, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 31, 0, 0))
            .type(OrganizationBoardCategory.RECRUIT)
            .hashTags(Collections.emptyList())
            .imageUrlList(Collections.emptyList())
            .build();

        //when
        ApiResponse<OrganizationBoardInfoResponse> response = organizationBoardMockMvc.updateOrganizationBoard(subDomain, request, token, 200);

        //then
        assertThat(response.getData().getTitle()).isEqualTo(updateTitle);
    }

    @Test
    void 그룹_관리자가_게시한_게시물을_삭제한다() throws Exception {
        // given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, testMember.getId(), "게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        DeleteOrganizationBoardRequest request = DeleteOrganizationBoardRequest.testInstance(organizationBoard.getId());

        // when
        ApiResponse<String> response = organizationBoardMockMvc.deleteOrganizationBoard(subDomain, request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @Test
    void 그룹_게시물을_좋아요_누른다() throws Exception {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(organization.getSubDomain(), 999L, "이전의 게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        LikeOrganizationBoardRequest request = LikeOrganizationBoardRequest.testInstance(organizationBoard.getId());

        // when
        ApiResponse<String> response = organizationBoardMockMvc.addOrganizationBoardLiked(request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @Test
    void 그룹_게시물을_좋아요를_취소한다() throws Exception {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(organization.getSubDomain(), 999L, "이전의 게시글", OrganizationBoardCategory.RECRUIT);
        organizationBoard.addLike(testMember.getId());
        organizationBoardRepository.save(organizationBoard);

        LikeOrganizationBoardRequest request = LikeOrganizationBoardRequest.testInstance(organizationBoard.getId());

        // when
        ApiResponse<String> response = organizationBoardMockMvc.cancelOrganizationBoardLike(request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

}
