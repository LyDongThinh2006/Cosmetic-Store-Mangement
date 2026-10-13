# Actor, RBAC và phạm vi truy cập thực tế

Nguồn quyết định: [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:90](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L90), [src/main/java/com/thinh/cosmetic/security/AppUserDetailsService.java:40](../../../../src/main/java/com/thinh/cosmetic/security/AppUserDetailsService.java#L40), [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19). Chưa đọc dữ liệu tài khoản/role trong database đang chạy.

| Actor | Vai trò | Authority / permission | Phạm vi |
|---|---|---|---|
| Khách vãng lai | Người tra cứu chưa đăng nhập. | Không cần JWT. | Tra cứu công khai; đăng ký/đăng nhập/đặt lại mật khẩu. |
| Khách hàng | AccountType.CUSTOMER, customerId trong JWT. | ROLE_CUSTOMER tự cấp theo account type. | Trang account/cart/checkout; đơn riêng ở đường có ownership. API khách hàng/yêu thích/đổi trả có các ngoại lệ công khai. |
| Nhân viên | AccountType.EMPLOYEE, không bắt buộc có role cụ thể. | ROLE_EMPLOYEE tự cấp; authorities vai trò/permission là snapshot JWT. | Mọi trang admin, kho ở mọi storeId qua API; đơn theo EmployeeStore. |
| Quản trị viên | Nhân viên được gán role ADMIN. | ROLE_EMPLOYEE + ADMIN + ROLE_ADMIN; seeder gán 12 permission. | StoreScope trả null: đơn mọi cửa hàng; không có quyền kế thừa CUSTOMER. |
| Nhân viên xử lý đơn | Nhân viên được gán ORDER_STAFF. | ROLE_EMPLOYEE; seed ORDER_VIEW, ORDER_PROCESS, INVENTORY_VIEW. | Đơn tại cửa hàng được phân công; inventory API không scope. Permission không được dùng làm guard. |
| Nhân viên kho | Nhân viên được gán WAREHOUSE_STAFF. | ROLE_EMPLOYEE; seed INVENTORY_VIEW, INVENTORY_ADJUST. | Vẫn có thể xử lý đơn tại cửa hàng phân công như mọi EMPLOYEE; kho API không scope. |
| Nhân viên quản lý sản phẩm | Nhân viên được gán PRODUCT_MANAGER; role có trong seeder. | ROLE_EMPLOYEE; seed CATALOG_MANAGE, INVENTORY_VIEW. | Có quyền EMPLOYEE thực tế; catalog mutation public. Không tạo độc quyền quản lý sản phẩm từ permission. |
| Người gọi API công khai | Bất kỳ client HTTP, kể cả chưa đăng nhập; actor giao tiếp API. | permitAll; mutation vẫn cần X-Requested-With: fetch/XMLHttpRequest nếu không có Bearer header. | API public thực tế về khách/nhân viên/catalog/cửa hàng/nhập/chuyển/đổi trả/review. Đây là truy cập hiện hành do thiếu guard, không phải yêu cầu nghiệp vụ mới. |

## Luật URL và kiểm tra business logic

| Nhóm | Guard URL | Ownership / StoreScope |
|---|---|---|
| /admin/** và /api/admin/** | ROLE_EMPLOYEE | Order service lọc EmployeeStore; ADMIN/ROLE_ADMIN/ALL_STORES đi toàn chuỗi. |
| /api/inventory/** | ROLE_EMPLOYEE | Không kiểm StoreScope; mọi employee có thể truyền storeId bất kỳ. UI lọc menu cửa hàng không thay thế guard API. |
| /cart, /checkout, /order-success, /account/**, /api/cart/** | ROLE_CUSTOMER | Cart sửa/xóa item thiếu ownership; success page thiếu ownership mã đơn. |
| /api/orders/my-orders, /preview, /{id}/cancel | ROLE_CUSTOMER | CustomerId ưu tiên principal, có fallback; cancel chỉ PENDING và kiểm chủ đơn. |
| /api/orders/{id}/status | ROLE_EMPLOYEE | UpdateStatus kiểm scope nếu đơn có assignedStore. Không kiểm ORDER_PROCESS. |
| /api/orders và các path còn lại | authenticated() | POST tạo đơn cả customer/employee; customer/{id} chưa ownership; code/{code} không scope cho employee. |
| /api/account/** | authenticated() | Không có Account REST mapping nghiệp vụ ở namespace này. |
| /api/auth/** | permitAll | /me tự trả 401 nếu không principal. Reset không OTP/token sở hữu. |
| /api/customers, /employees, /products, /brands, /categories, /stores, /suppliers, /stock-transfers, /purchase-orders, /wishlist, /returns, /reviews | permitAll | Không @PreAuthorize; các operation mutation cũng công khai. |

`XRequestedWithFilter` yêu cầu header `X-Requested-With: fetch/XMLHttpRequest` với mutation `/api/**` không dùng Bearer header. Header này là điều kiện request, không là xác thực hay quyền RBAC.

## Quan hệ actor được xác minh

ADMIN, ORDER_STAFF, WAREHOUSE_STAFF và PRODUCT_MANAGER chuyên biệt hóa **Nhân viên**. AppUserDetailsService luôn cấp ROLE_EMPLOYEE cho AccountType.EMPLOYEE rồi mới thêm role/permission; EmployeeService tạo account EMPLOYEE trước khi gán roleIds. Đó là bằng chứng subtype, không chỉ hai vai trò có quyền giống nhau.

Không có RoleHierarchy; không nối ADMIN kế thừa CUSTOMER hoặc các role nhân viên khác. ADMIN được đi mọi store chỉ bởi StoreScope. Các permission chưa làm guard, nên warehouse/product manager cũng gọi được API xử lý đơn nếu có ROLE_EMPLOYEE và đúng store scope.

Role CUSTOMER trong seeder không tạo actor khách hàng thứ hai: ROLE_CUSTOMER được cấp theo AccountType, không cần EmployeeRole. ALL_STORES là authority đặc biệt được code nhận biết, chưa seed role/permission này; không tạo actor độc lập.

Quyền là snapshot JWT; đổi phân công vai trò không thu hồi token đã phát. Employee.status INACTIVE không được AppUserDetailsService kiểm; deactivate không khóa Account.status.

## Actor bên ngoài không đưa vào mô hình

Không có triển khai gọi cổng thanh toán, hãng vận chuyển, Google Maps API, Cloudinary upload hay Email/OTP. COD và cập nhật trạng thái được thực hiện nội bộ. Cloudinary/MSSQL dependency, ảnh remote và CDN trình bày không chứng minh một supporting actor nghiệp vụ. Nhà cung cấp chỉ là bản ghi do client quản lý, không có portal/API dành riêng cho nhà cung cấp.

Nhật ký quyền cụ thể theo UC: [use-case-matrix.csv](../traceability/use-case-matrix.csv).
