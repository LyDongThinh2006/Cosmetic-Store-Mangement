# Danh mục module và Use Case

ID/tên UC đối chiếu báo cáo Nhom01_BaoCaoCuoiKy_review.pdf, trang PDF 48–61. IMPLEMENTED là có luồng source, PARTIAL là có phần chức năng cùng giới hạn, NOT IMPLEMENTED là chưa có luồng yêu cầu. Không trạng thái nào tương đương PASS. Entity/DTO/enum không đủ chứng minh một chức năng đã triển khai.

| Nhóm module | UC gốc | Phần có luồng source | Nguồn đối chiếu |
| --- | --- | --- | --- |
| Xác thực và OTP | UC01, UC02, UC03, UC09 | UC01; UC02; UC03; UC09 | src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java; src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java; src/main/java/com/thinh/cosmetic/service/account/impl/OtpServiceImpl.java; src/main/java/com/thinh/cosmetic/config/SecurityConfig.java; src/main/java/com/thinh/cosmetic/security/JwtAuthConverter.java |
| Khách hàng | UC10, UC11, UC12, UC58, UC59, UC61, UC91 | UC10; UC11; UC12; UC58; UC59 | src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java; src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java |
| Yêu thích | UC14 | UC14 | src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java; src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java |
| Danh mục sản phẩm | UC04, UC05, UC06, UC07, UC08, UC13, UC29, UC30, UC31, UC32, UC33, UC34, UC35, UC36, UC41 | UC04; UC05; UC06; UC07; UC08; UC13; UC29; UC30; UC31; UC32; UC33; UC41 | src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java; src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java; src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java; src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java |
| Giỏ hàng | UC15, UC16, UC17, UC18 | UC15; UC16; UC17; UC18 | src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java; src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java |
| Đặt hàng và giữ tồn | UC19, UC20, UC21, UC54, UC55, UC68, UC71, UC78, UC79 | UC19; UC20; UC21; UC54; UC55; UC68; UC71; UC78; UC79 | src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java; src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java; src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java; src/main/java/com/thinh/cosmetic/domain/entity/inventory/InventoryEntity.java |
| Vòng đời đơn hàng | UC22, UC23, UC24, UC25, UC60, UC65, UC66, UC67, UC69, UC70, UC72, UC73, UC75 | UC22; UC23; UC24; UC25; UC60; UC65; UC66; UC67; UC69; UC70; UC73 | src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java; src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java; src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java; src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java |
| Trả hàng | UC26, UC62, UC63, UC74, UC64 | UC26; UC62; UC63; UC74 | src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java; src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java |
| Đánh giá sản phẩm | UC27, UC28, UC37 | UC27; UC28; UC37 | src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java; src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java |
| Chi nhánh | UC38, UC39, UC40, UC42 | UC38; UC39; UC40; UC42 | src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java; src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java |
| Nhập hàng | UC43, UC44, UC45, UC46, UC47, UC48, UC50 | UC43; UC44; UC45; UC46; UC47; UC48; UC50 | src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java; src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java; src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java |
| Tồn kho | UC49, UC51, UC52, UC56, UC57 | UC49; UC51; UC56 | src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java; src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java; src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java |
| Điều chuyển | UC53 | UC53 | src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java; src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java |
| Nhân viên và vai trò | UC82, UC83, UC84, UC85, UC86, UC87 | UC82; UC83; UC84; UC85; UC86 | src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java; src/main/java/com/thinh/cosmetic/rest/account/RoleRestController.java; src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java; src/main/java/com/thinh/cosmetic/security/StoreScope.java |
| Khuyến mãi chưa có module quản lý | UC76, UC77, UC80, UC81 | Chưa có module quản lý | src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java; src/main/java/com/thinh/cosmetic/domain/enums/VoucherType.java |
| Tổng quan quản trị | UC88, UC89, UC90 | UC88; UC89 | src/main/java/com/thinh/cosmetic/controller/AdminPageController.java |

## Toàn bộ 91 Use Case

| UC | Chức năng | Module | Mức triển khai | Giới hạn/lý do |
| --- | --- | --- | --- | --- |
| UC01 | Đăng nhập | Xác thực và OTP | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC02 | Đăng xuất | Xác thực và OTP | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC03 | Khôi phục mật khẩu | Xác thực và OTP | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC04 | Xem danh sách sản phẩm | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC05 | Tìm kiếm sản phẩm | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC06 | Lọc và sắp xếp sản phẩm | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC07 | Xem chi tiết sản phẩm | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC08 | Xem tình trạng sản phẩm tại cửa hàng | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC09 | Đăng ký tài khoản | Xác thực và OTP | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC10 | Cập nhật hồ sơ cá nhân | Khách hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC11 | Quản lý địa chỉ nhận hàng | Khách hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC12 | Quản lý Beauty Profile | Khách hàng | PARTIAL | Có GET/PUT Beauty Profile qua API; chưa có form UI riêng. |
| UC13 | Xem sản phẩm gợi ý | Danh mục sản phẩm | PARTIAL | Có sản phẩm liên quan category/brand; chưa gợi ý cá nhân hóa từ Beauty Profile. |
| UC14 | Quản lý danh sách yêu thích | Yêu thích | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC15 | Xem giỏ hàng | Giỏ hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC16 | Thêm sản phẩm vào giỏ hàng | Giỏ hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC17 | Cập nhật số lượng sản phẩm trong giỏ | Giỏ hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC18 | Xóa sản phẩm khỏi giỏ hàng | Giỏ hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC19 | Áp dụng voucher | Đặt hàng và giữ tồn | PARTIAL | Chỉ áp dụng ba mã cố định trong mã nguồn; không có vòng đời voucher. |
| UC20 | Tính tổng giá trị đơn hàng | Đặt hàng và giữ tồn | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC21 | Đặt hàng | Đặt hàng và giữ tồn | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC22 | Xem lịch sử đơn hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC23 | Xem chi tiết đơn hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC24 | Theo dõi trạng thái đơn hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC25 | Hủy đơn hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC26 | Gửi yêu cầu trả hàng | Trả hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC27 | Đánh giá sản phẩm | Đánh giá sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC28 | Xem đánh giá sản phẩm | Đánh giá sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC29 | Tra cứu sản phẩm | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC30 | Tra cứu SKU | Danh mục sản phẩm | PARTIAL | Có thông tin SKU trong chi tiết sản phẩm và tồn theo skuId; chưa có luồng tra cứu SKU độc lập theo mã. |
| UC31 | Quản lý sản phẩm | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC32 | Quản lý danh mục sản phẩm | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC33 | Quản lý thương hiệu | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC34 | Quản lý thuộc tính mỹ phẩm | Danh mục sản phẩm | NOT IMPLEMENTED | Có entity thuộc tính và dữ liệu seed; không có luồng CRUD thuộc tính/giá trị. |
| UC35 | Quản lý SKU/biến thể | Danh mục sản phẩm | NOT IMPLEMENTED | Có SKU entity/DTO và tra cứu trong sản phẩm; không có controller/service CRUD SKU. |
| UC36 | Quản lý hình ảnh sản phẩm | Danh mục sản phẩm | NOT IMPLEMENTED | Có ảnh và dữ liệu seed; không có API/luồng quản lý ảnh hoàn chỉnh. |
| UC37 | Kiểm duyệt đánh giá sản phẩm | Đánh giá sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC38 | Tra cứu cửa hàng | Chi nhánh | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC39 | Quản lý cửa hàng | Chi nhánh | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC40 | Quản lý giờ hoạt động | Chi nhánh | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC41 | Tra cứu sản phẩm tại cửa hàng | Danh mục sản phẩm | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC42 | Xem vị trí cửa hàng | Chi nhánh | PARTIAL | Có địa chỉ/tọa độ ở API; chưa có bản đồ/chỉ đường hoàn chỉnh. |
| UC43 | Tra cứu nhà cung cấp | Nhập hàng | PARTIAL | Có API list/get nhà cung cấp; chưa có luồng tìm kiếm riêng. |
| UC44 | Quản lý thông tin nhà cung cấp | Nhập hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC45 | Lập/Cập nhật phiếu nhập hàng | Nhập hàng | PARTIAL | Tạo DRAFT, xem và xác nhận; không có API cập nhật nội dung DRAFT. |
| UC46 | Xác nhận nhập hàng | Nhập hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC47 | Tra cứu lịch sử nhập hàng | Nhập hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC48 | Xem chi tiết phiếu nhập | Nhập hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC49 | Tra cứu tồn kho | Tồn kho | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC50 | Ghi nhận nhập kho | Nhập hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC51 | Điều chỉnh tồn kho | Tồn kho | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC52 | Kiểm kê kho | Tồn kho | NOT IMPLEMENTED | Không có phiếu/luồng kiểm kê; điều chỉnh tồn không phải nghiệp vụ kiểm kê đầy đủ. |
| UC53 | Điều chuyển hàng giữa cửa hàng | Điều chuyển | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC54 | Quản lý hàng giữ | Đặt hàng và giữ tồn | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC55 | Tính tồn khả dụng | Đặt hàng và giữ tồn | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC56 | Tra cứu sản phẩm tồn thấp | Tồn kho | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC57 | Xuất báo cáo tồn kho | Tồn kho | NOT IMPLEMENTED | Không có endpoint/luồng xuất báo cáo tồn. |
| UC58 | Tra cứu khách hàng | Khách hàng | PARTIAL | Có list/get/profile; chưa có tìm kiếm khách hàng riêng. |
| UC59 | Xem hồ sơ khách hàng | Khách hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC60 | Xem lịch sử mua hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC61 | Cập nhật trạng thái tài khoản khách hàng | Khách hàng | NOT IMPLEMENTED | Không có API cập nhật trạng thái tài khoản khách hàng. |
| UC62 | Tiếp nhận yêu cầu trả hàng | Trả hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC63 | Xử lý yêu cầu trả hàng | Trả hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC64 | Tra cứu lịch sử yêu cầu hỗ trợ | Trả hàng | NOT IMPLEMENTED | Không có module ticket/lịch sử hỗ trợ; lịch sử trả hàng không thay thế chức năng này. |
| UC65 | Tra cứu đơn hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC66 | Xem chi tiết đơn hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC67 | Xác nhận đơn hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC68 | Phân công chi nhánh xử lý | Đặt hàng và giữ tồn | PARTIAL | Tự chọn hoặc nhận fulfillmentStoreId lúc checkout; chưa có API nhân viên phân công lại. |
| UC69 | Cập nhật trạng thái đơn hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC70 | Hủy đơn hàng | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC71 | Tính tổng tiền đơn hàng | Đặt hàng và giữ tồn | PARTIAL | Tính tiền khi preview/checkout; chưa có công cụ nhân viên sửa/tính lại đơn. |
| UC72 | Cập nhật thông tin giao hàng | Vòng đời đơn hàng | NOT IMPLEMENTED | Đơn lưu snapshot giao hàng nhưng không có API sửa giao hàng sau khi tạo. |
| UC73 | Xác nhận hoàn thành đơn | Vòng đời đơn hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC74 | Xử lý đơn trả hàng đã duyệt | Trả hàng | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC75 | In/Xuất thông tin đơn hàng | Vòng đời đơn hàng | NOT IMPLEMENTED | Không có API/luồng in hoặc xuất đơn chuyên dụng. |
| UC76 | Quản lý chương trình khuyến mãi | Khuyến mãi chưa có module quản lý | NOT IMPLEMENTED | Không có module chương trình khuyến mãi. |
| UC77 | Quản lý voucher | Khuyến mãi chưa có module quản lý | NOT IMPLEMENTED | Không có module CRUD voucher; chỉ ba mã cố định trong OrderServiceImpl. |
| UC78 | Kiểm tra điều kiện voucher | Đặt hàng và giữ tồn | PARTIAL | Nhận biết ba mã cố định; chưa có ngày hiệu lực, quota hoặc điều kiện cấp phát. |
| UC79 | Tính giá trị giảm | Đặt hàng và giữ tồn | PARTIAL | Có công thức ba mã cố định, chưa có cấu hình chương trình khuyến mãi. |
| UC80 | Xem hiệu quả chương trình khuyến mãi | Khuyến mãi chưa có module quản lý | NOT IMPLEMENTED | Không có thống kê hiệu quả chương trình khuyến mãi. |
| UC81 | Xuất báo cáo khuyến mãi | Khuyến mãi chưa có module quản lý | NOT IMPLEMENTED | Không có xuất báo cáo khuyến mãi. |
| UC82 | Tra cứu nhân viên | Nhân viên và vai trò | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC83 | Quản lý nhân viên | Nhân viên và vai trò | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC84 | Quản lý trạng thái tài khoản nhân viên | Nhân viên và vai trò | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC85 | Phân nhân viên vào chi nhánh | Nhân viên và vai trò | IMPLEMENTED | Có luồng controller/service tương ứng; mức triển khai không phải kết luận đã kiểm thử đạt. |
| UC86 | Phân quyền nhân viên | Nhân viên và vai trò | PARTIAL | Có đọc roles và gán roleIds qua nhân viên; không có CRUD vai trò/permission. |
| UC87 | Tra cứu nhật ký thao tác | Nhân viên và vai trò | NOT IMPLEMENTED | Có AuditLogEntity/repository; chưa có luồng ghi và tra cứu nhật ký. |
| UC88 | Tổng quan doanh thu và đơn hàng | Tổng quan quản trị | PARTIAL | Có số liệu dashboard/doanh thu đơn hoàn tất và lọc chi nhánh; chưa có bộ báo cáo theo kỳ đầy đủ. |
| UC89 | Báo cáo sản phẩm và tồn kho | Tổng quan quản trị | PARTIAL | Có bảng tồn/low-stock; chưa có đầy đủ sản phẩm bán chạy/xuất báo cáo. |
| UC90 | Báo cáo nhập hàng và khuyến mãi | Tổng quan quản trị | NOT IMPLEMENTED | Có danh sách phiếu nhập nhưng chưa có báo cáo tổng hợp nhập hàng/khuyến mãi. |
| UC91 | Báo cáo khách hàng | Khách hàng | NOT IMPLEMENTED | Có hồ sơ/danh sách khách hàng, chưa có chức năng báo cáo khách hàng. |

Các thành phần hỗ trợ không có UC độc lập: Spring Security/JWT, bộ lọc X-Requested-With, xử lý lỗi toàn cục, cấu hình local/prod/test, seeding, email service, Thymeleaf và JavaScript. Chúng được liên kết qua các ca AUTH/PQ/EMAIL và ledger source_inventory.json. Không có cổng thanh toán online, thu hồi JWT, CRUD quyền/role, upload ảnh hoặc báo cáo xuất file hoàn chỉnh chỉ vì có dependency/entity liên quan.
