package com.softwebdevelopers.ecommerce.models.user;

import com.softwebdevelopers.ecommerce.common.BaseEntity;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "user_verification_token")
public class UserVerificationToken extends BaseEntity {
    private static final Long EXPIRATION = 60 * 60 * 24 * 30L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true, updatable = false)
    private final String token = ECOMUtilities.generateGUIDLink();

    @Column
    private final Long expiryDate = ECOMUtilities.calculateExpiryUnixTime(EXPIRATION);

    @Column
    private boolean expired;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
}
