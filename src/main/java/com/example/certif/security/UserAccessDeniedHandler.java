package com.example.certif.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 권한이 부족한 사용자가 접근할 때 "Forbidden" 오류 응답 반환
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }
}
