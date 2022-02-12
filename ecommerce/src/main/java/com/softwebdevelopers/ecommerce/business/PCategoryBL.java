package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.config.FileStorageProperties;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.modelmapper.CategoryMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.SelectItemsMapper;
import com.softwebdevelopers.ecommerce.services.IFileStorageService;
import com.softwebdevelopers.ecommerce.services.IPCategoryService;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class PCategoryBL {

    @Autowired
    private IPCategoryService service;

    @Autowired
    private IFileStorageService fileStorageService;

    @Autowired
    private ImageBL imageService;

    @Autowired
    private AuditActivityRecordBL auditService;

    @Autowired
    CategoryMapper mapper;

    @Autowired
    SelectItemsMapper itemsMapper;

    private final Path fileStorageLocation;

    @Autowired
    public PCategoryBL(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    public List<SelectItemsDto.SelectItemIntDto> getAllList(String type) throws RecordNotFoundException {
        List<SelectItemsDto.SelectItemIntDto> dtoList = new ArrayList<>();
        List<Category> categoryList = service.getAll("parent");
        for (Category category : categoryList) {
            dtoList.add(itemsMapper.toDto(category.getId(), category.getName()));
        }
        return dtoList;
    }

    public PaginationRecordsDto<PCategoryDto> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<PCategoryDto> dto = mapper.toDto(service.getAll(page));
        List<Category> categoryList = service.getAll("parent");

        for (PCategoryDto var : dto.getData()) {
//            ImageDto image = imageService.getById(var.getId(), Category.class.toString());
//            var.setLogoPath(Preconditions.checkNotNull(image) ? image.getImagePath() : null);

            if (var.getParentId() != null && var.getParentId() != 0) {
                for (Category category : categoryList) {
                    if (var.getParentId() == category.getId()) {
                        var.setParentName(category.getName());
                        break;
                    }
                }
            }
        }
        return dto;
    }

    public PaginationRecordsDto<PCategoryDto> getAllActive(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<PCategoryDto> dto = mapper.toDto(service.getAllActive(page));
        List<Category> categoryList = service.getAll("parent");

        for (PCategoryDto var : dto.getData()) {
//            ImageDto image = imageService.getById(var.getId(), Category.class.toString());
//            var.setLogoPath(Preconditions.checkNotNull(image) ? image.getImagePath() : null);

            if (var.getParentId() != null && var.getParentId() != 0) {
                for (Category category : categoryList) {
                    if (var.getParentId() == category.getId()) {
                        var.setParentName(category.getName());
                        break;
                    }
                }
            }
        }
        return dto;
    }

    public PaginationRecordsDto<PCategoryDto> getAllInactive(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<PCategoryDto> dto = mapper.toDto(service.getAllInactive(page));
        List<Category> categoryList = service.getAll("parent");

        for (PCategoryDto var : dto.getData()) {
//            ImageDto image = imageService.getById(var.getId(), Category.class.toString());
//            var.setLogoPath(Preconditions.checkNotNull(image) ? image.getImagePath() : null);

            if (var.getParentId() != null && var.getParentId() != 0) {
                for (Category category : categoryList) {
                    if (var.getParentId() == category.getId()) {
                        var.setParentName(category.getName());
                        break;
                    }
                }
            }
        }
        return dto;
    }

    public PCategoryDto getById(Long id) throws RecordNotFoundException {
        PCategoryDto dto = mapper.toDto(service.getById(id));
        List<Category> categoryList = service.getAll("parent");

//        ImageDto image = imageService.getById(dto.getId(), Category.class.toString());
//        dto.setLogoPath(Preconditions.checkNotNull(image) ? image.getImagePath() : null);
        if (dto.getParentId() != null && dto.getParentId() != 0) {
            for (Category category : categoryList) {
                if (dto.getParentId() == category.getId()) {
                    dto.setParentName(category.getName());
                    break;
                }
            }
        }

        return dto;
    }

    public PCategoryDto create(PCategoryDto category) {

        ImageDto imageLogo = null;
        if (Preconditions.checkNotNull(category.getLogoFile())) {
            imageLogo = fileStorageService.storeFile(category.getLogoFile(),
                    new FileStorageDto().toBuilder()
                            .title("LOGO_" + category.getName())
                            .uuid(UUID.randomUUID().toString())
                            .dirName("category")
                            .build());
        }

        if (Preconditions.checkNotNull(category.getBannerFile())) {
            ImageDto imageBanner = fileStorageService.storeFile(category.getBannerFile(),
                    new FileStorageDto().toBuilder()
                            .title("BANNER_" + category.getName())
                            .uuid(UUID.randomUUID().toString())
                            .dirName("category")
                            .build());

            category.setBannerPath(imageBanner.getFileDownloadUri());
        }

        Category created = service.create(mapper.toEntity(category));
        if (created == null) {
            log.warn("The category [{}] creation failed", category.getName());
            return null;
        }

        if (Preconditions.checkNotNull(imageLogo)) {
            imageLogo.setImagePath(imageLogo.getFileDownloadUri());
            imageLogo.setModelId(created.getId());
            imageLogo.setModelType(Category.class.toString());
            imageLogo = imageService.create(imageLogo);
            if (imageLogo == null) {
                log.warn("The image creation failed for category [{}]", category.getName());
                return null;
            }
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PCategoryBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Added new <b>Category</b>, <b>"
                                .concat(created.getName())
                                .concat("</b>")
                                .concat(" by ")
                                .concat(created.getCreatedUser().getName()))
                        .build());

        PCategoryDto categoryDto = mapper.toDto(created);
//        categoryDto.setLogoPath(Preconditions.checkNotNull(imageLogo) ? imageLogo.getImagePath() : null);
        return categoryDto;
    }

    public PCategoryDto updateById(PCategoryDto category, Long id) throws RecordNotFoundException {

        category.setId(id);
        ImageDto imageLogo = null;
        if (Preconditions.checkNotNull(category.getLogoFile())) {
            imageLogo = fileStorageService.storeFile(category.getLogoFile(),
                    new FileStorageDto().toBuilder()
                            .title("LOGO_" + category.getName())
                            .uuid(UUID.randomUUID().toString())
                            .dirName("category")
                            .build());
//            category.setLogoPath(imageLogo.getFileDownloadUri());
        }

        if (Preconditions.checkNotNull(category.getBannerFile())) {
            ImageDto imageBanner = fileStorageService.storeFile(category.getBannerFile(),
                    new FileStorageDto().toBuilder()
                            .title("BANNER_" + category.getName())
                            .uuid(UUID.randomUUID().toString())
                            .dirName("category")
                            .build());

            category.setBannerPath(imageBanner.getFileDownloadUri());
        }

        Category updated = service.update(mapper.toEntity(category));
        if (updated == null) {
            log.warn("The category id: [{}] update failed", id);
            return null;
        }

        if (Preconditions.checkNotNull(imageLogo)) {
            ImageDto image = imageService.getById(id, Category.class.toString());
            if (Preconditions.checkNotNull(image)) {
                ECOMUtilities.deleteSingleFile(this.fileStorageLocation.toString() + "/category/" + image.getFileName());
                imageLogo.setId(image.getId());
            }
            imageLogo.setImagePath(imageLogo.getFileDownloadUri());
            imageLogo.setModelId(updated.getId());
            imageLogo.setModelType(Category.class.toString());
            imageLogo = imageService.create(imageLogo);
            if (imageLogo == null) {
                log.warn("The image creation failed for category [{}]", category.getName());
                return null;
            }
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PCategoryBL.class.toString())
                        .method(ECOMConstants.UPDATED)
                        .asString("Updated Category, <b>"
                                .concat(updated.getName())
                                .concat("</b>")
                                .concat(" by ")
                                .concat(updated.getUpdatedUser().getName()))
                        .build());

        PCategoryDto categoryDto = mapper.toDto(updated);
//        categoryDto.setLogoPath(Preconditions.checkNotNull(imageLogo) ? imageLogo.getImagePath() : null);
        return categoryDto;
    }

    public void deleteById(Long id) throws RecordNotFoundException {
        Category deleted = service.deleteSoft(id);
        if (Preconditions.checkNotNull(deleted)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(PCategoryBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Deleted Category, <b>"
                                    .concat(deleted.getName())
                                    .concat("</b>")
                                    .concat(" by ")
                                    .concat(deleted.getUpdatedUser().getName()))
                            .build());
        }
    }
}
