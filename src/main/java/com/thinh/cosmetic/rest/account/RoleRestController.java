package com.thinh.cosmetic.rest.account;

import com.thinh.cosmetic.domain.dto.response.account.RoleResponse;
import com.thinh.cosmetic.repository.account.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleRestController {

    private final RoleRepository roleRepository;

    @PreAuthorize("hasAnyAuthority('EMPLOYEE_MANAGE', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roles = roleRepository.findAll().stream()
                .filter(r -> r.getName() != null && !"CUSTOMER".equalsIgnoreCase(r.getName()))
                .map(r -> RoleResponse.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .description(r.getDescription())
                        .build())
                .toList();
        return ResponseEntity.ok(roles);
    }
}
