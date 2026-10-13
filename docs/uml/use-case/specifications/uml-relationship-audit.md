# Kiểm tra quan hệ UML sau khi đối chiếu PDF

| Quan hệ | Sử dụng | Bằng chứng / hướng |
|---|---|---|
| Association | Actor nghiệp vụ — mục tiêu trực tiếp | Đường liền không mũi tên. Vai trò không đồng nghĩa guard độc quyền; security-and-actors.md ghi quyền thật. |
| Include | 0 | CRUD là nhánh lựa chọn; đăng nhập là điều kiện trước. Không tạo ellipse repository/map DTO/tính giá để có include. |
| Extend | UC033 → UC032; UC033 → UC034 | Áp mã khi có LUNEA10/GIAM50K/FREESHIP tại extension point Tính tổng cuối. OrderServiceImpl.previewOrder/placeOrder có nhánh điều kiện cùng rule; checkout.applyVoucher gửi mã. |
| Actor generalization | ADMIN/ORDER_STAFF/WAREHOUSE_STAFF/PRODUCT_MANAGER → EMPLOYEE | AppUserDetailsService luôn cấp ROLE_EMPLOYEE cho AccountType.EMPLOYEE rồi thêm role. Đường liền, tam giác rỗng tại Nhân viên. |
| Use-case generalization | 0 | Các nhánh quản lý dữ liệu nằm trong scenario; không có hợp đồng chuyên biệt hóa đủ chứng cứ. |

Hướng extend tuân theo [SDE02b trang 27](<../../SDE02b - UML Use Case.pdf#page=27>): use case bổ sung → use case cơ sở. Áp mã là tùy chọn và use case cơ sở vẫn tính được tổng không có mã. Không nối tiến trình trạng thái như activity hoặc mọi chức năng tới Đăng nhập.

Actor ở ngoài khung, mọi ellipse ở trong System Boundary có tên hệ thống. Sơ đồ tổng quan chọn các UC thật của catalogue; không tạo UC tổng hợp khác chưa được đặc tả.
