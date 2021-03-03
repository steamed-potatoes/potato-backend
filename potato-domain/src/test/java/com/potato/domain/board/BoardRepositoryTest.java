package com.potato.domain.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void cleanUp() {
        boardRepository.deleteAll();
    }

    @Test
    void findPublicBoardsOrderByIdDesc_아무런_게시판이_없을경우_빈_리스트가_조회된다() {
        // when
        List<Board> boardList = boardRepository.findPublicBoardsOrderByIdDesc(3);

        // then
        assertThat(boardList).isEmpty();
    }

    @Test
    void findPublicBoardsOrderByIdDesc_가장_최신의_게시물_3개를_가져온다() {
        // given
        Board board1 = BoardCreator.create("subDomain", 1L, "게시물1");
        Board board2 = BoardCreator.create("subDomain", 1L, "게시물2");
        Board board3 = BoardCreator.create("subDomain", 1L, "게시물3");
        boardRepository.saveAll(Arrays.asList(board1, board2, board3));

        // when
        List<Board> boardList = boardRepository.findPublicBoardsOrderByIdDesc(3);

        // then
        assertThat(boardList).hasSize(3);
        assertThat(boardList.get(0).getTitle()).isEqualTo(board3.getTitle());
        assertThat(boardList.get(1).getTitle()).isEqualTo(board2.getTitle());
        assertThat(boardList.get(2).getTitle()).isEqualTo(board1.getTitle());
    }

    @Test
    void findPublicBoardsOrderByIdDesc_비공개_게시물은_조회되지_않는다() {
        // given
        boardRepository.saveAll(Collections.singletonList(BoardCreator.createPrivate("subDomain", 1L, "비공개 게시물")));

        // when
        List<Board> boardList = boardRepository.findPublicBoardsOrderByIdDesc(1);

        // then
        assertThat(boardList).isEmpty();
    }

    @Test
    void findPublicBoardsLessThanOrderByIdDescLimit_id_가_4보다_작은_게시물_3개를_불러온다() {
        // given
        Board board1 = BoardCreator.create("subDomain", 1L, "게시물1");
        Board board2 = BoardCreator.create("subDomain", 1L, "게시물2");
        Board board3 = BoardCreator.create("subDomain", 1L, "게시물3");
        Board board4 = BoardCreator.create("subDomain", 1L, "게시물4");
        Board board5 = BoardCreator.create("subDomain", 1L, "게시물5");
        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        // when
        List<Board> boardList = boardRepository.findPublicBoardsLessThanOrderByIdDescLimit(board4.getId(), 3);

        // then
        assertThat(boardList).hasSize(3);
        assertThat(boardList.get(0).getTitle()).isEqualTo(board3.getTitle());
        assertThat(boardList.get(1).getTitle()).isEqualTo(board2.getTitle());
        assertThat(boardList.get(2).getTitle()).isEqualTo(board1.getTitle());
    }

    @Test
    void findPublicBoardsLessThanOrderByIdDescLimit_가_4보다_작은_게시물_3개를_불러올때_해당하는_게시물이_3개보다_적을때_그_수만큼_조회한다() {
        // given
        Board board1 = BoardCreator.create("subDomain", 1L, "게시물1");
        Board board2 = BoardCreator.create("subDomain", 1L, "게시물2");
        Board board3 = BoardCreator.create("subDomain", 1L, "게시물3");
        Board board4 = BoardCreator.create("subDomain", 1L, "게시물4");
        Board board5 = BoardCreator.create("subDomain", 1L, "게시물5");
        boardRepository.saveAll(Arrays.asList(board1, board2, board3, board4, board5));

        // when
        List<Board> boardList = boardRepository.findPublicBoardsLessThanOrderByIdDescLimit(board4.getId(), 4);

        // then
        assertThat(boardList).hasSize(3);
        assertThat(boardList.get(0).getTitle()).isEqualTo(board3.getTitle());
        assertThat(boardList.get(1).getTitle()).isEqualTo(board2.getTitle());
        assertThat(boardList.get(2).getTitle()).isEqualTo(board1.getTitle());
    }

    @Test
    void findPublicBoardsLessThanOrderByIdDescLimit_비공개_게시물은_포함되지_않는다() {
        // given
        boardRepository.saveAll(Collections.singletonList(BoardCreator.createPrivate("subDomain", 1L, "비공개 게시물")));

        // when
        List<Board> boardList = boardRepository.findPublicBoardsLessThanOrderByIdDescLimit(10000L, 1);

        // then
        assertThat(boardList).isEmpty();
    }

}
