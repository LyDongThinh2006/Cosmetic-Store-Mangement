# Kiểm tra quan hệ UML 2.x

| Quan hệ | Kết quả | Bằng chứng / quyết định |
|---|---|---|
| Association | Đường liền không mũi tên giữa actor và UC trực tiếp. | use-cases.json ghi actors/primary_actors/api_access_actors. API public không được gán độc quyền ADMIN. |
| Include | 0 connector. | Không tách việc đọc repository, map DTO, giữ tồn hay tính giá thành UC kỹ thuật. Preview không bắt buộc trước đặt hàng. Login là precondition. |
| Extend | 0 connector. | Voucher có nhánh điều kiện trong preview/place và UI gọi preview; không tuyên bố extension point chung độc lập chưa được xác minh. CRUD, click navigation, trạng thái tuần tự không tự tạo extend. |
| Actor generalization | 4 quan hệ chuyên biệt → Nhân viên. | Role nằm trên account EMPLOYEE; AppUserDetailsService tự cấp ROLE_EMPLOYEE. Dùng --|> từ subtype tới parent, tam giác rỗng ở parent. |
| Use-case generalization | 0 connector. | Các mục tiêu chưa có hợp đồng chuyên biệt hóa đủ chứng cứ; không dùng để gom CRUD. |

Không nối mọi UC tới Đăng nhập; không kế thừa ADMIN từ Customer/warehouse/order/product. Tất cả actor nằm ngoài rectangle system, ellipse nằm trong. Màu chỉ là trạng thái phân tích, không phải UML stereotype thay thế notation.

Các sơ đồ actor chuyên biệt thể hiện association trên actor Nhân viên rồi nối subtype --|> Nhân viên; hành vi kế thừa được trình bày trong từng phần.
