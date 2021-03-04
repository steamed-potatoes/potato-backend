package com.potato.service.comment;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardCreator;
import com.potato.domain.board.BoardRepository;
import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentCreator;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.service.OrganizationMemberSetUpTest;
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardCommentServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private BoardCommentService boardCommentService;

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        boardRepository.deleteAll();
        boardCommentRepository.deleteAll();
    }

    private Board board;

    @BeforeEach
    void setUpBoard() {
        board = boardRepository.save(BoardCreator.create(subDomain, memberId, "게시물"));
    }

    @Test
    void 가장_최상위의_댓글을_작성한다() {
        // given
        String content = "부모 댓글";
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(board.getId(), null, content);

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(1);
        assertBoardComment(boardCommentList.get(0), board.getId(), memberId, content);
        assertThat(boardCommentList.get(0).getParentComment()).isNull();
    }

    @Test
    void 최상위_댓글_하위에_자식_댓글을_추가한다() {
        // given
        String content = "자식 댓글";
        BoardComment rootComment = BoardCommentCreator.createRootComment(board.getId(), memberId, "부모 댓글");
        boardCommentRepository.save(rootComment);

        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(board.getId(), rootComment.getId(), content);

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(2);
        assertBoardComment(boardCommentList.get(0), board.getId(), memberId, "부모 댓글");
        assertThat(boardCommentList.get(0).getParentComment()).isNull();

        assertBoardComment(boardCommentList.get(1), board.getId(), memberId, content);
        assertThat(boardCommentList.get(1).getParentComment().getId()).isEqualTo(boardCommentList.get(0).getId());
    }

    @Test
    void 특정_게시물의_댓글들을_불러온다() {
        // given
        BoardComment rootComment1 = BoardCommentCreator.createRootComment(board.getId(), memberId, "부모 댓글1");
        rootComment1.addChildComment(memberId, "자식 댓글1-1");
        rootComment1.addChildComment(memberId, "자식 댓글1-2");
        BoardComment rootComment2 = BoardCommentCreator.createRootComment(board.getId(), memberId, "부모 댓글2");
        rootComment2.addChildComment(memberId, "자식 댓글2-1");
        boardCommentRepository.saveAll(Arrays.asList(rootComment1, rootComment2));

        // when
        List<BoardCommentResponse> responses = boardCommentService.retrieveBoardCommentList(board.getId());

        // then
        assertThat(responses).hasSize(2);
        assertBoardCommentResponse(responses.get(0), "부모 댓글1");
        assertBoardCommentResponse(responses.get(1), "부모 댓글2");

        assertThat(responses.get(0).getChildren()).hasSize(2);
        assertBoardCommentResponse(responses.get(0).getChildren().get(0), "자식 댓글1-1");
        assertBoardCommentResponse(responses.get(0).getChildren().get(1), "자식 댓글1-2");

        assertThat(responses.get(1).getChildren()).hasSize(1);
        assertBoardCommentResponse(responses.get(1).getChildren().get(0), "자식 댓글2-1");
    }

    private void assertBoardCommentResponse(BoardCommentResponse response, String content) {
        assertThat(response.getContent()).isEqualTo(content);
    }

    private void assertBoardComment(BoardComment boardComment, Long boardId, Long memberId, String content) {
        assertThat(boardComment.getBoardId()).isEqualTo(boardId);
        assertThat(boardComment.getMemberId()).isEqualTo(memberId);
        assertThat(boardComment.getContent()).isEqualTo(content);
    }

}
