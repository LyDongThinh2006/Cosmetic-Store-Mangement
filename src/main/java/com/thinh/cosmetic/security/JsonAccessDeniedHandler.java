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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        String path = request.getRequestURI();

        if (path.startsWith("/api/")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.FORBIDDEN.value())
                    .code(ErrorCode.ACCESS_DENIED.name())
                    .message("Access is denied")
                    .path(path)
                    .build();

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            request.getRequestDispatcher("/error/403").forward(request, response);
        }
    }
}
