package com.softwebdevelopers.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softwebdevelopers.ecommerce.dto.response.UserResponseDto;
import com.softwebdevelopers.ecommerce.models.order.Order;
import com.softwebdevelopers.ecommerce.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OPaymentDto {

    private Long id;

    private UUID uuid;

    private Long orderId;

    private String invoiceNumber;

    private float amount;

    private String notes;

    private String filePath;

    private MultipartFile file;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime acceptedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private UserResponseDto user;
}
