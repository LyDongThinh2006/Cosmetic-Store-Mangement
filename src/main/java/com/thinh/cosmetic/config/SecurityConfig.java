package com.thinh.cosmetic.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.thinh.cosmetic.security.CookieBearerTokenResolver;
import com.thinh.cosmetic.security.JsonAccessDeniedHandler;
import com.thinh.cosmetic.security.JsonAuthEntryPoint;
import com.thinh.cosmetic.security.JwtAuthConverter;
import com.thinh.cosmetic.security.XRequestedWithFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppProperties appProperties;
    private final CookieBearerTokenResolver cookieBearerTokenResolver;
    private final JwtAuthConverter jwtAuthConverter;
    private final XRequestedWithFilter xRequestedWithFilter;
    private final JsonAuthEntryPoint jsonAuthEntryPoint;
    private final JsonAccessDeniedHandler jsonAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey jwtSecretKey() {
        byte[] keyBytes = appProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey) {
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey secretKey) {
        JWKSource<SecurityContext> jwks = new ImmutableSecret<>(secretKey.getEncoded());
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(xRequestedWithFilter, BearerTokenAuthenticationFilter.class)
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/admin/**", "/api/admin/**").hasAuthority("ROLE_EMPLOYEE")
                        .requestMatchers("/cart", "/checkout", "/order-success", "/account/**",
                                "/api/cart/**", "/api/checkout/**", "/api/orders/**").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers("/api/account/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(jsonAuthEntryPoint)
                        .accessDeniedHandler(jsonAccessDeniedHandler)
                )
                .oauth2ResourceServer(o -> o
                        .bearerTokenResolver(cookieBearerTokenResolver)
                        .authenticationEntryPoint(jsonAuthEntryPoint)
                        .accessDeniedHandler(jsonAccessDeniedHandler)
                        .jwt(j -> j.jwtAuthenticationConverter(jwtAuthConverter))
                );

        return http.build();
    }
}