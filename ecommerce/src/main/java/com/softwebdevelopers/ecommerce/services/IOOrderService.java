package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.order.Order;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IOOrderService {

    List<Order> getAll() throws RecordNotFoundException;

    Page<Order> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Order> getAllByDateRange(LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Order> getAllByDeliveryStatus(String deliveryStatus, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Order> getAllByDeliveryStatusAndDateRange(String deliveryStatus, LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Order> getAllByPaymentStatus(String paymentStatus, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Page<Order> getAllByPaymentStatusAndDateRange(String paymentStatus, LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException;

    Order getById(Long id) throws RecordNotFoundException;

    Order create(Order order)  throws RecordNotFoundException;

    Order update(Order order)  throws RecordNotFoundException;

    Order updateDeliveryStatus(Long id, String status) throws RecordNotFoundException;

    Order deleteSoft(Long id) throws RecordNotFoundException;
}
