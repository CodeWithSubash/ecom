SELECT user_employees.id, user_employees.user_id, firstname, lastname, email, designation, phone, mobile, city, state, zip_code,
            commission,
            COUNT(orders.order_placed_by) AS total_order,
            IFNULL(SUM(orders.total_amount), 0) AS total_revenue,
            IFNULL(SUM(orders.discount_amount), 0) AS total_discount_amount,
            IFNULL(currentOrder.currentOrder, 0) AS current_count,
            IFNULL(currentOrder.total_amount, 0) AS current_total_amount,
            IFNULL(currentOrder.discount_amount, 0) AS current_discount_amount,
            IFNULL(previousOrder.previousOrder, 0) AS previous_count,
            IFNULL(previousOrder.total_amount, 0) AS previous_total_amount,
            IFNULL(previousOrder.discount_amount, 0) AS previous_discount_amount
            FROM user_employees
            LEFT JOIN orders on orders.user_id = user_employees.user_id
            LEFT JOIN (
            SELECT order_placed_by, COUNT(order_placed_by) AS currentOrder, SUM(total_amount) AS total_amount, SUM(discount_amount) AS discount_amount
            FROM orders
            WHERE
            YEAR(created_date) = YEAR(NOW())
            AND MONTH(created_date) = MONTH(NOW())
            GROUP BY order_placed_by) currentOrder ON currentOrder.order_placed_by = user_employees.user_id
            LEFT JOIN (
            SELECT order_placed_by, COUNT(order_placed_by) AS previousOrder, SUM(total_amount) AS total_amount, SUM(discount_amount) AS discount_amount
            FROM orders
            WHERE
            YEAR(created_date) = YEAR(NOW() - INTERVAL 1 MONTH)
            AND MONTH(created_date) = MONTH(NOW() - INTERVAL 1 MONTH)
            GROUP BY order_placed_by) previousOrder ON previousOrder.order_placed_by = user_employees.user_id
            GROUP BY user_employees.id, orders.order_placed_by;

SELECT user_retailers.id, user_retailers.user_id, business_name,
        COUNT(wishList.product_id)
            FROM user_retailers
            LEFT JOIN wishList ON wishList.user_id = user_retailers.user_id

            GROUP BY user_retailers.user_id, wishList.user_id