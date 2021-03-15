package com.potato.domain.board;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrganizationBoardRepositoryTest {

    @Autowired
    private OrganizationBoardRepository organizationBoardRepository;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void cleanUp() {
        organizationBoardRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
    }

    @Test
    void 그룹_게시물을_저장한다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain("subDomain")
            .memberId(1L)
            .startDateTime(LocalDateTime.of(2021, 3, 5, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 7, 0, 0))
            .title("감자 신입회원 모집")
            .content("감자팀 신입회원을 모집합니다. 많은 참여바랍니다")
            .imageUrl("http://image.com")
            .type(OrganizationBoardType.RECRUIT)
            .build();

        // when
        organizationBoardRepository.save(organizationBoard);

        // then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);

        List<OrganizationBoard> organizationRepository = organizationBoardRepository.findAll();
        assertThat(organizationRepository).hasSize(1);
    }

    @Test
    void 그룹_게시물을_삭제한다() {
        // given
        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain("subDomain")
            .memberId(1L)
            .startDateTime(LocalDateTime.of(2021, 3, 5, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 7, 0, 0))
            .title("감자 신입회원 모집")
            .content("감자팀 신입회원을 모집합니다. 많은 참여바랍니다")
            .imageUrl("http://image.com")
            .type(OrganizationBoardType.RECRUIT)
            .build();

        // when
        organizationBoardRepository.save(organizationBoard);

        organizationBoardRepository.delete(organizationBoard);

        // then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).isEmpty();

        List<OrganizationBoard> organizationRepository = organizationBoardRepository.findAll();
        assertThat(organizationRepository).isEmpty();
    }

}
