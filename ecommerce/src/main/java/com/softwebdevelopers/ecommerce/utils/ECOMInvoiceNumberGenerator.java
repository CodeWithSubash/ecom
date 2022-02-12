package com.softwebdevelopers.ecommerce.utils;


import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
import com.softwebdevelopers.ecommerce.models.common.InvoiceReference;
import com.softwebdevelopers.ecommerce.repository.CInvoiceReferenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
public class ECOMInvoiceNumberGenerator {

    @Autowired
    CInvoiceReferenceRepository repo;

    public String getInvoiceNumber(Long userId) {
        if(userId == 0)
            return null;
        try {
            Optional<InvoiceReference> invoiceReference = repo.findByIdYearAndIdUserId(LocalDateTime.now().getYear(), userId);

//          FORMAT:: 2022-01INV00000001
            String year = String.valueOf(LocalDateTime.now().getYear());
            String order = "01";
            String invoiceNumber = "00000001";
            if (invoiceReference.isPresent()) {
                if (invoiceReference.get().getLatestSequence() == 99999999) {
                    int num = Integer.parseInt(invoiceReference.get().getLatestInvoiceNumber().substring(5, 6)) + 1;
                    order = String.format("%02d", num);
                    invoiceNumber = String.format("%08d", 1);
                } else {
                    invoiceNumber = String.format("%08d", invoiceReference.get().getLatestSequence() + 1);
                }
            }

            return year.concat("-").concat(order).concat(ECOMConstants.INVOICE_CHAR).concat(invoiceNumber);
        } catch (Exception e) {
            log.error("Error while fetching invoice reference number.");
            throw new ECOMException("Error while fetching invoice reference number.", e);
        }
    }

}
