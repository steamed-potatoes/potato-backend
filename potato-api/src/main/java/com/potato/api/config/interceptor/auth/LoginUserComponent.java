package com.potato.api.config.interceptor.auth;

import com.potato.api.config.session.MemberSession;
import com.potato.api.config.session.SessionConstants;
import com.potato.common.exception.model.UnAuthorizedException;
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
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        Session session = extractSessionFromHeader(header);
        if (session == null) {
            throw new UnAuthorizedException(String.format("잘못된 세션입니다 (%s)", header));
        }
        MemberSession memberSession = session.getAttribute(SessionConstants.AUTH_SESSION);
        return memberSession.getMemberId();
    }

    public Long getMemberIdIfExists(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        Session session = extractSessionFromHeader(header);
        if (session == null) {
            return null;
        }
        MemberSession memberSession = session.getAttribute(SessionConstants.AUTH_SESSION);
        return memberSession.getMemberId();
    }

    private Session extractSessionFromHeader(String header) {
        if (StringUtils.hasText(header) && header.startsWith(BEARER_TOKEN)) {
            return sessionRepository.findById(header.split(BEARER_TOKEN)[1]);
        }
        return null;
    }

}
