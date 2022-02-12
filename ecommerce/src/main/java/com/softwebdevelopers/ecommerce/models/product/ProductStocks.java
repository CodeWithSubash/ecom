package com.softwebdevelopers.ecommerce.models.product;

import com.softwebdevelopers.ecommerce.common.BaseEntity;
import com.softwebdevelopers.ecommerce.models.enums.EStockRemark;
import com.softwebdevelopers.ecommerce.models.enums.EStockType;
import com.softwebdevelopers.ecommerce.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "product_stocks")
@NoArgsConstructor
@AllArgsConstructor
public class ProductStocks extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "old_stock", columnDefinition = "default 0")
    private float oldStock;

    @Column(name = "change_stock", columnDefinition = "default 0")
    private float changeStock;

    @Column(name = "new_stock", columnDefinition = "default 0")
    private float newStock;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", length = 50, nullable = false)
    private EStockType changeType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private EStockRemark remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
