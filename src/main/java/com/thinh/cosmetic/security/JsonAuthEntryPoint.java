package com.thinh.cosmetic.security;

import tools.jackson.databind.ObjectMapper;
import com.thinh.cosmetic.domain.dto.response.ErrorResponse;
import com.thinh.cosmetic.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JsonAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final AuthCookieFactory authCookieFactory;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        String path = request.getRequestURI();

        if (path.startsWith("/api/")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ErrorCode errorCode = (authException instanceof BadCredentialsException)
                    ? ErrorCode.INVALID_CREDENTIALS
                    : ErrorCode.UNAUTHENTICATED;

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .code(errorCode.name())
                    .message(authException != null && authException.getMessage() != null ? authException.getMessage() : errorCode.getDefaultMessage())
                    .path(path)
                    .build();

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } else {
            // HTML page navigation without valid auth: clear invalid cookie and redirect to login
            response.addHeader("Set-Cookie", authCookieFactory.createLogoutCookie().toString());
            String encodedRedirect = URLEncoder.encode(path, StandardCharsets.UTF_8);
            response.sendRedirect("/login?redirect=" + encodedRedirect);
        }
    }
}
