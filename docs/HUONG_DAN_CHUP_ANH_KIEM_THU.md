# Chụp ảnh cho mục 6.2 – bản tối ưu bố cục

Bản Word giữ đủ **13 ca chức năng**, có **9 vùng ảnh/chú thích** và bảng kết quả thực tế của **5 integration test đã chạy lại**. Đã gộp phần thêm/cập nhật giỏ và xem/hủy đơn; hai vùng ảnh trùng được bỏ, không có ảnh thật nào bị xóa.

Microsoft Word đã xuất bản cuối thành **13 trang**, tính cả nội dung, 9 vùng ảnh và hai bảng kết quả tích hợp. Giữ kích thước ảnh theo hướng dẫn để bảo toàn giới hạn 15 trang.

## Chuẩn bị

1. Dùng ứng dụng và PostgreSQL QA; ghi ngày/giờ, URL/profile, trình duyệt/phiên bản và người thực hiện. Xác nhận KH `khach01@lunea.test`, NV-HCM `order_hcm`, SKU `SKU-CCN-BD-70ML` giá bán 265.000 đồng, tài khoản/sản phẩm hoạt động và đủ tồn.
2. Thử đăng nhập đúng và sai trong hai cửa sổ riêng tư độc lập. Sai mật khẩu: kiểm tra POST `/api/auth/login` trả HTTP 401, mã INVALID_CREDENTIALS; không bắt buộc xuất hiện toast lỗi.
3. Giỏ bắt đầu rỗng. Thêm 1+1 cùng SKU, tải lại: 2 SP/530.000 đồng. Khi thử cập nhật lên 3, xem PUT và tải lại; source hiện gửi JSON trong khi controller nhận query parameter. Nếu có lỗi, chụp thực tế và tiếp tục kiểm tra nút xóa độc lập; không sửa code/API để làm ca UI đạt. Sau xóa, thêm lại đúng 1 SP cho ca tiếp theo.
4. Mỗi mã LUNEA10, GIAM50K, FREESHIP dùng lượt tải mới /checkout. Phải đối chiếu POST `/api/orders/preview` HTTP 200, response và số tiền trên giao diện; client có fallback nên chỉ nhìn tổng tiền chưa đủ.
5. Khi đặt DON-A: chọn + Nhập địa chỉ giao hàng mới, chọn TP. Hồ Chí Minh và Phường Sài Gòn (Bến Nghé), điền tên/điện thoại/địa chỉ QA. Giỏ 1 SP, không ưu đãi, COD, tổng 295.000 đồng. Các select phải tải/chọn hợp lệ trước khi thử bỏ trống số điện thoại.
6. Lưu mã thực DON-A từ lần đặt hàng. Xem chi tiết PENDING rồi hủy cùng đơn. Tạo DON-B mới thuộc HCM để nhân viên xử lý, không dùng đơn đã hủy.
7. Tạo DON-C bằng địa chỉ mới: QA LUNEA, 0901234567, 10 Xuân Thủy, chọn Hà Nội và Phường Cầu Giấy. Form không có ô nhập quận riêng; không đặt giả định nhập Hàng Bài/Hoàn Kiếm. Xác nhận đơn thực sự thuộc Hà Nội; đăng nhập NV-HCM chỉ thuộc HCM rồi yêu cầu GET chi tiết DON-C.

## Chèn ảnh và giữ giới hạn 15 trang

Mở `CHUONG_6_2_KIEM_THU.docx`, dùng Go To → Bookmark, chọn `ANH_CN_01` đến `ANH_CN_09`; chèn thay dấu cách trong vùng trống. Giữ chú thích có sẵn, dùng **In Line with Text**, khóa tỷ lệ, rộng tối đa **15,5 cm**. Chiều cao tối đa theo bảng dưới. Cắt vùng trình duyệt không liên quan để chữ và số tiền đọc được; có thể phóng to trình duyệt trước khi chụp. Không sửa kết quả trong ảnh.

| Bookmark / hình | Ca và trang bố trí | Cao ảnh tối đa | Chụp tại bước nào / nội dung |
| --- | --- | --- | --- |
| `ANH_CN_01` / Hình 6.2 | KT-DN-01; trang dự kiến 3 | 5.0 cm | Chụp sau bước 2 của KT-DN-01: trang /account/orders hoặc hồ sơ hiển thị đúng tài khoản KH. Không chụp ô mật khẩu hay vùng điền nhanh demo có hiện mật khẩu. |
| `ANH_CN_02` / Hình 6.3 | KT-SP-01; trang dự kiến 4 | 5.0 cm | Chụp bước 1 của KT-SP-01, thấy từ khóa Cocoon và thẻ sản phẩm đúng tên. Kiểm tra giá biến thể 70ml tại bước 2; nếu cần, lưu ảnh chi tiết ngoài chương. |
| `ANH_CN_03` / Hình 6.4 | KT-GH-02; trang dự kiến 5 | 3.7 cm | Ảnh chung của nhóm giỏ: sau bước cập nhật và tải lại, thấy SKU, số lượng/tiền thực tế; nếu có lỗi, thấy PUT và HTTP/response trong Network. Kỳ vọng 3 SP/795.000 chỉ là tiêu chí, không phải kết quả đã có. Lưu riêng ảnh giỏ 2 SP sau thêm và giỏ sau xóa trong hồ sơ khi cần. |
| `ANH_CN_04` / Hình 6.5 | KT-KM-01; trang dự kiến 6 | 5.0 cm | Chụp sau bước 3 với LUNEA10: thấy mã, tiền hàng 265.000, giảm 26.500, phí 30.000 và tổng 268.500 đồng. Chỉ chụp khi số tiền đúng với đầu vào; lưu kết quả GIAM50K/FREESHIP trong biên bản. |
| `ANH_CN_05` / Hình 6.6 | KT-DH-01; trang dự kiến 7 | 6.7 cm | Chụp ngay sau bước 3 của KT-DH-01 khi chuyển tới /order-success?orderCode=...; thấy thông báo thành công và mã đơn thực. Ghi mã vào biên bản DON-A; đối chiếu tổng/địa chỉ trong chi tiết đơn ở bước 4. |
| `ANH_CN_06` / Hình 6.7 | KT-DH-02; trang dự kiến 8 | 6.7 cm | Chụp sau khi nhấn Đặt hàng với ô Số điện thoại rỗng; thấy ô trống và cảnh báo HTML. Có thể mở tab Network bên cạnh nhưng phải ẩn request headers/cookie/token. Đối chiếu không có request checkout mới và lịch sử không tăng. |
| `ANH_CN_07` / Hình 6.8 | KT-DH-04; trang dự kiến 9 | 4.3 cm | Chụp sau khi xác nhận hủy và tải lại chi tiết; thấy cùng mã DON-A, Đã hủy và không còn nút hủy khả dụng. Không chụp riêng hộp xác nhận trước khi thao tác hoàn tất. |
| `ANH_CN_08` / Hình 6.9 | KT-XL-01; trang dự kiến 10 | 6.7 cm | Chụp sau bước 3 của KT-XL-01: /admin/orders/{id}, thấy đúng mã DON-B, chi nhánh HCM, Hoàn thành và tổng tiền. Trước đó ghi lại kết quả từng trạng thái; kiểm tra KH cũng thấy Hoàn thành. |
| `ANH_CN_09` / Hình 6.10 | KT-PQ-01; trang dự kiến 11 | 6.7 cm | Chụp bước 3: Network chọn đúng GET /api/admin/orders/{id DON-C}, thấy Status 403 và Response từ chối. Thu gọn phần headers để không lộ cookie/JWT; không lấy 401 chưa đăng nhập thay cho 403 sai chi nhánh. |

Ảnh lịch sử/chi tiết DON-A trước hủy (KT-DH-03) lưu trong hồ sơ ngoài chương, cùng response/biên bản, để đối chiếu PENDING và số tiền. Chương giữ ảnh đặt thành công và ảnh sau hủy để tiết kiệm trang. Ảnh lỗi đăng nhập, kết quả rỗng, giỏ sau xóa, hai mã ưu đãi còn lại và trạng thái xử lý trung gian cũng lưu ngoài chương; vẫn phải thực hiện đầy đủ tiêu chí.

## Kết quả và bằng chứng

13 ca giao diện hiện **NOT RUN**. Sau khi thực hiện, ghi kết quả quan sát và PASS/FAIL vào dòng “Thực tế” và cập nhật bảng tổng hợp; trường hợp thiếu điều kiện ghi BLOCKED kèm nguyên nhân. Không tự đổi tất cả thành PASS khi chèn ảnh. KT-KM-01 chỉ PASS nếu cả ba bộ dữ liệu đạt.

5 integration test đã chạy lại ngày 21/10/2026, 04:00:09–04:00:42 (+07:00), **5 tests, 0 failures/errors/skipped, BUILD SUCCESS**. Có bảng kết quả thực tế riêng ở cuối Word. XML/log/lệnh nằm tại `kiem_thu/2026-10-21/minh_chung/xac_minh_5_ca_pass/`; không dùng số PASS này thay cho kết quả trình duyệt.

Ảnh chỉ dùng dữ liệu QA. Không để lộ mật khẩu, cookie, JWT hoặc chuỗi kết nối; chú ý vùng điền nhanh demo trên trang login có thể hiển thị mật khẩu. Với Network, chụp Status/Response và thu gọn Headers. Sau khi chèn ảnh, xuất PDF để xác nhận tổng mục 6.2 vẫn không quá 15 trang.
