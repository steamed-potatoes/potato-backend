package com.potato.api.controller.comment;

import com.potato.api.controller.ApiResponse;
import com.potato.api.controller.AbstractControllerTest;
import com.potato.api.controller.comment.api.BoardCommentMockMvc;
import com.potato.api.service.comment.dto.request.DeleteBoardCommentRequest;
import com.potato.api.service.comment.dto.request.LikeBoardCommentRequest;
import com.potato.api.service.comment.dto.request.RetrieveBoardCommentsRequest;
import com.potato.api.service.comment.dto.request.UpdateBoardCommentRequest;
import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.board.admin.AdminBoard;
import com.potato.domain.domain.board.admin.AdminBoardCreator;
import com.potato.domain.domain.board.admin.AdminBoardRepository;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.common.exception.ErrorCode;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.domain.comment.BoardComment;
import com.potato.domain.domain.comment.BoardCommentCreator;
import com.potato.domain.domain.comment.BoardCommentRepository;
import com.potato.api.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.api.service.comment.dto.response.BoardCommentResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardCommentControllerTest extends AbstractControllerTest {

    private BoardCommentMockMvc boardCommentMockMvc;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    private OrganizationBoard organizationBoard;
    private AdminBoard adminBoard;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        organizationBoard = OrganizationBoardCreator.create("subDomain", testMember.getId(), "그룹 게시글", OrganizationBoardCategory.RECRUIT);
        adminBoard = AdminBoardCreator.create("관리자 공지 게시물", testMember.getId());
        boardCommentMockMvc = new BoardCommentMockMvc(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        adminBoardRepository.deleteAll();
        organizationBoardRepository.deleteAll();
        boardCommentRepository.deleteAll();
    }

    @DisplayName("GET /api/v2/board/comment/list 200 OK")
    @Test
    void 특정_그룹_게시물의_댓글들을_조회한다() throws Exception {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardType type = BoardType.ORGANIZATION_BOARD;
        String content = "루트 댓글";
        BoardComment rootComment1 = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), testMember.getId(), content);

        String childContent1 = "자식 댓글1";
        rootComment1.addChildComment(testMember.getId(), childContent1);

        String childContent2 = "자식 댓글2";
        rootComment1.addChildComment(testMember.getId(), childContent2);
        boardCommentRepository.save(rootComment1);

        RetrieveBoardCommentsRequest request = RetrieveBoardCommentsRequest.testInstance(type, organizationBoard.getId());

        // when
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(request, token, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertBoardCommentResponse(response.getData().get(0), type, organizationBoard.getId(), content, testMember.getId());
        assertBoardCommentResponse(response.getData().get(0).getChildren().get(0), type, organizationBoard.getId(), childContent1, testMember.getId());
        assertBoardCommentResponse(response.getData().get(0).getChildren().get(1), type, organizationBoard.getId(), childContent2, testMember.getId());
    }

    @DisplayName("GET /api/v2/board/comment/list 200 OK")
    @Test
    void 특정_관리자_게시물의_댓글들을_불러온다() throws Exception {
        adminBoardRepository.save(adminBoard);

        BoardType type = BoardType.ADMIN_BOARD;
        String rootContent = "루트 댓글 내용";
        BoardComment rootComment = BoardCommentCreator.createRootComment(type, adminBoard.getId(), testMember.getId(), rootContent);

        String childContent = "자식 댓글";
        rootComment.addChildComment(testMember.getId(), childContent);
        boardCommentRepository.save(rootComment);

        RetrieveBoardCommentsRequest request = RetrieveBoardCommentsRequest.testInstance(type, adminBoard.getId());

        // when
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(request, token, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertBoardCommentResponse(response.getData().get(0), type, adminBoard.getId(), rootContent, testMember.getId());
        assertBoardCommentResponse(response.getData().get(0).getChildren().get(0), type, adminBoard.getId(), childContent, testMember.getId());
    }

    @DisplayName("GET /api/v2/board/comment/list 200 OK")
    @Test
    void 좋아요를_누른_유저가_게시물의_댓글을_조회하면_좋아요눌렀다고_표시된다() throws Exception {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment rootComment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), testMember.getId(), "루트 댓글1");
        rootComment.addLike(testMember.getId());
        boardCommentRepository.save(rootComment);

        RetrieveBoardCommentsRequest request = RetrieveBoardCommentsRequest.testInstance(type, organizationBoard.getId());

        // when
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(request, token, 200);

        // then
        assertThat(response.getData().get(0).getIsLike()).isTrue();
    }

    @DisplayName("GET /api/v2/board/comment/list 200 OK")
    @Test
    void 좋아요를_누르지_않은_유저가_게시물의_댓글을_조회하면_좋아요를_누르지_않았다고_표시된다() throws Exception {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment rootComment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), testMember.getId(), "루트 댓글1");
        boardCommentRepository.save(rootComment);

        RetrieveBoardCommentsRequest request = RetrieveBoardCommentsRequest.testInstance(type, organizationBoard.getId());

        // when
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(request, token, 200);

        // then
        assertThat(response.getData().get(0).getIsLike()).isFalse();
    }

    @DisplayName("GET /api/v2/board/comment/list 404 NOT FOUND")
    @Test
    void 존재하지_않는_게시물의_댓글을_조회하면_404에러가_발생() throws Exception {
        // given
        RetrieveBoardCommentsRequest request = RetrieveBoardCommentsRequest.testInstance(BoardType.ADMIN_BOARD, 999L);

        // when
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(request, token, 404);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

    @DisplayName("GET /api/v2/board/comment/list 200 OK")
    @Test
    void 삭제된_댓글을_조회시_삭제되었다고_표시된다() throws Exception {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment rootComment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), testMember.getId(), "루트 댓글1");
        rootComment.delete();
        boardCommentRepository.save(rootComment);

        RetrieveBoardCommentsRequest request = RetrieveBoardCommentsRequest.testInstance(type, organizationBoard.getId());

        // when
        ApiResponse<List<BoardCommentResponse>> response = boardCommentMockMvc.retrieveBoardComments(request, token, 200);

        // then
        assertThat(response.getData()).hasSize(1);
        assertBoardCommentResponse(response.getData().get(0), type, organizationBoard.getId(), "삭제된 메시지입니다", null);
    }

    @DisplayName("POST /api/v2/board/comment 200 OK")
    @Test
    void 게시물에_댓글을_추가한다() throws Exception {
        // given
        String token = memberMockMvc.getMockMemberToken();

        organizationBoardRepository.save(organizationBoard);

        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), null, "댓글");

        // when
        ApiResponse<String> response = boardCommentMockMvc.addBoardComment(request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("POST /api/v2/board/comment 401 ERROR")
    @Test
    void 로그인하지_않은_유저가_댓글을_추가하려하면_401_에러발생() throws Exception {
        // given
        organizationBoardRepository.save(organizationBoard);

        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardType.ADMIN_BOARD, organizationBoard.getId(), null, "로그인 하지 않은 유저가 댓글을 달 경우");

        // when
        ApiResponse<String> response = boardCommentMockMvc.addBoardComment(request, "wrong token", 401);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.getCode());
    }

    @DisplayName("POST /api/v2/board/comment 404 NOT FOUND")
    @Test
    void 존재하지_않는_게시물에_댓글을_추가하려하면_404_에러발생() throws Exception {
        // given
        String token = memberMockMvc.getMockMemberToken();

        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardType.ORGANIZATION_BOARD, 999L, null, "댓글");

        // when
        ApiResponse<String> response = boardCommentMockMvc.addBoardComment(request, token, 404);

        // then
        assertThat(response.getCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION.getCode());
    }

    @Test
    void 작성한_댓글을_수정한다() throws Exception {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardComment comment = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), testMember.getId(), "변경되기 전 댓글입니다.");
        boardCommentRepository.save(comment);
        String content = "변경한 댓글입니다.";
        UpdateBoardCommentRequest request = UpdateBoardCommentRequest.testInstance(comment.getId(), content);

        // when
        ApiResponse<String> response = boardCommentMockMvc.updateBoardComment(request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }


    @Test
    void 작성한_댓글을_삭제한다() throws Exception {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardComment comment = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), testMember.getId(), "댓글");
        boardCommentRepository.save(comment);

        DeleteBoardCommentRequest request = DeleteBoardCommentRequest.testInstance(comment.getId());

        // when
        ApiResponse<String> response = boardCommentMockMvc.deleteBoardComment(request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("POST /api/v2/board/comment/like 200 OK")
    @Test
    void 댓글예_좋아요를_추가합니다() throws Exception {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardComment comment = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), testMember.getId(), "댓글");
        boardCommentRepository.save(comment);

        LikeBoardCommentRequest request = LikeBoardCommentRequest.testInstance(comment.getId());

        // when
        ApiResponse<String> response = boardCommentMockMvc.likeBoardComment(request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("DELETE /api/v2/board/comment/like 200 OK")
    @Test
    void 댓글예_좋아요를_취소합니다() throws Exception {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardComment comment = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), testMember.getId(), "댓글");
        comment.addLike(testMember.getId());
        boardCommentRepository.save(comment);
        LikeBoardCommentRequest request = LikeBoardCommentRequest.testInstance(comment.getId());

        // when
        ApiResponse<String> response = boardCommentMockMvc.unlikeBoardComment(request, token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    private void assertBoardCommentResponse(BoardCommentResponse boardCommentResponse, BoardType type, Long organizationBoardId, String content, Long memberId) {
        assertThat(boardCommentResponse.getBoardId()).isEqualTo(organizationBoardId);
        assertThat(boardCommentResponse.getType()).isEqualTo(type);
        assertThat(boardCommentResponse.getContent()).isEqualTo(content);
        assertThat(boardCommentResponse.getMemberId()).isEqualTo(memberId);
    }

}
