package com.potato.service.board;

import com.potato.exception.ConflictException;
import com.potato.exception.ForbiddenException;
import com.potato.service.OrganizationMemberSetUpTest;
import com.potato.domain.board.*;
import com.potato.exception.NotFoundException;
import com.potato.service.board.dto.response.BoardWithCreatorInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.potato.service.board.BoardServiceTestUtils.assertBoardInfoResponse;
import static com.potato.service.board.BoardServiceTestUtils.assertBoardWithCreatorInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BoardServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardLikeRepository boardLikeRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        boardLikeRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
    }

    @Test
    void 가장_최신의_게시물_3개를_불러온다() {
        //given
        Board board1 = BoardCreator.create(subDomain, memberId, "게시물1");
        Board board2 = BoardCreator.create(subDomain, memberId, "게시물2");
        Board board3 = BoardCreator.create(subDomain, memberId, "게시물3");

        boardRepository.saveAll(Arrays.asList(board1, board2, board3));

        //when
        List<BoardWithCreatorInfoResponse> responses = boardService.retrievePublicLatestBoardList(0L, 3);

        //then
        assertThat(responses.size()).isEqualTo(3);
        assertBoardWithCreatorInfoResponse(responses.get(0), board3.getTitle(), memberId);
        assertBoardWithCreatorInfoResponse(responses.get(1), board2.getTitle(), memberId);
        assertBoardWithCreatorInfoResponse(responses.get(2), board1.getTitle(), memberId);
    }

    @Test
    void 가장_최신의_게시물_3개를_불러올때_게시글이_없을_경우_빈리스트를_반환한다() {
        //when
        List<BoardWithCreatorInfoResponse> boards = boardService.retrievePublicLatestBoardList(0L, 2);

        //then
        assertThat(boards).isEmpty();
    }

    @DisplayName("게시물 4 이후로부터 3개를 조회했으니 [3, 2, 1] 가 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_1() {
        //given
        Board board1 = BoardCreator.create(subDomain, memberId, "게시물1");
        Board board2 = BoardCreator.create(subDomain, memberId, "게시물2");
        Board board3 = BoardCreator.create(subDomain, memberId, "게시물3");
        Board board4 = BoardCreator.create(subDomain, memberId, "게시물4");
        Board board5 = BoardCreator.create(subDomain, memberId, "게시물5");

        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        //when
        List<BoardWithCreatorInfoResponse> responses = boardService.retrievePublicLatestBoardList(board4.getId(), 3);

        //then
        assertThat(responses.size()).isEqualTo(3);
        assertBoardWithCreatorInfoResponse(responses.get(0), board3.getTitle(), memberId);
        assertBoardWithCreatorInfoResponse(responses.get(1), board2.getTitle(), memberId);
        assertBoardWithCreatorInfoResponse(responses.get(2), board1.getTitle(), memberId);
    }

    @DisplayName("게시물 5 이후로부터 3개를 조회했으니 [4,3,2] 가 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_2() {
        //given
        Board board1 = BoardCreator.create(subDomain, memberId, "게시물1");
        Board board2 = BoardCreator.create(subDomain, memberId, "게시물2");
        Board board3 = BoardCreator.create(subDomain, memberId, "게시물3");
        Board board4 = BoardCreator.create(subDomain, memberId, "게시물4");
        Board board5 = BoardCreator.create(subDomain, memberId, "게시물5");

        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        //when
        List<BoardWithCreatorInfoResponse> responses = boardService.retrievePublicLatestBoardList(board5.getId(), 3);

        //then
        assertThat(responses.size()).isEqualTo(3);
        assertBoardWithCreatorInfoResponse(responses.get(0), board4.getTitle(), memberId);
        assertBoardWithCreatorInfoResponse(responses.get(1), board3.getTitle(), memberId);
        assertBoardWithCreatorInfoResponse(responses.get(2), board2.getTitle(), memberId);
    }

    @DisplayName("게시물 5 이후로부터 2개를 조회했으니 [4,3] 가 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_3() {
        //given
        Board board1 = BoardCreator.create(subDomain, memberId, "게시물1");
        Board board2 = BoardCreator.create(subDomain, memberId, "게시물2");
        Board board3 = BoardCreator.create(subDomain, memberId, "게시물3");
        Board board4 = BoardCreator.create(subDomain, memberId, "게시물4");
        Board board5 = BoardCreator.create(subDomain, memberId, "게시물5");

        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        //when
        List<BoardWithCreatorInfoResponse> responses = boardService.retrievePublicLatestBoardList(board5.getId(), 2);

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertBoardWithCreatorInfoResponse(responses.get(0), board4.getTitle(), memberId);
        assertBoardWithCreatorInfoResponse(responses.get(1), board3.getTitle(), memberId);
    }

    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_더이상_게시물이_존재하지_않을경우() {
        //given
        Board board1 = BoardCreator.create(subDomain, memberId, "게시물1");
        boardRepository.saveAll(Collections.singletonList(board1));

        //when
        List<BoardWithCreatorInfoResponse> responses = boardService.retrievePublicLatestBoardList(board1.getId(), 3);

        //then
        assertThat(responses).isEmpty();
    }

    @Test
    void 조직내의_비공개_게시물은_전체_조회시_포함되지_않는다() {
        // given
        boardRepository.save(BoardCreator.createPrivate(subDomain, memberId, "비공개 게시물"));

        // when
        List<BoardWithCreatorInfoResponse> responses = boardService.retrievePublicLatestBoardList(0L, 3);

        // then
        assertThat(responses).isEmpty();
    }

    @Test
    void 특정_게시물을_자세히_조회할때_정상적으로_정보를_조회한다() {
        //given
        String title = "게시글1";
        String content = "게시글1입니다.";
        String imageUrl = "123";

        Board board = BoardCreator.create(subDomain, memberId, title, content, imageUrl);

        boardRepository.save(board);

        //when
        BoardWithCreatorInfoResponse response = boardService.getDetailBoard(board.getId());

        //then
        assertBoardInfoResponse(response.getBoard(), Visible.PUBLIC, title, content, imageUrl, Category.RECRUIT);
        assertThat(response.getCreator().getId()).isEqualTo(memberId);
    }

    @Test
    void 특정_게시물을_자세히_조회할때_특정게시물이_없을경우_에러() {
        //when
        assertThatThrownBy(
            () -> boardService.getDetailBoard(1L)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 특정_게시물을_자세히_조회할때_비공개_일경우_조회되지_않는다() {
        // given
        Board board = BoardCreator.createPrivate(subDomain, memberId, "비공개 게시물");
        boardRepository.save(board);

        // when & then
        assertThatThrownBy(() -> boardService.getDetailBoard(board.getId())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 내가_게시한_게시물에_좋아요를_누른다() {
        // given
        Board board = BoardCreator.create(subDomain, memberId, "비공개 게시물");
        boardRepository.save(board);

        // when
        boardService.addBoardLike(board.getId(), 100L);

        // then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getLikesCount()).isEqualTo(1);

        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(1);
        assertThat(boardLikeList.get(0).getMemberId()).isEqualTo(100L);
    }

    @Test
    void 이미_해당_게시물에_좋아요를_누르면_더이상_누를수_없다() {
        // given
        Board board = BoardCreator.create(subDomain, memberId, "비공개 게시물");
        board.addLike(memberId);
        boardRepository.save(board);

        // when & then
        assertThatThrownBy(() -> boardService.addBoardLike(board.getId(), memberId)).isInstanceOf(ConflictException.class);
    }

    @Test
    void 내가_속하지_않은_조직의_공개된_게시물을_좋아요_누를수_있다() {
        // given
        Board board = BoardCreator.create(subDomain, memberId, "비공개 게시물");
        boardRepository.save(board);

        // when
        boardService.addBoardLike(board.getId(), 100L);

        // then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getLikesCount()).isEqualTo(1);

        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).hasSize(1);
        assertThat(boardLikeList.get(0).getMemberId()).isEqualTo(100L);
    }

    @Test
    void 내가_속하지_않은_조직의_비공개_게시물을_좋아요_누를수_없다() {
        // given
        Board board = BoardCreator.createPrivate(subDomain, memberId, "비공개 게시물");
        boardRepository.save(board);

        // when & then
        assertThatThrownBy(() -> boardService.addBoardLike(board.getId(), 100L)).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 게시물에_누른_좋아요를_취소한다() {
        // given
        Board board = BoardCreator.create(subDomain, memberId, "비공개 게시물");
        board.addLike(memberId);
        boardRepository.save(board);

        // when
        boardService.cancelBoardLike(board.getId(), memberId);

        // then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getLikesCount()).isEqualTo(0);

        List<BoardLike> boardLikeList = boardLikeRepository.findAll();
        assertThat(boardLikeList).isEmpty();
    }

    @Test
    void 게시물에_누르지_않은_좋아요를_취소하면_에러가_발생() {
        // given
        Board board = BoardCreator.create(subDomain, memberId, "비공개 게시물");
        boardRepository.save(board);

        // when & then
        assertThatThrownBy(() -> boardService.cancelBoardLike(board.getId(), memberId)).isInstanceOf(NotFoundException.class);
    }

}

