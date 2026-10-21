# CHƯƠNG 7: KẾT LUẬN

## 7.1. Kết quả đạt được

### 7.1.1. Về kiến thức

Nhóm đã vận dụng kiến thức Công nghệ phần mềm vào khảo sát, xác định yêu cầu, phân tích, thiết kế, cài đặt và kiểm thử LUNEA; xác định tác nhân, chức năng và quy định nghiệp vụ bằng danh sách yêu cầu, lược đồ Use Case và đặc tả. Việc đối chiếu với thiết kế và chương trình giúp nhóm hiểu quan hệ giữa các giai đoạn phát triển phần mềm.

Nhóm đã sử dụng Java, Spring Boot, mô hình MVC và kiến trúc phân tầng; tìm hiểu Spring Data JPA, Hibernate, PostgreSQL để lưu trữ dữ liệu và Spring Security, JWT, BCrypt để xử lý xác thực, phân quyền, lưu mật khẩu. Khi xây dựng giao diện bằng Thymeleaf, HTML, CSS và JavaScript, nhóm vận dụng các nguyên tắc bố trí rõ ràng, thống nhất màu sắc, phân cấp thông tin và phản hồi thao tác.

### 7.1.2. Về kỹ năng

Về chuyên môn, nhóm rèn luyện phân tích nghiệp vụ, lập sơ đồ yêu cầu, thiết kế cơ sở dữ liệu, khóa và ràng buộc giữa các bảng; thực hành xây dựng entity, DTO, repository, service, controller, giao diện và kết nối API. Việc thiết kế kịch bản, đọc kết quả kiểm thử và đối chiếu dữ liệu giúp nhóm nhận diện khác biệt giữa yêu cầu với hành vi chương trình.

Về kỹ năng mềm, nhóm rèn luyện phân công công việc, trao đổi tiến độ, phối hợp tích hợp và quản lý thời gian. Git hỗ trợ theo dõi và phối hợp mã nguồn; tài liệu framework, thư viện và thông báo lỗi tiếng Anh giúp nhóm cải thiện kỹ năng đọc hiểu chuyên ngành và chủ động tìm kiếm thông tin.

### 7.1.3. Về thái độ

Nhóm nhận thức rõ trách nhiệm đối với nghiệp vụ, dữ liệu người dùng và khả năng sử dụng của phần mềm; rèn luyện tinh thần học hỏi, tiếp nhận góp ý và trách nhiệm với công việc. Việc ghi nhận kết quả có căn cứ, phân biệt nội dung đã làm với dự kiến giúp hình thành tác phong cẩn thận, trung thực và phù hợp với yêu cầu nghề nghiệp.


### 7.1.4. Về sản phẩm

Nhóm đã xây dựng tài liệu khảo sát hiện trạng, danh sách yêu cầu, nhận diện tác nhân, lược đồ và đặc tả Use Case, thiết kế dữ liệu, thiết kế giao diện, hướng dẫn cài đặt và nội dung kiểm thử. Các lược đồ dữ liệu được đối chiếu với entity và quan hệ trong mã nguồn. Hướng dẫn cài đặt mô tả công nghệ, môi trường, cấu hình cơ sở dữ liệu và các bước khởi chạy, hỗ trợ bàn giao và tái sử dụng chương trình trong môi trường học tập.

Website LUNEA được hiện thực với khu vực khách hàng và quản trị. Phía khách hàng có các luồng tài khoản, tìm kiếm và lọc sản phẩm, xem biến thể, danh sách yêu thích, giỏ hàng, áp dụng ưu đãi và đặt hàng thanh toán khi nhận hàng (COD). Hệ thống có chức năng xem lịch sử, chi tiết, theo dõi trạng thái, hủy đơn khi còn chờ xác nhận, đánh giá sản phẩm và yêu cầu trả hàng.

Phía quản trị có các chức năng quản lý sản phẩm, danh mục, thương hiệu, chi nhánh, nhân viên; tra cứu, xác nhận và cập nhật trạng thái đơn. Tồn kho được tổ chức theo SKU tại từng cửa hàng; các luồng quản lý nhà cung cấp, lập và xác nhận phiếu nhập, tra cứu tồn, điều chuyển hàng đã được xây dựng. Chức năng voucher hỗ trợ tạo, cập nhật, thay đổi trạng thái và kiểm tra điều kiện theo thời gian, giới hạn lượt dùng, giá trị đơn tối thiểu.

Chương trình đã hiện thực việc chọn chi nhánh đáp ứng giỏ hàng, lưu thông tin sản phẩm và đơn giá tại thời điểm đặt, tạo bản ghi giữ tồn và xử lý thay đổi khi hủy hoặc giao hàng. Kiến trúc phân tầng phân định trách nhiệm giao diện, xử lý nghiệp vụ và truy cập dữ liệu. Cơ chế xác thực, kiểm tra quyền và phạm vi chi nhánh được tích hợp trong ứng dụng, trong đó các thao tác đơn hàng có kiểm tra phạm vi dữ liệu của nhân viên.

Nhóm đã thiết kế các ca kiểm thử chức năng chính với điều kiện trước, đầu vào, bước thực hiện, kết quả mong đợi; xây dựng các lớp kiểm thử bằng JUnit Jupiter, Mockito, Spring Boot Test và MockMvc. Hồ sơ lưu kết quả theo từng lần chạy, phân biệt ca đã thực thi với kịch bản chờ kiểm tra. Các tài liệu và công cụ này hỗ trợ tái kiểm thử, đối chiếu hành vi chương trình và xác định phần cần tiếp tục hoàn thiện.


## 7.2. Ưu điểm và nhược điểm

### 7.2.1. Ưu điểm

- Giao diện có màu sắc và cách bố trí thống nhất giữa các màn hình.
- Các chức năng được tổ chức rõ ràng theo vai trò người sử dụng.
- Dữ liệu sản phẩm và tồn kho phù hợp với mô hình chuỗi cửa hàng.
- Đơn hàng lưu thông tin sản phẩm và giá tại thời điểm đặt.
- Mã nguồn được phân lớp, thuận tiện cho việc đọc và bảo trì.
- Tài liệu phân tích, thiết kế và cài đặt được trình bày có hệ thống.

### 7.2.2. Nhược điểm

- Việc đồng bộ giao diện và API ở thao tác cập nhật giỏ hàng chưa hoàn chỉnh.
- Một số tình huống ngoại lệ của nhập, điều chuyển và giữ tồn cần tiếp tục hoàn thiện.
- Thanh toán hiện chỉ hỗ trợ COD, chưa tích hợp cổng thanh toán trực tuyến.
- Phạm vi kiểm thử còn tập trung vào dữ liệu QA, chưa bao phủ tải lớn và vận hành dài hạn.

## 7.3. Hướng phát triển

- Hoàn thiện việc đồng bộ giao diện với API và xử lý các tình huống ngoại lệ của giỏ hàng, đơn hàng và tồn kho.
- Bổ sung phương thức thanh toán trực tuyến và kết nối theo dõi giao hàng với đơn vị vận chuyển.
- Mở rộng báo cáo quản trị theo kỳ, theo chi nhánh và chức năng xuất báo cáo phục vụ tra cứu.
- Mở rộng kiểm thử trên trình duyệt, kiểm thử hiệu năng và đánh giá ứng dụng trong môi trường sử dụng thực tế.
