package com.potato.recruit.service.board;

import com.potato.recruit.domain.board.Board;
import com.potato.recruit.domain.board.BoardRepository;
import com.potato.recruit.domain.board.PhoneNumber;
import com.potato.recruit.service.board.dto.BoardSaveRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void cleanUp() {
        boardRepository.deleteAll();
    }

    @Test
    void board에_저장이된다() {
        //given
        String name = "고예림";
        Integer studentId = 201710052;
        String major = "컴퓨터공학과";
        String experience = "감자의 일원으로 활동하고 있습니다.";
        String portFolio = "github/yerimkoko";
        String password = "werwerwer";
        String phoneNumber = "01012345678";

        BoardSaveRequestDto saveDto = BoardSaveRequestDto.builder()
            .name(name)
            .studentId(studentId)
            .password(password)
            .major(major)
            .experience(experience)
            .portFolio(portFolio)
            .phoneNumber(phoneNumber)
            .build();

        //when
        boardService.createBoard(saveDto);

        //then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(1);
        Board board = boardList.get(0);

        assertThat(board.getExperience()).isEqualTo(saveDto.getExperience());
        assertThat(board.getName()).isEqualTo(saveDto.getName());
        assertThat(board.getMajor()).isEqualTo(saveDto.getMajor());
        assertThat(board.getStudentId()).isEqualTo(saveDto.getStudentId());
        assertThat(board.getPhoneNumber()).isEqualTo(new PhoneNumber(saveDto.getPhoneNumber()));
        assertThat(board.getPortFolio()).isEqualTo(saveDto.getPortFolio());
        assertThat(board.getPassword()).isEqualTo(saveDto.getPassword());
    }

}
