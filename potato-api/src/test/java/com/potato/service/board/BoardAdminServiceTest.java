package com.potato.service.board;

import com.potato.OrganizationMemberSetUpTest;
import com.potato.domain.board.*;
import com.potato.exception.NotFoundException;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.request.UpdateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.potato.service.board.BoardServiceTestUtils.assertBoard;
import static com.potato.service.board.BoardServiceTestUtils.assertBoardInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BoardAdminServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private BoardAdminService boardAdminService;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        boardRepository.deleteAll();
    }

    @Test
    void 조직의_관리자가_새로운_게시글을_생성한다() {
        //given
        Visible visible = Visible.PUBLIC;
        String title = "감자모집";
        String content = "감자인원을 모집합니다.";
        String imageUrl = "123";
        Category category = Category.RECRUIT;

        CreateBoardRequest request = CreateBoardRequest.testBuilder()
            .visible(visible)
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .category(category)
            .build();

        //when
        boardAdminService.createBoard(subDomain, request, memberId);

        //then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertBoard(boardList.get(0), visible, title, content, imageUrl, category);
    }

    @Test
    void 게시물을을_생성할때_해당하는_조직이_없을_경우_생성할_수없다() {
        //given
        String title = "감자모집";
        String content = "감자인원을 모집합니다.";
        String imageUrl = "123";

        CreateBoardRequest request = CreateBoardRequest.testBuilder()
            .visible(Visible.PUBLIC)
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .category(Category.RECRUIT)
            .build();

        // when & then
        assertThatThrownBy(
            () -> boardAdminService.createBoard("wrong-suDomain", request, memberId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 게시글을_변경한다() {
        //given
        Board board = BoardCreator.create(subDomain, memberId, "title", "content", "imageUrl");
        boardRepository.save(board);

        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        String updateImageUrl = "updateImageUrl";
        Category updateCategory = Category.RECRUIT;

        UpdateBoardRequest request = UpdateBoardRequest.testBuilder()
            .visible(board.getVisible())
            .title(updateTitle)
            .content(updateContent)
            .imageUrl(updateImageUrl)
            .category(updateCategory)
            .build();

        //when
        BoardInfoResponse response = boardAdminService.updateBoard(board.getSubDomain(), board.getId(), request);

        //then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertBoard(boardList.get(0), board.getVisible(), updateTitle, updateContent, updateImageUrl, updateCategory);

        assertBoardInfo(response, board.getVisible(), updateTitle, updateContent, updateImageUrl, board.getCategory());
    }

    @Test
    void 게시물_변경시_subDomain_이_다르면_변경할_수없다() {
        // given
        boardRepository.save(BoardCreator.create(subDomain, memberId, "title"));

        UpdateBoardRequest request = UpdateBoardRequest.testBuilder()
            .visible(Visible.PUBLIC)
            .category(Category.RECRUIT)
            .build();

        //when & then
        assertThatThrownBy(
            () -> boardAdminService.updateBoard("another-subDomain", memberId, request)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 게시물_변경시_해당하는_게시물이_존재하지_않을경우_에러가_발생한다() {
        //given
        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        String updateImageUrl = "updateImageUrl";

        UpdateBoardRequest request = UpdateBoardRequest.testBuilder()
            .visible(Visible.PUBLIC)
            .title(updateTitle)
            .content(updateContent)
            .imageUrl(updateImageUrl)
            .category(Category.RECRUIT)
            .build();

        //when & then
        assertThatThrownBy(
            () -> boardAdminService.updateBoard(subDomain, 999L, request)
        ).isInstanceOf(NotFoundException.class);
    }

}
