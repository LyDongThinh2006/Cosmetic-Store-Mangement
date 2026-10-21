package com.thinh.cosmetic.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.thinh.cosmetic.security.AuthCookieFactory;
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
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration(proxyBeanMethods = false)
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
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000))
                )
                .addFilterBefore(xRequestedWithFilter, BearerTokenAuthenticationFilter.class)
                .authorizeHttpRequests(a -> a
                        // ─── Trang web tĩnh & public ───
                        .requestMatchers("/", "/home", "/products", "/products/**",
                                "/product", "/product/**",
                                "/brands", "/stores", "/login", "/register",
                                "/reset-password", "/error", "/error/**",
                                "/css/**", "/js/**", "/images/**", "/fonts/**",
                                "/favicon.ico", "/robots.txt", "/sitemap.xml", "/site.webmanifest",
                                "/webjars/**").permitAll()

                        // ─── API công khai (không cần đăng nhập) ───
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**",
                                "/api/categories/**", "/api/brands/**",
                                "/api/stores", "/api/stores/{id}",
                                "/api/reviews/product/**").permitAll()

                        // ─── Module Quản lý Nhân sự & Phân quyền (Chỉ Admin / EMPLOYEE_MANAGE) ───
                        .requestMatchers("/admin/employees", "/admin/employees/**",
                                "/api/employees", "/api/employees/**",
                                "/api/roles", "/api/roles/**").access(new WebExpressionAuthorizationManager("authentication.name == 'admin' or hasAnyAuthority('EMPLOYEE_MANAGE', 'ROLE_ADMIN', 'ADMIN')"))

                        // ─── Module Quản lý Đơn hàng (Admin / Nhân viên đơn hàng) ───
                        .requestMatchers("/admin/orders", "/admin/orders/**").access(new WebExpressionAuthorizationManager("authentication.name == 'admin' or hasAnyAuthority('ORDER_VIEW', 'ORDER_PROCESS', 'ROLE_ORDER_STAFF', 'ROLE_ADMIN', 'ADMIN')"))
                        .requestMatchers("/api/orders/{id}/status").hasAnyAuthority("ORDER_PROCESS", "ROLE_ORDER_STAFF", "ROLE_ADMIN", "ADMIN")

                        // ─── Module Quản lý Tồn kho & Nhập hàng (Admin / Quản lý kho / Thủ kho) ───
                        .requestMatchers("/admin/inventory", "/admin/inventory/**",
                                "/admin/suppliers", "/admin/suppliers/**",
                                "/admin/purchase-orders", "/admin/purchase-orders/**",
                                "/admin/stock-transfers", "/admin/stock-transfers/**",
                                "/admin/vouchers", "/admin/vouchers/**").access(new WebExpressionAuthorizationManager("authentication.name == 'admin' or hasAnyAuthority('INVENTORY_VIEW', 'INVENTORY_ADJUST', 'PURCHASE_MANAGE', 'ROLE_WAREHOUSE_STAFF', 'ROLE_ADMIN', 'ADMIN')"))
                        .requestMatchers("/api/inventory/**").hasAnyAuthority("INVENTORY_VIEW", "INVENTORY_ADJUST", "ROLE_WAREHOUSE_STAFF", "ROLE_ADMIN", "ADMIN")
                        .requestMatchers("/api/purchase-orders/**", "/api/suppliers/**").hasAnyAuthority("PURCHASE_MANAGE", "ROLE_WAREHOUSE_STAFF", "ROLE_ADMIN", "ADMIN")
                        .requestMatchers("/api/stock-transfers/**").hasAnyAuthority("INVENTORY_VIEW", "INVENTORY_ADJUST", "ROLE_WAREHOUSE_STAFF", "ROLE_ADMIN", "ADMIN")

                        // ─── Module Quản lý Sản phẩm, Danh mục, Đánh giá & Chi nhánh ───
                        .requestMatchers("/admin/products", "/admin/products/**",
                                "/admin/reviews", "/admin/reviews/**").access(new WebExpressionAuthorizationManager("authentication.name == 'admin' or hasAnyAuthority('CATALOG_MANAGE', 'ROLE_PRODUCT_MANAGER', 'ROLE_ADMIN', 'ADMIN')"))
                        .requestMatchers(HttpMethod.POST, "/api/products", "/api/products/**").hasAnyAuthority("CATALOG_MANAGE", "ROLE_PRODUCT_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyAuthority("CATALOG_MANAGE", "ROLE_PRODUCT_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyAuthority("CATALOG_MANAGE", "ROLE_PRODUCT_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/categories", "/api/categories/**").hasAnyAuthority("CATALOG_MANAGE", "ROLE_PRODUCT_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasAnyAuthority("CATALOG_MANAGE", "ROLE_PRODUCT_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasAnyAuthority("CATALOG_MANAGE", "ROLE_PRODUCT_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/brands", "/api/brands/**").hasAnyAuthority("CATALOG_MANAGE", "ROLE_PRODUCT_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/brands/**").hasAnyAuthority("CATALOG_MANAGE", "ROLE_PRODUCT_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/brands/**").hasAnyAuthority("CATALOG_MANAGE", "ROLE_PRODUCT_MANAGER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/stores").hasAnyAuthority("STORE_MANAGE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/stores/**").hasAnyAuthority("STORE_MANAGE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/stores/**").hasAnyAuthority("STORE_MANAGE", "ROLE_ADMIN")

                        // ─── Portal Quản trị chung (Chuyển tiếp theo nghiệp vụ nhân viên) ───
                        .requestMatchers("/admin", "/api/admin/**").hasAuthority("ROLE_EMPLOYEE")
                        .requestMatchers("/api/orders/my-orders", "/api/orders/preview").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers("/api/orders/{id}/cancel").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers("/cart", "/checkout", "/order-success", "/account", "/account/**",
                                "/api/cart/**", "/api/checkout/**", "/api/wishlist/**", "/api/wishlist").hasAuthority("ROLE_CUSTOMER")

                        // ─── API đánh giá & đổi trả & khách hàng ───
                        .requestMatchers("/api/customers/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/reviews/my-reviews").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/reviews/*/status").hasAuthority("ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/reviews").hasAuthority("ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/returns").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers("/api/returns/customer/**", "/api/returns/order/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/returns/*/status").hasAuthority("ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/returns").hasAuthority("ROLE_EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/returns/{id}").authenticated()


                        .requestMatchers("/api/orders", "/api/orders/**").authenticated()
                        .requestMatchers("/api/account/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(jsonAuthEntryPoint)
                        .accessDeniedHandler(jsonAccessDeniedHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies(AuthCookieFactory.COOKIE_NAME, "JSESSIONID")
                        .logoutSuccessUrl("/login")
                        .permitAll()
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