package com.thinh.cosmetic.domain.entity.returns;

import com.thinh.cosmetic.domain.entity.order.OrderItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "return_items")
public class ReturnItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "return_request_id")
    private ReturnRequestEntity returnRequest;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItemEntity orderItem;

    private Integer quantity;

    private String detailReason;
}
