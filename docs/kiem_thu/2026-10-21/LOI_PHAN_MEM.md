# Lỗi phần mềm và nhận xét kiểm thử

10 defect dưới đây có assertion FAIL thực thi trên cả H2 và PostgreSQL 16.15; mỗi defect là một vấn đề nghiệp vụ/bảo mật, không phải đếm mỗi assertion thành lỗi mới. Mức nghiêm trọng là đánh giá QA theo tác động, chưa phải điểm CVSS hay mức độ khai thác trên môi trường triển khai. Tất cả đang OPEN, chưa sửa production.

## BUG-01 — Tái kiểm tra SKU ngừng bán tại checkout

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Cao / OPEN |
| Ca tái hiện | QA-INT-05; checkoutMustRejectInactiveSku |
| Điều kiện / input | OrderRequest: fulfillmentStoreId=HCM; recipientName="QA Receiver"; recipientPhone="0901234567"; shippingAddress="QA test shipping address"; ward="QA Ward"; district="QA District"; province="TP. Hồ Chí Minh"; paymentMethod=COD; không voucher. Đây là dữ liệu QA tổng hợp, không phải địa chỉ thật. Đặt SKU1 INACTIVE sau khi có sẵn trong giỏ. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức checkoutMustRejectInactiveSku. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | Ném BusinessException, không tạo đơn/giữ tồn mới. Đây là tiêu chí QA bảo toàn điều kiện được phép bán. |
| Quan sát | Không ném BusinessException: placeOrder trả về đơn cho SKU đã INACTIVE. Assertion thất bại. |
| Tác động | Đơn có thể bán SKU đã ngừng hoạt động. |
| Vị trí source | src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:69 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#checkoutMustRejectInactiveSku; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | Tái kiểm tra product/SKU ACTIVE tại checkout, trong cùng giao dịch với chọn tồn và tạo đơn. |

## BUG-02 — Rollback toàn bộ phiếu nhập khi dòng sau có SKU không tồn tại

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Nghiêm trọng / OPEN |
| Ca tái hiện | QA-INT-06; purchaseMustRollbackWhenLaterSkuMissing |
| Điều kiện / input | supplierId=nhà cung cấp QA mới; receivingStoreId=HCM; items=[{skuId:SKU1,quantity:1,unitPrice:100000},{skuId:999999999,quantity:1,unitPrice:100000}]. Fixture không có SKU 999999999. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức purchaseMustRollbackWhenLaterSkuMissing. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | Không còn header hoặc dòng phiếu phát sinh từ yêu cầu lỗi. |
| Quan sát | Ném Exception nhưng header và dòng đầu vẫn commit; số dòng tăng 1. |
| Tác động | Phiếu nhập dở dang được commit dù trả lỗi. |
| Vị trí source | src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java:41 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#purchaseMustRollbackWhenLaterSkuMissing; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | Validate tất cả dòng trước ghi; bảo đảm rollback cho loại exception thực tế; test đọc lại sau service transaction. |

## BUG-03 — Điều chuyển phải bảo toàn lượng đã giữ cho đơn

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Cao / OPEN |
| Ca tái hiện | QA-INT-07; transferMustRespectHeldStock |
| Điều kiện / input | HCM SKU1 actual=10, held=8, available=2; phiếu HCM → HN, SKU1 quantity=5. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức transferMustRespectHeldStock. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | Từ chối xuất vì 5>2; actual vẫn 10. |
| Quan sát | Xuất thành công; actual=5, held=8. Tồn thực tế thấp hơn lượng đã giữ. |
| Tác động | Điều chuyển tiêu thụ lượng đã giữ cho khách, có thể làm thiếu hàng giao. |
| Vị trí source | src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:73 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#transferMustRespectHeldStock; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | So lượng xuất với tồn khả dụng; đồng bộ với checkout/reservation và dùng cơ chế khóa hoặc cập nhật có điều kiện phù hợp. |

## BUG-04 — Rollback trừ kho khi dòng điều chuyển thứ hai thiếu hàng

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Nghiêm trọng / OPEN |
| Ca tái hiện | QA-INT-08; transferMustRollbackAllLines |
| Điều kiện / input | HCM SKU1 actual=10, held=0, xuất 2; SKU2 actual=1, held=0, xuất 2; đích HN. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức transferMustRollbackAllLines. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | SKU1 còn 10; phiếu PENDING, không trừ dở dang. |
| Quan sát | SKU1 còn 8 dù thao tác lỗi; phiếu vẫn PENDING. |
| Tác động | Thao tác xuất lỗi vẫn trừ kho dòng trước, phiếu còn PENDING. |
| Vị trí source | src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:73 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#transferMustRollbackAllLines; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | Bảo đảm toàn bộ shipment atomic; validate trước ghi và rollback khi bất kỳ dòng nào lỗi. |

## BUG-05 — Lưu số lần OTP sai qua giao dịch lỗi

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Cao / OPEN |
| Ca tái hiện | QA-INT-09; otpFailedAttemptMustPersist |
| Điều kiện / input | Hai fixture OTP đăng ký và đặt lại mật khẩu riêng, còn hạn +5 phút, failedAttempts=0, used=false; mã fixture 654321, nhập sai 111111. Mã là hằng QA, không là OTP đang hoạt động của người dùng. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức otpFailedAttemptMustPersist. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | Mỗi failedAttempts tăng 0→1 dù xác minh trả lỗi. |
| Quan sát | Cả hai failedAttempts vẫn 0; cập nhật bị rollback cùng BusinessException. |
| Tác động | Giới hạn số lần sai không được tích lũy qua các lần xác minh lỗi. |
| Vị trí source | src/main/java/com/thinh/cosmetic/service/account/impl/OtpServiceImpl.java:130 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#otpFailedAttemptMustPersist; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | Tách transaction ghi sự kiện sai hoặc thiết kế exception/transaction bảo toàn counter; kiểm tra cả đăng ký/reset và ca hết hạn/đã dùng. |

## BUG-06 — Không lộ OTP trong response đăng ký công khai

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Cao / OPEN |
| Ca tái hiện | QA-INT-10; publicResponseMustNotExposeOtp |
| Điều kiện / input | POST /api/auth/register/send-otp; JSON {emailOrPhone:"qa-audit-N-public@example.test"}; X-Requested-With: fetch. EmailService được thay bằng no-op riêng trong test. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức publicResponseMustNotExposeOtp. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | Có thể phản hồi tiếp nhận, nhưng body không chứa OTP dùng được. |
| Quan sát | HTTP 200; body có debugOtp 6 chữ số. Chỉ xác minh lộ response đăng ký, chưa thử reset/tấn công hoàn chỉnh hoặc gửi email thật. |
| Tác động | Response công khai cấp ngay OTP đăng ký; làm mất ý nghĩa chứng minh kiểm soát email. |
| Vị trí source | src/main/java/com/thinh/cosmetic/service/account/impl/OtpServiceImpl.java:270 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#publicResponseMustNotExposeOtp; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | Không trả/log OTP dùng được ở môi trường sử dụng; tách hẳn phương thức test email. Nhánh reset cùng gán debugOtp ở source cần test riêng. |

## BUG-07 — Chặn nhân viên HCM điều chỉnh tồn HN

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Cao / OPEN |
| Ca tái hiện | QA-INT-11; inventoryMustEnforceStoreScope |
| Điều kiện / input | Principal ROLE_EMPLOYEE + INVENTORY_VIEW + INVENTORY_ADJUST, chỉ có employee_store HCM; POST /api/inventory/adjust {storeId:HN,skuId:SKU1,newQuantity:33,reason:"QA cross-store probe"}; X-Requested-With: fetch. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức inventoryMustEnforceStoreScope. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | HTTP 403; actual HN giữ 10. |
| Quan sát | HTTP 200; actual HN đổi thành 33. |
| Tác động | Nhân viên một chi nhánh sửa được kho chi nhánh khác. |
| Vị trí source | src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java:48 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#inventoryMustEnforceStoreScope; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | Áp StoreScope cho cả đọc/ghi, lấy employeeId từ principal; kiểm tra module PO/transfer/return tương tự, chưa suy rộng kết quả runtime. |

## BUG-08 — Chặn quyền xử lý đơn bị thiếu trên admin API

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Cao / OPEN |
| Ca tái hiện | QA-INT-12; adminOrderApiMustEnforceFunctionalPermission |
| Điều kiện / input | Đơn A tại HCM PENDING; principal ROLE_EMPLOYEE + INVENTORY_VIEW, không ORDER_PROCESS; PUT /api/admin/orders/{id}/status?status=CONFIRMED; X-Requested-With: fetch. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức adminOrderApiMustEnforceFunctionalPermission. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | HTTP 403; đơn vẫn PENDING. |
| Quan sát | HTTP 200; đơn chuyển CONFIRMED. |
| Tác động | Có ROLE_EMPLOYEE là đủ đi qua đường admin để xử lý đơn dù thiếu quyền chức năng. |
| Vị trí source | src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:37 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#adminOrderApiMustEnforceFunctionalPermission; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | Áp cùng chính sách ORDER_PROCESS tại mọi đường cập nhật/cancel; giữ kiểm tra chi nhánh ở service. |

## BUG-09 — Chặn xem đơn người khác qua trang đặt hàng thành công

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Cao / OPEN |
| Ca tái hiện | QA-INT-14; successPageMustEnforceOrderOwnership |
| Điều kiện / input | Đơn thuộc B; principal ROLE_CUSTOMER của A; GET /order-success?orderCode={mã đơn B vừa tạo}. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức successPageMustEnforceOrderOwnership. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | Model không tiết lộ đơn B; phản hồi phải từ chối hoặc không hiển thị dữ liệu đơn khác chủ. |
| Quan sát | Model có OrderResponse với customerId của B. Chưa chạy JavaScript/trình duyệt. |
| Tác động | Trang success nhận orderCode có thể trả đơn của khách khác. |
| Vị trí source | src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java:46 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#successPageMustEnforceOrderOwnership; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | Kiểm tra chủ đơn/StoreScope trước đưa OrderResponse vào model; không dựa vào khó đoán orderCode. |

## BUG-10 — Hai giao dịch tranh mua đơn vị tồn cuối

| Trường | Nội dung |
| --- | --- |
| Mức độ / trạng thái | Nghiêm trọng / OPEN |
| Ca tái hiện | QA-INT-15; concurrentCheckoutMustNotOversellLastUnit |
| Điều kiện / input | HCM SKU1 actual=1, held=0; A/B mỗi giỏ quantity=1; hai TransactionTemplate, hai thread, CyclicBarrier(2), timeout 20 giây. |
| Bước lặp lại | 1. Chạy fixture() trong src/test/java/com/thinh/cosmetic/audit/AuditBusinessIntegrationTest.java. 2. Chạy phương thức concurrentCheckoutMustNotOversellLastUnit. 3. Đối chiếu assertions và dữ liệu sau transaction theo DANH_SACH_TEST_CASE.md. |
| Mong đợi | Chỉ 1 giao dịch thành công và tổng HELD=1. |
| Quan sát | Cả 2 giao dịch thành công; tổng HELD=2 cho fixture actual=1. Lịch xen kẽ được điều khiển, không phải đo tải; test không assert giá trị held cuối cùng của inventory. |
| Tác động | Hai đơn HELD cùng tiêu thụ một đơn vị tồn; ảnh hưởng tính toàn vẹn đơn và kho. |
| Vị trí source | src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:291 |
| Minh chứng | minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml#concurrentCheckoutMustNotOversellLastUnit; đối chứng cùng phương thức tại minh_chung/final_h2/. |
| Đề xuất, chưa triển khai | Bảo vệ đọc–kiểm tra–giữ tồn bằng khóa/optimistic version/cập nhật điều kiện; thống nhất thứ tự khóa nhiều SKU, rollback và cơ chế retry hợp lý. |

## Hai failures thuộc chất lượng test hiện hữu

`OrderEndpointsTest.testAdminOrdersPageAccess` (dòng 86) và `WebPageControllersTest.testAdminPagesForEmployee` (dòng 157) mong HTTP 200 với chỉ ROLE_EMPLOYEE tại trang hiện yêu cầu quyền chức năng. Thực chạy nhận 403 ở cả baseline và final H2. Đây là test fixture/expectation lỗi thời, không đủ căn cứ kết luận production sai hoặc nới phân quyền. Đề xuất chia ca được phép/bị chặn và cấp đúng authority của trang. Chưa chỉnh hai test cũ trong đợt audit để giữ bằng chứng nguyên trạng. Xem CSV/XML final_h2 để lấy đầy đủ tên class và stack trace.

## Rủi ro chỉ được nhận diện qua source, chưa gán FAIL

| ID | Nhận xét | Kiểm tra còn thiếu |
|---|---|---|
| SR-01 | JWT dùng quyền trong claims; logout xóa cookie, không thu hồi token hay kiểm tra lại trạng thái account mỗi request. | Chốt yêu cầu thời gian hiệu lực quyền/khóa; chạy MAN-AUTH-04 bằng token thật. |
| SR-02 | PO, transfer và returns có nhánh thiếu StoreScope; returns update có ORDER_PROCESS, không được mô tả là thiếu toàn bộ quyền. | MAN-PQ-03, kiểm tra cả đọc và ghi; QA-INT-11 chỉ xác nhận inventory adjust. |
| SR-03 | Checkout UI tự tính fallback khi preview lỗi; mã lạ có discount0 nhưng toast có thể gây hiểu lầm. | MAN-ORD-05, ghi cả response và đơn server. |
| SR-04 | Nhánh reset cũng gán debugOtp trong OtpServiceImpl; log có mã. | Chạy test response reset và kiểm tra logging; chưa chứng minh runtime reset hoặc chiếm tài khoản. |
| SR-05 | Cập nhật địa chỉ mặc định chưa có cơ chế đồng nhất mọi địa chỉ; ID role/store sai có thể bị bỏ qua. | MAN-ACCOUNT-01, MAN-EMP-01. |
| SR-06 | Giá/tồn tổng sản phẩm có thể gồm SKU hoặc kho không hoạt động; wishlist inStock dựa SKU ACTIVE. | MAN-CATALOG-02, MAN-WISH-01; đối chiếu hiển thị với tồn khả dụng có điều kiện. |
| SR-07 | Thiếu uniqueness(store,sku)/version ở InventoryEntity; sinh orderCode ngẫu nhiên chưa retry xung đột. | Test trùng inventory, orderCode collision và concurrency nhiều SKU/khác nghiệp vụ. |
| SR-08 | low-stock hiện dùng actualStock<=minimumStock, khác chỉ tiêu availableStock. | Chốt định nghĩa với quy định yêu cầu, rồi kiểm thử; không đổi oracle tùy ý để ép FAIL. |
| SR-09 | Source chưa chặn điều chuyển cùng nguồn/đích, enum có PENDING_EXPORT không được service sử dụng. | Chốt quy tắc điều chuyển; kiểm thử MAN-XFER-01, không mô tả là đã có validation. |

Các đề xuất chỉ là hướng sửa. Cần thay đổi source được cho phép, kiểm tra hồi quy và biên bản chạy lại trước khi đóng lỗi; không tự đổi FAIL thành PASS vì đã viết đề xuất.
