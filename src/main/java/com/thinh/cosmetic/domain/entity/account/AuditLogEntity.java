package com.thinh.cosmetic.domain.entity.account;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "audit_logs")
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Employee cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @NotBlank(message = "Action cannot be blank")
    @Size(max = 100, message = "Action must not exceed 100 characters")
    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Size(max = 100, message = "Target type must not exceed 100 characters")
    @Column(name = "target_type", length = 100)
    private String targetType;

    @Size(max = 100, message = "Target id must not exceed 100 characters")
    @Column(name = "target_id", length = 100)
    private String targetId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Size(max = 45, message = "IP address must not exceed 45 characters")
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
