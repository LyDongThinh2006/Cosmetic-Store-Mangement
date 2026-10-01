package com.thinh.cosmetic.domain.entity.review;

import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.order.OrderItemEntity;
import com.thinh.cosmetic.domain.enums.ReviewModerationStatus;
import jakarta.persistence.*;
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
@Table(name = "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    //private OrderItemEntity orderItem;

    private Integer rating;

    private String content;

    @Enumerated(EnumType.STRING)
    private ReviewModerationStatus moderationStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime reviewDate;
}
