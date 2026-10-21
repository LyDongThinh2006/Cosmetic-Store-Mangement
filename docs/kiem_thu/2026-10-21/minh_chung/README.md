# Quy ước minh chứng

XML và CSV được trích từ Surefire sau thực thi. XML giữ tên test, số đếm, thời gian, assertions/stack trace và dòng QA_FIXTURE; đã bỏ properties cùng stdout/stderr ngoài fixture để tránh lộ dữ liệu xác thực/OTP. Log *_maven_summary.log là bản lọc những dòng tổng kết Maven, không phải toàn bộ log ứng dụng. SHA-256 của XML gốc và bản đã che được lưu để truy vết. Log/XML gốc nằm trong thư mục tạm của phiên rà soát, không đưa bản chứa bí mật vào báo cáo.

PASS chỉ xác nhận assertions của test cụ thể trong môi trường của lần chạy. FAIL không đồng nghĩa một defect mới cho mỗi assertion. Baseline và final H2 có phần trùng nhau; PostgreSQL chạy lại 15 ca tích hợp, không cộng thành 164 ca độc lập. Không có ảnh trình duyệt, email thật hoặc bộ sưu tập API thực thi thủ công trong hồ sơ này.
