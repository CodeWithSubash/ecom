package com.softwebdevelopers.ecommerce.models.order;

import com.softwebdevelopers.ecommerce.common.BaseEntity;
import com.softwebdevelopers.ecommerce.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Collection;

@Data
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", length = 100, nullable = false)
    private String invoiceNumber;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float totalAmount;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float discountAmount;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float deliveryCharge;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float totalVat;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float grandTotalAmount;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float amountPaid;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float amountRemaining;

    @Column(name = "delivery_status", length = 50, nullable = false)
    private String deliveryStatus;

    @Column(name = "payment_status", length = 50, nullable = false)
    private String paymentStatus;

    @Column(name = "payment_method", length = 50, nullable = false)
    private String paymentMethod;

    @Column(name = "order_tracking_number", length = 80)
    private String orderTrackingNumber;

    @Column(name = "delivery_type", length = 50)
    private String deliveryType;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_placed_user_id", nullable = false)
    private User orderPlacedUserId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Payment> payments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
