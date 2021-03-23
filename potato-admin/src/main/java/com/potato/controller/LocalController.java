package com.potato.controller;

import com.potato.config.session.AdminMemberSession;
import com.potato.domain.administrator.Administrator;
import com.potato.domain.administrator.AdministratorCreator;
import com.potato.domain.administrator.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.potato.config.session.SessionConstants.AUTH_SESSION;

@Profile("local")
@RequiredArgsConstructor
@RestController
public class LocalController {

    private final HttpSession httpSession;
    private final AdministratorRepository administratorRepository;

    @GetMapping("/test-session")
    public ApiResponse<String> getSession() {
        Administrator administrator = administratorRepository.save(AdministratorCreator.create("admin.test@gmail.com", "테스트 관리자"));
        httpSession.setAttribute(AUTH_SESSION, AdminMemberSession.of(administrator.getId()));
        return ApiResponse.success(httpSession.getId());
    }

}
