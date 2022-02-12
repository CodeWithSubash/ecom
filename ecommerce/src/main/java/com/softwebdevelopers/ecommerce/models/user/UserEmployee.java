package com.softwebdevelopers.ecommerce.models.user;

import com.softwebdevelopers.ecommerce.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "user_employees")
@NoArgsConstructor
@AllArgsConstructor
//@NamedStoredProcedureQuery(name = "getEmployeeRecord",
//        procedureName = "sp_GetEmployeeRecord",
//        parameters = {
//        @StoredProcedureParameter(mode = ParameterMode.IN, name = "userId", type = Long.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_id", type = Long.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_user_id", type = Long.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_firstname", type = String.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_lastname", type = String.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_email", type = String.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_designation", type = String.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_phone", type = String.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_mobile", type = String.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_city", type = String.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_state", type = String.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_zip_code", type = String.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "val_commission", type = Float.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "total_revenue", type = Float.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "total_average_price", type = Float.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "current_revenue", type = Float.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "current_order", type = Integer.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "previous_revenue", type = Float.class),
//        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "previous_order", type = Float.class)
//})
public class UserEmployee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(length = 100)
    private String firstname;

    @Column(length = 100)
    private String lastname;

    @Column
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 20)
    private String mobile;

    @Column(name = "alternative_phone", length = 20)
    private String alternativePhone;

    @Column(name = "alternative_mobile", length = 20)
    private String alternativeMobile;

    @Column(length = 100)
    private String street;

    @Column(length = 100)
    private String street2;

    @Column(length = 25)
    private String city;

    @Column(length = 50)
    private String state;

    @Column(name = "zip_code", length = 25)
    private String zipCode;

    @Column(length = 30)
    private String designation;

    @Column
    private float commission;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
