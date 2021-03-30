package com.potato.controller.comment;

import com.potato.controller.ApiResponse;
import com.potato.controller.ControllerTestUtils;
import com.potato.controller.advice.ErrorCode;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardType;
import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentCreator;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
class BoardCommentControllerTest extends ControllerTestUtils {

    private BoardCommentMockMvc boardCommentMockMvc;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @BeforeEach
    void setUp() {
        super.setup();
        boardCommentMockMvc = new BoardCommentMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAll();
        boardCommentRepository.deleteAll();
    }

    @Test
    void 게시물의_댓글을_조회한다() throws Exception {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create("subDomain", testMember.getId(), "123", OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        BoardComment rootComment = BoardCommentCreator.createRootComment(organizationBoard.getId(), testMember.getId(), "루트 댓글1");
        rootComment.addChildComment(testMember.getId(), "대댓글1");
        rootComment.addChildComment(testMember.getId(), "대댓글2");
        boardCommentRepository.save(rootComment);

        // when
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(organizationBoard.getId());

        // then
        assertThat(response.getData()).hasSize(1);
        assertBoardCommentResponse(response.getData().get(0), organizationBoard.getId(), "루트 댓글1", testMember.getId());

        assertThat(response.getData().get(0).getChildren()).hasSize(2);
        assertBoardCommentResponse(response.getData().get(0).getChildren().get(0), organizationBoard.getId(), "대댓글1", testMember.getId());
        assertBoardCommentResponse(response.getData().get(0).getChildren().get(1), organizationBoard.getId(), "대댓글2", testMember.getId());
    }

    @Test
    void 존재하지_않는_게시물의_댓글을_조회하면_404에러가_발생() throws Exception {
        // when
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(999L);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

    private void assertBoardCommentResponse(BoardCommentResponse boardCommentResponse, Long organizationBoardId, String content, Long memberId) {
        assertThat(boardCommentResponse.getBoardId()).isEqualTo(organizationBoardId);
        assertThat(boardCommentResponse.getContent()).isEqualTo(content);
        assertThat(boardCommentResponse.getMemberId()).isEqualTo(memberId);
    }

}
