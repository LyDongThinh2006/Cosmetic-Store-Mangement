# Sơ đồ logic LUNEA đã đối chiếu mã nguồn

Cập nhật ngày 20/10/2026 theo entity hiện tại trong workspace. Sơ đồ thể hiện 39 bảng và 59 khóa ngoại; không suy ra cấu trúc từ tên property hoặc từ DTO.

## Tệp kết quả

- `LUNEA_so_do_logic.pdf`: hai sơ đồ vector, phóng to vẫn đọc được chữ.
- `LUNEA_so_do_logic.drawio`: hai trang chỉnh sửa được trong diagrams.net.
- `01_*.svg`, `02_*.svg`: ảnh vector; các tệp PNG tương ứng dùng để chèn Word.
- `../Nhom01_BaoCaoCuoiKy_review_so_do_logic_da_sua.pdf`: bản sao báo cáo đã thay hai hình tại trang PDF 174 và 175.
- `schema_from_entities.json`: bảng, cột và FK lấy từ entity; gồm cả cột thông thường không vẽ trong sơ đồ.

## Hướng mũi tên và bội số

**FK → PK**: đầu mũi tên nằm tại hàng `id` của bảng được tham chiếu. Ví dụ `orders.fulfillment_store_id → stores.id`.

Nét liền: FK không cho NULL. Nét đứt và dấu `?`: FK cho NULL. `UQ`: duy nhất, bao gồm quan hệ được ánh xạ `@OneToOne`. Mỗi FK có nhãn `số con mỗi cha : số cha mỗi con`, ví dụ `0..N : 1` hoặc `0..1 : 1`. Nhãn được đặt cạnh nguồn FK để các quan hệ cùng tham chiếu một PK không chồng chữ.

Khối `[TC]` chỉ lặp lại cùng bảng để thể hiện quan hệ giữa hai nhóm, không phải bảng mới. Đường giao nhau không có nghĩa là kết nối. Sơ đồ chỉ hiển thị PK/FK để đọc được quan hệ; JSON chứa danh sách đầy đủ các cột.

## Những điểm đã sửa so với hình cũ

1. Thêm hướng tham chiếu và cột PK/FK cho mọi đường nối; không dùng đường liên kết vô hướng.
2. Bổ sung `accounts`, `password_reset_otps`, `registration_otps` và `product_attribute_values` theo cài đặt hiện tại. `registration_otps.email` là dữ liệu xác thực trước đăng ký, không phải FK tới `accounts.email`.
3. Không vẽ bảng Voucher hay Chương trình khuyến mãi như bảng đã cài đặt: source chưa có entity tương ứng. Ba mã ưu đãi hiện được xử lý trong service; `voucherCode` trong DTO không tạo FK.
4. `order_reservations` tham chiếu `orders`, `skus`, `stores`; không tham chiếu trực tiếp `inventories`.
5. `return_items.order_item_id` tham chiếu `order_items`; `return_requests` còn tham chiếu khách hàng và nhân viên xử lý.
6. `reviews` có FK tới khách hàng, sản phẩm, đơn và chi tiết đơn; `order_item_id` được ánh xạ `@OneToOne`, còn `order_id` và `order_item_id` cho phép NULL ở mức mapping.
7. Phiếu điều chuyển có hai FK riêng tới cửa hàng: `from_store_id` và `to_store_id`; đồng thời có `employee_id` tới người lập.
8. Thêm quan hệ tự tham chiếu `categories.parent_id → categories.id`; thêm liên kết SKU tùy chọn của `product_images`.
9. Các bảng `employee_roles`, `role_permissions`, `employee_stores` dùng PK `id` theo entity; không gán nhầm khóa chính ghép chỉ vì có các lớp `*Id` chưa được ánh xạ.
10. `orders` lưu snapshot địa chỉ và dữ liệu nhận hàng; không có FK `customer_address_id` trong entity. Giá, tên sản phẩm trong chi tiết đơn cũng là snapshot.

## Phân biệt mapping và quy tắc nghiệp vụ

Sơ đồ bám mapping JPA hiện tại. Không tự bổ sung UNIQUE(store_id, sku_id) cho tồn kho hoặc UNIQUE cho các cặp ở bảng nối vì entity chưa khai báo các ràng buộc đó. Quy tắc một tài khoản chỉ thuộc một loại khách/nhân viên, chống trả vượt số lượng và giữ tồn đúng chi nhánh là quy tắc cần kiểm tra thêm ở tầng nghiệp vụ; không thể biểu diễn đầy đủ bằng riêng các FK.

Bản PDF báo cáo mới chỉ thay hai hình; phần mô tả chi tiết 37 bảng ở các trang sau vẫn là thiết kế cũ và cần đồng bộ riêng nếu báo cáo được chuyển hoàn toàn sang mô tả 39 bảng đã cài đặt.

## Danh sách khóa ngoại đã đối chiếu

| STT | Cột FK (nguồn mũi tên) | PK được tham chiếu (đầu mũi tên) | Số con mỗi cha | Số cha mỗi con | Entity |
|---|---|---|---|---|---|
| 1 | `audit_logs.employee_id` | `employees.id` | 0..N | 1 | `AuditLogEntity.java:29` |
| 2 | `beauty_profiles.customer_id` | `customers.id` | 0..1 | 1 | `BeautyProfileEntity.java:27` |
| 3 | `cart_items.cart_id` | `carts.id` | 0..N | 1 | `CartItemEntity.java:26` |
| 4 | `cart_items.sku_id` | `skus.id` | 0..N | 1 | `CartItemEntity.java:31` |
| 5 | `carts.customer_id` | `customers.id` | 0..1 | 1 | `CartEntity.java:29` |
| 6 | `categories.parent_id` | `categories.id` | 0..N | 0..1 | `CategoryEntity.java:31` |
| 7 | `customer_addresses.customer_id` | `customers.id` | 0..N | 1 | `CustomerAddressEntity.java:26` |
| 8 | `customers.account_id` | `accounts.id` | 0..1 | 1 | `CustomerEntity.java:30` |
| 9 | `employee_roles.employee_id` | `employees.id` | 0..N | 1 | `EmployeeRoleEntity.java:24` |
| 10 | `employee_roles.role_id` | `roles.id` | 0..N | 1 | `EmployeeRoleEntity.java:29` |
| 11 | `employee_stores.employee_id` | `employees.id` | 0..N | 1 | `EmployeeStoreEntity.java:25` |
| 12 | `employee_stores.store_id` | `stores.id` | 0..N | 1 | `EmployeeStoreEntity.java:30` |
| 13 | `employees.account_id` | `accounts.id` | 0..1 | 1 | `EmployeeEntity.java:28` |
| 14 | `inventories.sku_id` | `skus.id` | 0..N | 1 | `InventoryEntity.java:33` |
| 15 | `inventories.store_id` | `stores.id` | 0..N | 1 | `InventoryEntity.java:28` |
| 16 | `inventory_adjustments.adjusted_by` | `employees.id` | 0..N | 1 | `InventoryAdjustmentEntity.java:57` |
| 17 | `inventory_adjustments.sku_id` | `skus.id` | 0..N | 1 | `InventoryAdjustmentEntity.java:37` |
| 18 | `inventory_adjustments.store_id` | `stores.id` | 0..N | 1 | `InventoryAdjustmentEntity.java:32` |
| 19 | `order_items.order_id` | `orders.id` | 0..N | 1 | `OrderItemEntity.java:31` |
| 20 | `order_items.sku_id` | `skus.id` | 0..N | 1 | `OrderItemEntity.java:36` |
| 21 | `order_reservations.order_id` | `orders.id` | 0..N | 1 | `OrderReservationEntity.java:28` |
| 22 | `order_reservations.sku_id` | `skus.id` | 0..N | 1 | `OrderReservationEntity.java:33` |
| 23 | `order_reservations.store_id` | `stores.id` | 0..N | 1 | `OrderReservationEntity.java:38` |
| 24 | `orders.customer_id` | `customers.id` | 0..N | 1 | `OrderEntity.java:42` |
| 25 | `orders.fulfillment_store_id` | `stores.id` | 0..N | 1 | `OrderEntity.java:47` |
| 26 | `password_reset_otps.account_id` | `accounts.id` | 0..N | 1 | `PasswordResetOtpEntity.java:32` |
| 27 | `product_attribute_values.attribute_id` | `attributes.id` | 0..N | 1 | `ProductAttributeValueEntity.java:31` |
| 28 | `product_attribute_values.product_id` | `products.id` | 0..N | 1 | `ProductAttributeValueEntity.java:26` |
| 29 | `product_images.product_id` | `products.id` | 0..N | 1 | `ProductImageEntity.java:27` |
| 30 | `product_images.sku_id` | `skus.id` | 0..N | 0..1 | `ProductImageEntity.java:31` |
| 31 | `products.brand_id` | `brands.id` | 0..N | 1 | `ProductEntity.java:30` |
| 32 | `products.category_id` | `categories.id` | 0..N | 1 | `ProductEntity.java:35` |
| 33 | `purchase_order_items.purchase_order_id` | `purchase_orders.id` | 0..N | 1 | `PurchaseOrderItemEntity.java:29` |
| 34 | `purchase_order_items.sku_id` | `skus.id` | 0..N | 1 | `PurchaseOrderItemEntity.java:34` |
| 35 | `purchase_orders.created_by` | `employees.id` | 0..N | 1 | `PurchaseOrderEntity.java:48` |
| 36 | `purchase_orders.receiving_store_id` | `stores.id` | 0..N | 1 | `PurchaseOrderEntity.java:37` |
| 37 | `purchase_orders.supplier_id` | `suppliers.id` | 0..N | 1 | `PurchaseOrderEntity.java:32` |
| 38 | `return_items.order_item_id` | `order_items.id` | 0..N | 1 | `ReturnItemEntity.java:34` |
| 39 | `return_items.return_request_id` | `return_requests.id` | 0..N | 1 | `ReturnItemEntity.java:29` |
| 40 | `return_requests.customer_id` | `customers.id` | 0..N | 1 | `ReturnRequestEntity.java:40` |
| 41 | `return_requests.handled_by` | `employees.id` | 0..N | 0..1 | `ReturnRequestEntity.java:66` |
| 42 | `return_requests.order_id` | `orders.id` | 0..N | 1 | `ReturnRequestEntity.java:35` |
| 43 | `reviews.customer_id` | `customers.id` | 0..N | 1 | `ReviewEntity.java:34` |
| 44 | `reviews.order_id` | `orders.id` | 0..N | 0..1 | `ReviewEntity.java:43` |
| 45 | `reviews.order_item_id` | `order_items.id` | 0..1 | 0..1 | `ReviewEntity.java:47` |
| 46 | `reviews.product_id` | `products.id` | 0..N | 1 | `ReviewEntity.java:39` |
| 47 | `role_permissions.permission_id` | `permissions.id` | 0..N | 1 | `RolePermissionEntity.java:29` |
| 48 | `role_permissions.role_id` | `roles.id` | 0..N | 1 | `RolePermissionEntity.java:24` |
| 49 | `sku_attribute_values.attribute_id` | `attributes.id` | 0..N | 1 | `SkuAttributeValueEntity.java:31` |
| 50 | `sku_attribute_values.sku_id` | `skus.id` | 0..N | 1 | `SkuAttributeValueEntity.java:26` |
| 51 | `skus.product_id` | `products.id` | 0..N | 1 | `ProductSkuEntity.java:30` |
| 52 | `stock_transfer_items.sku_id` | `skus.id` | 0..N | 1 | `StockTransferItemEntity.java:31` |
| 53 | `stock_transfer_items.stock_transfer_id` | `stock_transfers.id` | 0..N | 1 | `StockTransferItemEntity.java:26` |
| 54 | `stock_transfers.employee_id` | `employees.id` | 0..N | 1 | `StockTransferEntity.java:46` |
| 55 | `stock_transfers.from_store_id` | `stores.id` | 0..N | 1 | `StockTransferEntity.java:31` |
| 56 | `stock_transfers.to_store_id` | `stores.id` | 0..N | 1 | `StockTransferEntity.java:36` |
| 57 | `wishlist_items.product_id` | `products.id` | 0..N | 1 | `WishListItemEntity.java:32` |
| 58 | `wishlist_items.wishlist_id` | `wishlists.id` | 0..N | 1 | `WishListItemEntity.java:27` |
| 59 | `wishlists.customer_id` | `customers.id` | 0..1 | 1 | `WishListEntity.java:28` |

## Tái tạo

```bash
python3 docs/so_do_logic/tao_so_do_logic.py
```

Script đọc lại entity, kiểm tra đủ bảng/FK và đúng hướng source/target, kiểm tra đường nối không xuyên khối bảng, sau đó xuất lại các tệp. Khi schema thay đổi, cần rà soát bố cục và số lượng mong đợi trong script.
