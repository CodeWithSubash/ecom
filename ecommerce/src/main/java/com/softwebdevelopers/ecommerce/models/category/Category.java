package com.softwebdevelopers.ecommerce.models.category;

import com.softwebdevelopers.ecommerce.common.BaseEntity;
import com.softwebdevelopers.ecommerce.models.common.FeaturedImage;
import com.softwebdevelopers.ecommerce.models.order.Coupon;
import com.softwebdevelopers.ecommerce.models.product.Product;
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
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(name = "banner_path", columnDefinition = "TEXT")
    private String bannerPath;

    @Column(name = "parent_id")
    private Long parentId;

    @OneToOne(mappedBy = "category", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = true)
    private User updatedUser;

//    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
//    private Collection<FeaturedImage> featured;
//
//    @OneToMany(mappedBy = "category", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    private Collection<Coupon> coupons;
}
