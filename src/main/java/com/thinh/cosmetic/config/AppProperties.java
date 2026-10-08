package com.thinh.cosmetic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Jwt jwt = new Jwt();
    private Security security = new Security();
    private Shipping shipping = new Shipping();
    private Seed seed = new Seed();

    @Data
    public static class Jwt {
        private String secret = "lunea_super_secret_jwt_key_must_be_at_least_32_bytes_long_for_hs256";
        private long accessTtl = 7200L;
    }

    @Data
    public static class Security {
        private boolean cookieSecure = false;
    }

    @Data
    public static class Shipping {
        private BigDecimal standardFee = new BigDecimal("30000");
        private BigDecimal freeThreshold = new BigDecimal("500000");
    }

    @Data
    public static class Seed {
        private boolean enabled = true;
    }
}
