## 6.2. Kiểm thử

Việc kiểm thử nhằm đánh giá tính đúng đắn của các chức năng chính trong hệ thống quản lý chuỗi cửa hàng mỹ phẩm LUNEA. Nội dung tập trung vào xác thực tài khoản, giỏ hàng theo biến thể sản phẩm, đặt hàng, giải phóng tồn khi hủy đơn và phân quyền theo chi nhánh. Các ca được thiết kế theo phương pháp hộp đen, kết hợp quan sát giao diện, phản hồi API và dữ liệu trước–sau giao dịch.

Môi trường thực hiện gồm ứng dụng chạy với profile local, PostgreSQL, trình duyệt và công cụ kiểm tra API. Khi thực hiện, nhóm ghi lại ngày kiểm thử, phiên bản trình duyệt và bản mã nguồn sử dụng. Mỗi chức năng có luồng hợp lệ và trường hợp ngoại lệ tương ứng.

> **Ghi chú biên soạn:** Đây là bản thảo nội dung kiểm thử. Output và số liệu dưới đây là kết quả mong đợi theo ca thiết kế; chưa phải kết quả đã chạy. Thay các khung chèn ảnh bằng ảnh thực tế, cập nhật bảng kết quả ở cuối và xóa ghi chú này khi hoàn thiện báo cáo. Cách chụp từng ảnh được mô tả trong file HUONG_DAN_CHUP_ANH_KIEM_THU.md.

### 6.2.1. Kiểm thử chức năng đăng nhập tài khoản

**Mục tiêu và điều kiện trước:** Kiểm tra khả năng xác thực khách hàng đang hoạt động. Tài khoản thử đã tồn tại; trình duyệt chưa đăng nhập hoặc đã đăng xuất khỏi tài khoản trước đó.

**Input:** Email khach01@lunea.test và mật khẩu demo 123456 theo dữ liệu khởi tạo. Nhóm xác nhận lại tài khoản và mật khẩu thực tế trước khi thực hiện.

**Output mong đợi:** Đăng nhập thành công, chuyển đến trang phù hợp với tài khoản khách hàng; truy cập được thông tin cá nhân. Hệ thống thiết lập cookie xác thực JWT; thông tin hiển thị thuộc đúng khách hàng vừa đăng nhập.

**Thực hiện:**

1. Truy cập /login, nhập email và mật khẩu của tài khoản thử.
2. Chụp biểu mẫu đã điền trước khi nhấn “Đăng nhập”; giữ mật khẩu ở dạng che.
3. Nhấn “Đăng nhập”, sau đó mở /account/profile.
4. Đối chiếu thông tin tài khoản trên trang hồ sơ; thử thêm trường hợp sai mật khẩu và bỏ trống trường bắt buộc để kiểm tra phản hồi lỗi.

> **[CHÈN HÌNH 6.2 TẠI ĐÂY]**
> Chụp vùng biểu mẫu /login: email đã nhập, mật khẩu được che và nút “Đăng nhập”.

*Hình 6.2. Dữ liệu đầu vào kiểm thử đăng nhập tài khoản khách hàng*

> **[CHÈN HÌNH 6.3 TẠI ĐÂY]**
> Chụp trang /account/profile sau khi đăng nhập: thông tin nhận diện đúng khách hàng và khu vực tài khoản.

*Hình 6.3. Thông tin khách hàng dùng để đối chiếu sau đăng nhập*

<!-- NGAT_TRANG -->

### 6.2.2. Kiểm thử chức năng thêm sản phẩm theo SKU vào giỏ hàng

**Mục tiêu và điều kiện trước:** Kiểm tra việc lưu đúng biến thể và số lượng khách chọn. Khách hàng đã đăng nhập, giỏ hàng ban đầu rỗng và SKU còn hàng khả dụng.

**Input:** Sản phẩm “Serum Bí Đao Cocoon 7% Niacinamide Trị Mụn & Mờ Thâm”, biến thể “Dung tích 70ml”, mã SKU-CCN-BD-70ML, số lượng 1. Giá bán theo dữ liệu seed là 265.000 đồng; cần đối chiếu giá thực tế trước khi thử.

**Output mong đợi:** Giỏ hàng có đúng sản phẩm, biến thể 70ml và số lượng 1; tiền hàng bằng 265.000 đồng. Chỉ thêm vào giỏ chưa làm tăng hàng giữ trong kho. Thêm lại cùng SKU phải gộp số lượng; thêm vượt tồn khả dụng phải bị từ chối.

**Thực hiện:**

1. Từ danh sách sản phẩm, mở trang chi tiết sản phẩm Cocoon tương ứng.
2. Chọn biến thể 70ml và số lượng 1; đối chiếu tên biến thể, mã SKU và giá bán.
3. Nhấn “Thêm vào giỏ hàng”, sau đó mở /cart.
4. Đối chiếu dòng sản phẩm, số lượng và tiền hàng. Với ca gộp số lượng, thêm cùng SKU lần nữa; trước khi chuyển sang kiểm thử đặt hàng, đưa giỏ về 1 sản phẩm để thống nhất dữ liệu.

> **[CHÈN HÌNH 6.4 TẠI ĐÂY]**
> Chụp trang chi tiết: tên sản phẩm, biến thể 70ml đang chọn, giá 265.000 đồng, số lượng 1 và nút thêm giỏ.

*Hình 6.4. Lựa chọn biến thể và số lượng sản phẩm trước khi thêm vào giỏ*

> **[CHÈN HÌNH 6.5 TẠI ĐÂY]**
> Chụp /cart: đúng biến thể 70ml, số lượng 1, đơn giá và tiền hàng 265.000 đồng.

*Hình 6.5. Dữ liệu giỏ hàng dùng để đối chiếu sau thao tác thêm SKU*

**Trường hợp ngoại lệ:** Thử thêm số lượng lớn hơn tồn khả dụng; kết quả mong đợi là thông báo không đủ hàng và giỏ không bị cập nhật sai. Nếu kiểm tra số lượng 0 hoặc âm qua API thêm mới, dữ liệu phải bị từ chối. Ghi phản hồi thực tế trong biên bản kiểm thử.

<!-- NGAT_TRANG -->

### 6.2.3. Kiểm thử chức năng đặt hàng và áp dụng mã ưu đãi

**Mục tiêu và điều kiện trước:** Kiểm tra phép tính tiền, lựa chọn chi nhánh xử lý và giữ tồn khi tạo đơn COD. Giỏ có 1 SKU-CCN-BD-70ML giá 265.000 đồng; địa chỉ giao hàng hợp lệ; có ít nhất một cửa hàng đang hoạt động đủ toàn bộ hàng trong đơn.

**Input:** Thông tin người nhận và địa chỉ giao hàng thử, hình thức “Thanh toán khi nhận hàng (COD)”, mã LUNEA10. Các mã GIAM50K và FREESHIP được thử ở những lượt preview độc lập.

**Output mong đợi:** LUNEA10 giảm 10% tiền hàng, tương ứng 26.500 đồng; phí vận chuyển 30.000 đồng; tổng thanh toán 268.500 đồng. Giá trị trên giao diện phải khớp phản hồi preview và dữ liệu đơn được lưu.

**Thực hiện:**

1. Mở /checkout; xác nhận lại SKU, số lượng và tiền hàng.
2. Chọn hoặc nhập địa chỉ giao hàng đầy đủ; chọn COD.
3. Nhập LUNEA10 và nhấn “Áp dụng”; đối chiếu tiền hàng, giảm giá, phí vận chuyển và tổng thanh toán theo Bảng 6.3. Chụp ảnh trước khi gửi đặt hàng.

| Mã ưu đãi | Tiền hàng | Giảm giá | Phí vận chuyển | Tổng thanh toán |
|---|---:|---:|---:|---:|
| Không dùng mã | 265.000 | 0 | 30.000 | 295.000 |
| LUNEA10 | 265.000 | 26.500 | 30.000 | 268.500 |
| GIAM50K | 265.000 | 50.000 | 30.000 | 245.000 |
| FREESHIP | 265.000 | 0 | 0 | 265.000 |

*Bảng 6.3. Giá trị mong đợi của các mã ưu đãi với bộ dữ liệu kiểm thử, đơn vị: đồng*

> **[CHÈN HÌNH 6.6 TẠI ĐÂY]**
> Chụp checkout sau khi áp dụng LUNEA10: mã, tiền hàng 265.000, giảm 26.500, phí 30.000, tổng 268.500 và COD.

*Hình 6.6. Thông tin thanh toán dùng để kiểm tra phép tính khi áp dụng mã ưu đãi*

**Trường hợp ngoại lệ:** Thử giỏ rỗng, địa chỉ thuộc khách khác và trường hợp không có một chi nhánh đủ toàn bộ đơn; các thao tác này phải bị chặn, không để lại đơn hoặc reservation dở dang. Với mã không được hỗ trợ, không được phát sinh khoản giảm ngoài quy định; đối chiếu cả phản hồi API và thông báo giao diện.

<!-- NGAT_TRANG -->

**Đối chiếu kết quả tạo đơn và giữ tồn**

4. Sau khi đối chiếu đúng tổng tiền, nhấn “Đặt hàng”.
5. Ghi mã đơn, ID đơn và chi nhánh xử lý từ phản hồi tạo đơn; quan sát trang /order-success?orderCode=… và kiểm tra tổng tiền, COD, chi nhánh chuẩn bị hàng.
6. Mở lại giỏ để kiểm tra đã xóa các mặt hàng của đơn; đối chiếu đơn ở trạng thái PENDING và reservation ở trạng thái HELD.
7. Kiểm tra tồn kho đúng SKU, đúng cửa hàng trước–sau tạo đơn. Tồn thực tế giữ nguyên; hàng giữ tăng đúng số lượng đặt; tồn khả dụng giảm tương ứng.

> **[CHÈN HÌNH 6.7 TẠI ĐÂY]**
> Chụp trang đặt thành công với mã đơn thật, chi nhánh chuẩn bị hàng, COD và tổng thanh toán 268.500 đồng.

*Hình 6.7. Thông tin đơn hàng dùng để đối chiếu sau thao tác đặt COD*

Ví dụ, nếu cửa hàng xử lý đang có tồn thực tế 30 và hàng giữ 0, việc đặt 1 sản phẩm sẽ cho số liệu mong đợi như Bảng 6.4. Đây là bộ số liệu minh họa; khi chạy trên dữ liệu khác, nhóm ghi lại giá trị trước thao tác và tính lại giá trị mong đợi tương ứng.

| Thời điểm | Tồn thực tế | Hàng giữ | Khả dụng | Trạng thái reservation của đơn |
|---|---:|---:|---:|---|
| Trước tạo đơn | 30 | 0 | 30 | Chưa có |
| Sau tạo đơn | 30 | 1 | 29 | HELD |
| Sau hủy đơn đang chờ xác nhận | 30 | 0 | 30 | RELEASED |

*Bảng 6.4. Số liệu mong đợi của bộ tồn minh họa khi đặt và hủy 1 sản phẩm*

Ba mã ưu đãi được xử lý như mã mẫu trong chương trình hiện tại. Phần kiểm thử này đánh giá phép tính và sự thống nhất giữa preview với đơn hàng; chưa đưa các điều kiện thời hạn, lượt dùng hoặc phân phối mã vào tiêu chí kết luận.

<!-- NGAT_TRANG -->

### 6.2.4. Kiểm thử chức năng hủy đơn và giải phóng hàng giữ

**Mục tiêu và điều kiện trước:** Kiểm tra đồng bộ trạng thái đơn với tồn kho khi khách hàng hủy trước khi xử lý. Sử dụng chính đơn vừa tạo ở mục 6.2.3; đơn thuộc khách đang đăng nhập, còn PENDING và reservation đang HELD.

**Input:** ID đơn hàng của khách và thao tác “Hủy đơn hàng”.

**Output mong đợi:** Đơn chuyển sang CANCELLED; reservation chuyển RELEASED. Hàng giữ giảm đúng số lượng đã đặt, tồn thực tế không đổi và tồn khả dụng tăng trở lại. Đơn của khách khác hoặc đơn đã qua PENDING không được khách hủy.

**Thực hiện:**

1. Mở /account/orders và chọn đúng đơn đã ghi ở mục trước.
2. Ghi số liệu tồn kho và reservation trước khi hủy, sau đó nhấn “Hủy đơn hàng” và xác nhận trong hộp thoại.
3. Tải lại chi tiết đơn; đối chiếu mã đơn và trạng thái đã hủy.
4. Truy vấn lại cùng SKU và cửa hàng; so sánh hàng giữ, tồn khả dụng và trạng thái reservation trước–sau theo Bảng 6.4.

> **[CHÈN HÌNH 6.8 TẠI ĐÂY]**
> Chụp chi tiết đúng đơn vừa hủy: nhìn rõ mã đơn, trạng thái đã hủy/CANCELLED và mặt hàng của đơn.

*Hình 6.8. Trạng thái đơn hàng dùng để đối chiếu sau yêu cầu hủy của khách*

> **[CHÈN HÌNH 6.9 TẠI ĐÂY]**
> Ghép hai vùng dữ liệu: (a) trước hủy, reservation HELD; (b) sau hủy, RELEASED. Giữ rõ mã đơn, SKU, cửa hàng, tồn thực tế và hàng giữ.

*Hình 6.9. Đối chiếu tồn kho và reservation trước–sau thao tác hủy đơn*

**Trường hợp ngoại lệ:** Chuẩn bị một đơn độc lập đã CONFIRMED để thử khách hủy; kết quả mong đợi là bị từ chối và số liệu không đổi. Thử gửi lại yêu cầu hủy của đơn đã CANCELLED; hệ thống không được giải phóng hàng giữ thêm lần nữa.

<!-- NGAT_TRANG -->

### 6.2.5. Kiểm thử phân quyền theo vai trò và chi nhánh

**Mục tiêu và điều kiện trước:** Kiểm tra việc giới hạn dữ liệu phía máy chủ. Chuẩn bị nhân viên order_hcm thuộc chi nhánh HCM, một đơn HCM và một đơn HN. Xác nhận phân công cửa hàng và ID đơn thực tế trước khi thử.

**Input:** Nhân viên order_hcm đã đăng nhập; request GET /api/admin/orders/{id_don_HN}. ID được lấy từ dữ liệu thử, không suy đoán từ thứ tự đơn.

**Output mong đợi:** Nhân viên xem được đơn thuộc chi nhánh được giao; truy cập đơn HN bị từ chối với mã HTTP 403 và không nhận chi tiết đơn. Khách hàng không được truy cập các chức năng quản trị.

**Thực hiện:**

1. Đăng nhập bằng order_hcm, mở một đơn HCM để làm đối chứng được phép truy cập.
2. Trên cùng trình duyệt đã đăng nhập, gửi GET tới /api/admin/orders/{id_don_HN}; kiểm tra phản hồi trong Network hoặc công cụ API dùng cùng tài khoản.
3. Ghi lại HTTP status và nội dung từ chối; thử thêm tài khoản khách truy cập /admin/orders để kiểm tra quyền quản trị.

> **[CHÈN HÌNH 6.10 TẠI ĐÂY]**
> Chụp URL/API của đơn HN, mã phản hồi 403 và nội dung ACCESS_DENIED trong lượt thử bằng order_hcm; không để lộ cookie/token.

*Hình 6.10. Phản hồi từ chối truy cập đơn hàng ngoài phạm vi chi nhánh*

### 6.2.6. Tổng hợp kết quả kiểm thử

Kết quả được ghi theo từng lượt thực hiện, gồm dữ liệu đầu vào, giá trị quan sát, đánh giá và minh chứng. Một chức năng chỉ được kết luận đạt trong phạm vi các ca đã chạy thành công; trường hợp chưa chạy hoặc gặp lỗi được ghi riêng để tiếp tục kiểm tra.

| Nhóm chức năng | Điểm cần đối chiếu | Kết quả thực tế/đánh giá |
|---|---|---|
| Đăng nhập | Tài khoản và quyền truy cập sau xác thực | Chưa thực hiện |
| Giỏ hàng | Đúng SKU, số lượng, tiền hàng và phản hồi vượt tồn | Chưa thực hiện |
| Đặt hàng, ưu đãi | Tổng tiền, cửa hàng xử lý, giỏ và reservation | Chưa thực hiện |
| Hủy đơn | Trạng thái đơn và giải phóng hàng giữ đúng một lần | Chưa thực hiện |
| Phân quyền | Quyền chức năng và phạm vi đơn theo chi nhánh | Chưa thực hiện |

*Bảng 6.5. Biểu ghi tổng hợp kết quả kiểm thử các chức năng tiêu biểu*

Những sai lệch so với Output mong đợi cần có mô tả lỗi và kết quả kiểm thử lại sau sửa. Nếu bổ sung kết quả kiểm thử tự động, nhóm ghi riêng ngày chạy, môi trường, số tests/failures/errors/skipped từ Maven, tránh suy ra kết quả từ số phương thức test có trong source code.
