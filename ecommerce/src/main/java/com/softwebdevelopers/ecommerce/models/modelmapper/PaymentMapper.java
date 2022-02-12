package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.business.ImageBL;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.order.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentMapper {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ImageBL imageService;

    public OPaymentDto toDto(Payment entity) {

        if(Preconditions.checkNull(entity))
            return null;

        ImageDto image = imageService.getById(entity.getId(), Payment.class.toString());
        return new OPaymentDto().toBuilder()
                .id(entity.getId())
                .orderId(entity.getOrder().getId())
                .uuid(entity.getUuid())
                .invoiceNumber(entity.getOrder().getInvoiceNumber())
                .amount(entity.getAmount())
                .notes(entity.getNotes())
                .filePath(Preconditions.checkNotNull(image) ? image.getImagePath() : null)
                .status(Preconditions.checkNotNull(entity.getAcceptedDate()) ? "ACCEPTED" :
                        Preconditions.checkNotNull(entity.getDeletedDate()) ? "REJECTED" : "PENDING")
                .user(Preconditions.checkNotNull(entity.getUser()) ? userMapper.toResponseDto(entity.getUser()) : null)
                .createdAt(entity.getCreatedDate())
                .updatedAt(entity.getUpdatedDate())
                .deletedAt(entity.getDeletedDate())
                .acceptedAt(entity.getAcceptedDate())
                .build();
    }

    public Payment toEntity(OPaymentDto entity) {
        return new Payment().toBuilder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .amount(entity.getAmount())
                .notes(entity.getNotes())
                .build();
    }

    public List<OPaymentDto> toDto(List<Payment> entityList) {
        List<OPaymentDto> dtoList = new ArrayList<>();
        for (Payment entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public PaginationRecordsDto<OPaymentDto> toDto(Page<Payment> entityList) {
        List<OPaymentDto> dtoList = new ArrayList<>();
        for (Payment entity : entityList.getContent()) {
            dtoList.add(toDto(entity));
        }

        return new PaginationRecordsDto<OPaymentDto>().toBuilder()
                .paginationInfo(new PaginationInfoDto().toBuilder()
                        .currentPage(entityList.getNumber())
                        .totalItems(entityList.getTotalElements())
                        .totalPages(entityList.getTotalPages())
                        .size(entityList.getSize())
                        .numberOfItems(entityList.getNumberOfElements())
                        .isFirst(entityList.isFirst())
                        .isLast(entityList.isLast())
                        .hasNext(entityList.hasNext())
                        .hasPrevious(entityList.hasPrevious())
                        .build())
                .data(dtoList)
                .build();

    }
}
