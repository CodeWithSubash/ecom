package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.cart.Wishlist;
import com.softwebdevelopers.ecommerce.models.modelmapper.WishlistMapper;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.services.IPWishlistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PWishlistBL {

    @Autowired
    IPWishlistService service;

    @Autowired
    WishlistMapper mapper;

    @Autowired
    ImageBL imageService;

    @Autowired
    AuditActivityRecordBL auditService;

    public List<PWishlistDto> getAllByUser() throws RecordNotFoundException {
        List<PWishlistDto> dtos = mapper.toDto(service.getAllByUserId());

        for (PWishlistDto idx :
                dtos) {
            List<ImageDto> images = imageService.getAllById(idx.getProduct().getId(), Product.class.toString());
            if (Preconditions.checkNotNull(images)) {
                idx.getProduct().setFiles(images.stream().map(file -> new UploadFileResponseDto().toBuilder()
                                .fileName(file.getFileName())
                                .fileDownloadUri(file.getImagePath())
                                .size(file.getSize())
                                .fileType(file.getFileType())
                                .build())
                        .collect(Collectors.toList()));
            }
        }


        return dtos;
    }

    public PWishlistDto create(Long productId) {
        Wishlist created = service.create(productId);
        if (created == null) {
            log.warn("The cart detail creation failed.");
            return null;
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(PBrandBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Added new <b>wishlist</b> for product "
                                .concat("<b> ")
                                .concat(created.getProduct().getName())
                                .concat("</b>"))
                        .build());

        PWishlistDto wishlistDto = mapper.toDto(created);

        List<ImageDto> images = imageService.getAllById(wishlistDto.getProduct().getId(), Product.class.toString());
        if (Preconditions.checkNotNull(images)) {
            wishlistDto.getProduct().setFiles(images.stream().map(file -> new UploadFileResponseDto().toBuilder()
                            .fileName(file.getFileName())
                            .fileDownloadUri(file.getImagePath())
                            .size(file.getSize())
                            .fileType(file.getFileType())
                            .build())
                    .collect(Collectors.toList()));
        }
        return wishlistDto;
    }

    public void deleteById(Long id) throws RecordNotFoundException {
        Wishlist deleted = service.deleteSoft(id);
        if (Preconditions.checkNotNull(deleted)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(UUserBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Deleted wishlist, <b>"
                                    .concat(Preconditions.checkNotNull(deleted.getProduct()) ? deleted.getProduct().getName() : id.toString())
                                    .concat("</b>"))
                            .build());
        }
    }

    public void deleteByProductId(Long productId) throws RecordNotFoundException {
        Wishlist deleted = service.deleteSoftByProductId(productId);
        if (Preconditions.checkNotNull(deleted)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(UUserBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Deleted wishlist, <b>"
                                    .concat(Preconditions.checkNotNull(deleted.getProduct()) ? deleted.getProduct().getName() : productId.toString())
                                    .concat("</b>"))
                            .build());
        }
    }
}
