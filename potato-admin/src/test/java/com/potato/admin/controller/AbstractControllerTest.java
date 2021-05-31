package com.potato.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.admin.controller.admin.api.AdministratorMockMvc;
import com.potato.domain.domain.administrator.Administrator;
import com.potato.domain.domain.administrator.AdministratorCreator;
import com.potato.domain.domain.administrator.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AdministratorRepository administratorRepository;

    protected Administrator testAdministrator;

    protected AdministratorMockMvc administratorMockMvc;

    protected final String administratorEmail = "administrator@gmail.com";

    protected void setup() {
        testAdministrator = administratorRepository.save(AdministratorCreator.create(administratorEmail, "administrator"));
        administratorMockMvc = new AdministratorMockMvc(mockMvc, objectMapper);
    }

    protected void cleanup() {
        administratorRepository.deleteAll();
    }

}
