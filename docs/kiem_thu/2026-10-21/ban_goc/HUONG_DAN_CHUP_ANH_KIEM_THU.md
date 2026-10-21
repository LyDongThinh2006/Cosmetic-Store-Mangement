# Hướng dẫn chèn ảnh cho phần 6.2 — LUNEA

Tài liệu đi cùng [bản Word phần kiểm thử](CHUONG_6_2_KIEM_THU.docx) và [nội dung Markdown](CHUONG_6_2_KIEM_THU.md). Hai báo cáo mẫu chỉ được dùng để tham khảo cách tổ chức: mô tả Input/Output/các bước, đồng thời chú ý môi trường, ca ngoại lệ và quyền truy cập. Nội dung nghiệp vụ, số liệu và đường dẫn trong bản thảo được viết cho LUNEA.

Bản rút gọn chọn năm luồng minh họa. Bộ 50 mã ca trong kế hoạch trước là tài liệu mở rộng; không được ghi rằng toàn bộ 50 ca đã thực hiện chỉ từ chín hình của bản này.

## 1. Trước khi chụp

1. Khởi động PostgreSQL và ứng dụng profile local theo mục 6.1. Ghi ngày, commit/thay đổi chưa commit, trình duyệt và phiên bản thực tế. Lượt biên soạn này chưa chạy giao diện hoặc kiểm thử chức năng.
2. Xác nhận tài khoản demo còn đúng: khách `khach01@lunea.test`; nhân viên `order_hcm`, `order_hn`; quản trị viên để chuẩn bị/đối chiếu dữ liệu. Mật khẩu demo trong seed là `123456`; dữ liệu trên máy có thể đã được thay đổi.
3. Chọn SKU `SKU-CCN-BD-70ML`, biến thể “Dung tích 70ml” của sản phẩm Serum Bí Đao Cocoon. Giá seed là **265.000 đồng**, khác giá niêm yết 295.000 đồng. Dùng giá bán trong giỏ, không lấy nhầm giá gạch ngang.
4. Xóa các dòng thừa khỏi giỏ thử để chỉ còn SKU cần kiểm tra. Luồng minh họa sử dụng **số lượng 1**. Nếu thử gộp hai lần thì đưa số lượng về 1 trước khi chụp checkout.
5. Kiểm tra SKU còn tồn; ghi số liệu thực tế của từng cửa hàng. Chỉ dùng ví dụ 30/0/30 trong báo cáo khi dữ liệu trước giao dịch thật sự tương ứng. Nếu khác, sửa cả bảng, ảnh và nội dung mong đợi theo số liệu đã ghi.
6. Dùng cùng một tài khoản khách, SKU và **một mã đơn** từ Hình 6.7 đến Hình 6.9. Khi hủy, đơn phải còn PENDING; đừng để nhân viên xác nhận đơn này trước lúc chụp.

Số hình bắt đầu từ **6.2** vì bản 6.1 đã có Hình 6.1. Ba bảng mới tiếp nối Bảng 6.1–6.2 thành **Bảng 6.3–6.5**. Nếu báo cáo tổng thay đổi thứ tự, cập nhật lại cả chú thích và chỗ dẫn chiếu.

## 2. Danh sách ảnh và vị trí chính xác

| Hình | Ảnh cần chụp | Thời điểm chụp | Chèn tại đâu trong bản thảo |
|---|---|---|---|
| 6.2 | Biểu mẫu đăng nhập đã nhập email, mật khẩu đang che | Trước khi nhấn Đăng nhập | Mục 6.2.1, sau các bước, khung đầu tiên |
| 6.3 | Hồ sơ đúng khách hàng sau đăng nhập | Sau đăng nhập, mở /account/profile | Mục 6.2.1, khung thứ hai |
| 6.4 | Chi tiết sản phẩm đã chọn SKU 70ml và số lượng 1 | Trước khi nhấn Thêm vào giỏ hàng | Mục 6.2.2, khung đầu tiên |
| 6.5 | Giỏ có đúng biến thể, số lượng 1 và tiền hàng | Sau thêm, mở /cart | Mục 6.2.2, khung thứ hai |
| 6.6 | Checkout đã áp dụng LUNEA10 | Sau nhấn Áp dụng, trước Đặt hàng | Mục 6.2.3, ngay sau Bảng 6.3 |
| 6.7 | Trang đặt hàng thành công của đơn thật | Sau nhấn Đặt hàng và API trả thành công | Phần tiếp của 6.2.3, sau các bước 4–7, trước Bảng 6.4 |
| 6.8 | Chi tiết đúng đơn vừa hủy, trạng thái đã hủy | Sau hủy và tải lại trang | Mục 6.2.4, khung đầu tiên |
| 6.9 | Hai vùng đối chiếu tồn/reservation trước và sau hủy | Chụp một lần trước và một lần sau hủy | Mục 6.2.4, khung thứ hai; cùng một chú thích hình |
| 6.10 | Request đơn HN bị từ chối khi đăng nhập order_hcm | Sau gửi request với tài khoản HCM | Mục 6.2.5, trước phần tổng hợp |

### Hình 6.2 — Dữ liệu đầu vào đăng nhập

- Mở `http://localhost:8080/login` khi chưa đăng nhập. Điền `khach01@lunea.test` và mật khẩu demo thực tế.
- Cắt vùng form đủ rộng để thấy nhãn “Email hoặc số điện thoại”, email thử, ô mật khẩu dạng chấm và nút “Đăng nhập”. Có thể giữ tiêu đề LUNEA để nhận diện ứng dụng.
- Đây là ảnh **trước thao tác**. Không ghi chú là “đăng nhập thành công” dưới ảnh này.
- Tên lưu gợi ý: `H6_02_dang_nhap_dau_vao.png`.

### Hình 6.3 — Đối chiếu sau đăng nhập

- Nhấn Đăng nhập, chờ chuyển trang rồi mở `/account/profile`.
- Chụp khu vực thông tin cá nhân và phần nhận diện tài khoản. Đối chiếu email/tên đúng khách; theo seed là Lê Hoàng Yến, nhưng phải sử dụng tên thực tế nếu dữ liệu đã đổi.
- Nếu cần chứng minh xử lý JWT, kiểm tra request đăng nhập và cookie ở DevTools, ghi nhận vào biên bản. Không đưa giá trị token vào ảnh báo cáo.
- Tên lưu gợi ý: `H6_03_ho_so_sau_dang_nhap.png`.

### Hình 6.4 — Chọn biến thể sản phẩm

- Từ `/products`, tìm sản phẩm Serum Bí Đao Cocoon, bấm vào tên/ảnh để mở `/products/{id}`. ID được lấy từ dữ liệu thật; không mặc định sản phẩm có ID 1.
- Chọn “Dung tích 70ml”, xác nhận mã `SKU-CCN-BD-70ML`, số lượng 1, giá bán 265.000 đồng.
- Chụp vùng tên, lựa chọn biến thể, giá, số lượng và nút “Thêm vào giỏ hàng”. Nếu mã SKU ở dưới, mở rộng vùng cắt để thấy mã; không cần lấy toàn bộ trang dài.
- Tên lưu gợi ý: `H6_04_chon_sku_70ml.png`.

### Hình 6.5 — Giỏ hàng sau thêm

- Nhấn Thêm vào giỏ hàng, mở `/cart`; tải lại để kiểm tra dữ liệu vẫn tồn tại.
- Cần thấy tên, biến thể 70ml, số lượng **1**, đơn giá và tiền hàng **265.000 đồng**. Phân biệt tiền hàng với tổng có phí giao nếu giao diện trình bày riêng.
- Nếu ảnh đang có 2 sản phẩm do thử gộp, sửa về 1 rồi chụp lại. Nếu dùng số lượng khác thì phải sửa toàn bộ số liệu tiền ở bước sau.
- Tên lưu gợi ý: `H6_05_gio_hang_sau_them.png`.

### Hình 6.6 — Checkout và mã ưu đãi

- Mở `/checkout`, nhập/chọn địa chỉ thử hợp lệ và COD; nhập `LUNEA10`, nhấn Áp dụng.
- Phần ảnh phải thấy ô mã và các dòng: tiền hàng **265.000**, giảm giá **26.500**, phí giao **30.000**, tổng **268.500 đồng**. Giữ thêm vùng COD nếu bố cục cho phép.
- Kiểm tra Network: `POST /api/orders/preview` trả đúng `subtotal`, `discountAmount`, `shippingFee`, `totalAmount`. Một thông báo “thành công” trên giao diện chưa đủ chứng minh số liệu được máy chủ xác nhận.
- Source có nhánh tự tính ở trình duyệt khi preview API lỗi, và backend hiện không báo lỗi riêng cho mọi mã lạ. Nếu API lỗi hoặc nhập mã lạ vẫn xuất hiện thông báo thành công, ghi lại đúng hiện tượng; không dùng ảnh đó để kết luận ca hợp lệ đạt. Mã lạ không được tạo khoản giảm ngoài ba mã mẫu.
- Thử GIAM50K/FREESHIP bằng các lượt preview riêng, rồi áp dụng lại LUNEA10 trước khi tạo đơn minh họa. Không cần tạo ba đơn chỉ để chụp Bảng 6.3.
- Tên lưu gợi ý: `H6_06_checkout_lunea10.png`.

### Hình 6.7 — Đơn được tạo

- Trước khi nhấn Đặt hàng, lưu dữ liệu tồn theo truy vấn ở mục 3 để có mốc **trước tạo đơn**. Sau khi tạo đơn, đối chiếu đúng dòng cửa hàng được gán.
- Nhấn Đặt hàng. Trong Network lưu thông tin cần đối chiếu từ response `POST /api/orders/checkout`: `id`, `orderCode`, `fulfillmentStoreId`, `fulfillmentStoreName`, `status`, `totalAmount`. Endpoint tạo thành công trả HTTP 201.
- Chụp `/order-success?orderCode=...`: mã đơn, chi nhánh chuẩn bị hàng, COD và tổng thanh toán. Dữ liệu phải khớp response và đơn trong CSDL.
- Template có một số giá trị dự phòng khi không tải được đơn. Vì vậy phải đối chiếu với response/CSDL trước khi coi một trang có tiêu đề “thành công” là minh chứng tạo đơn thật.
- Ghi riêng **ID số của đơn** và **mã hiển thị của đơn**: URL `/account/orders/{id}` nhận ID số; truy vấn bằng `order_code` nhận mã hiển thị.
- Tên lưu gợi ý: `H6_07_dat_hang_thanh_cong.png`.

### Hình 6.8 — Đơn sau hủy

- Trước khi hủy, chụp/lưu phần (a) cho Hình 6.9. Sau đó mở `/account/orders/{id}` của đúng đơn ở Hình 6.7.
- Kiểm tra đơn còn PENDING, nhấn “Hủy đơn hàng”, chấp nhận hộp thoại xác nhận rồi tải lại.
- Chụp mã đơn, trạng thái đã hủy/CANCELLED và dòng sản phẩm. Chỉ có toast hủy thành công chưa đủ minh chứng trạng thái đã được lưu.
- Tên lưu gợi ý: `H6_08_don_hang_da_huy.png`.

### Hình 6.9 — Tồn và reservation trước–sau hủy

- **Phần (a):** Sau đặt đơn, trước hủy. Cùng cửa hàng/SKU có hàng giữ tăng thêm 1; reservation của đúng mã đơn là HELD.
- **Phần (b):** Sau hủy. Tồn thực tế không đổi, hàng giữ giảm 1; reservation của đơn thành RELEASED.
- Chạy lại cùng truy vấn ở mục 3 và chụp hai thời điểm. Đặt hai ảnh lần lượt hoặc cạnh nhau dưới cùng chú thích Hình 6.9; ghi rõ “(a) Trước hủy” và “(b) Sau hủy”.
- Cần thấy `order_code`, tên cửa hàng, `sku_code`, tồn thực tế, hàng giữ, khả dụng, số lượng reservation và trạng thái. Đây là hai ảnh dữ liệu thật, không phải vẽ lại bảng mong đợi để thay minh chứng.
- Tên lưu gợi ý: `H6_09a_truoc_huy.png`, `H6_09b_sau_huy.png`.

### Hình 6.10 — Quyền theo chi nhánh

- Chuẩn bị một đơn HCM và một đơn HN; lấy ID/chi nhánh bằng tài khoản quản trị hoặc CSDL. Không dùng một đơn không tồn tại để làm ca bị chặn quyền.
- Dùng profile trình duyệt riêng đăng nhập `order_hcm`, thử `/api/admin/orders/{id_don_HCM}` để xác nhận ca đối chứng được phép.
- Trên **cùng profile** gửi GET `/api/admin/orders/{id_don_HN}`. Có thể mở URL trực tiếp trên trình duyệt; nếu cần xem HTTP status thì mở Network rồi tải lại. Cookie xác thực được trình duyệt gửi tự động. Với Postman, phải cấu hình đúng tài khoản tương ứng.
- Chụp URL/request, HTTP 403 và nội dung từ chối/ACCESS_DENIED. Không chụp cookie hoặc Authorization. Lưu tài khoản thực hiện trong biên bản để chứng minh ca dùng order_hcm.
- HTTP 401 là chưa xác thực; HTTP 404 là sai ID; cả hai chưa chứng minh được ca nhân viên đã đăng nhập nhưng bị chặn do chi nhánh.
- Tên lưu gợi ý: `H6_10_chan_don_khac_chi_nhanh.png`.

## 3. Truy vấn chỉ đọc để đối chiếu dữ liệu

Các tên cột dưới đây đối chiếu từ entity hiện tại: tồn thực tế là `on_hand_qty`, hàng giữ là `reserved_qty`. Không dùng tên thuộc tính Java `actualStock`, `heldQuantity` làm tên cột SQL.

### 3.1. Trước và sau tạo đơn

Chạy trước đặt hàng và ngay sau đặt hàng. Dữ liệu trả về gồm các cửa hàng có bản ghi tồn của SKU; khi đối chiếu sau đặt, chọn đúng `store_id` của cửa hàng được gán cho đơn.

```sql
SELECT s.id AS store_id, s.name AS cua_hang, k.sku_code,
       i.on_hand_qty AS ton_thuc_te,
       i.reserved_qty AS hang_giu,
       i.on_hand_qty - i.reserved_qty AS kha_dung
FROM inventories i
JOIN stores s ON s.id = i.store_id
JOIN skus k ON k.id = i.sku_id
WHERE k.sku_code = 'SKU-CCN-BD-70ML'
ORDER BY s.id;
```

### 3.2. Trước và sau hủy đúng đơn

Thay `MA_DON_THUC_TE` bằng `orderCode` thật đã ghi từ Hình 6.7, giữ dấu nháy đơn SQL. Nếu chưa trả ra bản ghi nào, kiểm tra lại mã đơn/reservation trước khi chụp; không điền số liệu mẫu vào kết quả truy vấn.

```sql
SELECT o.id AS order_id, o.order_code, o.status AS trang_thai_don,
       s.name AS cua_hang, k.sku_code,
       i.on_hand_qty AS ton_thuc_te,
       i.reserved_qty AS hang_giu,
       i.on_hand_qty - i.reserved_qty AS kha_dung,
       r.quantity AS so_luong_giu_cua_don,
       r.status AS trang_thai_reservation
FROM orders o
JOIN order_reservations r ON r.order_id = o.id
JOIN stores s ON s.id = r.store_id
JOIN skus k ON k.id = r.sku_id
JOIN inventories i ON i.store_id = r.store_id AND i.sku_id = r.sku_id
WHERE o.order_code = 'MA_DON_THUC_TE'
ORDER BY k.sku_code;
```

## 4. Chèn vào Word và hoàn thiện kết quả

- Bản Word giữ phông Times New Roman 13, khổ A4 và lề như phần 6.1. Đã bố trí năm chỗ ngắt trang để tổ chức bản thảo thành sáu phần trang; số trang thực tế có thể tăng nếu ảnh cao hoặc nội dung được sửa.
- Thay khung màu nhạt bằng ảnh ở đúng vị trí; giữ chú thích bên dưới. Có thể xóa bảng khung rồi Insert → Pictures tại chỗ đó. Hình 6.9 dùng hai ảnh thật dưới một chú thích.
- Cắt ảnh vào vùng cần quan sát; giữ tỷ lệ ảnh, tên ứng dụng/ngữ cảnh và nhãn dữ liệu. Ảnh quá cao nên tách bố cục hoặc thu gọn vùng cắt thay vì kéo méo chữ. Chiều ngang tối đa theo vùng nội dung là khoảng 16 cm.
- Nếu cần rút số trang, đặt hai ảnh đầu vào/kết quả cùng hàng khi vẫn đọc rõ, giữ nguyên thứ tự Hình 6.2–6.3 hoặc 6.4–6.5. Word sẽ dàn lại khi ghép với chương tổng.
- Hình chỉ chứng minh đúng tình huống đã chụp. Các ca sai mật khẩu, vượt tồn, mã lạ, hủy sai trạng thái, quyền quản trị và quyền chi nhánh cần ghi riêng vào biên bản trước khi kết luận đã kiểm tra đầy đủ các nhánh nêu trong báo cáo.
- Điền Bảng 6.5: kết quả thực tế, đạt/không đạt/chưa thực hiện/bị chặn và mã ảnh hoặc biên bản. Ví dụ một dòng được ghi “Đạt” chỉ khi có quan sát thực tế khớp tiêu chí; không chép Output mong đợi vào cột kết quả khi chưa chạy.
- Nếu khác số liệu seed, cập nhật mọi chỗ liên quan: Input, Bảng 6.3, tổng tiền, các ảnh và Bảng 6.4. Nếu API hoặc giao diện có lỗi, ghi đúng lỗi và kiểm thử lại sau sửa.
- Xóa ghi chú biên soạn, khung hướng dẫn và nội dung chỉ dành cho người chụp khỏi bản nộp. Giữ lại nội dung nghiệp vụ, ảnh, chú thích và kết quả thực tế.

## 5. Căn cứ mã nguồn cho người đối chiếu

| Nội dung | Thành phần đã đối chiếu |
|---|---|
| Đăng nhập, hồ sơ | `AuthPageController`, `AuthRestController`, `AccountServiceImpl`, `auth/login.html`, `account/profile.html` |
| SKU và giá mẫu | `DataSeeder.createSku`, `ProductSkuEntity`; SKU Cocoon 70ml giá bán 265.000, giá niêm yết 295.000 |
| Thêm/gộp giỏ, kiểm tra tồn | `CartServiceImpl`, `CartRestController`, `product-detail.html`, `cart.html` |
| Ưu đãi, đặt COD, giữ tồn | `OrderServiceImpl.previewOrder/placeOrder`, `OrderRestController`, `checkout.html`, `order-success.html` |
| Hủy và giải phóng reservation | `OrderServiceImpl.cancelOrder/updateStatus`, `OrderStatus`, `ReservationStatus`, `account/order-detail.html` |
| Quyền đơn theo chi nhánh | `StoreScope`, `OrderServiceImpl.getById(AuthPrincipal, ...)`, `AdminOrderRestController`, `ErrorCode.ACCESS_DENIED` |
| Tên bảng/cột truy vấn | `InventoryEntity`, `StoreEntity`, `ProductSkuEntity`, `OrderEntity`, `OrderReservationEntity` |
