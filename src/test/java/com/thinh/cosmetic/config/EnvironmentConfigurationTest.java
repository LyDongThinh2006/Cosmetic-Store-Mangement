package com.thinh.cosmetic.config;

import com.thinh.cosmetic.security.AuthCookieFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnvironmentConfigurationTest {

    private ApplicationContextRunner runner(Map<String, Object> variables) {
        return new ApplicationContextRunner()
                .withInitializer(context -> {
                    var sources = context.getEnvironment().getPropertySources();
                    sources.remove(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);
                    sources.remove(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME);
                    sources.addFirst(new SystemEnvironmentPropertySource(
                            StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, variables));
                })
                // Load the actual application config, not the H2 config that shadows it in tests.
                .withPropertyValues("spring.config.location="
                        + Path.of("src/main/resources/").toAbsolutePath().toUri())
                .withInitializer(new ConfigDataApplicationContextInitializer())
                .withUserConfiguration(PropertiesConfiguration.class);
    }

    @Test
    void localUsesConfiguredCredentialsAndHttpCookies() {
        runner(Map.of(
                "SPRING_PROFILES_ACTIVE", "local",
                "SPRING_DATASOURCE_PASSWORD", "local-password",
                "APP_JWT_SECRET", "local-test-secret-with-at-least-32-bytes"
        )).run(context -> {
            assertThat(context).hasNotFailed();
            var environment = context.getEnvironment();
            assertThat(environment.getProperty("spring.datasource.url"))
                    .isEqualTo("jdbc:postgresql://localhost:5433/lunea");
            assertThat(environment.getProperty("spring.datasource.username")).isEqualTo("lunea");
            assertThat(environment.getProperty("spring.datasource.password")).isEqualTo("local-password");
            assertThat(environment.getProperty("spring.jpa.hibernate.ddl-auto")).isEqualTo("update");
            assertThat(environment.getProperty("server.port")).isEqualTo("8080");
            var properties = context.getBean(AppProperties.class);
            assertThat(keyConfiguration(properties).jwtSecretKey().getEncoded()).hasSizeGreaterThanOrEqualTo(32);
            var cookies = new AuthCookieFactory(properties);
            assertThat(cookies.createAccessTokenCookie("token").isSecure()).isFalse();
            assertThat(cookies.createLogoutCookie().isSecure()).isFalse();
        });
    }

    @Test
    void prodUsesDeploymentVariablesAndValidatesTheSchemaWithHttpsCookies() {
        runner(Map.of(
                "SPRING_PROFILES_ACTIVE", "prod",
                "SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres.railway.internal:5432/railway",
                "SPRING_DATASOURCE_USERNAME", "railway-user",
                "SPRING_DATASOURCE_PASSWORD", "deployment-password",
                "APP_JWT_SECRET", "deployment-only-secret-with-at-least-32-bytes",
                "APP_SEED_ENABLED", "false",
                "PORT", "9123"
        )).run(context -> {
            assertThat(context).hasNotFailed();
            var environment = context.getEnvironment();
            assertThat(environment.getActiveProfiles()).doesNotContain("local");
            assertThat(environment.getProperty("spring.datasource.url"))
                    .isEqualTo("jdbc:postgresql://postgres.railway.internal:5432/railway");
            assertThat(environment.getProperty("spring.datasource.username")).isEqualTo("railway-user");
            assertThat(environment.getProperty("spring.datasource.password")).isEqualTo("deployment-password");
            assertThat(environment.getProperty("spring.jpa.hibernate.ddl-auto")).isEqualTo("validate");
            assertThat(environment.getProperty("server.port")).isEqualTo("9123");
            var properties = context.getBean(AppProperties.class);
            assertThat(properties.getJwt().getSecret()).isEqualTo("deployment-only-secret-with-at-least-32-bytes");
            assertThat(properties.getSeed().isEnabled()).isFalse();
            var cookies = new AuthCookieFactory(properties);
            assertThat(cookies.createAccessTokenCookie("token").isSecure()).isTrue();
            assertThat(cookies.createLogoutCookie().isSecure()).isTrue();
        });
    }

    @Test
    void environmentVariablesOverrideLocalDefaults() {
        runner(Map.of(
                "SPRING_PROFILES_ACTIVE", "local",
                "SPRING_DATASOURCE_URL", "jdbc:postgresql://localhost:6543/custom",
                "SPRING_DATASOURCE_USERNAME", "custom-user",
                "SPRING_DATASOURCE_PASSWORD", "custom-password",
                "APP_JWT_SECRET", "custom-local-secret-with-at-least-32-bytes",
                "APP_SECURITY_COOKIE_SECURE", "true",
                "PORT", "8081"
        )).run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context.getEnvironment().getProperty("spring.datasource.url"))
                    .isEqualTo("jdbc:postgresql://localhost:6543/custom");
            assertThat(context.getEnvironment().getProperty("spring.datasource.username")).isEqualTo("custom-user");
            assertThat(context.getEnvironment().getProperty("spring.datasource.password")).isEqualTo("custom-password");
            assertThat(context.getEnvironment().getProperty("server.port")).isEqualTo("8081");
            var properties = context.getBean(AppProperties.class);
            assertThat(properties.getJwt().getSecret()).isEqualTo("custom-local-secret-with-at-least-32-bytes");
            assertThat(properties.getSecurity().isCookieSecure()).isTrue();
        });
    }

    @Test
    void deploymentCannotFallBackToLocalJwtSecret() {
        runner(Map.of()).run(context -> {
            if (context.getStartupFailure() != null) {
                assertThat(context.getStartupFailure()).hasStackTraceContaining("APP_JWT_SECRET");
            } else {
                assertThatThrownBy(() -> keyConfiguration(context.getBean(AppProperties.class)).jwtSecretKey())
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("APP_JWT_SECRET");
            }
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "too-short", "${APP_JWT_SECRET}"})
    void invalidJwtKeysFailBeforeServingLoginRequests(String secret) {
        var properties = new AppProperties();
        properties.getJwt().setSecret(secret);
        assertThatThrownBy(() -> keyConfiguration(properties).jwtSecretKey())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("APP_JWT_SECRET");
    }

    private SecurityConfig keyConfiguration(AppProperties properties) {
        return new SecurityConfig(properties, null, null, null, null, null);
    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(AppProperties.class)
    static class PropertiesConfiguration {
    }
}
