package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.order.Payment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOPaymentService {

    Page<Payment> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    List<Payment> getByOrderId(Long orderId) throws RecordNotFoundException;

    Payment create(Payment payment, Long orderId)  throws RecordNotFoundException;
}
