package com.softwebdevelopers.ecommerce.business;

import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.config.FileStorageProperties;
import com.softwebdevelopers.ecommerce.dto.*;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.modelmapper.PaymentMapper;
import com.softwebdevelopers.ecommerce.models.order.Order;
import com.softwebdevelopers.ecommerce.models.order.Payment;
import com.softwebdevelopers.ecommerce.repository.OOrderRepository;
import com.softwebdevelopers.ecommerce.services.IFileStorageService;
import com.softwebdevelopers.ecommerce.services.IOPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OPaymentBL {

    @Autowired
    IOPaymentService service;

    @Autowired
    PaymentMapper mapper;

    @Autowired
    ImageBL imageService;

    @Autowired
    AuditActivityRecordBL auditService;

    @Autowired
    IFileStorageService fileStorageService;

    private final Path fileStorageLocation;

    @Autowired
    public OPaymentBL(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    public PaginationRecordsDto<OPaymentDto> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        PaginationRecordsDto<OPaymentDto> dto = mapper.toDto(service.getAll(page));
        return dto;
    }

    public List<OPaymentDto> getByOrderId(Long id) throws RecordNotFoundException {
        List<OPaymentDto> dto = mapper.toDto(service.getByOrderId(id));
        return dto;
    }

    public OPaymentDto create(OPaymentDto payment) {

        ImageDto imageLogo = null;
        if(Preconditions.checkNotNull(payment.getFile())) {
            imageLogo = fileStorageService.storeFile(payment.getFile(),
                    new FileStorageDto().toBuilder()
                            .title("PAYMENT_" + payment.getOrderId())
                            .uuid(UUID.randomUUID().toString())
                            .dirName("receipt")
                            .build());
        }

        Payment entity = mapper.toEntity(payment);
        entity.setUuid(UUID.randomUUID());
        Payment created = service.create(entity, payment.getOrderId());
        if (created == null) {
            log.warn("The payment for order Id: [{}] creation failed", payment.getOrderId());
            return null;
        }

        if(Preconditions.checkNotNull(imageLogo)) {
            imageLogo.setImagePath(imageLogo.getFileDownloadUri());
            imageLogo.setModelId(created.getId());
            imageLogo.setModelType(Payment.class.toString());
            imageLogo = imageService.create(imageLogo);
            if (imageLogo == null) {
                log.warn("The image creation failed for payment with order Id: [{}]", payment.getOrderId());
                return null;
            }
        }

        auditService.create(
                new AuditActivityRecordDto().toBuilder()
                        .clazz(OPaymentBL.class.toString())
                        .method(ECOMConstants.CREATED)
                        .asString("Added new <b>Payment</b> for Invoice Number, <b>"
                                .concat(created.getOrder().getInvoiceNumber())
                                .concat("</b>"))
                        .build());

        OPaymentDto paymentDto = mapper.toDto(created);
        paymentDto.setFilePath(Preconditions.checkNotNull(imageLogo) ? imageLogo.getImagePath() : null);
        return paymentDto;
    }

}
