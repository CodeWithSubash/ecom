package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.cart.Cart;
import com.softwebdevelopers.ecommerce.models.common.InvoiceReference;
import com.softwebdevelopers.ecommerce.models.common.InvoiceReferenceIdentity;
import com.softwebdevelopers.ecommerce.models.enums.EDeliveryStatus;
import com.softwebdevelopers.ecommerce.models.enums.EStockRemark;
import com.softwebdevelopers.ecommerce.models.enums.EStockType;
import com.softwebdevelopers.ecommerce.models.modelmapper.OrderMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.ProductMapper;
import com.softwebdevelopers.ecommerce.models.modelmapper.UserMapper;
import com.softwebdevelopers.ecommerce.models.order.Order;
import com.softwebdevelopers.ecommerce.models.order.OrderDetail;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.services.ICInvoiceReferenceService;
import com.softwebdevelopers.ecommerce.services.IOOrderService;
import com.softwebdevelopers.ecommerce.services.IPCartService;
import com.softwebdevelopers.ecommerce.utils.ECOMInvoiceNumberGenerator;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OOrderBL {

    @Autowired
    IOOrderService service;

    @Autowired
    PCartBL cartService;

    @Autowired
    IPCartService cartDeleteService;

    @Autowired
    PProductStockBL stockBL;

    @Autowired
    ICInvoiceReferenceService invoiceReferenceService;

    @Autowired
    ECOMInvoiceNumberGenerator invoiceGeneratorService;

    @Autowired
    OrderMapper mapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuditActivityRecordBL auditService;

    public PaginationRecordsDto<OOrderDto> getAll(String deliveryStatus, String startDate, String endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        if (Preconditions.checkNotBlank(deliveryStatus) &&
                ECOMUtilities.enumContains(EDeliveryStatus.class, deliveryStatus) &&
                Preconditions.checkNotBlank(startDate) &&
                Preconditions.checkNotBlank(endDate)) {

            PaginationRecordsDto<OOrderDto> dto = mapper.toDto(service.getAllByDeliveryStatusAndDateRange(
                    deliveryStatus,
                    ECOMUtilities.convertToLocalDateTime(startDate, null).toLocalDate().atTime(LocalTime.MIN),
                    ECOMUtilities.convertToLocalDateTime(endDate, null).toLocalDate().atTime(LocalTime.MAX),
                    page), ECOMConstants.ORDER_NO_DETAIL);
            return dto;
        } else if (Preconditions.checkNotBlank(deliveryStatus) &&
                ECOMUtilities.enumContains(EDeliveryStatus.class, deliveryStatus)) {

            PaginationRecordsDto<OOrderDto> dto = mapper.toDto(service.getAllByDeliveryStatus(
                    deliveryStatus,
                    page), ECOMConstants.ORDER_NO_DETAIL);
            return dto;
        } else if (Preconditions.checkNotBlank(startDate) &&
                Preconditions.checkNotBlank(endDate)) {
            PaginationRecordsDto<OOrderDto> dto = mapper.toDto(service.getAllByDateRange(
                    ECOMUtilities.convertToLocalDateTime(startDate, null).toLocalDate().atTime(LocalTime.MIN),
                    ECOMUtilities.convertToLocalDateTime(endDate, null).toLocalDate().atTime(LocalTime.MAX),
                    page), ECOMConstants.ORDER_NO_DETAIL);
            return dto;
        } else {
            PaginationRecordsDto<OOrderDto> dto = mapper.toDto(service.getAll(page), ECOMConstants.ORDER_NO_DETAIL);
            return dto;
        }
    }

    public OOrderDto getById(Long id) throws RecordNotFoundException {
        OOrderDto dto = mapper.toDto(service.getById(id), ECOMConstants.ORDER_WITH_DETAIL);

        return dto;
    }

    public OOrderDto create(OOrderDto order, Long userId) {

        PCartDto cartDto = null;
        if (userId != null && userId != 0) {
            cartDto = cartService.getAllByCreatedUser(userId);
        } else {
            cartDto = cartService.getAllByUser();
        }

        if (Preconditions.checkNotNull(cartDto)) {
            Long wholesalerId = cartDto.getCartDetail()
                    .stream()
                    .findFirst().get()
                    .getProduct()
                    .getWholesalerId();
            order.setBillNumber(invoiceGeneratorService.getInvoiceNumber(wholesalerId));
            order.setGrandTotalAmount(order.getTotalAmount() - order.getDiscountAmount() + order.getDeliveryCharge() + order.getTotalVat());
            order.setAmountRemaining(order.getGrandTotalAmount());
            if(Preconditions.checkBlank(order.getBillingAddress())) {
                order.setBillingAddress(cartDto.getUser().getUserRetailer().getBusinessAddress());
            }

            Order entity = mapper.toEntity(order);
//            entity.setGrandTotalAmount(entity.getTotalAmount() - entity.getDiscountAmount() + entity.getDeliveryCharge() + entity.getTotalVat());
            entity.setUser(userMapper.toEntity(cartDto.getUser()));
//            entity.setInvoiceNumber(invoiceGeneratorService.getInvoiceNumber(wholesalerId));
//            entity.setAmountRemaining(entity.getGrandTotalAmount());
//            if(Preconditions.checkBlank(order.getBillingAddress())) {
//                entity.setBillingAddress(cartDto.getUser().getUserRetailer().getBusinessAddress());
//            }

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (PCartDetailDto idx : cartDto.getCartDetail()) {
                if (idx.getProduct().getAvailableStock() >= idx.getQuantity()) {
                    orderDetails.add(new OrderDetail().toBuilder()
                            .order(entity)
                            .quantity(idx.getQuantity())
                            .totalAmount(idx.getQuantity() * idx.getProduct().getPrice())
                            .deliveryCharge(0)
                            .discountAmount(0)
                            .vat(0)
                            .grandTotalAmount(idx.getQuantity() * idx.getProduct().getPrice())
                            .product(Preconditions.checkNotNull(idx.getProduct()) ? productMapper.toEntity(idx.getProduct()) : null)
                            .build());
                } else {
                    log.warn("The cart have the product [{}] quantity is more than the available.", idx.getProduct().getId());
                    throw new ECOMException("The cart have the product Id: [" + idx.getProduct().getId() + "] quantity is more than the available.");
                }
            }
            entity.setOrderDetails(orderDetails);

            Order created = service.create(entity);
            if (created == null) {
                log.warn("The order [0] creation failed");
                return null;
            }

            invoiceReferenceService.create(new InvoiceReference().toBuilder()
                    .id(new InvoiceReferenceIdentity().toBuilder()
                            .userId(wholesalerId)
                            .year(LocalDateTime.now().getYear())
                            .build())
                    .latestSequence(Integer.parseInt(entity.getInvoiceNumber().substring(entity.getInvoiceNumber().indexOf(ECOMConstants.INVOICE_CHAR) + ECOMConstants.INVOICE_CHAR.length())))
                    .latestInvoiceNumber(entity.getInvoiceNumber())
                    .build());

            Cart cart = cartDeleteService.deleteSoft(cartDto.getId());
            if (cart != null) {
                for (OrderDetail idx :
                        orderDetails) {
                    stockBL.create(new PProductStockDto().toBuilder()
                            .productId(idx.getProduct().getId())
                            .changeStock(idx.getQuantity())
                            .changeType(EStockType.DECREMENT.name())
                            .remarks(EStockRemark.PRODUCT_SOLD.name())
                            .build());
                }

            }
            auditService.create(
                    new AuditActivityRecordDto().toBuilder()
                            .clazz(OOrderBL.class.toString())
                            .method(ECOMConstants.CREATED)
                            .asString("Added new <b>Order</b> is placed by "
                                    .concat(created.getUser().getName()))
                            .build());
            return mapper.toDto(created, ECOMConstants.ORDER_WITH_DETAIL);
        } else {
            throw new ECOMException("No items found in cart to place order.");
        }
    }

    public OOrderDto updateDeliveryStatus(Long id, String status) {
        if (!ECOMUtilities.enumContains(EDeliveryStatus.class, status)) {
            throw new ECOMException("Provided delivery status is not applicable.");
        }

        return mapper.toDto(service.updateDeliveryStatus(id, status), ECOMConstants.ORDER_WITH_DETAIL);
    }
}
