package com.potato.recruit.domain.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    public void cleanUp() {
        boardRepository.deleteAll();
    }

    @Test
    void 저장한게시글_가져오기() {
        // given
        String name = "고예림";
        String password = "potato_is_delicious";
        Integer studentId = 201710052;
        String major = "컴퓨터공학과";
        String phoneNumber = "01012345678";
        String experience = "감자의 일원으로 활동하고 있습니다.";
        String portFolio = "github/yerimkoko";

        boardRepository.save(Board.builder()
            .name(name)
            .password(password)
            .major(major)
            .phoneNumber(phoneNumber)
            .studentId(studentId)
            .experience(experience)
            .portFolio(portFolio)
            .build());

        // when
        List<Board> boardList = boardRepository.findAll();

        assertThat(boardList).hasSize(1);

        // then
        Board board = boardList.get(0);
        assertThat(board.getExperience()).isEqualTo(experience);
        assertThat(board.getName()).isEqualTo(name);
        assertThat(board.getMajor()).isEqualTo(major);
        assertThat(board.getStudentId()).isEqualTo(studentId);
        assertThat(board.getPassword()).isEqualTo(password);

        assertThat(board.getPhoneNumber().getMobileCarrier()).isEqualTo("010");
        assertThat(board.getPhoneNumber().getMiddleNumber()).isEqualTo("1234");
        assertThat(board.getPhoneNumber().getLastNumber()).isEqualTo("5678");
    }

}
