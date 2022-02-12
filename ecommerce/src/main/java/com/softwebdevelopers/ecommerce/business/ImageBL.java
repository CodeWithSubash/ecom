package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.common.Image;
import com.softwebdevelopers.ecommerce.models.modelmapper.ImageMapper;
import com.softwebdevelopers.ecommerce.services.IFileStorageService;
import com.softwebdevelopers.ecommerce.services.IImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ImageBL {

    @Autowired
    IImageService service;

    @Autowired
    IFileStorageService fileStorageService;

    @Autowired
    ImageMapper mapper;

    public ImageDto getById(Long id, String modelType) {
        Image entity = service.getById(id, modelType);
        if(entity != null){
            return mapper.toDto(entity);
        }
        return null;
    }

    public List<ImageDto> getAllById(Long id, String modelType) {
        List<Image> entity = service.getAllById(id, modelType);
        if(entity != null){
            return mapper.toDto(entity);
        }
        return null;
    }

    public ImageDto create(ImageDto image) {
        Image created = service.create(mapper.toEntity(image));
        if (created == null) {
            log.warn("The image [{}] creation failed", image.getFileName());
            return null;
        }

        return mapper.toDto(created);
    }

    public ImageDto uploadFile(String dir, String fileType, MultipartFile file) {
        if (Preconditions.checkNotNull(file)) {
            return fileStorageService.storeFile(file,
                    new FileStorageDto().toBuilder()
                            .title(fileType)
                            .uuid(UUID.randomUUID().toString())
                            .dirName(dir)
                            .build());
        }
        return null;
    }

    public List<ImageDto> saveAll(List<ImageDto> images) {
        List<Image> created = service.saveAll(mapper.toEntity(images));
        if (created == null) {
            log.warn("The image [{}] creation failed");
            return null;
        }

        return mapper.toDto(created);
    }
}
