package com.thinh.cosmetic.security;

import com.thinh.cosmetic.domain.enums.AccountType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Long accountId = parseLong(jwt.getSubject());
        Long customerId = parseLong(jwt.getClaim("cid"));
        Long employeeId = parseLong(jwt.getClaim("eid"));
        String displayName = jwt.getClaimAsString("name");
        String typ = jwt.getClaimAsString("typ");
        AccountType accountType = null;
        if (typ != null && !typ.isBlank()) {
            try {
                accountType = AccountType.valueOf(typ);
            } catch (Exception ignored) {}
        }

        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        AuthPrincipal principal = AuthPrincipal.builder()
                .accountId(accountId)
                .customerId(customerId)
                .employeeId(employeeId)
                .displayName(displayName)
                .username(jwt.getSubject())
                .accountType(accountType)
                .authorities(authorities)
                .build();

        return new JwtAuthenticationToken(jwt, authorities, principal.getDisplayName()) {
            @Override
            public Object getPrincipal() {
                return principal;
            }
        };
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> authorityStrings = jwt.getClaimAsStringList("authorities");
        if (authorityStrings == null) {
            return List.of();
        }
        List<GrantedAuthority> list = new ArrayList<>();
        for (String auth : authorityStrings) {
            if (auth != null && !auth.isBlank()) {
                list.add(new SimpleGrantedAuthority(auth));
            }
        }
        return list;
    }

    private Long parseLong(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Number number) {
            return number.longValue();
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
