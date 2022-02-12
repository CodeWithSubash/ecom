package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.config.FileStorageProperties;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.common.FeaturedImage;
import com.softwebdevelopers.ecommerce.models.modelmapper.FeaturedImageMapper;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.services.IFeaturedImageService;
import com.softwebdevelopers.ecommerce.services.IFileStorageService;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class FeaturedImageBL {

    @Autowired
    IFeaturedImageService service;

    @Autowired
    IFileStorageService fileStorageService;

    @Autowired
    AuditActivityRecordBL auditService;

    @Autowired
    FeaturedImageMapper mapper;

    @Autowired
    ImageBL imageService;

    @Autowired
    PCategoryBL categoryService;

    private final Path fileStorageLocation;

    @Autowired
    public FeaturedImageBL(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    public PaginationRecordsDto<FeaturedImageDto> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<FeaturedImageDto> dto = mapper.toDto(service.getAll(page));

        for (FeaturedImageDto var : dto.getData()) {
            ImageDto image = imageService.getById(var.getId(), FeaturedImage.class.toString());
            var.setFiles(Preconditions.checkNotNull(image) ?
                    new UploadFileResponseDto().toBuilder()
                            .fileName(image.getFileName())
                            .fileDownloadUri(image.getImagePath())
                            .size(image.getSize())
                            .fileType(image.getFileType())
                            .build() :
                    null);
        }
        return dto;
    }

    public FeaturedImageDto getById(Long id) throws RecordNotFoundException {
        FeaturedImageDto dto = mapper.toDto(service.getById(id));

        ImageDto image = imageService.getById(dto.getId(), FeaturedImage.class.toString());
        if (Preconditions.checkNotNull(image)) {
            dto.setFiles(new UploadFileResponseDto().toBuilder()
                    .fileName(image.getFileName())
                    .fileDownloadUri(image.getImagePath())
                    .size(image.getSize())
                    .fileType(image.getFileType())
                    .build());
        }

        return dto;
    }

    public FeaturedImageDto create(FeaturedImageDto featured) {
        ImageDto imageLogo = null;
        if (Preconditions.checkNotNull(featured.getLogoFile())) {
            imageLogo = fileStorageService.storeFile(featured.getLogoFile(),
                    new FileStorageDto().toBuilder()
                            .title("FEATURED_" + featured.getTitle())
                            .uuid(UUID.randomUUID().toString())
                            .dirName("featured")
                            .build());
        }
        if (featured.getCategoryId() != null && featured.getCategoryId() != 0) {
            featured.setCategory(categoryService.getById(featured.getCategoryId()));
        }

        FeaturedImage created = service.create(mapper.toEntity(featured));
        if (created == null) {
            log.warn("The featured [{}] creation failed", featured.getTitle());
            return null;
        }

        if (Preconditions.checkNotNull(imageLogo)) {
            imageLogo.setImagePath(imageLogo.getFileDownloadUri());
            imageLogo.setModelId(created.getId());
            imageLogo.setModelType(FeaturedImage.class.toString());
            imageLogo = imageService.create(imageLogo);
            if (imageLogo == null) {
                log.warn("The image creation failed for featured image [{}]", featured.getTitle());
                return null;
            }
        }

        FeaturedImageDto featuredDto = mapper.toDto(created);
        featuredDto.setFiles(Preconditions.checkNotNull(imageLogo) ?
                new UploadFileResponseDto().toBuilder()
                        .fileName(imageLogo.getFileName())
                        .fileDownloadUri(imageLogo.getImagePath())
                        .size(imageLogo.getSize())
                        .fileType(imageLogo.getFileType())
                        .build() : null);
        return featuredDto;
    }

    public FeaturedImageDto updateById(FeaturedImageDto featured, Long id) throws RecordNotFoundException {

        featured.setId(id);
        ImageDto imageLogo = null;
        if (Preconditions.checkNotNull(featured.getLogoFile())) {
            imageLogo = fileStorageService.storeFile(featured.getLogoFile(),
                    new FileStorageDto().toBuilder()
                            .title("FEATURED_" + featured.getTitle())
                            .uuid(UUID.randomUUID().toString())
                            .dirName("featured")
                            .build());
        }

        if (featured.getCategoryId() != null && featured.getCategoryId() != 0) {
            featured.setCategory(categoryService.getById(featured.getCategoryId()));
        }

        FeaturedImage updated = service.update(mapper.toEntity(featured));
        if (updated == null) {
            log.warn("The featured id: [{}] update failed", id);
            return null;
        }

        if (Preconditions.checkNotNull(imageLogo)) {
            ImageDto image = imageService.getById(id, FeaturedImage.class.toString());
            if (Preconditions.checkNotNull(image)) {
                ECOMUtilities.deleteSingleFile(this.fileStorageLocation.toString() + "/featured/" + image.getFileName());
                imageLogo.setId(image.getId());
            }
            imageLogo.setImagePath(imageLogo.getFileDownloadUri());
            imageLogo.setModelId(updated.getId());
            imageLogo.setModelType(FeaturedImage.class.toString());
            imageLogo = imageService.create(imageLogo);
            if (imageLogo == null) {
                log.warn("The featured image creation failed for featured [{}]", featured.getTitle());
                return null;
            }
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PBrandBL.class.toString())
                        .method(ECOMConstants.UPDATED)
                        .asString("Updated Featured Image, <b>"
                                .concat(updated.getTitle())
                                .concat("</b>"))
                        .build());

        FeaturedImageDto featuredDto = mapper.toDto(updated);
        featuredDto.setFiles(Preconditions.checkNotNull(imageLogo) ?
                new UploadFileResponseDto().toBuilder()
                        .fileName(imageLogo.getFileName())
                        .fileDownloadUri(imageLogo.getImagePath())
                        .size(imageLogo.getSize())
                        .fileType(imageLogo.getFileType())
                        .build() : null);
        return featuredDto;
    }

    public FeaturedImageDto publishedById(Long id) throws RecordNotFoundException {
        FeaturedImage published = service.publishedById(id);
        if (Preconditions.checkNotNull(published)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(PBrandBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Published Featured Image, <b>"
                                    .concat(published.getTitle())
                                    .concat("</b>"))
                            .build());
        }

        return mapper.toDto(published);
    }

    public void deleteById(Long id) throws RecordNotFoundException {
        FeaturedImage deleted = service.delete(id);
        if (Preconditions.checkNotNull(deleted)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(UUserBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Deleted Featured Image, <b>"
                                    .concat(Preconditions.checkNotNull(deleted.getTitle()) ? deleted.getTitle() : "--")
                                    .concat("</b>"))
                            .build());
        }
    }
}
