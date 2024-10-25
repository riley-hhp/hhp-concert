package io.hhplus.concert.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
public class CustomLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청 및 응답을 캐시할 래퍼
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        // 요청 처리 시작 시간
        long startTime = System.currentTimeMillis();

        try {
            // 요청 정보 로깅
            logRequestDetails(cachingRequestWrapper);

            // 필터 체인을 통해 요청 처리
            filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);
        } finally {
            // 요청 처리 시간 계산
            long duration = System.currentTimeMillis() - startTime;

            // 응답 정보 로깅
            logResponseDetails(cachingResponseWrapper, duration);
            cachingResponseWrapper.copyBodyToResponse();  // 응답 본문 복사
        }
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) throws IOException {
        String requestBody = new String(request.getContentAsByteArray(), request.getCharacterEncoding());
        log.info("REQUEST - Method: {}, URI: {}, Body: {}", request.getMethod(), request.getRequestURI(), requestBody);
    }

    private void logResponseDetails(ContentCachingResponseWrapper response, long duration) throws IOException {
        String responseBody = new String(response.getContentAsByteArray(), response.getCharacterEncoding());
        log.info("RESPONSE - Status: {}, Content-Type: {}, Body: {}, Duration: {} ms",
                response.getStatus(), response.getContentType(), responseBody, duration);
    }
}