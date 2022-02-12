package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.models.order.Order;
import com.softwebdevelopers.ecommerce.models.order.Payment;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.OOrderRepository;
import com.softwebdevelopers.ecommerce.repository.OPaymentRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IOPaymentService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OPaymentServiceImpl implements IOPaymentService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    OPaymentRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Autowired
    OOrderRepository orderRepository;

    @Override
    public Page<Payment> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<Payment> paymentList = null;
        if(Preconditions.checkNotBlank(page.getKeyword())) {
            paymentList = repo.findByKeywordWithPagination(page.getKeyword(), indexPageWithElements);
        } else {
            paymentList = repo.findAll(indexPageWithElements);
        }

        if (Preconditions.checkNotNull(paymentList)) {
            log.info("The payment list returned successfully.");
            return paymentList;
        } else {
            log.warn("The payment [0] fetched failed. The payment list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public List<Payment> getByOrderId(Long orderId) throws RecordNotFoundException {
        Optional<List<Payment>> paymentList = repo.findByOrderId(orderId);
        if (paymentList.isPresent()) {
            log.info("The payment list with order Id: [{}] returned successfully.", orderId);
            return paymentList.get();
        } else {
            log.warn("The payment [0] fetched failed. The provided order Id: [{}] is not found.", orderId);
            throw new RecordNotFoundException("No payment record exists for given order Id: [" + orderId + "].");
        }
    }

    @Override
    public Payment create(Payment payment, Long orderId) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            Optional<Order> order = null;
            if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                order = orderRepository.findByIdAndUserId(user.get().getId(), orderId);
            } else {
                order = orderRepository.findById(orderId);
            }

            if (order.isEmpty()) {
                throw new ECOMException("No order exists.");
            }

            if (order.get().getAmountRemaining() < payment.getAmount()) {
                throw new ECOMException("Provided amount is more than remaining amount.");
            }

            payment.setOrder(order.get());
            payment.setUser(order.get().getUser());
            Payment entity = repo.save(payment);

            if (entity != null) {
                log.info("The payment [{}] created successfully.", entity.getId());
            } else {
                log.info("The payment for order id: [{}] creation failed.", payment.getOrder().getId());
            }
            return entity;
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }
}
