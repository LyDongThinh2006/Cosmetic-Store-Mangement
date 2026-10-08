package com.thinh.cosmetic.security;

import com.thinh.cosmetic.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AuthCookieFactory {

    public static final String COOKIE_NAME = "LUNEA_AT";

    private final AppProperties appProperties;

    public ResponseCookie createAccessTokenCookie(String token) {
        long accessTtl = appProperties.getJwt().getAccessTtl();
        boolean secure = appProperties.getSecurity().isCookieSecure();

        return ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofSeconds(accessTtl))
                .build();
    }

    public ResponseCookie createLogoutCookie() {
        boolean secure = appProperties.getSecurity().isCookieSecure();

        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ZERO)
                .build();
    }
}
