package com.softwebdevelopers.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softwebdevelopers.ecommerce.dto.response.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CheckInCheckOutLogDto {

    private UUID uuid;

    private Long retailerUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkOutTime;

    private String checkInLat;

    private String checkInLong;

    private String checkOutLat;

    private String checkOutLong;

    private String checkInLocation;

    private String checkOutLocation;

    private UserResponseDto user;

    private UserResponseDto retailerUser;
}
