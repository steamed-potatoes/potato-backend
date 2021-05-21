package com.potato.admin.controller;

import com.potato.admin.config.session.AdminSession;
import com.potato.domain.domain.administrator.Administrator;
import com.potato.domain.domain.administrator.AdministratorCreator;
import com.potato.domain.domain.administrator.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.potato.admin.config.session.SessionConstants.AUTH_SESSION;

@Profile("local")
@RequiredArgsConstructor
@RestController
public class LocalController {

    private final HttpSession httpSession;
    private final AdministratorRepository administratorRepository;

    @GetMapping("/test-session")
    public ApiResponse<String> getSession() {
        Administrator administrator = administratorRepository.save(AdministratorCreator.create("admin.test@gmail.com", "테스트 관리자"));
        httpSession.setAttribute(AUTH_SESSION, AdminSession.of(administrator.getId()));
        return ApiResponse.success(httpSession.getId());
    }

}
