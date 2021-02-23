package com.potato.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.potato.config.interceptor.Auth.Role.ORGANIZATION_ADMIN;

@RequiredArgsConstructor
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final LoginUserComponent loginUserComponent;
    private final OrganizationAdminComponent organizationAdminComponent;

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
        Long memberId = loginUserComponent.getMemberId(request);
        request.setAttribute("memberId", memberId);

        if (auth.role().compareTo(ORGANIZATION_ADMIN) == 0) {
            organizationAdminComponent.validateOrganizationAdmin(request, memberId);
        }
        return true;
    }

}
