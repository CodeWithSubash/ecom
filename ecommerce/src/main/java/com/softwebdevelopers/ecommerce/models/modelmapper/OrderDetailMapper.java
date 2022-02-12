package com.softwebdevelopers.ecommerce.models.modelmapper;

import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.OInvoiceDetailDto;
import com.softwebdevelopers.ecommerce.dto.OOrderDetailDto;
import com.softwebdevelopers.ecommerce.models.order.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDetailMapper {

    @Autowired
    ProductMapper mapper;

    public OOrderDetailDto toDto(OrderDetail entity) {
        if(Preconditions.checkNull(entity))
            return null;

        return new OOrderDetailDto().toBuilder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .totalAmount(entity.getTotalAmount())
                .deliveryCharge(entity.getDeliveryCharge())
                .discountAmount(entity.getDiscountAmount())
                .vat(entity.getVat())
                .grandTotalAmount(entity.getGrandTotalAmount())
                .product(Preconditions.checkNotNull(entity.getProduct()) ? mapper.toDto(entity.getProduct()) : null)
                .build();
    }

    public OrderDetail toEntity(OOrderDetailDto dto) {
        if(Preconditions.checkNull(dto))
            return null;

        return new OrderDetail().toBuilder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .totalAmount(dto.getTotalAmount())
                .deliveryCharge(dto.getDeliveryCharge())
                .discountAmount(dto.getDiscountAmount())
                .vat(dto.getVat())
                .grandTotalAmount(dto.getGrandTotalAmount())
                .product(Preconditions.checkNotNull(dto.getProduct()) ? mapper.toEntity(dto.getProduct()) : null)
                .build();
    }

    public List<OOrderDetailDto> toDto(List<OrderDetail> entityList) {
        if(Preconditions.checkNull(entityList))
            return null;

        List<OOrderDetailDto> dtoList = new ArrayList<>();
        for (OrderDetail entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public List<OrderDetail> toEntity(List<OOrderDetailDto> dtoList) {
        if(Preconditions.checkNull(dtoList))
            return null;

        List<OrderDetail> entityList = new ArrayList<>();
        for (OOrderDetailDto dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        return entityList;
    }

    public OInvoiceDetailDto toInvoiceDetailDto(OrderDetail entity) {
        if(Preconditions.checkNull(entity))
            return null;

        return new OInvoiceDetailDto().toBuilder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .grandTotalAmount(entity.getGrandTotalAmount())
                .product(Preconditions.checkNotNull(entity.getProduct()) ? mapper.toResponseDto(entity.getProduct()) : null)
                .build();
    }

    public List<OInvoiceDetailDto> toInvoiceDetailDto(List<OrderDetail> entityList) {
        if(Preconditions.checkNull(entityList))
            return null;

        List<OInvoiceDetailDto> dtoList = new ArrayList<>();
        for (OrderDetail entity : entityList) {
            dtoList.add(toInvoiceDetailDto(entity));
        }
        return dtoList;
    }
}
