package com.potato.service.board;

import com.potato.domain.board.*;
import com.potato.exception.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.potato.service.board.BoardServiceTestUtils.assertBoardInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class BoardServiceTest extends MemberSetupTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        boardRepository.deleteAll();
    }

    @Test
    void 가장_최신의_게시물_3개를_불러온다() {
        //given
        Board board1 = BoardCreator.create("게시물1");
        Board board2 = BoardCreator.create("게시물2");
        Board board3 = BoardCreator.create("게시물3");

        boardRepository.saveAll(Arrays.asList(board1, board2, board3));

        //when
        List<BoardInfoResponse> responses = boardService.retrievePublicLatestBoardList(0L, 3);

        //then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getTitle()).isEqualTo(board3.getTitle());
        assertThat(responses.get(1).getTitle()).isEqualTo(board2.getTitle());
        assertThat(responses.get(2).getTitle()).isEqualTo(board1.getTitle());
    }

    @Test
    void 가장_최신의_게시물_3개를_불러올때_게시글이_없을_경우_빈리스트를_반환한다() {
        //when
        List<BoardInfoResponse> boards = boardService.retrievePublicLatestBoardList(0L, 2);

        //then
        assertThat(boards).isEmpty();
    }

    @DisplayName("게시물 4 이후로부터 3개를 조회했으니 [3, 2, 1] 가 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_1() {
        //given
        Board board1 = BoardCreator.create("게시물1");
        Board board2 = BoardCreator.create("게시물2");
        Board board3 = BoardCreator.create("게시물3");
        Board board4 = BoardCreator.create("게시물4");
        Board board5 = BoardCreator.create("게시물5");

        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        //when
        List<BoardInfoResponse> responses = boardService.retrievePublicLatestBoardList(board4.getId(), 3);

        //then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getTitle()).isEqualTo(board3.getTitle());
        assertThat(responses.get(1).getTitle()).isEqualTo(board2.getTitle());
        assertThat(responses.get(2).getTitle()).isEqualTo(board1.getTitle());
    }

    @DisplayName("게시물 5 이후로부터 3개를 조회했으니 [4,3,2] 가 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_2() {
        //given
        Board board1 = BoardCreator.create("게시물1");
        Board board2 = BoardCreator.create("게시물2");
        Board board3 = BoardCreator.create("게시물3");
        Board board4 = BoardCreator.create("게시물4");
        Board board5 = BoardCreator.create("게시물5");

        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        //when
        List<BoardInfoResponse> responses = boardService.retrievePublicLatestBoardList(board5.getId(), 3);

        //then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getTitle()).isEqualTo(board4.getTitle());
        assertThat(responses.get(1).getTitle()).isEqualTo(board3.getTitle());
        assertThat(responses.get(2).getTitle()).isEqualTo(board2.getTitle());
    }

    @DisplayName("게시물 5 이후로부터 2개를 조회했으니 [4,3] 가 조회되어야 한다")
    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_3() {
        //given
        Board board1 = BoardCreator.create("게시물1");
        Board board2 = BoardCreator.create("게시물2");
        Board board3 = BoardCreator.create("게시물3");
        Board board4 = BoardCreator.create("게시물4");
        Board board5 = BoardCreator.create("게시물5");

        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        //when
        List<BoardInfoResponse> responses = boardService.retrievePublicLatestBoardList(board5.getId(), 2);

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getTitle()).isEqualTo(board4.getTitle());
        assertThat(responses.get(1).getTitle()).isEqualTo(board3.getTitle());
    }

    @Test
    void 게시물_스크롤_페이지네이션_조회_기능_더이상_게시물이_존재하지_않을경우() {
        //given
        Board board1 = BoardCreator.create("게시물1");
        boardRepository.saveAll(Collections.singletonList(board1));

        //when
        List<BoardInfoResponse> responses = boardService.retrievePublicLatestBoardList(board1.getId(), 3);

        //then
        assertThat(responses).isEmpty();
    }

    @Test
    void 조직내의_비공개_게시물은_전체_조회시_포함되지_않는다() {
        // given
        boardRepository.save(BoardCreator.createPrivate("비공개 게시물"));

        // when
        List<BoardInfoResponse> responses = boardService.retrievePublicLatestBoardList(0L, 3);

        // then
        assertThat(responses).isEmpty();
    }

    @Test
    void 특정_게시물을_자세히_조회할때_정상적으로_정보를_조회한다() {
        //given
        String title = "게시글1";
        String content = "게시글1입니다.";
        String imageUrl = "123";

        Board board = BoardCreator.create(title, content, imageUrl);

        boardRepository.save(board);

        //when
        BoardInfoResponse response = boardService.getDetailBoard(board.getId());

        //then
        assertBoardInfo(response, Visible.PUBLIC, title, content, imageUrl, Category.RECRUIT);
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
        Board board = BoardCreator.createPrivate("비공개 게시물");
        boardRepository.save(board);

        // when & then
        assertThatThrownBy(() -> {
            BoardInfoResponse response = boardService.getDetailBoard(board.getId());
        }).isInstanceOf(NotFoundException.class);
    }

}

