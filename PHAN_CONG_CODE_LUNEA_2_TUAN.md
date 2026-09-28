# PHÂN CÔNG NHIỆM VỤ CODE LUNEA -- 2 TUẦN

> **Dự án:** Xây dựng website bán mỹ phẩm LUNEA theo mô hình chuỗi cửa
> hàng\
> **Nhóm:** 01\
> **Thành viên:** Lý Đông Thịnh, Hà Nguyễn Nhật Nguyên\
> **Thời gian:** 2 tuần\
> **Định hướng kỹ thuật:** Spring Boot + Spring Data JPA + Thymeleaf +
> Bootstrap + Spring Security + PostgreSQL\
> **Nguyên tắc:** Ưu tiên hoàn thành luồng nghiệp vụ chạy được
> end-to-end, sau đó mới mở rộng các chức năng phụ.

------------------------------------------------------------------------

## 1. Mục tiêu sau 2 tuần

Sau 2 tuần, project cần đạt tối thiểu:

-   Project Spring Boot chạy ổn định.
-   Kết nối PostgreSQL bằng Docker.
-   Entity và quan hệ JPA khớp với database.
-   Có cấu trúc rõ ràng:
    -   `entity`
    -   `repository`
    -   `service`
    -   `controller`
    -   `dto` nếu cần
    -   `security`
    -   `templates`
    -   `static`
-   Đăng nhập/đăng xuất và phân quyền cơ bản hoạt động.
-   Khách hàng có thể:
    -   xem sản phẩm;
    -   tìm kiếm/lọc;
    -   xem chi tiết;
    -   thêm/cập nhật/xóa giỏ hàng;
    -   đặt hàng ở mức MVP.
-   Admin/nhân viên có thể:
    -   xem danh sách sản phẩm;
    -   thêm/sửa/xóa sản phẩm;
    -   quản lý danh mục/thương hiệu ở mức cơ bản;
    -   xem và cập nhật đơn hàng ở mức cơ bản.
-   Các màn hình chính kết nối được với backend thật, không chỉ dùng dữ
    liệu giả.
-   Có validation và xử lý lỗi cơ bản.
-   Có dữ liệu mẫu để demo.
-   Có test cho các nghiệp vụ quan trọng.
-   Hai nhánh code có thể merge mà không phá project.

------------------------------------------------------------------------

# 2. Nguyên tắc phân công

## 2.1. Không chia theo kiểu "mỗi người một nửa UC"

Project hiện có nhiều Use Case, nhưng nhiều UC phụ thuộc lẫn nhau. Ví
dụ:

`Xem sản phẩm → Chi tiết sản phẩm → Giỏ hàng → Đặt hàng → Đơn hàng`

Do đó chia theo module sẽ hợp lý hơn:

### Thành viên 1 -- Đông Thịnh

Phụ trách **Backend core + Authentication + Customer flow**

-   Cấu trúc Spring Boot.
-   Entity/JPA liên quan người dùng và khách hàng.
-   Spring Security.
-   Đăng nhập/đăng xuất.
-   Sản phẩm phía khách hàng.
-   Tìm kiếm/lọc.
-   Chi tiết sản phẩm.
-   Giỏ hàng.
-   Đặt hàng.
-   Validation nghiệp vụ phía customer.
-   API/service dùng chung khi cần.

### Thành viên 2 -- Nhật Nguyên

Phụ trách **Admin + Product Management + Order Management + giao diện
quản trị**

-   Entity/JPA liên quan sản phẩm, danh mục, thương hiệu, đơn hàng nếu
    được chia riêng.
-   Admin layout.
-   Dashboard cơ bản.
-   CRUD sản phẩm.
-   CRUD danh mục.
-   CRUD thương hiệu.
-   Quản lý đơn hàng.
-   Cập nhật trạng thái đơn.
-   Validation phía admin.
-   Dữ liệu mẫu và hỗ trợ kiểm thử.

> Hai người vẫn phải review code của nhau. "Phụ trách" không có nghĩa là
> người còn lại không được sửa module đó.

------------------------------------------------------------------------

# 3. Quy ước Git

## Branch chính

``` text
main
```

Không code trực tiếp trên `main`.

## Branch của Thịnh

``` text
feature/thinh-auth-customer
feature/thinh-product-customer
feature/thinh-cart-order
```

## Branch của Nguyên

``` text
feature/nguyen-admin-product
feature/nguyen-admin-order
feature/nguyen-category-brand
```

## Quy tắc commit

Dùng format:

``` text
feat: thêm chức năng đăng nhập
feat: thêm CRUD sản phẩm
fix: sửa lỗi cập nhật giỏ hàng
refactor: tách service xử lý đơn hàng
test: thêm test ProductService
docs: cập nhật README
```

Mỗi commit nên tương đối nhỏ và có ý nghĩa.

Không commit:

``` text
target/
.idea/
*.iml
.env
```

Thông tin mật khẩu/database phải nằm trong biến môi trường hoặc file
local không commit.

------------------------------------------------------------------------

# 4. TUẦN 1 -- XÂY DỰNG NỀN TẢNG VÀ MODULE CHÍNH

## Ngày 1 -- Chuẩn hóa project

### Đông Thịnh

-   Kiểm tra project Spring Boot hiện tại.
-   Chuẩn hóa `pom.xml`.
-   Kiểm tra PostgreSQL Docker.
-   Cấu hình `application.properties`/`application.yml`.
-   Kiểm tra connection đến database.
-   Tạo package structure.
-   Kiểm tra Spring Data JPA.
-   Kiểm tra Spring Security dependency.
-   Tạo `.gitignore`.
-   Tạo branch riêng.
-   Kiểm tra project có thể build sạch:

``` bash
./mvnw clean test
```

### Nhật Nguyên

-   Kiểm tra database hiện tại.
-   Đối chiếu bảng database với tài liệu thiết kế.
-   Xác định các bảng phục vụ MVP:
    -   user/account;
    -   role;
    -   product;
    -   category;
    -   brand;
    -   inventory nếu đã có;
    -   cart/cart_item;
    -   order/order_item;
    -   address nếu đã có.
-   Chuẩn bị dữ liệu mẫu.
-   Xác định các trường bắt buộc.
-   Ghi lại các FK và quan hệ.

### Kết quả cuối ngày

``` text
Spring Boot
    ↓
PostgreSQL Docker
    ↓
JPA
    ↓
Database
```

phải chạy được.

------------------------------------------------------------------------

# Ngày 2 -- Entity và Repository

## Đông Thịnh

Tạo/hoàn thiện:

``` text
User/Account
Role
Customer
Address
Cart
CartItem
```

Tạo repository tương ứng.

Ví dụ:

``` java
public interface CartRepository
        extends JpaRepository<Cart, Long> {
}
```

Kiểm tra:

-   `@Entity`
-   `@Id`
-   `@GeneratedValue`
-   `@ManyToOne`
-   `@OneToMany`
-   `@OneToOne`
-   `mappedBy`
-   cascade
-   fetch strategy

## Nhật Nguyên

Tạo/hoàn thiện:

``` text
Product
Category
Brand
ProductImage
Inventory
Order
OrderItem
```

Tạo repository tương ứng.

Đặc biệt kiểm tra:

``` text
Product → Category
Product → Brand
Product → ProductImage
Inventory → Product
Order → Customer
OrderItem → Order
OrderItem → Product
```

### Kết quả

Toàn bộ entity MVP khởi động được mà không lỗi Hibernate.

------------------------------------------------------------------------

# Ngày 3 -- Repository + Service

## Đông Thịnh

### Authentication

Xây dựng:

``` text
UserDetailsService
SecurityConfig
PasswordEncoder
Authentication
```

Hoàn thành:

-   đăng nhập;
-   đăng xuất;
-   password hashing;
-   phân quyền URL cơ bản.

Ví dụ:

``` text
/login
/logout
/customer/**
/admin/**
```

### Customer Service

Tạo:

``` text
CustomerService
CartService
AddressService
```

## Nhật Nguyên

Tạo:

``` text
ProductService
CategoryService
BrandService
InventoryService
OrderService
```

CRUD cơ bản:

``` text
findAll()
findById()
save()
update()
delete()
```

Không viết toàn bộ business logic trong Controller.

------------------------------------------------------------------------

# Ngày 4 -- Customer Product Flow

## Đông Thịnh

Làm phần website khách hàng:

### UC -- Product

-   Xem danh sách sản phẩm.
-   Tìm kiếm.
-   Lọc.
-   Sắp xếp.
-   Xem chi tiết.

URL dự kiến:

``` text
/products
/products/{id}
/products/search
```

Service cần hỗ trợ:

``` text
findAll()
findById()
search()
filter()
sort()
```

Nếu dùng Spring Data JPA Specification thì chỉ áp dụng khi cần; không
làm quá phức tạp trong tuần đầu.

## Nhật Nguyên

Làm Admin Product:

``` text
/admin/products
/admin/products/create
/admin/products/edit/{id}
/admin/products/delete/{id}
```

Chức năng:

-   danh sách;
-   thêm;
-   sửa;
-   xóa;
-   tìm kiếm;
-   lọc cơ bản.

------------------------------------------------------------------------

# Ngày 5 -- Cart + Category + Brand

## Đông Thịnh

Hoàn thành giỏ hàng:

``` text
Xem giỏ hàng
Thêm sản phẩm
Cập nhật số lượng
Xóa sản phẩm
Tính subtotal
```

URL dự kiến:

``` text
/cart
/cart/add
/cart/update
/cart/remove
```

Kiểm tra:

-   sản phẩm không tồn tại;
-   số lượng \<= 0;
-   số lượng vượt tồn kho;
-   user chưa đăng nhập.

## Nhật Nguyên

Hoàn thành:

### Category

``` text
/admin/categories
/admin/categories/create
/admin/categories/edit
/admin/categories/delete
```

### Brand

``` text
/admin/brands
/admin/brands/create
/admin/brands/edit
/admin/brands/delete
```

Đồng thời tích hợp Category/Brand vào form Product.

------------------------------------------------------------------------

# Ngày 6 -- Giao diện + tích hợp

## Đông Thịnh

Hoàn thiện giao diện customer:

``` text
templates/
├── layout/
├── home/
├── auth/
├── product/
└── cart/
```

Tích hợp:

``` text
Thymeleaf
+
Controller
+
Service
+
Database
```

Không để dữ liệu sản phẩm hard-code trong HTML.

## Nhật Nguyên

Hoàn thiện Admin layout:

``` text
templates/admin/
├── layout/
├── dashboard/
├── products/
├── categories/
├── brands/
└── orders/
```

Tạo:

-   sidebar;
-   header;
-   bảng dữ liệu;
-   form;
-   pagination nếu cần.

------------------------------------------------------------------------

# Ngày 7 -- Review tuần 1

## Cả hai cùng làm

### 1. Merge code

Không merge toàn bộ một lúc.

Thứ tự:

``` text
database
↓
entity
↓
repository
↓
service
↓
controller
↓
template
```

### 2. Kiểm tra

-   Project start được.
-   Login được.
-   Product list được.
-   Product detail được.
-   Admin product CRUD được.
-   Category/Brand CRUD được.
-   Cart hoạt động.

### 3. Sửa conflict

Nếu có conflict:

``` bash
git pull
git merge
```

Không tự ý xóa code của người kia để hết conflict.

### 4. Tag

Nếu tuần 1 ổn:

``` text
v0.1-week1
```

------------------------------------------------------------------------

# 5. TUẦN 2 -- ĐẶT HÀNG, ADMIN ORDER, SECURITY, TEST

# Ngày 8 -- Order

## Đông Thịnh

Phụ trách customer checkout:

``` text
/cart
    ↓
/checkout
    ↓
/order/create
    ↓
/order/success
```

Xử lý:

-   chọn địa chỉ;
-   kiểm tra tồn kho;
-   tính tổng;
-   tạo Order;
-   tạo OrderItem;
-   giảm/giữ tồn kho theo thiết kế hiện tại;
-   xóa giỏ hàng sau khi đặt thành công.

Quan trọng:

**Không tạo Order nếu một trong các bước nghiệp vụ chính thất bại.**

Nếu cần transaction:

``` java
@Transactional
```

## Nhật Nguyên

Làm Admin Order:

``` text
/admin/orders
/admin/orders/{id}
```

Hiển thị:

-   mã đơn;
-   khách hàng;
-   ngày đặt;
-   tổng tiền;
-   trạng thái.

Cho phép cập nhật trạng thái theo workflow đã thống nhất.

Ví dụ:

``` text
PENDING
→ CONFIRMED
→ PROCESSING
→ SHIPPING
→ DELIVERED
```

Các trạng thái hủy/trả xử lý riêng.

------------------------------------------------------------------------

# Ngày 9 -- Customer Account + Order History

## Đông Thịnh

Làm:

``` text
/account
/account/profile
/account/address
/orders
/orders/{id}
```

Chức năng:

-   xem thông tin cá nhân;
-   cập nhật thông tin;
-   quản lý địa chỉ;
-   xem lịch sử đơn;
-   xem chi tiết đơn.

## Nhật Nguyên

Bổ sung admin:

-   xem chi tiết đơn;
-   cập nhật trạng thái;
-   kiểm tra thông tin sản phẩm trong đơn;
-   kiểm tra tồn kho liên quan;
-   validation trạng thái.

------------------------------------------------------------------------

# Ngày 10 -- Security + Authorization

## Đông Thịnh

Hoàn thiện Spring Security.

Phân quyền:

``` text
ROLE_CUSTOMER
ROLE_STAFF
ROLE_ADMIN
```

Ví dụ:

``` text
/customer/** → CUSTOMER
/admin/products/** → ADMIN/STAFF phù hợp
/admin/orders/** → STAFF/ADMIN phù hợp
```

Kiểm tra:

-   chưa đăng nhập;
-   CUSTOMER truy cập admin;
-   STAFF truy cập chức năng không thuộc quyền;
-   ADMIN truy cập toàn bộ khu vực được cấp.

## Nhật Nguyên

Kiểm tra giao diện theo role:

-   menu admin;
-   menu customer;
-   ẩn/hiện nút theo quyền;
-   xử lý 403;
-   xử lý 404.

Không chỉ ẩn nút bằng HTML. Backend vẫn phải kiểm tra quyền.

------------------------------------------------------------------------

# Ngày 11 -- Validation + Error Handling

## Đông Thịnh

Thêm validation:

``` java
@NotBlank
@NotNull
@Size
@Email
@Positive
@Min
```

Áp dụng cho:

-   đăng ký;
-   profile;
-   address;
-   cart;
-   checkout.

## Nhật Nguyên

Validation admin:

-   product;
-   category;
-   brand;
-   order status.

Tạo xử lý lỗi thống nhất:

``` text
404
403
400
500
```

Có thể dùng:

``` text
@ControllerAdvice
```

nếu phù hợp với cấu trúc hiện tại.

------------------------------------------------------------------------

# Ngày 12 -- Dashboard + dữ liệu mẫu

## Nhật Nguyên

Làm Admin Dashboard mức MVP:

``` text
Tổng sản phẩm
Tổng đơn hàng
Tổng khách hàng
Doanh thu
Đơn hàng gần đây
Sản phẩm sắp hết hàng
```

Không cần làm biểu đồ phức tạp nếu chưa có dữ liệu ổn định.

## Đông Thịnh

Chuẩn bị dữ liệu demo:

-   tài khoản;
-   role;
-   customer;
-   product;
-   category;
-   brand;
-   inventory;
-   order;
-   order item.

Tạo script seed data hoặc cách import dữ liệu thống nhất.

------------------------------------------------------------------------

# Ngày 13 -- Testing + Bug fixing

## Đông Thịnh

Test:

### Authentication

-   login đúng;
-   login sai;
-   logout;
-   unauthorized.

### Product

-   list;
-   search;
-   filter;
-   detail.

### Cart

-   add;
-   update;
-   remove;
-   invalid quantity.

### Order

-   checkout;
-   invalid stock;
-   empty cart;
-   order history.

## Nhật Nguyên

Test:

### Admin Product

-   create;
-   update;
-   delete;
-   validation.

### Category/Brand

-   create;
-   update;
-   delete.

### Admin Order

-   list;
-   detail;
-   update status.

### Authorization

-   CUSTOMER → admin;
-   STAFF → chức năng không được phép;
-   ADMIN → chức năng admin.

------------------------------------------------------------------------

# Ngày 14 -- Integration + Demo

## Cả hai

### Bước 1 -- Clean project

``` bash
./mvnw clean
```

### Bước 2 -- Test

``` bash
./mvnw test
```

### Bước 3 -- Build

``` bash
./mvnw package
```

### Bước 4 -- Chạy PostgreSQL

``` bash
docker compose up -d
```

### Bước 5 -- Chạy Spring Boot

``` bash
./mvnw spring-boot:run
```

### Bước 6 -- Test luồng hoàn chỉnh

## Customer

``` text
Trang chủ
→ Sản phẩm
→ Tìm kiếm/lọc
→ Chi tiết
→ Đăng nhập
→ Thêm giỏ hàng
→ Checkout
→ Đặt hàng
→ Xem đơn hàng
```

## Admin

``` text
Đăng nhập
→ Dashboard
→ Product
→ Category
→ Brand
→ Order
→ Cập nhật trạng thái
```

### Bước 7 -- Tag phiên bản

``` text
v0.2-two-weeks
```

------------------------------------------------------------------------

# 6. Bảng phân công tổng hợp

  Hạng mục                   Thịnh    Nguyên
  ------------------------- -------- --------
  Spring Boot structure      Chính    Review
  PostgreSQL/Docker          Chính    Hỗ trợ
  Entity Account/Customer    Chính    Review
  Entity Product             Review   Chính
  Entity Order               Chính    Chính
  Repository                 Chính    Chính
  Spring Security            Chính    Review
  Customer Product           Chính    Hỗ trợ
  Admin Product              Review   Chính
  Category                   Hỗ trợ   Chính
  Brand                      Hỗ trợ   Chính
  Cart                       Chính    Review
  Checkout                   Chính    Review
  Order Customer             Chính    Review
  Order Admin                Review   Chính
  Customer Profile           Chính    Hỗ trợ
  Address                    Chính    Hỗ trợ
  Dashboard                  Hỗ trợ   Chính
  Validation                 Chính    Chính
  Error Handling             Chính    Chính
  Test                       Chính    Chính
  Integration                Chính    Chính
  Bug fixing                 Chính    Chính

------------------------------------------------------------------------

# 7. Phạm vi MVP nên ưu tiên

Nếu thời gian không đủ, thứ tự ưu tiên:

## P0 -- Bắt buộc

``` text
Database
Spring Boot
JPA
Login
Role
Product
Category
Brand
Product Detail
Cart
Checkout
Order
Admin Product
Admin Order
```

## P1 -- Nên có

``` text
Search
Filter
Customer Profile
Address
Order History
Inventory cơ bản
Dashboard
Validation
Error Handling
```

## P2 -- Có thể làm sau

``` text
Wishlist
Beauty Profile
Recommendation
Voucher
Promotion
Review
Return/Refund
Shipping integration
Payment gateway
Email/OTP
Google Maps
Advanced reporting
```

> Không nên cố hoàn thành toàn bộ 91 UC trong 2 tuần nếu các module P0
> chưa ổn định.

------------------------------------------------------------------------

# 8. Cách hai người phối hợp để tránh conflict

## Quy tắc 1 -- Không cùng sửa một file nếu không cần

Ví dụ:

``` text
Thịnh → SecurityConfig.java
Nguyên → ProductController.java
```

thay vì cả hai cùng sửa một Controller lớn.

## Quy tắc 2 -- Tách Service theo module

Không tạo một:

``` text
LuneaService.java
```

mà nên có:

``` text
ProductService
CartService
OrderService
CustomerService
CategoryService
BrandService
```

## Quy tắc 3 -- Controller mỏng

Không viết:

``` java
@PostMapping("/checkout")
public String checkout(...) {
    // 100 dòng xử lý nghiệp vụ
}
```

Nên:

``` java
@PostMapping("/checkout")
public String checkout(...) {
    orderService.createOrder(...);
    return "redirect:/orders";
}
```

## Quy tắc 4 -- Mỗi module phải chạy được trước khi merge

Không merge code "đang làm dở" vào `main`.

## Quy tắc 5 -- Cuối mỗi ngày phải push

Mỗi thành viên:

``` text
code
→ test
→ commit
→ push
```

------------------------------------------------------------------------

# 9. Definition of Done

Một chức năng chỉ được xem là hoàn thành khi:

-   [ ] Có Entity nếu cần.
-   [ ] Có Repository nếu cần.
-   [ ] Có Service.
-   [ ] Có Controller.
-   [ ] Có giao diện nếu là chức năng web.
-   [ ] Kết nối database thật.
-   [ ] Có validation cơ bản.
-   [ ] Có xử lý trường hợp lỗi.
-   [ ] Test được happy case.
-   [ ] Test được ít nhất một trường hợp lỗi.
-   [ ] Không hard-code dữ liệu nghiệp vụ trong HTML.
-   [ ] Không chứa password/API key trong Git.
-   [ ] Code đã được push lên branch.
-   [ ] Người còn lại đã pull và kiểm tra.

------------------------------------------------------------------------

# 10. Checklist cuối 2 tuần

## Backend

-   [ ] Spring Boot chạy.
-   [ ] PostgreSQL Docker chạy.
-   [ ] JPA mapping ổn định.
-   [ ] Repository hoạt động.
-   [ ] Service tách đúng nghiệp vụ.
-   [ ] Controller không chứa quá nhiều business logic.
-   [ ] Transaction cho nghiệp vụ cần thiết.
-   [ ] Validation.
-   [ ] Exception handling.

## Security

-   [ ] Login.
-   [ ] Logout.
-   [ ] Password hash.
-   [ ] Role.
-   [ ] Authorization.
-   [ ] 403/404.

## Customer

-   [ ] Product list.
-   [ ] Search.
-   [ ] Filter.
-   [ ] Product detail.
-   [ ] Cart.
-   [ ] Checkout.
-   [ ] Order history.
-   [ ] Profile.

## Admin

-   [ ] Dashboard.
-   [ ] Product CRUD.
-   [ ] Category CRUD.
-   [ ] Brand CRUD.
-   [ ] Order management.
-   [ ] Order status.

## Database

-   [ ] FK.
-   [ ] Unique constraint.
-   [ ] NOT NULL.
-   [ ] Index cho các cột tìm kiếm/JOIN quan trọng.
-   [ ] Seed data.
-   [ ] Docker volume giữ dữ liệu.

## Git

-   [ ] Không commit secret.
-   [ ] Không commit `target`.
-   [ ] Không commit `.idea`.
-   [ ] Branch rõ ràng.
-   [ ] Commit message rõ ràng.
-   [ ] Merge `main` trước khi demo.
-   [ ] Có tag phiên bản cuối.

------------------------------------------------------------------------

# 11. Lưu ý về phạm vi

Tài liệu này lấy **91 Use Case của LUNEA làm phạm vi tổng thể**, nhưng
kế hoạch 2 tuần chỉ tập trung vào một **MVP có thể chạy và demo được**.

Các UC còn lại không bị bỏ khỏi thiết kế hệ thống; chúng được đưa vào
backlog để phát triển tiếp sau khi luồng chính ổn định.

Đặc biệt, không nên ưu tiên các tích hợp bên ngoài như Payment Gateway,
Shipping Provider, Email/OTP hoặc Google Maps trước khi các nghiệp vụ
nội bộ của LUNEA đã chạy ổn định.
