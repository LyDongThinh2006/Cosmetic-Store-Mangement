# Kết quả kiểm thử tự động thực tế

Ngày thực thi: **21/10/2026, Asia/Ho_Chi_Minh (+07:00)**. Source nền: `6858efca55623eb55b4cca1b64d06a5dc6767083` cùng thay đổi chưa commit; thêm duy nhất test `AuditBusinessIntegrationTest.java`, không chỉnh production. SHA source và test được ghi trong minh_chung.

## Môi trường và công cụ

macOS 13.7.8, x86_64; Oracle JDK 21.0.10 được chọn tường minh (Java mặc định máy là 25.0.4). Maven Wrapper tải/cấu hình Maven 3.9.16, wrapper 3.3.4. Spring Boot **4.1.1**, JUnit Jupiter **6.0.3**, Mockito **5.23.0**, Surefire **3.5.6**, H2 **2.4.240**. Đã dùng Spring Boot Test, MockMvc, Spring Security Test và repository/service thật trong 15 ca mới. Không ghi là JUnit 5; dependency thực chạy là Jupiter 6. Chưa cấu hình JaCoCo và chưa có số liệu line/branch coverage.

PostgreSQL **16.15 (Debian 16.15-1.pgdg13+2)** trong Docker riêng, image digest `sha256:ca0bd484cb98bf4b24eb1010e73fb3fcbd6714d240fbc1a10eea5b7dbecb641d`. DB lunea_qa, user qa_lunea, host127.0.0.1:61651 của lần chạy; schema create-drop. Container tạm đã dừng/xóa sau kiểm tra, không truy cập DB local của dự án. Không dùng port này như địa chỉ môi trường tồn tại lâu dài. Metadata đầy đủ: minh_chung/postgres_environment.json.

## Các lần thực thi

| Lần | Bắt đầu–kết thúc (+07:00) | Thời gian tiến trình | Tests | PASS | Failures | Errors | Skipped | Exit |
|---|---|---|---|---|---|---|---|---|
| Baseline ngoài sandbox | Hoàn tất01:53:24 |108,80s (Maven1m42s)|134|132|2|0|0|1|
| Final H2, toàn suite |02:15:02–02:16:39|97,104s|149|137|12|0|0|1|
| PostgreSQL, 15 ca audit |02:17:54–02:18:55|61,240s|15|5|10|0|0|1|

Lần đầu trong sandbox gặp 66 errors do Mockito không tự attach agent (134 tests,2failures,66errors,0skipped); đây là cản trở môi trường cho các phương thức liên quan, không phải 66 defect ứng dụng. Đã chạy lại ngoài sandbox. Một lần thử H2 trung gian 15 ca cũng cho5PASS/10FAIL nhưng không dùng thay bằng chứng final source. Bảng trên ưu tiên lần đã xác nhận. Exit1 là kết quả thật do failures, không tuyên bố build xanh.

149 phương thức ở final H2 gồm134 hiện hữu +15 mới. PostgreSQL chạy lại cùng15 ca mới, không cộng thành164 ca độc lập. 12 failures H2 gồm10 lỗi phần mềm và2 expectation/fixture quyền của test cũ. Kết quả của15 ca mới trùng nhau trên H2/PostgreSQL. Xem LOI_PHAN_MEM.md để phân loại; số PASS chỉ tính các phương thức Surefire không failure/error/skipped trong lần thực thi này, không phải số chức năng hoàn chỉnh đã nghiệm thu.

## Lệnh đã thực thi và cách lặp lại

Thiết lập JDK21 và chạy tại thư mục gốc repo. Đường dẫn agent bên dưới là dependency đã có trong cache của máy thực chạy; máy khác thay bằng vị trí dependency tương ứng. Cờ -o chỉ dùng khi đủ cache.

```sh
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw -o test
./mvnw -o -DargLine=-javaagent:/Users/thinh/.m2/repository/org/mockito/mockito-core/5.23.0/mockito-core-5.23.0.jar test
```

Lệnh PostgreSQL được ghi ở dạng đã che mật khẩu:

```text
./mvnw -o -Dtest=AuditBusinessIntegrationTest -DargLine=-javaagent:/Users/thinh/.m2/repository/org/mockito/mockito-core/5.23.0/mockito-core-5.23.0.jar -Dqa.db.url=jdbc:postgresql://127.0.0.1:61651/lunea_qa -Dqa.db.user=qa_lunea -Dqa.db.password=[REDACTED] test
```

Để lặp trên PostgreSQL, tạo container/DB QA mới, lấy host/port/user/password của chính DB đó rồi truyền qa.db.*; không dùng DB đang chứa dữ liệu cần giữ, vì test cấu hình **create-drop**. Giá trị mật khẩu của lần audit không được bàn giao. Metadata JSON giữ mốc thời gian, argv đã che, duration và exit. `fixture()` trong test tự tạo dữ liệu riêng mỗi phương thức; không phụ thuộc thứ tự chạy hoặc IDs cố định. Thêm `-Dtest=AuditBusinessIntegrationTest#<method>` để chạy một ca; tên method trong DANH_SACH_TEST_CASE.md. Xuất Surefire ngay sau từng lần, tránh lần sau ghi đè bằng chứng.

15 ca không có transaction test bao ngoài; dữ liệu fixture được commit trước, trạng thái được đọc sau service transaction thật. EmailService thay bằng no-op để không gửi thư. Principal MockMvc được đưa vào context, chưa kiểm thử login/JWT cấp thật. QA-INT-15 đồng bộ hai giao dịch bằng barrier, chưa chứng minh tải hoặc mọi lịch cạnh tranh. H2 không thay PostgreSQL; 15 ca đã được xác nhận lại trên PostgreSQL nhưng toàn134 ca cũ chỉ chạy H2.

## Minh chứng

CSV *_test_execution.csv có từng class/method, thời gian và kết quả. XML đã che ở final_h2/ và audit_postgresql/ giữ stacktrace assertion; QA_FIXTURE ghi ID thực của dữ liệu từng ca. *_maven_summary.log giữ tổng kết Maven. XML đã loại properties/raw output chứa thông tin xác thực hoặc OTP; đây là bản trích an toàn, không giả mạo log gốc. Hai file hash ghi SHA bản gốc và bản bàn giao. Không có ảnh API/browser giả hoặc số liệu truy vấn PostgreSQL thủ công được ghi là đã chạy.
