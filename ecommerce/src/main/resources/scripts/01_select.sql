SELECT YEAR(o.created_date) AS oYear, MONTH(o.created_date) AS oMonth, MONTHNAME(o.created_date) as monthName, COUNT(o.id) AS countOrder, IFNULL(SUM(o.grand_total_amount), 0) AS totalAmount "
            + " FROM orders AS o "
            + " WHERE o.user_id = ?1 "
            + " GROUP BY YEAR(o.created_date), MONTH(o.created_date), MONTHNAME(o.created_date) "
            + " ORDER BY YEAR(o.created_date) DESC, MONTH(o.created_date) DESC LIMIT 12

SELECT YEAR(od.created_date) AS o_year, MONTH(od.created_date) AS o_month, MONTHNAME(od.created_date) AS month_name, o.product_id, p.name AS product_name, o.cnt AS product_count
    FROM order_details od
    INNER JOIN
    (SELECT od.product_id, COUNT(od.product_id) AS cnt FROM order_details od
        INNER JOIN orders o ON o.id = od.order_id
        WHERE o.order_placed_by = 4 AND o.created_date BETWEEN (NOW() - INTERVAL 12 MONTH) AND NOW()
        GROUP BY od.product_id
        ORDER BY cnt DESC LIMIT 5) AS o ON o.product_id = od.product_id
    INNER JOIN products p ON p.id = o.product_id
    GROUP BY YEAR(od.created_date), MONTH(od.created_date), MONTHNAME(od.created_date), o.product_id, p.name
    ORDER BY YEAR(od.created_date) DESC, MONTH(od.created_date) DESC LIMIT 60;

