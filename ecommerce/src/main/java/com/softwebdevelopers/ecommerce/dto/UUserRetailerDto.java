package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UUserRetailerDto {

    private Long id;

    private Long userId;

    private String logo;

    private String businessName;

    private String tradingStatus;

    private String businessAddress;

    private String landline;

    private String mobile;

    private String fax;

    private String email;

    private LocalDateTime dateOfCorporation;

    private String shareholders;

    private String directors;

    private String bankName;

    private String bankAddress;

    private String bankerContactPerson;

    private String bankerContactNumber;

    private boolean acceptVat;

    private String documentIdNumber;

    private String documentIdCertificate;

    private String businessRegistrationNumber;

    private String businessRegistrationCertificate;

    private String vatRegistrationNumber;

    private String vatRegistrationCertificate;

    private String notes;

    private boolean status;

    private LocalDateTime deletedAt;

}
