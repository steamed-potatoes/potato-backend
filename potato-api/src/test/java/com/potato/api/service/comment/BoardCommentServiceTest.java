package com.potato.api.service.comment;

import com.potato.api.service.comment.dto.request.DeleteBoardCommentRequest;
import com.potato.api.service.comment.dto.request.LikeBoardCommentRequest;
import com.potato.api.service.comment.dto.request.RetrieveBoardCommentsRequest;
import com.potato.common.exception.model.ForbiddenException;
import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.board.admin.AdminBoard;
import com.potato.domain.domain.board.admin.AdminBoardCreator;
import com.potato.domain.domain.board.admin.AdminBoardRepository;
import com.potato.domain.domain.board.organization.OrganizationBoard;
import com.potato.domain.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.domain.comment.BoardComment;
import com.potato.domain.domain.comment.BoardCommentCreator;
import com.potato.domain.domain.comment.BoardCommentLike;
import com.potato.domain.domain.comment.BoardCommentLikeRepository;
import com.potato.domain.domain.comment.BoardCommentRepository;
import com.potato.common.exception.model.ConflictException;
import com.potato.common.exception.model.NotFoundException;
import com.potato.api.service.OrganizationMemberSetUpTest;
import com.potato.api.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.api.service.comment.dto.request.UpdateBoardCommentRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private BoardCommentLikeRepository boardCommentLikeRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAllInBatch();
        adminBoardRepository.deleteAllInBatch();
        boardCommentRepository.deleteAll();
    }

    private OrganizationBoard organizationBoard;
    private AdminBoard adminBoard;

    @BeforeEach
    void setUpOrganizationBoard() {
        organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, "게시물", OrganizationBoardCategory.RECRUIT);
        adminBoard = AdminBoardCreator.create("관리자 공지 게시물", memberId);
    }

    @Test
    void 그룹_에서_업로드한_게시물에_새로운_댓글을_작성한다() {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardType type = BoardType.ORGANIZATION_BOARD;
        String content = "부모 댓글";
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(type, organizationBoard.getId(), null, content);

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(1);
        assertBoardComment(boardCommentList.get(0), type, organizationBoard.getId(), memberId, content, 0);
    }

    @Test
    void 최상위_댓글을_작성하면_부모_댓글_필드는_NULL_로_저장된다() {
        // given
        organizationBoardRepository.save(organizationBoard);

        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), null, "루트 댓글");

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(1);
        assertThat(boardCommentList.get(0).getParentComment()).isNull();
    }

    @Test
    void 관리자_게시물에_새로운_댓글을_작성한다() {
        // given
        adminBoardRepository.save(adminBoard);

        BoardType type = BoardType.ADMIN_BOARD;
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
    void 그룹_게시물에_작성된_댓글에_대댓글을_추가한다() {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardType type = BoardType.ORGANIZATION_BOARD;
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
    void 관리자_게시물에_작성된_댓글에_대댓글을_추가한다() {
        // given
        adminBoardRepository.save(adminBoard);

        BoardType type = BoardType.ADMIN_BOARD;
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

    @DisplayName("댓글은 2depth 까지만 허용한다")
    @Test
    void 대댓글에_댓글을_달수없다() {
        // given
        BoardType type = BoardType.ORGANIZATION_BOARD;
        organizationBoardRepository.save(organizationBoard);

        BoardComment rootComment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "루트 댓글");
        rootComment.addChildComment(memberId, "대댓글");
        boardCommentRepository.save(rootComment);

        Long childCommentId = boardCommentRepository.findAll().get(1).getId();
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(type, organizationBoard.getId(), childCommentId, "대댓글에 댓글을 추가한다");

        // when & then
        assertThatThrownBy(() -> boardCommentService.addBoardComment(request, memberId)).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 존재하지_않는_게시물에_댓글을_추가할_수_없다() {
        // given
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardType.ORGANIZATION_BOARD, 999L, null, "없는 게시물에 댓글");

        // when & then
        assertThatThrownBy(() -> boardCommentService.addBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void boardId가_같더라도_타입이_다른경우_존재하지_않는_게시물에_취급된다() {
        // given
        adminBoardRepository.save(adminBoard);
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardType.ORGANIZATION_BOARD, adminBoard.getId(), null, "없는 게시물에 댓글");

        // when & then
        assertThatThrownBy(() -> boardCommentService.addBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 존재하지_않는_댓글에_대댓글을_추가할_수_없다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), 999L, "대댓글");

        // when & then
        assertThatThrownBy(() -> boardCommentService.addBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 특정_게시물의_댓글을_조회할때_게시물_종류가_다르면_boardId가_같더라도_조회가_되지않는다() {
        // given
        organizationBoardRepository.save(organizationBoard);

        BoardComment rootComment1 = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, "부모 댓글1");
        boardCommentRepository.save(rootComment1);

        RetrieveBoardCommentsRequest request = RetrieveBoardCommentsRequest.testInstance(BoardType.ADMIN_BOARD, 999L);

        // when & then
        assertThatThrownBy(() -> boardCommentService.retrieveBoardCommentList(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 존재하지_않는_게시물의_댓글을_불러올_수_없다() {
        // given
        RetrieveBoardCommentsRequest request = RetrieveBoardCommentsRequest.testInstance(BoardType.ADMIN_BOARD, 999L);

        // when & then
        assertThatThrownBy(() -> boardCommentService.retrieveBoardCommentList(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 작성한_댓글을_수정하면_db에_수정이_된다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardComment comment = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, "변경되기 전 댓글입니다.");
        boardCommentRepository.save(comment);
        String content = "변경한 댓글입니다.";
        UpdateBoardCommentRequest request = UpdateBoardCommentRequest.testInstance(comment.getId(), content);

        // when
        boardCommentService.updateBoardComment(request, memberId);

        // then
        List<BoardComment> findBoardComments = boardCommentRepository.findAll();
        assertThat(findBoardComments).hasSize(1);
        assertThat(findBoardComments.get(0).getContent()).isEqualTo(content);
        assertThat(findBoardComments.get(0).getBoardId()).isEqualTo(comment.getBoardId());
        assertThat(findBoardComments.get(0).getMemberId()).isEqualTo(memberId);
        assertThat(findBoardComments.get(0).isDeleted()).isFalse();
    }

    @Test
    void 내가_작성하지_않은_댓글을_수정할_수_없다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardComment comment = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), 100L, "댓글");
        boardCommentRepository.save(comment);

        UpdateBoardCommentRequest request = UpdateBoardCommentRequest.testInstance(comment.getId(), "댓글 내용");

        // when
        assertThatThrownBy(() -> boardCommentService.updateBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 작성한_댓글을_삭제한다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        DeleteBoardCommentRequest request = DeleteBoardCommentRequest.testInstance(comment.getId());

        // when
        boardCommentService.deleteBoardComment(request, memberId);

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
        BoardComment comment = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        DeleteBoardCommentRequest request = DeleteBoardCommentRequest.testInstance(comment.getId());

        // when & then
        assertThatThrownBy(() -> boardCommentService.deleteBoardComment(request, 999L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 댓글에_좋아요를_추가할수있다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        LikeBoardCommentRequest request = LikeBoardCommentRequest.testInstance(comment.getId());

        // when
        boardCommentService.likeBoardComment(request, memberId);

        // then
        List<BoardCommentLike> boardCommentLikeList = boardCommentLikeRepository.findAll();
        assertThat(boardCommentLikeList).hasSize(1);
        assertBoardCommentLike(boardCommentLikeList.get(0), comment.getId(), memberId);
    }

    @Test
    void 대댓글에_좋아요를_하면_대댓글_좋아요수가_올라간다() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        comment.addChildComment(memberId, "대댓글_좋아요");
        boardCommentRepository.save(comment);

        LikeBoardCommentRequest request = LikeBoardCommentRequest.testInstance(comment.getChildComments().get(0).getId());

        //when
        boardCommentService.likeBoardComment(request, memberId);

        //then
        List<BoardCommentLike> boardCommentLikeList = boardCommentLikeRepository.findAll();
        assertThat(boardCommentLikeList).hasSize(1);
        assertBoardCommentLike(boardCommentLikeList.get(0), comment.getChildComments().get(0).getId(), memberId);
    }

    @Test
    void 댓글에_이미_좋아요를_했을경우_애러발생() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        comment.addLike(memberId);
        boardCommentRepository.save(comment);

        LikeBoardCommentRequest request = LikeBoardCommentRequest.testInstance(comment.getId());

        // when & then
        assertThatThrownBy(() -> boardCommentService.likeBoardComment(request, memberId)).isInstanceOf(ConflictException.class);
    }

    @Test
    void 댓글이_삭제된_댓글일_경우_애러발생() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        comment.delete();
        boardCommentRepository.save(comment);

        LikeBoardCommentRequest request = LikeBoardCommentRequest.testInstance(comment.getId());

        // when & then
        assertThatThrownBy(() -> boardCommentService.likeBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 댓글에_좋아요한것을_좋아요_취소한다() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        comment.addLike(memberId);
        boardCommentRepository.save(comment);

        LikeBoardCommentRequest request = LikeBoardCommentRequest.testInstance(comment.getId());

        //when
        boardCommentService.unLikeBoardComment(request, memberId);

        //then
        List<BoardCommentLike> boardCommentLikeList = boardCommentLikeRepository.findAll();
        assertThat(boardCommentLikeList).isEmpty();
    }

    @Test
    void 댓글에_좋아요를_하지않았는데_좋아요취소하면_애러가_발생한다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardType type = BoardType.ORGANIZATION_BOARD;
        BoardComment comment = BoardCommentCreator.createRootComment(type, organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        LikeBoardCommentRequest request = LikeBoardCommentRequest.testInstance(comment.getId());

        // when & then
        assertThatThrownBy(() -> boardCommentService.unLikeBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("이미 삭제된 댓글에 누른 좋아요를 취소하려하면 NOT FOUND EXCEPTION이 발생하면서 취소할 수 없다")
    @Test
    void 이미_삭제된_댓글에_누른_좋아요를_취소할_수없다() {
        //given
        organizationBoardRepository.save(organizationBoard);
        BoardComment comment = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, organizationBoard.getId(), memberId, "댓글");
        comment.delete();
        boardCommentRepository.save(comment);

        LikeBoardCommentRequest request = LikeBoardCommentRequest.testInstance(comment.getId());

        // when & then
        assertThatThrownBy(() -> boardCommentService.unLikeBoardComment(request, memberId)).isInstanceOf(NotFoundException.class);
    }

    private void assertBoardCommentLike(BoardCommentLike boardCommentLike, Long boardCommentId, Long memberId) {
        assertThat(boardCommentLike.getMemberId()).isEqualTo(memberId);
        assertThat(boardCommentLike.getBoardComment().getId()).isEqualTo(boardCommentId);
    }

    private void assertBoardComment(BoardComment boardComment, BoardType type, Long boardId, Long memberId, String content, int depth) {
        assertThat(boardComment.getType()).isEqualTo(type);
        assertThat(boardComment.getBoardId()).isEqualTo(boardId);
        assertThat(boardComment.getMemberId()).isEqualTo(memberId);
        assertThat(boardComment.getContent()).isEqualTo(content);
        assertThat(boardComment.getDepth()).isEqualTo(depth);
    }

}
