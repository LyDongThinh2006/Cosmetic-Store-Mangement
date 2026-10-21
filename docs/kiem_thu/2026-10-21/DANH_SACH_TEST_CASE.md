# Danh sách test case LUNEA

Ngày chốt: 21/10/2026 (+07:00). 53 kịch bản thiết kế; 15 đã thực thi trên cả H2 và PostgreSQL. Danh sách test gốc 134 phương thức có ledger riêng, không coi chúng là 134 ca E2E đã đạt.

## Quy ước và fixture

F-MAN là fixture dự kiến cho kiểm thử hệ thống, chưa được triển khai/chạy trong đợt audit. Có thể khởi tạo DB QA từ DataSeeder để lấy khách khach01@lunea.test và SKU-CCN-BD-70ML; phải đọc lại API/DB để xác nhận trước test. Không dùng thông tin seed để khẳng định DB local đang có dữ liệu. Tạo thêm khách B, nhân viên giới hạn HCM, hai chi nhánh HCM/HN và quan hệ role/store bằng API quản trị hiện có hoặc fixture được kiểm soát. Dùng email @example.test chỉ cho dữ liệu QA không gửi thư; luồng SMTP cần hộp thư QA thật.

Các tên A/B/HCM/HN/SKU1/SKU2 là alias: lấy ID thực từ response hoặc truy vấn, ghi vào biên bản mỗi lần chạy. Không gán ID cố định. Chi nhánh và địa chỉ ghi rõ tỉnh để tái hiện chọn cửa hàng. Tồn 10/2 hoặc các mức biên là dữ liệu phải chuẩn bị ở DB QA, không phải quan sát dữ liệu local. Khi tạo qua fixture cần lưu script/version, chụp trạng thái trước/sau và khôi phục riêng từng biến thể. Mật khẩu không ghi trong ảnh/log; dùng biến môi trường hoặc kho bí mật của nhóm, không đưa JWT/cookie vào tài liệu.

Mỗi ca MAN là một kịch bản hệ thống có các biến thể được nêu rõ. Khi thực thi phải ghi kết quả từng biến thể, chỉ kết luận PASS khi mọi tiêu chí của ca đã chạy đủ; phần chưa chạy phải tách kết quả, không lấy một biến thể đạt để đánh dấu cả ca. ID biến thể ghi hậu tố .V01, .V02 theo thứ tự dữ liệu; hiện chưa cấp kết quả cho biến thể nào. Trường BLOCKED nêu phụ thuộc thực tế, không dùng để che một ca đơn giản chưa được chạy.

Mọi request ghi dựa cookie cần X-Requested-With: fetch (hoặc XMLHttpRequest) theo XRequestedWithFilter; request Bearer có nhánh xử lý riêng. Với MockMvc audit, principal được đưa trực tiếp vào security context; đây không phải kiểm thử đăng nhập cấp JWT thật. Trong test tích hợp EmailService là no-op, phần còn lại đi qua service/repository/database thật. Không có transaction bao quanh test; kiểm tra rollback dựa việc đọc lại sau transaction service. Lịch đồng thời QA-INT-15 dùng hai transaction thật đã đọc trước barrier, chỉ xác minh một lịch xen kẽ có thể xảy ra.


Fixture F-INT được commit riêng trước mỗi test: A/B là khách hàng khác nhau; nhân viên chỉ thuộc HCM; hai chi nhánh HCM/HN ACTIVE; SKU1 ACTIVE giá 265.000; tồn SKU1 mỗi chi nhánh actual=10, held=2; SKU2 HCM actual=1, held=0; giỏ A=3 SKU1, B=1 SKU1. ID thực tế theo QA_FIXTURE trong XML. Không dùng tài khoản/DB local.

OrderRequest: fulfillmentStoreId=HCM; recipientName="QA Receiver"; recipientPhone="0901234567"; shippingAddress="QA test shipping address"; ward="QA Ward"; district="QA District"; province="TP. Hồ Chí Minh"; paymentMethod=COD; không voucher. Đây là dữ liệu QA tổng hợp, không phải địa chỉ thật.

Minh chứng E-PG là minh_chung/audit_postgresql/TEST-com.thinh.cosmetic.audit.AuditBusinessIntegrationTest.xml; E-H2 là XML cùng tên ở minh_chung/final_h2/. Tra theo method tại trường Minh chứng mỗi ca. Đường dẫn đầy đủ và điều kiện chung lặp lại được lưu trong JSON/CSV; bản Word/Markdown dẫn chiếu điều kiện chung để giảm trùng lặp.

## QA-INT-01 — Đặt hàng lưu tiền hàng, giữ tồn và xóa giỏ

| Trường (QA-INT-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC21 / UC54; Đặt hàng và giữ tồn |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh đặt hàng lưu tiền hàng, giữ tồn và xóa giỏ. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | OrderRequest: fulfillmentStoreId=HCM; recipientName="QA Receiver"; recipientPhone="0901234567"; shippingAddress="QA test shipping address"; ward="QA Ward"; district="QA District"; province="TP. Hồ Chí Minh"; paymentMethod=COD; không voucher. Đây là dữ liệu QA tổng hợp, không phải địa chỉ thật. |
| Các bước | 1. Gọi OrderService.placeOrder(A, request).; 2. Sau khi service commit, đọc lại orders, inventories, order_reservations và cart_items. |
| Kết quả mong đợi / tiêu chí chấp nhận | Đơn PENDING; subtotal=795.000, actual=10, held=5; reservation HELD; giỏ A trống. |
| Kết quả thực tế | Tất cả assertions trên thỏa mãn. Chưa chứng minh đầy đủ snapshot tên/giá/địa chỉ hoặc mọi thành phần totalAmount. |
| Trạng thái | PASS |
| Minh chứng | E-PG và E-H2; method=checkoutPersistsSnapshotAndHold. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## QA-INT-02 — Rollback checkout khi không đủ tồn

| Trường (QA-INT-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC21 / UC54; Đặt hàng và giữ tồn |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh rollback checkout khi không đủ tồn. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | OrderRequest: fulfillmentStoreId=HCM; recipientName="QA Receiver"; recipientPhone="0901234567"; shippingAddress="QA test shipping address"; ward="QA Ward"; district="QA District"; province="TP. Hồ Chí Minh"; paymentMethod=COD; không voucher. Đây là dữ liệu QA tổng hợp, không phải địa chỉ thật. Thay tồn HCM SKU1 thành actual=2, held=2. |
| Các bước | 1. Đếm địa chỉ của A, giữ giỏ 3 SKU1.; 2. Gọi placeOrder; bắt BusinessException.; 3. Đọc lại địa chỉ, đơn, giỏ và held trong truy vấn mới. |
| Kết quả mong đợi / tiêu chí chấp nhận | Không thêm địa chỉ/đơn; giỏ còn nguyên; held=2. |
| Kết quả thực tế | BusinessException phát sinh; số địa chỉ không đổi, không có đơn của A, giỏ còn 1 dòng, held=2. |
| Trạng thái | PASS |
| Minh chứng | E-PG và E-H2; method=failedCheckoutRollsBackAddress. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## QA-INT-03 — Hủy PENDING giải phóng đúng một lần

| Trường (QA-INT-03) | Nội dung |
| --- | --- |
| Use Case / Module | UC25 / UC54; Vòng đời đơn hàng |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh hủy PENDING giải phóng đúng một lần. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | OrderRequest: fulfillmentStoreId=HCM; recipientName="QA Receiver"; recipientPhone="0901234567"; shippingAddress="QA test shipping address"; ward="QA Ward"; district="QA District"; province="TP. Hồ Chí Minh"; paymentMethod=COD; không voucher. Đây là dữ liệu QA tổng hợp, không phải địa chỉ thật. |
| Các bước | 1. Tạo đơn A số lượng 3, ghi ID.; 2. Gọi cancelOrder(ID,A), rồi gọi lại.; 3. Đọc actual/held và reservation sau hai lần. |
| Kết quả mong đợi / tiêu chí chấp nhận | Lần hai bị từ chối; actual=10, held=2; reservation RELEASED. |
| Kết quả thực tế | Lần hủy thứ hai ném BusinessException; actual=10, held=2 và RELEASED. |
| Trạng thái | PASS |
| Minh chứng | E-PG và E-H2; method=cancelReleasesHoldExactlyOnce. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## QA-INT-04 — Giao hàng chỉ trừ tồn một lần

| Trường (QA-INT-04) | Nội dung |
| --- | --- |
| Use Case / Module | UC69 / UC73 / UC54; Vòng đời đơn hàng |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh giao hàng chỉ trừ tồn một lần. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | OrderRequest: fulfillmentStoreId=HCM; recipientName="QA Receiver"; recipientPhone="0901234567"; shippingAddress="QA test shipping address"; ward="QA Ward"; district="QA District"; province="TP. Hồ Chí Minh"; paymentMethod=COD; không voucher. Đây là dữ liệu QA tổng hợp, không phải địa chỉ thật. |
| Các bước | 1. Tạo đơn số lượng 3.; 2. Gọi updateStatus theo CONFIRMED → PREPARING → SHIPPING → SHIPPING → COMPLETED.; 3. Đọc lại tồn và reservation. |
| Kết quả mong đợi / tiêu chí chấp nhận | actual=7, held=2; reservation COMMITTED; lặp SHIPPING không trừ thêm. |
| Kết quả thực tế | actual=7, held=2, COMMITTED sau chuỗi lời gọi. Assertions trọng tâm là tồn và reservation. |
| Trạng thái | PASS |
| Minh chứng | E-PG và E-H2; method=shippingCommitsStockOnlyOnce. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## QA-INT-05 — Tái kiểm tra SKU ngừng bán tại checkout

| Trường (QA-INT-05) | Nội dung |
| --- | --- |
| Use Case / Module | UC21; Đặt hàng và giữ tồn |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh tái kiểm tra SKU ngừng bán tại checkout. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | OrderRequest: fulfillmentStoreId=HCM; recipientName="QA Receiver"; recipientPhone="0901234567"; shippingAddress="QA test shipping address"; ward="QA Ward"; district="QA District"; province="TP. Hồ Chí Minh"; paymentMethod=COD; không voucher. Đây là dữ liệu QA tổng hợp, không phải địa chỉ thật. Đặt SKU1 INACTIVE sau khi có sẵn trong giỏ. |
| Các bước | 1. Lưu trạng thái INACTIVE của SKU1.; 2. Gọi placeOrder(A,request). |
| Kết quả mong đợi / tiêu chí chấp nhận | Ném BusinessException, không tạo đơn/giữ tồn mới. Đây là tiêu chí QA bảo toàn điều kiện được phép bán. |
| Kết quả thực tế | Không ném BusinessException: placeOrder trả về đơn cho SKU đã INACTIVE. Assertion thất bại. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=checkoutMustRejectInactiveSku. |
| Ghi chú lỗi | BUG-01 |

## QA-INT-06 — Rollback toàn bộ phiếu nhập khi dòng sau có SKU không tồn tại

| Trường (QA-INT-06) | Nội dung |
| --- | --- |
| Use Case / Module | UC45; Nhập hàng |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh rollback toàn bộ phiếu nhập khi dòng sau có SKU không tồn tại. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | supplierId=nhà cung cấp QA mới; receivingStoreId=HCM; items=[{skuId:SKU1,quantity:1,unitPrice:100000},{skuId:999999999,quantity:1,unitPrice:100000}]. Fixture không có SKU 999999999. |
| Các bước | 1. Tạo supplier QA, đếm purchase_order_items.; 2. Gọi PurchaseOrderService.create(request,employeeId); bắt Exception.; 3. Đọc phiếu theo supplier và đếm lại dòng. |
| Kết quả mong đợi / tiêu chí chấp nhận | Không còn header hoặc dòng phiếu phát sinh từ yêu cầu lỗi. |
| Kết quả thực tế | Ném Exception nhưng header và dòng đầu vẫn commit; số dòng tăng 1. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=purchaseMustRollbackWhenLaterSkuMissing. |
| Ghi chú lỗi | BUG-02 |

## QA-INT-07 — Điều chuyển phải bảo toàn lượng đã giữ cho đơn

| Trường (QA-INT-07) | Nội dung |
| --- | --- |
| Use Case / Module | UC53 / UC55; Điều chuyển |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh điều chuyển phải bảo toàn lượng đã giữ cho đơn. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | HCM SKU1 actual=10, held=8, available=2; phiếu HCM → HN, SKU1 quantity=5. |
| Các bước | 1. Commit tồn 10/8.; 2. Tạo phiếu quantity=5.; 3. Gọi confirmShipment, rồi đọc actual. |
| Kết quả mong đợi / tiêu chí chấp nhận | Từ chối xuất vì 5>2; actual vẫn 10. |
| Kết quả thực tế | Xuất thành công; actual=5, held=8. Tồn thực tế thấp hơn lượng đã giữ. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=transferMustRespectHeldStock. |
| Ghi chú lỗi | BUG-03 |

## QA-INT-08 — Rollback trừ kho khi dòng điều chuyển thứ hai thiếu hàng

| Trường (QA-INT-08) | Nội dung |
| --- | --- |
| Use Case / Module | UC53; Điều chuyển |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh rollback trừ kho khi dòng điều chuyển thứ hai thiếu hàng. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | HCM SKU1 actual=10, held=0, xuất 2; SKU2 actual=1, held=0, xuất 2; đích HN. |
| Các bước | 1. Tạo phiếu hai dòng.; 2. Gọi confirmShipment và bắt Exception ở dòng SKU2.; 3. Đọc lại tồn SKU1 và trạng thái phiếu bằng repository. |
| Kết quả mong đợi / tiêu chí chấp nhận | SKU1 còn 10; phiếu PENDING, không trừ dở dang. |
| Kết quả thực tế | SKU1 còn 8 dù thao tác lỗi; phiếu vẫn PENDING. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=transferMustRollbackAllLines. |
| Ghi chú lỗi | BUG-04 |

## QA-INT-09 — Lưu số lần OTP sai qua giao dịch lỗi

| Trường (QA-INT-09) | Nội dung |
| --- | --- |
| Use Case / Module | UC03 / UC09; Xác thực và OTP |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh lưu số lần OTP sai qua giao dịch lỗi. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | Hai fixture OTP đăng ký và đặt lại mật khẩu riêng, còn hạn +5 phút, failedAttempts=0, used=false; mã fixture 654321, nhập sai 111111. Mã là hằng QA, không là OTP đang hoạt động của người dùng. |
| Các bước | 1. Lưu hai loại OTP.; 2. Gọi verifyOtp và verifyRegistrationOtp bằng mã sai, bắt BusinessException.; 3. Đọc lại từng entity sau transaction service. |
| Kết quả mong đợi / tiêu chí chấp nhận | Mỗi failedAttempts tăng 0→1 dù xác minh trả lỗi. |
| Kết quả thực tế | Cả hai failedAttempts vẫn 0; cập nhật bị rollback cùng BusinessException. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=otpFailedAttemptMustPersist. |
| Ghi chú lỗi | BUG-05 |

## QA-INT-10 — Không lộ OTP trong response đăng ký công khai

| Trường (QA-INT-10) | Nội dung |
| --- | --- |
| Use Case / Module | UC09; Xác thực và OTP |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh không lộ OTP trong response đăng ký công khai. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | POST /api/auth/register/send-otp; JSON {emailOrPhone:"qa-audit-N-public@example.test"}; X-Requested-With: fetch. EmailService được thay bằng no-op riêng trong test. |
| Các bước | 1. Gửi request không có principal.; 2. Đọc HTTP status và kiểm tra sự có mặt của debugOtp 6 chữ số, không xuất giá trị mã. |
| Kết quả mong đợi / tiêu chí chấp nhận | Có thể phản hồi tiếp nhận, nhưng body không chứa OTP dùng được. |
| Kết quả thực tế | HTTP 200; body có debugOtp 6 chữ số. Chỉ xác minh lộ response đăng ký, chưa thử reset/tấn công hoàn chỉnh hoặc gửi email thật. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=publicResponseMustNotExposeOtp. |
| Ghi chú lỗi | BUG-06 |

## QA-INT-11 — Chặn nhân viên HCM điều chỉnh tồn HN

| Trường (QA-INT-11) | Nội dung |
| --- | --- |
| Use Case / Module | UC51 / UC85; Phân quyền và chi nhánh |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh chặn nhân viên HCM điều chỉnh tồn HN. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | Principal ROLE_EMPLOYEE + INVENTORY_VIEW + INVENTORY_ADJUST, chỉ có employee_store HCM; POST /api/inventory/adjust {storeId:HN,skuId:SKU1,newQuantity:33,reason:"QA cross-store probe"}; X-Requested-With: fetch. |
| Các bước | 1. Gửi request bằng MockMvc với principal nêu trên.; 2. Đọc HTTP status và tồn SKU1 HN sau request. |
| Kết quả mong đợi / tiêu chí chấp nhận | HTTP 403; actual HN giữ 10. |
| Kết quả thực tế | HTTP 200; actual HN đổi thành 33. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=inventoryMustEnforceStoreScope. |
| Ghi chú lỗi | BUG-07 |

## QA-INT-12 — Chặn quyền xử lý đơn bị thiếu trên admin API

| Trường (QA-INT-12) | Nội dung |
| --- | --- |
| Use Case / Module | UC67 / UC69 / UC86; Phân quyền và chi nhánh |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh chặn quyền xử lý đơn bị thiếu trên admin API. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | Đơn A tại HCM PENDING; principal ROLE_EMPLOYEE + INVENTORY_VIEW, không ORDER_PROCESS; PUT /api/admin/orders/{id}/status?status=CONFIRMED; X-Requested-With: fetch. |
| Các bước | 1. Tạo đơn HCM.; 2. Gửi request cập nhật với principal thiếu quyền.; 3. Đọc HTTP và orders.status. |
| Kết quả mong đợi / tiêu chí chấp nhận | HTTP 403; đơn vẫn PENDING. |
| Kết quả thực tế | HTTP 200; đơn chuyển CONFIRMED. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=adminOrderApiMustEnforceFunctionalPermission. |
| Ghi chú lỗi | BUG-08 |

## QA-INT-13 — Chặn xem đơn của chi nhánh khác trên admin API

| Trường (QA-INT-13) | Nội dung |
| --- | --- |
| Use Case / Module | UC66 / UC85; Phân quyền và chi nhánh |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh chặn xem đơn của chi nhánh khác trên admin API. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | Đơn B tạo ở HN; nhân viên chỉ HCM, có ORDER_VIEW và ORDER_PROCESS; GET /api/admin/orders/{id}. |
| Các bước | 1. Tạo đơn gán HN.; 2. Gửi GET bằng principal nhân viên HCM.; 3. Đối chiếu status. |
| Kết quả mong đợi / tiêu chí chấp nhận | HTTP 403. |
| Kết quả thực tế | HTTP 403. Kết quả chỉ áp dụng endpoint xem đơn này, không suy rộng mọi API. |
| Trạng thái | PASS |
| Minh chứng | E-PG và E-H2; method=orderApiDeniesAnotherStore. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## QA-INT-14 — Chặn xem đơn người khác qua trang đặt hàng thành công

| Trường (QA-INT-14) | Nội dung |
| --- | --- |
| Use Case / Module | UC23; Phân quyền và chi nhánh |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh chặn xem đơn người khác qua trang đặt hàng thành công. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | Đơn thuộc B; principal ROLE_CUSTOMER của A; GET /order-success?orderCode={mã đơn B vừa tạo}. |
| Các bước | 1. Tạo đơn của B, lấy orderCode từ response.; 2. Gửi GET bằng customer A.; 3. Kiểm tra model thuộc trang có chứa OrderResponse của B không. |
| Kết quả mong đợi / tiêu chí chấp nhận | Model không tiết lộ đơn B; phản hồi phải từ chối hoặc không hiển thị dữ liệu đơn khác chủ. |
| Kết quả thực tế | Model có OrderResponse với customerId của B. Chưa chạy JavaScript/trình duyệt. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=successPageMustEnforceOrderOwnership. |
| Ghi chú lỗi | BUG-09 |

## QA-INT-15 — Hai giao dịch tranh mua đơn vị tồn cuối

| Trường (QA-INT-15) | Nội dung |
| --- | --- |
| Use Case / Module | UC21 / UC54 / UC55; Đặt hàng và giữ tồn |
| Ưu tiên / cấp kiểm thử | P0; Tích hợp service/DB hoặc MockMvc; H2 và PostgreSQL |
| Mục tiêu | Xác minh hai giao dịch tranh mua đơn vị tồn cuối. |
| Điều kiện trước | F-INT được tạo và commit riêng trước phương thức (xem Quy ước và fixture); thay đổi bổ sung theo Dữ liệu đầu vào. |
| Dữ liệu đầu vào | HCM SKU1 actual=1, held=0; A/B mỗi giỏ quantity=1; hai TransactionTemplate, hai thread, CyclicBarrier(2), timeout 20 giây. |
| Các bước | 1. Commit tồn và giỏ QA.; 2. Mỗi transaction đọc SKU1 trước barrier để cùng quan sát tồn cuối.; 3. Sau barrier, gọi placeOrder tương ứng trong transaction đó; đợi futures tối đa 40 giây.; 4. Đếm giao dịch thành công và tổng quantity reservation HELD cho SKU1. |
| Kết quả mong đợi / tiêu chí chấp nhận | Chỉ 1 giao dịch thành công và tổng HELD=1. |
| Kết quả thực tế | Cả 2 giao dịch thành công; tổng HELD=2 cho fixture actual=1. Lịch xen kẽ được điều khiển, không phải đo tải; test không assert giá trị held cuối cùng của inventory. |
| Trạng thái | FAIL |
| Minh chứng | E-PG và E-H2; method=concurrentCheckoutMustNotOversellLastUnit. |
| Ghi chú lỗi | BUG-10 |

## MAN-AUTH-01 — Đăng nhập thật, cookie và tài khoản bị khóa

| Trường (MAN-AUTH-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC01; Xác thực và OTP |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh đăng nhập thật, cookie và tài khoản bị khóa. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | khach01@lunea.test do DataSeeder khai báo; chỉ dùng nếu QA đã seed/xác nhận tồn tại. Mật khẩu lấy riêng từ fixture, không đưa vào ảnh. Biến thể sai mật khẩu; password rỗng; identifier không tồn tại; account INACTIVE. |
| Các bước | 1. Mở /login, gửi POST /api/auth/login bằng biểu mẫu hoặc JSON identifier/password.; 2. Ghi HTTP, nội dung lỗi và thuộc tính cookie (che giá trị).; 3. Đăng nhập hợp lệ, gọi GET /api/auth/me; chạy lại với từng biến thể trên fixture sạch. |
| Kết quả mong đợi / tiêu chí chấp nhận | Hợp lệ trả đúng danh tính và cookie; mật khẩu sai/tài khoản khóa không cấp token; rỗng bị từ chối; không lộ hash/mật khẩu. Ghi HTTP thật của từng biến thể, không tự gán mọi lỗi là 401. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-AUTH-02 — Đăng ký với OTP hợp lệ và dữ liệu trùng/không hợp lệ

| Trường (MAN-AUTH-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC09; Xác thực và OTP |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh đăng ký với OTP hợp lệ và dữ liệu trùng/không hợp lệ. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Email QA mới có hộp thư kiểm soát; RegisterRequest fullName="QA Đăng ký", email=<emailQA>, phone="0901234567", password=<mật khẩu fixture >=6>, otp=<mã nhận thật>; biến thể email đã tồn tại, password 5/6 ký tự, otp 5/6/7 chữ số. |
| Các bước | 1. POST /api/auth/register/send-otp với emailOrPhone=emailQA.; 2. Lấy mã qua kênh QA hợp lệ, POST /api/auth/register.; 3. Đối chiếu account/customer/cart, thử dùng lại mã; lặp các biến thể. |
| Kết quả mong đợi / tiêu chí chấp nhận | Luồng hợp lệ tạo duy nhất account CUSTOMER và hồ sơ; password hash; OTP không dùng lại; trùng và đầu vào sai bị từ chối, không tạo dữ liệu dở dang. Không dùng debugOtp để kết luận email đã gửi. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-AUTH-03 — Đặt lại mật khẩu và ngăn tái sử dụng OTP

| Trường (MAN-AUTH-03) | Nội dung |
| --- | --- |
| Use Case / Module | UC03; Xác thực và OTP |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh đặt lại mật khẩu và ngăn tái sử dụng OTP. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Account QA ACTIVE có hộp thư riêng; POST /api/auth/forgot-password emailOrPhone=<emailQA>; VerifyOtpRequest emailOrPhone/otp; ResetPasswordRequest emailOrPhone/otp/newPassword. Mật khẩu được quản lý ngoài ảnh. |
| Các bước | 1. Gửi OTP và lấy mã qua hộp thư QA.; 2. POST /api/auth/verify-otp, sau đó /api/auth/reset-password.; 3. Đăng nhập lại với mật khẩu cũ/mới; dùng lại OTP; thử mã hết hạn. |
| Kết quả mong đợi / tiêu chí chấp nhận | Chỉ mã đúng còn hạn đổi mật khẩu; mật khẩu mới đăng nhập được, cũ bị từ chối; OTP đã dùng/hết hạn không đổi lần nữa. Kiểm tra JWT cũ riêng theo MAN-AUTH-04. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-AUTH-04 — Đăng xuất, token bị sửa và thay đổi quyền trong phiên

| Trường (MAN-AUTH-04) | Nội dung |
| --- | --- |
| Use Case / Module | UC02 / UC01 / UC84 / UC86; Xác thực và OTP |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh đăng xuất, token bị sửa và thay đổi quyền trong phiên. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Hai trình duyệt/tác nhân QA; token chỉ lưu an toàn ngoài báo cáo. Có phiên hợp lệ và biến thể JWT sửa chữ ký; token hết hạn; nhân viên vừa bị khóa/đổi vai trò. |
| Các bước | 1. POST /api/auth/logout với X-Requested-With: fetch; đọc thuộc tính xóa cookie.; 2. Gọi /api/auth/me khi không cookie, JWT bị sửa hoặc hết hạn.; 3. Thử token cấp trước lúc khóa/đổi quyền trong công cụ QA, ghi hành vi rồi so với chính sách phiên được nhóm xác nhận. |
| Kết quả mong đợi / tiêu chí chấp nhận | Logout xóa cookie trình duyệt; chữ ký sai/hết hạn bị từ chối. Source không có thu hồi JWT ngay: ghi hạn chế, không tự áp tiêu chí thu hồi chưa được phê duyệt; cần chốt yêu cầu để kết luận biến thể token cũ. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-AUTH-05 — Biên OTP, giới hạn gửi lại và số lần nhập sai

| Trường (MAN-AUTH-05) | Nội dung |
| --- | --- |
| Use Case / Module | UC03 / UC09; Xác thực và OTP |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh biên OTP, giới hạn gửi lại và số lần nhập sai. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Mỗi loại registration_otps/password_reset_otps: fixture expiryTime=now−1s/now+1s, used=true/false, failedAttempts=4/5; gửi lại tại 29s/31s. Mã 6 chữ số được lấy qua fixture an toàn. |
| Các bước | 1. Chuẩn bị từng trạng thái trong DB QA hoặc test harness có clock kiểm soát.; 2. Gửi xác minh với mã đúng/sai; gửi lại sau 29s và 31s.; 3. Đọc failed_attempts,is_used,expiry_time sau lỗi bằng transaction mới. |
| Kết quả mong đợi / tiêu chí chấp nhận | TTL 5 phút, cooldown 30 giây, tối đa 5 lần sai theo hằng source; dữ liệu hết hạn/đã dùng/chạm ngưỡng bị từ chối. Counter phải persist; BUG-05 đã xác minh riêng một lần sai, không thay kết quả toàn bộ ca biên. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ACCOUNT-01 — Hồ sơ và địa chỉ theo chủ sở hữu

| Trường (MAN-ACCOUNT-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC10 / UC11 / UC58 / UC59; Khách hàng |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh hồ sơ và địa chỉ theo chủ sở hữu. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | A/B QA; CustomerProfileRequest fullName="QA Hồ sơ", phone="0901234567", gender=FEMALE; address recipientName="QA", phone="0901234567",province="TP. Hồ Chí Minh",district="Quận 1",ward="Phường QA",addressDetail="Địa chỉ QA",isDefault=true; fullName rỗng/150/151 ký tự. |
| Các bước | 1. Nhân viên CUSTOMER_MANAGE GET /api/customers và /{A}; PUT /api/customers/{A}/profile bằng A.; 2. POST/GET/PUT/DELETE /api/customers/{A}/addresses[/addressId] với hai địa chỉ, đổi mặc định.; 3. A thử sửa địa chỉ B bằng addressId thật; chạy dữ liệu biên. |
| Kết quả mong đợi / tiêu chí chấp nhận | Dữ liệu hợp lệ lưu/đọc đúng; A không sửa B; rỗng/quá giới hạn bị từ chối; tiêu chí một địa chỉ mặc định cần kiểm tra cả nhánh cập nhật, không chỉ nhánh thêm. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ACCOUNT-02 — Lưu Beauty Profile qua API hiện có

| Trường (MAN-ACCOUNT-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC12; Khách hàng |
| Ưu tiên / cấp kiểm thử | P2; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh lưu Beauty Profile qua API hiện có. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | A/B QA; body skinType="DAU",skinConcerns="MUN",careNeeds="Duong am",preferences="QA",priceRangeInterest="200000-500000" (chuỗi QA, không khẳng định là enum bắt buộc). |
| Các bước | 1. PUT /api/customers/{A}/beauty-profile.; 2. GET lại với A, rồi thử truy cập bằng B. |
| Kết quả mong đợi / tiêu chí chấp nhận | Các trường chuỗi trả đúng và giới hạn chủ hồ sơ; không yêu cầu form Beauty Profile hay cá nhân hóa gợi ý vì source chưa có. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-WISH-01 — Yêu thích, loại trùng và chuyển giỏ

| Trường (MAN-WISH-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC14; Yêu thích |
| Ưu tiên / cấp kiểm thử | P2; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh yêu thích, loại trùng và chuyển giỏ. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | A QA; productId từ sản phẩm ACTIVE có SKU1 và một sản phẩm không có SKU bán được; quantity đích kiểm tra từ giỏ thật. |
| Các bước | 1. GET /api/wishlist; POST /api/wishlist/products/{productId} hai lần.; 2. GET /api/wishlist/product-ids; POST /api/wishlist/move-all-to-cart.; 3. DELETE /api/wishlist/products/{productId}; DELETE /api/wishlist. |
| Kết quả mong đợi / tiêu chí chấp nhận | Không nhân đôi sản phẩm; thao tác đúng tài khoản; chuyển giỏ tạo/cộng SKU hợp lệ; sản phẩm không chuyển được được xử lý theo response. Không suy ra tồn vật lý từ nhãn inStock chỉ dựa SKU ACTIVE. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-CATALOG-01 — Danh sách, tìm kiếm, lọc và sắp xếp

| Trường (MAN-CATALOG-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC04 / UC05 / UC06 / UC29; Danh mục sản phẩm |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh danh sách, tìm kiếm, lọc và sắp xếp. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Seed QA có Cocoon; keyword="Cocoon"/"cocoon"/"QA_NO_MATCH"; brandId/categoryId lấy từ API; priceRange=under300 (UI ghi dưới500000),300to600 (500000..1000000),above1000; sortBy=newest,featured,price-asc,price-desc; skinType="Da Dầu",concern="Trị Mụn". |
| Các bước | 1. Mở trang sản phẩm, ghi request GET /api/products và query do UI sinh.; 2. Thử tìm kiếm/brand/category/skinType/concern/priceRange và từng thứ tự sắp xếp được UI cung cấp.; 3. Đối chiếu tập ID và giá từ API; thử không có kết quả. |
| Kết quả mong đợi / tiêu chí chấp nhận | Tập hiển thị khớp bộ lọc và thứ tự; trạng thái rỗng rõ ràng; không ghi đã kiểm thử phân trang thật vì tham số page hiện không tạo phân trang dữ liệu. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-CATALOG-02 — Chi tiết sản phẩm, SKU, tồn chi nhánh và gợi ý liên quan

| Trường (MAN-CATALOG-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC07 / UC08 / UC13 / UC30 / UC41; Danh mục sản phẩm |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh chi tiết sản phẩm, SKU, tồn chi nhánh và gợi ý liên quan. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | GET /api/products/{productId} từ Cocoon; SKU-CCN-BD-70ML giá seed 265000; chọn từng SKU trả về; actual/held ở QA đo thực tế trước thao tác. |
| Các bước | 1. Mở chi tiết, chọn biến thể, so giá/SKU với response.; 2. GET /api/products/sku/{skuId}/stores, đối chiếu available theo chi nhánh.; 3. Xem sản phẩm liên quan; thử product/SKU ngừng bán trên fixture QA. |
| Kết quả mong đợi / tiêu chí chấp nhận | SKU/giá/tồn hiển thị đồng nhất response và DB; gợi ý chỉ theo category/brand như source, không khẳng định cá nhân hóa; ghi riêng rủi ro SKU INACTIVE/tồn chi nhánh ngừng hoạt động. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-CATALOG-03 — CRUD và trạng thái sản phẩm qua API quản trị

| Trường (MAN-CATALOG-03) | Nội dung |
| --- | --- |
| Use Case / Module | UC31; Danh mục sản phẩm |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh cRUD và trạng thái sản phẩm qua API quản trị. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Nhân viên CATALOG_MANAGE; brandId/categoryId hợp lệ; ProductRequest name="QA Product",description="QA",origin="VN",status=ACTIVE; biến thể tên rỗng/200/201, brandId không tồn tại. |
| Các bước | 1. POST /api/products, ghi ID; GET /api/products/{id}.; 2. PUT /api/products/{id}; PUT /{id}/deactivate rồi /activate.; 3. DELETE /api/products/{id}; quan sát soft delete/dependency theo source và kiểm tra quyền thiếu. |
| Kết quả mong đợi / tiêu chí chấp nhận | Tạo/cập nhật/đổi trạng thái đúng; input sai và thiếu quyền không đổi dữ liệu. Không yêu cầu SKU/ảnh/thuộc tính CRUD vì chưa có luồng đó; UI sản phẩm chỉ hỗ trợ tra cứu/lọc/trạng thái. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-CATALOG-04 — CRUD danh mục và thương hiệu

| Trường (MAN-CATALOG-04) | Nội dung |
| --- | --- |
| Use Case / Module | UC32 / UC33; Danh mục sản phẩm |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh cRUD danh mục và thương hiệu. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | CATALOG_MANAGE; BrandRequest name="QA Brand", CategoryRequest name="QA Category"; tạo tên riêng chưa dùng; thêm biến thể tên trùng/rỗng và bản ghi có sản phẩm phụ thuộc. |
| Các bước | 1. POST/GET/PUT /api/brands và /api/categories, dùng ID response.; 2. Thử /{id}/activate, /deactivate và DELETE.; 3. Đối chiếu trạng thái/lọc ACTIVE và lỗi dữ liệu phụ thuộc. |
| Kết quả mong đợi / tiêu chí chấp nhận | Lưu/hiển thị đúng; ràng buộc tên/dependency theo service được bảo toàn; thiếu CATALOG_MANAGE bị chặn; không xóa dây chuyền dữ liệu bán hàng ngoài quy định. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-CART-01 — Thêm, cộng gộp, cập nhật và xóa SKU trong giỏ

| Trường (MAN-CART-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC15 / UC16 / UC17 / UC18; Giỏ hàng |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh thêm, cộng gộp, cập nhật và xóa SKU trong giỏ. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | A QA; SKU-CCN-BD-70ML, giá 265000; tồn đủ; quantity lần lượt 1,1 rồi PUT quantity=3. cartItemId lấy từ CartResponse, không phải skuId. |
| Các bước | 1. POST /api/cart/items {skuId,quantity:1} hai lần; GET /api/cart.; 2. PUT /api/cart/items/{cartItemId}?quantity=3 (query parameter, không phải JSON body).; 3. DELETE dòng, sau đó DELETE /api/cart. |
| Kết quả mong đợi / tiêu chí chấp nhận | Một dòng cùng SKU có qty2 rồi3; subtotal=530000 rồi795000 nếu giá fixture giữ265000; xóa đúng dòng/tài khoản. Đây là oracle chưa phải kết quả quan sát. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-CART-02 — Giỏ hàng với số lượng không hợp lệ và vượt tồn

| Trường (MAN-CART-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC16 / UC17; Giỏ hàng |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh giỏ hàng với số lượng không hợp lệ và vượt tồn. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | SKU ACTIVE có tổng available=2 trên toàn hệ thống fixture QA; quantity=-1,0,1,2,3; SKU không tồn tại/inactive; cartItemId thuộc B. |
| Các bước | 1. Khôi phục giỏ trước từng request POST /api/cart/items (JSON) hoặc PUT /api/cart/items/{cartItemId}?quantity=... (query).; 2. Gửi các giá trị và thử A sửa dòng B.; 3. Đọc response, giỏ và tồn; so cảnh báo giao diện với phản hồi backend. |
| Kết quả mong đợi / tiêu chí chấp nhận | POST quantity<1 bị DTO validation từ chối; PUT quantity<=0 xóa dòng theo service hiện tại. Dòng khác chủ bị từ chối; SKU không bán được không được POST thêm; qty3 vượt tổng available2 bị từ chối. Không coi thêm giỏ là giữ hàng; tổng tồn của giỏ không bảo đảm một chi nhánh đủ toàn đơn. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ORD-01 — Công thức tiền và dữ liệu biên mã ưu đãi

| Trường (MAN-ORD-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC19 / UC20 / UC71 / UC78 / UC79; Đặt hàng và giữ tồn |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh công thức tiền và dữ liệu biên mã ưu đãi. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Fixture Cocoon qty1×265000; mã rỗng/LUNEA10/GIAM50K/FREESHIP. Fixture QA riêng với subtotal=499999,500000,500001 và1000000/1000001 để xét ngưỡng phí/cap, không coi các mức này là seed hiện có. |
| Các bước | 1. Chuẩn bị giá/giỏ riêng từng biến thể trong DB QA.; 2. POST /api/orders/preview với OrderRequest; ghi subtotal/discountAmount/shippingFee/totalAmount.; 3. Tạo đơn tương ứng, so DB và response với oracle; khôi phục fixture sau mỗi biến thể. |
| Kết quả mong đợi / tiêu chí chấp nhận | subtotal=sum(qty×giá SKU); LUNEA10=min(subtotal×10%,100000); GIAM50K=50000; phí=0 nếu subtotal>=500000, ngược lại30000; FREESHIP phí0; total=max(0,subtotal−discount+fee). Qty1 seed:295000/268500/245000/265000. Không có điều kiện hạn dùng/lượt dùng voucher trong source. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ORD-02 — Đặt COD, chọn chi nhánh và snapshot đơn

| Trường (MAN-ORD-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC21 / UC68; Đặt hàng và giữ tồn |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh đặt COD, chọn chi nhánh và snapshot đơn. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | A QA, SKU1 qty3; địa chỉ A; paymentMethod=COD; fulfillmentStoreId một chi nhánh đủ tất cả SKU; sau tạo thử sửa giá/tên SKU trên fixture riêng. |
| Các bước | 1. Mở /checkout, đối chiếu preview, POST /api/orders/checkout.; 2. Ghi orderId/orderCode/assignedStoreId, địa chỉ snapshot, giá item và xóa giỏ.; 3. Đọc orders/order_items/reservations/inventories; sửa catalog rồi đọc đơn cũ. |
| Kết quả mong đợi / tiêu chí chấp nhận | Đơn chỉ gán một chi nhánh đủ toàn bộ SKU; PENDING, HELD, giữ tồn đúng, snapshot không đổi theo catalog. Chỉ COD được hỗ trợ, không thêm test gateway thanh toán không tồn tại. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ORD-03 — Không có một chi nhánh đủ toàn bộ giỏ

| Trường (MAN-ORD-03) | Nội dung |
| --- | --- |
| Use Case / Module | UC21 / UC68 / UC55; Đặt hàng và giữ tồn |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh không có một chi nhánh đủ toàn bộ giỏ. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | QA có SKU1/SKU2, giỏ mỗi loại2; HCM available SKU1=2,SKU2=0; HN SKU1=0,SKU2=2; biến thể cửa hàng inactive; thêm fulfillmentStoreId chỉ định sai vùng/thiếu hàng. |
| Các bước | 1. Chụp tồn/giỏ trước thao tác.; 2. Gọi checkout và ghi phản hồi.; 3. Đọc lại đơn, địa chỉ, reservation, giỏ và mọi tồn liên quan. |
| Kết quả mong đợi / tiêu chí chấp nhận | Không chia ngầm thành nhiều chi nhánh, không đặt khi không có cửa hàng đủ; không lưu dở dang hoặc giữ tồn. Cần xác nhận chính sách ưu tiên tỉnh bằng kết quả thật. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ORD-04 — Không dùng địa chỉ nhận hàng của khách khác

| Trường (MAN-ORD-04) | Nội dung |
| --- | --- |
| Use Case / Module | UC21 / UC11; Đặt hàng và giữ tồn |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh không dùng địa chỉ nhận hàng của khách khác. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | A/B có địa chỉ riêng; A gửi OrderRequest customerAddressId=<addressId của B>, COD và giỏ hợp lệ; biến thể customerAddressId không tồn tại. |
| Các bước | 1. Ghi số đơn/giữ tồn trước request.; 2. POST /api/orders/checkout với A.; 3. Đọc response và kiểm tra DB không phát sinh đơn/giữ tồn trái phép. |
| Kết quả mong đợi / tiêu chí chấp nhận | Từ chối địa chỉ khác chủ/không tồn tại; không lấy thông tin B, không đổi tồn/giỏ. Không suy từ test mock sang DB thật. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ORD-05 — Mã không biết và lỗi preview trên giao diện

| Trường (MAN-ORD-05) | Nội dung |
| --- | --- |
| Use Case / Module | UC19 / UC78; Đặt hàng và giữ tồn |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh mã không biết và lỗi preview trên giao diện. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | voucherCode="QA_UNKNOWN"; giỏ265000; biến thể DevTools chặn request preview (thao tác QA cục bộ, không sửa production). |
| Các bước | 1. Áp mã QA_UNKNOWN tại checkout; ghi toast và API preview.; 2. Mô phỏng lỗi mạng preview; ghi tổng fallback.; 3. Tạo đơn trên QA nếu đủ điều kiện, đối chiếu số tiền server, không lấy toast làm bằng chứng tính tiền. |
| Kết quả mong đợi / tiêu chí chấp nhận | Theo source mã lạ discount0, total295000 ở fixture này; giao diện không được chứng minh mã hợp lệ chỉ vì response200. Lỗi preview không được làm client quyết định tiền đơn; ghi khác biệt thông báo thành issue cần chốt UX. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ORD-06 — Lịch sử, chi tiết và dữ liệu theo chủ đơn/chi nhánh

| Trường (MAN-ORD-06) | Nội dung |
| --- | --- |
| Use Case / Module | UC22 / UC23 / UC24 / UC60 / UC65 / UC66; Vòng đời đơn hàng |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh lịch sử, chi tiết và dữ liệu theo chủ đơn/chi nhánh. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | A/B mỗi người một đơn QA; HCM/HN mỗi nơi có đơn; nhân viên HCM ORDER_VIEW; mã/ID lấy từ tạo đơn thực. |
| Các bước | 1. GET /api/orders/my-orders, /{id}, /code/{orderCode}; A thử ID/mã của B.; 2. GET /api/orders/customer/{customerId}; kiểm tra quyền nhân viên.; 3. GET /api/admin/orders và /{id} với nhân viên từng chi nhánh; đối chiếu trạng thái UI. |
| Kết quả mong đợi / tiêu chí chấp nhận | Khách chỉ thấy đơn mình; nhân viên chỉ thấy chi nhánh được phân, trừ phạm vi quản trị hợp lệ. Trang /order-success được kiểm tra riêng ở QA-INT-14, không thay bằng kết quả endpoint khác. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ORD-07 — Chuyển trạng thái hợp lệ, sai thứ tự và lặp yêu cầu

| Trường (MAN-ORD-07) | Nội dung |
| --- | --- |
| Use Case / Module | UC67 / UC69 / UC73; Vòng đời đơn hàng |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh chuyển trạng thái hợp lệ, sai thứ tự và lặp yêu cầu. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Đơn QA PENDING, nhân viên ORDER_PROCESS đúng chi nhánh; chuỗi CONFIRMED,PREPARING,SHIPPING,COMPLETED; biến thể PENDING→COMPLETED; lặp SHIPPING/COMPLETED. |
| Các bước | 1. PUT /api/orders/{id}/status?status=... theo chuỗi; ghi sau từng bước.; 2. Khôi phục đơn mới cho bước nhảy không hợp lệ; gửi lại cùng trạng thái.; 3. Đối chiếu actual/held/reservation và timestamps. |
| Kết quả mong đợi / tiêu chí chấp nhận | Chỉ chuyển theo luồng source; SHIPPING trừ actual và giải held một lần; COMPLETED không trừ lần nữa; nhảy trạng thái không hợp lệ không lưu dở dang. So cả route admin để phát hiện lệch quyền. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ORD-08 — Hủy đơn theo tác nhân và trạng thái

| Trường (MAN-ORD-08) | Nội dung |
| --- | --- |
| Use Case / Module | UC25 / UC70; Vòng đời đơn hàng |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh hủy đơn theo tác nhân và trạng thái. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Đơn QA riêng cho mỗi PENDING/CONFIRMED/PREPARING/SHIPPING/COMPLETED/CANCELLED; customer chủ đơn/khác chủ; nhân viên ORDER_PROCESS đúng/khác chi nhánh. |
| Các bước | 1. Gọi PUT /api/orders/{id}/cancel và admin cancel bằng tác nhân phù hợp.; 2. Gửi lặp yêu cầu được phép một lần nữa.; 3. Đọc status,reservation,actual,held sau từng thao tác. |
| Kết quả mong đợi / tiêu chí chấp nhận | Khách chỉ hủy PENDING của mình; nhân viên được hủy PENDING/CONFIRMED/PREPARING trong chi nhánh; trạng thái sau giao không hủy; RELEASED đúng một lần, actual không đổi trước giao. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-INV-01 — Tồn khả dụng, hàng giữ và định nghĩa tồn thấp

| Trường (MAN-INV-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC49 / UC54 / UC55 / UC56; Tồn kho |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh tồn khả dụng, hàng giữ và định nghĩa tồn thấp. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | QA SKU actual=10,held=8,minimumStock=5, available=2; biến thể actual=5/6; nhân viên INVENTORY_VIEW. |
| Các bước | 1. GET /api/inventory/store/{storeId}, /sku/{skuId}/available và /low-stock.; 2. Đối chiếu inventories và tổng reservation HELD bằng truy vấn chỉ đọc.; 3. Ghi điều kiện tồn thấp thực tế, đối chiếu định nghĩa trong yêu cầu đã được nhóm duyệt. |
| Kết quả mong đợi / tiêu chí chấp nhận | API available=max(0,actual−held); đối chiếu cả on_hand_qty/reserved_qty để phát hiện dữ liệu actual<held thay vì chỉ nhìn available bị chặn ở0. Source lọc low-stock bằng actual<=minimum, không phải available. Chưa chốt định nghĩa thì ghi chênh lệch yêu cầu, không bịa FAIL. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-INV-02 — Điều chỉnh tồn hợp lệ và biên nhỏ hơn hàng giữ

| Trường (MAN-INV-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC51; Tồn kho |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh điều chỉnh tồn hợp lệ và biên nhỏ hơn hàng giữ. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Fixture actual10,held2; POST /api/inventory/adjust {storeId:HCM,skuId:SKU1,newQuantity:2/1/0/-1,reason:"QA stock adjustment"}; INVENTORY_ADJUST. |
| Các bước | 1. Khôi phục tồn trước mỗi giá trị.; 2. Gửi điều chỉnh và đọc lại actual/held; thử thiếu reason theo validation/service thực tế.; 3. Thử người không quyền; cross-branch đã có QA-INT-11 nhưng cần chạy E2E riêng. |
| Kết quả mong đợi / tiêu chí chấp nhận | Điều chỉnh hợp lệ lưu đúng; số âm hoặc thấp hơn held phải bị chặn theo business rule; không đổi held, không phá reservation. Ghi HTTP/thông báo thật, không mặc định mọi lỗi đều403. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-PUR-01 — Nhà cung cấp qua API

| Trường (MAN-PUR-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC43 / UC44; Nhập hàng |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh nhà cung cấp qua API. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | PURCHASE_MANAGE; SupplierRequest name="QA Supplier",phone="0901234567",email="qa-supplier@example.test",address="Địa chỉ QA",status=ACTIVE; ID trả về. |
| Các bước | 1. POST /api/suppliers; GET danh sách và /{id}.; 2. PUT /{id}, DELETE /{id}; thử tên rỗng và thiếu quyền. |
| Kết quả mong đợi / tiêu chí chấp nhận | Thông tin cập nhật đúng; deactivate theo source; lỗi input/quyền không đổi dữ liệu. Không giả định có màn hình tìm kiếm nhà cung cấp khi chỉ có API list/get. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-PUR-02 — Nhập hàng, xác nhận và ngăn cộng tồn hai lần

| Trường (MAN-PUR-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC45 / UC46 / UC47 / UC48 / UC50; Nhập hàng |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh nhập hàng, xác nhận và ngăn cộng tồn hai lần. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | PURCHASE_MANAGE; supplier ACTIVE, receivingStoreId=HCM; items SKU1 qty2 unitPrice100000; actual ban đầu10,held2. |
| Các bước | 1. POST /api/purchase-orders; ghi DRAFT và ID.; 2. GET danh sách và /{id}; PUT /{id}/confirm.; 3. Gọi confirm lần hai; đọc actual/held/trạng thái sau mỗi lần. |
| Kết quả mong đợi / tiêu chí chấp nhận | Phiếu tổng200000; xác nhận cộng actual10→12, held2 không đổi; xác nhận lặp không cộng nữa. Không gọi API cập nhật DRAFT không tồn tại; rollback lỗi dòng sau đã có QA-INT-06. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-XFER-01 — Tạo, xuất và nhận điều chuyển đúng một lần

| Trường (MAN-XFER-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC53; Điều chuyển |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh tạo, xuất và nhận điều chuyển đúng một lần. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | INVENTORY_ADJUST; nguồn HCM actual10,held2; đích HN actual10,held2; SKU1qty2; biến thể sourceStoreId=destinationStoreId, qty0. |
| Các bước | 1. POST /api/stock-transfers với sourceStoreId,destinationStoreId,items.; 2. PUT /{id}/ship, rồi /receive; GET lại.; 3. Gửi lặp ship/receive và biến thể nguồn=đích; đối chiếu hai tồn. |
| Kết quả mong đợi / tiêu chí chấp nhận | Luồng thực tế PENDING→IN_TRANSIT→RECEIVED; nguồn trừ đúng2 khi ship, đích cộng2 khi receive, held không đổi; không trừ/cộng lặp; qty0 bị validation từ chối. Source chưa chặn nguồn=đích: biến thể này cần chốt quy tắc nghiệp vụ, không ghi đã có kiểm tra. PENDING_EXPORT chỉ có trong enum, không có bước chuyển tương ứng tại service. Không thêm bước cancel chưa có. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-RETURN-01 — Tạo yêu cầu trả theo chủ đơn, trạng thái và số lượng

| Trường (MAN-RETURN-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC26 / UC62; Trả hàng |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh tạo yêu cầu trả theo chủ đơn, trạng thái và số lượng. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Đơn QA COMPLETED thuộc A, orderItem qty2; POST /api/returns {orderId,reason:"QA return",items:[{orderItemId,quantity:1}]}; biến thể qty0/2/3, đơn B/chưa hoàn thành, yêu cầu trước chưa bị REJECTED. |
| Các bước | 1. Tạo request hợp lệ, ghi ID.; 2. Lặp biến thể bằng fixture riêng và đọc tổng quantity đã trả không bị từ chối.; 3. GET /api/returns/customer/{A} và /order/{id}, kiểm tra người khác. |
| Kết quả mong đợi / tiêu chí chấp nhận | Chỉ chủ đơn COMPLETED được yêu cầu; tổng trả không vượt đã mua sau khi loại REJECTED; trả lỗi không lưu dở dang; không suy có hoàn tiền qua cổng thanh toán. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-RETURN-02 — Duyệt trả và nhập lại tồn một lần

| Trường (MAN-RETURN-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC63 / UC74; Trả hàng |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh duyệt trả và nhập lại tồn một lần. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Nhân viên ROLE_EMPLOYEE + ORDER_PROCESS; request QA quantity1; trạng thái REQUESTED/APPROVED/PROCESSING/REJECTED/COMPLETED; nhân viên khác chi nhánh là biến thể riêng. |
| Các bước | 1. PUT /api/returns/{id}/status?status=APPROVED rồi PROCESSING rồi COMPLETED.; 2. Gửi lặp COMPLETED; thử nhánh REJECTED trên request khác.; 3. Đọc actual của chi nhánh đơn và kiểm tra quyền/chi nhánh qua API. |
| Kết quả mong đợi / tiêu chí chấp nhận | Hoàn tất tăng tồn đúng quantity một lần, không tăng cho REJECTED; thứ tự trạng thái theo source; người không quyền bị chặn. Rủi ro thiếu StoreScope của returns chưa có runtime chứng minh trong đợt này. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-REVIEW-01 — Đánh giá, giới hạn sao và kiểm duyệt

| Trường (MAN-REVIEW-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC27 / UC28 / UC37; Đánh giá sản phẩm |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh đánh giá, giới hạn sao và kiểm duyệt. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | A có đơn COMPLETED chứa product; POST /api/reviews {productId,orderId,rating:5,comment:"QA review"}; rating0/1/5/6, sai product/đơn B, gửi trùng; ReviewStatus=PENDING,VISIBLE,HIDDEN. |
| Các bước | 1. Gửi đánh giá hợp lệ, GET /api/reviews/my-reviews và /product/{id}.; 2. Gửi biến thể; nhân viên PUT /api/reviews/{id}/status với trạng thái hợp lệ.; 3. Đọc lại danh sách công khai trước/sau duyệt; thử customer cập nhật moderation. |
| Kết quả mong đợi / tiêu chí chấp nhận | Chỉ đánh giá hàng đã mua hoàn tất, một lần/product/order; sao1..5; trạng thái chưa duyệt không công khai; chỉ nhân viên duyệt theo quyền hiện có. Chưa có yêu cầu CATALOG_MANAGE ở controller moderation: cần rà lại mức phân quyền mong muốn. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-EMP-01 — Quản lý nhân viên, trạng thái và chi nhánh

| Trường (MAN-EMP-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC82 / UC83 / UC84 / UC85; Nhân viên và vai trò |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh quản lý nhân viên, trạng thái và chi nhánh. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | EMPLOYEE_MANAGE hoặc ROLE_ADMIN; EmployeeRequest fullName="QA Employee",email="qa-employee@example.test",username="qa-employee",password=<fixture>=6,roleIds=[ID thật],storeIds=[HCM]; biến thể roleId/storeId không tồn tại. |
| Các bước | 1. POST/GET/PUT /api/employees; ghi mapping employee_roles/employee_stores theo entity thực tế.; 2. PUT /{id}/deactivate và /activate; kiểm tra login mới.; 3. Đổi chi nhánh HCM→HN rồi chạy request từng chi nhánh với phiên mới. |
| Kết quả mong đợi / tiêu chí chấp nhận | CRUD/khóa/mở đúng; mapping khớp yêu cầu; không bỏ qua ID sai một cách khó phát hiện; truy cập bị giới hạn theo quyền/chi nhánh đã gán. Token cũ xử lý theo MAN-AUTH-04, không coi khóa là thu hồi JWT ngay. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-STORE-01 — Chi nhánh, giờ hoạt động và tọa độ qua API

| Trường (MAN-STORE-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC38 / UC39 / UC40 / UC42; Chi nhánh |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh chi nhánh, giờ hoạt động và tọa độ qua API. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | STORE_MANAGE; StoreRequest name="QA Store",address="Địa chỉ QA",province="TP. Hồ Chí Minh",openTime="08:00:00",closeTime="21:00:00",latitude=10.77,longitude=106.70,status=ACTIVE; name rỗng. |
| Các bước | 1. POST/GET/PUT /api/stores và /{id}; đối chiếu openTime/closeTime/latitude/longitude.; 2. Gọi activate/deactivate; xem danh sách public/checkout.; 3. Nếu gửi operatingHours, ghi trường nào thực sự được lưu, không giả định mapper hỗ trợ. |
| Kết quả mong đợi / tiêu chí chấp nhận | Lưu các trường entity hỗ trợ; input sai bị chặn, trạng thái ảnh hưởng danh sách theo service. Có dữ liệu tọa độ không đồng nghĩa có bản đồ/chỉ đường hoàn chỉnh trên UI. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-ROLE-01 — Danh sách vai trò và gán vai trò có thật

| Trường (MAN-ROLE-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC86; Nhân viên và vai trò |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh danh sách vai trò và gán vai trò có thật. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | GET /api/roles bằng EMPLOYEE_MANAGE/ROLE_ADMIN; roleIds trả về được gán qua EmployeeRequest; hai phiên nhân viên với quyền khác nhau. |
| Các bước | 1. Đọc roles/permissions trả về, che dữ liệu nhạy cảm.; 2. PUT /api/employees/{id} với roleIds hợp lệ và đăng nhập lại.; 3. Thử endpoint chức năng có/không quyền. |
| Kết quả mong đợi / tiêu chí chấp nhận | Quyền phiên mới khớp vai trò được gán; không có API CRUD role/permission nên không thiết kế request tưởng tượng. Thay đổi token cũ phải ghi hạn chế theo MAN-AUTH-04. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-PQ-01 — Người chưa đăng nhập và bộ lọc yêu cầu ghi

| Trường (MAN-PQ-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC01 / UC86; Phân quyền và chi nhánh |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh người chưa đăng nhập và bộ lọc yêu cầu ghi. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | GET /api/cart, /api/admin/orders; GET /checkout; POST /api/cart/items không auth, lần1 không header, lần2 X-Requested-With: fetch; Bearer hợp lệ chỉ quản lý trong công cụ QA. |
| Các bước | 1. Gửi các GET chưa đăng nhập, ghi status/location.; 2. So POST có/không X-Requested-With bằng cùng body.; 3. Phân biệt403 do XRequestedWithFilter với401 chưa xác thực; thử Bearer theo bộ lọc thật. |
| Kết quả mong đợi / tiêu chí chấp nhận | API không xác thực trả401 khi qua được bộ lọc; trang cần login chuyển302; ghi thiếu header có thể403 trước auth. Không dùng403 thiếu header làm bằng chứng phân quyền chức năng. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-PQ-02 — Ma trận quyền chức năng và đường truy cập thay thế

| Trường (MAN-PQ-02) | Nội dung |
| --- | --- |
| Use Case / Module | UC31 / UC39 / UC44 / UC51 / UC67 / UC69 / UC83 / UC86; Phân quyền và chi nhánh |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh ma trận quyền chức năng và đường truy cập thay thế. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Nhân viên cùng HCM chỉ INVENTORY_VIEW; phiên khác có CATALOG_MANAGE,STORE_MANAGE,PURCHASE_MANAGE,INVENTORY_ADJUST,ORDER_PROCESS,EMPLOYEE_MANAGE tách biệt; JWT thật qua login QA. |
| Các bước | 1. Với mỗi phiên, gửi một thao tác module được phép và một thao tác thiếu quyền.; 2. So /api/orders/{id}/status với /api/admin/orders/{id}/status; so page/API.; 3. Đọc DB để chắc chắn request bị từ chối không đổi dữ liệu. |
| Kết quả mong đợi / tiêu chí chấp nhận | Không vượt quyền chức năng qua route khác; status đúng và DB nguyên vẹn. QA-INT-12 đã xác minh một đường thiếu quyền; chưa bao phủ mọi cặp vai trò/endpoint. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-PQ-03 — Giới hạn chi nhánh ngoài module đơn hàng

| Trường (MAN-PQ-03) | Nội dung |
| --- | --- |
| Use Case / Module | UC45 / UC49 / UC51 / UC53 / UC63 / UC85; Phân quyền và chi nhánh |
| Ưu tiên / cấp kiểm thử | P0; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh giới hạn chi nhánh ngoài module đơn hàng. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Nhân viên chỉ HCM, có đúng quyền từng module; HN có inventory/PO/transfer/return fixture và ID thật; cả GET lẫn POST/PUT. |
| Các bước | 1. Thử đọc/điều chỉnh inventory HN, tạo/xác nhận PO HN, ship/receive transfer ngoài phạm vi, xem/xử lý return HN.; 2. Mỗi bước lấy snapshot DB trước/sau, gửi X-Requested-With cho request ghi.; 3. Thử admin có phạm vi rộng làm đối chứng. |
| Kết quả mong đợi / tiêu chí chấp nhận | Phải tôn trọng chính sách chi nhánh đã thống nhất trên server. Inventory adjust đã FAIL tại QA-INT-11; PO/transfer/returns mới là rủi ro source, chưa được ghi FAIL runtime. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-DASH-01 — Chỉ số tổng quan theo đơn hoàn tất và bộ lọc chi nhánh

| Trường (MAN-DASH-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC88 / UC89; Tổng quan quản trị |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh chỉ số tổng quan theo đơn hoàn tất và bộ lọc chi nhánh. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | QA có đơn COMPLETED/PENDING/CANCELLED ở HCM/HN với total_amount ghi từ DB; ít nhất một SKU low-stock theo actual<=minimum. |
| Các bước | 1. Mở dashboard admin đúng quyền; ghi API requests thực UI sử dụng.; 2. Chọn từng chi nhánh và đối chiếu số đơn/doanh thu với truy vấn chỉ đọc.; 3. Đối chiếu low-stock; kiểm tra dữ liệu nhân viên ngoài chi nhánh. |
| Kết quả mong đợi / tiêu chí chấp nhận | Doanh thu theo đơn COMPLETED và số đơn/low-stock khớp định nghĩa source; lọc chi nhánh không tiết lộ dữ liệu vượt quyền. Không ghi có báo cáo thời gian/xuất Excel/best-seller khi source không có. |
| Kết quả thực tế | Chưa thực thi. |
| Trạng thái | NOT RUN |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |

## MAN-EMAIL-01 — Gửi OTP qua SMTP và nhận thư thực tế

| Trường (MAN-EMAIL-01) | Nội dung |
| --- | --- |
| Use Case / Module | UC03 / UC09; Xác thực và OTP |
| Ưu tiên / cấp kiểm thử | P1; Thủ công/API E2E hoặc fixture DB riêng dự kiến |
| Mục tiêu | Xác minh gửi OTP qua SMTP và nhận thư thực tế. |
| Điều kiện trước | F-MAN và điều kiện QA chung nêu trên; tác nhân/quyền, đối tượng và trạng thái ban đầu theo Dữ liệu đầu vào. Khôi phục fixture riêng cho mỗi biến thể. |
| Dữ liệu đầu vào | Cần SMTP QA được phép dùng, hộp thư nhận thuộc nhóm, kiểm soát số lần gửi; không dùng email minh họa @example.test để xác nhận giao thư. |
| Các bước | 1. Cấu hình SMTP QA ngoài tài liệu, ghi loại môi trường và thời điểm không ghi secret.; 2. Gửi OTP đăng ký và reset qua giao diện.; 3. Đối chiếu message delivery/hộp thư, thời gian còn hạn và xác minh một lần; che toàn bộ mã trong ảnh công khai. |
| Kết quả mong đợi / tiêu chí chấp nhận | Thư đến đúng người nhận và mục đích, mã còn hạn dùng một lần; không có OTP trong response/log chia sẻ. EmailService no-op trong integration không chứng minh SMTP. |
| Kết quả thực tế | Chưa có cấu hình SMTP QA và hộp thư nhận được chỉ định để kiểm thử giao thư thật; integration chủ động dùng no-op, không gửi ra ngoài. |
| Trạng thái | BLOCKED |
| Minh chứng | Chưa có; mã thu thập theo Test Case ID tại MINH_CHUNG_CAN_THU_THAP.md. |
| Ghi chú lỗi | Chưa ghi nhận defect runtime riêng ở ca này; không đồng nghĩa toàn module đạt. |
