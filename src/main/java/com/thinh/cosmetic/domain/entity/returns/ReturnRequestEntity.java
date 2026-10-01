package com.thinh.cosmetic.domain.entity.returns;

import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.entity.order.OrderEntity;
import com.thinh.cosmetic.domain.enums.ReturnStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "return_requests")
public class ReturnRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private String reason;

    @Enumerated(EnumType.STRING)
    private ReturnStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime requestedAt;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private EmployeeEntity processedBy;

    private LocalDateTime processedAt;

    @OneToMany(mappedBy = "returnRequest", cascade = CascadeType.ALL)
    private List<ReturnItemEntity> items;
}
