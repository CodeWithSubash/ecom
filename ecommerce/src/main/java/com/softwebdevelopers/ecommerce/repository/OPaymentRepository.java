package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.order.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OPaymentRepository extends JpaRepository<Payment, Long> {

    Optional<List<Payment>> findByOrderId(Long orderId);

    @Query(value = "SELECT p.* FROM payments p INNER JOIN orders o ON o.id = p.order_id WHERE o.invoice_number LIKE %?1%",
            countQuery = "SELECT count(*) FROM payments",
            nativeQuery = true)
    Page<Payment> findByKeywordWithPagination(String keyword, Pageable pageable);

}
