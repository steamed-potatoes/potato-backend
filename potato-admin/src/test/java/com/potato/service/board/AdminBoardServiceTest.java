package com.potato.service.board;

import com.potato.domain.board.Board;
import com.potato.domain.board.BoardRepository;
import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import com.potato.service.AdminSetupTest;
import com.potato.service.board.dto.request.CreateAdminBoardRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AdminBoardServiceTest extends AdminSetupTest {

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @Autowired
    private AdminBoardService adminBoardService;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void cleanup() {
        super.cleanUp();
        adminBoardRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
    }

    @Test
    void 관리자_게시글에_글을_쓴다() {
        //given
        String content = "content";
        String title = "title";
        CreateAdminBoardRequest request = CreateAdminBoardRequest.testBuilder()
            .content(content)
            .title(title)
            .startDateTime(LocalDateTime.of(2021,9, 3, 12,12))
            .endDateTime(LocalDateTime.of(2021, 9, 5, 12,12))
            .build();

        //when
        adminBoardService.createAdminBoard(request, adminMemberId);

        //then
        List<AdminBoard> adminBoardList = adminBoardRepository.findAll();
        assertThat(adminBoardList).hasSize(1);
        assertThat(adminBoardList.get(0).getContent()).isEqualTo(content);
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        assertThat(boardList.get(0).getTitle()).isEqualTo(title);
    }

}
