package com.softwebdevelopers.ecommerce.models.user;

import com.softwebdevelopers.ecommerce.common.BaseEntity;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.order.Order;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Data
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name="uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//    private String userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(unique = true, length = 50, nullable = false)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(length = 50, nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default false")
    private boolean enabled;

    @Column(name = "is_email_verified", nullable = false, columnDefinition = "boolean default false")
    private boolean isEmailVerified;

    @Column(name = "token_expired", nullable = false, columnDefinition = "boolean default true")
    private boolean tokenExpired;

    @Column(name = "user_type", length = 100)
    private String userType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(
                    name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(
                    name = "role_id", referencedColumnName = "id")})
    private Collection<Role> roles;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private UserEmployee userEmployee;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private UserRetailer userRetailer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_retailers",
            joinColumns = {@JoinColumn(
                    name = "employee_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(
                    name = "retailer_id", referencedColumnName = "id")})
    private Collection<User> retailers;

    @ManyToMany(mappedBy = "retailers")
    private Collection<User> employees;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "wishlist",
            joinColumns = {@JoinColumn(
                    name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(
                    name = "product_id", referencedColumnName = "id")})
    private Collection<Product> products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = true)
    private User ownerUser;

//    @OneToMany(mappedBy = "createdUser", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    private Collection<Category> cCategories;
//
//    @OneToMany(mappedBy = "updatedUser", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    private Collection<Category> uCategories;
//
//    @OneToMany(mappedBy = "createdUser", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    private Collection<Brand> cBrands;
//
//    @OneToMany(mappedBy = "updatedUser", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    private Collection<Brand> uBrands;
//
//    @OneToMany(mappedBy = "wholesaler", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    private Collection<Product> productOwner;
//
//    @OneToMany(mappedBy = "createdUser", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    private Collection<Product> cProducts;
//
//    @OneToMany(mappedBy = "updatedUser", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    private Collection<Product> uProducts;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = false)
//    private Collection<Order> orders;
}
