# LUNEA

**Cosmetic Store Chain Management — Website bán mỹ phẩm và quản lý chuỗi cửa hàng**

LUNEA kết hợp cửa hàng trực tuyến với hệ thống quản trị dành cho nhân viên. Thông tin sản phẩm được quản lý tập trung; biến thể sản phẩm, tồn kho và đơn hàng được tổ chức theo từng chi nhánh. Dự án sử dụng Spring Boot để phục vụ cả giao diện Thymeleaf và REST API.

**Java 21 · Spring Boot 4.1.1 · PostgreSQL 16 · Thymeleaf · Spring Security**

[Chạy local](#quick-start) · [Tài khoản mẫu](#demo-accounts) · [Cấu hình](#configuration) · [Kiểm thử](#testing) · [API](#api) · [Xử lý lỗi](#troubleshooting)

## Chức năng chính

| Khu vực | Chức năng |
| --- | --- |
| Mua sắm | Xem, tìm kiếm, lọc và sắp xếp sản phẩm; xem biến thể SKU, thương hiệu và tồn kho tại cửa hàng. |
| Tài khoản khách hàng | Đăng ký bằng OTP, đăng nhập, khôi phục mật khẩu, cập nhật hồ sơ và địa chỉ nhận hàng. |
| Giỏ hàng và yêu thích | Thêm, cập nhật số lượng, xóa sản phẩm; quản lý danh sách yêu thích. |
| Đặt hàng | Xem trước giá trị đơn, áp dụng voucher, chọn thông tin giao nhận và đặt hàng thanh toán khi nhận hàng (COD). |
| Sau mua hàng | Xem lịch sử, chi tiết và trạng thái đơn; hủy đơn theo điều kiện; gửi đánh giá và yêu cầu trả hàng. |
| Quản trị danh mục | Quản lý sản phẩm, SKU, danh mục, thương hiệu; tải ảnh lên Cloudinary khi đã cấu hình dịch vụ. |
| Vận hành chuỗi | Quản lý chi nhánh, nhân viên, nhà cung cấp, phiếu nhập, tồn kho và điều chuyển hàng. |
| Xử lý nghiệp vụ | Cập nhật trạng thái đơn, xử lý trả hàng, kiểm duyệt đánh giá và quản lý voucher. |
| Phân quyền | Kiểm soát truy cập theo vai trò, quyền chức năng và phạm vi chi nhánh trong nghiệp vụ đơn hàng. |

Tồn kho được theo dõi theo SKU tại từng cửa hàng. Khi đặt hàng, hệ thống lưu thông tin sản phẩm và giá tại thời điểm đặt, đồng thời tạo bản ghi giữ tồn cho đơn hàng.

Phiên bản hiện tại hỗ trợ COD. Dashboard cung cấp số liệu tổng quan; thanh toán trực tuyến, tích hợp vận chuyển và bộ báo cáo xuất file là những phần có thể tiếp tục phát triển.

## Công nghệ

| Thành phần | Công nghệ |
| --- | --- |
| Backend | Java 21, Spring Boot 4.1.1, Spring MVC |
| Giao diện | Thymeleaf, HTML, CSS, JavaScript, Fetch API |
| Truy cập dữ liệu | Spring Data JPA, Hibernate |
| Cơ sở dữ liệu local | PostgreSQL 16, Docker Compose |
| Xác thực và phân quyền | Spring Security, JWT HS256, BCrypt |
| Kiểm tra và ánh xạ dữ liệu | Jakarta Validation, MapStruct 1.6.3, Lombok 1.18.48 |
| Dịch vụ tùy chọn | Spring Mail/SMTP, Cloudinary |
| Kiểm thử | JUnit Jupiter, Mockito, MockMvc, Spring Boot Test, H2 |
| Công cụ build | Maven Wrapper, Maven 3.9.16 |

<a id="quick-start"></a>

## Chạy local

### 1. Chuẩn bị môi trường

- **JDK 21**; bảo đảm `JAVA_HOME` trỏ đến đúng JDK.
- **Docker** đang hoạt động và có **Docker Compose v2**.
- **Git** nếu lấy mã nguồn bằng lệnh clone.
- Kết nối Internet cho lần đầu tải Maven, thư viện và image PostgreSQL.

Kiểm tra môi trường:

```bash
java -version
docker --version
docker compose version
```

Trên macOS có nhiều JDK, có thể chọn Java 21 cho terminal hiện tại:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
```

### 2. Lấy mã nguồn

```bash
git clone https://github.com/nhatnguyen10a1thd-png/CNPM.git lunea
cd lunea
```

Nếu nhận mã nguồn dưới dạng ZIP, giải nén và mở terminal tại thư mục có `pom.xml`, `mvnw` và `docker-compose.yml`.

### 3. Khởi động PostgreSQL

```bash
docker compose up -d postgres
docker compose exec postgres pg_isready -U lunea -d lunea
```

Chờ kết quả `accepting connections` trước khi chạy ứng dụng. Có thể xem trạng thái và log bằng:

```bash
docker compose ps
docker compose logs --tail=50 postgres
```

Thông tin kết nối theo [docker-compose.yml](docker-compose.yml):

| Thuộc tính | Giá trị local |
| --- | --- |
| Host | `localhost` |
| Port trên máy | `5433` |
| Database | `lunea` |
| Username | `lunea` |
| Password mẫu | `12345` |
| JDBC URL | `jdbc:postgresql://localhost:5433/lunea` |

Docker Compose trong repository khởi chạy PostgreSQL; ứng dụng Java được chạy riêng bằng Maven hoặc JAR. Dữ liệu PostgreSQL được lưu trong volume `lunea_pgdata` do Compose quản lý.

### 4. Thiết lập biến môi trường và chạy ứng dụng

**macOS / Linux — Bash hoặc Zsh**

```bash
export SPRING_DATASOURCE_PASSWORD='12345'
export APP_JWT_SECRET="$(openssl rand -base64 48)"

./mvnw -v
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Lệnh `openssl` sinh khóa JWT ngẫu nhiên cho môi trường local. Dòng kiểm tra Maven cần hiển thị Java 21.

**Windows — PowerShell**

```powershell
$env:SPRING_DATASOURCE_PASSWORD = '12345'
$luneaJwtBytes = New-Object byte[] 48
$luneaJwtRng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$luneaJwtRng.GetBytes($luneaJwtBytes)
$env:APP_JWT_SECRET = [Convert]::ToBase64String($luneaJwtBytes)
$luneaJwtRng.Dispose()

.\mvnw.cmd -v
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

Các biến trên có hiệu lực trong terminal hiện tại. Khi mở terminal mới, thiết lập lại chúng trước khi chạy. Nếu có biến `PORT` từ môi trường khác, ứng dụng sẽ dùng cổng đó thay cho `8080`.

### 5. Truy cập ứng dụng

Khi log thông báo ứng dụng đã khởi động, mở:

| Trang | Địa chỉ mặc định |
| --- | --- |
| Trang chủ | <http://localhost:8080/> |
| Sản phẩm | <http://localhost:8080/products> |
| Thương hiệu | <http://localhost:8080/brands> |
| Cửa hàng | <http://localhost:8080/stores> |
| Đăng nhập | <http://localhost:8080/login> |
| Đăng ký | <http://localhost:8080/register> |
| Quản trị | <http://localhost:8080/admin> |

Giao diện được phục vụ cùng ứng dụng Spring Boot. Hibernate tạo/cập nhật cấu trúc bảng theo Entity và `DataSeeder` tự nạp dữ liệu mẫu; không cần nhập một tệp SQL riêng để bắt đầu với database trống.

Để dừng ứng dụng, nhấn `Ctrl+C` tại terminal đang chạy. Dừng PostgreSQL bằng `docker compose stop postgres`; volume dữ liệu được giữ lại.

<a id="demo-accounts"></a>

## Tài khoản mẫu

Các tài khoản sau được tạo khi khởi tạo database trống, theo [DataSeeder.java](src/main/java/com/thinh/cosmetic/config/DataSeeder.java). **Mật khẩu mẫu: `123456`.**

| Vai trò | Thông tin đăng nhập | Phạm vi |
| --- | --- | --- |
| Quản trị viên | `admin@lunea.test` hoặc `admin` | Quản trị hệ thống |
| Nhân viên đơn hàng TP.HCM | `order.hcm@lunea.test` hoặc `order_hcm` | Đơn hàng thuộc chi nhánh được phân công |
| Nhân viên đơn hàng Hà Nội | `order.hn@lunea.test` hoặc `order_hn` | Đơn hàng thuộc chi nhánh được phân công |
| Nhân viên kho TP.HCM | `warehouse.hcm@lunea.test` hoặc `warehouse_hcm` | Nghiệp vụ kho được cấp quyền |
| Khách hàng | `khach01@lunea.test` | Mua sắm và quản lý tài khoản cá nhân |

Khi database đã có dữ liệu nền, các lần khởi động tiếp theo còn đồng bộ tài khoản nghiệp vụ như `product_manager`, nhân viên chi nhánh khác và khách hàng mẫu. Seeder có thể đặt lại mật khẩu, vai trò và phạm vi của các tài khoản demo.

Để thử luồng mua hàng, đăng nhập khách hàng, chọn SKU, thêm vào giỏ và đặt đơn COD. Dùng một hồ sơ trình duyệt khác hoặc cửa sổ riêng tư để đăng nhập nhân viên và xử lý đơn song song.

Voucher mẫu được tạo khi bảng voucher đang trống:

| Mã | Điều kiện và giá trị giảm theo dữ liệu seed |
| --- | --- |
| `LUNEA10` | Giảm 10%, tối đa 100.000 đồng; tiền hàng từ 300.000 đồng. |
| `GIAM50K` | Giảm 50.000 đồng; tiền hàng từ 400.000 đồng. |
| `FREESHIP` | Giảm phí vận chuyển tối đa 30.000 đồng. |
| `LUNEAVIP` | Giảm 20%, tối đa 300.000 đồng; tiền hàng từ 1.000.000 đồng. |

Khả năng áp dụng còn phụ thuộc thời gian hiệu lực, trạng thái và lượt sử dụng hiện tại của voucher trong database.

<a id="configuration"></a>

## Cấu hình

### Profile và biến môi trường

| Profile | Cấu hình | Đặc điểm |
| --- | --- | --- |
| `local` | [application-local.properties](src/main/resources/application-local.properties) | PostgreSQL `localhost:5433/lunea`, username mặc định `lunea`, cookie dùng được qua HTTP local. |
| `prod` | [application-prod.properties](src/main/resources/application-prod.properties) | Nhận kết nối database qua biến môi trường; bật thuộc tính `Secure` cho cookie, cần truy cập ứng dụng qua HTTPS. |
| Kiểm thử | [src/test/resources/application.properties](src/test/resources/application.properties) | H2 trong bộ nhớ, `ddl-auto=create-drop`. |

Các thiết lập chung nằm trong [application.properties](src/main/resources/application.properties).

| Biến môi trường | Sử dụng |
| --- | --- |
| `APP_JWT_SECRET` | Bắt buộc khi chạy ứng dụng; khóa ký HS256 dài tối thiểu 32 byte. Giữ nguyên khóa giữa các lần chạy nếu muốn JWT còn hạn tiếp tục được xác thực. |
| `SPRING_DATASOURCE_PASSWORD` | Bắt buộc; phải khớp mật khẩu PostgreSQL. Với Compose mẫu là `12345`. |
| `SPRING_DATASOURCE_USERNAME` | Mặc định `lunea` ở profile `local`; bắt buộc ở `prod`. |
| `SPRING_DATASOURCE_URL` | Bắt buộc ở `prod` hoặc khi chạy không chọn profile; dùng dạng `jdbc:postgresql://host:port/database`. Profile `local` đã khai báo URL riêng. |
| `PORT` | Cổng HTTP, mặc định `8080`. |

Spring Boot nhận biến môi trường từ tiến trình chạy; một tệp `.env` tự tạo không được ứng dụng tự động nạp. Các mật khẩu demo trong README chỉ dùng cho môi trường chạy thử.

**Lưu ý cấu hình hiện tại:** cả `local` và `prod` đều dùng `spring.jpa.hibernate.ddl-auto=update`; `DataSeeder` vẫn tự chạy khi khởi động, kể cả với `prod`. Profile `prod` chưa tách việc tạo/đồng bộ dữ liệu demo khỏi môi trường vận hành dữ liệu thật.

### Email và OTP

Có thể chạy thử đăng ký và khôi phục mật khẩu khi chưa cấu hình SMTP: `EmailServiceImpl` ghi OTP vào log ứng dụng. Khi gửi email thất bại, mã nguồn hiện cũng có nhánh ghi OTP vào log dự phòng.

Để gửi thư thật, cung cấp các thuộc tính sau qua cấu hình riêng của môi trường hoặc biến môi trường Spring Boot tương ứng:

| Thuộc tính | Nội dung |
| --- | --- |
| `spring.mail.host` | Máy chủ SMTP của nhà cung cấp email. |
| `spring.mail.port` | Cổng SMTP. |
| `spring.mail.username` | Tài khoản gửi thư. |
| `spring.mail.password` | Thông tin xác thực SMTP hoặc mật khẩu ứng dụng. |
| `spring.mail.properties.mail.smtp.auth` | Bật xác thực nếu nhà cung cấp yêu cầu. |
| `spring.mail.properties.mail.smtp.starttls.enable` | Bật STARTTLS nếu sử dụng máy chủ hỗ trợ/yêu cầu. |

Ví dụ các biến thông dụng: `SPRING_MAIL_HOST`, `SPRING_MAIL_PORT`, `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD`. Giữ thông tin xác thực ngoài mã nguồn và không đưa log chứa OTP vào tài liệu công khai.

### Tải ảnh lên Cloudinary

Tính năng upload ảnh sử dụng các thuộc tính `cloudinary.cloud-name`, `cloudinary.api-key`, `cloudinary.api-secret`, được đọc tại [CloudinaryConfig.java](src/main/java/com/thinh/cosmetic/config/CloudinaryConfig.java).

Ứng dụng vẫn khởi động khi chưa cung cấp các giá trị này; thao tác upload sẽ báo thiếu cấu hình. Endpoint upload là `POST /api/upload/image`, dùng `multipart/form-data`, trường `file`, dung lượng ảnh tối đa 5 MB theo kiểm tra ở Service. Giới hạn multipart của Spring Boot cũng được áp dụng; có thể điều chỉnh bằng `spring.servlet.multipart.max-file-size` và `spring.servlet.multipart.max-request-size` khi cần tải ảnh lớn hơn mức mặc định.

<a id="testing"></a>

## Kiểm thử

Chạy các lệnh tại thư mục gốc của repository, sử dụng JDK 21:

```bash
# Chạy toàn bộ bộ kiểm thử hiện có
./mvnw test

# Chỉ chạy các lớp kiểm thử nghiệp vụ được chọn
./mvnw "-Dtest=AccountServiceTest,OrderServiceTest,InventoryServiceTest,VoucherServiceTest" test

# Chỉ chạy lớp kiểm thử tích hợp nghiệp vụ
./mvnw -Dtest=AuditBusinessIntegrationTest test
```

Trên Windows PowerShell, thay `./mvnw` bằng `.\mvnw.cmd`.

- **Unit test:** kiểm tra quy tắc trong các Service với Mockito.
- **Controller và phân quyền:** kiểm tra endpoint, phản hồi HTTP và quyền truy cập bằng MockMvc.
- **Integration test:** kiểm tra phối hợp nghiệp vụ và dữ liệu bằng Spring Boot Test; cấu hình mặc định dùng H2.

Kết quả từng lần chạy nằm trong `target/surefire-reports/` dưới dạng TXT/XML. Kết quả với H2 cần được phân biệt với kiểm thử trên PostgreSQL và kiểm thử thao tác trình duyệt.

## Đóng gói và chạy JAR

Sau khi thiết lập biến môi trường và khởi động PostgreSQL như phần chạy local:

```bash
./mvnw clean package
java -jar target/cosmetic-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

Trên Windows:

```powershell
.\mvnw.cmd clean package
java -jar target/cosmetic-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

Maven chạy kiểm thử trước khi tạo gói. Nếu có test không đạt, xem báo cáo Surefire trước khi tiếp tục. Dừng tiến trình `spring-boot:run` trước khi chạy JAR để tránh dùng trùng cổng HTTP.

<a id="api"></a>

## API

Base URL local: `http://localhost:8080`.

| Nhóm | Đường dẫn chính |
| --- | --- |
| Xác thực, OTP | `/api/auth` |
| Sản phẩm, SKU, danh mục, thương hiệu | `/api/products`, `/api/categories`, `/api/brands` |
| Giỏ hàng, yêu thích | `/api/cart`, `/api/wishlist` |
| Đặt hàng và đơn hàng khách hàng | `/api/orders` |
| Tra cứu đơn hàng phía nhân viên | `/api/admin/orders` |
| Chi nhánh, tồn kho, điều chuyển | `/api/stores`, `/api/inventory`, `/api/stock-transfers` |
| Nhà cung cấp, phiếu nhập | `/api/suppliers`, `/api/purchase-orders` |
| Voucher, đánh giá, trả hàng | `/api/vouchers`, `/api/reviews`, `/api/returns` |
| Khách hàng, nhân viên, vai trò | `/api/customers`, `/api/employees`, `/api/roles` |
| Upload ảnh | `/api/upload/image` |

Ví dụ đăng nhập bằng Postman hoặc công cụ HTTP:

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json
X-Requested-With: fetch

{
  "identifier": "admin@lunea.test",
  "password": "123456"
}
```

Đăng nhập thành công trả thông tin tài khoản và đặt cookie `LUNEA_AT` qua `Set-Cookie`. Giữ cookie trong cookie jar để gọi `GET /api/auth/me` hoặc API được cấp quyền tiếp theo.

Với các yêu cầu `POST`, `PUT`, `PATCH`, `DELETE` đến `/api/**` dùng cookie, thêm `X-Requested-With: fetch` hoặc `XMLHttpRequest`. Backend cũng hỗ trợ `Authorization: Bearer <token>`; bộ lọc hiện bỏ qua yêu cầu header `X-Requested-With` khi nhận Bearer token. Quyền truy cập cụ thể được kiểm tra ở Spring Security và các thành phần nghiệp vụ.

## Cấu trúc dự án

```text
.
├── .mvn/wrapper/                 # Cấu hình Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/thinh/cosmetic/
│   │   │   ├── config/           # Cấu hình, bảo mật, dữ liệu mẫu
│   │   │   ├── controller/       # Điều hướng trang Thymeleaf
│   │   │   ├── rest/             # REST API
│   │   │   ├── service/          # Xử lý nghiệp vụ và dịch vụ tích hợp
│   │   │   ├── repository/       # Truy cập dữ liệu qua JPA
│   │   │   ├── domain/           # Entity, DTO, enum
│   │   │   ├── mapper/           # Ánh xạ Entity và DTO
│   │   │   ├── security/         # JWT, cookie, quyền, phạm vi chi nhánh
│   │   │   └── exception/        # Lỗi nghiệp vụ và xử lý lỗi tập trung
│   │   └── resources/
│   │       ├── templates/       # Giao diện khách hàng và quản trị
│   │       ├── static/          # CSS, JavaScript, hình ảnh
│   │       └── application*.properties
│   └── test/                    # Kiểm thử và cấu hình H2
├── docker-compose.yml           # PostgreSQL local
├── mvnw / mvnw.cmd              # Maven Wrapper cho Unix / Windows
└── pom.xml                      # Dependency và cấu hình build
```

Các module được tổ chức quanh Controller/REST Controller, Service, Repository, Entity và DTO. Giao diện nhận HTML do Thymeleaf dựng hoặc gọi REST API bằng Fetch API để cập nhật dữ liệu sau thao tác.

<a id="troubleshooting"></a>

## Xử lý lỗi thường gặp

| Hiện tượng | Cách kiểm tra |
| --- | --- |
| Không tìm thấy Docker daemon | Mở Docker Desktop hoặc khởi động dịch vụ Docker, sau đó chạy lại `docker compose up -d postgres`. |
| Không kết nối được PostgreSQL | Kiểm tra `docker compose ps`, `docker compose logs postgres` và `pg_isready`. Cổng truy cập từ máy là `5433`, không phải cổng `5432` bên trong container. |
| `password authentication failed` | Đối chiếu `SPRING_DATASOURCE_PASSWORD` với database đang lưu trong volume. Đổi giá trị trong Compose không tự đổi mật khẩu của database đã khởi tạo. |
| `Could not resolve placeholder` | Kiểm tra các biến môi trường bắt buộc; chọn profile `local` để sử dụng URL và username local đã khai báo. |
| Java/Maven dùng sai phiên bản | Kiểm tra `java -version`, `./mvnw -v` và `JAVA_HOME`; dùng JDK 21. |
| `Permission denied: ./mvnw` | Trên macOS/Linux, cấp quyền thực thi bằng `chmod +x mvnw`. |
| Cổng HTTP đang được sử dụng | Dừng ứng dụng cũ hoặc thiết lập biến môi trường `PORT` sang cổng còn trống. |
| Đăng nhập local nhưng không giữ được trạng thái | Chạy với profile `local`; kiểm tra cookie. Cookie `Secure` của profile `prod` cần HTTPS. |
| API ghi dữ liệu trả 403 | Kiểm tra tài khoản/quyền, cookie hoặc Bearer token và header `X-Requested-With`; với đơn hàng, kiểm tra cả chi nhánh được phân công. |
| Không nhận được OTP qua email | Kiểm tra cấu hình SMTP; khi chạy thử chưa cấu hình gửi thư, lấy OTP ở log terminal ứng dụng. |
| Upload ảnh bị từ chối | Kiểm tra cấu hình Cloudinary, quyền tài khoản, dung lượng ảnh và giới hạn multipart. |

## Nhóm thực hiện

Dự án được phát triển trong học phần **Công nghệ phần mềm**, Khoa Công nghệ Thông tin, Trường Đại học Công nghệ Kỹ thuật TP. Hồ Chí Minh — **Nhóm 01**.

| Thành viên | MSSV |
| --- | --- |
| Hà Nguyễn Nhật Nguyên | 24110288 |
| Lý Đông Thịnh | 24110337 |

**Giảng viên hướng dẫn:** ThS. Nguyễn Trần Thi Văn.
