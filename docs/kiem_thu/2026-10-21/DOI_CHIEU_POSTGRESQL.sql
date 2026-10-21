-- NOT RUN: bộ truy vấn chỉ đọc dự kiến cho PostgreSQL QA; không phải kết quả audit.
-- Dùng psql variables thực tế từ response/fixture: sku_id,store_id,order_id,customer_id,
-- po_id,transfer_id,account_id,qa_email. Gán bằng \set trước khi chạy từng khối.
-- Không SELECT password_hash, otp_code, JWT hoặc dữ liệu khách thật.
BEGIN TRANSACTION READ ONLY;

-- Q1. Chụp trước/sau đặt/hủy/giao/điều chuyển/điều chỉnh; tên cột theo InventoryEntity.
SELECT store_id, sku_id, on_hand_qty, reserved_qty,
       GREATEST(0, on_hand_qty-reserved_qty) AS available_qty,
       low_stock_threshold
FROM inventories WHERE store_id=:'store_id' AND sku_id=:'sku_id';

-- Q2. Tiền/trạng thái/chi nhánh và snapshot item sau checkout hoặc đổi trạng thái.
SELECT id, order_code, customer_id, fulfillment_store_id, status, payment_method,
       subtotal, discount_amount, shipping_fee, total_amount
FROM orders WHERE id=:'order_id';
SELECT order_id,sku_id,product_name,variant_name,unit_price,quantity,line_total
FROM order_items WHERE order_id=:'order_id' ORDER BY id;

-- Q3. Giữ tồn đúng đơn; dùng delta nếu fixture đã có held nền.
SELECT order_id,store_id,sku_id,quantity,status FROM order_reservations
WHERE order_id=:'order_id' ORDER BY id;
SELECT store_id,sku_id,SUM(quantity) AS held_units
FROM order_reservations WHERE sku_id=:'sku_id' AND status='HELD'
GROUP BY store_id,sku_id;

-- Q4. Sau xác minh OTP sai, kiểm tra bằng transaction mới; tuyệt đối không lấy mã OTP.
SELECT id,account_id,failed_attempts,is_used,expiry_time,created_at
FROM password_reset_otps WHERE account_id=:'account_id' ORDER BY created_at DESC;
SELECT id,failed_attempts,is_used,expiry_time,created_at
FROM registration_otps WHERE email=:'qa_email' ORDER BY created_at DESC;

-- Q5. Sau create lỗi hoặc confirm phiếu nhập.
SELECT id,supplier_id,receiving_store_id,status,total_amount
FROM purchase_orders WHERE id=:'po_id';
SELECT purchase_order_id,sku_id,quantity,unit_price
FROM purchase_order_items WHERE purchase_order_id=:'po_id' ORDER BY id;

-- Q6. Trước/sau ship/receive hoặc lỗi dòng sau, kết hợp Q1 cho từng SKU/cửa hàng.
SELECT id,from_store_id,to_store_id,status FROM stock_transfers WHERE id=:'transfer_id';
SELECT stock_transfer_id,sku_id,quantity FROM stock_transfer_items
WHERE stock_transfer_id=:'transfer_id' ORDER BY id;
COMMIT;
