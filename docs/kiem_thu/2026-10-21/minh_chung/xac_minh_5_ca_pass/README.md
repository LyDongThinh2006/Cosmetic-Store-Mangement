# Xác minh lại 5 integration test

Ngày 21/10/2026, 04:00:09–04:00:42 (+07:00): **5 tests, 5 PASS, 0 failures, 0 errors, 0 skipped; BUILD SUCCESS, exit 0**.

- [Metadata và lệnh](lan_chay.json).
- [XML Surefire đã bỏ properties/output nhạy cảm](TEST-5_CA_PASS.xml).
- [Tổng kết Maven](maven_summary.log).
- [Rà soát và đối chiếu 13 ca/5 method](../../RA_SOAT_13_CA_VA_KET_QUA_TICH_HOP.md).

Đã kiểm tra đúng năm method và trạng thái với hồ sơ H2/PostgreSQL trước. Lượt mới dùng H2 riêng, không chạy PostgreSQL mới, không đổi source/test và không thực thi trình duyệt/SMTP. Principal MockMvc được cung cấp vào security context. Không cộng nhiều lượt chạy thành các ca độc lập.
