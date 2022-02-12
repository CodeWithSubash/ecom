package com.softwebdevelopers.ecommerce.models.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "coupons")
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @Column(name = "coupon_code", length = 15, unique = true)
    private String couponCode;

    @Column(name = "coupon_type", length = 25)
    private String couponType;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float discountValue;

    @Column(nullable = false, columnDefinition = "default '0'")
    private float minimumOrderValue;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float maximumDiscountAmount;

    @Column(name = "is_redeem_allowed", nullable = false, columnDefinition = "boolean default false")
    private boolean isRedeemAllowed;

    @Column(name = "valid_from")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validFrom;

    @Column(name = "valid_until")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validUntil;

    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;
}
