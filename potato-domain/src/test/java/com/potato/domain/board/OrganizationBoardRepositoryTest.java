package com.potato.domain.board;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardCreator;
import com.potato.domain.board.organization.OrganizationBoardRepository;
import com.potato.domain.board.organization.OrganizationBoardType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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
    void OrganizaitonBoard저장시_Board도_함께_저장된다() {
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
    void OrganizationBoard를_삭제하면_Board도_삭제된다() {
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

    @Test
    void 그룹_게시물_조회_시_앞쪽에_걸치는경우_포함된다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain("subDomain")
            .memberId(1L)
            .startDateTime(LocalDateTime.of(2021, 2, 28, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 1, 0, 1))
            .title("감자 신입회원 모집")
            .content("감자팀 신입회원을 모집합니다. 많은 참여바랍니다")
            .imageUrl("http://image.com")
            .type(OrganizationBoardType.RECRUIT)
            .build();
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllBetweenDate(startDate, endDate);

        // then
        assertThat(organizationBoardList).hasSize(1);
    }

    @Test
    void 그룹_게시물_뒷쪽에_걸치는경우_포함된다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain("subDomain")
            .memberId(1L)
            .startDateTime(LocalDateTime.of(2021, 3, 31, 11, 59))
            .endDateTime(LocalDateTime.of(2021, 4, 1, 0, 0))
            .title("감자 신입회원 모집")
            .content("감자팀 신입회원을 모집합니다. 많은 참여바랍니다")
            .imageUrl("http://image.com")
            .type(OrganizationBoardType.RECRUIT)
            .build();
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllBetweenDate(startDate, endDate);

        // then
        assertThat(organizationBoardList).hasSize(1);
    }

    @Test
    void 그룹_게시_조회_시_앞쪽_경계선을_넘어서는경우_포함되지_않는다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain("subDomain")
            .memberId(1L)
            .startDateTime(LocalDateTime.of(2021, 2, 28, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 1, 0, 0))
            .title("감자 신입회원 모집")
            .content("감자팀 신입회원을 모집합니다. 많은 참여바랍니다")
            .imageUrl("http://image.com")
            .type(OrganizationBoardType.RECRUIT)
            .build();
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllBetweenDate(startDate, endDate);

        // then
        assertThat(organizationBoardList).isEmpty();
    }

    @Test
    void 그룹_게시물_조회_시_뒷쪽_경계선을_넘어서는경우_포함되지_않는다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain("subDomain")
            .memberId(1L)
            .startDateTime(LocalDateTime.of(2021, 4, 1, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 4, 2, 0, 0))
            .title("감자 신입회원 모집")
            .content("감자팀 신입회원을 모집합니다. 많은 참여바랍니다")
            .imageUrl("http://image.com")
            .type(OrganizationBoardType.RECRUIT)
            .build();
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllBetweenDate(startDate, endDate);

        // then
        assertThat(organizationBoardList).isEmpty();
    }

    @Test
    void 그룹_게시물_조회_시_안쪽에_있는경우_포함된다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        OrganizationBoard organizationBoard = OrganizationBoard.builder()
            .subDomain("subDomain")
            .memberId(1L)
            .startDateTime(LocalDateTime.of(2021, 3, 15, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 16, 0, 0))
            .title("감자 신입회원 모집")
            .content("감자팀 신입회원을 모집합니다. 많은 참여바랍니다")
            .imageUrl("http://image.com")
            .type(OrganizationBoardType.RECRUIT)
            .build();
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllBetweenDate(startDate, endDate);

        // then
        assertThat(organizationBoardList).hasSize(1);
    }

    @Test
    void findBetween_아직_시작하지_않았고_종료날짜가_대상_종료날짜보다_이전인경우_포함된다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 2, 11, 59);
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create("subDomain", 1L, "게시물", startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllByBetweenDateTimeWithLimit(LocalDateTime.of(2021, 4, 1, 0, 0), LocalDateTime.of(2021, 4, 3, 0, 0), 1);

        // then
        assertThat(organizationBoardList).hasSize(1);
    }

    @Test
    void findBetween_이미_시작하고_종료날짜가_대상_종료날짜보다_이전인경우_포함된다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 31, 11, 59);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 2, 11, 59);
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create("subDomain", 1L, "게시물", startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllByBetweenDateTimeWithLimit(LocalDateTime.of(2021, 4, 1, 0, 0), LocalDateTime.of(2021, 4, 3, 0, 0), 1);

        // then
        assertThat(organizationBoardList).hasSize(1);
    }

    @Test
    void findBetween_이미_시작하였고_종료날짜가_대상종료날짜보다_이후인경우_포함되지_않는다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 3, 0, 1);
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create("subDomain", 1L, "게시물", startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllByBetweenDateTimeWithLimit(LocalDateTime.of(2021, 4, 1, 0, 0), LocalDateTime.of(2021, 4, 3, 0, 0), 1);

        // then
        assertThat(organizationBoardList).isEmpty();
    }

    @Test
    void findBetween_아예_끝난경우_포함되지_않는다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 31, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 3, 31, 11, 59);
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create("subDomain", 1L, "게시물", startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllByBetweenDateTimeWithLimit(LocalDateTime.of(2021, 4, 1, 0, 0), LocalDateTime.of(2021, 4, 3, 0, 0), 1);

        // then
        assertThat(organizationBoardList).isEmpty();
    }

    @Test
    void findBetween_거의_종료되가지만_아직_안끝난경우_포함된다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 3, 31, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 1, 0, 1);
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create("subDomain", 1L, "게시물", startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllByBetweenDateTimeWithLimit(LocalDateTime.of(2021, 4, 1, 0, 0), LocalDateTime.of(2021, 4, 3, 0, 0), 1);

        // then
        assertThat(organizationBoardList).hasSize(1);
    }

    @Test
    void findBetween_아직_시작하지_않았지만_종료날짜가_대상종료날짜보다_이후인경우_포함되지_않는다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 4, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 4, 10, 0, 0);
        OrganizationBoard organizationBoard = OrganizationBoardCreator.create("subDomain", 1L, "게시물", startDateTime, endDateTime, OrganizationBoardType.RECRUIT);
        organizationBoardRepository.save(organizationBoard);

        // when
        List<OrganizationBoard> organizationBoardList = organizationBoardRepository.findAllByBetweenDateTimeWithLimit(LocalDateTime.of(2021, 4, 1, 0, 0), LocalDateTime.of(2021, 4, 3, 0, 0), 1);

        // then
        assertThat(organizationBoardList).isEmpty();
    }

}
