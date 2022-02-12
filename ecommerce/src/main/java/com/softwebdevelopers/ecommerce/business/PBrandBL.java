package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.config.FileStorageProperties;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.modelmapper.BrandMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.SelectItemsMapper;
import com.softwebdevelopers.ecommerce.services.IFileStorageService;
import com.softwebdevelopers.ecommerce.services.IPBrandService;
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
public class PBrandBL {

    @Autowired
    IPBrandService service;

    @Autowired
    IFileStorageService fileStorageService;

    @Autowired
    ImageBL imageService;

    @Autowired
    AuditActivityRecordBL auditService;

    @Autowired
    BrandMapper mapper;

    @Autowired
    SelectItemsMapper itemsMapper;

    private final Path fileStorageLocation;

    @Autowired
    public PBrandBL(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    public List<SelectItemsDto.SelectItemIntDto> getAllList() throws RecordNotFoundException {
        List<SelectItemsDto.SelectItemIntDto> dtoList = new ArrayList<>();
        List<Brand> brandList = service.getAll();
        for (Brand brand : brandList) {
            dtoList.add(itemsMapper.toDto(brand.getId(), brand.getName()));
        }
        return dtoList;
    }

    public PaginationRecordsDto<PBrandDto> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<PBrandDto> dto = mapper.toDto(service.getAll(page));

//        for (PBrandDto var : dto.getData()) {
//            ImageDto image = imageService.getById(var.getId(), Brand.class.toString());
//            var.setLogoPath(Preconditions.checkNotNull(image) ? image.getImagePath() : null);
//        }
        return dto;
    }

    public PaginationRecordsDto<PBrandDto> getAllActive(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<PBrandDto> dto = mapper.toDto(service.getAllActive(page));

//        for (PBrandDto var : dto.getData()) {
//            ImageDto image = imageService.getById(var.getId(), Brand.class.toString());
//            var.setLogoPath(Preconditions.checkNotNull(image) ? image.getImagePath() : null);
//        }
        return dto;
    }

    public PaginationRecordsDto<PBrandDto> getAllInactive(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<PBrandDto> dto = mapper.toDto(service.getAllInactive(page));

//        for (PBrandDto var : dto.getData()) {
//            ImageDto image = imageService.getById(var.getId(), Brand.class.toString());
//            var.setLogoPath(Preconditions.checkNotNull(image) ? image.getImagePath() : null);
//        }
        return dto;
    }

    public PBrandDto getById(Long id) throws RecordNotFoundException {
        PBrandDto dto = mapper.toDto(service.getById(id));

        return dto;
    }

    public PBrandDto create(PBrandDto brand) {

        ImageDto imageLogo = null;
        if(Preconditions.checkNotNull(brand.getLogoFile())) {
            imageLogo = fileStorageService.storeFile(brand.getLogoFile(),
                    new FileStorageDto().toBuilder()
                            .title("BRAND_" + brand.getName())
                            .uuid(UUID.randomUUID().toString())
                            .dirName("brand")
                            .build());
        }

        Brand created = service.create(mapper.toEntity(brand));
        if (created == null) {
            log.warn("The brand [{}] creation failed", brand.getName());
            return null;
        }

        if(Preconditions.checkNotNull(imageLogo)) {
            imageLogo.setImagePath(imageLogo.getFileDownloadUri());
            imageLogo.setModelId(created.getId());
            imageLogo.setModelType(Brand.class.toString());
            imageLogo = imageService.create(imageLogo);
            if (imageLogo == null) {
                log.warn("The image creation failed for brand [{}]", brand.getName());
                return null;
            }
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PBrandBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Added new <b>Brand</b>, <b>"
                                .concat(created.getName())
                                .concat("</b>")
                                .concat(" by ")
                                .concat(created.getCreatedUser().getName()))
                        .build());

        PBrandDto brandDto = mapper.toDto(created);
        brandDto.setLogoPath(Preconditions.checkNotNull(imageLogo) ? imageLogo.getImagePath() : null);
        return brandDto;
    }

    public PBrandDto updateById(PBrandDto brand, Long id) throws RecordNotFoundException {

        brand.setId(id);
        ImageDto imageLogo = null;
        if(Preconditions.checkNotNull(brand.getLogoFile())) {
            imageLogo = fileStorageService.storeFile(brand.getLogoFile(),
                    new FileStorageDto().toBuilder()
                            .title("BRAND_" + brand.getName())
                            .uuid(UUID.randomUUID().toString())
                            .dirName("brand")
                            .build());
        }

        Brand updated = service.update(mapper.toEntity(brand));
        if (updated == null) {
            log.warn("The brand id: [{}] update failed", id);
            return null;
        }

        if(Preconditions.checkNotNull(imageLogo)) {
            ImageDto image = imageService.getById(id, Brand.class.toString());
            if(Preconditions.checkNotNull(image)) {
                ECOMUtilities.deleteSingleFile(this.fileStorageLocation.toString() + "/brand/" + image.getFileName());
                imageLogo.setId(image.getId());
            }
            imageLogo.setImagePath(imageLogo.getFileDownloadUri());
            imageLogo.setModelId(updated.getId());
            imageLogo.setModelType(Brand.class.toString());
            imageLogo = imageService.create(imageLogo);
            if (imageLogo == null) {
                log.warn("The image creation failed for brand [{}]", brand.getName());
                return null;
            }
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PBrandBL.class.toString())
                        .method(ECOMConstants.UPDATED)
                        .asString("Updated Brand, <b>"
                                .concat(updated.getName())
                                .concat("</b>")
                                .concat(" by ")
                                .concat(updated.getUpdatedUser().getName()))
                        .build());

        PBrandDto brandDto = mapper.toDto(updated);
        brandDto.setLogoPath(Preconditions.checkNotNull(imageLogo) ? imageLogo.getImagePath() : null);
        return brandDto;
    }

    public void deleteById(Long id) throws RecordNotFoundException {
        Brand deleted = service.deleteSoft(id);
        if(Preconditions.checkNotNull(deleted)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(PBrandBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Deleted Brand, <b>"
                                    .concat(deleted.getName())
                                    .concat("</b>")
                                    .concat(" by ")
                                    .concat(deleted.getUpdatedUser().getName()))
                            .build());
        }
    }
}
