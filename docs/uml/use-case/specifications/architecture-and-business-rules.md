# Kiến trúc và quy tắc nghiệp vụ từ implementation

Tài liệu ghi nhận hành vi trong source hiện tại của các module sản phẩm, thương hiệu/danh mục, cửa hàng, tồn kho, chuyển kho và nhập hàng. Đây là phân tích tĩnh; tài liệu này không khẳng định đã chạy hoặc kiểm thử các luồng nghiệp vụ. Danh sách file đã đọc và chứng cứ từng use case nằm trong [analysis-operations.json](../traceability/analysis-operations.json). ID và trạng thái triển khai cuối cùng được tổng hợp cùng frontend trong bộ đặc tả use case.

## 1. Kiến trúc thực tế

Ứng dụng là một dự án Maven Java 21 dùng Spring Boot 4.1.1. Spring MVC phục vụ cả REST và các trang Thymeleaf; service chứa nghiệp vụ, Spring Data JPA truy cập dữ liệu, MapStruct chuyển Entity/DTO và Jakarta Bean Validation kiểm tra request/entity. Các dependency Cloudinary và JDBC SQL Server trong POM không tự chứng minh có chức năng upload ảnh hoặc tích hợp SQL Server đang hoạt động. Nguồn: [pom.xml](../../../../pom.xml).

| Thành phần | Hành vi được xác minh trong source | Nguồn |
| --- | --- | --- |
| REST | Controller nhận request, gọi service và trả ResponseEntity; các module operations dùng API `/api/...`. | [ProductRestController](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java), [InventoryRestController](../../../../src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java) |
| Trang web | Page controller trả tên view và đưa DTO vào Model; các trang public gồm `/`, `/products`, `/products/{id}`, `/brands`, `/stores`; trang operations cho nhân viên gồm `/admin/products`, `/admin/inventory`. | [HomeController](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java), [ProductPageController](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java), [AdminPageController](../../../../src/main/java/com/thinh/cosmetic/controller/AdminPageController.java) |
| Nghiệp vụ | Các service implementation gắn `@Transactional`; truy vấn đọc thường gắn `readOnly=true`. | [ProductServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java), [StockTransferServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java) |
| Dữ liệu | Repository mở rộng JpaRepository; có finder theo store/SKU, phiếu và query JPQL tồn thấp. | [InventoryRepository](../../../../src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java), [PurchaseOrderItemRepository](../../../../src/main/java/com/thinh/cosmetic/repository/purchase/PurchaseOrderItemRepository.java) |
| Mapping | ProductMapper bỏ qua brand/category khi map; service tìm và gán quan hệ. CategoryMapper bỏ qua parent khi tạo và sửa; chỉ service create gán parent. | [ProductMapper](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java), [CategoryMapper](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java) |
| Cấu hình DB | Profile local dùng PostgreSQL tại cổng 5433 và `ddl-auto=update`; prod lấy kết nối từ biến môi trường và dùng `ddl-auto=validate`. Test dùng H2 trong bộ nhớ với `create-drop`. Đây là cấu hình, không phải xác nhận kết nối runtime thành công. | [application-local.properties](../../../../src/main/resources/application-local.properties), [application-prod.properties](../../../../src/main/resources/application-prod.properties), [cấu hình test](../../../../src/test/resources/application.properties) |
| Bảo mật | JWT HS256, session STATELESS, BCrypt; bật method security. Các controller/service operations đã đọc không có `@PreAuthorize`. | [SecurityConfig](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java) |

Giao diện các luồng trên được triển khai qua Spring MVC/Thymeleaf; không lấy giả định React từ mô tả ban đầu làm kiến trúc hiện hành. Thông tin công nghệ này dựa trên POM, cấu hình Thymeleaf và implementation page controller.

## 2. Module và mức hành vi đã có

| Module | Hành vi thật trong implementation | Phần không được xem là đã có |
| --- | --- | --- |
| Sản phẩm | Xem danh sách/chi tiết; enrich SKU, ảnh, thuộc tính và stock; tìm/lọc/sắp xếp trong bộ nhớ; thêm/sửa/xóa vật lý thông tin sản phẩm. | ProductSkuRequest/Entity/Repository chưa tạo thành CRUD SKU; không có API quản lý ảnh/thuộc tính. |
| Thương hiệu, danh mục | CRUD thương hiệu; CRUD danh mục, gán danh mục cha khi tạo. | Chuyển/cắt quan hệ cha khi sửa; cây danh mục đầy đủ; lọc sản phẩm theo toàn bộ descendants. |
| Cửa hàng | Danh sách/chi tiết; tạo/sửa; DELETE chuyển trạng thái INACTIVE. | Lưu trường operatingHours dạng chuỗi; tự dừng các nghiệp vụ liên quan khi INACTIVE. |
| Tồn kho | Tồn theo store/SKU, số lượng khả dụng, cảnh báo tồn thấp; điều chỉnh và ghi lịch sử; public stock theo SKU/cửa hàng. | Kiểm kê nhiều bước, thay ngưỡng tối thiểu, API xem lịch sử điều chỉnh, phân quyền phạm vi store tại inventory service. |
| Chuyển kho | Tạo PENDING; xuất nguồn chuyển IN_TRANSIT; nhận đích chuyển RECEIVED. | Phê duyệt riêng, PENDING_EXPORT đang hoạt động, hủy, nhận từng phần hoặc completion riêng. |
| Nhà cung cấp, nhập hàng | Supplier CRUD với soft deactivate; PO DRAFT; confirm đồng thời nhập toàn bộ stock và chuyển CONFIRMED. | Phê duyệt/receipt tách biệt, phiếu nhận hàng riêng, sửa/hủy PO hoặc nhận từng phần. |

Nguồn nghiệp vụ chính: [ProductServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java), [CategoryServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java), [StoreServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java), [InventoryServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java), [StockTransferServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java), [PurchaseOrderServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java).

## 3. Entity, bảng và quan hệ operations

Ký hiệu N:1 dưới đây tương ứng `@ManyToOne`; “tùy chọn” nghĩa quan hệ không khai báo bắt buộc. Các quan hệ chỉ chứng minh mô hình dữ liệu, không tự chứng minh có use case quản lý tương ứng.

| Entity / bảng | Quan hệ và ràng buộc chính |
| --- | --- |
| [AttributeEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/catalog/AttributeEntity.java) / `attributes` | Tên bắt buộc ≤120, group bắt buộc ≤30; không quan hệ tới actor. |
| [BrandEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/catalog/BrandEntity.java) / `brands` | Tên unique, status bắt buộc; Entity khai báo mặc định ACTIVE. |
| [CategoryEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/catalog/CategoryEntity.java) / `categories` | N:1 tự tham chiếu `parent`, tùy chọn; không có tập children trong Entity. |
| [ProductEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/catalog/ProductEntity.java) / `products` | N:1 bắt buộc tới Brand và Category; featured/status bắt buộc; createdAt tự sinh. |
| [ProductSkuEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/catalog/ProductSkuEntity.java) / `skus` | N:1 bắt buộc tới Product; skuCode unique, barcode unique tùy chọn; price ≥0 bắt buộc, listPrice ≥0 tùy chọn. |
| [ProductImageEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/catalog/ProductImageEntity.java) / `product_images` | N:1 bắt buộc tới Product; N:1 tùy chọn tới ProductSku; URL bắt buộc; isPrimary và sortOrder bắt buộc. |
| [ProductAttributeValueEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/catalog/ProductAttributeValueEntity.java) / `product_attribute_values` | N:1 bắt buộc tới Product và Attribute; value bắt buộc ≤100. |
| [SkuAttributeValueEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/catalog/SkuAttributeValueEntity.java) / `sku_attribute_values` | N:1 bắt buộc tới SKU và Attribute; value bắt buộc ≤255. |
| [StoreEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/StoreEntity.java) / `stores` | Tên, địa chỉ, tỉnh, status bắt buộc; openTime/closeTime, tọa độ tùy chọn; không có operatingHours. |
| [InventoryEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/InventoryEntity.java) / `inventories` | N:1 bắt buộc tới Store và SKU; on_hand_qty, reserved_qty, low_stock_threshold ≥0; mặc định 0/0/5. Entity không khai báo uniqueConstraint store×SKU hoặc @Version. |
| [InventoryAdjustmentEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/InventoryAdjustmentEntity.java) / `inventory_adjustments` | N:1 bắt buộc tới Store, SKU, Employee; before/after ≥0, reason bắt buộc; adjustedAt tự sinh. |
| [StockTransferEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/StockTransferEntity.java) / `stock_transfers` | N:1 bắt buộc tới Store nguồn, Store đích và Employee tạo; 1:N items với cascade ALL; status bắt buộc, createdAt tự sinh. |
| [StockTransferItemEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/StockTransferItemEntity.java) / `stock_transfer_items` | N:1 bắt buộc tới StockTransfer và SKU; quantity ≥1. |
| [SupplierEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/SupplierEntity.java) / `suppliers` | Tên/status bắt buộc; liên lạc và email có validation; Entity không khai báo tên unique. |
| [PurchaseOrderEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/PurchaseOrderEntity.java) / `purchase_orders` | N:1 bắt buộc tới Supplier, Store nhận, Employee tạo; 1:N items cascade ALL; totalAmount ≥0; DRAFT mặc định; createdAt tự sinh. |
| [PurchaseOrderItemEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/PurchaseOrderItemEntity.java) / `purchase_order_items` | N:1 bắt buộc tới PurchaseOrder và SKU; quantity ≥1, unit_cost ≥0, line_total ≥0. |

Các collection items của phiếu chuyển/nhập có cascade ALL nhưng không có orphanRemoval. ProductEntity không khai báo cascade dọn SKU, ảnh hoặc thuộc tính khi xóa. Những điểm này được ghi nhận từ Entity; không khẳng định schema DB triển khai bên ngoài repository chỉ có các ràng buộc này.

## 4. Phạm vi quyền truy cập được thực thi

| Đường dẫn | Kiểm tra thực tế | Phạm vi dữ liệu |
| --- | --- | --- |
| `/api/products/**`, `/api/brands/**`, `/api/categories/**` | Rơi vào permitAll, gồm POST/PUT/DELETE; không có @PreAuthorize. | Không kiểm tra quyền quản trị hoặc store. |
| `/api/stores/**`, `/api/suppliers/**`, `/api/stock-transfers/**`, `/api/purchase-orders/**` | Rơi vào permitAll, gồm mutation; không có @PreAuthorize. | Không kiểm tra StoreScope. |
| `/api/inventory/**` | Yêu cầu ROLE_EMPLOYEE tại SecurityConfig. | InventoryServiceImpl không gọi StoreScope; storeId do request quyết định. |
| `/admin/**` | Yêu cầu ROLE_EMPLOYEE. | AdminPageController lọc accessibleStores để dựng UI. Việc lọc UI không tạo kiểm tra store tại API inventory. |

[StoreScope](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java) trả toàn bộ cửa hàng nếu principal có ALL_STORES, ROLE_ADMIN hoặc ADMIN. Nhân viên khác lấy IDs từ EmployeeStoreRepository; principal thiếu employeeId hoặc authorities nhận tập rỗng. Đây là quy tắc scope, không phải chứng cứ role cấp cao được thừa hưởng mọi permission.

[XRequestedWithFilter](../../../../src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java) yêu cầu header `X-Requested-With: fetch` hoặc `XMLHttpRequest` cho POST/PUT/PATCH/DELETE `/api/**` khi request không dùng Bearer header. Header này không phải xác thực hay permission; người chưa đăng nhập vẫn có thể gọi các API permitAll bằng header phù hợp. Nguồn tổng hợp: [SecurityConfig](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java), [AdminPageController](../../../../src/main/java/com/thinh/cosmetic/controller/AdminPageController.java).

## 5. Quy tắc danh mục sản phẩm

### Đọc và enrich

`getAll` và `getById` bổ sung SKU, ảnh, minPrice/maxPrice/listPrice, tổng tồn khả dụng, skinTypes và concerns. Ảnh hiển thị ưu tiên ảnh isPrimary, tiếp theo ảnh đầu theo sortOrder, cuối cùng URL fallback Unsplash. Giá min/max lấy trên tất cả SKU có giá; không có SKU thì bằng 0. Tổng stock cộng mọi inventory của mọi SKU, không lọc trạng thái cửa hàng/SKU/sản phẩm.

Thuộc tính được chuyển thành tên Attribute: group SKIN_TYPE vào skinTypes; các nhóm còn lại vào concerns, kể cả NEED. Trường value của ProductAttributeValue không tham gia xác định loại da/nhu cầu. Nguồn: [ProductServiceImpl.enrich](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java).

### Tìm, lọc và sắp xếp

Hệ thống enrich toàn bộ danh sách trước rồi lọc trong bộ nhớ. Từ khóa được so khớp có/không dấu trên tên sản phẩm, thương hiệu, danh mục, mô tả, nguyên liệu, công dụng, xuất xứ, SKU code và tên biến thể. brandId/categoryId phải khớp ID chính xác; lọc category không mở rộng xuống danh mục con. skinType/concern so khớp tên thuộc tính theo chứa chuỗi, có chuẩn hóa dấu.

| priceRange đầu vào | Điều kiện theo minPrice |
| --- | --- |
| under300 hoặc under500 | Giá < 500.000 |
| 300to600 hoặc 500to1000 | 500.000 ≤ giá ≤ 1.000.000 |
| above1000 | Giá > 1.000.000 |
| Giá trị khác hoặc rỗng | Không lọc giá |

sortBy hỗ trợ price-asc, price-desc, featured; trường hợp khác dùng ID giảm dần. Page controller nhận page nhưng không phân trang kết quả. Chi tiết sản phẩm gợi ý tối đa bốn sản phẩm khác cùng brand hoặc category; đây là quy tắc liên quan đơn giản. Nguồn: [ProductServiceImpl.searchAndFilter](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java), [ProductPageController](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java).

### Ghi và trạng thái

ProductRequest bắt buộc brandId, categoryId, tên không trống ≤200, origin ≤100. Service tìm brand/category rồi gán vào Entity. Request không có SKU, ảnh hoặc attribute values; tạo sản phẩm không đồng thời tạo biến thể có giá hoặc tồn kho. Update trả DTO cơ bản qua ProductMapper, không gọi enrich.

Brand/category update bỏ qua null. ProductMapper.updateEntity không đặt NullValuePropertyMappingStrategy.IGNORE; các trường optional null có thể làm mất giá trị cũ. Category.create xử lý parentId nhưng Category.update không xử lý parentId, trong khi mapper bỏ qua parent ở cả hai thao tác. DELETE product/brand/category gọi xóa vật lý, có thể bị dữ liệu tham chiếu ngăn xóa. status=INACTIVE là trường được sửa qua update, không phải endpoint deactivate riêng. Nguồn: [ProductRequest](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/catalog/ProductRequest.java), [ProductMapper](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java), [CategoryServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java), [CategoryMapper](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java).

## 6. Tồn kho và điều chỉnh

`availableStock = max(0, actualStock - heldQuantity)`.

`totalAvailableStock(product) = Σ availableStock` của mọi bản ghi inventory thuộc mọi SKU của sản phẩm.

Cảnh báo tồn thấp được query bằng `actualStock <= minimumStock`, bao gồm trường hợp bằng ngưỡng; query không sử dụng availableStock. Không có API operations sửa minimumStock trong implementation đã đọc. Nguồn: [InventoryServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java), [InventoryRepository.findLowStock](../../../../src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java).

Điều chỉnh tồn là một hành động với các bước nội bộ:

1. Tìm inventory theo storeId/SKU; không có thì thất bại.
2. Kiểm tra newQuantity ≥ heldQuantity. DTO còn yêu cầu số lượng không null, ≥0 và reason không trống, ≤255.
3. Ghi InventoryAdjustment với tồn trước/sau, lý do, Employee thực hiện và thời gian tự sinh.
4. Đặt actualStock=newQuantity; giữ nguyên heldQuantity; lưu và trả stock mới.

Controller ưu tiên employeeId từ principal, thiếu thì dùng query employeeId hoặc mặc định 1. Service lookup Employee có thể trả null, trong khi Entity yêu cầu performedBy bắt buộc. Không có endpoint xem lịch sử adjustment dù lịch sử được lưu. Nguồn: [InventoryRestController.adjustStock](../../../../src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java), [InventoryServiceImpl.adjustStock](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java), [InventoryAdjustmentRequest](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/store/InventoryAdjustmentRequest.java).

Public stock theo SKU trả mọi cửa hàng, gồm actualStock và heldQuantity. Không có inventory tại một cửa hàng thì các lượng bằng 0. Không kiểm tra SKU có tồn tại; ID SKU sai cũng có thể trả tất cả cửa hàng với stock 0. Không lọc cửa hàng INACTIVE.

## 7. Vòng đời chuyển kho

| Trạng thái trước | Mục tiêu người dùng / API | Điều kiện và thay đổi | Trạng thái sau |
| --- | --- | --- | --- |
| Chưa có phiếu | Lập phiếu chuyển — POST /api/stock-transfers | Tìm Store nguồn/đích và SKU; lưu Employee từ employeeId query, mặc định 1; lưu items. Không trừ hoặc giữ tồn. | PENDING |
| PENDING | Xác nhận xuất — PUT /api/stock-transfers/{id}/ship | Mỗi dòng phải có inventory nguồn và actualStock ≥ quantity; trừ quantity khỏi actualStock; ghi shippedAt. | IN_TRANSIT |
| IN_TRANSIT | Xác nhận nhận — PUT /api/stock-transfers/{id}/receive | Cộng toàn bộ quantity vào inventory đích; thiếu inventory thì tạo actual=0, held=0, minimum=5 trước khi cộng; ghi receivedAt. | RECEIVED |

Nguồn: [StockTransferRestController](../../../../src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java), [StockTransferServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java).

TransferStatus còn có PENDING_EXPORT nhưng service không dùng trạng thái này. Không có endpoint approval/completion riêng, hủy hoặc nhận thiếu. Nguồn: [TransferStatus](../../../../src/main/java/com/thinh/cosmetic/domain/enums/TransferStatus.java).

StockTransferRequest yêu cầu source/destination không null và items không rỗng. Các nested item có @NotNull SKU/quantity và @Min(1), nhưng list không gắn @Valid nên validation DTO các dòng chưa cascade từ request. Entity item vẫn có constraints khi persistence. Service không cấm nguồn bằng đích, SKU lặp hoặc store/SKU INACTIVE. Ship kiểm tra actualStock mà không trừ heldQuantity, có thể xuất phần đang giữ cho khách. Nguồn: [StockTransferRequest](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/store/StockTransferRequest.java), [StockTransferServiceImpl.confirmShipment](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java).

## 8. Nhà cung cấp và vòng đời nhập hàng

Supplier create mặc định ACTIVE nếu status null; update bỏ qua null; DELETE chuyển INACTIVE. Tên bắt buộc ≤200, người liên hệ ≤150, địa chỉ ≤255, phone ≤20, email hợp lệ ≤150. Repository có existsByName nhưng service không gọi và Entity không unique name. Nguồn: [SupplierServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java), [SupplierRequest](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/purchase/SupplierRequest.java), [SupplierEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/SupplierEntity.java).

| Trạng thái trước | Mục tiêu người dùng / API | Điều kiện và thay đổi | Trạng thái sau |
| --- | --- | --- | --- |
| Chưa có PO | Lập đơn nhập — POST /api/purchase-orders | Tìm Supplier/Store nhận/SKU; Employee từ query mặc định 1; lineTotal=unitPrice×quantity; totalAmount=ΣlineTotal; lưu items. Chưa tăng stock. | DRAFT |
| DRAFT | Xác nhận nhập kho — PUT /api/purchase-orders/{id}/confirm | Cộng toàn bộ quantity từng dòng vào inventory của Store nhận; thiếu inventory tạo actual=0, held=0, minimum=5 trước khi cộng; ghi confirmedAt. | CONFIRMED |

Confirm vừa đổi trạng thái vừa tăng stock, nên đặc tả gọi mục tiêu này là “Xác nhận nhập hàng vào kho”. Không có hai hành động approve và receive độc lập; ReceiptStatus có enum nhưng không có entity/service/REST phiếu nhận riêng trong code operations. Nguồn: [PurchaseOrderRestController](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java), [PurchaseOrderServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java), [ReceiptStatus](../../../../src/main/java/com/thinh/cosmetic/domain/enums/ReceiptStatus.java).

PurchaseOrderRequest yêu cầu supplierId/receivingStoreId không null, items không rỗng. Nested item yêu cầu SKU/quantity/unitPrice không null, quantity ≥1 và unitPrice ≥0 nhưng items thiếu @Valid. Entity có constraints tương ứng; phép nhân giá/số lượng diễn ra trước khi lưu item, nên giá trị null có thể gây lỗi trong logic trước entity validation. Không có sửa/hủy PO, nhận từng phần, kiểm tra supplier/store/SKU ACTIVE hoặc đối chiếu giá nhập theo supplier. Nguồn: [PurchaseOrderRequest](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/purchase/PurchaseOrderRequest.java), [PurchaseOrderServiceImpl.create](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java).

## 9. Repository và truy vấn quan trọng

| Repository / method | Cách dùng thực tế |
| --- | --- |
| [ProductRepository](../../../../src/main/java/com/thinh/cosmetic/repository/catalog/ProductRepository.java): findByCategoryId, findByBrandId, findByIsFeaturedTrue, findByNameContainingIgnoreCase | Được định nghĩa nhưng ProductService.searchAndFilter không gọi; service đọc findAll và lọc trong bộ nhớ. |
| [ProductSkuRepository](../../../../src/main/java/com/thinh/cosmetic/repository/catalog/ProductSkuRepository.java): findByProductId | Enrich tất cả SKU của sản phẩm; không lọc status. findBySkuCode chưa được operations service dùng. |
| [ProductImageRepository](../../../../src/main/java/com/thinh/cosmetic/repository/catalog/ProductImageRepository.java): findByProductIdAndIsPrimaryTrue, findByProductIdOrderBySortOrderAsc | Chọn ảnh chính và danh sách ảnh theo thứ tự cho enrich. |
| [ProductAttributeValueRepository](../../../../src/main/java/com/thinh/cosmetic/repository/catalog/ProductAttributeValueRepository.java): findByProductId | Đọc tên/group Attribute cho loại da và nhu cầu. |
| [InventoryRepository](../../../../src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java): findByStoreId, findByStoreIdAndSkuId, findBySkuId | Phục vụ tra kho, công thức available/product total, adjustment, transfer và PO confirm. |
| InventoryRepository: findLowStock | JPQL chọn store và actualStock ≤ minimumStock. |
| [StockTransferItemRepository](../../../../src/main/java/com/thinh/cosmetic/repository/store/StockTransferItemRepository.java): findByStockTransferId | Đọc dòng hàng khi ship/receive và fallback response. |
| [StockTransferRepository](../../../../src/main/java/com/thinh/cosmetic/repository/store/StockTransferRepository.java): findBySourceStoreIdOrDestinationStoreId | Có finder nhưng service.getAll dùng findAll; không có lọc store ở endpoint. |
| [PurchaseOrderItemRepository](../../../../src/main/java/com/thinh/cosmetic/repository/purchase/PurchaseOrderItemRepository.java): findByPurchaseOrderId | Đọc items cho confirm và fallback response. |
| [PurchaseOrderRepository](../../../../src/main/java/com/thinh/cosmetic/repository/purchase/PurchaseOrderRepository.java): findBySupplierId, findByReceivingStoreId | Định nghĩa finder nhưng getAll không dùng; API không nhận filter tương ứng. |
| [SupplierRepository](../../../../src/main/java/com/thinh/cosmetic/repository/purchase/SupplierRepository.java): existsByName | Định nghĩa finder nhưng create/update không gọi. |
| [EmployeeStoreRepository](../../../../src/main/java/com/thinh/cosmetic/repository/account/EmployeeStoreRepository.java): findByEmployeeId | StoreScope dùng để dựng quyền cửa hàng; inventory/transfer/PO service không gọi StoreScope. |

## 10. Giới hạn và khoảng trống ảnh hưởng đặc tả

| Khoảng trống | Hệ quả cần phản ánh trong use case |
| --- | --- |
| Mutation operations permitAll, thiếu permission và StoreScope | Association cho người gọi API công khai phản ánh truy cập thật. Không tự gán Warehouse Staff/Branch Manager từ tài liệu thiết kế. Inventory chỉ chứng minh ROLE_EMPLOYEE. |
| CRUD SKU/ảnh/thuộc tính chưa có | Entity/DTO/repository hoặc dữ liệu seed không đủ để đánh dấu chức năng quản lý hoàn chỉnh. |
| parentId bị bỏ qua khi update category | Cập nhật danh mục chỉ có tên/mô tả/status; thay cha chưa hoàn thiện. |
| Dữ liệu INACTIVE vẫn nằm trong getAll/enrich/stock | Deactivate không đồng nghĩa tự ẩn khỏi mọi luồng công khai hoặc chặn transfer/PO. |
| createdBy transfer/PO nhận employeeId query | Không thể xem người tạo phiếu đã được ràng buộc chắc với principal. |
| Ship dùng actualStock thay availableStock | Có khả năng actualStock sau xuất thấp hơn heldQuantity. |
| Tạo/lưu từng dòng trước khi có thể throw checked Exception | @Transactional không khai báo rollbackFor cho checked exceptions; theo cơ chế rollback mặc định, có rủi ro commit một phần khi dòng sau không tìm thấy hoặc thiếu tồn. Đây là suy luận từ code, chưa kiểm chứng bằng test runtime riêng. |
| Không @Version hoặc repository locking | Chưa có chứng cứ bảo đảm cập nhật stock/ship/confirm an toàn khi cạnh tranh. |
| Entity Inventory không khai báo unique store×SKU | Không suy ra DB ngoài repository đã bảo đảm một inventory cho mỗi cặp store/SKU; public stock chọn bản ghi đầu nếu dữ liệu trùng. |
| Nested items thiếu @Valid | Không khẳng định request validation kiểm tra đầy đủ từng dòng; Entity vẫn có validation ở bước lưu. |
| Chỉ adjustment ghi InventoryAdjustment | Không có UC xem lịch sử hoàn chỉnh hoặc chứng cứ mọi chuyển/nhập kho đều ghi adjustment audit. |
| operatingHours không có field Entity; product update không enrich | String giờ hoạt động không được lưu; response update sản phẩm thiếu dữ liệu enrich đến khi đọc lại. |
| Not-found/wrong-state dùng checked Exception chung | GlobalExceptionHandler thường trả HTTP 500; không tự ghi 404/409 cho các nhánh nghiệp vụ này trong đặc tả. |

Nguồn lỗi và HTTP: [GlobalExceptionHandler](../../../../src/main/java/com/thinh/cosmetic/exception/GlobalExceptionHandler.java). Chứng cứ cụ thể của từng khoảng trống được ghi tại trường gaps trong [analysis-operations.json](../traceability/analysis-operations.json).

Các thao tác repository, mapping, tính stock, ghi audit bên trong adjustment là bước nội bộ. Tính tuần tự của trạng thái phiếu không tự tạo quan hệ include/extend; đăng nhập được biểu diễn bằng precondition cho API cần xác thực. Phần operations chưa có đủ chứng cứ để khai báo actor generalization hoặc include/extend giữa các CRUD.

## 11. Phạm vi chứng cứ kiểm thử

Đã đọc [InventoryServiceTest](../../../../src/test/java/com/thinh/cosmetic/service/InventoryServiceTest.java), [BrandTests](../../../../src/test/java/com/thinh/cosmetic/BrandTests.java), [Phase1RestEndpointsTest](../../../../src/test/java/com/thinh/cosmetic/controller/Phase1RestEndpointsTest.java) và [WebPageControllersTest](../../../../src/test/java/com/thinh/cosmetic/controller/WebPageControllersTest.java). Nội dung test bổ sung chứng cứ về công thức available, việc gọi lưu audit adjustment, public stock và page routing; các ID UC trong tên test không được dùng làm ID của bộ tài liệu mới. Việc đọc test không chứng minh test đã pass trong phiên phân tích này. Các rủi ro transaction/cạnh tranh nêu trên chưa được xác nhận bằng test riêng.



## Tài khoản, giỏ hàng và đơn hàng — đối chiếu bổ sung

Tài khoản có hai AccountType CUSTOMER/EMPLOYEE, trạng thái ACTIVE/INACTIVE. Account liên kết một-một với Customer hoặc Employee; BeautyProfile một-một với Customer, CustomerAddress nhiều-một Customer. EmployeeRole/RolePermission/EmployeeStore là entity liên kết. Các helper *Id không được entity hiện tại dùng làm composite key. AuditLogEntity có model/repository nhưng chưa có workflow ghi hoặc xem nhật ký.

Đăng ký chuẩn hóa email/SĐT, băm BCrypt, tạo customer với điểm 0; không email/OTP. Đăng nhập nạp role/permission và phát JWT HS256 cookie HttpOnly LUNEA_AT; authorities không cập nhật tự động khi role đổi. Đăng xuất xóa cookie, không có server-side JWT revocation. AppUserDetailsService kiểm Account.status, không kiểm Employee.status. [AccountServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java), [AppUserDetailsService](../../../../src/main/java/com/thinh/cosmetic/security/AppUserDetailsService.java), [JwtService](../../../../src/main/java/com/thinh/cosmetic/security/JwtService.java).

Cart/WishList liên kết Customer; CartItem liên kết SKU, WishListItem liên kết Product. Thêm giỏ cộng số lượng SKU đã có và kiểm tổng khả dụng toàn chuỗi; giỏ không giữ tồn. GET cart/wishlist có thể tạo container. Cart sửa/xóa chưa kiểm item thuộc cart hiện tại. Wishlist chỉ có get/add/remove, không có move-to-cart. [CartServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java), [WishListServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java).

Luồng đặt hàng hiện thực: đọc toàn bộ giỏ → xác định địa chỉ → tính giá SKU hiện tại/phí/ưu đãi → chọn cửa hàng → lưu Order PENDING và OrderItem snapshot → lưu OrderReservation HELD và tăng held → xóa cart items. Không bắt buộc gọi preview trước place. Store được chọn qua request không được kiểm đủ tồn; auto selection có nhánh fallback store đầu dù không đủ. Chưa có khóa hoặc kiểm lại để chống đặt hàng cạnh tranh. deliveryMethod request bị bỏ qua và OrderEntity mặc định HOME_DELIVERY; PaymentMethod chỉ có COD. Không có API nhân viên gán lại store. [OrderServiceImpl.placeOrder](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L63), [OrderRequest](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/order/OrderRequest.java).

Phí ship hardcode 30.000 khi subtotal dưới 500.000, từ ngưỡng đó là 0. LUNEA10 giảm 10% tối đa 100.000; GIAM50K giảm 50.000; FREESHIP phí ship 0. Total=max(0, subtotal-discount+shipping). Mã lạ không bị từ chối, giảm 0. Đây là nhánh code hardcode ở preview/place; không có voucher table/quota/expiry/usage/hoàn lượt. AppProperties.shipping có default tương tự nhưng OrderServiceImpl dùng literal, không đọc properties này.

Khách chỉ được hủy đơn PENDING và phải sở hữu đơn. Nhân viên đọc/sửa đơn theo StoreScope; ADMIN/ROLE_ADMIN/ALL_STORES dùng mọi store, employee khác dựa EmployeeStore. Khi đích CONFIRMED ghi confirmedAt. SHIPPING hoặc COMPLETED đổi reservation HELD→COMMITTED, giảm actual và held; COMPLETED ghi completedAt. CANCELLED đổi HELD→RELEASED và giảm held. Không hoàn actual của COMMITTED. Service chỉ chặn sửa terminal CANCELLED/COMPLETED, cùng status idempotent; các non-terminal có thể nhảy/lùi, khác nút UI giới hạn theo bước. [OrderServiceImpl.updateStatus](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L393), [StoreScope](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java).

ReturnRequest liên kết Order/Customer/Employee xử lý, các ReturnItem liên kết OrderItem. create không gán Customer bắt buộc, nên có defect persistence thật dù mock test trả success. updateStatus chỉ đổi trạng thái/người/thời điểm, không refund/restock, không kiểm luồng trạng thái, ownership hoặc quantity bound. Review liên kết Product/Customer và tùy chọn Order/OrderItem; create lưu VISIBLE ngay, không xác minh đơn đã mua/hoàn tất, không chống duplicate. UI review tab tĩnh; chưa gọi review API. [ReturnServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java), [ReturnRequestEntity](../../../../src/main/java/com/thinh/cosmetic/domain/entity/returns/ReturnRequestEntity.java), [ReviewServiceImpl](../../../../src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java).

OrderRepository truy vấn theo customerId và assignedStoreId(s), sắp createdAt giảm dần; CartItemRepository tìm cặp cartId/skuId và xóa theo cartId; OrderReservationRepository lấy reservation theo orderId; ReturnRequestRepository lọc customer/order; ReviewRepository chọn product+moderation status. Không xem các repository methods này là use case độc lập. [Commerce analysis và queries](../traceability/analysis-commerce.json), [Entity inventory 37 bảng](entity-inventory.md).
