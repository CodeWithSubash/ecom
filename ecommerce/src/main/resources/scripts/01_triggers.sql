
-- TRIGGER TO UPDATE AVAILABLE STOCK IN PRODUCT TABLE WHEN INSERTED INTO PRODUCT_STOCK TABLE

DROP TRIGGER IF EXISTS trg_ins_product_stocks_update_products;

CREATE TRIGGER trg_ins_product_stocks_update_products
AFTER INSERT ON product_stocks
FOR EACH ROW

		UPDATE products
		SET available_stock = NEW.new_stock,
		    updated_by = NEW.updated_by,
		    updated_date = LOCALTIMESTAMP()
		WHERE id = NEW.product_id;


-- TRIGGER TO UPDATE REMAINING AMOUNT IN ORDER TABLE WHEN INSERTED INTO PAYMENT TABLE

DROP TRIGGER IF EXISTS trg_ins_payment_update_order;

DELIMITER $$
CREATE TRIGGER trg_ins_payment_update_order
AFTER INSERT ON payments
FOR EACH ROW
    BEGIN
        UPDATE orders
        SET amount_paid = amount_paid + NEW.amount,
            amount_remaining = amount_remaining - NEW.amount,
            updated_date = LOCALTIMESTAMP()
        WHERE id = NEW.order_id
        AND user_id = NEW.user_id;

        IF(SELECT amount_remaining FROM orders WHERE id = New.order_id <= 0) THEN
            UPDATE orders
            SET payment_status = 'PAID'
            WHERE id = NEW.order_id
            AND user_id = NEW.user_id;
        END IF;
    END$$
DELIMITER ;

