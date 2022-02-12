package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.custom.IProductCount;
import com.softwebdevelopers.ecommerce.models.order.Order;
import com.softwebdevelopers.ecommerce.models.custom.IRetailerCount;
import com.softwebdevelopers.ecommerce.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OOrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT YEAR(o.created_date) AS oYear, MONTH(o.created_date) AS oMonth, MONTHNAME(o.created_date) as monthName, COUNT(o.id) AS countOrder, IFNULL(SUM(o.grand_total_amount), 0) AS totalAmount "
            + " FROM orders AS o "
            + " WHERE o.user_id = ?1 "
            + " GROUP BY YEAR(o.created_date), MONTH(o.created_date), MONTHNAME(o.created_date) "
            + " ORDER BY YEAR(o.created_date) DESC, MONTH(o.created_date) DESC LIMIT 12", nativeQuery = true)
    List<IRetailerCount> countRetailerTotalOrder(Long userId);

    @Query(value = "SELECT YEAR(od.created_date) AS o_year, MONTH(od.created_date) AS o_month, MONTHNAME(od.created_date) AS month_name, o.product_id, p.name AS product_name, o.cnt AS product_count " +
            "    FROM order_details od" +
            "    INNER JOIN" +
            "    (SELECT od.product_id, COUNT(od.product_id) AS cnt FROM order_details od" +
            "        INNER JOIN orders o ON o.id = od.order_id" +
            "        WHERE o.order_placed_user_id = ?1 AND o.created_date BETWEEN (NOW() - INTERVAL 12 MONTH) AND NOW()" +
            "        GROUP BY od.product_id" +
            "        ORDER BY cnt DESC LIMIT 5) AS o ON o.product_id = od.product_id" +
            "    INNER JOIN products p ON p.id = o.product_id" +
            "    GROUP BY YEAR(od.created_date), MONTH(od.created_date), MONTHNAME(od.created_date), o.product_id, p.name" +
            "    ORDER BY YEAR(od.created_date) DESC, MONTH(od.created_date) DESC LIMIT 60", nativeQuery = true)
    List<IProductCount> countEmployeeTotalOrder(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT * FROM orders WHERE id = ?1 AND order_placed_user_id = ?2 ",
            nativeQuery = true)
    Optional<Order> findByIdAndOrderPlacedUserId(Long id, Long userId);

    Optional<Order> findAllByInvoiceNumber(String name);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 ",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findByOrderPlacedUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 ",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findAllByInvoiceNumberContaining(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND invoice_number LIKE %?2% ",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndInvoiceNumberContaining(Long userId, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND invoice_number LIKE %?2%",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndInvoiceNumberContaining(Long userId, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND created_date >= ?2 AND created_date <= ?3",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND created_date >= ?2 AND created_date <= ?3",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Order> findAllByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND created_date >= ?2 AND created_date <= ?3 AND invoice_number LIKE %?4% ",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(Long userId, LocalDateTime startDate, LocalDateTime endDate, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND created_date >= ?2 AND created_date <= ?3 AND invoice_number LIKE %?4% ",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(Long userId, LocalDateTime startDate, LocalDateTime endDate, String keyword, Pageable pageable);

    Page<Order> findAllByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(LocalDateTime startDate, LocalDateTime endDate, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND delivery_status = ?2 ",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndDeliveryStatus(Long userId, String deliveryStatus, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND delivery_status = ?2 ",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndDeliveryStatus(Long userId, String deliveryStatus, Pageable pageable);

    Page<Order> findAllByDeliveryStatus(String deliveryStatus, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND delivery_status = ?2 AND invoice_number LIKE %?3% ",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndDeliveryStatusAndInvoiceNumberContaining(Long userId, String deliveryStatus, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND delivery_status = ?2 AND invoice_number LIKE %?3% ",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndDeliveryStatusAndInvoiceNumberContaining(Long userId, String deliveryStatus, String keyword, Pageable pageable);

    Page<Order> findAllByDeliveryStatusAndInvoiceNumberContaining(String deliveryStatus, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND delivery_status = ?2 AND created_date >= ?3 AND created_date <= ?4",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Long userId, String deliveryStatus, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND delivery_status = ?2 AND created_date >= ?3 AND created_date <= ?4",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Long userId, String deliveryStatus, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Order> findAllByDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(String deliveryStatus, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND delivery_status = ?2 AND created_date >= ?3 AND created_date <= ?4 AND invoice_number LIKE %?5% ",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(Long userId, String deliveryStatus, LocalDateTime startDate, LocalDateTime endDate, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND delivery_status = ?2 AND created_date >= ?3 AND created_date <= ?4 AND invoice_number LIKE %?5% ",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(Long userId, String deliveryStatus, LocalDateTime startDate, LocalDateTime endDate, String keyword, Pageable pageable);

    Page<Order> findAllByDeliveryStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(String deliveryStatus, LocalDateTime startDate, LocalDateTime endDate, String keyword, Pageable pageable);

    //Invoice Payments
    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND payment_status = ?2 AND created_date >= ?3 AND created_date <= ?4",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Long userId, String paymentStatus, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND payment_status = ?2 AND created_date >= ?3 AND created_date <= ?4",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(Long userId, String paymentStatus, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Order> findAllByPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual(String paymentStatus, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND payment_status = ?2 AND created_date >= ?3 AND created_date <= ?4 AND invoice_number LIKE %?5% ",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(Long userId, String paymentStatus, LocalDateTime startDate, LocalDateTime endDate, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND payment_status = ?2 AND created_date >= ?3 AND created_date <= ?4 AND invoice_number LIKE %?5% ",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(Long userId, String paymentStatus, LocalDateTime startDate, LocalDateTime endDate, String keyword, Pageable pageable);

    Page<Order> findAllByPaymentStatusAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndInvoiceNumberContaining(String paymentStatus, LocalDateTime startDate, LocalDateTime endDate, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND payment_status = ?2 ",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndPaymentStatus(Long userId, String paymentStatus, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND payment_status = ?2 ",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndPaymentStatus(Long userId, String paymentStatus, Pageable pageable);

    Page<Order> findAllByPaymentStatus(String paymentStatus, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND payment_status = ?2 AND invoice_number LIKE %?3% ",
            countQuery = "SELECT count(*) FROM orders WHERE user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByUserIdAndPaymentStatusAndInvoiceNumberContaining(Long userId, String paymentStatus, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE order_placed_user_id = ?1 AND payment_status = ?2 AND invoice_number LIKE %?3% ",
            countQuery = "SELECT count(*) FROM orders WHERE order_placed_user_id = ?1",
            nativeQuery = true)
    Page<Order> findAllByOrderPlacedUserIdAndPaymentStatusAndInvoiceNumberContaining(Long userId, String paymentStatus, String keyword, Pageable pageable);

    Page<Order> findAllByPaymentStatusAndInvoiceNumberContaining(String paymentStatus, String keyword, Pageable pageable);
}
