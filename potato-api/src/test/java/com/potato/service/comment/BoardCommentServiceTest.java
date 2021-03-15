package com.potato.service.comment;

import com.potato.domain.boardV2.BoardV2Repository;
import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.potato.domain.boardV2.organization.OrganizationBoardCreator;
import com.potato.domain.boardV2.organization.OrganizationBoardRepository;
import com.potato.domain.boardV2.organization.OrganizationBoardType;
import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentCreator;
import com.potato.domain.comment.BoardCommentRepository;
import com.potato.exception.NotFoundException;
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
    private BoardV2Repository boardRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        boardCommentRepository.deleteAll();
    }

    private OrganizationBoard organizationBoard;

    @BeforeEach
    void setUpOrganizationBoard() {
        organizationBoard = OrganizationBoardCreator.create(subDomain, memberId, "게시물", OrganizationBoardType.RECRUIT);
    }

    @Test
    void 가장_최상위의_댓글을_작성한다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        String content = "부모 댓글";
        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(organizationBoard.getId(), null, content);

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(1);
        assertBoardComment(boardCommentList.get(0), organizationBoard.getId(), memberId, content, 0);
        assertThat(boardCommentList.get(0).getParentComment()).isNull();
    }

    @Test
    void 최상위_댓글_하위에_자식_댓글을_추가한다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        String content = "자식 댓글";
        BoardComment rootComment = BoardCommentCreator.createRootComment(organizationBoard.getId(), memberId, "부모 댓글");
        boardCommentRepository.save(rootComment);

        AddBoardCommentRequest request = AddBoardCommentRequest.testInstance(organizationBoard.getId(), rootComment.getId(), content);

        // when
        boardCommentService.addBoardComment(request, memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(2);
        assertBoardComment(boardCommentList.get(0), organizationBoard.getId(), memberId, "부모 댓글", 0);
        assertThat(boardCommentList.get(0).getParentComment()).isNull();

        assertBoardComment(boardCommentList.get(1), organizationBoard.getId(), memberId, content, 1);
        assertThat(boardCommentList.get(1).getParentComment().getId()).isEqualTo(boardCommentList.get(0).getId());
    }

    @Test
    void 특정_게시물의_댓글들을_불러온다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardComment rootComment1 = BoardCommentCreator.createRootComment(organizationBoard.getId(), memberId, "부모 댓글1");
        rootComment1.addChildComment(memberId, "자식 댓글1-1");
        rootComment1.addChildComment(memberId, "자식 댓글1-2");
        BoardComment rootComment2 = BoardCommentCreator.createRootComment(organizationBoard.getId(), memberId, "부모 댓글2");
        rootComment2.addChildComment(memberId, "자식 댓글2-1");
        boardCommentRepository.saveAll(Arrays.asList(rootComment1, rootComment2));

        // when
        List<BoardCommentResponse> responses = boardCommentService.retrieveBoardCommentList(organizationBoard.getId());

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
    void 작성한_댓글을_삭제한다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardComment comment = BoardCommentCreator.createRootComment(organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        // when
        boardCommentService.deleteBoardComment(comment.getId(), memberId);

        // then
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(1);
        assertBoardComment(boardCommentList.get(0), organizationBoard.getId(), memberId, comment.getContent(), 0);
        assertThat(boardCommentList.get(0).isDeleted()).isTrue();
    }

    @Test
    void 내가_작성하지_않은_댓글을_삭제할_수_없다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardComment comment = BoardCommentCreator.createRootComment(organizationBoard.getId(), memberId, "댓글");
        boardCommentRepository.save(comment);

        // when & then
        assertThatThrownBy(() -> boardCommentService.deleteBoardComment(comment.getId(), 999L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 삭제된_댓글은_작성자가_보이지않고_삭제된_메시지가_보인다() {
        // given
        organizationBoardRepository.save(organizationBoard);
        BoardComment comment = BoardCommentCreator.createRootComment(organizationBoard.getId(), memberId, "댓글");
        comment.delete();
        boardCommentRepository.save(comment);

        // when
        List<BoardCommentResponse> responses = boardCommentService.retrieveBoardCommentList(organizationBoard.getId());

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getContent()).isNotEqualTo(comment.getContent());
        assertThat(responses.get(0).getMemberId()).isNull();
    }

    private void assertBoardCommentResponse(BoardCommentResponse response, String content) {
        assertThat(response.getContent()).isEqualTo(content);
    }

    private void assertBoardComment(BoardComment boardComment, Long boardId, Long memberId, String content, int depth) {
        assertThat(boardComment.getOrganizationBoardId()).isEqualTo(boardId);
        assertThat(boardComment.getMemberId()).isEqualTo(memberId);
        assertThat(boardComment.getContent()).isEqualTo(content);
        assertThat(boardComment.getDepth()).isEqualTo(depth);
    }

}
