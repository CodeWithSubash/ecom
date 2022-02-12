package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EmailDto {

    private Long id;

    private String senderEmail;

    private String subject;

    private String body;

    private String message;

    private String password;

    private List<String> recipientEmails;
}
