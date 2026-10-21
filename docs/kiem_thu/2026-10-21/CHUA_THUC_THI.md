# Kiểm thử chưa được thực thi

37 NOT RUN; 1 BLOCKED. Chưa chạy thủ công trình duyệt/API hoặc SMTP, không có kết quả E2E. Các ca NOT RUN là phạm vi còn lại, không phải lỗi phần mềm.

| ID | Ưu tiên | Chức năng | Trạng thái | Điều kiện còn thiếu |
| --- | --- | --- | --- | --- |
| MAN-AUTH-01 | P0 | Đăng nhập thật, cookie và tài khoản bị khóa | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-AUTH-02 | P0 | Đăng ký với OTP hợp lệ và dữ liệu trùng/không hợp lệ | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-AUTH-03 | P0 | Đặt lại mật khẩu và ngăn tái sử dụng OTP | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-AUTH-04 | P0 | Đăng xuất, token bị sửa và thay đổi quyền trong phiên | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-AUTH-05 | P0 | Biên OTP, giới hạn gửi lại và số lần nhập sai | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ACCOUNT-01 | P1 | Hồ sơ và địa chỉ theo chủ sở hữu | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ACCOUNT-02 | P2 | Lưu Beauty Profile qua API hiện có | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-WISH-01 | P2 | Yêu thích, loại trùng và chuyển giỏ | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-CATALOG-01 | P1 | Danh sách, tìm kiếm, lọc và sắp xếp | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-CATALOG-02 | P1 | Chi tiết sản phẩm, SKU, tồn chi nhánh và gợi ý liên quan | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-CATALOG-03 | P1 | CRUD và trạng thái sản phẩm qua API quản trị | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-CATALOG-04 | P1 | CRUD danh mục và thương hiệu | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-CART-01 | P1 | Thêm, cộng gộp, cập nhật và xóa SKU trong giỏ | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-CART-02 | P1 | Giỏ hàng với số lượng không hợp lệ và vượt tồn | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ORD-01 | P0 | Công thức tiền và dữ liệu biên mã ưu đãi | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ORD-02 | P0 | Đặt COD, chọn chi nhánh và snapshot đơn | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ORD-03 | P0 | Không có một chi nhánh đủ toàn bộ giỏ | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ORD-04 | P0 | Không dùng địa chỉ nhận hàng của khách khác | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ORD-05 | P1 | Mã không biết và lỗi preview trên giao diện | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ORD-06 | P0 | Lịch sử, chi tiết và dữ liệu theo chủ đơn/chi nhánh | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ORD-07 | P0 | Chuyển trạng thái hợp lệ, sai thứ tự và lặp yêu cầu | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ORD-08 | P0 | Hủy đơn theo tác nhân và trạng thái | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-INV-01 | P0 | Tồn khả dụng, hàng giữ và định nghĩa tồn thấp | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-INV-02 | P0 | Điều chỉnh tồn hợp lệ và biên nhỏ hơn hàng giữ | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-PUR-01 | P1 | Nhà cung cấp qua API | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-PUR-02 | P0 | Nhập hàng, xác nhận và ngăn cộng tồn hai lần | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-XFER-01 | P0 | Tạo, xuất và nhận điều chuyển đúng một lần | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-RETURN-01 | P1 | Tạo yêu cầu trả theo chủ đơn, trạng thái và số lượng | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-RETURN-02 | P0 | Duyệt trả và nhập lại tồn một lần | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-REVIEW-01 | P1 | Đánh giá, giới hạn sao và kiểm duyệt | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-EMP-01 | P1 | Quản lý nhân viên, trạng thái và chi nhánh | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-STORE-01 | P1 | Chi nhánh, giờ hoạt động và tọa độ qua API | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-ROLE-01 | P0 | Danh sách vai trò và gán vai trò có thật | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-PQ-01 | P0 | Người chưa đăng nhập và bộ lọc yêu cầu ghi | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-PQ-02 | P0 | Ma trận quyền chức năng và đường truy cập thay thế | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-PQ-03 | P0 | Giới hạn chi nhánh ngoài module đơn hàng | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-DASH-01 | P1 | Chỉ số tổng quan theo đơn hoàn tất và bộ lọc chi nhánh | NOT RUN | Cần fixture F-MAN, phiên QA thật và biên bản từng biến thể theo danh sách test case. |
| MAN-EMAIL-01 | P1 | Gửi OTP qua SMTP và nhận thư thực tế | BLOCKED | Chưa có cấu hình SMTP QA và hộp thư nhận được chỉ định để kiểm thử giao thư thật; integration chủ động dùng no-op, không gửi ra ngoài. |

Ưu tiên tiếp theo: các ca ORD/INV/PUR/XFER/PQ mức P0, sau khi sửa và kiểm tra lại 10 defect đã xác minh; kế đến luồng tài khoản/OTP thật, returns, quản trị và dashboard. Kiểm tra concurrency bổ sung giữa checkout–transfer–cancel–return vẫn chưa chạy; QA-INT-15 chỉ chứng minh một lịch checkout–checkout. Chưa đo hiệu năng, tải kéo dài, accessibility, đa trình duyệt, khả năng khôi phục sự cố hoặc E2E triển khai production.
