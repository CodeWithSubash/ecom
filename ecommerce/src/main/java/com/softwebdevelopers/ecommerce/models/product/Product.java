package com.softwebdevelopers.ecommerce.models.product;

import com.softwebdevelopers.ecommerce.common.BaseEntity;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.order.OrderDetail;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Collection;

@Data
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User wholesaler;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false, unique = true, updatable = false)
    private String slug = ECOMUtilities.generateGUIDLink();

    @Column(nullable = false, columnDefinition = "decimal(10, 2) default '0.00'")
    private float mrp;

    @Column(columnDefinition = "decimal(10, 2) default '0.00'")
    private float price;

    @Column
    private String description;

    @Column(name = "shipping_and_delivery")
    private String shippingAndDelivery;

    @Column(name = "payment_and_return")
    private String paymentAndReturn;

    @Column(name = "product_condition", length = 50)
    private String productCondition;

    @Column(name = "available_stock", columnDefinition = "default 0")
    private float availableStock;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deal;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "products_tags",
            joinColumns = {@JoinColumn(
                    name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(
                    name = "tag_id", referencedColumnName = "id")})
    private Collection<Tag> tags;

    @ManyToMany(mappedBy = "products")
    private Collection<User> users;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = false)
    private Collection<ProductStocks> productStocks;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = false)
    private Collection<OrderDetail> orderDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private User createdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedUser;
}
