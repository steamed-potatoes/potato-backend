package com.potato.domain.domain.administrator;

import com.potato.domain.domain.BaseTimeEntity;
import com.potato.domain.domain.member.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(name = "uni_administrator_1", columnNames = {"email"})
)
public class Administrator extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Column(nullable = false, length = 50)
    private String name;

    @Builder()
    public Administrator(String email, String name) {
        this.email = Email.of(email);
        this.name = name;
    }

}

