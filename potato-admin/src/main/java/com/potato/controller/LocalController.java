package com.potato.controller;

import com.potato.config.session.AdminMemberSession;
import com.potato.domain.adminMember.AdminMember;
import com.potato.domain.adminMember.AdminMemberCreator;
import com.potato.domain.adminMember.AdminMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.potato.config.session.SessionConstants.AUTH_SESSION;

@Profile({"local", "dev"})
@RequiredArgsConstructor
@RestController
public class LocalController {

    private final HttpSession httpSession;
    private final AdminMemberRepository adminMemberRepository;

    @GetMapping("/test-session")
    public ApiResponse<String> getSession() {
        AdminMember adminMember = adminMemberRepository.save(AdminMemberCreator.create("admin.test@gmail.com", "테스트 관리자"));
        httpSession.setAttribute(AUTH_SESSION, AdminMemberSession.of(adminMember.getId()));
        return ApiResponse.success(httpSession.getId());
    }

}
