package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.common.InvoiceReference;

public interface ICInvoiceReferenceService {

    InvoiceReference getById(Long userId, int year) throws RecordNotFoundException;

    InvoiceReference create(InvoiceReference invoiceRef)  throws RecordNotFoundException;

    InvoiceReference update(InvoiceReference invoiceRef)  throws RecordNotFoundException;
}
