# Use Case LUNEA — bộ hình để vẽ theo

**14 sơ đồ PNG: 1 tổng quan + 13 nhóm nghiệp vụ. Mỗi sơ đồ chỉ có một file hình.** Đã bỏ các bản PlantUML/SVG và các bộ hình theo từng actor -01/-02/-03.

[Mở sơ đồ tổng quan](overview/overall-system.png) · [Đặc tả 46 Use Case](specifications/use-case-specification.md) · [Bảng truy vết](traceability/use-case-matrix.md) · [Đối chiếu PDF](specifications/pdf-review.md)

## Các hình cần vẽ

| Nhóm | Một file hình |
|---|---|
| Tổng quan hệ thống | [overall-system.png](overview/overall-system.png) |
| Tài khoản và xác thực | [authentication.png](modules/authentication.png) |
| Hồ sơ khách hàng | [customers.png](modules/customers.png) |
| Quản lý nhân viên | [employees.png](modules/employees.png) |
| Sản phẩm và tra cứu mỹ phẩm | [catalog.png](modules/catalog.png) |
| Thương hiệu và danh mục | [brand_category.png](modules/brand_category.png) |
| Cửa hàng | [store.png](modules/store.png) |
| Tồn kho | [inventory.png](modules/inventory.png) |
| Điều chuyển kho | [stock_transfer.png](modules/stock_transfer.png) |
| Nhà cung cấp và nhập hàng | [purchase.png](modules/purchase.png) |
| Giỏ hàng và yêu thích | [cart.png](modules/cart.png) |
| Đặt hàng và vận hành đơn | [order.png](modules/order.png) |
| Yêu cầu trả hàng | [returns.png](modules/returns.png) |
| Đánh giá sản phẩm | [review.png](modules/review.png) |

Chỉ cần mở các PNG trong bảng để vẽ theo trong Enterprise Architect. Hình tổng quan chọn các chức năng chính; hình nghiệp vụ chứa các mục tiêu chi tiết. Không cần vẽ lại từng role một bộ chức năng giống nhau.

## Các chỉnh sửa theo PDF môn học

- Gộp **83 hành vi nghiệp vụ** đã đọc từ code thành **46 mục tiêu Use Case**; thêm/sửa/xóa hoặc chuyển trạng thái cùng mục tiêu là các luồng lựa chọn. Ví dụ: Quản lý thương hiệu; Quản lý địa chỉ nhận hàng; Cập nhật tiến trình đơn hàng.
- Đổi theme được giữ trong nhật ký source như tiện ích giao diện, ngoài bộ sơ đồ nghiệp vụ. Không thêm chức năng mới.
- Actor là vai trò người tương tác; các chi tiết endpoint, JWT, repository và lỗi bảo vệ API nằm trong đặc tả/bảng truy vết.
- Actor đứng ngoài khung hệ thống, use case là ellipse bên trong. Association là đường liền. Generalization có tam giác rỗng trỏ từ role nhân viên chuyên biệt về Nhân viên.
- Có **2 extend**: Áp dụng mã ưu đãi → Xem tổng tiền trước khi đặt; Áp dụng mã ưu đãi → Đặt hàng thanh toán khi nhận. Điều kiện: một trong ba mã đang được code hỗ trợ; điểm mở rộng: Tính tổng cuối.
- Không nối mọi chức năng với Đăng nhập. Không dùng include cho toàn bộ CRUD hoặc nối các trạng thái thành luồng thời gian trên use-case diagram.

Căn cứ chính: [SDE02b — UML Use Case, trang 6–10, 16–23, 26–27, 31–37](<../SDE02b - UML Use Case.pdf#page=6>), [UML tiếng Việt, trang 8–12](<../UML tieng Viet.pdf#page=8>), [V02 — Xác định yêu cầu, trang 38–43](<../V02 - Xac dinh yeu cau.pdf#page=38>). Nội dung nguồn được giữ trong [bảng ánh xạ trước/sau khi gộp](traceability/raw-behavior-map.csv). ID UC được đánh lại đồng bộ với bộ mới.

## Actor và quyền

| Actor | Ý nghĩa |
|---|---|
| Khách vãng lai | Người tra cứu chưa đăng nhập. |
| Khách hàng | AccountType.CUSTOMER, customerId trong JWT. |
| Nhân viên | AccountType.EMPLOYEE, không bắt buộc có role cụ thể. |
| Quản trị viên | Nhân viên được gán role ADMIN. |
| Nhân viên xử lý đơn | Nhân viên được gán ORDER_STAFF. |
| Nhân viên kho | Nhân viên được gán WAREHOUSE_STAFF. |
| Nhân viên quản lý sản phẩm | Nhân viên được gán PRODUCT_MANAGER; role có trong seeder. |

Các association trong hình thể hiện **vai trò thực hiện nghiệp vụ**, không khẳng định vai trò đó có quyền độc quyền hoặc mọi API đã chặn đúng RBAC. Ví dụ quản trị viên và nhân viên quản lý sản phẩm tham gia quản lý danh mục; nhân viên thao tác nhập/chuyển/tồn; khách hàng quản lý hồ sơ/giỏ/đơn của mình. Các vai trò này có bằng chứng account type, role, permission mô tả và dữ liệu nghiệp vụ trong source.

**Code hiện tại vẫn có nhiều API permitAll**, chưa guard theo permission, và inventory API chưa kiểm store scope. Các truy cập này không được biến thành chức năng nghiệp vụ hoặc một actor Người gọi API công khai trên hình. Bảng [Security/RBAC](specifications/security-and-actors.md) và [khoảng trống triển khai](specifications/implementation-gaps.md) giữ nguyên bằng chứng về các lỗi này. Không sửa quyền hoặc source ứng dụng.

Màu hình: vàng = có luồng tích hợp; xanh = chỉ backend; cam = còn lỗi/thiếu nghiệp vụ. Sơ đồ có chức năng màu cam/xanh để phản ánh source hiện tại; không khẳng định chức năng đã chạy hoàn chỉnh.

## Tài liệu đối chiếu

[Đặc tả](specifications/use-case-specification.md) · [Traceability CSV](traceability/use-case-matrix.csv) · [API inventory](traceability/endpoint-inventory.md) · [Entity và quan hệ](specifications/entity-inventory.md) · [Kiến trúc/rules](specifications/architecture-and-business-rules.md) · [Kiểm tra UML](specifications/uml-relationship-audit.md) · [Kết quả kiểm tra](traceability/validation-report.md)

Frontend thực tế là Spring MVC/Thymeleaf + JavaScript; backend Spring Boot/JPA/Security, PostgreSQL theo profile. Khảo sát 277 tệp nguồn/config vẫn được giữ; hash source không đổi. Các lỗi đăng ký, redirect đăng nhập, wishlist/reset header, giỏ hàng, return/customer và tồn kho được ghi ở đặc tả, không sửa trong lần chỉnh tài liệu này.

## Nếu cần tạo lại hình

Bộ bàn giao chỉ giữ PNG; source dựng hình nằm chung trong một script, không có file diagram trùng định dạng. Chạy từ repository root:

```bash
python3 docs/uml/use-case/traceability/rebuild.py --jar /path/to/plantuml.jar --cleanup
```

Script dựng/check PlantUML trong thư mục tạm, xuất PNG vào docs và kiểm tra coverage/links. Không chạy ứng dụng, database, seed hoặc migration. Không xuất XMI vì chưa kiểm chứng import EA. PNG là hình tham khảo để bạn tự tạo Actor/UseCase/Connector native trong EA.
