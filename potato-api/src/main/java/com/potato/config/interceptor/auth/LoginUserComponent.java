package com.potato.config.interceptor.auth;

import com.potato.config.session.MemberSession;
import com.potato.config.session.SessionConstants;
import com.potato.exception.model.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class LoginUserComponent {

    private static final String BEARER_TOKEN = "Bearer ";

    private final SessionRepository<? extends Session> sessionRepository;

    public Long getMemberId(HttpServletRequest request) {
        return getMemberSession(request).getMemberId();
    }

    private MemberSession getMemberSession(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        Session session = extractSessionFromHeader(header);
        return session.getAttribute(SessionConstants.AUTH_SESSION);
    }

    private Session extractSessionFromHeader(String header) {
        if (StringUtils.hasText(header) && header.startsWith(BEARER_TOKEN)) {
            Session session = sessionRepository.getSession(header.split(BEARER_TOKEN)[1]);
            if (session == null) {
                throw new UnAuthorizedException(String.format("잘못된 세션입니다 (%s)", header));
            }
            return session;
        }
        throw new UnAuthorizedException(String.format("잘못된 세션입니다 (%s)", header));
    }

}
