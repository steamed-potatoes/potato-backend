package com.potato.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    void 멤버를_저장한다() {
        // when
        memberRepository.save(MemberCreator.create("will.seungho@gmail.com"));

        // then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(1);
    }

}
