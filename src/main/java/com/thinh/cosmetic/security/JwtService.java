package com.thinh.cosmetic.security;

import com.thinh.cosmetic.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final AppProperties appProperties;

    public String issueAccessToken(AppUserDetails userDetails) {
        Instant now = Instant.now();
        long accessTtl = appProperties.getJwt().getAccessTtl();
        Instant expiresAt = now.plusSeconds(accessTtl);

        List<String> authorityStrings = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("lunea")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(userDetails.getAccountId() != null ? userDetails.getAccountId().toString() : "")
                .claim("typ", userDetails.getAccountType() != null ? userDetails.getAccountType().name() : "")
                .claim("name", userDetails.getDisplayName())
                .claim("authorities", authorityStrings);

        if (userDetails.getCustomerId() != null) {
            claimsBuilder.claim("cid", userDetails.getCustomerId());
        }
        if (userDetails.getEmployeeId() != null) {
            claimsBuilder.claim("eid", userDetails.getEmployeeId());
        }

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsBuilder.build())).getTokenValue();
    }

    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }
}
