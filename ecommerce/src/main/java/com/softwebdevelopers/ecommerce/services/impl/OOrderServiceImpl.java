package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.ECOMException;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.enums.EDeliveryStatus;
import com.softwebdevelopers.ecommerce.models.enums.EPaymentStatus;
import com.softwebdevelopers.ecommerce.models.enums.ERole;
import com.softwebdevelopers.ecommerce.models.order.Order;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.product.ProductStocks;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.OOrderRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IOOrderService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OOrderServiceImpl implements IOOrderService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    OOrderRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Override
    public List<Order> getAll() throws RecordNotFoundException {
        return null;
    }

    @Override
    public Page<Order> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

            Page<Order> orderList = null;

            try {
                if (Preconditions.checkNotBlank(page.getKeyword())) {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndInvoiceNumberContaining(user.get().getId(), page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndInvoiceNumberContaining(user.get().getId(), page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByInvoiceNumberContaining(page.getKeyword(), indexPageWithElements);
                    }
                } else {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findByOrderPlacedUserId(user.get().getId(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findByUserId(user.get().getId(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAll(indexPageWithElements);
                    }
                }

                if (Preconditions.checkNotNull(orderList)) {
                    log.info("The order list returned successfully.");
                    return orderList;
                } else {
                    log.warn("The order [0] fetched failed. The order list are not found.");
                    throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
                }
            } catch (Exception ex) {
                throw new ECOMException("Error while fetching data of Order.");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<Order> getAllByDateRange(LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

            Page<Order> orderList = null;

            try {
                if (Preconditions.checkNotBlank(page.getKeyword())) {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(user.get().getId(), startDate, endDate, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(user.get().getId(), startDate, endDate, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(startDate, endDate, page.getKeyword(), indexPageWithElements);
                    }
                } else {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(user.get().getId(), startDate, endDate, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(user.get().getId(), startDate, endDate, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(startDate, endDate, indexPageWithElements);
                    }
                }

                if (Preconditions.checkNotNull(orderList)) {
                    log.info("The order list returned successfully.");
                    return orderList;
                } else {
                    log.warn("The order [0] fetched failed. The order list are not found.");
                    throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
                }
            } catch (Exception ex) {
                throw new ECOMException("Error while fetching data of Order.");
            }

        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<Order> getAllByDeliveryStatus(String deliveryStatus, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

            Page<Order> orderList = null;
            try {
                if (Preconditions.checkNotBlank(page.getKeyword())) {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndDeliveryStatusAndInvoiceNumberContaining(user.get().getId(), deliveryStatus, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndDeliveryStatusAndInvoiceNumberContaining(user.get().getId(), deliveryStatus, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByDeliveryStatusAndInvoiceNumberContaining(deliveryStatus, page.getKeyword(), indexPageWithElements);
                    }
                } else {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndDeliveryStatus(user.get().getId(), deliveryStatus, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndDeliveryStatus(user.get().getId(), deliveryStatus, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByDeliveryStatus(deliveryStatus, indexPageWithElements);
                    }
                }

                if (Preconditions.checkNotNull(orderList)) {
                    log.info("The order list returned successfully.");
                    return orderList;
                } else {
                    log.warn("The order [0] fetched failed. The order list are not found.");
                    throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
                }
            } catch (Exception ex) {
                throw new ECOMException("Error while fetching data of Order.");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<Order> getAllByDeliveryStatusAndDateRange(String deliveryStatus, LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

            Page<Order> orderList = null;
            try {
                if (Preconditions.checkNotBlank(page.getKeyword())) {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(user.get().getId(), deliveryStatus, startDate, endDate, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(user.get().getId(), deliveryStatus, startDate, endDate, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(deliveryStatus, startDate, endDate, page.getKeyword(), indexPageWithElements);
                    }
                } else {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(user.get().getId(), deliveryStatus, startDate, endDate, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(user.get().getId(), deliveryStatus, startDate, endDate, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(deliveryStatus, startDate, endDate, indexPageWithElements);
                    }
                }

                if (Preconditions.checkNotNull(orderList)) {
                    log.info("The order list returned successfully.");
                    return orderList;
                } else {
                    log.warn("The order [0] fetched failed. The order list are not found.");
                    throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
                }
            } catch (Exception ex) {
                throw new ECOMException("Error while fetching data of Order.");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<Order> getAllByPaymentStatus(String paymentStatus, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

            Page<Order> orderList = null;
            try {
                if (Preconditions.checkNotBlank(page.getKeyword())) {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndPaymentStatusAndInvoiceNumberContaining(user.get().getId(), paymentStatus, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndPaymentStatusAndInvoiceNumberContaining(user.get().getId(), paymentStatus, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByPaymentStatusAndInvoiceNumberContaining(paymentStatus, page.getKeyword(), indexPageWithElements);
                    }
                } else {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndPaymentStatus(user.get().getId(), paymentStatus, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndPaymentStatus(user.get().getId(), paymentStatus, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByPaymentStatus(paymentStatus, indexPageWithElements);
                    }
                }

                if (Preconditions.checkNotNull(orderList)) {
                    log.info("The order list returned successfully.");
                    return orderList;
                } else {
                    log.warn("The order [0] fetched failed. The order list are not found.");
                    throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
                }
            } catch (Exception ex) {
                throw new ECOMException("Error while fetching data of Order.");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Page<Order> getAllByPaymentStatusAndDateRange(String paymentStatus, LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                    page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

            Page<Order> orderList = null;
            try {
                if (Preconditions.checkNotBlank(page.getKeyword())) {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(user.get().getId(), paymentStatus, startDate, endDate, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(user.get().getId(), paymentStatus, startDate, endDate, page.getKeyword(), indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(paymentStatus, startDate, endDate, page.getKeyword(), indexPageWithElements);
                    }
                } else {
                    if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name()))) {
                        orderList = repo.findAllByOrderPlacedUserIdAndPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(user.get().getId(), paymentStatus, startDate, endDate, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name()))) {
                        orderList = repo.findAllByUserIdAndPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(user.get().getId(), paymentStatus, startDate, endDate, indexPageWithElements);
                    } else if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SUPERADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_ADMIN.name()))
                            || user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_WHOLESALER.name()))) {
                        orderList = repo.findAllByPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(paymentStatus, startDate, endDate, indexPageWithElements);
                    }
                }

                if (Preconditions.checkNotNull(orderList)) {
                    log.info("The order list returned successfully.");
                    return orderList;
                } else {
                    log.warn("The order [0] fetched failed. The order list are not found.");
                    throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
                }
            } catch (Exception ex) {
                throw new ECOMException("Error while fetching data of Order.");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Order getById(Long id) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Optional<Order> order = repo.findById(id);


            if (order.isPresent()) {
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_SALES.name())) &&
                        order.get().getOrderPlacedUserId().getId() != user.get().getId()) {
                    log.info("The order with Id: [{}] is not authorized to view by User: [{}].", id, user.get().getId());
                    throw new ECOMException("The salesperson is not authorized to view this order.");
                }
                if (user.get().getRoles().stream().anyMatch(item -> item.getName().equalsIgnoreCase(ERole.ROLE_RETAILER.name())) &&
                        order.get().getUser().getId() != user.get().getId()) {
                    log.info("The order with Id: [{}] is not authorized to view by User: [{}].", id, user.get().getId());
                    throw new ECOMException("The user is not authorized to view this order.");
                }

                log.info("The order with Id: [{}] returned successfully.", id);
                return order.get();
            } else {
                log.warn("The order record [0] fetched failed. The provided Id: [{}] is not found.", id);
                throw new RecordNotFoundException("No product order record exists for given Id: [" + id + "].");
            }
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Order create(Order order) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            order.setDeliveryStatus(EDeliveryStatus.PLACED.name());
            order.setPaymentStatus(EPaymentStatus.PENDING.name());
            order.setOrderPlacedUserId(user.get());
            Order entity = repo.save(order);

            if (entity != null) {
                log.info("The order [{}] created successfully.", entity.getId());
            } else {
                log.info("The order [{}] creation failed.", order.getUser().getName());
            }
            return entity;
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Order update(Order order) throws RecordNotFoundException {
        return null;
    }

    @Override
    public Order updateDeliveryStatus(Long id, String status) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());

        if (user.isPresent()) {
            Order entity = repo.getById(id);
            entity.setDeliveryStatus(status);
            repo.save(entity);

            if (entity != null) {
                log.info("The order status [{}] updated successfully.", status);
            } else {
                log.info("The order status [{}] updated failed.", status);
            }
            return entity;
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public Order deleteSoft(Long id) throws RecordNotFoundException {
        return null;
    }
}
