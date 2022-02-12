package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.config.FileStorageProperties;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.EProductFilterType;
import com.softwebdevelopers.ecommerce.models.modelmapper.ProductMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.ProductStockMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.SelectItemsMapper;
import com.softwebdevelopers.ecommerce.models.enums.EStockRemark;
import com.softwebdevelopers.ecommerce.models.enums.EStockType;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.services.IFileStorageService;
import com.softwebdevelopers.ecommerce.services.IPProductService;
import com.softwebdevelopers.ecommerce.services.IPProductStockService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PProductBL {

//    private static final String uuid = UUID.randomUUID().toString();

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    IPProductService service;

    @Autowired
    PCategoryBL categoryService;

    @Autowired
    PBrandBL brandService;

    @Autowired
    IPProductStockService stockService;

    @Autowired
    IFileStorageService fileStorageService;

    @Autowired
    ImageBL imageService;

    @Autowired
    AuditActivityRecordBL auditService;

    @Autowired
    ProductMapper mapper;

    @Autowired
    ProductStockMapper stockMapper;

    @Autowired
    SelectItemsMapper itemsMapper;

    private final Path fileStorageLocation;

    @Autowired
    public PProductBL(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    public List<SelectItemsDto.SelectItemIntDto> getAllList() throws RecordNotFoundException {
        List<SelectItemsDto.SelectItemIntDto> dtoList = new ArrayList<>();
        List<Product> productList = service.getAll();
        for (Product product : productList) {
            dtoList.add(itemsMapper.toDto(product.getId(), product.getName()));
        }
        return dtoList;
    }

    public PaginationRecordsDto<PProductDto> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<PProductDto> dto = mapper.toDto(service.getAll(page));

//        for (PProductDto var : dto.getData()) {
//            List<ImageDto> images = imageService.getAllById(var.getId(), Product.class.toString());
//            if (Preconditions.checkNotNull(images)) {
//                var.setFiles(images.stream().map(file -> new UploadFileResponseDto().toBuilder()
//                                .fileName(file.getFileName())
//                                .fileDownloadUri(file.getImagePath())
//                                .size(file.getSize())
//                                .fileType(file.getFileType())
//                                .build())
//                        .collect(Collectors.toList()));
//            }
//        }
        return dto;
    }

    public PaginationRecordsDto<PProductDto> getAllActive(PagingSortingAndSearchDto page) throws RecordNotFoundException {

        PaginationRecordsDto<PProductDto> dto = null;
        if (page.getOrderBy().equalsIgnoreCase(EProductFilterType.POPULARITY.name())) {
            dto = mapper.toDto(service.getAllByPopularity(page, 0L));
        } else {
            if (page.getOrderBy().equalsIgnoreCase(EProductFilterType.PRICE_HIGH_TO_LOW.name())) {
                page.setOrderBy("price");
                page.setOrderType("desc");
            } else if (page.getOrderBy().equalsIgnoreCase(EProductFilterType.PRICE_LOW_TO_HIGH.name())) {
                page.setOrderBy("price");
                page.setOrderType("asc");
            } else if (page.getOrderBy().equalsIgnoreCase(EProductFilterType.LATEST.name())) {
                page.setOrderBy("created_date");
                page.setOrderType("desc");
            }
            dto = mapper.toDto(service.getAllActive(page));
        }

//        if (Preconditions.checkNotNull(dto)) {
//            for (PProductDto var : dto.getData()) {
//                List<ImageDto> images = imageService.getAllById(var.getId(), Product.class.toString());
//                if (Preconditions.checkNotNull(images)) {
//                    var.setFiles(images.stream().map(file -> new UploadFileResponseDto().toBuilder()
//                                    .fileName(file.getFileName())
//                                    .fileDownloadUri(file.getImagePath())
//                                    .size(file.getSize())
//                                    .fileType(file.getFileType())
//                                    .build())
//                            .collect(Collectors.toList()));
//                }
//            }
//        }
        return dto;
    }

    public PaginationRecordsDto<PProductDto> getAllByCategory(PagingSortingAndSearchDto page, Long categoryId) throws RecordNotFoundException {
        PaginationRecordsDto<PProductDto> dto = null;
        if (page.getOrderBy().equalsIgnoreCase(EProductFilterType.POPULARITY.name())) {
            dto = mapper.toDto(service.getAllByPopularity(page, categoryId));
        } else {
            if (page.getOrderBy().equalsIgnoreCase(EProductFilterType.PRICE_HIGH_TO_LOW.name())) {
                page.setOrderBy("price");
                page.setOrderType("desc");
            } else if (page.getOrderBy().equalsIgnoreCase(EProductFilterType.PRICE_LOW_TO_HIGH.name())) {
                page.setOrderBy("price");
                page.setOrderType("asc");
            } else if (page.getOrderBy().equalsIgnoreCase(EProductFilterType.LATEST.name())) {
                page.setOrderBy("created_date");
                page.setOrderType("desc");
            }
            dto = mapper.toDto(service.getAllByCategory(page, categoryId));
        }

        String username = new ECOMSecurityContextHolder().getUsername();

        for (PProductDto var : dto.getData()) {
            if(username.equalsIgnoreCase(ECOMConstants.ANONYMOUS_USER)) {
                var.setMrp(0);
                var.setPrice(0);
            }
//            List<ImageDto> images = imageService.getAllById(var.getId(), Product.class.toString());
//            if (Preconditions.checkNotNull(images)) {
//                var.setFiles(images.stream().map(file -> new UploadFileResponseDto().toBuilder()
//                                .fileName(file.getFileName())
//                                .fileDownloadUri(file.getImagePath())
//                                .size(file.getSize())
//                                .fileType(file.getFileType())
//                                .build())
//                        .collect(Collectors.toList()));
//            }
        }
        return dto;
    }

    public PProductDto getById(Long id) throws RecordNotFoundException {
        PProductDto dto = mapper.toDto(service.getById(id));

//        List<ImageDto> images = imageService.getAllById(dto.getId(), Product.class.toString());
//        if (Preconditions.checkNotNull(images)) {
//            dto.setFiles(images.stream().map(file -> new UploadFileResponseDto().toBuilder()
//                            .fileName(file.getFileName())
//                            .fileDownloadUri(file.getImagePath())
//                            .size(file.getSize())
//                            .fileType(file.getFileType())
//                            .build())
//                    .collect(Collectors.toList()));
//        }

        return dto;
    }

    public PProductDto create(PProductDto product, Long id) {

        List<ImageDto> imageFiles = null;
        if (Preconditions.checkNotNull(product.getImages()) && product.getImages()[0].getSize() != 0) {
            imageFiles = Arrays.asList(product.getImages())
                    .stream()
                    .map(file -> fileStorageService.storeFile(file,
                            new FileStorageDto().toBuilder()
                                    .title("PRODUCT_" + product.getName())
                                    .uuid(UUID.randomUUID().toString())
                                    .dirName("product")
                                    .build()))
                    .collect(Collectors.toList());
        }

        PBrandDto brandDto = brandService.getById(product.getBrandId());
        if (Preconditions.checkNotNull(brandDto)) {
            PCategoryDto categoryDto = categoryService.getById(product.getCategoryId());
            if (Preconditions.checkNotNull(categoryDto)) {
                product.setBrand(brandDto);
                product.setCategory(categoryDto);
            } else {
                log.warn("The product [0] creation failed. The provided category Id: [{}] is not found.", product.getCategoryId());
                throw new RecordNotFoundException("No product record exists for given category Id: [" + product.getCategoryId() + "].");
            }
        } else {
            log.warn("The product [0] creation failed. The provided brand Id: [{}] is not found.", product.getBrandId());
            throw new RecordNotFoundException("No product record exists for given brand Id: [" + product.getBrandId() + "].");
        }

        product.setAvailableStock(product.getOpeningStock());
        Product created = service.create(mapper.toEntity(product), id);
        if (created == null) {
            log.warn("The product [{}] creation failed", product.getName());
            return null;
        }

        if (Preconditions.checkNotNull(imageFiles)) {
            for (ImageDto val : imageFiles) {
                val.setImagePath(val.getFileDownloadUri());
                val.setModelId(created.getId());
                val.setModelType(Product.class.toString());
            }

            imageFiles = imageService.saveAll(imageFiles);
            if (imageFiles == null) {
                log.warn("The image creation failed for product [{}]", product.getName());
                return null;
            }
        }

        PProductDto productDto = mapper.toDto(created);

        stockService.create(stockMapper.toEntity(new PProductStockDto().toBuilder()
                .oldStock(0)
                .changeStock(product.getOpeningStock())
                .newStock(product.getOpeningStock())
                .changeType(EStockType.OPENING.name())
                .remarks(EStockRemark.PRODUCT_CREATED.name())
                .product(productDto)
                .build()));

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PBrandBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Added new <b>Product</b>, <b>"
                                .concat(created.getName())
                                .concat("</b>")
                                .concat(" by ")
                                .concat(created.getCreatedUser().getName()))
                        .build());

//        productDto.setFiles(Preconditions.checkNotNull(imageFiles) ? imageFiles.stream().map(file -> new UploadFileResponseDto().toBuilder()
//                        .fileName(file.getFileName())
//                        .fileDownloadUri(file.getImagePath())
//                        .size(file.getSize())
//                        .fileType(file.getFileType())
//                        .build())
//                .collect(Collectors.toList()) : null);
        return productDto;
    }

    public PProductDto updateById(PProductDto product, Long id, Long wholesalerId) throws RecordNotFoundException {

        product.setId(id);
        List<ImageDto> imageFiles = null;
        if (Preconditions.checkNotNull(product.getImages()) && product.getImages()[0].getSize() != 0) {
            imageFiles = Arrays.asList(product.getImages())
                    .stream()
                    .map(file -> fileStorageService.storeFile(file,
                            new FileStorageDto().toBuilder()
                                    .title("PRODUCT_" + product.getName())
                                    .uuid(UUID.randomUUID().toString())
                                    .dirName("product")
                                    .build()))
                    .collect(Collectors.toList());
        }

        PBrandDto brandDto = brandService.getById(product.getBrandId());
        if (Preconditions.checkNotNull(brandDto)) {
            PCategoryDto categoryDto = categoryService.getById(product.getCategoryId());
            if (Preconditions.checkNotNull(categoryDto)) {
                product.setBrand(brandDto);
                product.setCategory(categoryDto);
            } else {
                log.warn("The product [0] update failed. The provided category Id: [{}] is not found.", product.getId());
                throw new RecordNotFoundException("No product record exists for given category Id: [" + product.getId() + "].");
            }
        } else {
            log.warn("The product [0] update failed. The provided brand Id: [{}] is not found.", product.getId());
            throw new RecordNotFoundException("No product record exists for given brand Id: [" + product.getId() + "].");
        }

        Product updated = service.update(mapper.toEntity(product), wholesalerId);
        if (updated == null) {
            log.warn("The product id: [{}] update failed", id);
            return null;
        }

        if (Preconditions.checkNotNull(imageFiles)) {
            for (ImageDto val : imageFiles) {
                val.setImagePath(val.getFileDownloadUri());
                val.setModelId(updated.getId());
                val.setModelType(Product.class.toString());
            }

            imageFiles = imageService.saveAll(imageFiles);
            if (imageFiles == null) {
                log.warn("The image creation failed for product [{}]", product.getName());
                return null;
            }
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PBrandBL.class.toString())
                        .method(ECOMConstants.UPDATED)
                        .asString("Updated Product, <b>"
                                .concat(updated.getName())
                                .concat("</b>")
                                .concat(" by ")
                                .concat(updated.getUpdatedUser().getName()))
                        .build());

        PProductDto productDto = mapper.toDto(updated);
//        productDto.setFiles(Preconditions.checkNotNull(imageFiles) ? imageFiles.stream().map(file -> new UploadFileResponseDto().toBuilder()
//                        .fileName(file.getFileName())
//                        .fileDownloadUri(file.getImagePath())
//                        .size(file.getSize())
//                        .fileType(file.getFileType())
//                        .build())
//                .collect(Collectors.toList()) : null);
        return productDto;
    }

    public void deleteById(Long id) throws RecordNotFoundException {
        Product deleted = service.deleteSoft(id);
        if (Preconditions.checkNotNull(deleted)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(PBrandBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Deleted Product, <b>"
                                    .concat(deleted.getName())
                                    .concat("</b>")
                                    .concat(" by ")
                                    .concat(deleted.getUpdatedUser().getName()))
                            .build());
        }
    }
}
