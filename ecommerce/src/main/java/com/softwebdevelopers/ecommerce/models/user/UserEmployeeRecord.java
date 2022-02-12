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
@Table(name = "v_employee_record")
@NoArgsConstructor
@AllArgsConstructor
public class UserEmployeeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String firstname;

    private String lastname;

    private String email;

    private String phone;

    private String mobile;

    private String city;

    private String state;

    private String zipCode;

    private String designation;

    private float commission;

    private int totalOrder;

    private float totalRevenue;

    private float totalDiscountAmount;

    private int currentOrder;

    private float currentTotalAmount;

    private float currentDiscountAmount;

    private int previousOrder;

    private float previousTotalAmount;

    private float previousDiscountAmount;
}
