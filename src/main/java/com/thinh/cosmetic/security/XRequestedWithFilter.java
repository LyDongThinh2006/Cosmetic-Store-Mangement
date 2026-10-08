package com.thinh.cosmetic.security;

import tools.jackson.databind.ObjectMapper;
import com.thinh.cosmetic.domain.dto.response.ErrorResponse;
import com.thinh.cosmetic.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class XRequestedWithFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Apply only to state-modifying REST endpoints (/api/**)
        if (path.startsWith("/api/") && isStateModifying(method)) {
            String authHeader = request.getHeader("Authorization");
            // If request uses Bearer token in header, it's immune to cookie CSRF
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                String requestedWith = request.getHeader("X-Requested-With");
                if (requestedWith == null || (!requestedWith.equalsIgnoreCase("fetch") && !requestedWith.equalsIgnoreCase("XMLHttpRequest"))) {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");

                    ErrorResponse errorResponse = ErrorResponse.builder()
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.FORBIDDEN.value())
                            .code(ErrorCode.ACCESS_DENIED.name())
                            .message("Missing X-Requested-With header for state-modifying request")
                            .path(path)
                            .build();

                    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isStateModifying(String method) {
        return "POST".equalsIgnoreCase(method) ||
                "PUT".equalsIgnoreCase(method) ||
                "PATCH".equalsIgnoreCase(method) ||
                "DELETE".equalsIgnoreCase(method);
    }
}
