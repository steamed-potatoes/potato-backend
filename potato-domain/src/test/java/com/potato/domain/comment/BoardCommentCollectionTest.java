package com.potato.domain.comment;

import com.potato.domain.domain.board.BoardType;
import com.potato.domain.domain.comment.BoardComment;
import com.potato.domain.domain.comment.BoardCommentCollection;
import com.potato.domain.domain.comment.BoardCommentCreator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardCommentCollectionTest {

    @Test
    void 자신을_포함한_하위_댓글들의_작성자_PK를_가져온다() {
        // given
        Long boardId = 100L;
        BoardComment parent = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, boardId, 10L, "부모 댓글");
        parent.addChildComment(11L, "자식 댓글1");
        parent.addChildComment(12L, "자식 댓글2");

        BoardCommentCollection collection = BoardCommentCollection.of(Collections.singletonList(parent));

        // when
        List<Long> authorIds = collection.getAuthorIds();

        // then
        assertThat(authorIds).containsAll(Arrays.asList(10L, 11L, 12L));
    }

    @Test
    void 따로_대댓글이_없는경우_루트_댓글의_작성자_PK만_가져온다() {
        // given
        Long boardId = 100L;
        BoardComment parent = BoardCommentCreator.createRootComment(BoardType.ORGANIZATION_BOARD, boardId, 10L, "부모 댓글");

        BoardCommentCollection collection = BoardCommentCollection.of(Collections.singletonList(parent));

        // when
        List<Long> authorIds = collection.getAuthorIds();

        // then
        assertThat(authorIds).isEqualTo(Collections.singletonList(10L));
    }

    @Test
    void 빈_댓글이_넘어오면_작성자_PK_가_빈_리스트가_반환된다() {
        // given
        BoardCommentCollection collection = BoardCommentCollection.of(Collections.emptyList());

        // when
        List<Long> authorIds = collection.getAuthorIds();

        // then
        assertThat(authorIds).isEmpty();
    }

}
