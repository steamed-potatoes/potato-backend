package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardRepository;
import com.potato.domain.board.Category;
import com.potato.domain.board.Visible;
import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;
import com.potato.domain.organization.OrganizationCreator;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.exception.NotFoundException;
import com.potato.service.MemberSetupTest;
import com.potato.service.board.dto.request.CreateBoardRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class BoardServiceTest extends MemberSetupTest {

    @Autowired
    private BoardAdminService boardAdminService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @AfterEach
    void cleanUp() {
        boardRepository.deleteAll();
    }

    @Test
    void 어드민이_게시글을_생성한다() {
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

        Organization organization = OrganizationCreator.create(subDomain, "감자", "스터디입니다.", "123", OrganizationCategory.NON_APPROVED_CIRCLE);
        organization.addAdmin(memberId);
        organizationRepository.save(organization);

        //when
        boardAdminService.createBoard(subDomain, request, memberId);

        //then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertBoard(boardList.get(0), title, content, imageUrl);
    }

//    @Test
//    void 조직이_없을_경우() {
//        //given
//        String subDomain = "감자";
//        String title = "감자모집";
//        String content = "감자인원을 모집합니다.";
//        String imageUrl = "123";
//
//        CreateBoardRequest request = CreateBoardRequest.testBuilder()
//            .visible(Visible.PUBLIC)
//            .title(title)
//            .content(content)
//            .imageUrl(imageUrl)
//            .category(Category.RECRUIT)
//            .build();
//
//        // when & then
//        assertThatThrownBy(
//            () -> boardAdminService.createBoard(subDomain, request, memberId)
//        ).isInstanceOf(NotFoundException.class);
//    }

    private void assertBoard(Board board, String title, String content, String imageUrl) {
        assertThat(board.getVisible()).isEqualTo(Visible.PUBLIC);
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getImageUrl()).isEqualTo(imageUrl);
        assertThat(board.getCategory()).isEqualTo(Category.RECRUIT);
    }

}
