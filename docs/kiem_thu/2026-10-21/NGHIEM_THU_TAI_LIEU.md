# Kiểm tra bản tối ưu hiện hành – 13 trang

- Giữ đủ **13 ca giao diện**, liên kết 18 UC trực tiếp; UC85 là điều kiện phân chi nhánh, không được đếm là thao tác đã kiểm thử ở KT-PQ-01.
- Word giảm **14 → 13 trang**, có **9 vùng ảnh** và hai bảng kết quả tích hợp thực tế. Giữ Times New Roman 13pt thân bài/12pt bảng; A4, trái 3cm, phải 2cm, trên/dưới 2,5cm.
- **20 bảng, 315 ô** và toàn bộ đoạn văn khớp PDF; không phát hiện tách dòng bảng hoặc tràn trang. Markdown đã sửa dấu phân cách header để số cột nhất quán.
- 9 bookmark ANH_CN_01–09, chú thích được đánh lại; chưa có ảnh thật. Hai vùng ảnh dự trù trùng được bỏ; không xóa ảnh thật hoặc lược bỏ ca/tiêu chí quan trọng.
- Source rà lại cho thấy khác biệt JSON/query parameter ở cập nhật giỏ; chỉ ghi rủi ro source, không tự kết luận FAIL cho ca giao diện chưa chạy. Địa chỉ và tiêu chí đăng nhập/ưu đãi đã chỉnh theo mã nguồn.
- Chạy lại đúng 5 integration method có sẵn: **5 PASS, 0 failures/errors/skipped, exit 0**, 04:00:09–04:00:42 (+07:00), tiến trình 33,620 giây. XML và bảng kết quả thực tế đối chiếu đúng từng assertion. PostgreSQL là bằng chứng trước, không phải lượt chạy mới.
- **299 tệp source/build không đổi SHA-256**; không thêm/xóa source/test. 13 ca giao diện vẫn NOT RUN; không dùng 5 PASS tích hợp thay cho kết quả trình duyệt.
- [Biên bản đối chiếu 13 ca và 5 test](RA_SOAT_13_CA_VA_KET_QUA_TICH_HOP.md); [minh chứng mới](minh_chung/xac_minh_5_ca_pass/README.md).

## Nhật ký các bản trước

# Bản trước: 14 trang/11 vùng ảnh

- Chương hiện hành có **13 ca, 7 nhóm chức năng**, đủ dữ liệu đầu vào, điều kiện, bước thao tác, kết quả mong đợi, thực tế/trạng thái và liên kết hình. Ưu tiên luồng website thay vì lấy test runner làm nội dung chính.
- Microsoft Word xuất bản cuối thành **14 trang, đã bao gồm 11 vùng ảnh trống cùng chú thích**. Giới hạn 15 trang; ảnh thật chèn theo kích thước hướng dẫn, không thêm trang ảnh riêng.
- 18 bảng; **265 ô** và toàn bộ đoạn văn đã đối chiếu đầy đủ với PDF. Không phát hiện dòng bảng tách trang hoặc chữ nằm ngoài trang. Phông chỉ gồm họ Times New Roman; A4, thân bài 13pt, bảng 12pt, lề trái 3cm, phải 2cm, trên/dưới 2,5cm.
- Có 11 bookmark ANH_CN_01–ANH_CN_11; DOCX hiện có 0 ảnh thật. Vùng trống là diện tích dành cho người dùng bổ sung ảnh, không phải ảnh minh chứng giả. Hướng dẫn mô tả đúng bước và màn hình cần chụp.
- Các ca giao diện: **0 PASS, 0 FAIL, 13 NOT RUN, 0 BLOCKED** tại thời điểm biên tập. Năm PASS tự động hiện có được ghi riêng, không suy ra giao diện đã đạt. Không chạy thêm test trong lần biên tập này.
- SHA-256 của **299 tệp source/build không đổi**; không thêm/xóa tệp trong src. Giữ nguyên các thay đổi code đã có của người dùng.
- PDF xem bố cục và metadata đã cập nhật khớp DOCX mới. Audit, lỗi phần mềm và bằng chứng kiểm thử trước được giữ nguyên.

## Nhật ký các bản trước

# Bản trước: năm ca tích hợp PASS

- Đã chạy lại 5method hiện có của AuditBusinessIntegrationTest: **5 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS; 03:17:52–03:18:27 ngày 21/10/2026 (+07:00), thời gian tiến trình 35,002 giây.
- Chương mới có đủ 5biên bản KT-P01–KT-P05 cùng mục tiêu, môi trường, input, bước, expected, actual, PASS và kết luận. PostgreSQL là đối chứng PASS của cùng 5method trong hồ sơ trước; không ghi là lần chạy PostgreSQL mới.
- Word đã xuất **10 trang bao gồm 6vùng ảnh trống và chú thích có sẵn**, không vượt 15trang. Chưa có ảnh thực được chèn; các vùng trống là vị trí người dùng bổ sung ảnh, không phải ảnh minh chứng giả.
- Có 8bảng; 139 ô và toàn bộ đoạn văn khớp PDF; không phát hiện dòng bảng tách trang hoặc chữ tràn trang. Phông PDF chỉ có họ Times New Roman.
- 299tệp source/build có SHA-256 không đổi; không thêm hoặc xóa tệp trong src. Không viết/sửa test mới.
- Bản PDF xem bố cục, metadata, XML/log của 5ca và hướng dẫn ảnh đã cập nhật. Báo cáo đầy đủ và kết quả các ca khác giữ nguyên.

## Nhật ký các phiên bản trước

# Kiểm tra bản chương 6.2 rút gọn hiện hành

Bản hiện hành chỉ trình bày 10kịch bản chính KT-01–KT-10; phần kết quả tự động trích 10ca cũ có liên quan (5 PASS / 5 FAIL), không chạy thêm kiểm thử hoặc thay đổi code trong lần biên tập này. Các ca thao tác chưa có biên bản vẫn NOT RUN.

- Microsoft Word xuất bản nội dung **11 trang**,14 bảng;227 ô bảng và mọi đoạn văn đối chiếu đầy đủ với PDF, không phát hiện dòng bảng bị tách giữa hai trang.
- Có 8bookmark không in ra trang để chèn ảnh, không có ảnh/placeholder giả trong DOCX. Phép kiểm tra dành đủ 8vùng ảnh 7cm cùng chú thích cho **14 trang**, nằm trong giới hạn 20trang; bản mô phỏng chỉ giữ ở thư mục tạm, không đưa vào báo cáo.
- Giữ A4, lềtrái/trên/dưới 3cm, phải 2cm; thân bài Times New Roman 13, bảng 12. Hướng dẫn chụp và kích thước ảnh nằm tại docs/HUONG_DAN_CHUP_ANH_KIEM_THU.md.
- SHA-256 của 299tệp source/build được theo dõi không đổi; không thêm/xóa tệp trong src. Các thay đổi code có trước lần biên tập được giữ nguyên.
- Bản PDF BO_CUC_CHUONG_6_2.pdf và chapter_layout_checks.json đã cập nhật khớp DOCX rút gọn. Phụ lục testcase, audit và kết quả tự động đầy đủ vẫn giữ nguyên.

## Ghi chép lịch sử: kiểm tra bản đầy đủ trước khi rút gọn

# Kiểm tra bàn giao tài liệu

Đây là kiểm tra chất lượng hồ sơ, không phải xác nhận phần mềm đạt nghiệm thu.

| Hạng mục | Kết quả kiểm tra tài liệu |
|---|---|
| DOCX chương 6.2 | Microsoft Word đã xuất bản cuối: **20 trang**,17 bảng; toàn bộ546 ô bảng có nội dung khớp PDF. |
| DOCX danh sách ca | Microsoft Word đã xuất: **52 trang**,53 bảng; nội dung mọi ô được đối chiếu với PDF, không phát hiện ô thiếu. Đây là phụ lục tra cứu đầy đủ, không cộng vào số trang chương chính. |
| Phân trang bảng | Không phát hiện dòng bảng có các ô nằm khác trang; header được lặp, mỗi dòng có cantSplit. Các bảng dài vẫn nối sang trang sau giữa hai dòng; không khẳng định toàn bộ một bảng luôn vừa một trang. |
| Bố cục | Không phát hiện chữ nằm ngoài khung trang; đã xem hình render các trang đầu, chiến lược, ca trọng yếu, bảng lỗi và phụ lục. Các ca trọng yếu trong chương chính được đặt đầu trang để dễ đọc. |
| Phông/khổ giấy | Times New Roman,thân bài 13pt, bảng11,5pt; A4, lềtrên/dưới/trái3cm, phải 2cm. PDF cuối không còn Calibri ở tiêu đề. |
| Ảnh/placeholder | DOCX chương và danh sách ca có0 ảnh; không có khung CHÈN HÌNH/TODO/PLACEHOLDER. Các PDF ở minh_chung chỉ là bản xem bố cục báo cáo, không phải minh chứng phần mềm qua ảnh. |
| Tính nhất quán |53 IDca duy nhất;91 IDUC;75 UC có ít nhất một phần triển khai đều liên kết ca;16 UC chưa triển khai có lý do, không tạo API/test giả. |
| Trạng thái | Bộ thiết kế:5PASS,10FAIL,37NOT RUN,1BLOCKED. Mỗi trạng thái PASS/FAIL của15 ca mới đã đối chiếu với XML H2 và PostgreSQL. |
| Toàn suite | H2 cuối149tests:137PASS,12failures,0errors/skipped; PostgreSQL chạy lại15 ca mới:5PASS,10failures,0errors/skipped. Không cộng lần chạy trùng. |
| Source |SHA-256 của300 tệp ban đầu không đổi; thay đổi chưa commit có trước được giữ nguyên. Chỉ bổ sung AuditBusinessIntegrationTest.java. |
| Dữ liệu nhạy cảm | XML bỏ properties/output có thể chứa bí mật; không có mật khẩu PostgreSQL tạm trong bản bàn giao. Không có ảnh chứa mật khẩu/cookie/JWT. |

Bản Word gốc8 trang được lưu tại ban_goc/. Audit ban đầu đã nêu9 khung chờ ảnh, không có ảnh thật, thiếu ID/traceability và bảng tổng hợp ngắt trang7–8. Không gọi nội dung “Chưa thực hiện” trong bản cũ là giả PASS.

Các thử xuất lỗi hoặc lấy nhầm tài liệu đang mở không được đưa vào hồ sơ. Hai PDF BO_CUC_* được xuất từ bản sao QA có tên riêng, nội dung đối chiếu đúng hai DOCX bàn giao. Lỗi timeout/mở tài liệu trong quá trình dùng Word không được quy thành lỗi phần mềm LUNEA. Bản cuối đã xuất thành công.

Giới hạn: chưa có E2E trình duyệt/SMTP, ảnh API/DB thủ công hoặc coverage mã nguồn. Các defect vẫn OPEN, regression vẫn FAIL; cần sửa có phê duyệt và chạy lại trước khi nghiệm thu hệ thống. Minh chứng và bước còn phải thu thập được liệt kê riêng, không nằm dưới dạng ảnh chờ trong chương.
