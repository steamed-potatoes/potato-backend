package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.Category;
import com.potato.domain.board.DeletedBoard;
import com.potato.domain.board.Visible;
import com.potato.service.board.dto.response.BoardInfoResponse;
import com.potato.service.board.dto.response.BoardWithCreatorInfoResponse;

import static com.potato.service.member.MemberServiceTestUtils.assertMemberInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;

final class BoardServiceTestUtils {

    static void assertBoard(Board board, Visible visible, String title, String content, String imageUrl, Category category) {
        assertThat(board.getVisible()).isEqualTo(visible);
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getImageUrl()).isEqualTo(imageUrl);
        assertThat(board.getCategory()).isEqualTo(category);
    }

    static void assertBoardInfoResponse(BoardInfoResponse response, Visible visible, String title, String content, String imageUrl, Category category) {
        assertThat(response.getVisible()).isEqualTo(visible);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getImageUrl()).isEqualTo(imageUrl);
        assertThat(response.getCategory()).isEqualTo(category);
    }

    static void assertBoardInfoResponse(BoardInfoResponse response, String title) {
        assertThat(response.getTitle()).isEqualTo(title);
    }

    static void assertDeletedBoard(DeletedBoard deletedBoard, Long backUpId, String subDomain, Long memberId, String title,
                                   String content, Category category, String imageUrl, Visible visible) {
        assertThat(deletedBoard.getBackUpId()).isEqualTo(backUpId);
        assertThat(deletedBoard.getSubDomain()).isEqualTo(subDomain);
        assertThat(deletedBoard.getMemberId()).isEqualTo(memberId);
        assertThat(deletedBoard.getTitle()).isEqualTo(title);
        assertThat(deletedBoard.getContent()).isEqualTo(content);
        assertThat(deletedBoard.getCategory()).isEqualTo(category);
        assertThat(deletedBoard.getImageUrl()).isEqualTo(imageUrl);
        assertThat(deletedBoard.getVisible()).isEqualTo(visible);
    }

    static void assertBoardWithCreatorInfoResponse(BoardWithCreatorInfoResponse response, String title, Long memberId) {
        assertBoardInfoResponse(response.getBoard(), title);
        assertMemberInfoResponse(response.getCreator(), memberId);
    }

}
