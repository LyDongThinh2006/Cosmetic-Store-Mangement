# Đối chiếu PDF trong docs/uml

Đã trích xuất văn bản của 13 PDF (536 trang), khảo sát các chương liên quan và đọc kỹ phần Use Case cùng hình minh họa actor/include/extend. Các PDF là tài liệu phương pháp, không chứa yêu cầu riêng cho hệ thống mỹ phẩm. Code và nhật ký khảo sát là nguồn xác nhận chức năng.

| PDF | Trang | Nội dung áp dụng |
|---|---|---|
| [SDE02a - UML Fundamentals.pdf](<../../SDE02a - UML Fundamentals.pdf>) | 36 | Trang 6, 19, 25–26: góc nhìn người dùng; một sơ đồ chỉ cần biểu diễn phần liên quan của mô hình. |
| [SDE02b - UML Use Case.pdf](<../../SDE02b - UML Use Case.pdf>) | 50 | Trang 6–10, 16–23: actor/vai trò, mục tiêu trọn vẹn, luồng chính/ngoại lệ. Trang 26–27: include/extend. Trang 31–37: một use case có các nhánh CREATE/REVIEW/MODIFY/DELETE. |
| [SDE02d - UML Interaction Diagram.pdf](<../../SDE02d - UML Interaction Diagram.pdf>) | 52 | Trang 4–5, 15–18: sequence/collaboration mô tả tương tác và hiện thực UC; không đưa controller/service/repository thành actor trên use-case diagram. |
| [SDE02e - UML Activity Diagram.pdf](<../../SDE02e - UML Activity Diagram.pdf>) | 24 | Trang 3–9, 20–21: chuỗi hành động, decision/swimlane thuộc activity; các trạng thái thao tác không tự tạo include/extend. |
| [UML tieng Viet.pdf](<../../UML tieng Viet.pdf>) | 104 | Chương Use Case trang 8–12: actor, ellipse, boundary, association, đặc tả, generalization/include/extend; trang 13 phân biệt What với How của activity. |
| [V00 - Gioi thieu mon hoc.pdf](<../../V00 - Gioi thieu mon hoc.pdf>) | 12 | Phạm vi và công cụ môn học; không dùng ví dụ công nghệ trong bài giảng để phủ định stack thực tế của repository. |
| [V01 - Tong quan ve CNPM.pdf](<../../V01 - Tong quan ve CNPM.pdf>) | 54 | Tổng quan kiến trúc/chất lượng/quy trình; không bổ sung yêu cầu mới cho cửa hàng mỹ phẩm từ ví dụ của giáo trình. |
| [V02 - Xac dinh yeu cau.pdf](<../../V02 - Xac dinh yeu cau.pdf>) | 47 | Trang 19–21, 34, 38–43: công việc xuất phát từ nghiệp vụ; mô hình cô đọng, nhiều mức chi tiết, không lệ thuộc cài đặt. |
| [V03 - Thiet ke phan mem.pdf](<../../V03 - Thiet ke phan mem.pdf>) | 37 | Trang 7: đi từ tổng thể tới chi tiết; trang 14–27: DFD/phân rã, không dùng ký hiệu hay luồng DFD trong use-case diagram. |
| [V04 - Thiet ke du lieu.pdf](<../../V04 - Thiet ke du lieu.pdf>) | 42 | Trang 5–12, 24–29: entity/bảng/quan hệ dùng trong mô hình dữ liệu, không tự biến thành UC quản lý khi chưa có hành vi. |
| [V05 - Thiet ke giao dien.pdf](<../../V05 - Thiet ke giao dien.pdf>) | 35 | Trang 5, 12–13: sơ đồ luân chuyển màn hình là đầu ra thiết kế UI, không phải use-case diagram; trang 18 khuyến nghị ẩn thông tin kỹ thuật thô. |
| [V07 - Kiem thu.pdf](<../../V07 - Kiem thu.pdf>) | 26 | Trang 3–5, 7–13: phân biệt kiểm tra hình/source với thực nghiệm nghiệp vụ; không tuyên bố đã chạy test ứng dụng. |
| [V08 - Suu lieu.pdf](<../../V08 - Suu lieu.pdf>) | 17 | Trang 3–6, 13–14: tài liệu dễ dùng, có hình chức năng, thống nhất định dạng và cập nhật cùng hệ thống. |

## Các thay đổi áp dụng

1. Một mục tiêu như Quản lý thương hiệu chứa các nhánh thêm/sửa/xóa; không dùng include để ép mọi nhánh cùng chạy. Cách nhóm này theo ví dụ đăng ký học phần trang 31–37 của SDE02b.
2. Mọi use case vẫn truy vết về hành vi nguồn; 83 hành vi đã nhóm đủ một lần, tiện ích theme được ghi ngoài phạm vi nghiệp vụ.
3. Bỏ bộ hình theo từng actor vì phần hành vi kế thừa đã thể hiện ở hình tổng quan và module; bỏ việc xuất cùng một hình dưới ba định dạng.
4. Vai trò con nhân viên → Nhân viên giữ generalization đúng hướng. Không suy diễn ADMIN kế thừa CUSTOMER hoặc các role ngang cấp.
5. Mã ưu đãi là hành vi bổ sung có điều kiện tại Tính tổng cuối trong preview/place. Mũi tên extend trỏ từ UC bổ sung về UC cơ sở.
6. Login là điều kiện của các chức năng bảo vệ; thao tác repository, map DTO, giữ tồn nội bộ không được thêm thành ellipse kỹ thuật.
7. Lỗi thiếu guard API được ghi ở báo cáo quyền, không được trình bày thành mục tiêu quản trị của khách vãng lai. Association là lựa chọn vai trò tham gia nghiệp vụ, không là bằng chứng quyền độc quyền.

## Chỗ cần đọc cẩn thận trong PDF

Hình extend ở UML tiếng Việt trang 12 có mũi tên B → A nhưng câu chú thích phía dưới gọi A extend B, hai phần không thống nhất. Bộ sửa dùng quy ước minh họa rõ của SDE02b trang 27: Extending Use Case → Base Use Case. Không đảo mũi tên theo câu chú thích mâu thuẫn này.

Không chuyển các ví dụ ATM/thư viện/đăng ký học phần/blog hoặc dịch vụ ngoài trong PDF thành chức năng của dự án. Không dựng sequence/activity/class/DFD trong bộ use-case PNG.
