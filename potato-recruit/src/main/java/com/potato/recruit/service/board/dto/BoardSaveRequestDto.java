package com.potato.recruit.service.board.dto;

import com.potato.recruit.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BoardSaveRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotNull(message = "학번을 입력해주세요.")
    private Integer studentId;

    @NotBlank(message = "전공을 입력해주세요.")
    private String major;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

    @NotBlank(message = "활동경험을 입력해주세요.")
    private String experience;

    private String portFolio;

    @Builder
    public BoardSaveRequestDto(String name, String password, String phoneNumber, Integer studentId, String major, String experience, String portFolio) {
        this.name = name;
        this.password = password;
        this.studentId = studentId;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.experience = experience;
        this.portFolio = portFolio;
    }

    public Board toEntity() {
        return Board.builder()
            .name(name)
            .password(password)
            .studentId(studentId)
            .major(major)
            .phoneNumber(phoneNumber)
            .experience(experience)
            .portFolio(portFolio)
            .build();
    }

}
