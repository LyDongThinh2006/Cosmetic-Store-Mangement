# 6.2. Kiểm thử các chức năng chính

Kiểm thử chức năng được xây dựng nhằm đối chiếu hoạt động của hệ thống Cosmetic Store Chain Management – LUNEA với các nghiệp vụ đã triển khai. Nội dung tập trung vào quá trình từ đăng nhập, tìm sản phẩm, thao tác giỏ hàng và đặt hàng COD đến theo dõi, hủy và xử lý đơn theo chi nhánh. Các chức năng được lựa chọn vì có tần suất sử dụng cao hoặc ảnh hưởng trực tiếp đến số tiền và trạng thái đơn hàng.

## 6.2.1. Mục tiêu, phạm vi và điều kiện kiểm thử

Mục tiêu là kiểm tra dữ liệu hiển thị, khả năng hoàn thành thao tác và sự nhất quán của thông tin giữa các màn hình. Phương pháp chủ đạo là kiểm thử hộp đen trên trình duyệt, kết hợp các trường hợp hợp lệ, dữ liệu không hợp lệ và giới hạn quyền truy cập. Một ca chỉ đạt khi đáp ứng đầy đủ tiêu chí đã nêu và có kết quả thực tế tương ứng.

*Bảng 6.3. Phạm vi các chức năng chính*

| Nhóm chức năng | Nội dung lựa chọn |
| --- | --- |
| Xác thực | Đăng nhập đúng thông tin; từ chối mật khẩu sai. |
| Sản phẩm | Tìm kiếm, mở chi tiết; tìm kiếm không có kết quả. |
| Giỏ hàng | Thêm và gộp cùng SKU; cập nhật số lượng và xóa. |
| Ưu đãi và tính tiền | Đối chiếu ba mã cố định LUNEA10, GIAM50K, FREESHIP. |
| Đặt hàng COD | Đặt hàng hợp lệ; kiểm tra trường số điện thoại bắt buộc trên form. |
| Đơn hàng của khách | Xem lịch sử, chi tiết và hủy đơn đang chờ xác nhận. |
| Xử lý và phân quyền | Nhân viên xử lý đơn; từ chối xem đơn ngoài chi nhánh. |

*Bảng 6.4. Môi trường và công cụ*

| Thành phần | Thông tin sử dụng |
| --- | --- |
| Hệ thống đối tượng | LUNEA hiện có; Spring Boot 4.1.1, Java 21, Spring Security; giao diện Thymeleaf và JavaScript. |
| Thực thi thủ công | Trình duyệt máy tính có JavaScript; dùng cùng một bản ứng dụng và CSDL PostgreSQL QA trong toàn bộ lượt kiểm thử. |
| Môi trường đã xác nhận | Máy biên tập macOS 13.7.8; Oracle JDK 21.0.10, Maven Wrapper 3.9.16. Hồ sơ tự động dùng H2 2.4.240; có đối chứng PostgreSQL 16.15. |
| Ghi nhận minh chứng | Ảnh màn hình và tab Network khi cần. Phiên bản trình duyệt, URL và thời điểm thao tác ghi vào biên bản thu thập ảnh. |

### Dữ liệu và quy ước thực hiện

Bộ đầu vào được chọn từ DataSeeder và quy tắc tính tiền trong source. Trước khi chạy, đối chiếu tài khoản ACTIVE, giá bán và tồn kho trên CSDL QA; bảng này chưa phải kết quả quan sát từ giao diện.

*Bảng 6.5. Dữ liệu dùng chung cho các ca kiểm thử*

| Ký hiệu / thành phần | Giá trị và cách sử dụng |
| --- | --- |
| KH – khách hàng | khach01@lunea.test; tài khoản ACTIVE. Dùng mật khẩu hiện hành được cấp cho tài khoản QA; không đưa mật khẩu vào báo cáo hoặc ảnh. |
| NV-HCM – nhân viên | Tên đăng nhập order_hcm; tài khoản xử lý đơn, chỉ thuộc chi nhánh HCM. Dùng mật khẩu QA được cấp. |
| SP – sản phẩm | Serum Bí Đao Cocoon 7% Niacinamide Trị Mụn & Mờ Thâm; SKU-CCN-BD-70ML; biến thể Dung tích 70ml; giá bán 265.000 đồng. Giá niêm yết 295.000 đồng không dùng để tính tiền hàng. |
| Tồn kho và giỏ | Sản phẩm/SKU đang ACTIVE; một chi nhánh đủ tồn cho toàn bộ giỏ. Dọn giỏ qua giao diện trước mỗi kịch bản độc lập. Số lượng lớn nhất của các ca giỏ là 3. |
| Người nhận thử nghiệm | Tên: QA LUNEA; điện thoại: 0901234567; địa chỉ: 123 Đường Đồng Khởi; phường: Phường Bến Nghé; quận: Quận 1; tỉnh/thành: Hồ Chí Minh. Đây là đầu vào QA, không phải thông tin giao hàng thật. |
| DON-A / DON-B / DON-C | DON-A là mã đơn thực được tạo tại KT-DH-01, dùng xem và hủy. DON-B tạo mới cùng cách để nhân viên xử lý. DON-C là đơn thuộc Hà Nội dùng kiểm tra phạm vi chi nhánh; không tự đặt mã đơn hoặc ID. |
| Tiền hàng và phí | 1 SP: tiền hàng 265.000; phí giao hàng 30.000; không ưu đãi: tổng 295.000 đồng. Tiền hàng từ 500.000 đồng được miễn phí giao hàng theo source. |

Các ca có quan hệ được thực hiện theo chuỗi: KT-DN-01 → KT-SP-01 → KT-GH-01/02 → KT-KM-01 → KT-DH-01 → KT-DH-03 → KT-DH-04. Trước ca đặt hàng, đưa giỏ về đúng 1 SP và bỏ mã ưu đãi. DON-B phải là đơn mới, chưa bị hủy; DON-C phải được xác nhận thực sự thuộc Hà Nội trước ca phân quyền.

Quy ước: PASS khi đã chạy và mọi tiêu chí đạt; FAIL khi đã chạy nhưng có tiêu chí sai; NOT RUN khi chưa chạy; BLOCKED khi thiếu điều kiện thực thi. P0: ưu tiên cao nhất; P1: cao; P2: bổ sung. 13 ca giao diện hiện giữ NOT RUN; kết quả tích hợp được ghi riêng tại mục 6.2.9. OTP/email, kiểm thử tải và toàn bộ nghiệp vụ quản trị nằm ngoài phạm vi chương.

## 6.2.2. Kiểm thử chức năng đăng nhập

Kiểm tra đăng nhập đúng và từ chối mật khẩu sai trong hai phiên riêng tư độc lập.

*Bảng 6.6. KT-DN-01 – Đăng nhập bằng tài khoản hợp lệ*

| Nội dung | KT-DN-01  /  P1  /  UC01 |
| --- | --- |
| Điều kiện | KH đang ACTIVE; mở cửa sổ riêng tư chưa đăng nhập. |
| Đầu vào | Email: khach01@lunea.test; mật khẩu đúng được cấp cho KH. |
| Thao tác | 1. Mở /login và điền email, mật khẩu đúng.<br>2. Nhấn Đăng nhập; sau đó mở /account/orders và tải lại trang. |
| Mong đợi | Đăng nhập thành công; trang cá nhân/lịch sử đơn mở được với đúng KH; tải lại vẫn nhận diện KH. Không lấy riêng thông báo thành công làm tiêu chí đạt. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.2 (chờ thu thập). |

*Bảng 6.7. KT-DN-02 – Từ chối mật khẩu không đúng*

| Nội dung | KT-DN-02  /  P1  /  UC01 |
| --- | --- |
| Điều kiện | KH tồn tại; dùng cửa sổ riêng tư mới, chưa đăng nhập. |
| Đầu vào | Email: khach01@lunea.test; mật khẩu sai: QA_SAI_2026!; xác nhận chuỗi này khác mật khẩu hiện hành. |
| Thao tác | 1. Mở /login, điền email đúng và mật khẩu sai.<br>2. Nhấn Đăng nhập; xem request /api/auth/login trong Network; thử mở /account/orders. |
| Mong đợi | API đăng nhập trả HTTP 401, mã INVALID_CREDENTIALS; không xác thực KH. Trang /account/orders chuyển về /login. Không dùng riêng sự xuất hiện hoặc thiếu toast làm tiêu chí xác thực. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng chưa thu thập. |

*Hình 6.2. Giao diện tài khoản sau thao tác đăng nhập hợp lệ*

## 6.2.3. Kiểm thử tìm kiếm và xem sản phẩm

Kiểm tra tìm đúng sản phẩm và trạng thái không có kết quả; bỏ các bộ lọc trước khi thử.

*Bảng 6.8. KT-SP-01 – Tìm sản phẩm và xem đúng biến thể*

| Nội dung | KT-SP-01  /  P1  /  UC05, UC07 |
| --- | --- |
| Điều kiện | SP đang được công khai trong danh sách sản phẩm. |
| Đầu vào | Từ khóa: Cocoon; biến thể cần đối chiếu: Dung tích 70ml, SKU-CCN-BD-70ML. |
| Thao tác | 1. Mở /products, nhập Cocoon và thực hiện tìm kiếm.<br>2. Mở SP trong kết quả; chọn biến thể 70ml. |
| Mong đợi | Danh sách có SP Cocoon; trang chi tiết hiển thị đúng tên, biến thể 70ml và giá bán 265.000 đồng. Việc chọn biến thể không dùng giá niêm yết 295.000 đồng làm giá bán. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.3 (chờ thu thập). |

*Bảng 6.9. KT-SP-02 – Tìm kiếm không có kết quả*

| Nội dung | KT-SP-02  /  P2  /  UC05 |
| --- | --- |
| Điều kiện | Danh sách sản phẩm đã tải; không có sản phẩm chứa từ khóa thử. |
| Đầu vào | Từ khóa: QA_NO_MATCH_20261021; bỏ các bộ lọc khác. |
| Thao tác | 1. Thay từ khóa bằng QA_NO_MATCH_20261021 và tìm kiếm.<br>2. Quan sát danh sách; xóa từ khóa và tìm lại. |
| Mong đợi | Không có thẻ sản phẩm phù hợp hoặc có trạng thái rỗng; trang không phát sinh lỗi. Khi bỏ từ khóa, danh sách sản phẩm được hiển thị lại. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng chưa thu thập. |

*Hình 6.3. Danh sách sản phẩm sau khi tìm kiếm từ khóa Cocoon*

## 6.2.4. Kiểm thử chức năng giỏ hàng

Kiểm tra đúng SKU, số lượng và tiền hàng; giỏ ban đầu rỗng, chỉ dùng biến thể 70ml.

*Bảng 6.10. KT-GH-01 – Thêm sản phẩm và gộp cùng SKU*

| Nội dung | KT-GH-01  /  P0  /  UC15, UC16 |
| --- | --- |
| Điều kiện | KH đăng nhập; giỏ rỗng; SP có ít nhất 2 đơn vị khả dụng. |
| Đầu vào | SP 265.000 đồng; thêm 1 rồi thêm lại 1 cùng SKU. |
| Thao tác | 1. Chọn SP 70ml, số lượng 1; thêm vào giỏ.<br>2. Thêm tiếp 1 cùng SKU; mở /cart.<br>3. Tải lại; đối chiếu dòng hàng, số lượng, tiền. |
| Mong đợi | Chỉ 1 dòng SKU-CCN-BD-70ML, số lượng 2; thành tiền/tạm tính 530.000 đồng. Vẫn đúng sau tải lại, không mất hoặc nhân đôi dòng; chưa cộng phí giao hàng. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.4 (chờ thu thập). |

### Cập nhật số lượng và xóa khỏi giỏ

*Bảng 6.11. KT-GH-02 – Cập nhật số lượng và xóa sản phẩm*

| Nội dung | KT-GH-02  /  P0  /  UC17, UC18 |
| --- | --- |
| Điều kiện | Giỏ có 1 dòng SP, số lượng 2 từ KT-GH-01; khả dụng ít nhất 3. |
| Đầu vào | Đổi số lượng 2 → 3; sau đó xóa dòng SP bằng nút xóa. |
| Thao tác | 1. Tăng lên 3; xem PUT trong Network; tải lại.<br>2. Đối chiếu số lượng/tiền; nếu cập nhật lỗi, ghi lỗi và tiếp tục thử xóa.<br>3. Nhấn xóa dòng, xác nhận; tải lại. |
| Mong đợi | Cập nhật: 1 dòng, số lượng 3, tiền hàng 795.000 đồng. Xóa: hết dòng SP, số sản phẩm 0, giỏ rỗng. Chỉ thử nút xóa, không suy ra hành vi nhập số lượng 0. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.4 (chờ thu thập). |

Rủi ro source: UI gửi quantity bằng JSON, controller nhận query parameter. Ca chưa chạy vẫn NOT RUN; cần ghi HTTP và dữ liệu sau tải lại. Sau khi thử xóa, thêm lại 1 SP cho các ca tiếp theo.

*Hình 6.4. Minh chứng thao tác cập nhật số lượng trong giỏ hàng*

## 6.2.5. Kiểm thử áp dụng ưu đãi và tính tiền

Mã nguồn hiện xử lý ba mã cố định. Ca kiểm thử đối chiếu giá trị giảm, phí vận chuyển và tổng thanh toán trong phạm vi ba mã này. Mỗi bộ dữ liệu được chạy độc lập để tránh giữ hiệu ứng của mã trước.

*Bảng 6.12. KT-KM-01 – Tính tiền khi áp dụng mã ưu đãi*

| Nội dung | KT-KM-01  /  P0  /  UC19, UC20 |
| --- | --- |
| Điều kiện | KH đăng nhập; giỏ có đúng 1 SP giá 265.000 đồng; mở /checkout. |
| Đầu vào | Lần lượt thử LUNEA10, GIAM50K, FREESHIP; mỗi lần tải mới /checkout trước khi nhập một mã. |
| Thao tác | 1. Ghi nhận tổng ban đầu khi chưa áp dụng mã.<br>2. Nhập từng mã, nhấn áp dụng; xem POST /api/orders/preview trong Network.<br>3. Đối chiếu response và giao diện với Bảng 6.13; tải mới trước mã tiếp theo. |
| Mong đợi | Cả 3 request preview trả 200 và số tiền đúng bảng; giao diện khớp response. Tổng = tiền hàng − giảm giá + phí giao hàng. Nếu API lỗi nhưng client tự tính, ghi lỗi riêng; không coi ảnh số tiền đúng là bằng chứng API đã đạt. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.5 (chờ thu thập). |

*Bảng 6.13. Số tiền mong đợi với 1 SP; đơn vị: đồng*

| Mã áp dụng | Tiền hàng | Giảm giá | Phí giao | Tổng |
| --- | --- | --- | --- | --- |
| Không mã | 265.000 | 0 | 30.000 | 295.000 |
| LUNEA10 | 265.000 | 26.500 | 30.000 | 268.500 |
| GIAM50K | 265.000 | 50.000 | 30.000 | 245.000 |
| FREESHIP | 265.000 | 0 | 0 | 265.000 |

*Hình 6.5. Tổng tiền khi áp dụng LUNEA10 cho một sản phẩm 265.000 đồng*

## 6.2.6. Kiểm thử chức năng đặt hàng COD

Nhóm ca xác minh khả năng tạo đơn từ giỏ hợp lệ và việc kiểm tra dữ liệu bắt buộc trên form. Dữ liệu tài khoản, địa chỉ và số tiền được giữ thống nhất từ trang thanh toán đến trang thông báo đặt hàng và lịch sử đơn.

*Bảng 6.14. KT-DH-01 – Đặt đơn COD hợp lệ*

| Nội dung | KT-DH-01  /  P0  /  UC20, UC21 |
| --- | --- |
| Điều kiện | KH đăng nhập; giỏ đúng 1 SP; không mã ưu đãi; chi nhánh phù hợp đủ hàng. |
| Đầu vào | Người nhận QA LUNEA, 0901234567, địa chỉ thử tại Hồ Chí Minh theo Bảng 6.5; COD; số lượng 1. |
| Thao tác | 1. Tải mới /checkout; chọn + Nhập địa chỉ giao hàng mới.<br>2. Điền người nhận; chọn TP. Hồ Chí Minh và Phường Sài Gòn (Bến Nghé); điền địa chỉ. Kiểm tra tiền hàng 265.000, phí 30.000, tổng 295.000 đồng; COD.<br>3. Nhấn Đặt hàng một lần; lưu mã đơn thực là DON-A.<br>4. Mở lịch sử/chi tiết DON-A và mở lại giỏ để kiểm tra. |
| Mong đợi | Tạo đúng 1 đơn của KH; mã thực trên trang thành công khớp lịch sử. Đơn có 1 SP giá 265.000, tổng 295.000 đồng, COD và trạng thái Chờ xác nhận (PENDING); địa chỉ đúng đầu vào; giỏ đã được làm rỗng. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.6 (chờ thu thập). |

DON-A là ký hiệu tham chiếu trong báo cáo. Mã đơn trên ảnh phải là mã do hệ thống sinh ra ở chính lần thao tác này; không sửa ảnh hoặc lấy một đơn có sẵn để thay thế.

*Hình 6.6. Thông báo đặt hàng COD và mã đơn do hệ thống tạo*

### Kiểm tra thông tin bắt buộc trước khi đặt hàng

*Bảng 6.15. KT-DH-02 – Không gửi form khi thiếu số điện thoại*

| Nội dung | KT-DH-02  /  P1  /  UC21 |
| --- | --- |
| Điều kiện | KH đăng nhập; thêm lại 1 SP; danh sách tỉnh/thành, phường/xã đã tải và được chọn hợp lệ. |
| Đầu vào | Nhập đầy đủ địa chỉ QA nhưng để trống ô Số điện thoại; không chọn lại địa chỉ lưu sẵn làm ô được điền tự động. |
| Thao tác | 1. Chọn nhập địa chỉ mới; điền các trường khác và xóa hết số điện thoại.<br>2. Ghi nhận danh sách đơn hiện có; nhấn Đặt hàng trên form.<br>3. Quan sát cảnh báo của trường bắt buộc; đối chiếu tab Network và lịch sử đơn. |
| Mong đợi | Trình duyệt chặn gửi form tại trường số điện thoại bắt buộc; không có request checkout mới từ thao tác này, không tạo thêm đơn và giỏ còn 1 SP. Đây là kiểm tra form HTML, không chứng minh API từ chối mọi số điện thoại thiếu/sai. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.7 (chờ thu thập). |

Sau ca, điền lại số điện thoại đúng nếu tiếp tục tạo DON-B cho nhân viên xử lý. Chỉ kết luận theo thao tác trên form đã mô tả; các giá trị số điện thoại không đúng định dạng hoặc yêu cầu gửi trực tiếp tới API thuộc phạm vi khác.

*Hình 6.7. Cảnh báo trường số điện thoại bắt buộc tại trang thanh toán*

## 6.2.7. Kiểm thử xem và hủy đơn của khách hàng

Dùng cùng DON-A để xem và hủy; chưa cho nhân viên xác nhận đơn này.

*Bảng 6.16. KT-DH-03 – Xem lịch sử và chi tiết đơn vừa đặt*

| Nội dung | KT-DH-03  /  P1  /  UC22, UC23, UC24 |
| --- | --- |
| Điều kiện | KH đã tạo thành công DON-A ở KT-DH-01; đơn còn PENDING. |
| Đầu vào | DON-A của KT-DH-01; 1 SP 70ml, COD, tổng 295.000 đồng. |
| Thao tác | 1. Mở /account/orders, tìm đúng DON-A.<br>2. Mở chi tiết; đối chiếu sản phẩm, tiền, người nhận, trạng thái.<br>3. Tải lại và kiểm tra dữ liệu. |
| Mong đợi | Lịch sử KH có DON-A, mã khớp trang thành công. Chi tiết: 1 SP 70ml/265.000, phí 30.000, tổng 295.000 đồng, COD, người nhận QA đúng, Chờ xác nhận; không đổi sau tải lại. |
| Thực tế | NOT RUN – chưa có biên bản. Lưu ảnh/response chi tiết trước hủy trong hồ sơ, không thêm vào chương. |

### Hủy đơn đang chờ xác nhận

*Bảng 6.17. KT-DH-04 – Khách hàng hủy đơn PENDING của mình*

| Nội dung | KT-DH-04  /  P0  /  UC25 |
| --- | --- |
| Điều kiện | KH xem DON-A của mình; đơn còn PENDING. |
| Đầu vào | DON-A đã được đối chiếu ở KT-DH-03; hủy bằng nút của giao diện. |
| Thao tác | 1. Nhấn Hủy đơn DON-A và xác nhận.<br>2. Tải lại lịch sử/chi tiết, đối chiếu mã đơn.<br>3. Kiểm tra trạng thái và nút hủy. |
| Mong đợi | DON-A chuyển Đã hủy (CANCELLED), vẫn tra cứu được và giữ trạng thái sau tải lại; hết nút hủy. Không suy ra tồn hoặc lượt dùng voucher chỉ từ banner. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.8 (chờ thu thập). |

*Hình 6.8. Đơn DON-A chuyển sang trạng thái Đã hủy*

## 6.2.8. Kiểm thử xử lý đơn và phạm vi chi nhánh

Nhóm ca đại diện nghiệp vụ của nhân viên: cập nhật vòng đời đơn được giao và giới hạn xem dữ liệu theo chi nhánh. Dùng đơn mới, tách khỏi DON-A đã hủy để không làm sai điều kiện trước của các ca khách hàng.

*Bảng 6.18. KT-XL-01 – Nhân viên xử lý đơn theo vòng đời hợp lệ*

| Nội dung | KT-XL-01  /  P0  /  UC67, UC69, UC73 |
| --- | --- |
| Điều kiện | Tạo DON-B theo KT-DH-01; xác nhận đơn được phân HCM, PENDING. NV-HCM có quyền xử lý đơn; chưa có nhân viên khác thao tác. |
| Đầu vào | DON-B mới; tài khoản order_hcm; chuỗi PENDING → CONFIRMED → PREPARING → SHIPPING → COMPLETED. |
| Thao tác | 1. Đăng nhập NV-HCM, mở /admin/orders và chi tiết DON-B.<br>2. Xác nhận đơn; chuyển Đang đóng gói; bắt đầu giao hàng; xác nhận giao thành công, lần lượt theo nút đang khả dụng.<br>3. Sau từng bước tải lại chi tiết và đối chiếu trạng thái; cuối cùng dùng KH mở lại DON-B. |
| Mong đợi | Mỗi bước lưu đúng trạng thái theo thứ tự; cuối cùng DON-B là Hoàn thành (COMPLETED) ở cả giao diện nhân viên và khách. Mã đơn, số lượng, COD và tổng 295.000 đồng không bị thay đổi qua các bước. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.9 (chờ thu thập). |

Các trạng thái phản ánh tiến độ xử lý tại tài khoản nhân viên và được hiển thị lại trong lịch sử của khách. Việc giữ nguyên sản phẩm, số lượng và tổng tiền qua các bước là tiêu chí đối chiếu bổ sung bên cạnh nhãn trạng thái.

*Hình 6.9. Nhân viên cập nhật DON-B đến trạng thái Hoàn thành*

### Giới hạn xem đơn ngoài chi nhánh được phân công

*Bảng 6.19. KT-PQ-01 – Từ chối nhân viên xem đơn thuộc chi nhánh khác*

| Nội dung | KT-PQ-01  /  P0  /  UC66 |
| --- | --- |
| Điều kiện | NV-HCM chỉ thuộc HCM. DON-C đã tồn tại và được xác nhận thuộc Hà Nội. Dùng đơn QA; không thay dữ liệu phân công để làm kết quả đạt. |
| Đầu vào | Tên đăng nhập order_hcm; ID thực DON-C thuộc Hà Nội; GET /api/admin/orders/{id của DON-C}. |
| Thao tác | 1. Chuẩn bị DON-C bằng checkout địa chỉ Hà Nội trên QA; kiểm tra chi nhánh được phân thực tế và lưu ID từ URL chi tiết.<br>2. Đăng xuất KH; đăng nhập NV-HCM trong cửa sổ sạch.<br>3. Mở trực tiếp /api/admin/orders/{id thực DON-C}; xem HTTP status trong Network, chỉ đọc phần Response. |
| Mong đợi | Request chi tiết đơn trả HTTP 403; không trả dữ liệu chi tiết DON-C cho NV-HCM. Nếu đơn không thuộc Hà Nội hoặc nhân viên có nhiều chi nhánh thì điều kiện trước chưa hợp lệ, không kết luận PASS. |
| Thực tế | NOT RUN – chưa có biên bản. Minh chứng: Hình 6.10 (chờ thu thập). |

Tạo DON-C bằng địa chỉ mới: QA LUNEA, 0901234567, 10 Xuân Thủy; chọn Hà Nội và Phường Cầu Giấy theo danh sách thực của form, giỏ 1 SP, COD. Form không có ô nhập quận độc lập; ca này không kết luận tính đúng địa chỉ hành chính. Xác nhận chi nhánh Hà Nội trước khi thử. UC85 là điều kiện phân nhân viên, không phải chức năng gán chi nhánh được kiểm tra trong ca này.

*Hình 6.10. HTTP 403 khi nhân viên HCM yêu cầu xem đơn của chi nhánh Hà Nội*

## 6.2.9. Tổng hợp kết quả và đánh giá

*Bảng 6.20. Liên kết Use Case và trạng thái 13 ca chức năng*

| Mã ca | Use Case | Chức năng kiểm thử | Trạng thái |
| --- | --- | --- | --- |
| KT-DN-01 | UC01 | Đăng nhập bằng tài khoản hợp lệ | NOT RUN |
| KT-DN-02 | UC01 | Từ chối mật khẩu không đúng | NOT RUN |
| KT-SP-01 | UC05, UC07 | Tìm sản phẩm và xem đúng biến thể | NOT RUN |
| KT-SP-02 | UC05 | Tìm kiếm không có kết quả | NOT RUN |
| KT-GH-01 | UC15, UC16 | Thêm sản phẩm và gộp cùng SKU | NOT RUN |
| KT-GH-02 | UC17, UC18 | Cập nhật số lượng và xóa sản phẩm | NOT RUN |
| KT-KM-01 | UC19, UC20 | Tính tiền khi áp dụng mã ưu đãi | NOT RUN |
| KT-DH-01 | UC20, UC21 | Đặt đơn COD hợp lệ | NOT RUN |
| KT-DH-02 | UC21 | Không gửi form khi thiếu số điện thoại | NOT RUN |
| KT-DH-03 | UC22, UC23, UC24 | Xem lịch sử và chi tiết đơn vừa đặt | NOT RUN |
| KT-DH-04 | UC25 | Khách hàng hủy đơn PENDING của mình | NOT RUN |
| KT-XL-01 | UC67, UC69, UC73 | Nhân viên xử lý đơn theo vòng đời hợp lệ | NOT RUN |
| KT-PQ-01 | UC66 | Từ chối nhân viên xem đơn thuộc chi nhánh khác | NOT RUN |

13 ca giao diện chưa có biên bản/ảnh mới: 0 PASS, 0 FAIL, 13 NOT RUN, 0 BLOCKED. KT-KM-01 gồm 3 bộ dữ liệu và chỉ đạt khi cả 3 bộ đạt; không cộng thành 3 ca.

### Đánh giá phạm vi và hạn chế

13 ca liên kết 18 Use Case trong báo cáo yêu cầu, bao phủ luồng mua hàng, xử lý đơn và ngoại lệ chính. Đây là liên kết thiết kế ca, chưa phải bằng chứng 18 Use Case đã đạt. UC19 chỉ được kiểm tra trong phạm vi ba mã cố định mà source đã triển khai; chưa có kiểm tra thời hạn hoặc lượt sử dụng voucher.

Rà source cho thấy rủi ro cập nhật giỏ ở KT-GH-02 và việc client tự tính ưu đãi khi API preview lỗi. Khi chạy, cần đối chiếu Network và dữ liệu sau tải lại. UC85 là điều kiện phân chi nhánh của KT-PQ-01, không được tính là đã kiểm thử thao tác phân nhân viên. Chưa đủ bằng chứng giao diện để tính tỷ lệ đạt hoặc nghiệm thu toàn hệ thống.

### Kết quả thực tế của 5 integration test đã xác minh

Đã đối chiếu XML H2, XML PostgreSQL đã lưu và chạy lại đúng 5 method hiện có trên H2 riêng. Fixture của lượt chạy lại dùng SKU giá 265.000 đồng, giỏ 3 đơn vị, tồn ban đầu actual=10/held=2; ca thiếu tồn đổi thành actual=2/held=2. Dữ liệu này tách biệt bộ đầu vào 1 SP trên giao diện.

*Bảng 6.21. Kết quả thực tế của lần chạy tích hợp chọn lọc*

| Mã ca | Nghiệp vụ | Kết quả quan sát qua assertion | Kết quả |
| --- | --- | --- | --- |
| QA-INT-01 | Đặt hàng và giữ tồn | PENDING; subtotal 795.000 đồng; actual=10, held=5; reservation HELD; giỏ rỗng. | PASS |
| QA-INT-02 | Rollback khi thiếu tồn | Có BusinessException; số địa chỉ không tăng; không có đơn mới; held=2; giỏ còn 1 dòng. | PASS |
| QA-INT-03 | Hủy và giải phóng giữ | Hủy lặp có BusinessException; actual=10, held=2; reservation RELEASED. | PASS |
| QA-INT-04 | Giao hàng, không trừ lặp | Sau chuỗi có SHIPPING lặp: actual=7, held=2; reservation COMMITTED. | PASS |
| QA-INT-13 | Đọc đơn ngoài chi nhánh | Nhân viên chỉ thuộc HCM yêu cầu GET chi tiết đơn gán HN: HTTP 403. | PASS |

*Bảng 6.22. Tổng kết lần thực thi đã xác minh*

| Hạng mục | Số liệu thực tế |
| --- | --- |
| Thời điểm | 21/10/2026, 04:00:09–04:00:42, Asia/Ho_Chi_Minh (+07:00). |
| Môi trường | macOS 13.7.8, x86_64; JDK 21.0.10; Maven Wrapper 3.9.16; H2 2.4.240, schema QA create-drop. |
| Kết quả Surefire | 5 tests; 5 PASS; failures 0; errors 0; skipped 0. Maven BUILD SUCCESS; exit code 0. |
| Thời gian | Suite Surefire 24,188 giây; Maven 30,343 giây; tiến trình 33,620 giây, |
| Bằng chứng | Hồ sơ kiểm thử ngày 21/10/2026, minh_chung/xac_minh_5_ca_pass/: XML Surefire, lan_chay.json và maven_summary.log. Lệnh đầy đủ nằm trong lan_chay.json. |

Các kết quả chỉ xác nhận những assertion trong bảng: không mặc định đã kiểm tra mọi trường snapshot, trạng thái cuối hoặc mọi quyền truy cập. Principal MockMvc được đưa vào security context; email là no-op; không xác nhận trình duyệt, JWT login hoặc SMTP thật. PostgreSQL là đối chứng đã lưu, không phải lần chạy mới. Không cộng trùng các lần chạy hoặc dùng 5 PASS thay cho 13 ca giao diện.
