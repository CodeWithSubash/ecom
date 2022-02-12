package com.softwebdevelopers.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Long id;

    private String fileName;

    private String uuid;

    private String imagePath;

    private Long modelId;

    private String modelType;

    private String fileDownloadUri;

    private String fileType;

    private Long size;
}
