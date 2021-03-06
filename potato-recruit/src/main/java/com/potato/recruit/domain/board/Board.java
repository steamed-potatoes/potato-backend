package com.potato.recruit.domain.board;

import com.potato.recruit.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 10)
    private Integer studentId;

    @Column(nullable = false, length = 30)
    private String major;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String experience;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String portFolio;

    @Column
    private boolean isDeleted;

    @Embedded
    private PhoneNumber phoneNumber;

    @Builder
    public Board (Long id, String name, String password, String phoneNumber, Integer studentId, String major, String experience, String portFolio) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.studentId = studentId;
        this.major = major;
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.experience = experience;
        this.portFolio = portFolio;
        this.isDeleted = false;
    }

}
