package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.models.order.Order;
import com.softwebdevelopers.ecommerce.models.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    OrderDetailMapper mapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PaymentMapper paymentMapper;

    public OOrderDto toDto(Order entity, String mapperType) {
        return new OOrderDto().toBuilder()
                .id(entity.getId())
                .billNumber(entity.getInvoiceNumber())
                .billingAddress(entity.getBillingAddress())
                .totalAmount(entity.getTotalAmount())
                .deliveryCharge(entity.getDeliveryCharge())
                .discountAmount(entity.getDiscountAmount())
                .totalVat(entity.getTotalVat())
                .grandTotalAmount(entity.getGrandTotalAmount())
                .amountPaid(entity.getAmountPaid())
                .amountRemaining(entity.getAmountRemaining())
                .deliveryStatus(entity.getDeliveryStatus())
                .paymentStatus(entity.getPaymentStatus())
                .paymentMethod(entity.getPaymentMethod())
                .orderTrackingNumber(entity.getOrderTrackingNumber())
                .deliveryType(entity.getDeliveryType())
                .notes(entity.getNotes())
                .user(Preconditions.checkNotNull(entity.getUser()) ? userMapper.toDto(entity.getUser()) : null)
                .orderDetails(Preconditions.checkNotBlank(mapperType) && mapperType.equalsIgnoreCase(ECOMConstants.ORDER_WITH_DETAIL) && Preconditions.checkNotNull(entity.getOrderDetails()) ? mapper.toDto(entity.getOrderDetails().stream().collect(Collectors.toList())) : null)
                .createdAt(entity.getCreatedDate())
                .createdUser(Preconditions.checkNotNull(entity.getOrderPlacedUserId()) ? entity.getOrderPlacedUserId().getName() : null)
                .build();
    }

    public Order toEntity(OOrderDto dto) {
        return new Order().toBuilder()
                .id(dto.getId())
                .invoiceNumber(dto.getBillNumber())
                .billingAddress(dto.getBillingAddress())
                .totalAmount(dto.getTotalAmount())
                .deliveryCharge(dto.getDeliveryCharge())
                .discountAmount(dto.getDiscountAmount())
                .totalVat(dto.getTotalVat())
                .grandTotalAmount(dto.getGrandTotalAmount())
                .amountPaid(dto.getAmountPaid())
                .amountRemaining(dto.getAmountRemaining())
                .deliveryStatus(dto.getDeliveryStatus())
                .paymentStatus(dto.getPaymentStatus())
                .paymentMethod(dto.getPaymentMethod())
                .orderTrackingNumber(dto.getOrderTrackingNumber())
                .deliveryType(dto.getDeliveryType())
                .notes(dto.getNotes())
//                .user(Preconditions.checkNotNull(dto.getUser()) ? userMapper.toEntity(dto.getUser()) : null)
//                .orderDetails(Preconditions.checkNotNull(dto.getOrderDetails()) ? mapper.toEntity(dto.getOrderDetails()) : null)
                .build();
    }

    public List<OOrderDto> toDto(List<Order> entityList, String mapperType) {
        List<OOrderDto> dtoList = new ArrayList<>();
        for (Order entity : entityList) {
            dtoList.add(toDto(entity, mapperType));
        }
        return dtoList;
    }

    public PaginationRecordsDto<OOrderDto> toDto(Page<Order> entityList, String mapperType) {
        if(Preconditions.checkNull(entityList)) {
            return null;
        }

        List<OOrderDto> dtoList = new ArrayList<>();
        for (Order entity : entityList.getContent()) {
            dtoList.add(toDto(entity, mapperType));
        }

        return new PaginationRecordsDto<OOrderDto>().toBuilder()
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

    public OInvoiceDto toInvoiceDto(Order entity, String mapperType) {
        if(Preconditions.checkNull(entity))
            return null;

        return new OInvoiceDto().toBuilder()
                .id(entity.getId())
                .billNumber(entity.getInvoiceNumber())
                .billingAddress(entity.getBillingAddress())
                .totalAmount(entity.getTotalAmount())
                .deliveryCharge(entity.getDeliveryCharge())
                .totalVat(entity.getTotalVat())
                .grandTotalAmount(entity.getGrandTotalAmount())
                .paymentStatus(entity.getPaymentStatus())
                .paymentMethod(entity.getPaymentMethod())
                .payments(Preconditions.checkNotNull(entity.getPayments()) ? paymentMapper.toDto(entity.getPayments().stream().collect(Collectors.toList())) : null)
                .retailerName(Preconditions.checkNotNull(entity.getUser()) ? entity.getUser().getUserRetailer().getBusinessName() : null)
                .orderDetails(Preconditions.checkNotBlank(mapperType) && mapperType.equalsIgnoreCase(ECOMConstants.ORDER_WITH_DETAIL) && Preconditions.checkNotNull(entity.getOrderDetails()) ? mapper.toInvoiceDetailDto(entity.getOrderDetails().stream().collect(Collectors.toList())) : null)
                .createdAt(entity.getCreatedDate())
                .build();
    }

    public PaginationRecordsDto<OInvoiceDto> toInvoiceDto(Page<Order> entityList, String mapperType) {
        if(Preconditions.checkNull(entityList)) {
            return null;
        }

        List<OInvoiceDto> dtoList = new ArrayList<>();
        for (Order entity : entityList.getContent()) {
            dtoList.add(toInvoiceDto(entity, mapperType));
        }

        return new PaginationRecordsDto<OInvoiceDto>().toBuilder()
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
