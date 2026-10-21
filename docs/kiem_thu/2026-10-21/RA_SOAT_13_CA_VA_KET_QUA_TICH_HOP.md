# Rà soát 13 ca chức năng và xác minh 5 integration test

Phạm vi lần này là chỉnh tài liệu và chạy lại các test hiện có; không sửa source production hoặc test. Bản Word tiếp tục giữ 13 ca giao diện và 9 vị trí ảnh. Các trạng thái giao diện vẫn là NOT RUN do chưa có biên bản thực thi mới.

## Căn cứ Use Case và mã nguồn

Đã đối chiếu trực tiếp `Nhom01_BaoCaoCuoiKy_review.pdf`: các trang PDF 48, 50–51, 57–58 và 60 chứa danh sách UC liên quan; trang PDF 143 nêu việc kiểm tra phạm vi quyền khi xem đơn; trang PDF 165 mô tả UC85 là thao tác **phân nhân viên vào chi nhánh**. Quy định QLNV-QĐ3/4 nằm ở trang PDF 35. Số trang PDF tính từ 1, khác số trang in trong báo cáo.

| Ca | UC trực tiếp | Đối chiếu source và kết luận rà soát |
| --- | --- | --- |
| KT-DN-01 | UC01 | `auth/login.html` gửi POST `/api/auth/login`; `AuthRestController.login`, `AccountServiceImpl.login` cấp cookie qua `AuthCookieFactory`. Trang bảo vệ yêu cầu ROLE_CUSTOMER. Giữ bước mở lại lịch sử và tải lại; không khẳng định có HTTP session vì SecurityConfig dùng STATELESS. |
| KT-DN-02 | UC01 | `AccountServiceImpl.login` ném INVALID_CREDENTIALS khi xác thực sai; ErrorCode và GlobalExceptionHandler ánh xạ HTTP 401. Sửa tiêu chí thành HTTP 401/mã lỗi và chuyển về login khi mở trang bảo vệ; không bắt buộc toast lỗi vì nhánh 401 của api.js không hiển thị toast trên trang đăng nhập. |
| KT-SP-01 | UC05, UC07 | ProductPageController nhận `keyword`; ProductServiceImpl tìm theo tên/thương hiệu và trường liên quan. `selectVariant` của product-detail cập nhật giá và SKU. DataSeeder có SKU-CCN-BD-70ML, giá bán 265.000 và giá niêm yết 295.000 đồng. Giữ giá bán làm căn cứ tính tiền. |
| KT-SP-02 | UC05 | `searchAndFilter` và trạng thái rỗng trong product-list hỗ trợ tìm không có kết quả. Xác nhận từ khóa QA_NO_MATCH_20261021 không có trong dữ liệu trước khi thử; bỏ bộ lọc khác. |
| KT-GH-01 | UC15, UC16 | CartServiceImpl tìm dòng theo cartId/skuId rồi cộng số lượng; `toCartResponse` tính theo `sku.price`. 1+1=2, tiền hàng 530.000 đồng. Không thay giỏ bằng hai dòng SKU khác nhau. |
| KT-GH-02 | UC17, UC18 | Business rule cập nhật lên 3 có tiền hàng 795.000 đồng và xóa dòng là đúng. Tuy nhiên UI gửi JSON trong PUT, controller nhận `@RequestParam Integer quantity`; response cũng không có các trường phẳng `itemSubtotal/cartSubtotal` mà JavaScript đang đọc. Ghi rủi ro source, bổ sung Network/tải lại và tiếp tục kiểm tra xóa độc lập. Chưa chạy giao diện nên không tự ghi FAIL. |
| KT-KM-01 | UC19, UC20 | OrderServiceImpl và checkout có ba mã cố định. Giữ bốn hàng số tiền: không mã 295.000; LUNEA10 268.500; GIAM50K 245.000; FREESHIP 265.000 đồng khi tiền hàng 265.000. Bổ sung kiểm tra response `/api/orders/preview`: client có fallback nên ảnh số tiền đúng chưa chứng minh API thành công. UC19 không bao phủ đủ yêu cầu thời hạn/quota của voucher trong báo cáo yêu cầu. |
| KT-DH-01 | UC20, UC21 | Checkout gửi POST `/api/orders/checkout`, COD, sau thành công chuyển `/order-success?orderCode=...`; service tạo PENDING và xóa giỏ. Sửa bước chọn địa chỉ theo nhãn thật TP. Hồ Chí Minh / Phường Sài Gòn (Bến Nghé). Giữ tổng 295.000 đồng, mã DON-A động và kiểm tra dữ liệu đã lưu. |
| KT-DH-02 | UC21 | `recipientPhone` có HTML `required`; OrderRequest không có NotBlank tương ứng cho số điện thoại. Ca chỉ kiểm tra chặn form, không nâng thành bằng chứng validation API. Bổ sung điều kiện các select vị trí đã tải/chọn hợp lệ để không bị chặn vì một trường khác. |
| KT-DH-03 | UC22, UC23, UC24 | AccountPageController lấy lịch sử theo customerId và kiểm tra chủ sở hữu khi mở chi tiết bằng AuthPrincipal thực. Giữ kiểm tra cùng DON-A và tải lại, không dùng giá trị mẫu tĩnh của template. |
| KT-DH-04 | UC25 | cancelOrder chỉ cho chủ đơn hủy PENDING; UI chỉ hiện nút hủy ở trạng thái này. Giữ kiểm tra CANCELLED sau tải lại; không dùng banner hoàn voucher làm bằng chứng có quota voucher. Tồn giữ được đối chiếu riêng trong integration test. |
| KT-XL-01 | UC67, UC69, UC73 | OrderStatus và UI nhân viên hỗ trợ PENDING → CONFIRMED → PREPARING → SHIPPING → COMPLETED. Giữ DON-B mới, kiểm tra chi nhánh HCM/quyền xử lý, từng trạng thái sau tải lại và đồng bộ phía KH. |
| KT-PQ-01 | UC66 | AdminOrderRestController GET chi tiết gọi `OrderServiceImpl.getById(principal,id)`; StoreScope từ chối chi nhánh ngoài danh sách. UC85 chỉ là điều kiện dữ liệu đã phân chi nhánh, không phải UC được thao tác trực tiếp. Sửa dữ liệu DON-C sang lựa chọn Hà Nội / Phường Cầu Giấy hiện có; không giả định nhập được Hàng Bài/Hoàn Kiếm. |

Bộ ca liên kết **18 UC trực tiếp**, không tính UC85 là đã kiểm thử thao tác phân chi nhánh. Đây là phạm vi thiết kế test, không phải tỷ lệ UC đã PASS. 13 ID duy nhất và ba bộ mã của KT-KM-01 vẫn chỉ được đếm là một ca.

## Các điểm cần lưu ý từ rà source

| Mã | Mức ảnh hưởng dự kiến | Phát hiện | Trạng thái xác minh |
| --- | --- | --- | --- |
| SRC-01 | Cao | `cart.html:177–180` gửi JSON quantity; `CartRestController:70–75` nhận request parameter. JavaScript còn đọc `itemSubtotal/cartSubtotal`, khác cấu trúc CartResponse gồm items/subtotal. Có thể làm bước cập nhật giỏ không hoàn thành hoặc hiển thị sai trước tải lại. | Phát hiện bằng rà source; chưa tái hiện trên trình duyệt. Không đổi KT-GH-02 thành FAIL/PASS. |
| SRC-02 | Trung bình | `checkout.html:254–264` chỉ nạp một tập lựa chọn địa điểm; không có ô nhập quận độc lập. Các hidden input không hoàn toàn đồng bộ với select khi nạp/đổi địa chỉ. | Sửa dữ liệu hướng dẫn cho thao tác thực có. Không kết luận địa chỉ hành chính đã đúng; chưa thực thi kiểm thử địa chỉ đầy đủ. |
| SRC-03 | Trung bình | `checkout.html:297–333` tự tính ưu đãi nếu API preview lỗi. | Bổ sung tiêu chí đối chiếu response; giữ NOT RUN cho ca chưa chạy. |

Các điểm trên không thay thế danh sách lỗi đã xác nhận trong `LOI_PHAN_MEM.md`, không được cộng thành failures của lượt chạy 5 test và không dẫn tới sửa source trong lần này.

## Xác minh bằng chứng tích hợp

Đã kiểm tra đúng năm tên method trong XML H2 trước, XML PostgreSQL trước và lần chạy H2 mới; mỗi method có đúng một testcase, không có failure/error/skipped. Không dùng số method trong source làm bằng chứng PASS.

| ID | Method thực thi | Lần chạy H2 mới | Assertion quan sát được |
| --- | --- | --- | --- |
| QA-INT-01 | checkoutPersistsSnapshotAndHold | PASS; 0,396 giây | PENDING; subtotal 795000.00; actual 10, held 5; HELD; giỏ rỗng. Không có assertion đầy đủ mọi trường snapshot. |
| QA-INT-02 | failedCheckoutRollsBackAddress | PASS; 0,299 giây | BusinessException; số địa chỉ không tăng; không có đơn mới; held 2; giỏ còn 1 dòng. |
| QA-INT-03 | cancelReleasesHoldExactlyOnce | PASS; 1,184 giây | Hủy lặp có BusinessException; actual 10, held 2; RELEASED. Test không assertion riêng trường CANCELLED. |
| QA-INT-04 | shippingCommitsStockOnlyOnce | PASS; 0,327 giây | Chuỗi có hai lần SHIPPING rồi COMPLETED; actual 7, held 2; COMMITTED. Test không assertion riêng trạng thái cuối COMPLETED. |
| QA-INT-13 | orderApiDeniesAnotherStore | PASS; 0,619 giây | Đơn của khách A gán HN; principal nhân viên chỉ thuộc HCM; GET chi tiết admin trả 403. Không kiểm tra mọi route/quyền quản trị. |

- Ngày 21/10/2026, **04:00:09–04:00:42 (+07:00)**.
- **5 tests, 5 PASS, 0 failures, 0 errors, 0 skipped; BUILD SUCCESS, exit 0**.
- Suite Surefire: **24,188 giây**; Maven: **30,343 giây**; tiến trình: **33,620 giây**. Các đồng hồ có phạm vi đo khác nhau, không cộng chúng.
- macOS 13.7.8, x86_64; Oracle JDK 21.0.10; Maven Wrapper 3.9.16; H2 2.4.240 create-drop.
- Spring Boot Test chạy service/repository thật; fixture commit trước, không có transaction bọc toàn test. EmailService no-op; principal của ca MockMvc được đưa vào security context.
- Đối chứng PostgreSQL là kết quả đã lưu; lần này không chạy PostgreSQL mới. Không cộng ba lượt chạy thành 15 ca.

Lệnh đã chạy (JAVA_HOME trỏ tới JDK 21 như trong metadata):

```sh
./mvnw -o '-Dtest=AuditBusinessIntegrationTest#checkoutPersistsSnapshotAndHold+failedCheckoutRollsBackAddress+cancelReleasesHoldExactlyOnce+shippingCommitsStockOnlyOnce+orderApiDeniesAnotherStore' -DargLine=-javaagent:/Users/thinh/.m2/repository/org/mockito/mockito-core/5.23.0/mockito-core-5.23.0.jar test
```

[XML Surefire đã che output](minh_chung/xac_minh_5_ca_pass/TEST-5_CA_PASS.xml), [metadata/lệnh/môi trường](minh_chung/xac_minh_5_ca_pass/lan_chay.json), [tổng kết Maven](minh_chung/xac_minh_5_ca_pass/maven_summary.log). Raw log/XML ở thư mục QA tạm; bản bàn giao bỏ properties và chỉ giữ dòng QA_FIXTURE trong output để tránh lộ dữ liệu cấu hình.

Fixture khác bộ UI: giá 265.000 đồng nhưng số lượng **3**, tồn nền actual 10/held 2. Hàng giữ nền 2 không được tạo bằng reservation trong fixture; các assertion kiểm tra delta của đơn, không chứng minh tổng held bằng tổng mọi reservation trong hệ thống.

## Kiểm tra bố cục và bảo toàn source

- Word: **14 → 13 trang**, đã dành chỗ 9 ảnh và bổ sung hai bảng kết quả tích hợp; khổ A4, Times New Roman 13pt thân bài/12pt bảng.
- Đã giữ đủ 13 ca, gộp tiểu mục giỏ hàng và xem/hủy đơn, rút câu chữ lặp; bỏ hai vùng ảnh dự trù trùng. Không có ảnh thật bị xóa vì bản đầu chưa chèn ảnh.
- **20 bảng, 315 ô** cùng mọi đoạn văn khớp nội dung PDF; không phát hiện dòng bảng tách trang, chữ tràn hoặc lỗi số cột bảng Markdown. Chú thích ảnh và 9 bookmark đã đánh lại nhất quán.
- **299 tệp source/build có SHA-256 không đổi**, không thêm/xóa source/test. Chỉ chạy các method có sẵn.
- [PDF kiểm tra bố cục](minh_chung/BO_CUC_CHUONG_6_2.pdf) và [metadata kiểm tra](minh_chung/chapter_layout_checks.json). PDF bố cục không phải ảnh bằng chứng kiểm thử phần mềm.
