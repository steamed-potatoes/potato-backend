package com.potato.api.config.interceptor.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.potato.api.config.interceptor.auth.Auth.Role.OPTIONAL_LOGIN;
import static com.potato.api.config.interceptor.auth.Auth.Role.ORGANIZATION_ADMIN;
import static com.potato.api.config.interceptor.auth.Auth.Role.ORGANIZATION_MEMBER;

@RequiredArgsConstructor
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static final String MEMBER_ID = "memberId";

    private final LoginUserComponent loginUserComponent;
    private final OrganizationComponent organizationComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        if (auth == null) {
            return true;
        }

        if (auth.role().compareTo(OPTIONAL_LOGIN) == 0) {
            Long memberId = loginUserComponent.getMemberIdIfExists(request);
            request.setAttribute(MEMBER_ID, memberId);
            return true;
        }

        Long memberId = loginUserComponent.getMemberId(request);
        request.setAttribute(MEMBER_ID, memberId);

        // 그룹의 관리자인지 확인
        if (auth.role().compareTo(ORGANIZATION_ADMIN) == 0) {
            organizationComponent.validateOrganizationAdmin(request, memberId);
        }

        // 그룹에 속해있는 멤버인지 확인 (관리자 or 일반 멤버)
        if (auth.role().compareTo(ORGANIZATION_MEMBER) == 0) {
            organizationComponent.validateOrganizationMember(request, memberId);
        }
        return true;
    }

}
