package com.potato.service.comment;

import com.potato.domain.board.BoardRepository;
import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardCreator;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardType;
import com.potato.domain.comment.*;
import com.potato.exception.model.ConflictException;
import com.potato.exception.model.NotFoundException;
import com.potato.service.OrganizationMemberSetUpTest;
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BoardCommentServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private BoardCommentService boardCommentService;

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardCommentLikeRepository boardCommentLikeRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAllInBatch();
        adminBoardRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        boardCommentRepository.deleteAll();
    }

    private OrganizationBoard organizationBoard;
    private AdminBoard adminBoard;

    @BeforeEach
    void setUpOrganizationBoard() {
        organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, "게시물", OrganizationBoardType.RECRUIT);
        adminBoard = AdminBoardCreator.create("관리자 공지 게시물", memberId);
    }

    @Test
    void 그룹게시물_가장_최상위의_댓글을_작성한다() {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        String content = "부모 댓글";
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(type, organizationBoard.getId(), null, content);

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(1);
        assertBoardComment(boardCommentList.get(0), type, organizationBoard.getId(), memberId, content, 0);
        assertThat(boardCommentList.get(0).getParentComment()).isNull();
    }

    @Test
    void 관리자_게시물_가장_최상위의_댓글을_작성한다() {
        // given
        adminBoardRepository.save(adminBoard);

        BoardCommentType type = BoardCommentType.ADMIN_BOARD;
        String content = "부모 댓글";
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(type, adminBoard.getId(), null, content);

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(1);
        assertBoardComment(boardCommentList.get(0), type, adminBoard.getId(), memberId, content, 0);
        assertThat(boardCommentList.get(0).getParentComment()).isNull();
    }

    @Test
    void 그룹_게시물_최상위_댓글_하위에_자식_댓글을_추가한다() {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        String content = "자식 댓글";
        BoardComment rootComment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "부모 댓글");
        boardCommentRepository.save(rootComment);

        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(type, organizationBoard.getId(), rootComment.getId(), content);

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(2);
        assertBoardComment(boardCommentList.get(0), type, organizationBoard.getId(), memberId, "부모 댓글", 0);
        assertThat(boardCommentList.get(0).getParentComment()).isNull();

        assertBoardComment(boardCommentList.get(1), type, organizationBoard.getId(), memberId, content, 1);
        assertThat(boardCommentList.get(1).getParentComment().getId()).isEqualTo(boardCommentList.get(0).getId());
    }

    @Test
    void 관리자_게시물_최상위_댓글_하위에_자식_댓글을_추가한다() {
        // given
        adminBoardRepository.save(adminBoard);

        BoardCommentType type = BoardCommentType.ADMIN_BOARD;
        String content = "자식 댓글";
        BoardComment rootComment = BoardCommentCreator.createRootComment(type, adminBoard.getId(), memberId, "부모 댓글");
        boardCommentRepository.save(rootComment);

        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(type, adminBoard.getId(), rootComment.getId(), content);

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(2);
        assertBoardComment(boardCommentList.get(0), type, adminBoard.getId(), memberId, "부모 댓글", 0);
        assertThat(boardCommentList.get(0).getParentComment()).isNull();

        assertBoardComment(boardCommentList.get(1), type, adminBoard.getId(), memberId, content, 1);
        assertThat(boardCommentList.get(1).getParentComment().getId()).isEqualTo(boardCommentList.get(0).getId());
    }

    @Test
    void 존재하지_않는_게시물에_댓글을_추가할_수_없다() {
        // given
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardCommentType.ORGANIZATION_BOARD, 999L, null, "없는 게시물에 댓글");

        // when & then
        assertThatThrownBy(() -> boardCommentService.addBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void boardId가_같더라도_타입이_다른경우_존재하지_않는_게시물에_취급된다() {
        // given
        adminBoardRepository.save(adminBoard);
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardCommentType.ORGANIZATION_BOARD, adminBoard.getId(), null, "없는 게시물에 댓글");

        // when & then
        assertThatThrownBy(() -> boardCommentService.addBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 존재하지_않는_댓글에_대댓글을_추가할_수_없다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardCommentType.ORGANIZATION_BOARD, organizationBoard.getId(), 999L, "대댓글");

        // when & then
        assertThatThrownBy(() -> boardCommentService.addBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 특정_그룹_게시물의_댓글들을_불러온다() {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        BoardComment rootComment1 = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "부모 댓글1");
        rootComment1.addChildComment(memberId, "자식 댓글1-1");
        rootComment1.addChildComment(memberId, "자식 댓글1-2");
        BoardComment rootComment2 = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "부모 댓글2");
        rootComment2.addChildComment(memberId, "자식 댓글2-1");
        boardCommentRepository.saveAll(Arrays.asList(rootComment1, rootComment2));

        // when
        List<BoardCommentResponse> responses = boardCommentService.retrieveBoardCommentList(type, organizationBoard.getId());

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

    @Test
    void 특정_관리자_게시물의_댓글들을_불러온다() {
        // given
        adminBoardRepository.save(adminBoard);

        BoardCommentType type = BoardCommentType.ADMIN_BOARD;
        BoardComment rootComment1 = BoardCommentCreator.createRootComment(type, adminBoard.getId(), memberId, "부모 댓글1");
        rootComment1.addChildComment(memberId, "자식 댓글1-1");
        rootComment1.addChildComment(memberId, "자식 댓글1-2");
        BoardComment rootComment2 = BoardCommentCreator.createRootComment(type, adminBoard.getId(), memberId, "부모 댓글2");
        rootComment2.addLike(memberId);
        rootComment2.addChildComment(memberId, "자식 댓글2-1");
        boardCommentRepository.saveAll(Arrays.asList(rootComment1, rootComment2));

        // when
        List<BoardCommentResponse> responses = boardCommentService.retrieveBoardCommentList(type, adminBoard.getId());

        // then
        assertThat(responses).hasSize(2);
        assertBoardCommentResponse(responses.get(0), "부모 댓글1");
        assertBoardCommentResponse(responses.get(1), "부모 댓글2");

        assertThat(responses.get(0).getChildren()).hasSize(2);
        assertBoardCommentResponse(responses.get(0).getChildren().get(0), "자식 댓글1-1");
        assertBoardCommentResponse(responses.get(0).getChildren().get(1), "자식 댓글1-2");

        assertThat(responses.get(1).getChildren()).hasSize(1);
        assertBoardCommentResponse(responses.get(1).getChildren().get(0), "자식 댓글2-1");

        assertThat(responses.get(1).getBoardCommentLikeCounts()).isEqualTo(1);
    }

    @Test
    void 특정_게시물의_댓글을_조회할때_게시물_종류가_다르면_boardId가_같더라도_조회가_되지않는다() {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardComment rootComment1 = BoardCommentCreator.createRootComment(BoardCommentType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, "부모 댓글1");
        boardCommentRepository.save(rootComment1);

        // when & then
        assertThatThrownBy(() -> boardCommentService.retrieveBoardCommentList(BoardCommentType.ADMIN_BOARD, 999L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 존재하지_않는_게시물의_댓글을_불러올_수_없다() {
        // when & then
        assertThatThrownBy(() -> boardCommentService.retrieveBoardCommentList(BoardCommentType.ADMIN_BOARD, 999L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 작성한_댓글을_삭제한다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        // when
        boardCommentService.deleteBoardComment(comment.getId(), memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(1);
        assertBoardComment(boardCommentList.get(0), type, organizationBoard.getId(), memberId, comment.getContent(), 0);
        assertThat(boardCommentList.get(0).isDeleted()).isTrue();
    }

    @Test
    void 내가_작성하지_않은_댓글을_삭제할_수_없다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardComment comment = BoardCommentCreator.createRootComment(BoardCommentType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        // when & then
        assertThatThrownBy(() -> boardCommentService.deleteBoardComment(comment.getId(), 999L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 삭제된_댓글은_작성자가_보이지않고_삭제된_메시지가_보인다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        comment.delete();
        boardCommentRepository.save(comment);

        // when
        List<BoardCommentResponse> responses = boardCommentService.retrieveBoardCommentList(type, organizationBoard.getId());

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getContent()).isNotEqualTo(comment.getContent());
        assertThat(responses.get(0).getMemberId()).isNull();
    }

    @Test
    void 댓글에_좋아요를_한다_댓글에_좋아요수가_올라간다() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        //when
        boardCommentService.likeBoardComment(comment.getId(), memberId);

        //then
        List<BoardCommentLike> boardCommentLikeList = boardCommentLikeRepository.findAll();
        assertThat(boardCommentLikeList).hasSize(1);
        assertBoardCommentLike(boardCommentLikeList.get(0), comment.getId(), memberId);
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList.get(0).getCommentLikeCounts()).isEqualTo(1);
    }

    @Test
    void 대댓글에_좋아요를_하면_대댓글_좋아요수가_올라간다() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        comment.addChildComment(memberId, "대댓글_좋아요");
        boardCommentRepository.save(comment);

        //when
        boardCommentService.likeBoardComment(comment.getChildComments().get(0).getId(), memberId);

        //then
        List<BoardCommentLike> boardCommentLikeList = boardCommentLikeRepository.findAll();
        assertThat(boardCommentLikeList).hasSize(1);
        assertBoardCommentLike(boardCommentLikeList.get(0), comment.getChildComments().get(0).getId(), memberId);
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList.get(1).getCommentLikeCounts()).isEqualTo(1);
    }

    @Test
    void 댓글에_이미_좋아요를_했을경우_애러발생() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        comment.addLike(memberId);
        boardCommentRepository.save(comment);

        assertThatThrownBy(
            () -> boardCommentService.likeBoardComment(comment.getId(), memberId)
        ).isInstanceOf(ConflictException.class);
    }

    @Test
    void 댓글이_삭제된_댓글일_경우_애러발생() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        comment.delete();
        boardCommentRepository.save(comment);

        assertThatThrownBy(
            () -> boardCommentService.likeBoardComment(comment.getId(), memberId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 댓글에_좋아요한것을_좋아요_취소한다() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        comment.addLike(memberId);
        boardCommentRepository.save(comment);

        //when
        boardCommentService.unLikeBoardComment(comment.getId(), memberId);

        //then
        List<BoardCommentLike> boardCommentLikeList = boardCommentLikeRepository.findAll();
        assertThat(boardCommentLikeList).isEmpty();
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList.get(0).getCommentLikeCounts()).isEqualTo(0);
    }

    @Test
    void 댓글에_좋아요를_하지않았는데_좋아요취소하면_애러발생() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardCommentType type = BoardCommentType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        // when & then
        assertThatThrownBy(
            () -> boardCommentService.unLikeBoardComment(comment.getId(), memberId)
        ).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("이미 삭제된 댓글에 누른 좋아요를 취소하려하면 NOT FOUND EXCEPTION이 발생하면서 취소할 수 없다")
    @Test
    void 이미_삭제된_댓글에_누른_좋아요를_취소할_수없다() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardComment comment = BoardCommentCreator.createRootComment(BoardCommentType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, "댓글");
        comment.delete();
        boardCommentRepository.save(comment);

        // when & then
        assertThatThrownBy(() -> boardCommentService.unLikeBoardComment(comment.getId(), memberId)).isInstanceOf(NotFoundException.class);
    }

    private void assertBoardCommentLike(BoardCommentLike boardCommentLike, Long boardCommentId, Long memberId) {
        assertThat(boardCommentLike.getMemberId()).isEqualTo(memberId);
        assertThat(boardCommentLike.getBoardComment().getId()).isEqualTo(boardCommentId);
    }

    private void assertBoardCommentResponse(BoardCommentResponse response, String content) {
        assertThat(response.getContent()).isEqualTo(content);
    }

    private void assertBoardComment(BoardComment boardComment, BoardCommentType type, Long boardId, Long memberId, String content, int depth) {
        assertThat(boardComment.getType()).isEqualTo(type);
        assertThat(boardComment.getBoardId()).isEqualTo(boardId);
        assertThat(boardComment.getMemberId()).isEqualTo(memberId);
        assertThat(boardComment.getContent()).isEqualTo(content);
        assertThat(boardComment.getDepth()).isEqualTo(depth);
    }

}
