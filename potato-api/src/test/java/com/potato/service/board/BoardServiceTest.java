package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardCreator;
import com.potato.domain.board.BoardRepository;
import com.potato.service.MemberSetupTest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    void 모든_게시글을_가져온다() {
        //given
        String title1 = "게시글1";
        String title2 = "게시글2";

        Board board1 = BoardCreator.create(title1);
        Board board2 = BoardCreator.create(title2);

        boardRepository.saveAll(Arrays.asList(board1, board2));

        //when
        List<BoardInfoResponse> responses = boardService.getBoards();

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getTitle()).isEqualTo(title1);
        assertThat(responses.get(1).getTitle()).isEqualTo(title2);
    }

    @Test
    void 게시글이_없을_경우_빈리스트를_반환한다() {
        //when
        List<BoardInfoResponse> boards = boardService.getBoards();

        //then
        assertThat(boards).isEmpty();

    }

}

