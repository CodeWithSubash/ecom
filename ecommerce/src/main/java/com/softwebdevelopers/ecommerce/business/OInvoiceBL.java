package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.EDeliveryStatus;
import com.softwebdevelopers.ecommerce.models.enums.EPaymentStatus;
import com.softwebdevelopers.ecommerce.models.modelmapper.OrderMapper;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.services.IOOrderService;
import com.softwebdevelopers.ecommerce.utils.ECOMUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OInvoiceBL {

    @Autowired
    IOOrderService service;

    @Autowired
    ImageBL imageService;

    @Autowired
    OrderMapper mapper;

    public PaginationRecordsDto<OInvoiceDto> getAll(String paymentStatus, String startDate, String endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        if (Preconditions.checkNotBlank(paymentStatus) &&
                ECOMUtilities.enumContains(EPaymentStatus.class, paymentStatus) &&
                Preconditions.checkNotBlank(startDate) &&
                Preconditions.checkNotBlank(endDate)) {

            PaginationRecordsDto<OInvoiceDto> dto = mapper.toInvoiceDto(service.getAllByPaymentStatusAndDateRange(
                    paymentStatus,
                    ECOMUtilities.convertToLocalDateTime(startDate, null).toLocalDate().atTime(LocalTime.MIN),
                    ECOMUtilities.convertToLocalDateTime(endDate, null).toLocalDate().atTime(LocalTime.MAX),
                    page), ECOMConstants.ORDER_NO_DETAIL);
            return dto;
        } else if (Preconditions.checkNotBlank(paymentStatus) &&
                ECOMUtilities.enumContains(EPaymentStatus.class, paymentStatus)) {

            PaginationRecordsDto<OInvoiceDto> dto = mapper.toInvoiceDto(service.getAllByPaymentStatus(
                    paymentStatus,
                    page), ECOMConstants.ORDER_NO_DETAIL);
            return dto;
        } else if (Preconditions.checkNotBlank(startDate) &&
                Preconditions.checkNotBlank(endDate)) {
            PaginationRecordsDto<OInvoiceDto> dto = mapper.toInvoiceDto(service.getAllByDateRange(
                    ECOMUtilities.convertToLocalDateTime(startDate, null).toLocalDate().atTime(LocalTime.MIN),
                    ECOMUtilities.convertToLocalDateTime(endDate, null).toLocalDate().atTime(LocalTime.MAX),
                    page), ECOMConstants.ORDER_NO_DETAIL);
            return dto;
        } else {
            PaginationRecordsDto<OInvoiceDto> dto = mapper.toInvoiceDto(service.getAll(page), ECOMConstants.ORDER_NO_DETAIL);
            return dto;
        }
    }

    public OInvoiceDto getById(Long id) throws RecordNotFoundException {
        OInvoiceDto dto = mapper.toInvoiceDto(service.getById(id), ECOMConstants.ORDER_WITH_DETAIL);
        for (OInvoiceDetailDto var : dto.getOrderDetails()) {
            List<ImageDto> images = imageService.getAllById(var.getId(), Product.class.toString());
            if (Preconditions.checkNotNull(images)) {
                var.getProduct().setFiles(images.stream().map(file -> new UploadFileResponseDto().toBuilder()
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
}
