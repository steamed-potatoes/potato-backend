package com.potato.service;

import com.potato.domain.administrator.Administrator;
import com.potato.domain.administrator.AdministratorCreator;
import com.potato.domain.administrator.AdministratorRepository;
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
