package com.potato.config.interceptor;

import com.potato.config.session.MemberSession;
import com.potato.config.session.SessionConstants;
import com.potato.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final static String BEARER_TOKEN = "Bearer ";

    private final SessionRepository<? extends Session> sessionRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        if (auth == null) {
            return true;
        }
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        Session session = extractSessionFromHeader(header);
        MemberSession memberSession = session.getAttribute(SessionConstants.AUTH_SESSION);
        request.setAttribute("memberId", memberSession.getMemberId());
        return true;
    }

    private Session extractSessionFromHeader(String header) {
        if (header == null) {
            throw new UnAuthorizedException("세션이 없습니다");
        }
        if (!header.startsWith(BEARER_TOKEN)) {
            throw new UnAuthorizedException(String.format("잘못된 세션입니다 (%s)", header));
        }
        Session session = sessionRepository.getSession(header.split(BEARER_TOKEN)[1]);
        if (session == null) {
            throw new UnAuthorizedException(String.format("잘못된 세션입니다 (%s)", header));
        }
        return session;
    }

}
