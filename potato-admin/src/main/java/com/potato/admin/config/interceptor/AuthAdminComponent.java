package com.potato.admin.config.interceptor;

import com.potato.admin.config.session.AdminSession;
import com.potato.admin.config.session.SessionConstants;
import com.potato.domain.domain.administrator.AdministratorRepository;
import com.potato.common.exception.model.UnAuthorizedException;
import com.potato.admin.service.auth.AdminAuthServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class AuthAdminComponent {

    private static final String BEARER_TOKEN = "Bearer ";

    private final SessionRepository<? extends Session> sessionRepository;
    private final AdministratorRepository administratorRepository;

    public Long getAdminMemberId(HttpServletRequest request) {
        Long memberId = getAdminMemberSession(request).getAdminId();
        AdminAuthServiceUtils.validateExistAdminMember(administratorRepository, memberId);
        return memberId;
    }

    private AdminSession getAdminMemberSession(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        Session session = extractSessionFromHeader(header);
        return session.getAttribute(SessionConstants.AUTH_SESSION);
    }

    private Session extractSessionFromHeader(String header) {
        if (header == null) {
            throw new UnAuthorizedException("세션이 없습니다");
        }
        if (!header.startsWith(BEARER_TOKEN)) {
            throw new UnAuthorizedException(String.format("잘못된 세션입니다 (%s)", header));
        }
        Session session = sessionRepository.findById(header.split(BEARER_TOKEN)[1]);
        if (session == null) {
            throw new UnAuthorizedException(String.format("잘못된 세션입니다 (%s)", header));
        }
        return session;
    }

}
