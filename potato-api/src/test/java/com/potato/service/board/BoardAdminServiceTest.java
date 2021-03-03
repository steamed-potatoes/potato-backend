package com.potato.service.board;

import com.potato.domain.board.*;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;
import com.potato.domain.organization.OrganizationCreator;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.exception.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.board.dto.request.CreateBoardRequest;
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
public class BoardAdminServiceTest extends MemberSetupTest {

    @Autowired
    private BoardAdminService boardAdminService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        boardRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    void 어드민이_게시글을_생성한다() {
        //given
        String subDomain = "감자";

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

        Organization organization = OrganizationCreator.create(subDomain, "감자", "스터디입니다.", "123", OrganizationCategory.NON_APPROVED_CIRCLE);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        //when
        boardAdminService.createBoard(subDomain, request, memberId);

        //then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertBoard(boardList.get(0), visible, title, content, imageUrl, category);
    }

    @Test
    void 조직이_없을_경우() {
        //given
        String subDomain = "감자";
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
            () -> boardAdminService.createBoard(subDomain, request, memberId)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 퍼블릭_게시글을_변경한다() {
        //given
        Board board = BoardCreator.create("title", "content", "imageUrl");
        boardRepository.save(board);

        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        String updateImageUrl = "updateImageUrl";

        CreateBoardRequest request = CreateBoardRequest.testBuilder()
            .visible(board.getVisible())
            .title(updateTitle)
            .content(updateContent)
            .imageUrl(updateImageUrl)
            .category(board.getCategory())
            .build();

        //when
        BoardInfoResponse response = boardAdminService.updatePublicBoard(board.getId(), request);

        //then
        assertBoardInfo(response, board.getVisible(), updateTitle, updateContent, updateImageUrl, board.getCategory());
    }

    @Test
    void 게시글이_퍼블릭게시글이_존재하지_않을경우() {
        //given
        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        String updateImageUrl = "updateImageUrl";

        CreateBoardRequest request = CreateBoardRequest.testBuilder()
            .visible(Visible.PUBLIC)
            .title(updateTitle)
            .content(updateContent)
            .imageUrl(updateImageUrl)
            .category(Category.RECRUIT)
            .build();

        //when & then
        assertThatThrownBy(
            () -> boardAdminService.updatePublicBoard(1L, request)
        ).isInstanceOf(NotFoundException.class);
    }

}
