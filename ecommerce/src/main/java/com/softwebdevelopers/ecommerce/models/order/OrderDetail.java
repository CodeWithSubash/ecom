package com.softwebdevelopers.ecommerce.models.order;

import com.softwebdevelopers.ecommerce.common.BaseEntity;
import com.softwebdevelopers.ecommerce.models.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float quantity;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float totalAmount;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float discountAmount;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float deliveryCharge;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float vat;

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float grandTotalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
