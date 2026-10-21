> Cập nhật bản chương hiện hành: [Rà soát 13 ca và xác minh 5 integration test](RA_SOAT_13_CA_VA_KET_QUA_TICH_HOP.md). Word đã tối ưu còn **13 trang gồm 9 vùng ảnh**; chạy lại **5/5 PASS**, giữ 13 ca giao diện NOT RUN. Nội dung bên dưới là audit và lịch sử đã có, không bị sửa thành kết luận toàn hệ thống đạt.

# Audit chương 6.2 và cơ sở kiểm thử LUNEA

Ngày rà soát: **21/10/2026**, múi giờ Asia/Ho_Chi_Minh. Bản source đối chiếu: commit `6858efca55623eb55b4cca1b64d06a5dc6767083` **cùng các thay đổi chưa commit trong workspace**. Bản chương cũ được lưu trong `ban_goc/` trước khi chỉnh sửa.

Bản audit này được lập trước khi thay chương Word. Kết quả các phép kiểm tra bổ sung sẽ được ghi trong hồ sơ bàn giao; một nhận xét từ source không tự động trở thành FAIL.

## 1. Phạm vi đã khảo sát

- 236 tệp Java production: controller trang, REST controller, service/interface, repository, entity, DTO, enum, mapper, cấu hình, bảo mật và xử lý lỗi.
- 30 template Thymeleaf; mã JavaScript nhúng và `static/js/api.js`; tài nguyên CSS/JSON phục vụ giao diện, địa chỉ. Danh mục tệp và SHA-256 được lưu để truy vết bản source.
- 15 tệp test ban đầu, cấu hình test/H2, POM và Maven Wrapper.
- Toàn bộ nội dung đoạn văn/bảng của DOCX hiện tại; danh sách UC01–UC91 trong bảng use case của báo cáo gốc, PDF trang 48–61.

Việc đọc cấu hình được thực hiện với giá trị nhạy cảm được che trong đầu ra và tài liệu. Không sử dụng thông tin đăng nhập PostgreSQL/SMTP thật để tự ý phát sinh giao dịch.

## 2. Các vấn đề của tài liệu hiện tại

| ID | Mức độ | Bằng chứng/hiện trạng | Đề xuất xử lý |
|---|---|---|---|
| DOC-01 | Cao | Chương chỉ mô tả năm nhóm chức năng, chưa có Test Case ID hay liên kết UC; nhiều ngoại lệ nằm chung một đoạn. | Thiết kế ca có dữ liệu, bước, oracle, trạng thái và minh chứng riêng; lập ma trận UC → module → ca. |
| DOC-02 | Cao | DOCX có 9 khung `[CHÈN HÌNH …]`, 0 ảnh thực tế trong package Word; các chú thích chưa có bằng chứng đi kèm. | Xóa toàn bộ khung/chú thích ảnh chưa có khỏi bản chương cải thiện; chuyển thành danh sách cần thu thập. |
| DOC-03 | Cao | Tất cả kết quả hiện chỉ ghi “Chưa thực hiện” theo nhóm, không phân biệt ca, biến thể, chưa chạy và bị chặn. | Dùng PASS/FAIL/NOT RUN/BLOCKED ở cấp ca; tách trạng thái thực thi với mức triển khai chức năng. |
| DOC-04 | Trung bình | Môi trường local/PostgreSQL là môi trường dự kiến, chưa có phiên bản/lần chạy/minh chứng kiểm thử; dữ liệu seed chưa phải dữ liệu DB đã quan sát. | Ghi riêng môi trường thực chạy JDK/Maven/H2 và môi trường PostgreSQL/E2E cần xác nhận; fixture test phải tái lập được. |
| DOC-05 | Trung bình | Giá seed Cocoon 70ml 265.000 và các tổng tiền 295.000/268.500/245.000/265.000 khớp công thức source; tồn 30/0 chỉ là ví dụ. | Giữ phép tính dưới nhãn oracle/fixture; không gán PASS hay gọi là số liệu quan sát của PostgreSQL. |
| DOC-06 | Cao | Chưa có kết quả riêng cho OTP, rollback, giữ tồn đồng thời, vòng đời giao đơn, nhập hàng, điều chuyển, quản trị và các quyền ngoài đơn hàng. | Mở phạm vi thiết kế theo module thực có; ưu tiên tính toàn vẹn và quyền truy cập; ghi rõ phần chưa triển khai. |
| DOC-07 | Trung bình | DOCX có 5 ngắt trang cứng, 12 bảng, trong đó 9 bảng làm khung ảnh. XML có bảo vệ dòng bảng nhưng chưa có bằng chứng dàn trang từ Word. | Bỏ ngắt trang ép theo bản sáu trang, giữ dòng bảng không tách và lặp tiêu đề; xuất bản kiểm tra bố cục bằng Word nếu môi trường cho phép. Không khẳng định bảng đã bị ngắt khi chưa quan sát. |
| DOC-08 | Cao | Báo cáo gốc có 91 UC, nhưng nhiều UC chưa có endpoint/service/UI; các kiểu DTO/enum/entity tồn tại không đồng nghĩa có chức năng hoàn chỉnh. | Phân loại IMPLEMENTED/PARTIAL/NOT IMPLEMENTED với đường dẫn source và giới hạn; UC chưa triển khai không đưa vào bộ kiểm thử chính. |

## 3. Kết quả nền đã thực thi

Lệnh: `./mvnw -o test`, sử dụng JDK **21.0.10**, Spring Boot **4.1.1**, JUnit Jupiter **6.0.3**, Mockito **5.23.0**, Maven Surefire **3.5.6**, H2 **2.4.240**. Java mặc định của máy là 25, nên lần kiểm tra đặt JAVA_HOME rõ ràng sang JDK 21.

Lần đầu trong sandbox: 134 tests, 2 failures, 66 errors, 0 skipped; phần lớn errors do Mockito không tự gắn được agent. Đây là trở ngại môi trường cho các ca liên quan, không được quy thành 66 lỗi phần mềm. Sau khi được cho phép chạy ngoài sandbox, lần chạy lại có **134 tests, 2 failures, 0 errors, 0 skipped**, tương ứng 132 test không báo lỗi/thất bại. Thời gian Maven 1 phút 42 giây; thời gian tiến trình đo bằng `time -p` là 108,80 giây. Hoàn tất lúc **01:53:24 ngày 21/10/2026 (+07:00)**.

Hai failures có bằng chứng thực thi:

| Test | Mong đợi trong test | Quan sát | Phân tích |
|---|---|---|---|
| `OrderEndpointsTest.testAdminOrdersPageAccess` | 200 với chỉ ROLE_EMPLOYEE | 403 | Test dùng bộ quyền cũ; trang hiện yêu cầu ORDER_VIEW/ORDER_PROCESS/ROLE_ORDER_STAFF/ROLE_ADMIN. Chưa có căn cứ nới production để test đạt. |
| `WebPageControllersTest.testAdminPagesForEmployee` | 200 với ROLE_EMPLOYEE ở trang admin được chọn | 403 | Cần cập nhật fixture quyền và chia ca được phép/bị chặn theo chức năng. |

JUnit thực chạy là **Jupiter 6**, không ghi JUnit 5 theo thói quen hoặc theo tài liệu mẫu. Số 134 ở trên lấy từ Surefire của lần chạy, không lấy bằng cách đếm `@Test` trong source.

## 4. Rủi ro phần mềm cần kiểm chứng thêm

Các dòng dưới đây là kết quả rà soát source, **chưa được đánh dấu FAIL ở thời điểm audit ban đầu**.

| ID tạm | Mức độ | Rủi ro và vị trí source | Kiểm tra/đề xuất |
|---|---|---|---|
| RISK-01 | Nghiêm trọng | `OrderServiceImpl.placeOrder` đọc rồi gán `heldQuantity`; `InventoryRepository` không có khóa, entity không có version. | Hai giao dịch cùng mua lượng cuối; đối chiếu số đơn, tổng reservation và hàng giữ; cân nhắc khóa/conditional update sau khi nhóm chấp thuận sửa logic. |
| RISK-02 | Cao | `OtpServiceImpl` lưu failedAttempts/isUsed rồi ném BusinessException trong transaction mặc định. | Đọc lại OTP bằng giao dịch mới sau lỗi; xác minh bộ đếm thực sự được lưu, không chỉ verify mock.save. |
| RISK-03 | Cao | `SendOtpResponse.debugOtp` được gán giá trị và endpoint gửi OTP công khai; log cũng chứa mã. | Kiểm tra response có lộ mã không; tách hành vi debug theo môi trường khi sửa được phê duyệt. |
| RISK-04 | Nghiêm trọng | `PurchaseOrderServiceImpl`, `StockTransferServiceImpl` ném checked Exception sau khi đã lưu/đổi dữ liệu; mặc định @Transactional không rollback loại này. | Cố tình làm dòng sau thất bại; kiểm tra không để phiếu dở dang hoặc trừ kho một phần. |
| RISK-05 | Cao | `StockTransferServiceImpl.confirmShipment` so sánh tồn thực tế, chưa trừ hàng giữ. | Xuất điều chuyển nhiều hơn tồn khả dụng nhưng ít hơn tồn thực tế; phải bảo toàn hàng đã giữ cho đơn. |
| RISK-06 | Cao | API inventory/purchase/transfer nhận storeId nhưng thiếu StoreScope tại các luồng đã đọc. | Nhân viên HCM thử xem/điều chỉnh dữ liệu HN; tách quyền chức năng với phạm vi chi nhánh. |
| RISK-07 | Cao | `/api/admin/orders/**` được bảo vệ bởi ROLE_EMPLOYEE; controller không có kiểm tra ORDER_PROCESS tương đương route `/api/orders/{id}/status`. | Nhân viên có chi nhánh nhưng không được xử lý đơn thử đổi trạng thái qua đường admin. |
| RISK-08 | Cao | `CheckoutPageController.orderSuccess` tải theo orderCode mà không kiểm tra chủ đơn. | Khách A mở trang thành công theo mã đơn B; kiểm tra không trả dữ liệu đơn của B. |
| RISK-09 | Cao | Checkout chưa tái kiểm tra trạng thái sản phẩm/SKU sau khi đã nằm trong giỏ. | Ngừng bán SKU trước bước checkout; kiểm tra đơn và giữ tồn không được tạo. |
| RISK-10 | Trung bình | Frontend checkout có fallback tự tính khi API preview lỗi và thông báo “thành công” với response mã lạ giảm 0. | Kiểm tra request/response và đơn lưu; không kết luận chỉ dựa toast. |
| RISK-11 | Cao | JWT lấy quyền từ claims; logout xóa cookie, không có danh sách thu hồi token; trạng thái khóa/đổi quyền chưa được kiểm tra lại mỗi request. | Phân biệt logout phía trình duyệt với thu hồi token; đánh giá theo yêu cầu phiên/quyền của nhóm trước khi kết luận lỗi. |

Nguồn dòng cụ thể, trường hợp tái hiện và trạng thái xác minh cuối được ghi trong danh sách lỗi riêng. Không sửa business logic production trong đợt rà soát này.

## 5. Hướng cải thiện và tiêu chí bàn giao

1. Chương Word được tổ chức theo mục tiêu/phạm vi, môi trường/fixture, chiến lược, ca theo module, kết quả thực chạy, độ bao phủ và hạn chế.
2. Mỗi ca thiết kế có ID, UC/yêu cầu, ưu tiên, điều kiện, Input cụ thể, bước lặp lại, oracle, thực tế, trạng thái và minh chứng; giữ riêng nhận xét source với bằng chứng runtime.
3. Ma trận bao gồm đủ UC01–UC91; UC chưa triển khai có lý do và không được tính như ca NOT RUN của một API tưởng tượng.
4. Chạy các phép kiểm tra bổ sung ở test source và CSDL riêng; bảo toàn snapshot source production. Kết quả H2 không tự chứng minh behavior trên PostgreSQL hay trình duyệt.
5. Ảnh chưa có nằm trong danh sách thu thập; chương cải thiện không có ảnh giả hoặc khung placeholder. Các bảng không tách dòng, có tiêu đề lặp và tham chiếu hồ sơ chi tiết.

## 6. Cập nhật sau thực thi và kiểm tra bố cục

Word đã xuất đúng bản chương cũ (bản sao QA có tên riêng) thành PDF **8trang**. Trang2 gần như chỉ có hai khung ảnh; bảng tổng hợp ở cuối bị tách giữa trang7–8, dù tiêu đề cột được lặp. Do đó xác nhận DOC-07 là vấn đề bố cục có quan sát thực, không còn chỉ suy luận từ XML. DOCX cũ có0ảnh và9khung chờ, tất cả kết quả theo nhóm vẫn “Chưa thực hiện”; không có cơ sở cáo buộc bản cũ đã giả PASS.

Sau audit ban đầu,15ca integration đã được bổ sung và thực chạy trênH2/PostgreSQL. Final H2:149tests,137PASS,12failures,0errors,0skipped; PostgreSQL:15tests,5PASS,10failures,0errors,0skipped.10failures mới xác nhận10defect phần mềm;2failures còn lại là fixture quyền cũ. Rủi ro source chưa chạy được tách riêng. Xem LOI_PHAN_MEM.md và KET_QUA_KIEM_THU_TU_DONG.md; không sửa production trong đợt này.

Bản chương mới thay khung ảnh bằng bảng kết quả có ID/UC/expected/actual/status/minh chứng; danh sách cần chụp nằm riêng, không dùng placeholder. Toàn bộ91UC được kiểm kê; các chức năng chưa có không đưa vào danh sách test chính. Thông tin kiểm tra bố cục cuối và SHA bảo toàn source được ghi trong NGHIEM_THU_TAI_LIEU.md sau khi xuất Word kiểm tra. Bản chương là báo cáo trạng thái thực tế, không phải xác nhận nghiệm thu phần mềm đạt.

## 7. Đóng kiểm tra chất lượng tài liệu

Bản cuối đã xuất thành công bằng Microsoft Word: chương chính20trang, phụ lục52trang. Không phát hiện ô bảng mất nội dung, dòng bảng tách trang hoặc chữ tràn trang trong phép đối chiếu DOCX–PDF; phôngPDF chỉ có họTimes New Roman. Chương không có ảnh/placeholder. Chi tiết và giới hạn được ghi trong NGHIEM_THU_TAI_LIEU.md; đây không phải đóng10defect phần mềm.
