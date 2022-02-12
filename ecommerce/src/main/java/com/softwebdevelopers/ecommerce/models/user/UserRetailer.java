package com.softwebdevelopers.ecommerce.models.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softwebdevelopers.ecommerce.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "user_retailers")
@NoArgsConstructor
@AllArgsConstructor
public class UserRetailer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String logo;

    @Column(name = "business_name", length = 100)
    private String businessName;

    @Column(name = "trading_status", length = 100)
    private String tradingStatus;

    @Column(name = "business_address")
    private String businessAddress;

    @Column(length = 20)
    private String landline;

    @Column(length = 20)
    private String mobile;

    @Column(length = 20)
    private String fax;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "date_of_corporation")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateOfCorporation;

    @Column(name = "share_holders")
    private String shareHolders;

    @Column(name = "directors")
    private String directors;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_address")
    private String bankAddress;

    @Column(name = "banker_contact_person", length = 50)
    private String bankerContactPerson;

    @Column(name = "banker_contact_number", length = 20)
    private String bankerContactNumber;

    @Column(name = "document_id_number", length = 50)
    private String documentIdNumber;

    @Column(name = "document_id_certificate")
    private String documentIdCertificate;

    @Column(name = "business_registration_number", length = 50)
    private String businessRegistrationNumber;

    @Column(name = "business_registration_certificate")
    private String businessRegistrationCertificate;

    @Column(name = "vat_registration_number", length = 50)
    private String vatRegistrationNumber;

    @Column(name = "vat_registration_certificate")
    private String vatRegistrationCertificate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "status", nullable = false, columnDefinition = "boolean default false")
    private boolean status;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
