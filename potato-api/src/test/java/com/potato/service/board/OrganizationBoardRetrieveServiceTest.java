package com.potato.service.board;

import com.potato.domain.board.organization.*;
import com.potato.domain.board.organization.repository.dto.BoardWithOrganizationDto;
import com.potato.service.OrganizationMemberSetUpTest;
import com.potato.service.board.organization.OrganizationBoardRetrieveService;
import com.potato.service.board.organization.dto.request.RetrieveImminentBoardsRequest;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

    @Test
    void 가장_최신_게시물_3개_불러온다() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3));

        //when
        List<BoardWithOrganizationDto> responses = organizationBoardService.retrieveBoardsWithPagination(null, 0, 3);

        //then
        assertThat(responses).hasSize(3);
        assertOrganizationBoardWithCreatorInfo(responses.get(0), organizationBoard3.getId(), organizationBoard3.getTitle(), organizationBoard3.getStartDateTime(),
            organizationBoard3.getEndDateTime(), organizationBoard3.getSubDomain(), organizationBoard3.getType());
        assertOrganizationBoardWithCreatorInfo(responses.get(1), organizationBoard2.getId(), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
        assertOrganizationBoardWithCreatorInfo(responses.get(2), organizationBoard1.getId(), organizationBoard1.getTitle(), organizationBoard1.getStartDateTime(),
            organizationBoard1.getEndDateTime(), organizationBoard1.getSubDomain(), organizationBoard1.getType());
    }

    @Test
    void 가장_최신_게시물_3개_불러올때_게시물이_없을_경우_빈리스트_반환() {
        //given
        List<BoardWithOrganizationDto> responses = organizationBoardService.retrieveBoardsWithPagination(null, 0, 3);

        //then
        assertThat(responses).isEmpty();
    }

    @DisplayName("게시물 4 이후부터 3개 조회했으니 [3, 2, 1]이 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_1() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard4 = OrganizationBoardCreator.create(subDomain, memberId, "title4", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard5 = OrganizationBoardCreator.create(subDomain, memberId, "title5", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3, organizationBoard4, organizationBoard5));

        //when
        List<BoardWithOrganizationDto> responses = organizationBoardService.retrieveBoardsWithPagination(null, organizationBoard4.getId(), 3);

        //then
        assertThat(responses).hasSize(3);
        assertOrganizationBoardWithCreatorInfo(responses.get(0), organizationBoard3.getId(), organizationBoard3.getTitle(), organizationBoard3.getStartDateTime(),
            organizationBoard3.getEndDateTime(), organizationBoard3.getSubDomain(), organizationBoard3.getType());
        assertOrganizationBoardWithCreatorInfo(responses.get(1), organizationBoard2.getId(), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
        assertOrganizationBoardWithCreatorInfo(responses.get(2), organizationBoard1.getId(), organizationBoard1.getTitle(), organizationBoard1.getStartDateTime(),
            organizationBoard1.getEndDateTime(), organizationBoard1.getSubDomain(), organizationBoard1.getType());
    }

    @DisplayName("게시물 4 이후부터 2개 조회했으니 [3, 2]이 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_2() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard4 = OrganizationBoardCreator.create(subDomain, memberId, "title4", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard5 = OrganizationBoardCreator.create(subDomain, memberId, "title5", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3, organizationBoard4, organizationBoard5));

        //when
        List<BoardWithOrganizationDto> responses = organizationBoardService.retrieveBoardsWithPagination(null, organizationBoard4.getId(), 2);

        //then
        assertThat(responses).hasSize(2);
        assertOrganizationBoardWithCreatorInfo(responses.get(0), organizationBoard3.getId(), organizationBoard3.getTitle(), organizationBoard3.getStartDateTime(),
            organizationBoard3.getEndDateTime(), organizationBoard3.getSubDomain(), organizationBoard3.getType());
        assertOrganizationBoardWithCreatorInfo(responses.get(1), organizationBoard2.getId(), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
    }

    @DisplayName("게시물 5 이후부터 3개 조회했으니 [4, 3, 2]이 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_3() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard4 = OrganizationBoardCreator.create(subDomain, memberId, "title4", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard5 = OrganizationBoardCreator.create(subDomain, memberId, "title5", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3, organizationBoard4, organizationBoard5));

        //when
        List<BoardWithOrganizationDto> responses = organizationBoardService.retrieveBoardsWithPagination(null, organizationBoard5.getId(), 3);

        //then
        assertThat(responses).hasSize(3);
        assertOrganizationBoardWithCreatorInfo(responses.get(0), organizationBoard4.getId(), organizationBoard4.getTitle(), organizationBoard4.getStartDateTime(),
            organizationBoard4.getEndDateTime(), organizationBoard4.getSubDomain(), organizationBoard4.getType());
        assertOrganizationBoardWithCreatorInfo(responses.get(1), organizationBoard3.getId(), organizationBoard3.getTitle(), organizationBoard3.getStartDateTime(),
            organizationBoard3.getEndDateTime(), organizationBoard3.getSubDomain(), organizationBoard3.getType());
        assertOrganizationBoardWithCreatorInfo(responses.get(2), organizationBoard2.getId(), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
    }

    @Test
    void 게시물_스크롤_페이지네이션하는데_더이상_게시물이_존재하지_않을경우() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard1);

        //when
        List<BoardWithOrganizationDto> responses = organizationBoardService.retrieveBoardsWithPagination(null, organizationBoard1.getId(), 3);

        //then
        assertThat(responses).isEmpty();
    }

    @Test
    void 카테고리가_정해진_경우_카테고리만_조회한다() {
        //given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.EVENT);
        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2));

        //when
        List<BoardWithOrganizationDto> responses = organizationBoardService.retrieveBoardsWithPagination(OrganizationBoardType.RECRUIT, 0, 3);

        //then
        assertThat(responses).hasSize(1);
        assertOrganizationBoardWithCreatorInfo(responses.get(0), organizationBoard1.getId(), organizationBoard1.getTitle(), organizationBoard1.getStartDateTime(),
            organizationBoard1.getEndDateTime(), organizationBoard1.getSubDomain(), organizationBoard1.getType());
    }

    @MethodSource("아직_시작하지_않고_일주일_이내에_종료되는_게시물들")
    @ParameterizedTest
    void 얼마남지_않은_게시물_조회시_아직_시작하지_않고_일주일_이전에_종료되는_그룹_게시물들이_포함된다(LocalDateTime startDateTime, LocalDateTime endDateTime, String title) {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, title, startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        RetrieveImminentBoardsRequest request = RetrieveImminentBoardsRequest.testInstance(LocalDateTime.of(2021, 4, 23, 0, 0), 3);

        // when
        List<OrganizationBoardInfoResponse> organizationBoardInfoResponses = organizationBoardService.retrieveImminentBoards(request);

        // then
        assertThat(organizationBoardInfoResponses).hasSize(1);
        assertOrganizationBoardInfo(organizationBoardInfoResponses.get(0), title, startDateTime, endDateTime, subDomain, OrganizationBoardType.RECRUIT);
    }

    private static Stream<Arguments> 아직_시작하지_않고_일주일_이내에_종료되는_게시물들() {
        return Stream.of(
            Arguments.of(LocalDateTime.of(2021, 4, 24, 0, 0), LocalDateTime.of(2021, 4, 29, 11, 59), "게시물1"),
            Arguments.of(LocalDateTime.of(2021, 4, 23, 0, 1), LocalDateTime.of(2021, 4, 23, 0, 1), "게시물1")
        );
    }

    @MethodSource("아직_시작하지_않고_일주일_이후에_종료되는_게시물들")
    @ParameterizedTest
    void 얼마남지_않은_게시물_조회시_아직_시작하지_않고_일주일_이후로_종료되는_그룹_게시물들이_포함되지_않는다(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, "게시물", startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        RetrieveImminentBoardsRequest request = RetrieveImminentBoardsRequest.testInstance(LocalDateTime.of(2021, 4, 23, 0, 0), 3);

        // when
        List<OrganizationBoardInfoResponse> organizationBoardInfoResponses = organizationBoardService.retrieveImminentBoards(request);

        // then
        assertThat(organizationBoardInfoResponses).isEmpty();
    }

    private static Stream<Arguments> 아직_시작하지_않고_일주일_이후에_종료되는_게시물들() {
        return Stream.of(
            Arguments.of(LocalDateTime.of(2021, 4, 23, 0, 1), LocalDateTime.of(2021, 5, 3, 0, 1)),
            Arguments.of(LocalDateTime.of(2021, 4, 23, 0, 1), LocalDateTime.of(2021, 5, 1, 0, 0))
        );
    }

    @MethodSource("이미_시작하였고_일주일_이내에_종료되는_게시물들")
    @ParameterizedTest
    void 얼마남지_않은_게시물_조회시_이미_시작하였고_일주일_이전에_종료되는_그룹_게시물들이_포함된다(LocalDateTime startDateTime, LocalDateTime endDateTime, String title) {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, title, startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        RetrieveImminentBoardsRequest request = RetrieveImminentBoardsRequest.testInstance(LocalDateTime.of(2021, 4, 23, 0, 0), 3);

        // when
        List<OrganizationBoardInfoResponse> organizationBoardInfoResponses = organizationBoardService.retrieveImminentBoards(request);

        // then
        assertThat(organizationBoardInfoResponses).hasSize(1);
        assertOrganizationBoardInfo(organizationBoardInfoResponses.get(0), title, startDateTime, endDateTime, subDomain, OrganizationBoardType.RECRUIT);
    }

    private static Stream<Arguments> 이미_시작하였고_일주일_이내에_종료되는_게시물들() {
        return Stream.of(
            Arguments.of(LocalDateTime.of(2021, 4, 21, 0, 0), LocalDateTime.of(2021, 4, 29, 11, 59), "게시물1"),
            Arguments.of(LocalDateTime.of(2021, 4, 22, 11, 59), LocalDateTime.of(2021, 4, 23, 0, 1), "게시물1")
        );
    }

    @MethodSource("이미_끝난_게시물들")
    @ParameterizedTest
    void 얼마남지_않은_게시물_조회시_이미_끝난경우_조회되지_않는다(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, "게시물", startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        RetrieveImminentBoardsRequest request = RetrieveImminentBoardsRequest.testInstance(LocalDateTime.of(2021, 4, 23, 0, 0), 3);

        // when
        List<OrganizationBoardInfoResponse> organizationBoardInfoResponses = organizationBoardService.retrieveImminentBoards(request);

        assertThat(organizationBoardInfoResponses).isEmpty();
    }

    private static Stream<Arguments> 이미_끝난_게시물들() {
        return Stream.of(
            Arguments.of(LocalDateTime.of(2021, 4, 20, 0, 0), LocalDateTime.of(2021, 4, 22, 23, 50)),
            Arguments.of(LocalDateTime.of(2021, 4, 21, 0, 0), LocalDateTime.of(2021, 4, 22, 23, 59))
        );
    }

    @Test
    void 인기있는_게시물조회시_좋아요가_많은_순으로_조회한다() {
        // given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "title2", OrganizationBoardType.RECRUIT);
        organizationBoard1.addLike(2L);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "title3", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2));

        // when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrievePopularBoard(2);

        // then
        assertThat(responses).hasSize(2);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard1.getTitle(), organizationBoard1.getStartDateTime(),
            organizationBoard1.getEndDateTime(), organizationBoard1.getSubDomain(), organizationBoard1.getType());
        assertOrganizationBoardInfo(responses.get(1), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
    }

    @Test
    void 인기있는_게시물_조회시_좋아요수가_같으면_최신순부터_조회한다() {
        // given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "더 오래된 게시물", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "더 최신의 게시물", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2));

        // when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrievePopularBoard(3);

        // then
        assertThat(responses).hasSize(2);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
        assertOrganizationBoardInfo(responses.get(1), organizationBoard1.getTitle(), organizationBoard1.getStartDateTime(),
            organizationBoard1.getEndDateTime(), organizationBoard1.getSubDomain(), organizationBoard1.getType());
    }

    @Test
    void 인기있는_게시물_조회시_상위의_5개만_불러온다() {
        // given
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, memberId, "게시물1", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, memberId, "게시물2", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, memberId, "게시물3", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard4 = OrganizationBoardCreator.create(subDomain, memberId, "게시물4", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard5 = OrganizationBoardCreator.create(subDomain, memberId, "게시물5", OrganizationBoardType.RECRUIT);
        OrganizationBoard organizationBoard6 = OrganizationBoardCreator.create(subDomain, memberId, "게시물6", OrganizationBoardType.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3, organizationBoard4, organizationBoard5, organizationBoard6));

        // when
        List<OrganizationBoardInfoResponse> responses = organizationBoardService.retrievePopularBoard(5);

        // then
        assertThat(responses).hasSize(5);
        assertOrganizationBoardInfo(responses.get(0), organizationBoard6.getTitle(), organizationBoard6.getStartDateTime(),
            organizationBoard6.getEndDateTime(), organizationBoard6.getSubDomain(), organizationBoard6.getType());
        assertOrganizationBoardInfo(responses.get(1), organizationBoard5.getTitle(), organizationBoard5.getStartDateTime(),
            organizationBoard5.getEndDateTime(), organizationBoard5.getSubDomain(), organizationBoard5.getType());
        assertOrganizationBoardInfo(responses.get(2), organizationBoard4.getTitle(), organizationBoard4.getStartDateTime(),
            organizationBoard4.getEndDateTime(), organizationBoard4.getSubDomain(), organizationBoard4.getType());
        assertOrganizationBoardInfo(responses.get(3), organizationBoard3.getTitle(), organizationBoard3.getStartDateTime(),
            organizationBoard3.getEndDateTime(), organizationBoard3.getSubDomain(), organizationBoard3.getType());
        assertOrganizationBoardInfo(responses.get(4), organizationBoard2.getTitle(), organizationBoard2.getStartDateTime(),
            organizationBoard2.getEndDateTime(), organizationBoard2.getSubDomain(), organizationBoard2.getType());
    }

    private void assertOrganizationBoardWithCreatorInfo(BoardWithOrganizationDto organizationBoardInfoResponse, Long boardId, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String subDomain, OrganizationBoardType type) {
        assertThat(organizationBoardInfoResponse.getOrgSubDomain()).isEqualTo(subDomain);
        assertThat(organizationBoardInfoResponse.getBoardId()).isEqualTo(boardId);
        assertThat(organizationBoardInfoResponse.getBoardTitle()).isEqualTo(title);
        assertThat(organizationBoardInfoResponse.getBoardStartDateTime()).isEqualTo(startDateTime);
        assertThat(organizationBoardInfoResponse.getBoardEndDateTime()).isEqualTo(endDateTime);
        assertThat(organizationBoardInfoResponse.getBoardType()).isEqualTo(type);
    }

    private void assertOrganizationBoardInfo(OrganizationBoardInfoResponse organizationBoardInfoResponse, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String subDomain, OrganizationBoardType type) {
        assertThat(organizationBoardInfoResponse.getTitle()).isEqualTo(title);
        assertThat(organizationBoardInfoResponse.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(organizationBoardInfoResponse.getEndDateTime()).isEqualTo(endDateTime);
        assertThat(organizationBoardInfoResponse.getType()).isEqualTo(type);
        assertThat(organizationBoardInfoResponse.getSubDomain()).isEqualTo(subDomain);
    }

}
