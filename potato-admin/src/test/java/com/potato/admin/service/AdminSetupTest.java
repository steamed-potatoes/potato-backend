package com.potato.admin.service;

import com.potato.domain.domain.administrator.Administrator;
import com.potato.domain.domain.administrator.AdministratorCreator;
import com.potato.domain.domain.administrator.AdministratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminSetupTest {

    @Autowired
    protected AdministratorRepository administratorRepository;

    protected Long adminMemberId;

    @BeforeEach
    void setUp() {
        Administrator administrator = administratorRepository.save(AdministratorCreator.create("admin@gmail.com", "admin"));
        adminMemberId = administrator.getId();
    }

    protected void cleanUp() {
        administratorRepository.deleteAll();
    }

}
