package com.thinh.cosmetic.rest.account;

import com.thinh.cosmetic.domain.dto.request.account.LoginRequest;
import com.thinh.cosmetic.domain.dto.request.account.RegisterRequest;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;
import com.thinh.cosmetic.domain.dto.response.account.LoginResponse;
import com.thinh.cosmetic.domain.dto.response.account.LoginResult;
import com.thinh.cosmetic.security.AuthCookieFactory;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.account.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AccountService accountService;
    private final AuthCookieFactory authCookieFactory;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> register(@Valid @RequestBody RegisterRequest request) {
        CustomerResponse response = accountService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = accountService.login(request);
        ResponseCookie cookie = authCookieFactory.createAccessTokenCookie(result.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.getResponse());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie logoutCookie = authCookieFactory.createLogoutCookie();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, logoutCookie.toString())
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthPrincipal> me(@AuthenticationPrincipal AuthPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(principal);
    }
}
