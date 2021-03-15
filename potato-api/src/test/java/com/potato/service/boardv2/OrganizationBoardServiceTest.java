package com.potato.service.boardv2;

import com.potato.domain.boardV2.BoardV2;
import com.potato.domain.boardV2.BoardV2Repository;
import com.potato.domain.boardV2.organization.OrganizationBoard;
import com.potato.domain.boardV2.organization.OrganizationBoardRepository;
import com.potato.domain.boardV2.organization.OrganizationBoardType;
import com.potato.service.OrganizationMemberSetUpTest;
import com.potato.service.boardv2.dto.request.CreateOrganizationBoardRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrganizationBoardServiceTest extends OrganizationMemberSetUpTest {

    @Autowired
    private OrganizationBoardService organizationBoardService;

    @Autowired
    private BoardV2Repository boardRepository;

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        organizationBoardRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
    }

    @Test
    void 그룹관리자가_게시물을_작성한다() {
        // given
        String title = "감자 신입 회원 모집";
        String content = "감자 동아리에서 신입 회원을 모집합니다";
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 3, 5, 0, 0);
        OrganizationBoardType type = OrganizationBoardType.RECRUIT;

        CreateOrganizationBoardRequest request = CreateOrganizationBoardRequest.testBuilder()
            .title(title)
            .content(content)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .type(type)
            .build();

        // when
        organizationBoardService.createBoard(subDomain, request, memberId);

        // then
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAll();
        assertThat(organizationBoardList).hasSize(1);
        assertOrganizationBoard(organizationBoardList.get(0), content, type, subDomain);

        List<BoardV2> board = boardRepository.findAll();
        assertThat(board).hasSize(1);
        assertBoard(board.get(0), title, memberId, startDateTime, endDateTime);
    }

    private void assertBoard(BoardV2 board, String title, Long memberId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getMemberId()).isEqualTo(memberId);
        assertThat(board.getStartDateTime()).isEqualTo(startDateTime);
        assertThat(board.getEndDateTime()).isEqualTo(endDateTime);
    }

    private void assertOrganizationBoard(OrganizationBoard organizationBoard, String content, OrganizationBoardType type, String subDomain) {
        assertThat(organizationBoard.getContent()).isEqualTo(content);
        assertThat(organizationBoard.getType()).isEqualTo(type);
        assertThat(organizationBoard.getSubDomain()).isEqualTo(subDomain);
    }

}
