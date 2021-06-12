package com.potato.recruit.dto;

import com.potato.recruit.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSaveRequestDto {

    private Long id;

    private String name;

    private String password;

    private Integer studentId;

    private String major;

    private String phoneNumber;

    private String experience;

    private String portFolio;

    @Builder
    public BoardSaveRequestDto(Long id, String name, String password, Integer studentId, String major, String phoneNumber, String experience, String portFolio) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.studentId = studentId;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.experience = experience;
        this.portFolio = portFolio;
    }

    public Board entity() {
        return Board.builder().id(id).name(name).password(password).studentId(studentId).major(major).phoneNumber(phoneNumber).experience(experience).portFolio(portFolio).build();
    }

}
