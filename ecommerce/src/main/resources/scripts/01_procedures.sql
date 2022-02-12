DROP PROCEDURE IF EXISTS sp_GetSalesPersonRecords;

DELIMITER //

CREATE PROCEDURE sp_GetSalesPersonRecords(
	IN userId LONG
)
BEGIN
	DECLARE val_id LONG;
	DECLARE val_user_id LONG;
	DECLARE val_firstname VARCHAR(100);
	DECLARE val_lastname VARCHAR(100);
	DECLARE val_email VARCHAR(255);
	DECLARE val_designation VARCHAR(30);
	DECLARE val_phone VARCHAR(20);
	DECLARE val_mobile VARCHAR(20);
	DECLARE val_city VARCHAR(25);
	DECLARE val_state VARCHAR(50);
	DECLARE val_zip_code VARCHAR(25);
	DECLARE val_commission DECIMAL(10,2);

    DECLARE total_revenue DECIMAL(10,2) DEFAULT 0;
	DECLARE total_average_price DECIMAL(10,2) DEFAULT 0;
	DECLARE current_revenue DECIMAL(10,2) DEFAULT 0;
    DECLARE current_order INT DEFAULT 0;
	DECLARE previous_revenue DECIMAL(10,2) DEFAULT 0;
    DECLARE previous_order INT DEFAULT 0;

	SELECT id, user_id, firstname, lastname, email, designation, phone, mobile, city, state, zip_code, commission
    INTO val_id, val_user_id, val_firstname, val_lastname, val_email, val_designation, val_phone, val_mobile, val_city, val_state, val_zip_code, val_commission
    FROM user_employees
    WHERE user_id = userId;

    IF(val_commission > 0) THEN
		SELECT SUM(total_amount - discount_amount) / val_commission, SUM(total_amount - discount_amount) / COUNT(*)
		INTO total_revenue, total_average_price
		FROM orders
		WHERE user_id = userId;

		SELECT SUM(total_amount - discount_amount) / val_commission, COUNT(*)
		INTO current_revenue, current_order
		FROM orders
		WHERE user_id = userId
		AND YEAR(created_date) = YEAR(NOW())
		AND MONTH(created_date) = MONTH(NOW());

        SELECT SUM(total_amount - discount_amount) / val_commission, COUNT(*)
		INTO previous_revenue, previous_order
		FROM orders
		WHERE user_id = userId
		AND YEAR(created_date) = YEAR(NOW() - INTERVAL 1 MONTH)
		AND MONTH(created_date) = MONTH(NOW() - INTERVAL 1 MONTH);
	END IF;

    SELECT val_id, val_user_id, val_firstname, val_lastname, val_email, val_designation,
    val_phone, val_mobile, val_city, val_state, val_zip_code, val_commission,
    total_revenue, total_average_price, current_revenue, current_order, previous_revenue, previous_order;
END //

DELIMITER ;