package com.potato.api.controller.board;

import com.potato.api.controller.ApiResponse;
import com.potato.api.controller.AbstractControllerTest;
import com.potato.api.controller.board.api.OrganizationBoardRetrieveMockMvc;
import com.potato.api.service.board.organization.dto.request.RetrieveImminentBoardsRequest;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponseWithImage;
import com.potato.domain.domain.board.organization.repository.dto.BoardWithOrganizationDto;
import com.potato.domain.domain.image.BoardImage;
import com.potato.domain.domain.image.BoardImageRepository;
import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.domain.hashtag.BoardHashTag;
import com.potato.domain.domain.hashtag.BoardHashTagRepository;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationCreator;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.api.service.board.organization.dto.request.RetrieveLatestBoardsRequest;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardWithCreatorInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationBoardRetrieveControllerTest extends AbstractControllerTest {

    private OrganizationBoardRetrieveMockMvc organizationBoardRetrieveMockMvc;

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
        organizationBoardRetrieveMockMvc = new OrganizationBoardRetrieveMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAll();
        organizationRepository.deleteAll();
        boardHashTagRepository.deleteAll();
        boardImageRepository.deleteAll();
    }

    @DisplayName("GET /api/v2/organization/board 200 OK")
    @Test
    void 특정그룹의_게시물을_조회하는_경우() throws Exception {
        //given
        String subDomain = "potato";
        Organization organization = OrganizationCreator.create(subDomain);
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        String title = "title";
        OrganizationBoard board = OrganizationBoardCreator.create(subDomain, testMember.getId(), title, OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(board);

        String imageUrl = "potato.png";
        BoardImage boardImage = BoardImage.newInstance(board.getId(), BoardType.ORGANIZATION_BOARD, imageUrl);
        boardImageRepository.save(boardImage);

        boardHashTagRepository.saveAll(Collections.singletonList(BoardHashTag.newInstance(BoardType.ORGANIZATION_BOARD, board.getId(), testMember.getId(), "감자")));

        //when
        ApiResponse<OrganizationBoardWithCreatorInfoResponse> response = organizationBoardRetrieveMockMvc.retrieveOrganizationBoard(board.getId(), token, 200);

        //then
        assertThat(response.getData().getBoard().getTitle()).isEqualTo(title);
        assertThat(response.getData().getCreator().getEmail()).isEqualTo(testMember.getEmail());
        assertThat(response.getData().getOrganization().getSubDomain()).isEqualTo(subDomain);
        assertThat(response.getData().getHashTags()).isEqualTo(Collections.singletonList("감자"));
    }

    @DisplayName("GET /api/v2/organization/board 200 OK")
    @Test
    void 좋아요를_누른_게시물을_조회하는_경우_좋아요눌렀다고_표시된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        OrganizationBoard board = OrganizationBoardCreator.create(organization.getSubDomain(), testMember.getId(), "그룹 게시물", OrganizationBoardCategory.RECRUIT);
        board.addLike(testMember.getId());
        organizationBoardRepository.save(board);

        // when
        ApiResponse<OrganizationBoardWithCreatorInfoResponse> response = organizationBoardRetrieveMockMvc.retrieveOrganizationBoard(board.getId(), token, 200);

        // then
        assertThat(response.getData().getIsLike()).isTrue();
    }

    @DisplayName("GET /api/v2/organization/board 200 OK")
    @Test
    void 좋아요를_누르지_않은_게시물을_조회하는_경우_좋아요를_누르지_않았댜고_표시된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        OrganizationBoard board = OrganizationBoardCreator.create(organization.getSubDomain(), testMember.getId(), "그룹 게시물", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(board);

        // when
        ApiResponse<OrganizationBoardWithCreatorInfoResponse> response = organizationBoardRetrieveMockMvc.retrieveOrganizationBoard(board.getId(), token, 200);

        // then
        assertThat(response.getData().getIsLike()).isFalse();
    }

    @DisplayName("GET /api/v2/organization/board 200 OK")
    @Test
    void 로그인하지_않은_유저가_게시물을_조회하는_경우_좋아요를_누르지_않았다고_표시된다() throws Exception {
        // given
        organization.addAdmin(testMember.getId());
        organizationRepository.save(organization);

        OrganizationBoard board = OrganizationBoardCreator.create(organization.getSubDomain(), testMember.getId(), "그룹 게시물", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(board);

        // when
        ApiResponse<OrganizationBoardWithCreatorInfoResponse> response = organizationBoardRetrieveMockMvc.retrieveOrganizationBoard(board.getId(), "Wrong Token", 200);

        // then
        assertThat(response.getData().getIsLike()).isFalse();
    }

    @DisplayName("GET /api/v2/organization/board/list 200 OK")
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
        OrganizationBoard board1 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title1, OrganizationBoardCategory.RECRUIT);
        OrganizationBoard board2 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title2, OrganizationBoardCategory.RECRUIT);
        OrganizationBoard board3 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title3, OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.saveAll(Arrays.asList(board1, board2, board3));

        //when
        ApiResponse<List<BoardWithOrganizationDto>> response = organizationBoardRetrieveMockMvc.retrieveLatestOrganizationBoardList(0, 3, 200);

        //then
        assertThat(response.getData().get(0).getBoardTitle()).isEqualTo(title3);
        assertThat(response.getData().get(1).getBoardTitle()).isEqualTo(title2);
        assertThat(response.getData().get(2).getBoardTitle()).isEqualTo(title1);
    }

    @DisplayName("GET /api/v2/organization/board/list 200 OK")
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
        OrganizationBoard board1 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title1, OrganizationBoardCategory.RECRUIT);
        OrganizationBoard board2 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title2, OrganizationBoardCategory.RECRUIT);
        OrganizationBoard board3 = OrganizationBoardCreator.create(subDomain, testMember.getId(), title3, OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.saveAll(Arrays.asList(board1, board2, board3));

        //when
        ApiResponse<List<BoardWithOrganizationDto>> response = organizationBoardRetrieveMockMvc.retrieveLatestOrganizationBoardList(board3.getId(), 2, 200);

        //then
        assertThat(response.getData().get(0).getBoardTitle()).isEqualTo(title2);
        assertThat(response.getData().get(1).getBoardTitle()).isEqualTo(title1);
    }

    @DisplayName("GET /api/v2/organization/board/list 200 OK")
    @Test
    void 게시글이_없을_경우_빈배열_반환() throws Exception {
        //given

        //when
        ApiResponse<List<BoardWithOrganizationDto>> response = organizationBoardRetrieveMockMvc.retrieveLatestOrganizationBoardList(0, 3, 200);

        //then
        assertThat(response.getData()).isEmpty();
    }

    @DisplayName("GET /api/v2/organization/board/popular/list")
    @Test
    void 인기있는_게시글_5개를_가져오는_경우() throws Exception {
        //given
        String subDomain = "potato";
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, 1L, "title1", OrganizationBoardCategory.RECRUIT);
        organizationBoard1.addLike(2L);
        organizationBoard1.addLike(3L);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, 1L, "title2", OrganizationBoardCategory.RECRUIT);
        organizationBoard2.addLike(2L);
        OrganizationBoard organizationBoard3 = OrganizationBoardCreator.create(subDomain, 1L, "title3", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard4 = OrganizationBoardCreator.create(subDomain, 1L, "title4", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard5 = OrganizationBoardCreator.create(subDomain, 1L, "title5", OrganizationBoardCategory.RECRUIT);

        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2, organizationBoard3, organizationBoard4, organizationBoard5));

        //when
        ApiResponse<List<OrganizationBoardInfoResponse>> response = organizationBoardRetrieveMockMvc.retrievePopularBoard(200);

        //then
        assertThat(response.getData().get(0).getTitle()).isEqualTo(organizationBoard1.getTitle());
        assertThat(response.getData().get(1).getTitle()).isEqualTo(organizationBoard2.getTitle());
        assertThat(response.getData().get(2).getTitle()).isEqualTo(organizationBoard5.getTitle());
        assertThat(response.getData().get(3).getTitle()).isEqualTo(organizationBoard4.getTitle());
        assertThat(response.getData().get(4).getTitle()).isEqualTo(organizationBoard3.getTitle());
    }

    @DisplayName("GET /api/v2/organization/board/list/in")
    @Test
    void 특정_동아리에_속한_게시물만을_조회한다() throws Exception {
        // given
        String subDomain = "potato";
        organizationRepository.save(OrganizationCreator.create(subDomain));
        OrganizationBoard organizationBoard1 = OrganizationBoardCreator.create(subDomain, 1L, "title1", OrganizationBoardCategory.RECRUIT);
        OrganizationBoard organizationBoard2 = OrganizationBoardCreator.create(subDomain, 1L, "title2", OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.saveAll(Arrays.asList(organizationBoard1, organizationBoard2));

        RetrieveLatestBoardsRequest request = RetrieveLatestBoardsRequest.testBuilder()
            .size(5)
            .lastOrganizationBoardId(0)
            .build();

        // when
        ApiResponse<List<BoardWithOrganizationDto>> response = organizationBoardRetrieveMockMvc.getBoardsInOrganization(subDomain, request, 200);

        // then
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getBoardId()).isEqualTo(organizationBoard2.getId());
        assertThat(response.getData().get(1).getBoardId()).isEqualTo(organizationBoard1.getId());
    }

    @DisplayName("GET /api/v2/organization/board/list/imminentBoards")
    @MethodSource("아직_시작하지_않고_일주일_이내에_종료되는_게시물들")
    @ParameterizedTest
    void 얼마남지_않은_게시물_조회시_아직_시작하지_않고_일주일_이전에_종료되는_그룹_게시물들이_포함된다(LocalDateTime startDateTime, LocalDateTime endDateTime, String title) throws Exception {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(organization.getSubDomain(), testMember.getId(), title, startDateTime, endDateTime, OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        RetrieveImminentBoardsRequest request = RetrieveImminentBoardsRequest.testInstance(LocalDateTime.of(2021, 4, 23, 0, 0), 3);

        // when
        ApiResponse<List<OrganizationBoardInfoResponseWithImage>> response = organizationBoardRetrieveMockMvc.retrieveImminentBoards(request, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertOrganizationBoardInfo(response.getData().get(0), organizationBoard);
    }

    private static Stream<Arguments> 아직_시작하지_않고_일주일_이내에_종료되는_게시물들() {
        return Stream.of(
            Arguments.of(LocalDateTime.of(2021, 4, 24, 0, 0), LocalDateTime.of(2021, 4, 29, 11, 59), "게시물1"),
            Arguments.of(LocalDateTime.of(2021, 4, 23, 0, 1), LocalDateTime.of(2021, 4, 23, 0, 1), "게시물1")
        );
    }

    @DisplayName("GET /api/v2/organization/board/list/imminentBoards")
    @MethodSource("아직_시작하지_않고_일주일_이후에_종료되는_게시물들")
    @ParameterizedTest
    void 얼마남지_않은_게시물_조회시_아직_시작하지_않고_일주일_이후로_종료되는_그룹_게시물들이_포함되지_않는다(LocalDateTime startDateTime, LocalDateTime endDateTime) throws Exception {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(organization.getSubDomain(), testMember.getId(), "게시물", startDateTime, endDateTime, OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        RetrieveImminentBoardsRequest request = RetrieveImminentBoardsRequest.testInstance(LocalDateTime.of(2021, 4, 23, 0, 0), 3);

        // when
        ApiResponse<List<OrganizationBoardInfoResponseWithImage>> response = organizationBoardRetrieveMockMvc.retrieveImminentBoards(request, 200);

        // then
        assertThat(response.getData()).isEmpty();
    }

    private static Stream<Arguments> 아직_시작하지_않고_일주일_이후에_종료되는_게시물들() {
        return Stream.of(
            Arguments.of(LocalDateTime.of(2021, 4, 23, 0, 1), LocalDateTime.of(2021, 5, 3, 0, 1)),
            Arguments.of(LocalDateTime.of(2021, 4, 23, 0, 1), LocalDateTime.of(2021, 5, 1, 0, 0))
        );
    }

    @DisplayName("GET /api/v2/organization/board/list/imminentBoards")
    @MethodSource("이미_시작하였고_일주일_이내에_종료되는_게시물들")
    @ParameterizedTest
    void 얼마남지_않은_게시물_조회시_이미_시작하였고_일주일_이전에_종료되는_그룹_게시물들이_포함된다(LocalDateTime startDateTime, LocalDateTime endDateTime, String title) throws Exception {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(organization.getSubDomain(), testMember.getId(), title, startDateTime, endDateTime, OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        RetrieveImminentBoardsRequest request = RetrieveImminentBoardsRequest.testInstance(LocalDateTime.of(2021, 4, 23, 0, 0), 3);

        // when
        ApiResponse<List<OrganizationBoardInfoResponseWithImage>> response = organizationBoardRetrieveMockMvc.retrieveImminentBoards(request, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertOrganizationBoardInfo(response.getData().get(0), organizationBoard);
    }

    private static Stream<Arguments> 이미_시작하였고_일주일_이내에_종료되는_게시물들() {
        return Stream.of(
            Arguments.of(LocalDateTime.of(2021, 4, 21, 0, 0), LocalDateTime.of(2021, 4, 29, 11, 59), "게시물1"),
            Arguments.of(LocalDateTime.of(2021, 4, 22, 11, 59), LocalDateTime.of(2021, 4, 23, 0, 1), "게시물1")
        );
    }

    @DisplayName("GET /api/v2/organization/board/list/imminentBoards")
    @MethodSource("이미_끝난_게시물들")
    @ParameterizedTest
    void 얼마남지_않은_게시물_조회시_이미_끝난경우_조회되지_않는다(LocalDateTime startDateTime, LocalDateTime endDateTime) throws Exception {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create(organization.getSubDomain(), testMember.getId(), "제목", startDateTime, endDateTime, OrganizationBoardCategory.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        RetrieveImminentBoardsRequest request = RetrieveImminentBoardsRequest.testInstance(LocalDateTime.of(2021, 4, 23, 0, 0), 3);

        // when
        ApiResponse<List<OrganizationBoardInfoResponseWithImage>> response = organizationBoardRetrieveMockMvc.retrieveImminentBoards(request, 200);

        assertThat(response.getData()).isEmpty();
    }

    private static Stream<Arguments> 이미_끝난_게시물들() {
        return Stream.of(
            Arguments.of(LocalDateTime.of(2021, 4, 20, 0, 0), LocalDateTime.of(2021, 4, 22, 23, 50)),
            Arguments.of(LocalDateTime.of(2021, 4, 21, 0, 0), LocalDateTime.of(2021, 4, 22, 23, 59))
        );
    }

    private void assertOrganizationBoardInfo(OrganizationBoardInfoResponseWithImage organizationBoardInfoResponse, OrganizationBoard organizationBoard) {
        assertThat(organizationBoardInfoResponse.getId()).isEqualTo(organizationBoard.getId());
        assertThat(organizationBoardInfoResponse.getSubDomain()).isEqualTo(organizationBoard.getSubDomain());
        assertThat(organizationBoardInfoResponse.getTitle()).isEqualTo(organizationBoard.getTitle());
        assertThat(organizationBoardInfoResponse.getStartDateTime()).isEqualTo(organizationBoard.getStartDateTime());
        assertThat(organizationBoardInfoResponse.getEndDateTime()).isEqualTo(organizationBoard.getEndDateTime());
        assertThat(organizationBoardInfoResponse.getType()).isEqualTo(organizationBoard.getCategory());
    }

}
