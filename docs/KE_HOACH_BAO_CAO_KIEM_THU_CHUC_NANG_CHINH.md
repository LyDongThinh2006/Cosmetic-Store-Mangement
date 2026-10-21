# Kế hoạch viết báo cáo kiểm thử các chức năng chính — LUNEA

Ngày lập: **20/10/2026**.

Tài liệu này lập kế hoạch cho **mục 6.2. Kiểm thử** của báo cáo. Phạm vi đã được đối chiếu với source code hiện tại, các lớp kiểm thử và mẫu Nhóm 17. Các trường hợp dưới đây là ca **dự kiến thực hiện**, chưa phải kết quả chạy kiểm thử trong lần lập kế hoạch này.

## 1. Phạm vi và cách trình bày

Tập trung vào tám nhóm chức năng xuyên suốt hoạt động của hệ thống: tài khoản, sản phẩm, giỏ hàng, đặt hàng, xử lý đơn, tồn kho, phân quyền và trả hàng. Ưu tiên chứng minh ba điểm: người dùng thực hiện đúng nghiệp vụ; tiền và tồn kho được tính đúng; dữ liệu được bảo vệ theo người dùng, vai trò và chi nhánh.

Giữ cách viết của mẫu [Nhóm 17](Nhom17_BaoCaoCuoiKy.docx): **Input – Output mong đợi – Thực hiện – Hình minh họa**. Bổ sung điều kiện trước, bảng ca kiểm thử và kết quả thực tế để phần báo cáo có thể kiểm chứng. Giữ cách trình bày của mẫu nhưng dùng đúng cơ chế, giao diện và quy tắc của LUNEA.

| Nhóm chức năng | Nội dung trọng tâm | Kênh thực hiện | Mã ca dự kiến | Số mã ca |
|---|---|---|---|---:|
| Tài khoản và OTP | Đăng ký, đăng nhập, đăng xuất, khôi phục mật khẩu; OTP đúng/sai/hết hạn/đã dùng | Giao diện, API, CSDL; hộp thư khi cấu hình SMTP thật | AUTH-01 đến AUTH-14 | 14 |
| Sản phẩm | Tìm kiếm, lọc, xem SKU; tạo, sửa và ngừng kinh doanh sản phẩm | Giao diện khách; API quản lý; giao diện admin cho thao tác hiện có | SP-01 đến SP-06 | 6 |
| Giỏ hàng | Thêm đúng SKU, gộp số lượng, sửa, xóa, kiểm tra số lượng và tồn khả dụng | Giao diện và API | GH-01 đến GH-06 | 6 |
| Đặt hàng và ưu đãi | COD, địa chỉ, chọn cửa hàng đủ hàng, tính tiền, giữ tồn, ba mã ưu đãi mẫu | Giao diện, API và CSDL | DH-01 đến DH-08 | 8 |
| Xử lý và hủy đơn | Chuyển trạng thái đúng quy trình, hủy đơn, giải phóng hàng giữ, tránh trừ tồn lặp | Giao diện khách/admin, API và CSDL | XL-01 đến XL-05 | 5 |
| Tồn kho | Tồn thực tế, hàng giữ, tồn khả dụng; điều chỉnh tồn và lịch sử điều chỉnh | Giao diện/API và CSDL | KHO-01 đến KHO-03 | 3 |
| Phân quyền | Khách vãng lai, quyền chức năng, quyền sở hữu dữ liệu và phạm vi đơn hàng theo chi nhánh | URL trực tiếp và API, kết hợp giao diện | PQ-01 đến PQ-04 | 4 |
| Trả hàng | Điều kiện tạo yêu cầu, số lượng trả, duyệt/từ chối, nhập lại kho | Giao diện khi có thao tác tương ứng; API và CSDL | TH-01 đến TH-04 | 4 |
| **Tổng** | | | | **50** |

Đây là **50 mã ca thiết kế**, khác với số phương thức test tự động. Một mã ca có thể có nhiều biến thể dữ liệu; khi thực hiện phải ghi từng biến thể riêng, ví dụ `AUTH-03-DK` cho OTP đăng ký và `AUTH-03-QMK` cho OTP khôi phục mật khẩu. Tổng lượt chạy và tỷ lệ đạt được tính từ các lượt đã ghi nhận, không mặc định bằng 50.

Đánh giá sản phẩm, yêu thích, hồ sơ/địa chỉ, nhập hàng và điều chuyển được đưa vào bảng bổ sung hoặc phụ lục nếu cần mở rộng. Thanh toán trực tuyến, quản trị voucher đầy đủ và gợi ý cá nhân hóa theo Beauty Profile không thuộc tám nhóm chính này. Hiện `PaymentMethod` chỉ có COD và mã ưu đãi đang được xử lý bằng ba mã cố định trong service.

## 2. Dàn mục báo cáo đề xuất

```text
6.2. Kiểm thử
     Đoạn mở đầu: mục tiêu, phạm vi, môi trường, phương pháp và dữ liệu thử
6.2.1. Kiểm thử chức năng tài khoản và xác thực OTP
6.2.2. Kiểm thử chức năng tra cứu và quản lý sản phẩm
6.2.3. Kiểm thử chức năng giỏ hàng
6.2.4. Kiểm thử chức năng đặt hàng và áp dụng mã ưu đãi
6.2.5. Kiểm thử chức năng xử lý và hủy đơn hàng
6.2.6. Kiểm thử chức năng tra cứu và điều chỉnh tồn kho
6.2.7. Kiểm thử phân quyền và phạm vi chi nhánh
6.2.8. Kiểm thử chức năng yêu cầu trả hàng
6.2.9. Tổng hợp kết quả kiểm thử
```

Dự kiến khoảng **10–14 trang** cho mục 6.2, tùy cách dàn ảnh và bảng. Trình bày kỹ các luồng đăng nhập, giỏ hàng, đặt hàng và thay đổi tồn kho; những ca lặp lại theo dữ liệu có thể đưa vào phụ lục. Dự kiến chọn 10–12 ảnh minh chứng tiêu biểu, thay vì mỗi ca dùng một ảnh riêng. Số hình cụ thể tiếp nối phần 6.1 sau khi ghép chương.

## 3. Khuôn viết thống nhất cho mỗi chức năng

Mỗi mục 6.2.1–6.2.8 gồm:

1. **Mục tiêu:** hành vi hoặc quy tắc cần kiểm chứng; liên kết yêu cầu/use case tương ứng trong báo cáo hiện tại.
2. **Điều kiện trước:** tài khoản, vai trò, chi nhánh, trạng thái đơn, sản phẩm/SKU, dữ liệu tồn và cấu hình cần có.
3. **Input:** dữ liệu nhập cụ thể, tránh chỉ ghi chung chung “thông tin hợp lệ”.
4. **Output mong đợi:** giao diện, phản hồi API và thay đổi dữ liệu cần quan sát.
5. **Thực hiện:** các bước đánh số; dùng đúng đường dẫn, nút và thao tác của bản phần mềm được kiểm thử.
6. **Minh chứng:** ảnh chạy thật; với tiền và tồn kho có thêm bảng số liệu hoặc dữ liệu API/CSDL trước–sau.
7. **Bảng kết quả:** một dòng cho từng ca/biến thể đã chạy; có kết quả thực tế và đánh giá.

Mẫu biên bản dùng để thu thập dữ liệu trước khi viết Word:

| Mã ca/biến thể | Yêu cầu/UC | Điều kiện và Input | Các bước | Output mong đợi | Kết quả thực tế | Đánh giá | Minh chứng |
|---|---|---|---|---|---|---|---|
| … | … | … | … | … | Chưa thực hiện | Chưa thực hiện | … |

Mỗi lần chạy ghi thêm ngày, bản mã nguồn, trình duyệt/công cụ API và môi trường CSDL. Giữ cùng mã SKU, cửa hàng và mã đơn giữa ảnh thao tác với bảng đối chiếu. Trường “Kết quả thực tế” chỉ điền sau khi quan sát; nếu hành vi khác yêu cầu thì ghi lỗi, không sửa tiêu chí mong đợi để làm ca kiểm thử đạt.

## 4. Các ca kiểm thử dự kiến

### 4.1. Tài khoản và xác thực OTP — 14 mã ca

| Mã ca | Tình huống cần thực hiện | Output mong đợi/điểm cần quan sát |
|---|---|---|
| AUTH-01 | Đăng ký bằng email chưa tồn tại và OTP hợp lệ | Tạo tài khoản khách hàng cùng hồ sơ khách; OTP được đánh dấu đã dùng; đăng nhập được bằng thông tin mới. |
| AUTH-02 | Đăng ký/gửi OTP đăng ký với email đã tồn tại | Từ chối thao tác trùng email; không tạo thêm tài khoản. |
| AUTH-03 | Nhập OTP sai | Không hoàn tất đăng ký/đổi mật khẩu; ghi nhận số lần sai. Chạy riêng cho hai loại OTP. |
| AUTH-04 | Dùng OTP đã quá thời hạn | Từ chối mã hết hạn; không tạo tài khoản/đổi mật khẩu. Chạy riêng cho hai loại OTP. |
| AUTH-05 | Dùng lại OTP đã sử dụng thành công | Từ chối tái sử dụng; dữ liệu tài khoản không bị thay đổi thêm. |
| AUTH-06 | Gửi lại OTP trước và sau mốc chờ | Trước 30 giây bị chặn; sau thời gian chờ được cấp mã mới; mã cũ chưa dùng bị vô hiệu hóa. Ghi riêng từng lượt và thời điểm. |
| AUTH-07 | Nhập sai liên tiếp đến giới hạn 5 lần | Lượt sai thứ năm làm mã không còn dùng được; kiểm tra tiếp bằng một request mới và đối chiếu CSDL. |
| AUTH-08 | Đăng nhập với tài khoản đang hoạt động, mật khẩu đúng | Đăng nhập thành công; tên người dùng và trang chuyển đến phù hợp vai trò. Chạy riêng với khách và các vai trò nhân viên đại diện. |
| AUTH-09 | Đăng nhập bằng mật khẩu sai | Từ chối đăng nhập, phản hồi rõ ràng; không cấp quyền truy cập mới. |
| AUTH-10 | Bỏ trống trường bắt buộc trên form đăng nhập | Hiển thị kiểm tra dữ liệu; không đăng nhập thành công. |
| AUTH-11 | Đăng nhập tài khoản ngừng hoạt động | Từ chối truy cập dù mật khẩu đúng. |
| AUTH-12 | Khôi phục mật khẩu với email đã đăng ký và OTP hợp lệ | Mật khẩu mới đăng nhập được; mật khẩu cũ không còn đăng nhập được; OTP đã dùng không tái sử dụng được. |
| AUTH-13 | Yêu cầu khôi phục mật khẩu với email chưa có tài khoản | Không tạo OTP khôi phục gắn với tài khoản không tồn tại; ghi nhận phản hồi API/giao diện. |
| AUTH-14 | Đăng xuất rồi truy cập lại trang/API yêu cầu đăng nhập | Cookie xác thực được xóa; request tiếp theo không còn quyền của phiên đăng nhập vừa kết thúc. |

Thông số hiện tại trong `OtpServiceImpl`: OTP sáu chữ số, hiệu lực 5 phút, gửi lại sau 30 giây, tối đa 5 lần sai. Kiểm tra các giới hạn qua nhiều request độc lập và CSDL thực tế; mock trong unit test chưa đủ chứng minh bộ đếm được lưu sau một request bị lỗi. Thử mốc hết hạn trước/sau 5 phút; phép thử đúng thời điểm bằng 5 phút dùng đồng hồ kiểm soát nếu có, tránh kết luận từ thao tác tay không chính xác thời gian.

OTP đăng ký gắn với email trước khi tạo tài khoản; OTP khôi phục gắn với tài khoản đã tồn tại. Nếu sử dụng Gmail để kiểm thử thì đó là địa chỉ nhận email, không phải luồng đăng nhập OAuth bằng Google. Minh chứng nhận email thật được thu khi SMTP đã cấu hình; lấy mã từ môi trường phát triển chỉ chứng minh phần xác thực, chưa chứng minh gửi thư đến hộp thư thành công.

### 4.2. Tra cứu và quản lý sản phẩm — 6 mã ca

| Mã ca | Tình huống cần thực hiện | Output mong đợi/điểm cần quan sát |
|---|---|---|
| SP-01 | Tìm theo tên có dữ liệu và từ khóa không có kết quả | Danh sách phù hợp từ khóa; trường hợp rỗng hiển thị rõ ràng. Ghi hai biến thể riêng. |
| SP-02 | Lọc theo danh mục, thương hiệu và khoảng giá | Sản phẩm thỏa các bộ lọc đã chọn; ghi dữ liệu danh mục/giá dùng để đối chiếu. |
| SP-03 | Xem chi tiết và đổi SKU/biến thể | Tên biến thể, giá và tồn khả dụng theo cửa hàng khớp dữ liệu. |
| SP-04 | Tạo sản phẩm hợp lệ bằng tài khoản có `CATALOG_MANAGE` | API tạo thành công; truy vấn lại được sản phẩm và thông tin liên kết. |
| SP-05 | Cập nhật thông tin sản phẩm hợp lệ | API lưu đúng thay đổi; tải lại giao diện/API thấy dữ liệu mới. |
| SP-06 | Ngừng kinh doanh sản phẩm và thử thêm vào giỏ | Trạng thái được cập nhật; sản phẩm ngừng bán không được thêm mới vào giỏ. |

Thao tác tạo/sửa dùng API khi bản giao diện hiện tại chưa có form tương ứng; không dùng ảnh thiết kế màn hình làm bằng chứng đã thao tác thành công. Bộ kiểm thử tự động hiện có chỉ bao phủ một phần quyền API và đồng bộ trạng thái; tìm kiếm/lọc, SKU và thay đổi trên trình duyệt cần kiểm tra thêm.

### 4.3. Giỏ hàng — 6 mã ca

| Mã ca | Tình huống cần thực hiện | Output mong đợi/điểm cần quan sát |
|---|---|---|
| GH-01 | Thêm SKU đang bán, số lượng 1 | Đúng SKU, biến thể, số lượng, đơn giá và thành tiền; hàng giữ trong kho chưa tăng. |
| GH-02 | Thêm lại cùng SKU | Gộp số lượng vào dòng đã có; thành tiền và số lượng hiển thị cập nhật đúng. |
| GH-03 | Cập nhật số lượng dương trong giới hạn tồn | Lưu số lượng mới, tính lại tổng tiền. |
| GH-04 | Xóa một mặt hàng khỏi giỏ | Mặt hàng được xóa; tổng tiền và số lượng cập nhật. |
| GH-05 | Thêm mới với số lượng 0 hoặc âm qua API | DTO từ chối số lượng không hợp lệ; giỏ không phát sinh dòng sai. |
| GH-06 | Thêm/gộp số lượng vượt tồn khả dụng toàn hệ thống | Từ chối vượt tồn; số lượng và tổng tiền của giỏ vẫn đúng trạng thái trước thao tác thất bại. |

Lưu ý phân biệt thêm mới và cập nhật: API thêm mới yêu cầu số lượng từ 1; service cập nhật hiện xóa mặt hàng khi số lượng không dương. Không áp cùng một Output cho hai thao tác này. Tồn khả dụng ở bước thêm giỏ được cộng theo các bản ghi tồn của SKU; đến bước đặt hàng còn phải có một cửa hàng đáp ứng toàn bộ đơn.

### 4.4. Đặt hàng và mã ưu đãi — 8 mã ca

| Mã ca | Tình huống cần thực hiện | Output mong đợi/điểm cần quan sát |
|---|---|---|
| DH-01 | Đặt COD với giỏ và địa chỉ hợp lệ, không dùng mã | Tạo đơn `PENDING`, đúng tiền và cửa hàng xử lý; lưu giá chụp tại thời điểm đặt; tạo reservation `HELD`, tăng hàng giữ, xóa giỏ. Chạy thêm biến thể biên phí vận chuyển. |
| DH-02 | Đặt hàng khi giỏ rỗng | Không tạo đơn hoặc reservation; phản hồi giỏ rỗng. |
| DH-03 | Tổng tồn toàn chuỗi đủ nhưng không có một cửa hàng đủ toàn bộ đơn | Từ chối đặt hàng; không chia đơn sang nhiều chi nhánh, không để lại đơn/reservation/địa chỉ phát sinh dở dang do giao dịch thất bại. |
| DH-04 | Dùng ID địa chỉ thuộc khách khác | Từ chối truy cập địa chỉ; không tạo đơn bằng dữ liệu của khách khác. |
| DH-05 | Áp dụng `LUNEA10` | Giảm 10% tiền hàng, tối đa 100.000 đồng; tiền preview, giao diện và đơn lưu thống nhất. |
| DH-06 | Áp dụng `GIAM50K` | Giảm 50.000 đồng; tổng thanh toán không âm; preview khớp đơn lưu. |
| DH-07 | Áp dụng `FREESHIP` | Phí vận chuyển bằng 0; tổng tiền đúng. |
| DH-08 | Nhập mã không tồn tại | Không phát sinh khoản giảm ngoài ba mã hỗ trợ; ghi rõ phản hồi giao diện và API. Không tự suy ra API phải từ chối toàn bộ đơn hàng. |

Kiểm tra riêng các mốc tiền hàng 499.999, 500.000 và 500.001 đồng cho phí vận chuyển; mốc 1.000.000 đồng và trên mốc đó cho giới hạn `LUNEA10`. Phí giao hàng hiện được xét theo tiền hàng trước giảm giá: dưới 500.000 đồng là 30.000 đồng, từ 500.000 đồng là 0, trừ khi dùng `FREESHIP`.

Ba mã trên là mã mẫu cố định trong `OrderServiceImpl`. Báo cáo chỉ kết luận về phép tính và phản hồi đang hỗ trợ; không khẳng định đã kiểm thử hạn sử dụng, lượt dùng, đối tượng áp dụng hoặc chức năng quản trị voucher khi chưa có thành phần thực thi tương ứng.

### 4.5. Xử lý và hủy đơn — 5 mã ca

| Mã ca | Tình huống cần thực hiện | Output mong đợi/điểm cần quan sát |
|---|---|---|
| XL-01 | Nhân viên chuyển đủ luồng `PENDING → CONFIRMED → PREPARING → SHIPPING → COMPLETED` | Từng bước hợp lệ; khi `SHIPPING` trừ tồn thực tế và giải phóng hàng giữ, reservation chuyển `COMMITTED`; hoàn tất không trừ tồn thêm lần nữa. |
| XL-02 | Khách hủy đơn `PENDING` của mình | Đơn `CANCELLED`, reservation `RELEASED`, hàng giữ được giải phóng; tồn thực tế chưa bị trừ. |
| XL-03 | Khách hủy đơn đã qua `PENDING` | Từ chối thao tác; trạng thái và tồn kho không đổi. |
| XL-04 | Chuyển trạng thái trái quy trình | Ví dụ `PENDING → SHIPPING` bị chặn; không thay đổi tồn. Chạy thêm biến thể hủy bởi nhân viên theo các trạng thái được phép. |
| XL-05 | Gửi lại trạng thái `SHIPPING` hoặc `COMPLETED` đã xử lý | Không trừ tồn hai lần; trạng thái cuối và dữ liệu reservation nhất quán. |

Phân biệt quyền hủy của khách với chuyển trạng thái bởi nhân viên. `OrderStatus` hiện cho nhân viên hủy ở `PENDING`, `CONFIRMED`, `PREPARING`; luồng khách hủy chỉ chấp nhận `PENDING`. Khi biến thể hủy bởi nhân viên là hợp lệ, kết quả mong đợi là hủy thành công và giải phóng hàng giữ, không áp kết quả “bị chặn” của biến thể chuyển trái quy trình.

### 4.6. Tồn kho — 3 mã ca

| Mã ca | Tình huống cần thực hiện | Output mong đợi/điểm cần quan sát |
|---|---|---|
| KHO-01 | Tra cứu một SKU tại hai cửa hàng có số liệu khác nhau | Đúng cửa hàng/SKU; tồn khả dụng bằng tồn thực tế trừ hàng giữ. |
| KHO-02 | Nhân viên có quyền điều chỉnh đặt tồn thực tế mới hợp lệ | Đúng số lượng mới; lưu số lượng trước/sau, lý do và nhân viên thực hiện; hàng giữ vẫn giữ nguyên. |
| KHO-03 | Điều chỉnh tồn thực tế xuống thấp hơn hàng đang giữ | Từ chối thao tác; tồn và lịch sử không lưu một điều chỉnh thành công sai. |

`InventoryAdjustmentRequest` nhận **số lượng thực tế mới**, không phải lượng cộng/trừ. Dữ liệu minh họa: tồn 10, giữ 2, đổi tồn thực tế thành 12 thì khả dụng phải là 10. Phần thay đổi do đặt, giao, hủy và trả hàng được đối chiếu trong DH/XL/TH để tránh tách rời các nghiệp vụ.

### 4.7. Phân quyền và phạm vi chi nhánh — 4 mã ca

| Mã ca | Tình huống cần thực hiện | Output mong đợi/điểm cần quan sát |
|---|---|---|
| PQ-01 | Khách chưa đăng nhập truy cập trang/API được bảo vệ | Không truy cập được dữ liệu/chức năng yêu cầu đăng nhập; ghi phản hồi HTTP hoặc trang chuyển hướng theo từng kênh. |
| PQ-02 | Khách A mở chức năng quản trị hoặc sửa dữ liệu của khách B | Bị từ chối; dữ liệu B không bị thay đổi. Chạy các biến thể URL/API, giỏ hàng, đơn hoặc hồ sơ được chọn. |
| PQ-03 | Nhân viên thiếu quyền gọi trực tiếp chức năng không được giao | API từ chối dù sửa URL hoặc gửi request thủ công; ví dụ nhân viên kho không quản lý nhân sự, nhân viên không có `CATALOG_MANAGE` không tạo sản phẩm. |
| PQ-04 | Nhân viên đơn hàng chi nhánh HCM xem/sửa đơn thuộc HN | Bị từ chối; đơn đúng chi nhánh vẫn thao tác được. Kiểm tra cả danh sách, chi tiết và cập nhật đơn. |

Mỗi biến thể có một request hợp lệ làm đối chứng để phân biệt bị chặn quyền với gửi dữ liệu sai. Ẩn menu là một quan sát giao diện, cần kiểm tra API/URL trực tiếp để kết luận quyền được kiểm soát. Không suy rộng việc chặn đơn khác chi nhánh thành đã kiểm chứng mọi API kho/nhập/điều chuyển đều có cùng cơ chế; các API đó cần ca riêng nếu mở rộng phạm vi.

### 4.8. Yêu cầu trả hàng — 4 mã ca

| Mã ca | Tình huống cần thực hiện | Output mong đợi/điểm cần quan sát |
|---|---|---|
| TH-01 | Chủ đơn `COMPLETED` gửi trả với số lượng hợp lệ | Tạo yêu cầu `REQUESTED`, đúng mặt hàng và số lượng; tồn chưa tăng khi chỉ tạo yêu cầu. |
| TH-02 | Gửi trả không hợp lệ | Chạy riêng các biến thể đơn chưa hoàn thành, đơn của người khác, số lượng vượt phần còn được trả; đều bị chặn và không tăng tồn. |
| TH-03 | Nhân viên duyệt rồi hoàn tất nhận trả | Đi theo trạng thái hợp lệ; khi `COMPLETED`, tồn thực tế tăng đúng lượng tại cửa hàng xử lý của đơn. Gửi lại hoàn tất không tăng lần hai. |
| TH-04 | Nhân viên từ chối yêu cầu | Trạng thái `REJECTED`; tồn không tăng. |

Kiểm tra cập nhật dữ liệu trả hàng và tồn kho. Khi ghi số tiền trả theo response, không coi đó là minh chứng đã chuyển tiền hoàn cho khách qua một cổng thanh toán.

## 5. Môi trường và bộ dữ liệu cần chuẩn bị

### 5.1. Môi trường

| Thành phần | Cách chuẩn bị và ghi vào báo cáo |
|---|---|
| Backend | JDK 21, Maven Wrapper; ghi commit và các thay đổi chưa commit của bản chạy. |
| Ứng dụng chạy tay | Profile `local`, PostgreSQL 16 theo Compose; URL mặc định `http://localhost:8080`. Kiểm tra khả năng khởi động trước khi ghi là môi trường đã sử dụng. |
| Giao diện | Trình duyệt của nhóm; ghi phiên bản thật khi chạy, kiểm tra request/response bằng DevTools. |
| API | Postman hoặc cURL cho ca dữ liệu sai, quyền truy cập và các thao tác chưa có form. |
| Email | Hộp thư thử có thể nhận SMTP để chứng minh nhận OTP. Nếu chỉ dùng chế độ phát triển thì ghi đúng phạm vi đã kiểm tra. |
| Test tự động | JUnit, Mockito, Spring Boot Test/MockMvc; các test Spring hiện có cấu hình H2 trong `src/test/resources/application.properties`. |

Các lệnh dự kiến dùng trong giai đoạn thực thi:

```bash
docker compose up -d postgres
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Chạy test tự động ở terminal riêng:

```bash
./mvnw test
```

Khi ghi kết quả, phân biệt unit test với repository được mock, kiểm thử Spring/MockMvc và kiểm thử trình duyệt trên PostgreSQL. Có test source hoặc có báo cáo Surefire cũ chưa đủ kết luận bản đang báo cáo đã đạt; thu lại kết quả từ lần chạy chính thức.

### 5.2. Dữ liệu thử

| Dữ liệu | Chuẩn bị |
|---|---|
| Hai khách hàng A/B | Tài khoản đang hoạt động, địa chỉ và giỏ riêng; thêm tài khoản ngừng hoạt động. |
| Nhân viên đại diện | Quản trị viên; nhân viên quản lý sản phẩm; nhân viên đơn HCM/HN; nhân viên kho HCM. Có thể đối chiếu các tài khoản seed `order_hcm`, `order_hn`, `warehouse_hcm`, nhưng xác nhận quyền và chi nhánh thực tế trong CSDL. |
| Email/OTP | Một email chưa đăng ký và một email đã có tài khoản; các bản ghi OTP hợp lệ, hết hạn và đã dùng được chuẩn bị theo ca. |
| Sản phẩm | Ít nhất hai SKU đang bán, một sản phẩm ngừng bán, dữ liệu đủ danh mục/thương hiệu/khoảng giá để kiểm tra lọc. |
| Cửa hàng | Hai cửa hàng đang hoạt động; có bộ dữ liệu một nơi đủ toàn bộ đơn và bộ dữ liệu hàng phân tán không nơi nào đủ toàn bộ đơn. |
| Đơn hàng | Đơn riêng cho tạo–giao–hoàn tất, đơn riêng cho hủy; đơn hoàn tất dùng cho trả hàng. Không lấy lại đơn đã hủy để kiểm thử giao hàng. |
| Số liệu tiền/tồn | Bộ dữ liệu minh họa bên dưới; đặt lại dữ liệu trước từng kịch bản độc lập. Không mặc định giá seed trùng giá minh họa. |

**Bộ tiền minh họa:** 2 đơn vị SKU × 200.000 đồng = 400.000 đồng tiền hàng.

| Trường hợp | Giảm tiền hàng | Phí vận chuyển | Tổng thanh toán mong đợi |
|---|---:|---:|---:|
| Không dùng mã | 0 | 30.000 | 430.000 |
| `LUNEA10` | 40.000 | 30.000 | 390.000 |
| `GIAM50K` | 50.000 | 30.000 | 380.000 |
| `FREESHIP` | 0 | 0 | 400.000 |

**Bộ tồn minh họa:** chi nhánh A có tồn thực tế 10, đang giữ 2; khách đặt 3 đơn vị. Giả định không có giao dịch khác trong lúc quan sát.

| Thời điểm | Tồn thực tế | Hàng giữ | Khả dụng |
|---|---:|---:|---:|
| Trước đặt hàng | 10 | 2 | 8 |
| Sau đặt thành công | 10 | 5 | 5 |
| Nếu hủy đơn vừa đặt, trước xuất hàng | 10 | 2 | 8 |
| Nếu giao đơn 3 đơn vị, từ trạng thái sau đặt | 7 | 2 | 5 |
| Nếu sau hoàn tất đơn đó nhận trả 1 đơn vị | 8 | 2 | 6 |

Nhánh hủy và nhánh giao là hai kịch bản độc lập, cùng bắt đầu từ bộ dữ liệu ban đầu. Đối chiếu thêm `order_reservations`: đơn mới bắt đầu `HELD`, khi giao thành `COMMITTED`, khi hủy trước giao thành `RELEASED`.

## 6. Minh chứng cần thu để viết báo cáo

| Nhóm | Ảnh/bằng chứng đại diện |
|---|---|
| Tài khoản | Đăng nhập thành công; một lỗi xác thực; nhận OTP hoặc phản hồi xác thực tương ứng. |
| Sản phẩm | Danh sách đã lọc/chi tiết SKU; response tạo/sửa hoặc trạng thái ngừng bán của bản thực thi. |
| Giỏ | SKU, số lượng và thành tiền sau thao tác; một thông báo vượt tồn. |
| Đặt hàng | Checkout hiển thị đủ tiền hàng, giảm giá, phí giao và tổng; mã đơn tạo thành công; bảng tồn/reservation trước–sau. |
| Xử lý đơn | Mã đơn và trạng thái trước–sau; dữ liệu tồn khi giao hoặc hủy. |
| Kho | Dữ liệu một SKU tại cửa hàng; số lượng và bản ghi điều chỉnh trước–sau. |
| Quyền | Vai trò/chi nhánh thử, request tới dữ liệu không được phép và phản hồi từ chối. |
| Trả hàng | Yêu cầu, trạng thái xử lý và số lượng nhập lại kho. |
| Tự động | Lệnh Maven, thời điểm chạy, tổng tests/failures/errors/skipped và trạng thái build. |

Chọn 10–12 ảnh trong các nhóm trên; bảng dữ liệu và request/response có thể gộp nhiều quan sát của cùng kịch bản. Không dùng ảnh prototype thay ảnh chạy thật. Trong ảnh minh chứng, dùng tài khoản thử và không hiển thị mật khẩu/token/cookie xác thực.

## 7. Đối chiếu với kiểm thử tự động hiện có

Tại thời điểm khảo sát, thư mục `src/test/java` có **15 tệp Java kiểm thử với 134 phương thức gắn `@Test`**. Đây là thống kê mã nguồn, không phải tổng số test đạt trong lượt này.

| Nhóm báo cáo | Tệp test liên quan | Việc vẫn cần thực hiện để viết kết quả |
|---|---|---|
| Tài khoản/OTP | `AccountServiceTest`, `OtpServiceTest` | Chạy lại test; thử form, nhận email thật và trạng thái OTP qua các request/CSDL. |
| Sản phẩm | `BrandTests`, `Phase1RestEndpointsTest`, `SecurityAuthorizationTest`, `Priority4SynchronizationTest` | Chỉ có bao phủ một phần; thu kết quả tìm/lọc, SKU, tạo/sửa và ngừng bán. |
| Giỏ | `Priority4SynchronizationTest` có ca sản phẩm ngừng bán và quyền sở hữu giỏ | Chạy thêm các ca thêm/gộp/cập nhật/xóa và tính tiền trên giao diện/API. |
| Đặt hàng/xử lý đơn | `OrderServiceTest`, `OrderEndpointsTest` | Đối chiếu tiền, reservation và tồn kho trên PostgreSQL; chạy đủ chuỗi giao/hủy và request lặp. |
| Tồn kho | `InventoryServiceTest` | Đối chiếu giao diện/API, quyền điều chỉnh, lịch sử và số liệu trước–sau. |
| Phân quyền | `SecurityAuthorizationTest`, `RoleSidebarAndVerificationTest`, một phần `OrderServiceTest` | Thử tài khoản/chi nhánh thật và gửi URL/API trực tiếp. |
| Trả hàng | `ReturnServiceTest` | Chạy luồng xuyên suốt và kiểm tra tăng tồn một lần trên CSDL thực tế. |

Kết quả tự động trình bày thành bảng riêng trong 6.2.9. Không cộng 134 phương thức trong source với 50 mã ca kế hoạch để báo một tổng “đã kiểm thử”. Các ca kiểm thử đồng thời nhiều người mua cần một kịch bản và minh chứng riêng nếu mở rộng; việc thấy khóa/transaction trong code hoặc mock test chưa đủ kết luận tránh bán vượt tồn khi có cạnh tranh.

## 8. Trình tự hoàn thiện báo cáo

| Bước | Công việc | Đầu ra cần có | Ước lượng |
|---|---|---|---|
| 1 | Chốt tám nhóm, đối chiếu mã yêu cầu/UC và thống nhất Output mong đợi | Danh sách ca/biến thể và phạm vi báo cáo | 0,5 buổi |
| 2 | Khởi động ứng dụng, chuẩn bị tài khoản/chi nhánh/SKU/giá/tồn/OTP | Môi trường chạy được, dữ liệu thử có thể đặt lại | 0,5 buổi |
| 3 | Chạy bộ test tự động; ghi lỗi hiện có | Báo cáo Maven/Surefire của bản mã nguồn đang thử | Một phần buổi kiểm thử |
| 4 | Thực hiện ca theo thứ tự tài khoản → sản phẩm → giỏ → đặt → xử lý → trả; kiểm tra kho và quyền xen kẽ | Biên bản từng lượt, ảnh, request/response và dữ liệu trước–sau | 1–2 buổi, tùy lỗi phát sinh |
| 5 | Viết 6.2.1–6.2.8 theo khuôn thống nhất, chọn minh chứng đại diện | Bản thảo phần kiểm thử | 1 buổi |
| 6 | Tổng hợp 6.2.9, rà soát số liệu, hình, trạng thái và phạm vi kết luận | Bản Word ghép vào Chương 6 và bảng kết quả/phụ lục | 0,5 buổi |

Ước lượng chưa bao gồm thời gian sửa lỗi phần mềm. Khi sửa lỗi, ghi mã lỗi, bản sửa và kết quả kiểm thử lại; không thay kết quả ban đầu mà mất lịch sử.

Mục 6.2.9 tổng hợp riêng kiểm thử thủ công/API và tự động; ghi số ca đạt, không đạt, chưa thực hiện, bị chặn cùng lỗi còn lại. Với các lượt đã chạy xong, tỷ lệ đạt = số đạt / (số đạt + số không đạt) × 100%; công bố riêng số chưa thực hiện/bị chặn để tỷ lệ này không che mất phần chưa kiểm tra. Chưa có lượt nào hoàn tất thì ghi “Chưa có kết quả”, không tính tỷ lệ.

Phần báo cáo sẵn sàng khi mỗi ca/biến thể trong phạm vi chốt có trạng thái và minh chứng phù hợp, các lỗi còn lại được ghi rõ và số liệu tổng hợp khớp biên bản. Chỉ kết luận đạt những chức năng đã kiểm tra thành công; ghi phạm vi chưa thực hiện trong phần hạn chế.

## 9. Nguồn đối chiếu khi biên soạn

- [Báo cáo hiện tại Nhóm 01](Nhom01_BaoCaoCuoiKy_review.pdf), [mẫu Nhóm 17](Nhom17_BaoCaoCuoiKy.docx), [biểu mẫu yêu cầu](BIEU_MAU_DANH_SACH_YEU_CAU_LUNEA.md).
- [Kế hoạch toàn Chương 6](KE_HOACH_CHUONG_6_CAI_DAT_VA_KIEM_THU.md) và [phần 6.1 đã soạn](CHUONG_6_1_CAI_DAT_CHUONG_TRINH.md).
- `AccountServiceImpl`, `OtpServiceImpl`, `ProductServiceImpl`, `CartServiceImpl`, `OrderServiceImpl`, `InventoryServiceImpl`, `ReturnServiceImpl`; các REST controller và DTO tương ứng.
- `OrderStatus`, `ReturnStatus`, `SecurityConfig`, `StoreScope`, `DataSeeder`, `src/main/resources/templates`, `src/test/java`, `src/test/resources/application.properties` và `pom.xml`.
