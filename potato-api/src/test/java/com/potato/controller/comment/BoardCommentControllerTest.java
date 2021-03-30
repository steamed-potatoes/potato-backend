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
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
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
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(organizationBoard.getId(), 200);

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
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(999L, 404);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

    @Test
    void 삭제된_댓글을_조회시_삭제되었다고_표시된다() throws Exception {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create("subDomain", testMember.getId(), "123", OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        BoardComment rootComment = BoardCommentCreator.createRootComment(organizationBoard.getId(), testMember.getId(), "루트 댓글1");
        rootComment.delete();
        boardCommentRepository.save(rootComment);

        // when
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(organizationBoard.getId(), 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertBoardCommentResponse(response.getData().get(0), organizationBoard.getId(), "삭제된 메시지입니다", null);
    }

    @Test
    void 로그인하지_않은_유저가_댓글을_추가하려하면_401_에러발생() throws Exception {
        // given
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create("subDomain", testMember.getId(), "123", OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(organizationBoard.getId(), null, "로그인 하지 않은 유저가 댓글을 달 경우");

        // when
        ApiResponse<String> response = boardCommentMockMvc.addBoardComment(request, "wrong token", 401);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.getCode());
    }

    private void assertBoardCommentResponse(BoardCommentResponse boardCommentResponse, Long organizationBoardId, String content, Long memberId) {
        assertThat(boardCommentResponse.getBoardId()).isEqualTo(organizationBoardId);
        assertThat(boardCommentResponse.getContent()).isEqualTo(content);
        assertThat(boardCommentResponse.getMemberId()).isEqualTo(memberId);
    }

}