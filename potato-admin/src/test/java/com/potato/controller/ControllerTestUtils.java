package com.potato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.controller.admin.AdministratorMockMvc;
import com.potato.domain.administrator.Administrator;
import com.potato.domain.administrator.AdministratorCreator;
import com.potato.domain.administrator.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ControllerTestUtils {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AdministratorRepository administratorRepository;

    protected Administrator testAdministrator;

    protected AdministratorMockMvc administratorMockMvc;

    protected String administratorEmail = "administrator@gmail.com";

    protected void setup() {
        testAdministrator = administratorRepository.save(AdministratorCreator.create(administratorEmail, "administrator"));
        administratorMockMvc = new AdministratorMockMvc(mockMvc, objectMapper);
    }

    protected void cleanup() {
        administratorRepository.deleteAll();
    }

}