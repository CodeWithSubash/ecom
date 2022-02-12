package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.order.Coupon;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface IOCouponService {

    List<Coupon> getAll() throws RecordNotFoundException;

    Page<Coupon> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Coupon getById(UUID uuid) throws RecordNotFoundException;

    Coupon getByCouponCode(String code) throws RecordNotFoundException;

    Coupon create(Coupon order)  throws RecordNotFoundException;

    Coupon update(Coupon order)  throws RecordNotFoundException;

    Coupon dispose(String uuid) throws RecordNotFoundException;

    Coupon delete(Long id) throws RecordNotFoundException;
}
