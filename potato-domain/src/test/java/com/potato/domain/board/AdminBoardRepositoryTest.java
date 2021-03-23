package com.potato.domain.board;

import com.potato.domain.board.admin.AdminBoard;
import com.potato.domain.board.admin.AdminBoardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdminBoardRepositoryTest {

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @AfterEach
    void cleanUp() {
        adminBoardRepository.deleteAll();
    }

    @Test
    void 관리자_게시물_조회_시_앞쪽에_걸치는경우_포함된다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(1L)
            .title("학사일정")
            .content("학사일정 뭐시기")
            .startDateTime(LocalDateTime.of(2021, 2, 28, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 1, 0, 1))
            .build();

        adminBoardRepository.save(adminBoard);

        // when
        List<AdminBoard> adminBoardList = adminBoardRepository.findBetweenDate(startDate, endDate);

        // then
        assertThat(adminBoardList).hasSize(1);
    }

    @Test
    void 관리자_게시물_조회_시_뒷쪽에_걸치는경우_포함된다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(1L)
            .title("학사일정")
            .content("학사일정 뭐시기")
            .startDateTime(LocalDateTime.of(2021, 3, 31, 11, 59))
            .endDateTime(LocalDateTime.of(2021, 4, 1, 0, 0))
            .build();

        adminBoardRepository.save(adminBoard);

        // when
        List<AdminBoard> adminBoardList = adminBoardRepository.findBetweenDate(startDate, endDate);

        // then
        assertThat(adminBoardList).hasSize(1);
    }

    @Test
    void 관리자_게시물_조회_시_앞쪽_경계선을_넘어서는경우_포함되지_않는다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(1L)
            .title("학사일정")
            .content("학사일정 뭐시기")
            .startDateTime(LocalDateTime.of(2021, 2, 28, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 1, 0, 0))
            .build();

        adminBoardRepository.save(adminBoard);

        // when
        List<AdminBoard> adminBoardList = adminBoardRepository.findBetweenDate(startDate, endDate);

        // then
        assertThat(adminBoardList).isEmpty();
    }

    @Test
    void 관리자_게시물_조회_시_뒷쪽_경계선을_넘어서는경우_포함되지_않는다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(1L)
            .title("학사일정")
            .content("학사일정 뭐시기")
            .startDateTime(LocalDateTime.of(2021, 2, 28, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 1, 0, 0))
            .build();

        adminBoardRepository.save(adminBoard);

        // when
        List<AdminBoard> adminBoardList = adminBoardRepository.findBetweenDate(startDate, endDate);

        // then
        assertThat(adminBoardList).isEmpty();
    }

    @Test
    void 관리자_게시물_조회_시_안쪽에_있는경우_포함된다() {
        // given
        LocalDate startDate = LocalDate.of(2021, 3, 1);
        LocalDate endDate = LocalDate.of(2021, 3, 31);

        AdminBoard adminBoard = AdminBoard.builder()
            .administratorId(1L)
            .title("학사일정")
            .content("학사일정 뭐시기")
            .startDateTime(LocalDateTime.of(2021, 3, 15, 0, 0))
            .endDateTime(LocalDateTime.of(2021, 3, 16, 0, 0))
            .build();

        adminBoardRepository.save(adminBoard);

        // when
        List<AdminBoard> adminBoardList = adminBoardRepository.findBetweenDate(startDate, endDate);

        // then
        assertThat(adminBoardList).hasSize(1);
    }

}
