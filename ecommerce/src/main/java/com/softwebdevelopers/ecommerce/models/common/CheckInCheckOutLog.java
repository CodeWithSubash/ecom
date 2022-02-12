package com.softwebdevelopers.ecommerce.models.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softwebdevelopers.ecommerce.common.BaseEntity;
import com.softwebdevelopers.ecommerce.models.user.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "check_in_check_out_logs ")
public class CheckInCheckOutLog extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @Column(name = "check_in_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkOutTime;

    @Column(length = 32)
    private String checkInLat;

    @Column(length = 32)
    private String checkInLong;

    @Column(length = 32)
    private String checkOutLat;

    @Column(length = 32)
    private String checkOutLong;

    @Column(length = 128)
    private String checkInLocation;

    @Column(length = 128)
    private String checkOutLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retailer_user_id", nullable = false)
    private User retailerUser;
}
