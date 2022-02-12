package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.dto.ImageDto;
import com.softwebdevelopers.ecommerce.dto.PCategoryDto;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.common.Image;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageMapper {

    public ImageDto toDto(Image entity) {
        return new ImageDto().toBuilder()
                .id(entity.getId())
                .fileName(entity.getFileName())
                .uuid(entity.getUuid())
                .imagePath(entity.getImagePath())
                .modelId(entity.getModelId())
                .modelType(entity.getModelType())
                .build();
    }

    public Image toEntity(ImageDto dto) {
        return new Image().toBuilder()
                .id(dto.getId())
                .fileName(dto.getFileName())
                .uuid(dto.getUuid())
                .imagePath(dto.getImagePath())
                .modelId(dto.getModelId())
                .modelType(dto.getModelType())
                .size(dto.getSize())
                .fileType(dto.getFileType())
                .build();
    }

    public List<ImageDto> toDto(List<Image> imageList) {
        List<ImageDto> dtoList = new ArrayList<>();
        for (Image image : imageList) {
            dtoList.add(toDto(image));
        }
        return dtoList;
    }

    public List<Image> toEntity(List<ImageDto> dtoList) {
        List<Image> entityList = new ArrayList<>();
        for (ImageDto entity : dtoList) {
            entityList.add(toEntity(entity));
        }
        return entityList;
    }
}
