package com.softwebdevelopers.ecommerce.models.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Builder(toBuilder = true)
@Entity
@Immutable
@Table(name = "v_retailer_record")
@NoArgsConstructor
@AllArgsConstructor
public class UserRetailerRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String businessName;

    private int orderCount;

    private float totalAmount;

    private int wishlistCount;
}
