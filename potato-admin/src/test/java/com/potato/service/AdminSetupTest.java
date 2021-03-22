package com.potato.service;

import com.potato.domain.adminMember.AdminMember;
import com.potato.domain.adminMember.AdminMemberCreator;
import com.potato.domain.adminMember.AdminMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminSetupTest {

    @Autowired
    protected AdminMemberRepository adminMemberRepository;

    protected Long adminMemberId;

    @BeforeEach
    void setUp() {
        AdminMember adminMember = adminMemberRepository.save(AdminMemberCreator.create("admin@gmail.com", "admin"));
        adminMemberId = adminMember.getId();
    }

    protected void cleanUp() {
        adminMemberRepository.deleteAll();
    }

}
