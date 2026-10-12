# LUNEA – Website Bán Mỹ Phẩm Theo Mô Hình Chuỗi Cửa Hàng

Đồ án môn học **Công nghệ Phần mềm** – Tài liệu phân tích & thiết kế hệ thống cho website thương mại điện tử ngành mỹ phẩm, vận hành theo mô hình **chuỗi cửa hàng** (quản lý sản phẩm tập trung, tồn kho theo từng chi nhánh).

## Chạy ứng dụng trên local

Yêu cầu: **JDK 21**, Docker Compose. Maven đã có sẵn qua `mvnw` / `mvnw.cmd`.

```bash
docker compose up -d --wait postgres
export SPRING_DATASOURCE_PASSWORD=12345
export APP_JWT_SECRET="$(openssl rand -hex 32)"
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Mở **http://localhost:8080**. Trên Windows dùng `mvnw.cmd` thay `./mvnw`.
Trong IntelliJ, chọn JDK 21 và đặt **Active profiles = local** (hoặc biến môi trường
`SPRING_PROFILES_ACTIVE=local`) cho cấu hình chạy `CosmeticStoreChainManagementApplication`.

Profile `local` dùng PostgreSQL ở `localhost:5433`, database/user `lunea`.
Theo cấu hình hiện tại, bạn cần đặt `SPRING_DATASOURCE_PASSWORD` (mặc định của Docker Compose
là `12345`) và `APP_JWT_SECRET` (ít nhất 32 byte UTF-8) trong terminal hoặc Run Configuration
trước khi chạy. Các lệnh `export` ở trên dành cho macOS/Linux; trên Windows đặt hai biến
này trong terminal hoặc Run Configuration của IDE.

Cookie local dùng HTTP. Cấu hình chung yêu cầu khóa JWT qua biến môi trường; không có khóa
phát triển mặc định. Spring Boot không tự đọc file `.env`.

Các trường văn bản dài trong entity dùng `@Column(columnDefinition = "text")`, không còn
`@Lob` hoặc dialect riêng cho local. Local và Railway đều dùng ánh xạ `TEXT` với dialect
PostgreSQL chuẩn. Nếu database cũ đang lưu các cột này bằng OID, xem
[hướng dẫn chuyển OID sang TEXT](docs/POSTGRES_TEXT_MIGRATION.md) **trước khi chạy bản mới**.

Với database mới, ứng dụng tạo bảng và nạp dữ liệu mẫu:

| Tài khoản | Mật khẩu | Vai trò |
|---|---|---|
| `admin@lunea.test` | `123456` | Quản trị viên |
| `khach01@lunea.test` | `123456` | Khách hàng |

Nếu đã có dữ liệu, ứng dụng giữ dữ liệu hiện có. `docker compose stop` dừng database;
`docker compose down` giữ volume; **không dùng `down -v` nếu cần giữ dữ liệu**.

Các biến `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`,
`APP_JWT_SECRET`, `APP_SECURITY_COOKIE_SECURE` và `PORT` có thể ghi đè cấu hình.
Nếu terminal/IDE đang giữ biến database của Railway, bỏ các biến đó trước khi chạy local.

Kiểm tra và đóng gói (test dùng H2, không cần Docker hay database Railway):

```bash
./mvnw clean verify
java -jar target/cosmetic-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

Nếu cổng 8080 đang được dùng, có thể chạy `PORT=8081 ./mvnw spring-boot:run -Dspring-boot.run.profiles=local`
trên macOS/Linux. Nếu gặp lỗi mật khẩu PostgreSQL với volume cũ, dùng mật khẩu đã tạo volume đó;
đổi `POSTGRES_PASSWORD` trong Compose không đổi mật khẩu của database đã tồn tại.

## Giữ tương thích Railway

`application.properties` chứa cấu hình chung; `application-local.properties` và
`application-prod.properties` chứa cấu hình môi trường. Trên Railway đặt
**`SPRING_PROFILES_ACTIVE=prod`**, theo [cơ chế profile của Spring Boot](https://docs.spring.io/spring-boot/reference/features/profiles.html).
Không bật profile `local` trên Railway.

| Biến | Giá trị |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `SPRING_DATASOURCE_URL` | JDBC URL PostgreSQL hiện tại, bắt đầu bằng `jdbc:postgresql://` |
| `SPRING_DATASOURCE_USERNAME` | User database Railway |
| `SPRING_DATASOURCE_PASSWORD` | Password database Railway |
| `APP_JWT_SECRET` | Khóa JWT riêng của deployment, ít nhất 32 byte UTF-8; giữ khóa hiện tại nếu hợp lệ |
| `PORT` | Cổng Railway cung cấp |
| `APP_SEED_ENABLED` | Mặc định `true`; đặt `false` nếu không muốn nạp dữ liệu mẫu |

Profile `prod` giữ `ddl-auto=validate` và cookie `Secure=true` theo cấu hình hiện tại.
Database phải có schema phù hợp trước khi ứng dụng khởi động. Riêng thay đổi bỏ `@Lob`,
kiểm tra bảy cột và chuyển những cột OID bằng [script migration](docs/sql/convert-lob-oid-to-text.sql)
trước khi deploy; cột đã là TEXT không cần chuyển. Không dùng `ddl-auto=update` để tự ép
OID thành TEXT vì có thể chỉ giữ mã đối tượng thay vì nội dung. Xem
[hướng dẫn migration](docs/POSTGRES_TEXT_MIGRATION.md).

Giữ phiên bản dependency và cách đóng gói JAR hiện tại. Nếu cấu hình thủ công, build bằng
`./mvnw clean package -DskipTests`, start bằng `java -jar target/cosmetic-0.0.1-SNAPSHOT.jar`.
Xem [hướng dẫn Spring Boot của Railway](https://docs.railway.com/guides/spring-boot).

---

## 1. Thông tin đồ án

| | |
|---|---|
| **Trường** | Đại học Công nghệ Kỹ thuật TP. Hồ Chí Minh |
| **Khoa** | Công nghệ Thông tin |
| **Môn học** | Công nghệ Phần mềm |
| **Nhóm thực hiện** | Nhóm 01 |
| **Giảng viên hướng dẫn** | ThS. Nguyễn Trần Thi Văn |
| **Thời gian** | Tháng 9/2026 |

**Thành viên nhóm**

| Họ và tên | MSSV |
|---|---|
| Lý Đông Thịnh | 24110337 |
| Hà Nguyễn Nhật Nguyên | 24110288 |

**Tài liệu gốc:** `Nhom01_ThietKeGiaoDien.docx`

---

## 2. Giới thiệu

LUNEA là nền tảng thương mại điện tử kết nối nhiều cửa hàng mỹ phẩm vật lý. Sản phẩm và chính sách bán hàng được **quản lý tập trung**, trong khi **tồn kho, nhân viên xử lý và đơn hàng được theo dõi theo từng chi nhánh**. Hệ thống hỗ trợ song song bán lẻ trực tuyến (đa kênh) và nghiệp vụ bán sỉ cho đối tác.

Repo này tổng hợp toàn bộ tài liệu phân tích – thiết kế của đồ án, gồm 5 chương chính:

1. **Khảo sát hiện trạng** – tổng quan thị trường, tổ chức chuỗi LUNEA, nghiệp vụ hiện tại và khảo sát đối thủ (Sephora, Charlotte Tilbury, Fenty Beauty).
2. **Lập danh sách yêu cầu** – yêu cầu chức năng nghiệp vụ, chức năng hệ thống và yêu cầu phi chức năng.
3. **Xác định Actor và Use Case** – nhận diện tác nhân, sơ đồ use-case và đặc tả chi tiết từng chức năng.
4. **Thiết kế dữ liệu** – lược đồ logic và chi tiết 37 bảng dữ liệu.
5. **Thiết kế giao diện** – danh sách màn hình, sơ đồ luân chuyển và mô tả chi tiết từng màn hình.

> **Trạng thái:** Repo chứa tài liệu **phân tích & thiết kế** cùng mã nguồn Spring Boot, Thymeleaf, Spring Security/JWT và PostgreSQL. Hướng dẫn chạy local và cấu hình Railway nằm ở đầu README.

---

## 3. Tác nhân (Actors) trong hệ thống

| Mã số | Tác nhân | Vai trò |
|---|---|---|
| KH | Khách hàng | Tra cứu mỹ phẩm, quản lý hồ sơ, giỏ hàng, đặt hàng, theo dõi đơn, yêu cầu trả hàng, đánh giá sản phẩm |
| QLSP | Nhân viên quản lý sản phẩm | Quản lý danh mục, SKU/biến thể, thuộc tính mỹ phẩm, hình ảnh, kiểm duyệt đánh giá |
| QLCH | Quản trị viên / Quản lý chuỗi | Quản lý thông tin cửa hàng/chi nhánh: trạng thái, địa chỉ, giờ hoạt động |
| QLNH | Nhân viên nhập hàng | Quản lý nhà cung cấp, phiếu nhập, xác nhận nhập hàng |
| QLTK | Nhân viên kho | Quản lý tồn kho theo SKU – cửa hàng: kiểm kê, điều chỉnh, điều chuyển, giữ hàng |
| QLKH | Nhân viên chăm sóc khách hàng | Tra cứu hồ sơ/lịch sử mua, quản lý tài khoản, xử lý yêu cầu trả hàng |
| QLDH | Nhân viên xử lý đơn hàng | Xác nhận đơn, phân công chi nhánh, cập nhật vòng đời đơn, xử lý trả hàng |
| QLKM | Nhân viên Marketing | Quản lý khuyến mãi, voucher, theo dõi hiệu quả chương trình |
| QLNV | Quản trị viên | Quản lý nhân viên, phân chi nhánh, vai trò, quyền, nhật ký thao tác |
| BCTK | Quản lý / Quản trị viên | Khai thác dashboard và các báo cáo (doanh thu, đơn hàng, tồn kho, khuyến mãi...) |

Ngoài ra, hệ thống còn tương tác với các **actor bên ngoài**: cổng thanh toán, đơn vị vận chuyển, Google Maps API, Cloudinary (lưu trữ ảnh) và dịch vụ Email/OTP.

---

## 4. Các nhóm chức năng chính

- **Mua sắm & tài khoản khách hàng:** tìm kiếm/lọc sản phẩm, xem chi tiết & tồn kho theo cửa hàng, Beauty Profile & gợi ý sản phẩm, yêu thích, giỏ hàng, đặt hàng, theo dõi & hủy đơn, yêu cầu trả hàng, đánh giá sản phẩm.
- **Vận hành chuỗi:** quản lý sản phẩm/SKU, quản lý cửa hàng, nhập hàng từ nhà cung cấp, quản lý tồn kho & điều chuyển giữa các chi nhánh.
- **Xử lý đơn hàng:** vòng đời đơn *Chờ xác nhận → Đã xác nhận → Đang chuẩn bị → Đang giao/Chờ nhận tại cửa hàng → Hoàn thành* (kèm Đã hủy, Yêu cầu hoàn/trả).
- **Marketing:** khuyến mãi theo sản phẩm/danh mục/thương hiệu, voucher (%, số tiền cố định), ưu đãi vận chuyển, chương trình thành viên.
- **Quản trị hệ thống:** quản lý nhân viên, phân quyền theo vai trò **và** theo chi nhánh, nhật ký thao tác.
- **Báo cáo & thống kê:** doanh thu, đơn hàng, sản phẩm bán chạy, tồn kho/tồn thấp, nhập hàng, hiệu quả khuyến mãi, khách hàng.

---

## 5. Thiết kế dữ liệu

Lược đồ logic gồm **37 bảng**, chia thành 2 nhóm chính (thương mại điện tử – khách hàng; vận hành chuỗi – quản trị). Kiểu dữ liệu mang tính định hướng, có thể ánh xạ sang **MySQL / PostgreSQL / SQL Server** khi triển khai.

| Nhóm | Bảng dữ liệu |
|---|---|
| Tài khoản & người dùng | TaiKhoan, KhachHang, DiaChiKhachHang, BeautyProfile, NhanVien, VaiTro, Quyen, NhanVienVaiTro, VaiTroQuyen, NhanVienCuaHang, NhatKyThaoTac |
| Sản phẩm | ThuongHieu, DanhMuc, SanPham, SKU, ThuocTinh, GiaTriThuocTinhSKU, HinhAnhSanPham |
| Cửa hàng & tồn kho | CuaHang, TonKho, DieuChinhTon, DieuChuyenKho, ChiTietDieuChuyen |
| Giỏ hàng & yêu thích | GioHang, ChiTietGioHang, YeuThich, ChiTietYeuThich |
| Khuyến mãi | Voucher |
| Đơn hàng | DonHang, ChiTietDonHang, GiuTonDonHang, YeuCauTraHang, ChiTietTraHang, DanhGia |
| Nhập hàng | NhaCungCap, PhieuNhap, ChiTietPhieuNhap |

**Quy tắc quan trọng:** một sản phẩm có nhiều SKU · tồn kho quản lý theo cặp SKU–cửa hàng · hàng chỉ được giữ (giữ tồn) sau khi đơn tạo thành công · dữ liệu đã phát sinh giao dịch không xóa vật lý mà chuyển trạng thái · chi tiết đơn hàng lưu snapshot giá tại thời điểm đặt.

---

## 6. Thiết kế giao diện – Danh sách màn hình

| STT | Màn hình | Mục đích |
|---|---|---|
| 1 | Trang chủ | Giới thiệu LUNEA, danh mục và sản phẩm nổi bật |
| 2 | Danh sách sản phẩm | Hiển thị và lọc danh sách sản phẩm |
| 3 | Chi tiết sản phẩm | Hiển thị thông tin chi tiết một sản phẩm |
| 4 | Đăng nhập | Xác thực khách hàng |
| 5 | Đăng ký | Tạo tài khoản mới |
| 6 | Giỏ hàng | Hiển thị & quản lý sản phẩm đã chọn mua |
| 7 | Giao hàng & thanh toán | Nhập thông tin nhận hàng, chọn phương thức thanh toán |
| 8 | Đặt hàng thành công | Thông báo kết quả và thông tin đơn hàng |

**Luồng chính:** Trang chủ → Danh sách sản phẩm → Chi tiết sản phẩm → Giỏ hàng → Giao hàng & thanh toán → Đặt hàng thành công.

---

## 7. Yêu cầu phi chức năng nổi bật

- **Tương thích:** Responsive (desktop/tablet/mobile), hoạt động tốt trên Chrome, Edge, Firefox, Safari.
- **Hiệu quả:** thời gian phản hồi tìm kiếm/tải trang mục tiêu ≤ ~3 giây.
- **Bảo mật:** mật khẩu không lưu dạng văn bản rõ, kết nối HTTPS khi triển khai thực tế, kiểm soát quyền truy cập theo vai trò & chi nhánh.
- **Đúng đắn & toàn vẹn:** không để tồn khả dụng âm, ràng buộc dữ liệu nhất quán giữa khách hàng – đơn hàng – SKU – cửa hàng – tồn kho.
- **Tin cậy:** hỗ trợ sao lưu & phục hồi dữ liệu, không mất dữ liệu lịch sử.
- **Khả năng tiến hóa:** dễ dàng thêm chi nhánh, SKU mới, phương thức thanh toán, loại khuyến mãi mà không đổi kiến trúc lõi.

---

## 8. Tích hợp hệ thống ngoài

| Dịch vụ | Vai trò |
|---|---|
| Cổng thanh toán | Xử lý thanh toán (phiên bản hiện tại: COD) |
| Đơn vị vận chuyển | Giao hàng tận nơi |
| Google Maps API | Hiển thị vị trí cửa hàng |
| Cloudinary | Lưu trữ & xử lý hình ảnh sản phẩm |
| Email / OTP | Xác thực tài khoản, thông báo |

---

## 9. Định hướng tiếp theo

- [ ] Lựa chọn & thống nhất công nghệ triển khai (frontend, backend, hệ quản trị CSDL)
- [ ] Cài đặt cơ sở dữ liệu vật lý từ lược đồ logic (mục 5)
- [ ] Xây dựng các module theo đặc tả use case (mục 4)
- [ ] Hiện thực hóa 8 màn hình giao diện theo thiết kế (mục 6)
- [ ] Tích hợp thanh toán trực tuyến ngoài COD

---

## 10. License

Tài liệu phục vụ mục đích học thuật trong khuôn khổ môn học Công nghệ Phần mềm.
