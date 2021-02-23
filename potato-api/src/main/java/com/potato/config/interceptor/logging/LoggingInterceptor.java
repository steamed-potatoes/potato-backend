package com.potato.config.interceptor.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        log.info("PreHandle: Request URL: {} - {} RequestBody : {} Headers: {}",
            request.getMethod(), request.getRequestURI(), objectMapper.readTree(cachingRequest.getContentAsByteArray()), getHeaders(request)
        );
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

        log.info("PostHandle: Request URL: {} - {} {} RequestBody : {} / ResponseBody : {} Headers: {}",
            request.getMethod(), request.getRequestURI(), response.getStatus(),
            objectMapper.readTree(cachingRequest.getContentAsByteArray()),
            objectMapper.readTree(cachingResponse.getContentAsByteArray()),
            getHeaders(request)
        );
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

}
