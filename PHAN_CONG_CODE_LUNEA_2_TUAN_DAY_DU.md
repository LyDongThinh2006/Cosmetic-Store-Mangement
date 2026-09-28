# PHÂN CÔNG CODE PROJECT LUNEA -- 2 TUẦN

> **Project:** Xây dựng website bán mỹ phẩm LUNEA theo mô hình chuỗi cửa
> hàng\
> **Thành viên:** Thịnh và Nguyên\
> **Thời gian:** 2 tuần\
> **Mục tiêu:** Hoàn thiện một phiên bản chạy được, có kiến trúc rõ
> ràng, dễ merge, dễ mở rộng và đủ cơ sở để demo/báo cáo.

------------------------------------------------------------------------

# 1. THÔNG TIN CHUNG

## 1.1. Thành viên

  -----------------------------------------------------------------------
  Thành viên                          Phụ trách chính
  ----------------------------------- -----------------------------------
  **Thịnh**                           Entity, Repository, Service,
                                      Business Logic, DTO/Mapper khi liên
                                      quan đến backend core,
                                      JPA/Hibernate, PostgreSQL, xử lý dữ
                                      liệu

  **Nguyên**                          Controller, REST API, Thymeleaf
                                      Frontend, HTML, CSS, JavaScript,
                                      giao diện, tích hợp UI với backend,
                                      Security ở tầng request/UI
  -----------------------------------------------------------------------

### Nguyên tắc quan trọng

-   Thịnh chịu trách nhiệm chính cho **tầng dữ liệu và nghiệp vụ**.
-   Nguyên chịu trách nhiệm chính cho **tầng giao tiếp với client và
    giao diện**.
-   Không được để business logic quan trọng trong Controller.
-   Không để Frontend truy cập trực tiếp Repository.
-   Không để Controller xử lý trực tiếp database.
-   Không copy business logic sang cả Controller và Service.
-   Khi một chức năng cần cả hai người:
    -   Thịnh làm Entity → Repository → Service.
    -   Nguyên dựa vào Service/API contract để làm Controller →
        Frontend.
-   Nếu cần thay đổi database hoặc business rule, Nguyên phải trao đổi
    với Thịnh trước khi sửa.
-   Nếu cần thay đổi API contract, hai người phải thống nhất trước khi
    code.

------------------------------------------------------------------------

# 2. KIẾN TRÚC ĐƯỢC SỬ DỤNG

Project sử dụng kiến trúc nhiều tầng:

``` text
Frontend
   │
   ├── Thymeleaf Page
   │
   └── JavaScript / REST API
             │
             ▼
      Controller / REST Controller
             │
             ▼
          Service
             │
             ▼
        Repository
             │
             ▼
       Entity / JPA
             │
             ▼
        PostgreSQL
```

## 2.1. Quy tắc luồng dữ liệu

``` text
Request
   ↓
Controller
   ↓
DTO / Request Model
   ↓
Service
   ↓
Repository
   ↓
Entity
   ↓
Database
```

Chiều trả dữ liệu:

``` text
Database
   ↓
Entity
   ↓
Repository
   ↓
Service
   ↓
DTO / Response
   ↓
Controller
   ↓
Thymeleaf / JSON
   ↓
Frontend
```

## 2.2. Không được làm

``` java
// KHÔNG NÊN
@PostMapping("/products")
public String create(Product product) {
    productRepository.save(product);
    return "redirect:/products";
}
```

Controller không nên tự xử lý nghiệp vụ hoặc gọi Repository trực tiếp.

## 2.3. Nên làm

``` java
@PostMapping("/products")
public String create(@Valid ProductRequest request,
                     BindingResult result) {

    if (result.hasErrors()) {
        return "product/form";
    }

    productService.create(request);

    return "redirect:/products";
}
```

Controller chỉ tiếp nhận request và gọi Service.

------------------------------------------------------------------------

# 3. MÔ HÌNH HYBRID CONTROLLER + REST API

Project LUNEA sử dụng kết hợp:

## 3.1. `@Controller`

Dùng cho các trang Thymeleaf:

``` java
@Controller
@RequestMapping("/products")
public class ProductController {
}
```

Ví dụ:

``` text
GET /products
GET /products/{id}
GET /products/create
POST /products/create
GET /products/edit/{id}
```

Controller trả về tên view:

``` java
return "product/list";
```

## 3.2. `@RestController`

Dùng cho các API trả JSON:

``` java
@RestController
@RequestMapping("/api/products")
public class ProductRestController {
}
```

Ví dụ:

``` text
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
```

## 3.3. Không tạo API chỉ để có API

Nếu một chức năng chỉ cần:

``` text
Form Thymeleaf
→ POST Controller
→ Service
→ Database
```

thì không bắt buộc phải tạo thêm REST API.

REST API nên dùng khi:

-   JavaScript cần dữ liệu JSON.
-   Có AJAX/fetch.
-   Có tìm kiếm động.
-   Có phân trang động.
-   Có filter/sort động.
-   Chức năng có khả năng được dùng bởi client khác.
-   Cần tách rõ API contract.

------------------------------------------------------------------------

# 4. CẤU TRÚC PACKAGE

Đề xuất:

``` text
src/main/java/com/lunea/
│
├── config/
│
├── controller/
│   ├── HomeController.java
│   ├── ProductController.java
│   ├── CategoryController.java
│   ├── BrandController.java
│   ├── CartController.java
│   ├── OrderController.java
│   └── ...
│
├── rest/
│   ├── ProductRestController.java
│   ├── CategoryRestController.java
│   └── ...
│
├── dto/
│   ├── request/
│   │   ├── ProductRequest.java
│   │   ├── LoginRequest.java
│   │   └── ...
│   │
│   └── response/
│       ├── ProductResponse.java
│       ├── OrderResponse.java
│       └── ...
│
├── entity/
│   ├── Product.java
│   ├── Category.java
│   ├── Brand.java
│   ├── Customer.java
│   ├── Order.java
│   └── ...
│
├── repository/
│   ├── ProductRepository.java
│   ├── CategoryRepository.java
│   ├── BrandRepository.java
│   └── ...
│
├── service/
│   ├── ProductService.java
│   ├── CategoryService.java
│   └── ...
│
├── service/
│   └── impl/
│       ├── ProductServiceImpl.java
│       ├── CategoryServiceImpl.java
│       └── ...
│
├── mapper/
│
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── BusinessException.java
│   └── GlobalExceptionHandler.java
│
└── security/
    ├── SecurityConfig.java
    └── ...
```

> Nếu project đang dùng cấu trúc package khác thì **không tự ý đổi toàn
> bộ project chỉ vì tài liệu này**. Chỉ thống nhất cấu trúc mới nếu
> project chưa có cấu trúc ổn định.

------------------------------------------------------------------------

# 5. QUY ƯỚC ĐẶT TÊN JAVA

## 5.1. Class

Dùng PascalCase:

``` text
Product
ProductService
ProductRepository
ProductController
ProductRequest
ProductResponse
```

Không dùng:

``` text
product
product_service
PRODUCT
```

------------------------------------------------------------------------

## 5.2. Method

Dùng camelCase:

``` java
findById()
findAll()
createProduct()
updateProduct()
deleteProduct()
findProductsByCategory()
```

Không dùng:

``` java
CreateProduct()
create_product()
CREATEPRODUCT()
```

------------------------------------------------------------------------

## 5.3. Variable

``` java
Product product;
ProductRequest request;
List<Product> products;
Long productId;
```

Tên phải thể hiện ý nghĩa.

Không dùng:

``` java
Product p;
Product x;
List<Product> list;
```

trừ trường hợp biến cực kỳ ngắn trong lambda/loop đơn giản.

------------------------------------------------------------------------

# 6. QUY ƯỚC PACKAGE

Package viết lowercase:

``` text
entity
repository
service
controller
dto
exception
security
config
```

Không:

``` text
Entity
Repository
ProductService
```

------------------------------------------------------------------------

# 7. ENTITY -- THỊNH PHỤ TRÁCH

## 7.1. Trách nhiệm

Thịnh chịu trách nhiệm:

-   Thiết kế Entity.
-   Mapping Entity ↔ Database.
-   Quan hệ giữa Entity.
-   Primary Key.
-   Foreign Key mapping.
-   Enum.
-   Constraint liên quan Entity.
-   JPA annotations.
-   Hibernate.
-   Kiểm tra mapping.
-   Migration/schema nếu project sử dụng migration.
-   Phối hợp PostgreSQL.

## 7.2. Quy tắc Entity

Ví dụ:

``` java
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private BigDecimal price;
}
```

## 7.3. Không đặt business logic lớn trong Entity

Không biến Entity thành nơi xử lý:

-   Tính tổng đơn hàng phức tạp.
-   Thanh toán.
-   Kiểm tra quyền.
-   Gửi email.
-   Gọi API ngoài.

Các nghiệp vụ này thuộc Service hoặc thành phần chuyên biệt.

------------------------------------------------------------------------

# 8. QUAN HỆ JPA

Cần thống nhất rõ:

``` text
@OneToMany
@ManyToOne
@OneToOne
@ManyToMany
```

Ưu tiên thiết kế quan hệ thực tế, tránh dùng `@ManyToMany` nếu có thể
tạo entity trung gian rõ ràng.

Ví dụ:

``` text
Order
   ↓
OrderDetail
   ↓
Product
```

thường rõ ràng hơn việc dùng `ManyToMany` trực tiếp giữa Order và
Product.

## 8.1. Lazy/Eager

Ưu tiên:

``` java
FetchType.LAZY
```

khi không cần tải toàn bộ quan hệ ngay.

Không tùy tiện dùng:

``` java
FetchType.EAGER
```

vì có thể làm query lấy quá nhiều dữ liệu.

------------------------------------------------------------------------

# 9. REPOSITORY -- THỊNH PHỤ TRÁCH

## 9.1. Repository

Ví dụ:

``` java
public interface ProductRepository
        extends JpaRepository<Product, Long> {
}
```

## 9.2. Derived Query

Có thể dùng:

``` java
findByName()
findByCategoryId()
findByBrandId()
findByPriceBetween()
findByNameContainingIgnoreCase()
```

Ví dụ:

``` java
List<Product> findByNameContainingIgnoreCase(String keyword);
```

## 9.3. Khi query phức tạp

Có thể sử dụng:

``` java
@Query
```

hoặc Specification/Criteria khi cần.

Không viết query SQL tùy tiện nếu một derived query đơn giản đã đáp ứng
được.

------------------------------------------------------------------------

# 10. SERVICE -- THỊNH PHỤ TRÁCH

Service là nơi chứa **business logic**.

Ví dụ:

``` java
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
}
```

## 10.1. Service phải xử lý

-   Kiểm tra dữ liệu nghiệp vụ.
-   Tìm Entity.
-   Kiểm tra tồn tại.
-   Tạo Entity.
-   Update Entity.
-   Delete.
-   Tính toán.
-   Kiểm tra trạng thái.
-   Xử lý quan hệ giữa nhiều Entity.
-   Transaction.

## 10.2. Ví dụ

``` java
@Override
@Transactional
public ProductResponse create(ProductRequest request) {

    Category category = categoryRepository
            .findById(request.getCategoryId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Category not found"));

    Product product = new Product();

    product.setName(request.getName());
    product.setPrice(request.getPrice());
    product.setCategory(category);

    productRepository.save(product);

    return productMapper.toResponse(product);
}
```

------------------------------------------------------------------------

# 11. DTO

Không nên trả Entity trực tiếp ra API trong project chính.

Không nên:

``` java
@GetMapping("/{id}")
public Product getProduct(@PathVariable Long id) {
    return productService.findById(id);
}
```

Nên:

``` java
@GetMapping("/{id}")
public ProductResponse getProduct(@PathVariable Long id) {
    return productService.findById(id);
}
```

## 11.1. Request DTO

``` text
ProductRequest
CategoryRequest
LoginRequest
RegisterRequest
OrderRequest
```

## 11.2. Response DTO

``` text
ProductResponse
CategoryResponse
OrderResponse
CustomerResponse
```

DTO giúp:

-   Không expose Entity.
-   Kiểm soát dữ liệu trả về.
-   Validation rõ ràng.
-   API ổn định hơn.
-   Dễ thay đổi database.

------------------------------------------------------------------------

# 12. MAPPER

Nếu project có nhiều DTO, sử dụng Mapper.

Ví dụ:

``` java
public ProductResponse toResponse(Product product) {
    ...
}
```

Có thể dùng:

-   Mapper tự viết.
-   MapStruct nếu team thống nhất thêm dependency.

Trong 2 tuần đầu, nếu project nhỏ thì **mapper thủ công** là đủ và dễ
kiểm soát.

------------------------------------------------------------------------

# 13. CONTROLLER -- NGUYÊN PHỤ TRÁCH

Nguyên phụ trách:

-   `@Controller`
-   `@RestController`
-   Request mapping.
-   Binding request.
-   Validation result.
-   Redirect.
-   View.
-   API response.
-   HTTP status.

## 13.1. Controller không làm business logic

Không:

``` java
if (product.getPrice() < 0) {
    ...
}

if (order.getStatus() == ...) {
    ...
}
```

nếu đây là business rule.

Nên chuyển vào Service.

------------------------------------------------------------------------

# 14. REST API CONVENTION

## 14.1. URL

Dùng danh từ số nhiều:

``` text
/api/products
/api/categories
/api/brands
/api/customers
/api/orders
```

Không:

``` text
/api/getProducts
/api/createProduct
/api/deleteProduct
```

HTTP method đã thể hiện hành động.

------------------------------------------------------------------------

## 14.2. HTTP Method

  Method   Ý nghĩa
  -------- -------------------
  GET      Lấy dữ liệu
  POST     Tạo
  PUT      Cập nhật toàn bộ
  PATCH    Cập nhật một phần
  DELETE   Xóa

------------------------------------------------------------------------

# 15. HTTP STATUS CODE

  Status   Ý nghĩa
  -------- ---------------------------
  200      Thành công
  201      Tạo thành công
  204      Thành công, không có body
  400      Request không hợp lệ
  401      Chưa xác thực
  403      Không có quyền
  404      Không tìm thấy
  409      Conflict
  500      Server Error

Không trả `200 OK` cho mọi trường hợp.

------------------------------------------------------------------------

# 16. FORMAT RESPONSE

Có thể thống nhất dạng:

``` json
{
  "success": true,
  "message": "Product created successfully",
  "data": {}
}
```

Lỗi:

``` json
{
  "success": false,
  "message": "Product not found",
  "data": null
}
```

Nếu project đã có format response khác thì giữ format hiện tại để tránh
breaking change.

------------------------------------------------------------------------

# 17. VALIDATION

Sử dụng Bean Validation:

``` java
@NotBlank
@Size
@Email
@Min
@Max
@NotNull
@Positive
```

Ví dụ:

``` java
public class ProductRequest {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal price;
}
```

Controller:

``` java
public String create(
        @Valid ProductRequest request,
        BindingResult result) {
}
```

Validation nghiệp vụ phức tạp vẫn phải nằm ở Service.

------------------------------------------------------------------------

# 18. EXCEPTION HANDLING

Không viết:

``` java
try {
   ...
} catch (Exception e) {
   return null;
}
```

Không nuốt Exception.

Tạo exception:

``` java
public class ResourceNotFoundException
        extends RuntimeException {
}
```

Global handler:

``` java
@RestControllerAdvice
public class GlobalExceptionHandler {
}
```

Mục tiêu:

``` text
Service throw Exception
        ↓
GlobalExceptionHandler
        ↓
HTTP response
```

------------------------------------------------------------------------

# 19. FRONTEND -- NGUYÊN PHỤ TRÁCH

Cấu trúc:

``` text
src/main/resources/
│
├── static/
│   ├── css/
│   ├── js/
│   ├── images/
│   └── ...
│
└── templates/
    ├── fragments/
    ├── layout/
    ├── home/
    ├── product/
    ├── category/
    ├── brand/
    ├── cart/
    ├── order/
    ├── customer/
    └── admin/
```

## 19.1. Thymeleaf

Dùng:

``` html
th:text
th:if
th:unless
th:each
th:href
th:src
th:value
th:object
th:field
th:action
```

## 19.2. Fragment

Các thành phần dùng chung:

``` text
header
navbar
sidebar
footer
pagination
modal
```

Ví dụ:

``` html
<div th:replace="~{fragments/header :: header}"></div>
```

Không copy/paste header vào 20 trang.

------------------------------------------------------------------------

# 20. CSS

Không đặt toàn bộ CSS vào từng HTML.

Nên:

``` text
static/css/
├── common.css
├── admin.css
├── product.css
├── cart.css
└── ...
```

Class:

``` text
.product-card
.product-title
.product-price
.btn-primary
```

Không dùng tên vô nghĩa:

``` text
.a
.box1
.test123
```

------------------------------------------------------------------------

# 21. JAVASCRIPT

``` text
static/js/
├── common.js
├── product.js
├── cart.js
├── order.js
└── ...
```

Không viết toàn bộ JavaScript vào một file vài nghìn dòng.

Nếu dùng fetch:

``` javascript
fetch('/api/products')
    .then(response => response.json())
    .then(data => {
        // render
    });
```

JavaScript chỉ xử lý UI/API interaction.

Business rule quan trọng vẫn ở backend.

------------------------------------------------------------------------

# 22. FORM THYMELEAF

Ví dụ:

``` html
<form th:action="@{/products/create}"
      th:object="${productRequest}"
      method="post">

    <input th:field="*{name}">

    <input th:field="*{price}">

    <button type="submit">Lưu</button>
</form>
```

Validation error phải được hiển thị rõ ràng.

------------------------------------------------------------------------

# 23. SPRING SECURITY

Nguyên phụ trách chính phần tích hợp request/UI.

Thịnh hỗ trợ dữ liệu User/Role nếu cần.

Phân quyền cần thống nhất:

``` text
CUSTOMER
STAFF
ADMIN
SUPER_ADMIN
```

Ví dụ:

``` text
/customer/**
/staff/**
/admin/**
```

Không chỉ ẩn button trên frontend để bảo mật.

Phải bảo vệ endpoint ở backend.

Ví dụ:

``` java
@PreAuthorize("hasRole('ADMIN')")
```

hoặc cấu hình trong `SecurityFilterChain`.

------------------------------------------------------------------------

# 24. PASSWORD

Không lưu password dạng plain text.

Sử dụng:

``` java
PasswordEncoder
```

Ví dụ:

``` java
BCryptPasswordEncoder
```

Database chỉ lưu password đã hash.

------------------------------------------------------------------------

# 25. POSTGRESQL + DOCKER

Database chạy bằng Docker Compose.

Ví dụ:

``` yaml
services:
  postgres:
    image: postgres
    ports:
      - "5432:5432"
```

Dữ liệu PostgreSQL phải sử dụng volume nếu muốn giữ dữ liệu khi
container bị dừng/xóa.

Ví dụ:

``` yaml
volumes:
  postgres_data:

services:
  postgres:
    volumes:
      - postgres_data:/var/lib/postgresql/data
```

## 25.1. Không commit

Không commit:

``` text
.env
password
secret key
private key
```

Nếu có thông tin nhạy cảm:

``` text
application-local.properties
.env
```

và đưa vào `.gitignore`.

------------------------------------------------------------------------

# 26. APPLICATION CONFIG

Nên tách:

``` text
application.properties
application-dev.properties
application-prod.properties
```

Không hard-code:

``` text
password
JWT secret
Cloudinary secret
database password
```

------------------------------------------------------------------------

# 27. GIT WORKFLOW

Chỉ sử dụng **2 nhánh chính**:

``` text
main
develop
```

## 27.1. `main`

`main` là phiên bản ổn định.

Không code trực tiếp trên `main`.

Không push code đang lỗi lên `main`.

------------------------------------------------------------------------

## 27.2. `develop`

`develop` là nhánh tích hợp của nhóm.

Feature hoàn thành:

``` text
feature/*
      ↓
develop
```

Sau khi project ổn định:

``` text
develop
   ↓
main
```

------------------------------------------------------------------------

# 28. FEATURE BRANCH

Dù chỉ có 2 nhánh chính, mỗi người vẫn tạo branch feature riêng từ
`develop`.

Ví dụ Thịnh:

``` text
feature/thinh-product-entity
feature/thinh-product-repository
feature/thinh-product-service
feature/thinh-order-service
```

Nguyên:

``` text
feature/nguyen-product-controller
feature/nguyen-product-ui
feature/nguyen-order-controller
feature/nguyen-admin-ui
```

Không code trực tiếp trên `develop`.

------------------------------------------------------------------------

# 29. GIT COMMIT CONVENTION

Dùng:

``` text
feat:
fix:
refactor:
docs:
style:
test:
chore:
```

Ví dụ:

``` text
feat: add Product entity
feat: implement ProductRepository
feat: implement ProductService
feat: add product controller
feat: add product list page

fix: fix product validation
fix: fix product price mapping

refactor: simplify ProductService

test: add product service test

docs: update API documentation
```

Commit không nên:

``` text
update
fix
code
abc
final
final2
final-final
```

------------------------------------------------------------------------

# 30. QUY TẮC COMMIT

Mỗi commit nên có một mục đích rõ ràng.

Không:

``` text
feat: add everything
```

Nên:

``` text
feat: add Product entity
feat: add Product repository
feat: implement product service
```

Commit nhỏ giúp:

-   Dễ review.
-   Dễ revert.
-   Dễ tìm lỗi.
-   Dễ merge.

------------------------------------------------------------------------

# 31. QUY TRÌNH LÀM VIỆC HẰNG NGÀY

Trước khi code:

``` bash
git checkout develop
git pull origin develop
```

Tạo feature branch:

``` bash
git checkout -b feature/thinh-product-service
```

Code.

Sau đó:

``` bash
git add .
git commit -m "feat: implement ProductService"
git push -u origin feature/thinh-product-service
```

Sau khi hoàn thành:

``` text
feature branch
      ↓
develop
```

Không merge thẳng feature vào `main`.

------------------------------------------------------------------------

# 32. QUY TẮC TRƯỚC KHI CODE

Mỗi người phải pull `develop` trước khi bắt đầu một task lớn.

Nếu task phụ thuộc task của người kia:

``` text
Thịnh hoàn thành Entity
        ↓
push/merge develop
        ↓
Nguyên lấy Entity mới
        ↓
code Controller
```

Không tự tạo một Entity giả chỉ để Controller compile nếu Entity thật
sắp được merge.

------------------------------------------------------------------------

# 33. XỬ LÝ CONFLICT

Khi conflict:

1.  Không hoảng.
2.  Không chọn toàn bộ `ours` hoặc `theirs` một cách máy móc.
3.  Đọc từng đoạn conflict.
4.  Xác định code nào mới hơn.
5.  Nếu conflict liên quan business logic → hỏi người phụ trách.
6.  Test lại sau khi resolve.
7.  Commit merge/resolution.

Đặc biệt:

``` text
Entity
Repository
Service
SecurityConfig
application.properties
pom.xml
```

là các file dễ gây conflict.

------------------------------------------------------------------------

# 34. FILE PHẢI TRAO ĐỔI GIỮA THỊNH VÀ NGUYÊN

Trước khi Nguyên làm Controller, Thịnh cần cung cấp:

``` text
Entity
Service interface
Service method
DTO request
DTO response
Business rule
```

Ví dụ:

``` java
ProductResponse getById(Long id);

Page<ProductResponse> getAll(Pageable pageable);

ProductResponse create(ProductRequest request);

ProductResponse update(Long id, ProductRequest request);

void delete(Long id);
```

Nguyên dựa trên contract này để làm Controller.

------------------------------------------------------------------------

# 35. SERVICE INTERFACE

Khuyến nghị:

``` java
public interface ProductService {

    ProductResponse getById(Long id);

    Page<ProductResponse> getAll(Pageable pageable);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);
}
```

Implementation:

``` java
@Service
public class ProductServiceImpl implements ProductService {
}
```

Ưu điểm:

-   Tách contract và implementation.
-   Dễ test.
-   Dễ thay implementation.
-   Controller không phụ thuộc implementation cụ thể.

------------------------------------------------------------------------

# 36. PAGINATION

Danh sách sản phẩm không nên lấy toàn bộ database nếu dữ liệu lớn.

Ví dụ:

``` java
Page<Product> findAll(Pageable pageable);
```

API:

``` text
GET /api/products?page=0&size=10
```

Có thể thêm:

``` text
sort=name,asc
```

------------------------------------------------------------------------

# 37. SEARCH / FILTER

Ví dụ:

``` text
GET /api/products?keyword=serum
```

Filter:

``` text
/api/products?categoryId=1
/api/products?brandId=2
/api/products?minPrice=100000&maxPrice=500000
```

Không tạo hàng chục endpoint nếu một endpoint query/filter có thể xử lý
hợp lý.

------------------------------------------------------------------------

# 38. TRANSACTION

Các nghiệp vụ có nhiều thao tác database nên sử dụng:

``` java
@Transactional
```

Ví dụ tạo order:

``` text
Create Order
   ↓
Create OrderDetails
   ↓
Update stock
   ↓
Save
```

Nếu một bước lỗi thì transaction cần rollback phù hợp.

------------------------------------------------------------------------

# 39. BUSINESS LOGIC CẦN THỐNG NHẤT

Các rule quan trọng phải được thống nhất trước:

-   Giá sản phẩm.
-   Giá giảm.
-   Tồn kho.
-   Trạng thái sản phẩm.
-   Trạng thái đơn hàng.
-   Trạng thái tài khoản.
-   Giỏ hàng.
-   Voucher nếu có.
-   Điểm/thành viên nếu có.
-   Quyền user.
-   Hủy đơn.
-   Thanh toán.
-   Gia hạn/thời hạn nếu có trong nghiệp vụ hệ thống.

Không để Thịnh và Nguyên tự hiểu một rule theo hai cách khác nhau.

------------------------------------------------------------------------

# 40. PHÂN CÔNG MODULE

## 40.1. THỊNH

### Backend Core

``` text
Entity
Repository
Service
DTO/Mapper backend
JPA
Hibernate
PostgreSQL
Transaction
Business logic
Database relationship
Query
Validation nghiệp vụ
Exception nghiệp vụ
```

### Module ưu tiên

``` text
Product
Category
Brand
Customer/User
Cart
Order
OrderDetail
Payment-related data
Inventory/Stock
Review
Voucher nếu có
```

------------------------------------------------------------------------

## 40.2. NGUYÊN

### Web/API Layer

``` text
Controller
REST Controller
Thymeleaf
HTML
CSS
JavaScript
Fragment
Form
UI validation
API integration
Security integration
Admin pages
Customer pages
```

### Module ưu tiên

``` text
Home
Product listing
Product detail
Category
Brand
Search
Cart UI
Checkout UI
Order UI
Customer account
Admin dashboard
Product management UI
Order management UI
```

------------------------------------------------------------------------

# 41. THỨ TỰ PHÁT TRIỂN MỘT CHỨC NĂNG

Mỗi chức năng làm theo:

``` text
1. Phân tích requirement
        ↓
2. Database / Entity
        ↓
3. Repository
        ↓
4. Service
        ↓
5. DTO
        ↓
6. Controller / REST API
        ↓
7. Thymeleaf / JavaScript
        ↓
8. Test
        ↓
9. Merge
```

Không làm frontend trước khi contract backend đã đủ rõ đối với chức năng
phức tạp.

------------------------------------------------------------------------

# 42. LỘ TRÌNH 14 NGÀY

## DAY 1 -- Khởi tạo project

### Thịnh

-   Kiểm tra database.
-   Kiểm tra Entity hiện có.
-   Chuẩn hóa JPA.
-   Xác định quan hệ Entity.
-   Kiểm tra Repository.
-   Kiểm tra PostgreSQL.
-   Kiểm tra Docker.

### Nguyên

-   Kiểm tra Thymeleaf.
-   Chuẩn hóa templates.
-   Chuẩn hóa static.
-   Tạo layout/fragments.
-   Kiểm tra Controller.
-   Tạo trang Home cơ bản.

### Kết quả

``` text
Project chạy được
Database kết nối được
Git workflow thống nhất
```

------------------------------------------------------------------------

# DAY 2 -- Product

### Thịnh

-   Product Entity.
-   Category Entity.
-   Brand Entity.
-   Repository.
-   Service interface.
-   Service implementation.
-   DTO.

### Nguyên

-   Product Controller.
-   Product API.
-   Product list.
-   Product detail.
-   Product form cơ bản.

------------------------------------------------------------------------

# DAY 3 -- Category + Brand

### Thịnh

-   Category Repository/Service.
-   Brand Repository/Service.
-   Search query.
-   Filter query.

### Nguyên

-   Category page.
-   Brand page.
-   Filter UI.
-   Search UI.

------------------------------------------------------------------------

# DAY 4 -- Product Management

### Thịnh

-   CRUD hoàn chỉnh.
-   Validation.
-   Exception.
-   Pagination.
-   Sorting.

### Nguyên

-   Admin product list.
-   Create.
-   Edit.
-   Delete.
-   Search.
-   Filter.
-   Pagination UI.

------------------------------------------------------------------------

# DAY 5 -- Customer/User

### Thịnh

-   Customer/User Entity.
-   Role.
-   Repository.
-   Service.
-   Password handling.

### Nguyên

-   Register.
-   Login page.
-   Profile page.
-   Logout.
-   Security integration.

------------------------------------------------------------------------

# DAY 6 -- Cart

### Thịnh

-   Cart logic.
-   Cart item.
-   Add product.
-   Remove product.
-   Update quantity.
-   Calculate subtotal/total.

### Nguyên

-   Cart page.
-   Quantity control.
-   Remove.
-   Total.
-   Empty cart.

------------------------------------------------------------------------

# DAY 7 -- CHECKPOINT 1

Cả hai:

-   Merge code.
-   Fix compile errors.
-   Test toàn bộ flow.
-   Kiểm tra database.
-   Kiểm tra Git.
-   Fix conflict.

### MVP checkpoint

Phải chạy được:

``` text
Home
 ↓
Product
 ↓
Product Detail
 ↓
Add Cart
 ↓
Cart
```

------------------------------------------------------------------------

# DAY 8 -- Order

### Thịnh

-   Order Entity.
-   OrderDetail Entity.
-   Order Repository.
-   Order Service.
-   Transaction.
-   Order status.

### Nguyên

-   Checkout page.
-   Order confirmation.
-   Order success.
-   Order detail page.

------------------------------------------------------------------------

# DAY 9 -- Customer Order

### Thịnh

-   Customer order query.
-   Order history.
-   Order cancellation rule.

### Nguyên

-   Order history UI.
-   Order detail.
-   Cancel order.
-   Status display.

------------------------------------------------------------------------

# DAY 10 -- Admin Order

### Thịnh

-   Admin order service.
-   Update order status.
-   Query/filter.

### Nguyên

-   Admin order list.
-   Order detail.
-   Status update.
-   Search/filter.

------------------------------------------------------------------------

# DAY 11 -- Security + Authorization

### Thịnh

-   Kiểm tra User/Role data.
-   Business authorization rules.
-   Service-level permission rules nếu cần.

### Nguyên

-   SecurityConfig integration.
-   Login/logout.
-   Route authorization.
-   Hide/show UI theo role.

------------------------------------------------------------------------

# DAY 12 -- Validation + Error Handling

### Thịnh

-   Business validation.
-   Exception.
-   Transaction check.
-   Database constraints.
-   Query optimization cơ bản.

### Nguyên

-   Error page.
-   Validation message.
-   API error handling.
-   Empty state.
-   Loading state nếu có AJAX.

------------------------------------------------------------------------

# DAY 13 -- Integration

### Cả hai

-   Merge toàn bộ.
-   Test toàn hệ thống.
-   Fix bug.
-   Fix UI.
-   Fix responsive.
-   Fix database.
-   Kiểm tra security.
-   Kiểm tra API.
-   Kiểm tra quyền.

------------------------------------------------------------------------

# DAY 14 -- FINAL

### Cả hai

-   Freeze feature.
-   Không thêm chức năng lớn.
-   Chỉ fix bug.
-   Demo rehearsal.
-   Kiểm tra Git.
-   Kiểm tra database.
-   Kiểm tra Docker.
-   Kiểm tra README.
-   Kiểm tra báo cáo.

------------------------------------------------------------------------

# 43. DEFINITION OF DONE

Một task chỉ được xem là DONE khi:

-   [ ] Code compile.
-   [ ] Không có lỗi runtime.
-   [ ] Đúng naming convention.
-   [ ] Đúng package.
-   [ ] Đúng architecture.
-   [ ] Không đặt business logic sai tầng.
-   [ ] Validation hoạt động.
-   [ ] Exception được xử lý.
-   [ ] Database hoạt động.
-   [ ] Test chức năng chính.
-   [ ] UI hoạt động nếu task có UI.
-   [ ] API hoạt động nếu task có API.
-   [ ] Không hard-code secret.
-   [ ] Không commit file rác.
-   [ ] Commit message đúng convention.
-   [ ] Pull `develop` trước khi merge.
-   [ ] Không phá chức năng cũ.

------------------------------------------------------------------------

# 44. CHECKLIST CHO THỊNH

Trước khi bàn giao backend:

-   [ ] Entity hoàn chỉnh.
-   [ ] Relationship đúng.
-   [ ] Repository hoạt động.
-   [ ] Service interface rõ.
-   [ ] Service implementation hoàn chỉnh.
-   [ ] DTO rõ.
-   [ ] Mapper nếu cần.
-   [ ] Validation.
-   [ ] Exception.
-   [ ] Transaction.
-   [ ] Pagination.
-   [ ] Search/filter.
-   [ ] Không query thừa.
-   [ ] Không expose Entity không cần thiết.
-   [ ] API contract được ghi rõ cho Nguyên.

------------------------------------------------------------------------

# 45. CHECKLIST CHO NGUYÊN

Trước khi bàn giao frontend/controller:

-   [ ] Controller đúng route.
-   [ ] Không có business logic lớn.
-   [ ] API đúng HTTP method.
-   [ ] HTTP status hợp lý.
-   [ ] Thymeleaf binding đúng.
-   [ ] Form validation.
-   [ ] Fragment dùng lại.
-   [ ] CSS không trùng lặp quá mức.
-   [ ] JS không chứa business rule backend.
-   [ ] Responsive cơ bản.
-   [ ] Security route đúng.
-   [ ] Không hard-code dữ liệu database.

------------------------------------------------------------------------

# 46. CHECKLIST MERGE

Trước khi merge:

``` bash
git checkout develop
git pull origin develop
```

Sau đó cập nhật branch:

``` bash
git checkout feature/your-feature
git merge develop
```

Resolve conflict nếu có.

Sau đó:

``` bash
git add .
git commit -m "fix: resolve merge conflict"
```

Push:

``` bash
git push
```

Sau khi merge:

``` text
feature
   ↓
develop
```

Sau đó xóa feature branch nếu không còn cần:

``` bash
git branch -d feature/...
```

------------------------------------------------------------------------

# 47. NHỮNG THỨ TUYỆT ĐỐI KHÔNG LÀM

## Không commit

``` text
target/
.idea/
*.iml
.env
application-local.properties
password
secret
```

## Không làm

``` text
Controller → Repository
```

## Không làm

``` text
Frontend → Database
```

## Không làm

``` text
Entity → chứa toàn bộ business logic
```

## Không làm

``` text
Service → trả HTML
```

## Không làm

``` text
Controller → tự viết SQL
```

## Không làm

``` text
main → code trực tiếp
```

------------------------------------------------------------------------

# 48. README PROJECT

README nên có:

``` text
# LUNEA

## Introduction

## Features

## Tech Stack

## Architecture

## Requirements

## Installation

## Database Setup

## Docker Setup

## Configuration

## Run Project

## API Documentation

## Git Convention

## Team Members
```

------------------------------------------------------------------------

# 49. TECH STACK THỐNG NHẤT

## Backend

``` text
Java
Spring Boot
Spring MVC
Spring Data JPA
Hibernate
Spring Validation
Spring Security
```

## Frontend

``` text
Thymeleaf
HTML
CSS
JavaScript
Bootstrap
```

## Database

``` text
PostgreSQL
```

## Environment

``` text
Docker
Docker Compose
```

## Tools

``` text
IntelliJ IDEA
Git
GitHub
Postman
pgAdmin / DBeaver nếu cần
```

------------------------------------------------------------------------

# 50. API DOCUMENTATION

Mỗi API quan trọng nên ghi:

``` text
Method:
URL:
Description:
Request:
Response:
Status:
Authentication:
```

Ví dụ:

``` text
GET
/api/products/{id}

Description:
Get product detail

Response:
ProductResponse

Status:
200
404
```

------------------------------------------------------------------------

# 51. CONTRACT GIỮA THỊNH VÀ NGUYÊN

Mỗi module nên có contract dạng:

``` text
MODULE: PRODUCT

Entity:
Product

Service:
ProductService

Methods:
- getById()
- getAll()
- create()
- update()
- delete()
- search()

Request:
ProductRequest

Response:
ProductResponse

API:
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
```

Sau khi contract được thống nhất, hai người có thể làm song song.

------------------------------------------------------------------------

# 52. ƯU TIÊN KHI KHÔNG ĐỦ THỜI GIAN

## P0 -- BẮT BUỘC

``` text
Project chạy
Database
Login/Register
Product
Category
Brand
Product Detail
Cart
Order
Admin Product
Admin Order
Authorization
```

## P1 -- NÊN CÓ

``` text
Search
Filter
Pagination
Customer Order History
Validation
Exception Handling
Responsive
Dashboard cơ bản
```

## P2 -- CÓ THỂ BỔ SUNG

``` text
Voucher
Review
Wishlist
Recommendation
Advanced dashboard
Animation
Advanced filtering
```

Không hy sinh P0 để làm P2.

------------------------------------------------------------------------

# 53. NGUYÊN TẮC QUAN TRỌNG NHẤT

Project phải tuân theo:

``` text
Entity
   ↓
Repository
   ↓
Service
   ↓
Controller / REST API
   ↓
Frontend
```

Trong đó:

### Thịnh

``` text
Entity
Repository
Service
Database
Business Logic
```

### Nguyên

``` text
Controller
REST API
Thymeleaf
HTML
CSS
JavaScript
UI/Security integration
```

### Cả hai

``` text
Git
Testing
Integration
Bug fixing
Documentation
Demo
```

------------------------------------------------------------------------

# 54. SƠ ĐỒ PHÂN CÔNG CUỐI CÙNG

``` text
                         LUNEA
                           │
              ┌────────────┴────────────┐
              │                         │
            THỊNH                     NGUYÊN
              │                         │
       ┌──────┴──────┐          ┌───────┴────────┐
       │             │          │                │
    DATABASE       BACKEND    CONTROLLER       FRONTEND
       │             │          │                │
    PostgreSQL      Entity    MVC Controller   Thymeleaf
    Docker          Repository REST API        HTML
    JPA             Service   Validation       CSS
    Hibernate       DTO       Security         JavaScript
                    Mapper
       │             │          │                │
       └─────────────┴──────────┴────────────────┘
                           │
                        INTEGRATION
                           │
                         develop
                           │
                          main
```

------------------------------------------------------------------------

# 55. QUY TẮC CUỐI CÙNG CHO 2 TUẦN

1.  **Không code trực tiếp trên `main`.**
2.  Mọi feature bắt đầu từ `develop`.
3.  Mỗi feature có branch riêng.
4.  Thịnh phụ trách Entity → Repository → Service.
5.  Nguyên phụ trách Controller → REST API → Frontend.
6.  Business logic nằm trong Service.
7.  Controller không truy cập Repository trực tiếp.
8.  Frontend không truy cập Database.
9.  API dùng DTO.
10. Database dùng PostgreSQL.
11. PostgreSQL chạy Docker có volume.
12. Không commit password/secret.
13. Không tự ý thay đổi API contract của người khác.
14. Không tự ý sửa Entity/Database relationship nếu chưa trao đổi.
15. Mỗi task phải test trước khi merge.
16. Merge vào `develop` trước.
17. Chỉ đưa `develop` lên `main` khi phiên bản ổn định.
18. Ngày 14 đóng feature và tập trung sửa lỗi/demo.
19. Nếu có xung đột giữa tốc độ và kiến trúc, ưu tiên kiến trúc đơn giản
    nhưng đúng tầng.
20. Nếu một chức năng có thể làm đơn giản bằng Thymeleaf thì không bắt
    buộc phải tạo AJAX/REST API chỉ để tăng độ phức tạp.

------------------------------------------------------------------------

# 56. MỤC TIÊU SAU 2 TUẦN

Cuối 2 tuần, project cần đạt:

``` text
                    LUNEA
                      │
        ┌─────────────┴─────────────┐
        │                           │
     CUSTOMER                     ADMIN
        │                           │
   Login/Register             Dashboard
        │                           │
    Products                  Product CRUD
        │                           │
   Product Detail             Order Management
        │                           │
      Cart                    User Management
        │
     Checkout
        │
      Order
        │
  Order History
```

Backend:

``` text
Entity
Repository
Service
DTO
Exception
Validation
Security
Transaction
PostgreSQL
```

Frontend:

``` text
Thymeleaf
Controller
REST API
HTML
CSS
JavaScript
Bootstrap
Responsive UI
```

Git:

``` text
main
  ↑
develop
  ↑
feature/*
```

**Kết quả mong muốn:** một project có thể chạy từ đầu đến cuối, hai
thành viên có thể tiếp tục phát triển mà không phụ thuộc quá chặt vào
nhau, và code đủ rõ để dùng cho demo, báo cáo và các chức năng tiếp
theo.
