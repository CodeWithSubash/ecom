package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.cart.Cart;
import com.softwebdevelopers.ecommerce.models.cart.CartDetail;
import com.softwebdevelopers.ecommerce.models.modelmapper.CartDetailMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.CartMapper;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.services.IPCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PCartBL {

    @Autowired
    IPCartService service;
    
    @Autowired
    PProductBL productService;

    @Autowired
    ImageBL imageService;

    @Autowired
    CartMapper mapper;

    @Autowired
    CartDetailMapper cartDetailMapper;

    @Autowired
    AuditActivityRecordBL auditService;

    public PCartDto getAllByUser() throws RecordNotFoundException {
        PCartDto dto = mapper.toDto(service.getAllByUserId());

        for (PCartDetailDto idx :
                dto.getCartDetail()) {
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
        return dto;
    }

    public PCartDto getAllByCreatedUser(Long id) throws RecordNotFoundException {
        PCartDto dto = mapper.toDto(service.getAllByCreatedUserId(id));

        for (PCartDetailDto idx :
                dto.getCartDetail()) {
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

        return dto;
    }

    public PCartDetailDto create(PCartDetailDto cartDetail) {
        PProductDto product = productService.getById(cartDetail.getProductId());
        if(Preconditions.checkNotNull(product)) {
            cartDetail.setProduct(product);
            CartDetail created = service.create(cartDetailMapper.toEntity(cartDetail), cartDetail.getUserId());

            if (created == null) {
                log.warn("The cart detail creation failed.");
                return null;
            }

            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(PBrandBL.class.toString())
                            .method(ECOMConstants.CREATED)
                            .asString("Added new <b>Cart Detail</b> for product "
                                    .concat("<b> ")
                                    .concat(created.getProduct().getName())
                                    .concat("</b> by ")
                                    .concat(created.getCart().getCreatedUser().getName()))
                            .build());

            PCartDetailDto cartDetailDto = cartDetailMapper.toDto(created);

            List<ImageDto> images = imageService.getAllById(cartDetailDto.getProduct().getId(), Product.class.toString());
            if (Preconditions.checkNotNull(images)) {
                cartDetailDto.getProduct().setFiles(images.stream().map(file -> new UploadFileResponseDto().toBuilder()
                                .fileName(file.getFileName())
                                .fileDownloadUri(file.getImagePath())
                                .size(file.getSize())
                                .fileType(file.getFileType())
                                .build())
                        .collect(Collectors.toList()));
            }
            return cartDetailDto;
        } else {
            return null;
        }
    }

    public void deleteById(Long id) throws RecordNotFoundException {
        CartDetail deleted = service.delete(id);
        if (Preconditions.checkNotNull(deleted)) {
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(UUserBL.class.toString())
                            .method(ECOMConstants.DELETED)
                            .asString("Deleted cart detail, Product <b>"
                                    .concat(Preconditions.checkNotNull(deleted.getProduct().getName()) ? deleted.getProduct().getName() : "--")
                                    .concat("</b>"))
                            .build());
        }
    }
}
