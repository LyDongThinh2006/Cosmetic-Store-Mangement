# UML Use Case — Cosmetic Store Chain Management (LUNEA)

**Khảo sát source ngày 13/10/2026.** Bộ tài liệu phản ánh implementation tại commit nền `2e59a631da26d3055bb64a5c0583e33cf9ae3eee`; hashes từng tệp nằm trong source-inventory.csv. Không sửa source ứng dụng hoặc database.

**84 use case · 8 actor · 37 entity · 42 sơ đồ** (1 tổng quan, 24 phần theo actor, 17 phần theo module). Mỗi sơ đồ có `.puml`, `.svg`, `.png`; những actor có nhiều chức năng được chia phần để đọc rõ.

[Xem tổng quan SVG](overview/overall-system.svg) · [Đặc tả 84 UC](specifications/use-case-specification.md) · [Traceability Markdown](traceability/use-case-matrix.md) · [Traceability CSV](traceability/use-case-matrix.csv) · [Kết quả kiểm tra](traceability/validation-report.md)

## 1. Hệ thống và kiến trúc đã kiểm chứng

Spring Boot **4.1.1**, Java target **21**, Spring MVC/REST, Spring Data JPA/Hibernate, Spring Security JWT HS256, BCrypt, MapStruct/Lombok. Frontend là **Thymeleaf SSR + HTML/CSS + JavaScript Fetch**, không có React. Profile local/prod dùng PostgreSQL; Docker Compose khai báo PostgreSQL 16; test dùng H2. Cloudinary và MSSQL có dependency nhưng chưa có tích hợp nghiệp vụ được chứng minh.

Luồng UI → page controller/model hoặc REST → service → repository/JPA. Source có 37 entity; việc có entity/enum/permission không đồng nghĩa chức năng đã hoàn thành. Bộ khảo sát đọc đủ 277 tệp nguồn/config trong src; không còn tệp thiếu trong ledger. Binary, generated/build/cache/dependencies không thuộc phạm vi đọc nghiệp vụ.

[Kiến trúc, rule và queries](specifications/architecture-and-business-rules.md) · [Toàn bộ entity/quan hệ](specifications/entity-inventory.md) · [95 method/path API và coverage](traceability/endpoint-inventory.md) · [Source ledger/hashes](traceability/source-inventory.csv)

## 2. Actor và quyền thực tế

| Actor | Quyền / phạm vi đã kiểm chứng |
|---|---|
| Khách vãng lai | Không cần JWT. Tra cứu công khai; đăng ký/đăng nhập/đặt lại mật khẩu. |
| Khách hàng | ROLE_CUSTOMER tự cấp theo account type. Trang account/cart/checkout; đơn riêng ở đường có ownership. API khách hàng/yêu thích/đổi trả có các ngoại lệ công khai. |
| Nhân viên | ROLE_EMPLOYEE tự cấp; authorities vai trò/permission là snapshot JWT. Mọi trang admin, kho ở mọi storeId qua API; đơn theo EmployeeStore. |
| Quản trị viên | ROLE_EMPLOYEE + ADMIN + ROLE_ADMIN; seeder gán 12 permission. StoreScope trả null: đơn mọi cửa hàng; không có quyền kế thừa CUSTOMER. |
| Nhân viên xử lý đơn | ROLE_EMPLOYEE; seed ORDER_VIEW, ORDER_PROCESS, INVENTORY_VIEW. Đơn tại cửa hàng được phân công; inventory API không scope. Permission không được dùng làm guard. |
| Nhân viên kho | ROLE_EMPLOYEE; seed INVENTORY_VIEW, INVENTORY_ADJUST. Vẫn có thể xử lý đơn tại cửa hàng phân công như mọi EMPLOYEE; kho API không scope. |
| Nhân viên quản lý sản phẩm | ROLE_EMPLOYEE; seed CATALOG_MANAGE, INVENTORY_VIEW. Có quyền EMPLOYEE thực tế; catalog mutation public. Không tạo độc quyền quản lý sản phẩm từ permission. |
| Người gọi API công khai | permitAll; mutation vẫn cần X-Requested-With: fetch/XMLHttpRequest nếu không có Bearer header. API public thực tế về khách/nhân viên/catalog/cửa hàng/nhập/chuyển/đổi trả/review. Đây là truy cập hiện hành do thiếu guard, không phải yêu cầu nghiệp vụ mới. |

Bốn role nhân viên chuyên biệt hóa **Nhân viên** vì thuộc account EMPLOYEE và luôn nhận ROLE_EMPLOYEE. Không kế thừa ADMIN từ CUSTOMER hoặc từ role nhân viên khác. Không có actor Chain/Branch Manager, Sales Staff, Marketing hoặc hệ thống ngoài nếu source chưa chứng minh.

Các permission CATALOG_MANAGE, ORDER_VIEW/PROCESS, INVENTORY_VIEW/ADJUST, STORE_MANAGE, EMPLOYEE_MANAGE, VOUCHER_MANAGE, PURCHASE_MANAGE, CUSTOMER_MANAGE, REPORT_VIEW, AUDIT_VIEW được seed/nạp JWT nhưng chưa dùng làm guard nghiệp vụ. Order service có StoreScope; inventory chỉ kiểm ROLE_EMPLOYEE, không scope. API quản lý khách/nhân viên/catalog/cửa hàng/nhập/chuyển/returns/reviews/wishlist rơi vào permitAll.

**Người gọi API công khai** là actor client của các API này, phản ánh thiếu guard hiện tại; không phải yêu cầu trao quyền quản trị cho khách. Các association không khẳng định actor có quyền độc quyền. [Phân tích Security/RBAC đầy đủ](specifications/security-and-actors.md).

## 3. Module và Implementation Status

| Module | Số UC |
|---|---|
| Xác thực và phiên đăng nhập | 4 |
| Hồ sơ khách hàng và địa chỉ | 10 |
| Nhân viên và phân công | 4 |
| Sản phẩm và tra cứu mỹ phẩm | 6 |
| Thương hiệu và danh mục | 8 |
| Cửa hàng | 4 |
| Tồn kho | 4 |
| Điều chuyển kho | 4 |
| Nhà cung cấp và nhập hàng | 7 |
| Giỏ hàng | 5 |
| Yêu thích | 3 |
| Đặt hàng và xử lý đơn | 13 |
| Yêu cầu trả hàng | 6 |
| Đánh giá sản phẩm | 4 |
| Tổng quan vận hành | 1 |
| Tùy chọn giao diện | 1 |

| Status | Số UC | Ý nghĩa trong bộ tài liệu |
|---|---|---|
| Implemented | 25 | Có implementation và đường UI/API tương ứng được đối chiếu; không khẳng định nghiệm thu runtime. |
| Partially Implemented | 29 | Có code nhưng còn lỗi flow, rule hoặc tích hợp; xem alternative/notes từng UC. |
| Backend Only | 29 | Có service/controller/query thực hiện, chưa tích hợp giao diện. |
| UI Only | 1 | Đổi theme chạy hoàn chỉnh trên client/localStorage; không cần backend. Placeholder khác chỉ liệt kê ở báo cáo gaps. |
| Not Verified | 0 | Không gán cho feature chỉ có trong thiết kế; feature không có implementation không được tạo UC giả. |

Trạng thái là một chiều tổng hợp; cột UI/backend_assessment trong CSV cho biết chi tiết. Một backend chưa hoàn thiện và chưa có UI vẫn mang Partially Implemented, với UI=Chưa tích hợp. [Mọi UC và nguồn](traceability/use-cases.json).

## 4. Danh sách sơ đồ

Mọi actor ở ngoài rectangle tên hệ thống, use case là ellipse bên trong, association là đường liền. Generalization đi từ subtype tới Nhân viên, tam giác rỗng ở parent. Có 4 quan hệ actor generalization; **0 include/0 extend**, vì chưa có chứng cứ hành vi tái sử dụng hoặc extension point độc lập đủ rõ. Đăng nhập là precondition. [Relationship audit](specifications/uml-relationship-audit.md).

Sơ đồ tổng quan dùng mục tiêu tổng hợp, không nhồi 84 UC vào một hình; [overview-map.md](overview/overview-map.md) ánh xạ mỗi ellipse về các UC chi tiết. Màu tổng quan không biểu thị trạng thái. Module/actor diagrams dùng màu theo legend.

| Nhóm | Sơ đồ | PlantUML | SVG | PNG |
|---|---|---|---|---|
| overview | Tổng quan hệ thống | [source](overview/overall-system.puml) | [vector](overview/overall-system.svg) | [image](overview/overall-system.png) |
| modules | Xác thực và phiên đăng nhập | [source](modules/authentication.puml) | [vector](modules/authentication.svg) | [image](modules/authentication.png) |
| modules | Hồ sơ khách hàng và địa chỉ | [source](modules/customers.puml) | [vector](modules/customers.svg) | [image](modules/customers.png) |
| modules | Nhân viên và phân công | [source](modules/employees.puml) | [vector](modules/employees.svg) | [image](modules/employees.png) |
| modules | Sản phẩm và tra cứu mỹ phẩm | [source](modules/catalog.puml) | [vector](modules/catalog.svg) | [image](modules/catalog.png) |
| modules | Thương hiệu và danh mục | [source](modules/brand_category.puml) | [vector](modules/brand_category.svg) | [image](modules/brand_category.png) |
| modules | Cửa hàng | [source](modules/store.puml) | [vector](modules/store.svg) | [image](modules/store.png) |
| modules | Tồn kho | [source](modules/inventory.puml) | [vector](modules/inventory.svg) | [image](modules/inventory.png) |
| modules | Điều chuyển kho | [source](modules/stock_transfer.puml) | [vector](modules/stock_transfer.svg) | [image](modules/stock_transfer.png) |
| modules | Nhà cung cấp và nhập hàng | [source](modules/purchase.puml) | [vector](modules/purchase.svg) | [image](modules/purchase.png) |
| modules | Giỏ hàng | [source](modules/cart.puml) | [vector](modules/cart.svg) | [image](modules/cart.png) |
| modules | Yêu thích | [source](modules/wishlist.puml) | [vector](modules/wishlist.svg) | [image](modules/wishlist.png) |
| modules | Đặt hàng và xử lý đơn — phần 1 | [source](modules/order-1.puml) | [vector](modules/order-1.svg) | [image](modules/order-1.png) |
| modules | Đặt hàng và xử lý đơn — phần 2 | [source](modules/order-2.puml) | [vector](modules/order-2.svg) | [image](modules/order-2.png) |
| modules | Yêu cầu trả hàng | [source](modules/returns.puml) | [vector](modules/returns.svg) | [image](modules/returns.png) |
| modules | Đánh giá sản phẩm | [source](modules/review.puml) | [vector](modules/review.svg) | [image](modules/review.png) |
| modules | Tổng quan vận hành | [source](modules/dashboard.puml) | [vector](modules/dashboard.svg) | [image](modules/dashboard.png) |
| modules | Tùy chọn giao diện | [source](modules/ui_preferences.puml) | [vector](modules/ui_preferences.svg) | [image](modules/ui_preferences.png) |
| actors | Khách vãng lai | [source](actors/guest.puml) | [vector](actors/guest.svg) | [image](actors/guest.png) |
| actors | Khách hàng — phần 1 | [source](actors/customer-01.puml) | [vector](actors/customer-01.svg) | [image](actors/customer-01.png) |
| actors | Khách hàng — phần 2 | [source](actors/customer-02.puml) | [vector](actors/customer-02.svg) | [image](actors/customer-02.png) |
| actors | Khách hàng — phần 3 | [source](actors/customer-03.puml) | [vector](actors/customer-03.svg) | [image](actors/customer-03.png) |
| actors | Nhân viên — phần 1 | [source](actors/employee-01.puml) | [vector](actors/employee-01.svg) | [image](actors/employee-01.png) |
| actors | Nhân viên — phần 2 | [source](actors/employee-02.puml) | [vector](actors/employee-02.svg) | [image](actors/employee-02.png) |
| actors | Nhân viên — phần 3 | [source](actors/employee-03.puml) | [vector](actors/employee-03.svg) | [image](actors/employee-03.png) |
| actors | Quản trị viên — phần 1 | [source](actors/admin-01.puml) | [vector](actors/admin-01.svg) | [image](actors/admin-01.png) |
| actors | Quản trị viên — phần 2 | [source](actors/admin-02.puml) | [vector](actors/admin-02.svg) | [image](actors/admin-02.png) |
| actors | Quản trị viên — phần 3 | [source](actors/admin-03.puml) | [vector](actors/admin-03.svg) | [image](actors/admin-03.png) |
| actors | Nhân viên xử lý đơn — phần 1 | [source](actors/order_staff-01.puml) | [vector](actors/order_staff-01.svg) | [image](actors/order_staff-01.png) |
| actors | Nhân viên xử lý đơn — phần 2 | [source](actors/order_staff-02.puml) | [vector](actors/order_staff-02.svg) | [image](actors/order_staff-02.png) |
| actors | Nhân viên xử lý đơn — phần 3 | [source](actors/order_staff-03.puml) | [vector](actors/order_staff-03.svg) | [image](actors/order_staff-03.png) |
| actors | Nhân viên kho — phần 1 | [source](actors/warehouse_staff-01.puml) | [vector](actors/warehouse_staff-01.svg) | [image](actors/warehouse_staff-01.png) |
| actors | Nhân viên kho — phần 2 | [source](actors/warehouse_staff-02.puml) | [vector](actors/warehouse_staff-02.svg) | [image](actors/warehouse_staff-02.png) |
| actors | Nhân viên kho — phần 3 | [source](actors/warehouse_staff-03.puml) | [vector](actors/warehouse_staff-03.svg) | [image](actors/warehouse_staff-03.png) |
| actors | Nhân viên quản lý sản phẩm — phần 1 | [source](actors/product_manager-01.puml) | [vector](actors/product_manager-01.svg) | [image](actors/product_manager-01.png) |
| actors | Nhân viên quản lý sản phẩm — phần 2 | [source](actors/product_manager-02.puml) | [vector](actors/product_manager-02.svg) | [image](actors/product_manager-02.png) |
| actors | Nhân viên quản lý sản phẩm — phần 3 | [source](actors/product_manager-03.puml) | [vector](actors/product_manager-03.svg) | [image](actors/product_manager-03.png) |
| actors | Người gọi API công khai — phần 1 | [source](actors/public-01.puml) | [vector](actors/public-01.svg) | [image](actors/public-01.png) |
| actors | Người gọi API công khai — phần 2 | [source](actors/public-02.puml) | [vector](actors/public-02.svg) | [image](actors/public-02.png) |
| actors | Người gọi API công khai — phần 3 | [source](actors/public-03.puml) | [vector](actors/public-03.svg) | [image](actors/public-03.png) |
| actors | Người gọi API công khai — phần 4 | [source](actors/public-04.puml) | [vector](actors/public-04.svg) | [image](actors/public-04.png) |
| actors | Người gọi API công khai — phần 5 | [source](actors/public-05.puml) | [vector](actors/public-05.svg) | [image](actors/public-05.png) |

Các phần theo actor bao phủ mọi UC có association của actor trong mô hình. Role chuyên biệt hiển thị association ở Nhân viên để thể hiện kế thừa thật. Phạm vi chức năng public cũng có thể được mọi actor gọi; actor API công khai gom truy cập này, không tạo hierarchy giả với Guest.

## 5. Render PlantUML

Đã render local bằng **PlantUML 1.2026.8**, Java headless và **Smetana**; mỗi source tự khai báo `!pragma layout smetana`, nên không cần executable Graphviz. PNG/SVG giữ cạnh source `.puml`. [Smetana chính thức](https://plantuml.com/smetana02), [CLI chính thức](https://plantuml.com/command-line).

Tải JAR từ [PlantUML Downloads](https://plantuml.com/download), rồi chạy từ repository root:

```bash
python3 docs/uml/use-case/traceability/render.py /path/to/plantuml.jar
```

Hoặc render một hình:

```bash
java -Djava.awt.headless=true -jar /path/to/plantuml.jar --check-syntax docs/uml/use-case/overview/overall-system.puml
java -Djava.awt.headless=true -jar /path/to/plantuml.jar --format svg docs/uml/use-case/overview/overall-system.puml
java -Djava.awt.headless=true -jar /path/to/plantuml.jar --format png docs/uml/use-case/overview/overall-system.puml
```

Có thể chỉnh trực tiếp `.puml` và render lại. Các script build_catalog/build_diagrams dùng JSON evidence để tái tạo đặc tả/sơ đồ; chạy chúng sẽ ghi đè tài liệu sinh tự động, nên cập nhật evidence khi source đổi. Chúng chỉ ghi trong docs/uml/use-case; không chạy app/database.

## 6. Sử dụng trong Enterprise Architect

Để đưa **ảnh** vào EA: kéo file PNG vào diagram và chọn Image Asset, hoặc dùng Image Manager. Ảnh phục vụ xem/tài liệu; không trở thành Actor/UseCase/Connector có thể chỉnh từng phần. [Sparx: Image Assets](https://sparxsystems.com/enterprise_architect_user_guide/17.2/modeling_fundamentals/image_assets.html), [Sparx: Image Manager](https://www.sparxsystems.com/enterprise_architect_user_guide/17.2/modeling_fundamentals/bitmapimagesindiagrams.html).

Để tạo **model native chỉnh sửa trong EA**: tạo package Use Cases và Use Case diagram, tạo Actor/UseCase theo ID/tên trong use-cases.json/CSV, thêm System Boundary, association và 4 generalization theo relationship audit. Chép các precondition/scenario/alternative/postcondition vào phần specification/notes; reuse một UC element cùng ID giữa các diagram. `.puml` là source chỉnh sửa trong PlantUML, không được tuyên bố import trực tiếp thành native model EA.

**Không xuất XMI:** môi trường không có EA để kiểm tra import và diagram layout. PlantUML CLI mô tả XMI cho class diagrams; chưa có cơ sở xuất use-case XMI tương thích EA. EA hỗ trợ import các định dạng XMI/native XML phù hợp nhưng điều đó không xác minh một file tự sinh. [Sparx: Import from XMI](https://sparxsystems.com/enterprise_architect_user_guide/17.2/model_exchange/importxmi.html), [PlantUML CLI](https://plantuml.com/command-line).

## 7. Giới hạn và giả định

Đây là phân tích tĩnh toàn source ứng dụng cùng test/config để đối chứng. Không khởi động DB, không seed/migrate và không chạy API mutation hoặc Maven/test suite; không tuyên bố test nghiệp vụ đã pass. Config local/prod và seed role là bằng chứng source, không xác nhận dữ liệu database deployment. Test dùng mock không chứng minh constraint persistence thực.

Tài liệu kiến trúc/README cũ là thiết kế tham khảo; source có độ ưu tiên cao hơn. Không suy diễn chức năng từ dependency Cloudinary, permission REPORT_VIEW/VOUCHER_MANAGE/AUDIT_VIEW hay enum pickup/receipt. XMI chưa tạo/chưa kiểm chứng. Source hashes ghi lại snapshot; source đổi cần rà soát lại đặc tả.

## 8. Chức năng chưa hoàn thiện

Các điểm có tác động trực tiếp: đăng ký payload sai; đăng nhập employee redirect sai; wishlist/reset thiếu header; cart DTO/quantity UI sai; tạo return thiếu customer; default address không duy nhất; deactivate employee không khóa account; nhiều API thiếu guard/ownership; đặt hàng/ship transfer chưa bảo đảm stock khả dụng; trạng thái đơn cho nhảy/lùi.

Chưa có UI cho nhiều CRUD, BeautyProfile, nhập/chuyển kho và review. Chưa có CRUD SKU/ảnh/thuộc tính, quản trị voucher, audit, advanced reporting, pickup, reassign store, online payment/email/OTP/courier/maps. Các mục voucher cá nhân, notification, settings, blog, consultation và review testimonial chỉ là placeholder/static/link. [Danh sách đầy đủ và chứng cứ](specifications/implementation-gaps.md).

## Cấu trúc bàn giao

```text
docs/uml/use-case/
  overview/         tổng quan + mapping nhóm UC
  actors/           24 phần theo actor, mỗi phần .puml/.svg/.png
  modules/          17 phần theo module, mỗi phần .puml/.svg/.png
  specifications/   đặc tả UC, entity, security, rules, gaps, UML audit
  traceability/     matrix Markdown/CSV, JSON model, ledger, API inventory, validation, scripts
  README.md
```
