# Hồ sơ rà soát kiểm thử LUNEA – 21/10/2026

**Chương 6.2 hiện hành:** [Word chức năng chính](../../CHUONG_6_2_KIEM_THU.docx), **13 ca, 13 trang gồm 9 vùng ảnh** và hai bảng kết quả thực tế của integration test. Word đã kiểm tra 20 bảng/315 ô; không thiếu chữ, tách dòng bảng hoặc tràn trang. [Hướng dẫn ảnh](../../HUONG_DAN_CHUP_ANH_KIEM_THU.md) cập nhật bookmark, số hình và kích thước.

[Biên bản rà soát mới](RA_SOAT_13_CA_VA_KET_QUA_TICH_HOP.md) đối chiếu từng ca với source và Use Case gốc. Có sửa tiêu chí mật khẩu sai, dữ liệu địa chỉ, bước đối chiếu ưu đãi và liên kết UC66/UC85; ghi rủi ro UI cập nhật giỏ gửi JSON khác hợp đồng controller. 13 ca giao diện giữ NOT RUN khi chưa chạy.

**Đã chạy lại 5 integration test hiện có:** 21/10/2026, 04:00:09–04:00:42 (+07:00), **5 tests, 5 PASS, 0 failures/errors/skipped, BUILD SUCCESS**. [Minh chứng xác minh mới](minh_chung/xac_minh_5_ca_pass/README.md) có XML/log/metadata; kết quả cũ [5_ca_pass](minh_chung/5_ca_pass/README.md) và PostgreSQL được giữ để đối chứng. Không cộng các lượt chạy trùng hoặc suy ra giao diện đã PASS.

Audit đầy đủ, ma trận 91 UC và bộ 53 ca bên dưới vẫn là hồ sơ tra cứu, không phải toàn bộ nội dung bắt buộc đưa vào chương chính. Lần này không sửa production/test; 299 tệp source/build không đổi.

- [Chương6.2 cải thiện – Word](../../CHUONG_6_2_KIEM_THU.docx); [bản Markdown](../../CHUONG_6_2_KIEM_THU.md).
- [Audit trước chỉnh sửa và cập nhật](AUDIT_CHUONG_6_2.md); [bản gốc](ban_goc/).
- [Danh sách test case Word](DANH_SACH_TEST_CASE.docx), [Markdown](DANH_SACH_TEST_CASE.md), [CSV](DANH_SACH_TEST_CASE.csv), [JSON](test_cases.json).
- [Kiểm kê91Use Case](MODULE_VA_USE_CASE.md), [CSV](MODULE_VA_USE_CASE.csv); [ma trận độ bao phủ](MA_TRAN_DO_BAO_PHU.md), [CSV](MA_TRAN_DO_BAO_PHU.csv).
- [Kiểm thử chưa chạy/bị chặn](CHUA_THUC_THI.md).
- [Minh chứng cần thu thập theo bước](MINH_CHUNG_CAN_THU_THAP.md); [SQL chỉ đọc dự kiến](DOI_CHIEU_POSTGRESQL.sql).
- [Kết quả chạy tự động, môi trường và lệnh](KET_QUA_KIEM_THU_TU_DONG.md); [minh chứng Surefire đã che](minh_chung/README.md).
- [10lỗi phần mềm đã xác nhận,2vấn đề test cũ và rủi ro source](LOI_PHAN_MEM.md).
- [Kiểm tra tài liệu và bảo toàn source](NGHIEM_THU_TAI_LIEU.md); [PDF bố cục chương chính 13 trang](minh_chung/BO_CUC_CHUONG_6_2.pdf), [PDF phụ lục](minh_chung/BO_CUC_DANH_SACH_TEST_CASE.pdf).

Không cộng các lần chạy trùng thành ca độc lập. Không có ảnh trình duyệt/email thật trong hồ sơ. Source production giữ nguyên; chỉ bổ sung test hồi quy được phép. Các regression FAIL được giữ để phản ánh lỗi hiện hữu, không đổi expected để làm suite xanh. Đây là bản hoàn thiện nội dung báo cáo theo hiện trạng, không phải biên bản phần mềm đã đạt nghiệm thu.
