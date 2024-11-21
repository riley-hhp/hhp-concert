package io.hhplus.concert.config.interceptor;

import io.hhplus.concert.app.application.waitingqueue.WaitingQueueUsecase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final WaitingQueueUsecase waitingQueueUsecase;
    private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        if (StringUtils.hasText(token) && StringUtils.hasText(waitingQueueUsecase.getToken(token).getToken())) {
            return true;
        } else {
            logger.warn("Unauthorized access attempt with invalid token from IP: {}", request.getRemoteAddr());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access: Invalid token");
            return false;
        }
    }
}