# Khoảng trống triển khai và giới hạn xác minh

Phân biệt chức năng đã có luồng lưu/đọc, lỗi tích hợp và các mục mới chỉ xuất hiện trong thiết kế. Không suy diễn feature từ entity, enum, permission, dependency, test hoặc tên màn hình.

## Các lỗi/nghiệp vụ chưa hoàn thiện

| Vấn đề | Bằng chứng / hệ quả |
|---|---|
| Đăng ký UI | register.html gửi displayName thay fullName bắt buộc → 400; toast tự đăng nhập không có lời gọi login/cookie. |
| Reset/wishlist UI | Raw fetch thiếu X-Requested-With → 403 khi dùng cookie. API reset còn thiếu xác minh sở hữu. |
| Giỏ | PUT response items/subtotal khác itemSubtotal/cartSubtotal UI; +/- lấy quantity SSR cũ; sửa/xóa cart item thiếu ownership. |
| Trả hàng | create không set customer bắt buộc → persistence thật không có happy path hợp lệ; approval/reject chỉ đổi trạng thái, chưa hoàn kho/refund. |
| Đặt hàng và tồn | Cửa hàng chỉ định không kiểm đủ tồn; auto fallback store đầu dù thiếu; chưa lock/recheck khi giữ; có thể held vượt actual. |
| Vòng đời đơn | Service chặn trạng thái terminal nhưng cho nhảy/lùi các non-terminal; hủy sau SHIPPING không hoàn lại COMMITTED. |
| Quyền và ownership | Nhiều API quản lý public; permission không guard; inventory không scope; đọc đơn/address/customer có các đường thiếu ownership. |
| Hồ sơ/địa chỉ/nhân viên | Phone response không map; edit địa chỉ thiếu nạp vị trí; default có thể nhiều địa chỉ; email/internalEmail mismatch; deactivate employee không khóa account. |
| Danh mục | update parentId bị bỏ qua; xóa product/brand/category là xóa vật lý và có thể lỗi FK. |
| Chuyển/nhập kho | Ship kiểm actual không trừ held; thiếu lock/version, @Valid cascade; checked exception sau lưu dần có nguy cơ commit một phần (chưa xác minh runtime). |

## Backend đã có nhưng chưa có giao diện

BeautyProfile, danh sách khách hàng, CRUD/phân công nhân viên, thêm/sửa/xóa sản phẩm/thương hiệu/danh mục, cập nhật cửa hàng, nhà cung cấp/nhập hàng/điều chuyển, review thật và moderation, clear cart. Return PROCESSING/COMPLETED có API đổi trạng thái nhưng thiếu UI và nghiệp vụ hoàn hàng.

## UI placeholder — không đưa vào diagram như chức năng hoàn tất

| Control / nhãn | Hành vi source thực tế | Nguồn |
|---|---|---|
| Ghi nhớ đăng nhập | Checkbox has no id/name and handleLogin never reads/sends it; no refresh token. | [src/main/resources/templates/auth/login.html:62](../../../../src/main/resources/templates/auth/login.html#L62) |
| Xem đánh giá khách hàng trên chi tiết sản phẩm | Static single testimonial, no Review API fetch/create form; do not mark review UI integrated. | [src/main/resources/templates/product-detail.html:190](../../../../src/main/resources/templates/product-detail.html#L190) |
| Xem voucher của tôi | Toast hardcoded 3 vouchers or no vouchers, no fetch. | [src/main/resources/templates/account/profile.html:65](../../../../src/main/resources/templates/account/profile.html#L65)<br>[src/main/resources/templates/account/orders.html:53](../../../../src/main/resources/templates/account/orders.html#L53)<br>[src/main/resources/templates/account/wishlist.html:54](../../../../src/main/resources/templates/account/wishlist.html#L54) |
| Xem thông báo | Only toast Không có thông báo mới. | [src/main/resources/templates/account/orders.html:56](../../../../src/main/resources/templates/account/orders.html#L56) |
| Cài đặt tài khoản | Only toast tính năng đang hoàn thiện. | [src/main/resources/templates/account/orders.html:59](../../../../src/main/resources/templates/account/orders.html#L59) |
| Trang khuyến mãi | Link to /products?concern=Trị+Mụn, not real promotion/voucher catalogue. | [src/main/resources/templates/fragments/header.html:43](../../../../src/main/resources/templates/fragments/header.html#L43)<br>[src/main/resources/templates/fragments/footer.html:39](../../../../src/main/resources/templates/fragments/footer.html#L39) |
| Blog | Link / home; no blog route/entities/API. | [src/main/resources/templates/fragments/header.html:52](../../../../src/main/resources/templates/fragments/header.html#L52) |
| Đặt lịch tư vấn | Link /stores only; no reservation form/API. | [src/main/resources/templates/home.html:191](../../../../src/main/resources/templates/home.html#L191) |
| Tính ngày dự kiến giao hàng | Fixed text 26 - 28/09/2026; no shipping-provider API/calculation. | [src/main/resources/templates/checkout.html:127](../../../../src/main/resources/templates/checkout.html#L127) |
| Voucher giảm giá dự phòng tại client | Hardcoded codes after API error, may disagree persisted checkout. | [src/main/resources/templates/checkout.html:311](../../../../src/main/resources/templates/checkout.html#L311) |
| Xem điều khoản/chính sách | Static informational toast; not policy administration or backend workflow. | [src/main/resources/templates/fragments/footer.html:50](../../../../src/main/resources/templates/fragments/footer.html#L50)<br>[src/main/resources/templates/fragments/footer.html:77](../../../../src/main/resources/templates/fragments/footer.html#L77)<br>[src/main/resources/templates/fragments/footer.html:83](../../../../src/main/resources/templates/fragments/footer.html#L83) |

## Chưa triển khai dù có trong thiết kế

CRUD SKU, ảnh/thuộc tính/upload Cloudinary; BeautyProfile recommendation; voucher entity/administration/quota/expiry/usage; membership accrual/redemption; customer lock/unlock; role/permission CRUD; audit viewer/writer; pickup, staff assign/reassign store; online payment/email/OTP/courier/maps; advanced/exported reporting; transfer approval hoặc receipt tách riêng. Không tạo use case giả cho những phần này.

## Bằng chứng chi tiết của từng nhánh khảo sát

[Account/security](../traceability/analysis-account-security.json), [operations](../traceability/analysis-operations.json), [commerce](../traceability/analysis-commerce.json), [frontend](../traceability/analysis-frontend.json). Các JSON là nhật ký phân tích trung gian; trạng thái cuối cùng phải xem use-cases.json/đặc tả vì đã gộp cả frontend và backend.

Không chạy database, seed, migration, API mutation, Maven hoặc test suite ứng dụng trong lần tài liệu hóa này. Tests đã đọc chỉ là chứng cứ đối chiếu; mock save có thể bỏ qua validation thật. Render/syntax/document validation có kết quả riêng; không thay thế kiểm thử nghiệp vụ.

Chưa có Enterprise Architect để kiểm thử import. Không xuất XMI và không khẳng định tương thích EA. PlantUML --xmi được tài liệu chính thức mô tả cho class diagrams; không dùng nó tạo XMI use case giả.
