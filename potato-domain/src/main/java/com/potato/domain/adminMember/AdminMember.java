package com.potato.domain.adminMember;

import com.potato.domain.BaseTimeEntity;
import com.potato.domain.member.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AdminMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    private String name;

    @Builder()
    public AdminMember(String email, String name) {
        this.email = Email.of(email);
        this.name = name;
    }

}

