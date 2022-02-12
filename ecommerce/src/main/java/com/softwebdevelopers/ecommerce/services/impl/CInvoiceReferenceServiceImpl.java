package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.common.InvoiceReference;
import com.softwebdevelopers.ecommerce.repository.CInvoiceReferenceRepository;
import com.softwebdevelopers.ecommerce.services.ICInvoiceReferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CInvoiceReferenceServiceImpl implements ICInvoiceReferenceService {

    @Autowired
    CInvoiceReferenceRepository repo;

    @Override
    public InvoiceReference getById(Long userId, int year) throws RecordNotFoundException {
        Optional<InvoiceReference> invoiceReference = repo.findByIdYearAndIdUserId(year, userId);
        if (invoiceReference.isPresent()) {
            log.info("The Invoice Reference with User Id: [{}] and Year: [{}] returned successfully.", userId, year);
            return invoiceReference.get();
        } else {
            log.warn("The Invoice Reference [0] fetched failed. The provided User Id: [{}] and Year: [{}] is not found.", userId, year);
            throw new RecordNotFoundException("No Invoice Reference record exists for given User Id: [" + userId + "] and Year: [" + year + ".]");
        }
    }

    @Override
    public InvoiceReference create(InvoiceReference invoiceRef) throws RecordNotFoundException {
        InvoiceReference entity = repo.save(invoiceRef);

        if (entity != null) {
            log.info("The invoice reference [{}] created successfully.", entity.getId());
        } else {
            log.info("The invoice reference [{}] creation failed.", invoiceRef.getLatestSequence());
        }
        return entity;
    }

    @Override
    public InvoiceReference update(InvoiceReference invoiceRef) throws RecordNotFoundException {
        return null;
    }
}
