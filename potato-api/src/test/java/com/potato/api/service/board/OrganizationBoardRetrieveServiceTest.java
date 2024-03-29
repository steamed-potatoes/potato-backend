package com.potato.api.service.board;

import com.potato.api.service.board.organization.dto.response.OrganizationBoardWithOrganizationResponse;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.api.service.OrganizationMemberSetUpTest;
import com.potato.api.service.board.organization.OrganizationBoardRetrieveService;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrganizationBoardRetrieveServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private OrganizationBoardRetrieveService organizationBoardService;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAll();
    }

    @DisplayName("게시물 조회시 카테고리가 지정되지 않으면 전체 카테고리 범위에서 N개가 조회된다")
    @Test
    void 게시물_조회시_카테고리가_정해지지_않으면_전체_카테고리에서_조회한다() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardCategory.EVENT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardCategory.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3));

        //when
        List<OrganizationBoardWithOrganizationResponse> responses = organizationBoardService.retrieveBoardsWithPagination(null, 0, 3);

        //then
        assertThat(responses).hasSize(3);
        assertOrganizationBoardWithOrganizationResponse(responses.get(0), organizationBoard3);
        assertOrganizationBoardWithOrganizationResponse(responses.get(1), organizationBoard2);
        assertOrganizationBoardWithOrganizationResponse(responses.get(2), organizationBoard1);
    }

    @DisplayName("게시물 조회시 마지막 게시물의 id가 따로 지정되지 않으면, 가장 최신의 게시물 N개가 조회된다.")
    @Test
    void 게시물_조회시_마지막_게시물의_PK_가_0으로_지정되면_가장_최신의_게시물을_조회한다() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardCategory.EVENT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2));

        //when
        List<OrganizationBoardWithOrganizationResponse> responses = organizationBoardService.retrieveBoardsWithPagination(null, 0, 1);

        //then
        assertThat(responses).hasSize(1);
        assertOrganizationBoardWithOrganizationResponse(responses.get(0), organizationBoard2);
    }

    @Test
    void 게시물_조회시_없을_경우_NULL이_아닌_빈리스트를_반환() {
        //given
        List<OrganizationBoardWithOrganizationResponse> responses = organizationBoardService.retrieveBoardsWithPagination(null, 0, 3);

        //then
        assertThat(responses).isEmpty();
        assertThat(responses).isNotNull();
    }

    @Test
    void 게시물_조회시_마지막_게시물의_PK_이전의_게시물_N개가_조회된다() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardCategory.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3));

        //when
        List<OrganizationBoardWithOrganizationResponse> responses = organizationBoardService.retrieveBoardsWithPagination(null, organizationBoard3.getId(), 1);

        //then
        assertThat(responses).hasSize(1);
        assertOrganizationBoardWithOrganizationResponse(responses.get(0), organizationBoard2);
    }

    @Test
    void 게시물_조회시_마지막_게시물의_PK_이전의_해당하는_카테고리의_게시물_N개가_조회된다() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardCategory.EVENT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardCategory.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3));

        //when
        List<OrganizationBoardWithOrganizationResponse> responses = organizationBoardService.retrieveBoardsWithPagination(OrganizationBoardCategory.RECRUIT, organizationBoard3.getId(), 1);

        //then
        assertThat(responses).hasSize(1);
        assertOrganizationBoardWithOrganizationResponse(responses.get(0), organizationBoard1);
    }

    @Test
    void 게시물_스크롤_페이지네이션하는데_더이상_게시물이_존재하지_않을경우() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard1);

        //when
        List<OrganizationBoardWithOrganizationResponse> responses = organizationBoardService.retrieveBoardsWithPagination(null, organizationBoard1.getId(), 3);

        //then
        assertThat(responses).isEmpty();
    }

    @Test
    void 인기있는_게시물_조회시_좋아요가_많은_순으로_조회한다() {
        // given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardCategory.RECRUIT);
        organizationBoard1.addLike(2L);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardCategory.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2));

        // when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrievePopularBoard(2);

        // then
        assertThat(responses).hasSize(2);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard1);
        assertOrganizationBoardInfo(responses.get(1), organizationBoard2);
    }

    @Test
    void 인기있는_게시물_조회시_좋아요수가_같으면_최신순부터_조회한다() {
        // given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "더 오래된 게시물", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "더 최신의 게시물", OrganizationBoardCategory.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2));

        // when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrievePopularBoard(3);

        // then
        assertThat(responses).hasSize(2);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard2);
        assertOrganizationBoardInfo(responses.get(1), organizationBoard1);
    }

    @Test
    void 인기있는_게시물_조회시_SIZE_로_넘어온_수만큼_조회된다() {
        // given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "게시물1", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "게시물2", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "게시물3", OrganizationBoardCategory.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3));

        // when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrievePopularBoard(2);

        // then
        assertThat(responses).hasSize(2);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard3);
        assertOrganizationBoardInfo(responses.get(1), organizationBoard2);
    }

    private void assertOrganizationBoardWithOrganizationResponse(OrganizationBoardWithOrganizationResponse boardWithOrganizationDto, OrganizationBoard organizationBoard) {
        assertThat(boardWithOrganizationDto.getBoardId()).isEqualTo(organizationBoard.getId());
        assertThat(boardWithOrganizationDto.getOrgSubDomain()).isEqualTo(organizationBoard.getSubDomain());
        assertThat(boardWithOrganizationDto.getBoardTitle()).isEqualTo(organizationBoard.getTitle());
        assertThat(boardWithOrganizationDto.getBoardStartDateTime()).isEqualTo(organizationBoard.getStartDateTime());
        assertThat(boardWithOrganizationDto.getBoardEndDateTime()).isEqualTo(organizationBoard.getEndDateTime());
        assertThat(boardWithOrganizationDto.getBoardCategory()).isEqualTo(organizationBoard.getCategory());
    }

    private void assertOrganizationBoardInfo(OrganizationBoardInfoResponse organizationBoardInfoResponse, OrganizationBoard organizationBoard) {
        assertThat(organizationBoardInfoResponse.getId()).isEqualTo(organizationBoard.getId());
        assertThat(organizationBoardInfoResponse.getSubDomain()).isEqualTo(organizationBoard.getSubDomain());
        assertThat(organizationBoardInfoResponse.getTitle()).isEqualTo(organizationBoard.getTitle());
        assertThat(organizationBoardInfoResponse.getStartDateTime()).isEqualTo(organizationBoard.getStartDateTime());
        assertThat(organizationBoardInfoResponse.getEndDateTime()).isEqualTo(organizationBoard.getEndDateTime());
        assertThat(organizationBoardInfoResponse.getType()).isEqualTo(organizationBoard.getCategory());
    }

}
