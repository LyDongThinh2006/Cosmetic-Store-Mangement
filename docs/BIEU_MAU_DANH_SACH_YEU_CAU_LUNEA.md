# BIỂU MẪU DANH SÁCH YÊU CẦU

**Tên hệ thống:** LUNEA – Website bán mỹ phẩm theo mô hình chuỗi cửa hàng.
**Nhóm thực hiện:** Nhóm 01.
**Phiên bản:** ………………  **Ngày lập/cập nhật:** ………………  **Người lập:** ………………

Các dòng có dấu (*) và nền xám là ví dụ minh họa từ báo cáo hiện tại. Thay bằng nội dung đã thống nhất khi hoàn thiện báo cáo; thêm dòng nếu cần và đánh lại STT trong từng bảng.
Loại công việc nghiệp vụ: Tra cứu, Lưu trữ, Tính toán, Kết xuất. Mã quy định có dạng MÃ_TÁC_NHÂN-QĐn; giải thích nội dung quy định bên dưới bảng tương ứng.
Mô tả yêu cầu là hành vi hoặc chất lượng mong đợi, không phải kết quả kiểm thử. Với yêu cầu chất lượng, ghi điều kiện và tiêu chí đánh giá có thể kiểm tra.

## 1.5. LẬP DANH SÁCH YÊU CẦU

### 1.5.1. Danh sách yêu cầu chức năng nghiệp vụ

#### 1.5.1.1. Khách hàng – Mã số: KH

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Xem danh sách sản phẩm | Tra cứu | KH-QĐ1 | Chỉ hiển thị sản phẩm đang kinh doanh. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** KH-QĐ1: Chỉ sản phẩm có trạng thái Đang kinh doanh mới được hiển thị cho khách mua hàng.
KH-QĐ…: …………………………………………………………………

#### 1.5.1.2. Nhân viên quản lý sản phẩm – Mã số: QLSP

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Thêm sản phẩm | Lưu trữ | QLSP-QĐ1 | Nhập thông tin sản phẩm và các dữ liệu bắt buộc. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** QLSP-QĐ1: Mỗi sản phẩm có mã duy nhất, tên, thương hiệu, danh mục và các thông tin bắt buộc.
QLSP-QĐ…: …………………………………………………………………

#### 1.5.1.3. Quản trị viên / Quản lý chuỗi – Mã số: QLCH

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Cập nhật thông tin cửa hàng | Lưu trữ | QLCH-QĐ2 | Cập nhật thông tin chi nhánh. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** QLCH-QĐ2: Thông tin cửa hàng gồm tối thiểu tên, địa chỉ, số điện thoại, giờ hoạt động và trạng thái.
QLCH-QĐ…: …………………………………………………………………

#### 1.5.1.4. Nhân viên nhập hàng – Mã số: QLNH

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Lập phiếu nhập hàng | Lưu trữ | QLNH-QĐ3 | Xác định nhà cung cấp và cửa hàng/kho nhận. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** QLNH-QĐ3: Phiếu nhập phải xác định nhà cung cấp, nơi nhận, danh sách SKU và số lượng.
QLNH-QĐ…: …………………………………………………………………

#### 1.5.1.5. Nhân viên kho – Mã số: QLTK

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Tính tồn khả dụng | Tính toán | QLTK-QĐ8 | Theo từng cặp SKU – cửa hàng/kho. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** QLTK-QĐ8: Tồn khả dụng = Tồn thực tế − Số lượng đang giữ. Tồn khả dụng không được nhỏ hơn 0.
QLTK-QĐ…: …………………………………………………………………

#### 1.5.1.6. Nhân viên chăm sóc khách hàng – Mã số: QLKH

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Xem hồ sơ khách hàng | Tra cứu | QLKH-QĐ1 | Chỉ truy cập dữ liệu trong phạm vi được cấp. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** QLKH-QĐ1: Nhân viên chỉ được truy cập thông tin khách hàng trong phạm vi quyền được cấp.
QLKH-QĐ…: …………………………………………………………………

#### 1.5.1.7. Nhân viên xử lý đơn hàng – Mã số: QLDH

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Cập nhật trạng thái đơn hàng | Lưu trữ | QLDH-QĐ4 | Kiểm tra điều kiện trước khi chuyển trạng thái. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** QLDH-QĐ4: Trạng thái đơn tuân theo trình tự Chờ xác nhận → Đã xác nhận → Đang chuẩn bị → Đang giao → Hoàn thành; đơn có thể chuyển sang Đã hủy khi thỏa điều kiện hủy.
QLDH-QĐ…: …………………………………………………………………

#### 1.5.1.8. Nhân viên Marketing – Mã số: QLKM

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Kiểm tra điều kiện voucher | Tính toán | QLKM-QĐ4 | Xác định voucher đủ hoặc không đủ điều kiện. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** QLKM-QĐ4: Voucher phải đồng thời thỏa điều kiện thời gian, số lượt sử dụng, giá trị đơn và đối tượng áp dụng.
QLKM-QĐ…: …………………………………………………………………

#### 1.5.1.9. Quản trị viên – Mã số: QLNV

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Gán vai trò và quyền sử dụng | Lưu trữ | QLNV-QĐ4 | Phân quyền theo nhiệm vụ và chi nhánh. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** QLNV-QĐ4: Quyền truy cập được xác định theo vai trò và phạm vi chi nhánh.
QLNV-QĐ…: …………………………………………………………………

#### 1.5.1.10. Quản lý / Quản trị viên – Mã số: BCTK

| STT | Công việc | Loại công việc | Quy định/Công thức liên quan | Ghi chú |
| --- | --- | --- | --- | --- |
| 1* | Báo cáo doanh thu theo thời gian | Kết xuất | BCTK-QĐ1 | Theo ngày, tháng hoặc khoảng thời gian. |
| 2 |  |  |  |  |
| 3 |  |  |  |  |
| 4 |  |  |  |  |

**Quy định/Công thức minh họa:** BCTK-QĐ1: Doanh thu được tính dựa trên các đơn hàng ở trạng thái Hoàn thành.
BCTK-QĐ…: …………………………………………………………………

### 1.5.2. Danh sách yêu cầu chức năng hệ thống

Ghi các chức năng kỹ thuật hoặc chức năng dùng chung hỗ trợ vận hành hệ thống.

| STT | Nội dung | Mô tả chi tiết | Nhóm |
| --- | --- | --- | --- |
| 1* | Đăng nhập | Xác thực người dùng trước khi truy cập các chức năng yêu cầu đăng nhập. | Xác thực |
| 2* | Phân quyền theo chi nhánh | Nhân viên chỉ thao tác trên dữ liệu thuộc phạm vi được giao. | Phân quyền |
| 3 |  |  |  |
| 4 |  |  |  |
| 5 |  |  |  |
| 6 |  |  |  |
| 7 |  |  |  |
| 8 |  |  |  |

### 1.5.3. Danh sách yêu cầu chất lượng / phi chức năng

Ghi tiêu chí chất lượng và điều kiện đánh giá; chỉ tiêu định lượng cần có giá trị mục tiêu và điều kiện áp dụng.

| STT | Nội dung | Tiêu chuẩn | Mô tả chi tiết |
| --- | --- | --- | --- |
| 1* | Hiển thị Responsive | Tương thích | Website sử dụng được trên máy tính, máy tính bảng và điện thoại. |
| 2* | Thời gian tìm kiếm phù hợp | Hiệu quả | Mục tiêu phản hồi không quá khoảng 3 giây trong điều kiện vận hành bình thường của phạm vi đề tài; ghi điều kiện đo khi đánh giá. |
| 3* | Bảo vệ mật khẩu | Bảo mật | Mật khẩu không được lưu dưới dạng văn bản rõ. |
| 4 |  |  |  |
| 5 |  |  |  |
| 6 |  |  |  |
| 7 |  |  |  |
| 8 |  |  |  |

