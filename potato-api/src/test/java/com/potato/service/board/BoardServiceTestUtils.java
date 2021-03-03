package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.Category;
import com.potato.domain.board.Visible;
import com.potato.service.board.dto.response.BoardInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;

final class BoardServiceTestUtils {

    static void assertBoard(Board board, Visible visible, String title, String content, String imageUrl, Category category) {
        assertThat(board.getVisible()).isEqualTo(visible);
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getImageUrl()).isEqualTo(imageUrl);
        assertThat(board.getCategory()).isEqualTo(category);
    }

    static void assertBoardInfo(BoardInfoResponse response, Visible visible, String title, String content, String imageUrl, Category category) {
        assertThat(response.getVisible()).isEqualTo(visible);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getImageUrl()).isEqualTo(imageUrl);
        assertThat(response.getCategory()).isEqualTo(category);
    }

}
