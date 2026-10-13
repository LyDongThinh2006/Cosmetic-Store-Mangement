# Đặc tả Use Case theo mục tiêu nghiệp vụ

Bộ đặc tả theo tài liệu PDF môn học: một use case có mục tiêu trọn vẹn; thêm/sửa/xóa hoặc các trạng thái cùng mục tiêu là các luồng lựa chọn. ID đã được đánh lại; bảng raw-behavior-map.csv giữ ánh xạ từ bộ chi tiết trước khi gộp.

Association xác định vai trò tham gia, không khẳng định quyền độc quyền. Các API permitAll/thiếu scope vẫn ghi trung thực ở cột Quyền thực tế và báo cáo bảo mật. Trạng thái dựa trên khảo sát source, chưa nghiệm thu runtime.

| ID | Mục tiêu | Actor | Trạng thái |
|---|---|---|---|
| [UC001](#uc001) | Đăng ký tài khoản khách hàng | Khách vãng lai | Partially Implemented |
| [UC002](#uc002) | Đăng nhập | Khách vãng lai, Khách hàng, Nhân viên | Partially Implemented |
| [UC003](#uc003) | Đăng xuất | Khách hàng, Nhân viên | Implemented |
| [UC004](#uc004) | Đặt lại mật khẩu | Khách vãng lai | Partially Implemented |
| [UC005](#uc005) | Quản lý hồ sơ cá nhân | Khách hàng | Partially Implemented |
| [UC006](#uc006) | Quản lý địa chỉ nhận hàng | Khách hàng | Partially Implemented |
| [UC007](#uc007) | Quản lý hồ sơ làm đẹp | Khách hàng | Backend Only |
| [UC008](#uc008) | Tra cứu khách hàng | Nhân viên | Backend Only |
| [UC009](#uc009) | Quản lý nhân viên và phân công | Quản trị viên | Partially Implemented |
| [UC010](#uc010) | Tra cứu mỹ phẩm | Khách vãng lai, Khách hàng, Nhân viên | Implemented |
| [UC011](#uc011) | Xem chi tiết mỹ phẩm | Khách vãng lai, Khách hàng, Nhân viên | Implemented |
| [UC012](#uc012) | Quản lý sản phẩm | Nhân viên quản lý sản phẩm, Quản trị viên | Backend Only |
| [UC013](#uc013) | Tra cứu thương hiệu và danh mục | Khách vãng lai, Khách hàng, Nhân viên | Implemented |
| [UC014](#uc014) | Quản lý thương hiệu | Nhân viên quản lý sản phẩm, Quản trị viên | Backend Only |
| [UC015](#uc015) | Quản lý danh mục sản phẩm | Nhân viên quản lý sản phẩm, Quản trị viên | Partially Implemented |
| [UC016](#uc016) | Tra cứu cửa hàng | Khách vãng lai, Khách hàng, Nhân viên | Implemented |
| [UC017](#uc017) | Quản lý cửa hàng | Quản trị viên | Backend Only |
| [UC018](#uc018) | Kiểm tra còn hàng theo cửa hàng | Khách vãng lai, Khách hàng, Nhân viên | Implemented |
| [UC019](#uc019) | Tra cứu tồn kho | Nhân viên | Implemented |
| [UC020](#uc020) | Xem cảnh báo tồn thấp | Nhân viên | Implemented |
| [UC021](#uc021) | Điều chỉnh tồn kho | Nhân viên | Implemented |
| [UC022](#uc022) | Lập phiếu chuyển kho | Nhân viên | Backend Only |
| [UC023](#uc023) | Tra cứu phiếu chuyển kho | Nhân viên | Backend Only |
| [UC024](#uc024) | Xác nhận xuất chuyển kho | Nhân viên | Backend Only |
| [UC025](#uc025) | Xác nhận nhận chuyển kho | Nhân viên | Backend Only |
| [UC026](#uc026) | Quản lý nhà cung cấp | Nhân viên | Backend Only |
| [UC027](#uc027) | Lập đơn nhập hàng | Nhân viên | Backend Only |
| [UC028](#uc028) | Tra cứu đơn nhập hàng | Nhân viên | Backend Only |
| [UC029](#uc029) | Xác nhận nhập hàng vào kho | Nhân viên | Backend Only |
| [UC030](#uc030) | Quản lý giỏ hàng | Khách hàng | Partially Implemented |
| [UC031](#uc031) | Quản lý sản phẩm yêu thích | Khách hàng | Partially Implemented |
| [UC032](#uc032) | Xem tổng tiền trước khi đặt | Khách hàng | Implemented |
| [UC033](#uc033) | Áp dụng mã ưu đãi | Khách hàng | Partially Implemented |
| [UC034](#uc034) | Đặt hàng thanh toán khi nhận | Khách hàng, Nhân viên | Partially Implemented |
| [UC035](#uc035) | Tra cứu đơn hàng của tôi | Khách hàng | Partially Implemented |
| [UC036](#uc036) | Hủy đơn chờ xác nhận | Khách hàng | Implemented |
| [UC037](#uc037) | Tra cứu đơn hàng cửa hàng | Nhân viên | Implemented |
| [UC038](#uc038) | Cập nhật tiến trình đơn hàng | Nhân viên | Partially Implemented |
| [UC039](#uc039) | Hủy đơn hàng cửa hàng | Nhân viên | Partially Implemented |
| [UC040](#uc040) | Gửi yêu cầu trả hàng | Khách hàng | Partially Implemented |
| [UC041](#uc041) | Theo dõi yêu cầu trả hàng | Khách hàng, Nhân viên | Implemented |
| [UC042](#uc042) | Cập nhật xử lý trả hàng | Nhân viên | Partially Implemented |
| [UC043](#uc043) | Xem đánh giá sản phẩm | Khách vãng lai, Khách hàng, Nhân viên | Backend Only |
| [UC044](#uc044) | Viết đánh giá sản phẩm | Khách hàng | Partially Implemented |
| [UC045](#uc045) | Quản lý hiển thị đánh giá | Nhân viên | Partially Implemented |
| [UC046](#uc046) | Xem tổng quan vận hành | Nhân viên | Implemented |

<a id="uc001"></a>

## UC001 — Đăng ký tài khoản khách hàng

**Primary Actor:** Khách vãng lai  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Tài khoản và xác thực  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách vãng lai: đăng ký tài khoản khách hàng.

**Trigger:** Actor lựa chọn đăng ký tài khoản khách hàng.

**Preconditions theo nhánh**

- Đăng ký tài khoản khách hàng: Email/SĐT chưa tồn tại; dữ liệu theo RegisterRequest; không cần xác thực.

**Main Success Scenario**

1. Khách cung cấp họ tên, email, số điện thoại và mật khẩu mới.
2. Hệ thống kiểm tra dữ liệu, chuẩn hóa email/số điện thoại và kiểm tra trùng tài khoản.
3. Hệ thống tạo tài khoản khách hàng, hồ sơ với điểm tích lũy ban đầu bằng 0 và thông báo kết quả.

**Luồng lựa chọn / ngoại lệ**

- Đăng ký tài khoản khách hàng: Trùng email hoặc SĐT: 409 DUPLICATE_EMAIL/DUPLICATE_PHONE.; Dữ liệu sai @Valid: 400; không gửi email/OTP xác minh.; register.html gửi displayName thay vì fullName bắt buộc -> 400 validation; không tạo tài khoản từ form hiện tại.; Toast hứa tự động đăng nhập nhưng không có cookie/JWT hoặc lời gọi login.

**Postconditions theo nhánh**

- Đăng ký tài khoản khách hàng: Gọi API đúng payload tạo account/customer; form hiện tại chưa hoàn tất đăng ký.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:32](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L32) | AuthRestController · register | POST /api/auth/register |
| [src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java:47](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java#L47) | AccountServiceImpl · register | — |
| [src/main/resources/templates/auth/register.html:102](../../../../src/main/resources/templates/auth/register.html#L102) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L31) | AuthRestController · implementation | — |
| [src/main/java/com/thinh/cosmetic/domain/dto/request/account/RegisterRequest.java:16](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/account/RegisterRequest.java#L16) | RegisterRequest · implementation | — |

**UI và giới hạn triển khai**

- UI: UI sends displayName rather than required RegisterRequest.fullName -> @Valid returns 400 before account creation. Even with corrected field, register responds 201 without JWT cookie and UI falsely says auto login without invoking login.

<a id="uc002"></a>

## UC002 — Đăng nhập

**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Tài khoản và xác thực  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách vãng lai, Khách hàng, Nhân viên: đăng nhập.

**Trigger:** Actor lựa chọn đăng nhập.

**Preconditions theo nhánh**

- Đăng nhập: Có AccountEntity ACTIVE; mật khẩu đúng.

**Main Success Scenario**

1. Người dùng cung cấp email/số điện thoại/tên đăng nhập và mật khẩu.
2. Hệ thống kiểm tra tài khoản còn hoạt động và mật khẩu đúng.
3. Hệ thống xác lập danh tính cùng vai trò khách hàng hoặc nhân viên và trả kết quả đăng nhập.

**Luồng lựa chọn / ngoại lệ**

- Đăng nhập: Identifier rỗng hoặc sai mật khẩu/account INACTIVE: INVALID_CREDENTIALS.; Employee.status INACTIVE không được kiểm tại đăng nhập.; UI đọc res.isEmployee (không có trong LoginResponse), bỏ response.redirectUrl; employee đăng nhập thành công từ /login mặc định chuyển về / thay vì /admin.

**Postconditions theo nhánh**

- Đăng nhập: Cookie/JWT đã cấp khi credentials hợp lệ; backend trả redirectUrl nhưng UI chưa xử lý đúng hướng chuyển cho employee.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L38) | AuthRestController · login | POST /api/auth/login |
| [src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java:83](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java#L83) | AccountServiceImpl · login | — |
| [src/main/resources/templates/auth/login.html:268](../../../../src/main/resources/templates/auth/login.html#L268) | UI | — |
| [src/main/resources/static/js/api.js:65](../../../../src/main/resources/static/js/api.js#L65) | UI | — |
| [src/main/resources/templates/auth/login.html:295](../../../../src/main/resources/templates/auth/login.html#L295) | UI | — |
| [src/main/java/com/thinh/cosmetic/domain/dto/response/account/LoginResponse.java:15](../../../../src/main/java/com/thinh/cosmetic/domain/dto/response/account/LoginResponse.java#L15) | LoginResponse · implementation | — |
| [src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java:107](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java#L107) | AccountServiceImpl · implementation | — |

**UI và giới hạn triển khai**

- Checkbox Ghi nhớ đăng nhập không được đọc; không có refresh token.
- UI: Identifier/password; apiFetch attaches X-Requested-With and backend login issues JWT cookie. UI checks res.isEmployee, but LoginResponse has accountType/redirectUrl and no isEmployee; UI ignores backend redirectUrl, so default employee login navigates / rather than /admin (unless request redirect parameter itself /admin). Remember checkbox unread.

<a id="uc003"></a>

## UC003 — Đăng xuất

**Primary Actor:** Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Tài khoản và xác thực  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng, Nhân viên: đăng xuất.

**Trigger:** Actor lựa chọn đăng xuất.

**Preconditions theo nhánh**

- Đăng xuất: Client có cookie (API cũng chấp nhận chưa đăng nhập).

**Main Success Scenario**

1. Người dùng yêu cầu kết thúc đăng nhập trên trình duyệt.
2. Hệ thống xóa cookie xác thực của trình duyệt và đưa người dùng về trang đăng nhập.

**Luồng lựa chọn / ngoại lệ**

- Đăng xuất: Bearer/JWT đã cấp không bị thu hồi phía server.

**Postconditions theo nhánh**

- Đăng xuất: Cookie client bị xóa; token đã sao chép có hiệu lực tới exp.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:48](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L48) | AuthRestController · logout | POST /api/auth/logout |
| [src/main/java/com/thinh/cosmetic/security/AuthCookieFactory.java:31](../../../../src/main/java/com/thinh/cosmetic/security/AuthCookieFactory.java#L31) | AuthCookieFactory · createLogoutCookie | — |
| [src/main/resources/static/js/api.js:512](../../../../src/main/resources/static/js/api.js#L512) | UI | — |
| [src/main/java/com/thinh/cosmetic/controller/AuthPageController.java:30](../../../../src/main/java/com/thinh/cosmetic/controller/AuthPageController.java#L30) | AuthPageController · implementation | — |

**UI và giới hạn triển khai**

- Hành vi ở AuthRestController.logout/AuthCookieFactory.createLogoutCookie, không có AccountServiceImpl.logout. Spring Security cũng có /logout.
- UI: LUNEA.logout POST with credentials/include header then full GET /logout; AuthPageController evicts JWT cookie/clears context/session.

<a id="uc004"></a>

## UC004 — Đặt lại mật khẩu

**Primary Actor:** Khách vãng lai  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Tài khoản và xác thực  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách vãng lai: đặt lại mật khẩu.

**Trigger:** Actor lựa chọn đặt lại mật khẩu.

**Preconditions theo nhánh**

- Đặt lại mật khẩu: Account tồn tại; newPassword tối thiểu 6 ký tự; API public.

**Main Success Scenario**

1. Người dùng cung cấp email/số điện thoại của tài khoản và mật khẩu mới.
2. Hệ thống tìm tài khoản, kiểm tra mật khẩu mới có ít nhất 6 ký tự.
3. Hệ thống lưu mật khẩu mới dưới dạng băm và thông báo kết quả.

**Luồng lựa chọn / ngoại lệ**

- Đặt lại mật khẩu: Không tìm thấy tài khoản: 404; identifier trống: 400.; UI fetch thiếu X-Requested-With nên filter trả 403 trước controller.; Không yêu cầu OTP, reset token hay chứng minh quyền sở hữu.

**Postconditions theo nhánh**

- Đặt lại mật khẩu: Nếu gọi API đúng header, hash mật khẩu đổi; UI hiện tại không hoàn thành.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:56](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L56) | AuthRestController · resetPassword | POST /api/auth/reset-password |
| [src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java:123](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java#L123) | AccountServiceImpl · resetPassword | — |
| [src/main/resources/templates/auth/login.html:213](../../../../src/main/resources/templates/auth/login.html#L213) | UI | — |
| [src/main/resources/templates/auth/login.html:239](../../../../src/main/resources/templates/auth/login.html#L239) | UI | — |
| [src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java:36](../../../../src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java#L36) | XRequestedWithFilter · implementation | — |

**UI và giới hạn triển khai**

- UI: Modal sends emailOrPhone/newPassword, confirm local. Raw fetch omits X-Requested-With, filter rejects 403. Backend does not use Email/OTP actor.
- Đổi mật khẩu không thu hồi JWT hiện có; token tiếp tục tới exp.

<a id="uc005"></a>

## UC005 — Quản lý hồ sơ cá nhân

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Hồ sơ khách hàng  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: quản lý hồ sơ cá nhân.

**Trigger:** Actor lựa chọn quản lý hồ sơ cá nhân.

**Preconditions theo nhánh**

- Xem hồ sơ khách hàng: Customer id tồn tại; trang /account/profile yêu cầu ROLE_CUSTOMER nhưng API public.
- Cập nhật hồ sơ khách hàng: Customer tồn tại; fullName hợp lệ theo CustomerProfileRequest.

**Main Success Scenario**

1. Khách hàng yêu cầu xem hồ sơ gồm họ tên, ngày sinh, giới tính, số điện thoại và điểm tích lũy.
2. Khách hàng lựa chọn xem hoặc cập nhật họ tên, ngày sinh, giới tính, số điện thoại.
3. Hệ thống kiểm tra dữ liệu, lưu phần được cập nhật và trả hồ sơ hiện hành.

**Luồng lựa chọn / ngoại lệ**

- Xem hồ sơ khách hàng: Id không tồn tại: generic Exception được trả INTERNAL_ERROR 500.; API không kiểm ownership, cho phép đọc customer bất kỳ.
- Cập nhật hồ sơ khách hàng: API public, không xác minh customerId thuộc principal.; Phone trùng chỉ bị ràng buộc DB; CustomerMapper không map Account.phone vào CustomerResponse.phone.

**Postconditions theo nhánh**

- Xem hồ sơ khách hàng: Không đổi dữ liệu.
- Cập nhật hồ sơ khách hàng: Thông tin đã lưu; phone có thể không xuất hiện trong response.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:30](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L30) | CustomerRestController · getById | GET /api/customers/{id} |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:125](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L125) | CustomerServiceImpl · getCustomerById | — |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:35](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L35) | AccountPageController · implementation | — |
| [src/main/resources/templates/account/profile.html:80](../../../../src/main/resources/templates/account/profile.html#L80) | UI | — |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:38](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L38) | CustomerServiceImpl · getProfile | — |
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:35](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L35) | CustomerRestController · updateProfile | PUT /api/customers/{id}/profile |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:42](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L42) | CustomerServiceImpl · updateProfile | — |
| [src/main/resources/templates/account/profile.html:324](../../../../src/main/resources/templates/account/profile.html#L324) | UI | — |
| [src/main/resources/templates/account/profile.html:334](../../../../src/main/resources/templates/account/profile.html#L334) | UI | — |

**UI và giới hạn triển khai**

- UI: SSR calls customerService.getProfile and getAddresses; displays loyaltyPoints only (no redemption/accrual UI). Controller falls back customerId=1 when principal ID missing.
- CustomerMapper chỉ mapping account.id và account.email; CustomerEntity không có phone -> CustomerResponse.phone không map.
- UI: Full name/phone/dob/gender; email disabled, no password-change form.

<a id="uc006"></a>

## UC006 — Quản lý địa chỉ nhận hàng

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Hồ sơ khách hàng  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: quản lý địa chỉ nhận hàng.

**Trigger:** Actor lựa chọn quản lý địa chỉ nhận hàng.

**Preconditions theo nhánh**

- Xem sổ địa chỉ: Trang hồ sơ cần ROLE_CUSTOMER; API public.
- Thêm địa chỉ nhận hàng: Customer tồn tại; recipientName/phone/province/ward/addressDetail hợp lệ.
- Cập nhật địa chỉ nhận hàng: Address id tồn tại; request hợp lệ.
- Xóa địa chỉ nhận hàng: Address id được chọn; API public.
- Đặt địa chỉ nhận hàng mặc định: Address id tồn tại; request hợp lệ.

**Main Success Scenario**

1. Khách hàng yêu cầu xem các địa chỉ nhận hàng đã lưu.
2. Khách hàng chọn thêm, sửa, xóa hoặc đặt một địa chỉ làm mặc định.
3. Với thêm/sửa, khách cung cấp người nhận, số điện thoại, tỉnh, quận, phường và địa chỉ chi tiết.
4. Hệ thống thực hiện công việc được chọn và trả kết quả cập nhật sổ địa chỉ.

**Luồng lựa chọn / ngoại lệ**

- Xem sổ địa chỉ: Id không có địa chỉ trả danh sách rỗng.; API không kiểm ownership.
- Thêm địa chỉ nhận hàng: Không có customer: 500; trường bắt buộc sai: 400.; isDefault bị bỏ qua/null có thể vi phạm Entity @NotNull; UI gửi boolean.; Không kiểm ownership.
- Cập nhật địa chỉ nhận hàng: Không so sánh address.customer với customerId; không kiểm principal.; Cập nhật isDefault=true không bỏ mặc định các địa chỉ khác.; UI không nạp lại tỉnh/quận/phường khi mở edit, có thể gửi sai.
- Xóa địa chỉ nhận hàng: customerId bị bỏ qua, không kiểm ownership.; Xóa địa chỉ mặc định không chọn một địa chỉ khác làm mặc định.
- Đặt địa chỉ nhận hàng mặc định: Không so sánh địa chỉ với customerId hoặc principal; API công khai.; Có thể tồn tại nhiều địa chỉ isDefault=true vì update không reset các địa chỉ còn lại.; Địa chỉ không tồn tại: UI không tìm được target hoặc API trả lỗi.

**Postconditions theo nhánh**

- Xem sổ địa chỉ: Không đổi dữ liệu.
- Thêm địa chỉ nhận hàng: Địa chỉ mới được lưu; khi thêm mặc định, các địa chỉ cũ bị bỏ mặc định.
- Cập nhật địa chỉ nhận hàng: Địa chỉ thay đổi; tính duy nhất địa chỉ mặc định chưa bảo đảm.
- Xóa địa chỉ nhận hàng: Địa chỉ bị xóa nếu tồn tại.
- Đặt địa chỉ nhận hàng mặc định: Địa chỉ được chọn có isDefault=true; có thể tồn tại nhiều địa chỉ mặc định.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:43](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L43) | CustomerRestController · getAddresses | GET /api/customers/{id}/addresses |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:55](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L55) | CustomerServiceImpl · getAddresses | — |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:43](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L43) | AccountPageController · implementation | — |
| [src/main/resources/templates/account/profile.html:402](../../../../src/main/resources/templates/account/profile.html#L402) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:48](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L48) | CustomerRestController · addAddress | POST /api/customers/{id}/addresses |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:61](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L61) | CustomerServiceImpl · addAddress | — |
| [src/main/resources/templates/account/profile.html:384](../../../../src/main/resources/templates/account/profile.html#L384) | UI | — |
| [src/main/resources/templates/account/profile.html:448](../../../../src/main/resources/templates/account/profile.html#L448) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:56](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L56) | CustomerRestController · updateAddress | PUT /api/customers/{id}/addresses/{addressId} |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:73](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L73) | CustomerServiceImpl · updateAddress | — |
| [src/main/resources/templates/account/profile.html:398](../../../../src/main/resources/templates/account/profile.html#L398) | UI | — |
| [src/main/resources/templates/account/profile.html:441](../../../../src/main/resources/templates/account/profile.html#L441) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:65](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L65) | CustomerRestController · deleteAddress | DELETE /api/customers/{id}/addresses/{addressId} |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:81](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L81) | CustomerServiceImpl · deleteAddress | — |
| [src/main/resources/templates/account/profile.html:186](../../../../src/main/resources/templates/account/profile.html#L186) | UI | — |
| [src/main/resources/templates/account/profile.html:482](../../../../src/main/resources/templates/account/profile.html#L482) | UI | — |
| [src/main/resources/templates/account/profile.html:464](../../../../src/main/resources/templates/account/profile.html#L464) | UI | — |

**UI và giới hạn triển khai**

- UI: Initial SSR addresses; edit/default actions fetch actual list.
- UI: Recipient/phone/province/district/ward/detail/default from modal. Static locations includes 5 provinces but UI select only HCM/HN/DN.
- UI: Edit fetch only fills recipient/phone/detail/default; province/district/ward not prefilled from existing record, can save stale controls.
- UI: Confirmation; UI hides delete for default address, non-default card removed on success.
- UI: Fetch list, set target.isDefault=true, submit whole target. No dedicated endpoint.

<a id="uc007"></a>

## UC007 — Quản lý hồ sơ làm đẹp

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Hồ sơ khách hàng  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: quản lý hồ sơ làm đẹp.

**Trigger:** Actor lựa chọn quản lý hồ sơ làm đẹp.

**Preconditions theo nhánh**

- Xem hồ sơ làm đẹp: Trang cần customer; API public.
- Cập nhật hồ sơ làm đẹp: Customer tồn tại; API public.

**Main Success Scenario**

1. Khách hàng yêu cầu xem loại da, vấn đề da, nhu cầu chăm sóc, sở thích và khoảng giá quan tâm.
2. Khách hàng có thể cung cấp bộ giá trị mới cho hồ sơ làm đẹp.
3. Hệ thống tạo hồ sơ khi chưa có hoặc cập nhật hồ sơ hiện có và ghi thời điểm cập nhật.

**Luồng lựa chọn / ngoại lệ**

- Xem hồ sơ làm đẹp: Chưa có profile: response null, UI cho nhập mới.; Không xác minh chủ thể sở hữu.
- Cập nhật hồ sơ làm đẹp: Customer không tồn tại: 500.; DTO không có Bean Validation; entity có giới hạn kích thước, có thể lỗi khi lưu.; Field null ghi đè dữ liệu cũ; không kiểm ownership.

**Postconditions theo nhánh**

- Xem hồ sơ làm đẹp: Không đổi dữ liệu.
- Cập nhật hồ sơ làm đẹp: BeautyProfile được lưu; không có recommendation service.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:71](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L71) | CustomerRestController · getBeautyProfile | GET /api/customers/{id}/beauty-profile |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:87](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L87) | CustomerServiceImpl · getBeautyProfile | — |
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:76](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L76) | CustomerRestController · updateBeautyProfile | PUT /api/customers/{id}/beauty-profile |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:99](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L99) | CustomerServiceImpl · updateBeautyProfile | — |

**UI và giới hạn triển khai**


<a id="uc008"></a>

## UC008 — Tra cứu khách hàng

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Hồ sơ khách hàng  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: tra cứu khách hàng.

**Trigger:** Actor lựa chọn tra cứu khách hàng.

**Preconditions theo nhánh**

- Tra cứu danh sách khách hàng: API public, không cần CUSTOMER_MANAGE.

**Main Success Scenario**

1. Nhân viên yêu cầu danh sách khách hàng.
2. Hệ thống trả họ tên, email, ngày sinh, giới tính, điểm tích lũy và ngày tham gia của các khách hàng.

**Luồng lựa chọn / ngoại lệ**

- Tra cứu danh sách khách hàng: Có thể rỗng; không filter phạm vi và không kiểm quyền.

**Postconditions theo nhánh**

- Tra cứu danh sách khách hàng: Không đổi dữ liệu.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:25](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L25) | CustomerRestController · getAll | GET /api/customers |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:119](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L119) | CustomerServiceImpl · getAllCustomers | — |

**UI và giới hạn triển khai**


<a id="uc009"></a>

## UC009 — Quản lý nhân viên và phân công

**Primary Actor:** Quản trị viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Quản lý nhân viên  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Quản trị viên: quản lý nhân viên và phân công.

**Trigger:** Actor lựa chọn quản lý nhân viên và phân công.

**Preconditions theo nhánh**

- Tra cứu nhân viên: API public; id tồn tại đối với chi tiết.
- Tạo tài khoản nhân viên: email,password,fullName hợp lệ DTO; internalEmail cần cung cấp để service tạo username không rỗng.
- Cập nhật nhân viên và phân công: Employee tồn tại; REST dùng EmployeeRequest bắt buộc email/password/fullName ngay cả update.
- Ngừng hoạt động nhân viên: Employee tồn tại; API public.

**Main Success Scenario**

1. Quản trị viên chọn tra cứu, tạo, cập nhật hoặc ngừng hoạt động nhân viên.
2. Khi tạo/cập nhật, quản trị viên cung cấp họ tên, email nội bộ, số điện thoại, mật khẩu và vai trò/cửa hàng được phân công.
3. Hệ thống thực hiện nhánh công việc tương ứng; các vai trò và cửa hàng hợp lệ được liên kết với nhân viên.
4. Hệ thống trả nhân viên cùng tên vai trò, cửa hàng và trạng thái hiện hành.

**Luồng lựa chọn / ngoại lệ**

- Tra cứu nhân viên: Id không tồn tại: 500 generic Exception.; Không EMPLOYEE_MANAGE; không phân trang.
- Tạo tài khoản nhân viên: DTO bắt buộc email nhưng service bỏ qua email; internalEmail không bắt buộc nên có thể lỗi DB/validation.; Role/store id không tồn tại bị bỏ qua im lặng.; Fallback password 123456 trong service không đi qua REST @Valid nếu password trống.; API public cho phép gán ADMIN nếu biết roleId.
- Cập nhật nhân viên và phân công: Không đổi internalEmail/email/username dù DTO nhận.; Employee.phone đổi nhưng Account.phone không đồng bộ.; ID không tồn tại bị bỏ qua; danh sách rỗng xóa toàn bộ phân công.; API public; JWT đã phát giữ authorities cũ tới hết hạn.
- Ngừng hoạt động nhân viên: Không đặt Account.status INACTIVE; AppUserDetailsService chỉ kiểm account, nhân viên vẫn đăng nhập được.; Không thu hồi JWT hiện hành.

**Postconditions theo nhánh**

- Tra cứu nhân viên: Không đổi dữ liệu.
- Tạo tài khoản nhân viên: Nếu dữ liệu đáp ứng cả DTO/entity, tạo employee/account và các liên kết.
- Cập nhật nhân viên và phân công: Thông tin/phân công được lưu; quyền mới chỉ nạp khi đăng nhập lại.
- Ngừng hoạt động nhân viên: Employee INACTIVE; không bảo đảm khóa tài khoản đăng nhập.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java#L31) | EmployeeRestController · getAll / getById | GET /api/employees; GET /api/employees/{id} |
| [src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java:92](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java#L92) | EmployeeServiceImpl · getAll/getById | — |
| [src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java#L21) | EmployeeRestController · create | POST /api/employees |
| [src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java:32](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java#L32) | EmployeeServiceImpl · create | — |
| [src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java:36](../../../../src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java#L36) | EmployeeRestController · update | PUT /api/employees/{id} |
| [src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java:97](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java#L97) | EmployeeServiceImpl · update | — |
| [src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java:44](../../../../src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java#L44) | EmployeeRestController · deactivate | DELETE /api/employees/{id} |
| [src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java:133](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java#L133) | EmployeeServiceImpl · deactivate | — |

**UI và giới hạn triển khai**


<a id="uc010"></a>

## UC010 — Tra cứu mỹ phẩm

**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Sản phẩm và tra cứu mỹ phẩm  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách vãng lai, Khách hàng, Nhân viên: tra cứu mỹ phẩm.

**Trigger:** Actor lựa chọn tra cứu mỹ phẩm.

**Preconditions theo nhánh**

- Xem danh sách sản phẩm: Không yêu cầu đăng nhập.
- Tìm kiếm và lọc sản phẩm: Không yêu cầu đăng nhập.

**Main Success Scenario**

1. Người dùng yêu cầu danh sách mỹ phẩm hoặc cung cấp từ khóa, thương hiệu, danh mục, loại da, nhu cầu, khoảng giá và cách sắp xếp.
2. Hệ thống tìm các sản phẩm phù hợp và bổ sung ảnh, biến thể, giá cùng tồn khả dụng.
3. Hệ thống trả danh sách kết quả; không có kết quả thì trả danh sách rỗng.

**Luồng lựa chọn / ngoại lệ**

- Xem danh sách sản phẩm: Không có ảnh: dùng ảnh chính, ảnh đầu theo sortOrder hoặc URL Unsplash mặc định.; Không có SKU: giá min/max bằng 0, danh sách SKU rỗng.; Không có tồn: tồn khả dụng bằng 0.
- Tìm kiếm và lọc sản phẩm: Không có kết quả: trả danh sách rỗng.; priceRange không nhận diện: không lọc giá.; sortBy không nhận diện: sắp xếp ID giảm dần.

**Postconditions theo nhánh**

- Xem danh sách sản phẩm: Dữ liệu không thay đổi.
- Tìm kiếm và lọc sản phẩm: Kết quả truy vấn được hiển thị; dữ liệu không thay đổi.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:42](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L42) | ProductRestController · getAll | GET /api/products |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:88](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L88) | ProductServiceImpl · getAll / enrich | — |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:27](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L27) | HomeController · home | GET / |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:29](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L29) | ProductPageController · productList | GET /products; GET /product |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:27](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L27) | ProductPageController · implementation | — |
| [src/main/resources/templates/product-list.html:37](../../../../src/main/resources/templates/product-list.html#L37) | UI | — |
| [src/main/resources/templates/fragments/header.html:67](../../../../src/main/resources/templates/fragments/header.html#L67) | UI | — |
| [src/main/resources/templates/admin/products.html:120](../../../../src/main/resources/templates/admin/products.html#L120) | UI | — |
| [src/main/resources/templates/admin/products.html:170](../../../../src/main/resources/templates/admin/products.html#L170) | UI | — |
| [src/main/resources/templates/admin/products.html:194](../../../../src/main/resources/templates/admin/products.html#L194) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:42](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L42) | ProductRestController · getAll | GET /api/products?keyword=&brandId=&categoryId=&skinType=&concern=&priceRange=&sortBy= |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:97](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L97) | ProductServiceImpl · searchAndFilter / matchesKeyword / matchesSkinType / matchesConcern / matchesPriceRange / getComparator | — |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:29](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L29) | ProductPageController · productList | GET /products |

**UI và giới hạn triển khai**

- Không lọc ACTIVE cho sản phẩm, SKU, thương hiệu, danh mục hoặc cửa hàng.
- Không phân trang backend; tham số page của page controller không được áp dụng.
- UI: SSR: ProductPageController.productList calls productService.searchAndFilter(keyword, brandId, categoryId, skinType, concern, priceRange, sortBy). Không fetch API public catalog trên storefront. page parameter chỉ đưa vào Model, không paginate.
- UI: Fetch public reads, client filter name/brand/category/origin, KPI total products/SKUs/featured/ACTIVE, show prices/stock/status/link storefront. No editing actions/forms/modal.
- under300 và under500 cùng nghĩa giá < 500.000; 300to600 và 500to1000 cùng nghĩa 500.000–1.000.000; above1000 > 1.000.000.
- Category filter không mở rộng xuống các danh mục con.
- Tên thuộc tính được dùng cho skinTypes/concerns; value của ProductAttributeValue không dùng; các nhóm ngoài SKIN_TYPE đều vào concerns.

<a id="uc011"></a>

## UC011 — Xem chi tiết mỹ phẩm

**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Sản phẩm và tra cứu mỹ phẩm  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách vãng lai, Khách hàng, Nhân viên: xem chi tiết mỹ phẩm.

**Trigger:** Actor lựa chọn xem chi tiết mỹ phẩm.

**Preconditions theo nhánh**

- Xem chi tiết sản phẩm: ID sản phẩm tồn tại; không yêu cầu đăng nhập.

**Main Success Scenario**

1. Người dùng chọn một sản phẩm.
2. Hệ thống trả mô tả, công dụng, thành phần, thương hiệu, danh mục, hình ảnh và các biến thể.
3. Người dùng có thể chọn biến thể để xem giá và mã tương ứng.

**Luồng lựa chọn / ngoại lệ**

- Xem chi tiết sản phẩm: Sản phẩm không tồn tại: API ném checked Exception và được trả HTTP 500; page chuyển /error/404.; Không có ảnh/SKU/stock: dùng các giá trị fallback như danh sách.

**Postconditions theo nhánh**

- Xem chi tiết sản phẩm: Thông tin chi tiết được hiển thị.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:33](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L33) | ProductRestController · getById | GET /api/products/{id} |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:77](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L77) | ProductServiceImpl · getById / enrich | — |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:66](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L66) | ProductPageController · productDetail | GET /products/{id}; GET /product/{id} |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:63](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L63) | ProductPageController · implementation | — |
| [src/main/resources/templates/product-detail.html:23](../../../../src/main/resources/templates/product-detail.html#L23) | UI | — |
| [src/main/resources/templates/product-detail.html:295](../../../../src/main/resources/templates/product-detail.html#L295) | UI | — |

**UI và giới hạn triển khai**

- Related products chỉ theo brand/category, không có tư vấn AI hay cá nhân hóa; không suy diễn thêm actor external.
- UI: SSR productService.getById; selected SKU updates price and code locally. Related products actually same category OR brand, limit 4; no BeautyProfile personalization.

<a id="uc012"></a>

## UC012 — Quản lý sản phẩm

**Primary Actor:** Nhân viên quản lý sản phẩm, Quản trị viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Sản phẩm và tra cứu mỹ phẩm  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên quản lý sản phẩm, Quản trị viên: quản lý sản phẩm.

**Trigger:** Actor lựa chọn quản lý sản phẩm.

**Preconditions theo nhánh**

- Thêm sản phẩm: Payload có brandId, categoryId; tên không trống ≤ 200 ký tự; origin ≤ 100; brand/category tồn tại.; Mutation API có header X-Requested-With phù hợp hoặc dùng Bearer token.
- Cập nhật sản phẩm: Sản phẩm và brand/category tồn tại; validation như thêm sản phẩm.; Header mutation phù hợp.
- Xóa sản phẩm: Sản phẩm tồn tại; header mutation phù hợp.

**Main Success Scenario**

1. Nhân viên quản lý sản phẩm hoặc quản trị viên chọn thêm, cập nhật hoặc xóa sản phẩm.
2. Với thêm/cập nhật, người thực hiện cung cấp tên, thương hiệu, danh mục, xuất xứ, thành phần, công dụng, mô tả và trạng thái.
3. Hệ thống kiểm tra các dữ liệu liên quan, thực hiện nhánh được chọn và trả kết quả.
4. Việc quản lý này chỉ tác động bản ghi sản phẩm; chưa có thao tác quản lý SKU, ảnh hoặc thuộc tính.

**Luồng lựa chọn / ngoại lệ**

- Thêm sản phẩm: Request không hợp lệ: HTTP 400.; Brand/category không tồn tại: checked Exception, HTTP 500.; Nếu status hoặc isFeatured thành null sau mapping: entity @NotNull ngăn lưu; cần đối chiếu generated mapper/runtime.
- Cập nhật sản phẩm: Không có sản phẩm, brand hoặc category: checked Exception, HTTP 500.; Request không hợp lệ hoặc entity sau mapping vi phạm @NotNull: HTTP 400.
- Xóa sản phẩm: Sản phẩm không tồn tại: checked Exception, HTTP 500.; Quan hệ SKU, ảnh, attribute hoặc các dữ liệu khác còn tham chiếu có thể gây lỗi ràng buộc dữ liệu HTTP 409.

**Postconditions theo nhánh**

- Thêm sản phẩm: Một sản phẩm mới được lưu; không tạo SKU, ảnh, tồn kho hay thuộc tính.
- Cập nhật sản phẩm: Thông tin sản phẩm được cập nhật.
- Xóa sản phẩm: Bản ghi sản phẩm bị xóa khi không bị ràng buộc cản trở.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:24](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L24) | ProductRestController · create | POST /api/products |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:49](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L49) | ProductServiceImpl · create | — |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java:26](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java#L26) | ProductMapper · toEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:62](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L62) | ProductRestController · update | PUT /api/products/{id} |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:263](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L263) | ProductServiceImpl · update | — |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java:34](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java#L34) | ProductMapper · updateEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:72](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L72) | ProductRestController · delete | DELETE /api/products/{id} |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:296](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L296) | ProductServiceImpl · delete | — |

**UI và giới hạn triển khai**

- Không có xác thực/permission cho endpoint; truy cập công khai được ghi trong báo cáo quyền.
- ProductRequest không chứa SKU, ảnh hoặc attribute values; không xem những phần này là được quản lý qua form sản phẩm.
- status=INACTIVE là khả năng trong update; không có endpoint riêng ngừng kinh doanh.
- Response của update không gọi enrich nên thiếu SKU/ảnh/giá/stock/attribute fields cho đến khi GET lại.
- NullValuePropertyMappingStrategy.IGNORE không được đặt cho ProductMapper; null có thể xóa optional fields.
- Xóa vật lý khác với update status=INACTIVE; không có cascade cleanup trong implementation.

<a id="uc013"></a>

## UC013 — Tra cứu thương hiệu và danh mục

**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Thương hiệu và danh mục  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách vãng lai, Khách hàng, Nhân viên: tra cứu thương hiệu và danh mục.

**Trigger:** Actor lựa chọn tra cứu thương hiệu và danh mục.

**Preconditions theo nhánh**

- Xem thương hiệu: Không yêu cầu đăng nhập.
- Xem danh mục sản phẩm: Không yêu cầu đăng nhập.

**Main Success Scenario**

1. Người dùng yêu cầu danh sách/chi tiết thương hiệu hoặc danh mục mỹ phẩm.
2. Hệ thống trả tên, mô tả, trạng thái và quan hệ danh mục cha nếu có.

**Luồng lựa chọn / ngoại lệ**

- Xem thương hiệu: ID không tồn tại: checked Exception, HTTP 500.; Không có thương hiệu: danh sách rỗng.
- Xem danh mục sản phẩm: ID không tồn tại: checked Exception, HTTP 500.; Không có danh mục: danh sách rỗng.

**Postconditions theo nhánh**

- Xem thương hiệu: Thông tin thương hiệu được hiển thị.
- Xem danh mục sản phẩm: Thông tin danh mục được hiển thị.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java:37](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java#L37) | BrandRestController · getAll / getById | GET /api/brands; GET /api/brands/{id} |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java:45](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java#L45) | BrandServiceImpl · getAll / getById | — |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:51](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L51) | HomeController · brands | GET /brands |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:49](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L49) | HomeController · implementation | — |
| [src/main/resources/templates/brands.html:29](../../../../src/main/resources/templates/brands.html#L29) | UI | — |
| [src/main/resources/templates/admin/products.html:120](../../../../src/main/resources/templates/admin/products.html#L120) | UI | — |
| [src/main/resources/templates/admin/products.html:170](../../../../src/main/resources/templates/admin/products.html#L170) | UI | — |
| [src/main/resources/templates/admin/products.html:194](../../../../src/main/resources/templates/admin/products.html#L194) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java:39](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java#L39) | CategoryRestController · getAll / getById | GET /api/categories; GET /api/categories/{id} |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java:53](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java#L53) | CategoryServiceImpl · getAll / getById | — |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java:18](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java#L18) | CategoryMapper · toResponse | — |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:27](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L27) | ProductPageController · implementation | — |
| [src/main/resources/templates/product-list.html:37](../../../../src/main/resources/templates/product-list.html#L37) | UI | — |
| [src/main/resources/templates/fragments/header.html:67](../../../../src/main/resources/templates/fragments/header.html#L67) | UI | — |

**UI và giới hạn triển khai**

- Không lọc ACTIVE.
- UI: HomeController loads brandService.getAll; links filter catalog by brandId.
- UI: Fetch public reads, client filter name/brand/category/origin, KPI total products/SKUs/featured/ACTIVE, show prices/stock/status/link storefront. No editing actions/forms/modal.
- Response là danh sách phẳng, không trả cây descendants; không lọc ACTIVE.
- UI: SSR: ProductPageController.productList calls productService.searchAndFilter(keyword, brandId, categoryId, skinType, concern, priceRange, sortBy). Không fetch API public catalog trên storefront. page parameter chỉ đưa vào Model, không paginate.

<a id="uc014"></a>

## UC014 — Quản lý thương hiệu

**Primary Actor:** Nhân viên quản lý sản phẩm, Quản trị viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Thương hiệu và danh mục  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên quản lý sản phẩm, Quản trị viên: quản lý thương hiệu.

**Trigger:** Actor lựa chọn quản lý thương hiệu.

**Preconditions theo nhánh**

- Thêm thương hiệu: Tên không trống ≤ 150; tên phải thỏa unique của Entity/DB.; Header mutation phù hợp.
- Cập nhật thương hiệu: Thương hiệu tồn tại; tên không trống ≤ 150; header mutation phù hợp.
- Xóa thương hiệu: Thương hiệu tồn tại; header mutation phù hợp.

**Main Success Scenario**

1. Người thực hiện chọn thêm, sửa hoặc xóa thương hiệu.
2. Với thêm/sửa, người thực hiện cung cấp tên, mô tả và trạng thái thương hiệu.
3. Hệ thống thực hiện nhánh đã chọn và trả thương hiệu hoặc xác nhận xóa thành công.

**Luồng lựa chọn / ngoại lệ**

- Thêm thương hiệu: Tên không hợp lệ: HTTP 400.; Tên trùng: ràng buộc unique, HTTP 409.; status null được DTO cho phép nhưng entity bắt buộc; kết quả phụ thuộc null mapping, không có default trong service.
- Cập nhật thương hiệu: ID không tồn tại: checked Exception, HTTP 500.; Tên trùng hoặc sai validation: HTTP 409/400.
- Xóa thương hiệu: ID không tồn tại: checked Exception, HTTP 500.; Sản phẩm đang tham chiếu có thể ngăn xóa: HTTP 409.

**Postconditions theo nhánh**

- Thêm thương hiệu: Thương hiệu mới được lưu.
- Cập nhật thương hiệu: Thương hiệu được cập nhật.
- Xóa thương hiệu: Thương hiệu bị xóa nếu không bị FK cản trở.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java#L21) | BrandRestController · create | POST /api/brands |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java:23](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java#L23) | BrandServiceImpl · create | — |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/BrandMapper.java:14](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/BrandMapper.java#L14) | BrandMapper · toEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java:42](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java#L42) | BrandRestController · update | PUT /api/brands/{id} |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java:53](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java#L53) | BrandServiceImpl · update | — |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/BrandMapper.java:22](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/BrandMapper.java#L22) | BrandMapper · updateEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java:50](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java#L50) | BrandRestController · delete | DELETE /api/brands/{id} |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java:67](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java#L67) | BrandServiceImpl · delete | — |

**UI và giới hạn triển khai**

- Không gọi kiểm tra tồn tại/tên trùng trong service; không có RBAC.
- Có thể đặt status=INACTIVE hoặc ACTIVE; description null không xóa giá trị cũ.
- Không là soft delete.

<a id="uc015"></a>

## UC015 — Quản lý danh mục sản phẩm

**Primary Actor:** Nhân viên quản lý sản phẩm, Quản trị viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Thương hiệu và danh mục  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên quản lý sản phẩm, Quản trị viên: quản lý danh mục sản phẩm.

**Trigger:** Actor lựa chọn quản lý danh mục sản phẩm.

**Preconditions theo nhánh**

- Thêm danh mục sản phẩm: Tên không trống ≤ 150; description ≤ 500; parentId nếu có phải tồn tại.; Header mutation phù hợp.
- Cập nhật danh mục sản phẩm: Danh mục tồn tại; tên không trống ≤ 150; description ≤ 500.; Header mutation phù hợp.
- Xóa danh mục sản phẩm: Header mutation phù hợp; danh mục không bị FK cản trở.

**Main Success Scenario**

1. Người thực hiện chọn thêm, sửa hoặc xóa danh mục.
2. Với thêm/sửa, người thực hiện cung cấp tên, mô tả, trạng thái và danh mục cha nếu cần.
3. Hệ thống thực hiện nhánh được chọn và trả kết quả; việc đổi danh mục cha khi sửa hiện chưa hoạt động.

**Luồng lựa chọn / ngoại lệ**

- Thêm danh mục sản phẩm: Cha không tồn tại: RuntimeException, HTTP 500.; Validation không đạt: HTTP 400.; status null được request cho phép nhưng entity yêu cầu; service không tự default.
- Cập nhật danh mục sản phẩm: Không có category: checked Exception, HTTP 500.; Validation không đạt: HTTP 400.; Gửi parentId mới hoặc null: quan hệ cha vẫn giữ nguyên.
- Xóa danh mục sản phẩm: Danh mục cha có con hoặc danh mục có sản phẩm tham chiếu có thể bị FK ngăn xóa: HTTP 409.; Không kiểm tra tồn tại riêng trước deleteById.

**Postconditions theo nhánh**

- Thêm danh mục sản phẩm: Danh mục mới gốc/con được lưu.
- Cập nhật danh mục sản phẩm: Tên/mô tả/status được cập nhật; parent không đổi.
- Xóa danh mục sản phẩm: Danh mục được xóa khi thao tác thành công.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java#L21) | CategoryRestController · create | POST /api/categories |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java:23](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java#L23) | CategoryServiceImpl · create | — |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java:15](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java#L15) | CategoryMapper · toEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java:46](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java#L46) | CategoryRestController · update | PUT /api/categories/{id} |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java:61](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java#L61) | CategoryServiceImpl · update | — |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java:25](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java#L25) | CategoryMapper · updateEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java:56](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java#L56) | CategoryRestController · delete | DELETE /api/categories/{id} |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java:75](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java#L75) | CategoryServiceImpl · delete | — |

**UI và giới hạn triển khai**

- Không có nghiệp vụ quản lý cây đầy đủ hay endpoint di chuyển cây; chỉ gán cha khi create.
- Khoảng trống thực tế: chưa hỗ trợ chuyển danh mục cha hoặc tách thành danh mục gốc; không đánh dấu quản lý hierarchy đầy đủ.
- Không cascade chuyển sản phẩm/danh mục con hoặc soft deactivate.

<a id="uc016"></a>

## UC016 — Tra cứu cửa hàng

**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Cửa hàng  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách vãng lai, Khách hàng, Nhân viên: tra cứu cửa hàng.

**Trigger:** Actor lựa chọn tra cứu cửa hàng.

**Preconditions theo nhánh**

- Xem hệ thống cửa hàng: Không yêu cầu đăng nhập.

**Main Success Scenario**

1. Người dùng yêu cầu danh sách hoặc chi tiết cửa hàng trong chuỗi.
2. Hệ thống trả tên, địa chỉ, tỉnh, số điện thoại, giờ mở/đóng cửa và trạng thái cửa hàng.

**Luồng lựa chọn / ngoại lệ**

- Xem hệ thống cửa hàng: ID không tồn tại: checked Exception, HTTP 500.; Không có cửa hàng: danh sách rỗng.

**Postconditions theo nhánh**

- Xem hệ thống cửa hàng: Thông tin cửa hàng được hiển thị.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java#L31) | StoreRestController · getAll / getById | GET /api/stores; GET /api/stores/{id} |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java:39](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java#L39) | StoreServiceImpl · getAll / getById | — |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:40](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L40) | HomeController · stores | GET /stores |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:38](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L38) | HomeController · implementation | — |
| [src/main/resources/templates/stores.html:29](../../../../src/main/resources/templates/stores.html#L29) | UI | — |

**UI và giới hạn triển khai**

- findAll trả cả INACTIVE; không có tìm kiếm backend theo tỉnh/khoảng cách.
- operatingHours trong Response không có nguồn field Entity.
- UI: HomeController loads storeService.getAll; cards show province/name/address/phone/hours. Không map/booking.

<a id="uc017"></a>

## UC017 — Quản lý cửa hàng

**Primary Actor:** Quản trị viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Cửa hàng  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Quản trị viên: quản lý cửa hàng.

**Trigger:** Actor lựa chọn quản lý cửa hàng.

**Preconditions theo nhánh**

- Thêm cửa hàng: Tên ≤ 150, địa chỉ ≤ 255, tỉnh ≤ 100 đều không trống; phone ≤ 20.; Header mutation phù hợp.
- Cập nhật cửa hàng: Cửa hàng tồn tại; validation giống thêm cửa hàng; header mutation phù hợp.
- Ngừng hoạt động cửa hàng: Cửa hàng tồn tại; header mutation phù hợp.

**Main Success Scenario**

1. Quản trị viên chọn thêm, cập nhật hoặc ngừng hoạt động cửa hàng.
2. Khi thêm/cập nhật, quản trị viên cung cấp tên, địa chỉ, tỉnh, số điện thoại, giờ mở/đóng cửa và tọa độ.
3. Hệ thống lưu dữ liệu mới hoặc đổi trạng thái cửa hàng sang INACTIVE, rồi trả kết quả.

**Luồng lựa chọn / ngoại lệ**

- Thêm cửa hàng: Request sai validation/định dạng thời gian: HTTP 400.
- Cập nhật cửa hàng: Không có cửa hàng: checked Exception, HTTP 500.; Request sai validation: HTTP 400.; operatingHours được gửi nhưng không có nơi lưu.
- Ngừng hoạt động cửa hàng: Không có cửa hàng: checked Exception, HTTP 500.

**Postconditions theo nhánh**

- Thêm cửa hàng: Cửa hàng mới được lưu.
- Cập nhật cửa hàng: Thông tin cửa hàng được cập nhật.
- Ngừng hoạt động cửa hàng: Cửa hàng có status INACTIVE.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java#L21) | StoreRestController · create | POST /api/stores |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java:24](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java#L24) | StoreServiceImpl · create | — |
| [src/main/java/com/thinh/cosmetic/mapper/store/StoreMapper.java:11](../../../../src/main/java/com/thinh/cosmetic/mapper/store/StoreMapper.java#L11) | StoreMapper · toEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java:36](../../../../src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java#L36) | StoreRestController · update | PUT /api/stores/{id} |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java:44](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java#L44) | StoreServiceImpl · update | — |
| [src/main/java/com/thinh/cosmetic/mapper/store/StoreMapper.java:17](../../../../src/main/java/com/thinh/cosmetic/mapper/store/StoreMapper.java#L17) | StoreMapper · updateEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java:44](../../../../src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java#L44) | StoreRestController · deactivate | DELETE /api/stores/{id} |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java:52](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java#L52) | StoreServiceImpl · deactivate | — |

**UI và giới hạn triển khai**

- operatingHours là field DTO nhưng Entity không có; chỉ openTime/closeTime được lưu.
- Không tạo tồn kho hay nhân viên gán cửa hàng tự động.
- Có thể kích hoạt lại qua status=ACTIVE.
- Không kiểm tra openTime/closeTime hợp lệ theo nghiệp vụ hoặc lat/long range.
- Endpoint DELETE nhưng không xóa vật lý.
- Không tự ẩn trong StoreService.getAll, public stock, ProductService.enrich; không xử lý đơn/tồn/nhân viên hiện có.

<a id="uc018"></a>

## UC018 — Kiểm tra còn hàng theo cửa hàng

**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Tồn kho  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách vãng lai, Khách hàng, Nhân viên: kiểm tra còn hàng theo cửa hàng.

**Trigger:** Actor lựa chọn kiểm tra còn hàng theo cửa hàng.

**Preconditions theo nhánh**

- Tra cứu tồn sản phẩm theo cửa hàng: Không yêu cầu đăng nhập; skuId được truyền trên URL.

**Main Success Scenario**

1. Người dùng chọn một biến thể mỹ phẩm cần kiểm tra.
2. Hệ thống lấy tồn thực tế và lượng đang giữ của biến thể tại từng cửa hàng.
3. Hệ thống trả cửa hàng, tồn khả dụng và thông tin còn hàng; bước này không giữ hàng.

**Luồng lựa chọn / ngoại lệ**

- Tra cứu tồn sản phẩm theo cửa hàng: skuId không tồn tại: không kiểm tra SKU, vẫn trả mọi cửa hàng với số lượng 0.

**Postconditions theo nhánh**

- Tra cứu tồn sản phẩm theo cửa hàng: Stock snapshot theo cửa hàng được hiển thị.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:83](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L83) | ProductRestController · getSkuStoreStock | GET /api/products/sku/{skuId}/stores |
| [src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java:87](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java#L87) | InventoryServiceImpl · getStoreStockBySku | — |
| [src/main/resources/templates/product-detail.html:357](../../../../src/main/resources/templates/product-detail.html#L357) | UI | — |
| [src/main/resources/templates/product-detail.html:383](../../../../src/main/resources/templates/product-detail.html#L383) | UI | — |

**UI và giới hạn triển khai**

- Trả cả cửa hàng INACTIVE và actual/held nội bộ; không chỉ available.
- Inventory trùng store-SKU chọn bản ghi đầu qua merge (a,b)->a.
- UI: Modal per selectedSkuId displays storeName/address/phone/inStock/availableStock. Không giữ hàng.

<a id="uc019"></a>

## UC019 — Tra cứu tồn kho

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Tồn kho  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: tra cứu tồn kho.

**Trigger:** Actor lựa chọn tra cứu tồn kho.

**Preconditions theo nhánh**

- Xem tồn kho cửa hàng: Token hợp lệ có ROLE_EMPLOYEE theo SecurityConfig.; API không yêu cầu cửa hàng thuộc phạm vi của nhân viên.

**Main Success Scenario**

1. Nhân viên chọn cửa hàng cần tra cứu tồn kho.
2. Hệ thống trả các SKU cùng tồn thực tế, lượng đang giữ, tồn khả dụng và ngưỡng tối thiểu.

**Luồng lựa chọn / ngoại lệ**

- Xem tồn kho cửa hàng: Store hoặc SKU không có inventory: list rỗng hoặc /available trả 0; detail ném checked Exception HTTP 500.; Chưa xác thực/không ROLE_EMPLOYEE: HTTP 401/403.

**Postconditions theo nhánh**

- Xem tồn kho cửa hàng: Stock snapshot được hiển thị.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- /api/inventory/** requires ROLE_EMPLOYEE

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java:20](../../../../src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java#L20) | InventoryRestController · getByStore / getByStoreAndSku / getAvailableStock | GET /api/inventory/store/{storeId}; GET /api/inventory/store/{storeId}/sku/{skuId}; GET /api/inventory/store/{storeId}/sku/{skuId}/available |
| [src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java:34](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java#L34) | InventoryServiceImpl · getByStore / getByStoreAndSku / getAvailableStock | — |
| [src/main/resources/templates/admin/inventory.html:19](../../../../src/main/resources/templates/admin/inventory.html#L19) | UI | — |
| [src/main/resources/templates/admin/inventory.html:182](../../../../src/main/resources/templates/admin/inventory.html#L182) | UI | — |
| [src/main/resources/templates/admin/inventory.html:219](../../../../src/main/resources/templates/admin/inventory.html#L219) | UI | — |

**UI và giới hạn triển khai**

- StoreScope chỉ phục vụ danh sách cửa hàng ở AdminPageController; API/service này không sử dụng nên nhân viên gọi ID cửa hàng khác được.
- Không có permission riêng INVENTORY_VIEW/INVENTORY_ADJUST trong code.
- UI: SKU/name local filter; actual/held/available/minimum/status + sums KPI. Accessible branch options; query storeId in dashboard link ignored in initialization.

<a id="uc020"></a>

## UC020 — Xem cảnh báo tồn thấp

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Tồn kho  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: xem cảnh báo tồn thấp.

**Trigger:** Actor lựa chọn xem cảnh báo tồn thấp.

**Preconditions theo nhánh**

- Xem cảnh báo tồn thấp: Token hợp lệ có ROLE_EMPLOYEE.

**Main Success Scenario**

1. Nhân viên yêu cầu danh sách tồn thấp tại một cửa hàng.
2. Hệ thống chọn các SKU có tồn thực tế không vượt minimumStock và trả kết quả.

**Luồng lựa chọn / ngoại lệ**

- Xem cảnh báo tồn thấp: Không có bản ghi thỏa: danh sách rỗng.; Chưa xác thực/không ROLE_EMPLOYEE: HTTP 401/403.

**Postconditions theo nhánh**

- Xem cảnh báo tồn thấp: Danh sách SKU tồn thấp được trả.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- /api/inventory/** requires ROLE_EMPLOYEE

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java:33](../../../../src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java#L33) | InventoryRestController · getLowStockItems | GET /api/inventory/store/{storeId}/low-stock |
| [src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java:72](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java#L72) | InventoryServiceImpl · getLowStockItems | — |
| [src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java:16](../../../../src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java#L16) | InventoryRepository · findLowStock | — |
| [src/main/resources/templates/admin/dashboard.html:211](../../../../src/main/resources/templates/admin/dashboard.html#L211) | UI | — |
| [src/main/resources/templates/admin/dashboard.html:274](../../../../src/main/resources/templates/admin/dashboard.html#L274) | UI | — |

**UI và giới hạn triển khai**

- Dùng actualStock, không dùng khả dụng sau giữ hàng; ngưỡng bao gồm trường hợp bằng.
- Không có workflow đặt ngưỡng/đặt mua tự động, thông báo gửi bên ngoài hay scheduler.
- UI: Shows count/all and first five. With all-store dashboard, chooses options[1] (first store), no chain aggregation.

<a id="uc021"></a>

## UC021 — Điều chỉnh tồn kho

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Tồn kho  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: điều chỉnh tồn kho.

**Trigger:** Actor lựa chọn điều chỉnh tồn kho.

**Preconditions theo nhánh**

- Điều chỉnh tồn kho: Token có ROLE_EMPLOYEE; inventory store-SKU tồn tại.; newQuantity không null, >=0; lý do không trống ≤255; newQuantity >= heldQuantity.; Header mutation phù hợp.

**Main Success Scenario**

1. Nhân viên cung cấp cửa hàng, SKU, số tồn thực tế mới và lý do kiểm kê.
2. Hệ thống kiểm tra tồn mới không âm và không thấp hơn lượng đang giữ.
3. Hệ thống lưu số lượng trước/sau, lý do, người thực hiện và thời điểm, rồi cập nhật tồn thực tế.

**Luồng lựa chọn / ngoại lệ**

- Điều chỉnh tồn kho: Không có inventory: checked Exception HTTP 500.; Tồn mới dưới lượng giữ: checked Exception HTTP 500.; Request không hợp lệ: HTTP 400.; Employee lookup không có kết quả: performedBy=null; Entity bắt buộc employee nên validation/constraint ngăn lưu.

**Postconditions theo nhánh**

- Điều chỉnh tồn kho: Tồn thực tế và audit adjustment được lưu; heldQuantity không đổi.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- /api/inventory/** requires ROLE_EMPLOYEE

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java#L38) | InventoryRestController · adjustStock | POST /api/inventory/adjust |
| [src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java:48](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java#L48) | InventoryServiceImpl · adjustStock | — |
| [src/main/java/com/thinh/cosmetic/domain/entity/inventory/InventoryAdjustmentEntity.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/InventoryAdjustmentEntity.java#L1) | InventoryAdjustmentEntity · entity field: performedBy / adjustedAt | — |
| [src/main/resources/templates/admin/inventory.html:163](../../../../src/main/resources/templates/admin/inventory.html#L163) | UI | — |
| [src/main/resources/templates/admin/inventory.html:318](../../../../src/main/resources/templates/admin/inventory.html#L318) | UI | — |
| [src/main/resources/templates/admin/inventory.html:344](../../../../src/main/resources/templates/admin/inventory.html#L344) | UI | — |

**UI và giới hạn triển khai**

- Không kiểm tra StoreScope.
- Có lịch sử trong DB nhưng không có REST/page xem lịch sử điều chỉnh; không tạo UC tra audit hoàn chỉnh.
- Không có pessimistic lock/@Version; chưa bảo đảm cạnh tranh với giữ hàng trong đơn.
- UI: NewQuantity >= held UI enforced; required reason preset/custom. Sends storeId/skuId/newQuantity/reason. Reasons mentioning purchase/transfer only update one stock record, not actual purchase/transfer processes.

<a id="uc022"></a>

## UC022 — Lập phiếu chuyển kho

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Điều chuyển kho  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: lập phiếu chuyển kho.

**Trigger:** Actor lựa chọn lập phiếu chuyển kho.

**Preconditions theo nhánh**

- Lập phiếu chuyển kho: sourceStoreId, destinationStoreId không null; items không rỗng; store/SKU và employeeId phải tồn tại để lưu hợp lệ.; Header mutation phù hợp.

**Main Success Scenario**

1. Nhân viên cung cấp cửa hàng nguồn, cửa hàng đích và các SKU/số lượng cần chuyển.
2. Hệ thống tìm cửa hàng, SKU và người lập rồi lưu phiếu cùng các dòng hàng.
3. Hệ thống trả phiếu trạng thái PENDING; chưa giữ hoặc trừ tồn ở bước lập.

**Luồng lựa chọn / ngoại lệ**

- Lập phiếu chuyển kho: Nguồn/đích/SKU không tồn tại: checked Exception HTTP 500.; employeeId không tồn tại: null createdBy vi phạm Entity @NotNull.; items rỗng/null: HTTP 400; thiếu @Valid trên list nên nested validation DTO không được cascade, entity validation là lớp sau.

**Postconditions theo nhánh**

- Lập phiếu chuyển kho: Phiếu PENDING và các dòng hàng được lưu; chưa trừ/giữ tồn.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java#L21) | StockTransferRestController · create | POST /api/stock-transfers?employeeId=1 |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:32](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java#L32) | StockTransferServiceImpl · create | — |

**UI và giới hạn triển khai**

- Không RBAC, không StoreScope; có thể giả danh createdBy qua query.
- Không cấm cùng nguồn/đích, SKU lặp, store/SKU INACTIVE; không kiểm tra tồn khi lập.
- checked Exception sau save có rủi ro không rollback toàn bộ do @Transactional không rollbackFor checked exceptions.

<a id="uc023"></a>

## UC023 — Tra cứu phiếu chuyển kho

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Điều chuyển kho  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: tra cứu phiếu chuyển kho.

**Trigger:** Actor lựa chọn tra cứu phiếu chuyển kho.

**Preconditions theo nhánh**

- Xem phiếu chuyển kho: Không yêu cầu đăng nhập.

**Main Success Scenario**

1. Nhân viên yêu cầu danh sách hoặc chọn một phiếu chuyển kho.
2. Hệ thống trả nguồn, đích, người lập, trạng thái và các dòng SKU/số lượng.

**Luồng lựa chọn / ngoại lệ**

- Xem phiếu chuyển kho: ID không tồn tại: checked Exception HTTP 500.; Không có phiếu: danh sách rỗng.

**Postconditions theo nhánh**

- Xem phiếu chuyển kho: Thông tin phiếu chuyển được hiển thị.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java:34](../../../../src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java#L34) | StockTransferRestController · getAll / getById | GET /api/stock-transfers; GET /api/stock-transfers/{id} |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:123](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java#L123) | StockTransferServiceImpl · getAll / getById / toResponse | — |

**UI và giới hạn triển khai**

- Không lọc theo store/status/người; repository có finder sourceOrDestination nhưng service không dùng.
- Response không có source/destination IDs; chỉ tên.

<a id="uc024"></a>

## UC024 — Xác nhận xuất chuyển kho

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Điều chuyển kho  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: xác nhận xuất chuyển kho.

**Trigger:** Actor lựa chọn xác nhận xuất chuyển kho.

**Preconditions theo nhánh**

- Xác nhận xuất chuyển kho: Phiếu tồn tại và status=PENDING.; Mỗi dòng có inventory tại nguồn; actualStock >= quantity.; Header mutation phù hợp.

**Main Success Scenario**

1. Nhân viên yêu cầu xuất hàng cho một phiếu PENDING.
2. Hệ thống kiểm tra tồn thực tế nguồn theo từng dòng hàng.
3. Hệ thống trừ tồn thực tế nguồn và đổi phiếu sang IN_TRANSIT.

**Luồng lựa chọn / ngoại lệ**

- Xác nhận xuất chuyển kho: Phiếu không tồn tại/không PENDING: checked Exception HTTP 500.; Thiếu inventory hoặc không đủ actualStock: checked Exception HTTP 500.; Không đủ tồn ở dòng sau khi dòng trước đã save: có nguy cơ commit một phần do checked Exception không thuộc rollback mặc định.

**Postconditions theo nhánh**

- Xác nhận xuất chuyển kho: Khi thành công, tồn nguồn giảm theo quantity; phiếu IN_TRANSIT có shippedAt.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java:39](../../../../src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java#L39) | StockTransferRestController · confirmShipment | PUT /api/stock-transfers/{id}/ship |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:63](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java#L63) | StockTransferServiceImpl · confirmShipment | — |

**UI và giới hạn triển khai**

- Không có bước phê duyệt riêng; enum PENDING_EXPORT không được service sử dụng.
- Kiểm tra actualStock, bỏ qua heldQuantity; có thể xuất phần đã giữ cho khách.
- Không khóa/Version; hai lần ship cạnh tranh có rủi ro trừ kho sai; không ghi InventoryAdjustment audit.

<a id="uc025"></a>

## UC025 — Xác nhận nhận chuyển kho

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Điều chuyển kho  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: xác nhận nhận chuyển kho.

**Trigger:** Actor lựa chọn xác nhận nhận chuyển kho.

**Preconditions theo nhánh**

- Xác nhận nhận chuyển kho: Phiếu tồn tại và status=IN_TRANSIT; header mutation phù hợp.

**Main Success Scenario**

1. Nhân viên yêu cầu nhận hàng cho một phiếu IN_TRANSIT.
2. Hệ thống tạo bản ghi tồn đích nếu thiếu, rồi cộng số lượng của từng SKU.
3. Hệ thống đổi phiếu sang RECEIVED và trả kết quả.

**Luồng lựa chọn / ngoại lệ**

- Xác nhận nhận chuyển kho: Phiếu không tồn tại/không IN_TRANSIT: checked Exception HTTP 500.; Lỗi persistence/validation khi lưu inventory: request thất bại.

**Postconditions theo nhánh**

- Xác nhận nhận chuyển kho: Khi thành công, tồn đích tăng; phiếu RECEIVED có receivedAt.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java:44](../../../../src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java#L44) | StockTransferRestController · confirmReceipt | PUT /api/stock-transfers/{id}/receive |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:87](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java#L87) | StockTransferServiceImpl · confirmReceipt | — |

**UI và giới hạn triển khai**

- Nhận toàn bộ theo quantity gốc; không hỗ trợ nhận thiếu/hư hỏng/hủy.
- Không có approval hoặc completion status riêng; RECEIVED là terminal implementation.
- Không RBAC/StoreScope, lock, Version hoặc adjustment audit.

<a id="uc026"></a>

## UC026 — Quản lý nhà cung cấp

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: quản lý nhà cung cấp.

**Trigger:** Actor lựa chọn quản lý nhà cung cấp.

**Preconditions theo nhánh**

- Xem nhà cung cấp: Không yêu cầu đăng nhập.
- Thêm nhà cung cấp: Tên không trống ≤200; contact ≤150; address ≤255; phone ≤20; email hợp lệ ≤150.; Header mutation phù hợp.
- Cập nhật nhà cung cấp: Supplier tồn tại; validation như create; header mutation phù hợp.
- Ngừng hợp tác nhà cung cấp: Supplier tồn tại; header mutation phù hợp.

**Main Success Scenario**

1. Nhân viên chọn tra cứu, thêm, cập nhật hoặc ngừng hợp tác nhà cung cấp.
2. Với thêm/cập nhật, nhân viên cung cấp tên, địa chỉ, số điện thoại và email liên hệ.
3. Hệ thống thực hiện nhánh được chọn; ngừng hợp tác đặt trạng thái INACTIVE.

**Luồng lựa chọn / ngoại lệ**

- Xem nhà cung cấp: ID không tồn tại: checked Exception HTTP 500.; Không có supplier: danh sách rỗng.
- Thêm nhà cung cấp: Sai validation/JSON: HTTP 400.
- Cập nhật nhà cung cấp: Không có supplier: checked Exception HTTP 500.; Request không hợp lệ: HTTP 400.
- Ngừng hợp tác nhà cung cấp: ID không tồn tại: checked Exception HTTP 500.

**Postconditions theo nhánh**

- Xem nhà cung cấp: Thông tin nhà cung cấp được trả.
- Thêm nhà cung cấp: Nhà cung cấp được lưu.
- Cập nhật nhà cung cấp: Nhà cung cấp được cập nhật.
- Ngừng hợp tác nhà cung cấp: Nhà cung cấp có status INACTIVE.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java#L31) | SupplierRestController · getAll / getById | GET /api/suppliers; GET /api/suppliers/{id} |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java:39](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java#L39) | SupplierServiceImpl · getAll / getById | — |
| [src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java#L21) | SupplierRestController · create | POST /api/suppliers |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java:24](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java#L24) | SupplierServiceImpl · create | — |
| [src/main/java/com/thinh/cosmetic/mapper/purchase/SupplierMapper.java:11](../../../../src/main/java/com/thinh/cosmetic/mapper/purchase/SupplierMapper.java#L11) | SupplierMapper · toEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java:36](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java#L36) | SupplierRestController · update | PUT /api/suppliers/{id} |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java:44](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java#L44) | SupplierServiceImpl · update | — |
| [src/main/java/com/thinh/cosmetic/mapper/purchase/SupplierMapper.java:17](../../../../src/main/java/com/thinh/cosmetic/mapper/purchase/SupplierMapper.java#L17) | SupplierMapper · updateEntity | — |
| [src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java:44](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java#L44) | SupplierRestController · deactivate | DELETE /api/suppliers/{id} |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java:52](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java#L52) | SupplierServiceImpl · deactivate | — |

**UI và giới hạn triển khai**

- Không lọc ACTIVE và không phân trang/tìm kiếm.
- existsByName trong repository không được service sử dụng; Entity name không unique.
- Có thể ACTIVE lại qua status; null không xóa dữ liệu optional cũ.
- Không chặn tạo PO từ supplier INACTIVE; không tự lọc khỏi danh sách.

<a id="uc027"></a>

## UC027 — Lập đơn nhập hàng

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: lập đơn nhập hàng.

**Trigger:** Actor lựa chọn lập đơn nhập hàng.

**Preconditions theo nhánh**

- Lập đơn nhập hàng: supplierId/receivingStoreId không null; items không rỗng; supplier/store/SKU và employeeId phải tồn tại.; Header mutation phù hợp.

**Main Success Scenario**

1. Nhân viên cung cấp nhà cung cấp, cửa hàng nhận và các SKU, số lượng, giá nhập.
2. Hệ thống tìm nhà cung cấp/cửa hàng/SKU/người lập và lưu đơn cùng các dòng hàng.
3. Hệ thống trả đơn DRAFT; tồn kho chưa tăng ở bước lập.

**Luồng lựa chọn / ngoại lệ**

- Lập đơn nhập hàng: Supplier/store/SKU không tồn tại: checked Exception HTTP 500.; Không có employee: createdBy=null vi phạm Entity bắt buộc.; items rỗng/null: HTTP 400; nested item validation không cascade do thiếu @Valid trên list; entity constraint là lớp sau.; unitPrice/quantity null: phép tính có thể ném exception trước entity validation.

**Postconditions theo nhánh**

- Lập đơn nhập hàng: PO DRAFT và dòng hàng được lưu; chưa tăng tồn.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java#L21) | PurchaseOrderRestController · create | POST /api/purchase-orders?employeeId=1 |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java:38](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java#L38) | PurchaseOrderServiceImpl · create | — |

**UI và giới hạn triển khai**

- Không RBAC/StoreScope; employeeId query cho phép gán người tạo, không lấy principal.
- Không kiểm tra status ACTIVE, giá supplier hay trùng SKU; giá nhập do request quyết định.
- Checked Exception sau lưu PO/items có nguy cơ commit một phần.

<a id="uc028"></a>

## UC028 — Tra cứu đơn nhập hàng

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: tra cứu đơn nhập hàng.

**Trigger:** Actor lựa chọn tra cứu đơn nhập hàng.

**Preconditions theo nhánh**

- Xem đơn nhập hàng: Không yêu cầu đăng nhập.

**Main Success Scenario**

1. Nhân viên yêu cầu danh sách hoặc chọn một đơn nhập hàng.
2. Hệ thống trả nhà cung cấp, cửa hàng nhận, trạng thái, các dòng và tổng giá trị nhập.

**Luồng lựa chọn / ngoại lệ**

- Xem đơn nhập hàng: Không có PO ID: checked Exception HTTP 500.; Không có đơn: danh sách rỗng.

**Postconditions theo nhánh**

- Xem đơn nhập hàng: Thông tin đơn nhập được trả.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java:34](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java#L34) | PurchaseOrderRestController · getAll / getById | GET /api/purchase-orders; GET /api/purchase-orders/{id} |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java:113](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java#L113) | PurchaseOrderServiceImpl · getAll / getById / toResponse | — |

**UI và giới hạn triển khai**

- Không lọc supplier/store/status; repository findBySupplierId/findByReceivingStoreId không dùng.
- Response chỉ names, không supplier/store IDs.

<a id="uc029"></a>

## UC029 — Xác nhận nhập hàng vào kho

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: xác nhận nhập hàng vào kho.

**Trigger:** Actor lựa chọn xác nhận nhập hàng vào kho.

**Preconditions theo nhánh**

- Xác nhận nhập hàng vào kho: PO tồn tại và status=DRAFT; header mutation phù hợp.

**Main Success Scenario**

1. Nhân viên yêu cầu xác nhận một đơn nhập hàng DRAFT.
2. Hệ thống cộng toàn bộ số lượng từng SKU vào tồn cửa hàng nhận, tạo bản ghi tồn khi thiếu.
3. Hệ thống đổi đơn sang CONFIRMED và ghi thời điểm xác nhận.

**Luồng lựa chọn / ngoại lệ**

- Xác nhận nhập hàng vào kho: ID không tồn tại/không DRAFT: checked Exception HTTP 500.; Persistence/validation error khi lưu inventory hoặc PO: request thất bại.

**Postconditions theo nhánh**

- Xác nhận nhập hàng vào kho: Khi thành công, tồn kho nhận tăng toàn bộ lượng trong PO; PO CONFIRMED có confirmedAt.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- anyRequest().permitAll()

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java:39](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java#L39) | PurchaseOrderRestController · confirm | PUT /api/purchase-orders/{id}/confirm |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java:77](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java#L77) | PurchaseOrderServiceImpl · confirm | — |

**UI và giới hạn triển khai**

- Không có approve rồi receipt tách biệt; confirm đồng thời tăng kho nên tên UC phản ánh hành vi nhập hàng thật.
- Không có phiếu nhận riêng, nhận từng phần, sửa/hủy PO hay đối chiếu hàng thiếu.
- Không RBAC/StoreScope, lock/Version hay adjustment audit.

<a id="uc030"></a>

## UC030 — Quản lý giỏ hàng

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Giỏ hàng và yêu thích  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: quản lý giỏ hàng.

**Trigger:** Actor lựa chọn quản lý giỏ hàng.

**Preconditions theo nhánh**

- Xem giỏ hàng: Đã đăng nhập ROLE_CUSTOMER.
- Thêm sản phẩm vào giỏ: Đã đăng nhập; SKU tồn tại; quantity >= 1.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Điều chỉnh số lượng trong giỏ: Đã đăng nhập; cartItemId tồn tại.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Xóa sản phẩm khỏi giỏ: Đã đăng nhập.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Làm trống giỏ hàng: Đã đăng nhập.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Khách hàng yêu cầu xem giỏ hoặc chọn thêm SKU, đổi số lượng, xóa một dòng hay làm trống giỏ.
2. Khi thêm/đổi số lượng, hệ thống kiểm tra tổng khả dụng của SKU trên chuỗi cửa hàng.
3. Hệ thống thực hiện nhánh đã chọn và trả các dòng hàng, giá hiện tại, tổng tiền và tổng số lượng.
4. Giỏ hàng không giữ tồn; giữ tồn chỉ xảy ra khi tạo đơn.

**Luồng lựa chọn / ngoại lệ**

- Xem giỏ hàng: Giỏ chưa có sản phẩm: trả danh sách rỗng và tổng 0.; Khách không tồn tại nhưng chưa có giỏ: code tạo customer=null rồi persistence không đáp ứng constraint.
- Thêm sản phẩm vào giỏ: SKU không tồn tại: generic Exception -> INTERNAL_ERROR 500.; Số lượng vượt tổng tồn khả dụng: INSUFFICIENT_STOCK 409.; DTO thiếu skuId/quantity hoặc quantity < 1: VALIDATION_ERROR 400.
- Điều chỉnh số lượng trong giỏ: Không đủ tồn: INSUFFICIENT_STOCK 409.; Không có item: generic Exception -> 500.; CartItem của khách khác vẫn được xử lý vì thiếu kiểm item.cart.id == cart.id.
- Xóa sản phẩm khỏi giỏ: CartItem thuộc giỏ người khác cũng bị xóa vì không kiểm quyền sở hữu.; Item không tồn tại: không có business validation riêng; hành vi deleteById do repository.
- Làm trống giỏ hàng: Giỏ đã rỗng: vẫn trả 204.

**Postconditions theo nhánh**

- Xem giỏ hàng: Giỏ được hiển thị; lần đọc đầu tiên có thể tạo giỏ.
- Thêm sản phẩm vào giỏ: Giỏ chứa SKU với số lượng tăng; chưa giữ tồn.
- Điều chỉnh số lượng trong giỏ: Số lượng dòng được đổi hoặc dòng bị xóa.
- Xóa sản phẩm khỏi giỏ: Dòng giỏ theo ID bị xóa.
- Làm trống giỏ hàng: Giỏ vẫn tồn tại và không còn dòng.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- ROLE_CUSTOMER theo SecurityConfig.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L31) | CartRestController · getCart | GET /api/cart |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:41](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L41) | CartServiceImpl · getCart | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/controller/CartPageController.java:24](../../../../src/main/java/com/thinh/cosmetic/controller/CartPageController.java#L24) | CartPageController · implementation | — |
| [src/main/resources/static/js/api.js:134](../../../../src/main/resources/static/js/api.js#L134) | UI | — |
| [src/main/resources/templates/cart.html:35](../../../../src/main/resources/templates/cart.html#L35) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:39](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L39) | CartRestController · addItem | POST /api/cart/items |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:47](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L47) | CartServiceImpl · addItem | — |
| [src/main/resources/templates/product-detail.html:317](../../../../src/main/resources/templates/product-detail.html#L317) | UI | — |
| [src/main/resources/static/js/api.js:157](../../../../src/main/resources/static/js/api.js#L157) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:48](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L48) | CartRestController · updateItemQuantity | PUT /api/cart/items/{cartItemId}?quantity=... |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:82](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L82) | CartServiceImpl · updateItemQuantity | — |
| [src/main/resources/templates/cart.html:70](../../../../src/main/resources/templates/cart.html#L70) | UI | — |
| [src/main/resources/templates/cart.html:169](../../../../src/main/resources/templates/cart.html#L169) | UI | — |
| [src/main/resources/templates/cart.html:182](../../../../src/main/resources/templates/cart.html#L182) | UI | — |
| [src/main/java/com/thinh/cosmetic/domain/dto/response/cart/CartResponse.java:14](../../../../src/main/java/com/thinh/cosmetic/domain/dto/response/cart/CartResponse.java#L14) | CartResponse · implementation | — |
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:58](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L58) | CartRestController · removeItem | DELETE /api/cart/items/{cartItemId} |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:106](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L106) | CartServiceImpl · removeItem | — |
| [src/main/resources/templates/cart.html:201](../../../../src/main/resources/templates/cart.html#L201) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:67](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L67) | CartRestController · clearCart | DELETE /api/cart |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:113](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L113) | CartServiceImpl · clearCart | — |

**UI và giới hạn triển khai**

- Query customerId chỉ được dùng khi AuthPrincipal không chứa customerId; fallback 1L.
- UI: SSR cartService.getCart; JS cart badge separately fetches /api/cart.
- Không kiểm activeStatus SKU/product/store.
- Tồn cộng toàn hệ thống không bảo đảm một cửa hàng đáp ứng toàn bộ giỏ.
- UI: Requires SKU selection; quantity 1..99; JSON skuId/quantity; updates badge. Backend actual stock checks delegated commerce.
- Không chứng minh quyền sở hữu dòng; đây là lỗi triển khai, không phải chức năng quản lý giỏ người khác.
- Frontend agent xác minh cart.html dùng số lượng SSR cố định ở nút +/-; AJAX đọc itemSubtotal/cartSubtotal không có trong CartResponse nên tổng tiền stale và click lặp không tăng tiếp.
- UI: updateQty calls API; response code expects itemSubtotal/cartSubtotal although CartResponse has items/subtotal/totalItems, therefore money stale until reload. +/- onclick embeds SSR quantity and repeats same target across clicks.
- Không gán mục tiêu quản trị giỏ khách khác dù lỗ hổng cho phép.
- UI: Confirm then reload. newQty<1 also routes to remove.

<a id="uc031"></a>

## UC031 — Quản lý sản phẩm yêu thích

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Giỏ hàng và yêu thích  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: quản lý sản phẩm yêu thích.

**Trigger:** Actor lựa chọn quản lý sản phẩm yêu thích.

**Preconditions theo nhánh**

- Xem sản phẩm yêu thích: API không yêu cầu đăng nhập; customerId nếu được chỉ định phải tồn tại để tạo wishlist mới.
- Lưu sản phẩm yêu thích: Product tồn tại; API không yêu cầu đăng nhập.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Bỏ sản phẩm yêu thích: API không yêu cầu đăng nhập.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Khách hàng yêu cầu xem danh sách yêu thích hoặc chọn lưu/bỏ một sản phẩm.
2. Hệ thống tìm hoặc tạo danh sách của khách và thực hiện nhánh được chọn.
3. Hệ thống trả danh sách/kết quả cập nhật; không có bước tự chuyển sản phẩm vào giỏ.

**Luồng lựa chọn / ngoại lệ**

- Xem sản phẩm yêu thích: Không có sản phẩm: danh sách rỗng.; Customer không tồn tại và chưa có wishlist: customer=null vi phạm entity constraint.
- Lưu sản phẩm yêu thích: Product đã tồn tại: không tạo dòng trùng.; Product không tồn tại: generic Exception -> 500.; UI raw fetch thiếu X-Requested-With nên request cookie JWT bị chặn 403 trước controller.
- Bỏ sản phẩm yêu thích: Product không ở wishlist: không có business exception riêng.; UI raw fetch thiếu X-Requested-With: 403 trước controller.

**Postconditions theo nhánh**

- Xem sản phẩm yêu thích: Wishlist được đọc hoặc tạo mới.
- Lưu sản phẩm yêu thích: Backend có thể lưu wishlist item nếu request hợp lệ.
- Bỏ sản phẩm yêu thích: Wishlist không còn sản phẩm chỉ định.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java:28](../../../../src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java#L28) | WishListRestController · getWishList | GET /api/wishlist |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java:38](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java#L38) | WishListServiceImpl · getWishList | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:96](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L96) | AccountPageController · implementation | — |
| [src/main/resources/templates/account/wishlist.html:102](../../../../src/main/resources/templates/account/wishlist.html#L102) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java:36](../../../../src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java#L36) | WishListRestController · addProduct | POST /api/wishlist/products/{productId} |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java:44](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java#L44) | WishListServiceImpl · addProduct | — |
| [src/main/resources/static/js/api.js:167](../../../../src/main/resources/static/js/api.js#L167) | UI | — |
| [src/main/resources/static/js/api.js:176](../../../../src/main/resources/static/js/api.js#L176) | UI | — |
| [src/main/resources/templates/home.html:248](../../../../src/main/resources/templates/home.html#L248) | UI | — |
| [src/main/resources/templates/product-list.html:152](../../../../src/main/resources/templates/product-list.html#L152) | UI | — |
| [src/main/resources/templates/product-detail.html:105](../../../../src/main/resources/templates/product-detail.html#L105) | UI | — |
| [src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java:36](../../../../src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java#L36) | XRequestedWithFilter · implementation | — |
| [src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java:45](../../../../src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java#L45) | WishListRestController · removeProduct | DELETE /api/wishlist/products/{productId} |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java:60](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java#L60) | WishListServiceImpl · removeProduct | — |
| [src/main/resources/templates/account/wishlist.html:165](../../../../src/main/resources/templates/account/wishlist.html#L165) | UI | — |
| [src/main/resources/templates/account/wishlist.html:172](../../../../src/main/resources/templates/account/wishlist.html#L172) | UI | — |

**UI và giới hạn triển khai**

- Trang /account/wishlist yêu cầu ROLE_CUSTOMER; API rơi vào permitAll.
- Giá min gồm cả SKU inactive; không có lọc active.
- UI: SSR wishListService.getWishList; card links to product detail, no move-to-cart operation.
- Tích hợp UI phải Partially Implemented: frontend agent xác minh header thiếu.
- API cho phép guest sửa wishlist qua customerId/default 1; không coi đây là độc quyền Customer.
- UI: toggleWishlist raw fetch only Content-Type, lacks X-Requested-With. Blocked by filter with cookie JWT. Hearts not initialized from server wishlist state.
- Tích hợp UI phải Partially Implemented do header thiếu.
- UI: Both toggleWishlist and removeWishlistItem raw fetch omit X-Requested-With -> 403.

<a id="uc032"></a>

## UC032 — Xem tổng tiền trước khi đặt

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đặt hàng và vận hành đơn  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: xem tổng tiền trước khi đặt.

**Trigger:** Actor lựa chọn xem tổng tiền trước khi đặt.

**Preconditions theo nhánh**

- Xem tổng tiền trước khi đặt hàng: Đã đăng nhập ROLE_CUSTOMER; customer và giỏ có sản phẩm tồn tại.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Khách hàng yêu cầu xem tổng tiền của giỏ trước khi đặt hàng.
2. Hệ thống lấy giá hiện tại của các SKU và tính tổng tiền hàng.
3. Tại điểm mở rộng tính tổng cuối, hệ thống có thể áp dụng mã ưu đãi nếu khách cung cấp mã phù hợp.
4. Hệ thống tính phí giao hàng, tổng phải trả và hiển thị kết quả; chưa tạo đơn hoặc giữ tồn.

**Luồng lựa chọn / ngoại lệ**

- Xem tổng tiền trước khi đặt hàng: Giỏ rỗng: EMPTY_CART 400.; Customer không tồn tại: CUSTOMER_NOT_FOUND 404.; Mã lạ: không báo lỗi, discount 0; shipping theo threshold.

**Postconditions theo nhánh**

- Xem tổng tiền trước khi đặt hàng: Khách nhận tổng tiền dự kiến; DB không tạo đơn.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- ROLE_CUSTOMER theo SecurityConfig.

**Extension Point**

- Tính tổng cuối: sau tổng tiền hàng và trước khi trả/lưu tổng phải trả.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:37](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L37) | OrderRestController · previewOrder | POST /api/orders/preview |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:249](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L249) | OrderServiceImpl · previewOrder | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java:30](../../../../src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java#L30) | CheckoutPageController · implementation | — |
| [src/main/resources/templates/checkout.html:39](../../../../src/main/resources/templates/checkout.html#L39) | UI | — |
| [src/main/resources/templates/checkout.html:263](../../../../src/main/resources/templates/checkout.html#L263) | UI | — |

**UI và giới hạn triển khai**

- Không kiểm tồn; không bắt buộc gọi preview trước placeOrder.
- UI: SSR cart+saved addresses+stores loaded; UI selects existing address or new. Only standard delivery and COD. No store selection or deliveryMethod payload.
- SSR /checkout hiển thị giá giỏ; preview API được UI dùng khi áp mã. Chưa tạo đơn hoặc giữ tồn ở bước xem tổng.

<a id="uc033"></a>

## UC033 — Áp dụng mã ưu đãi

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đặt hàng và vận hành đơn  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: áp dụng mã ưu đãi.

**Trigger:** Actor lựa chọn áp dụng mã ưu đãi.

**Preconditions theo nhánh**

- Áp dụng mã ưu đãi khi thanh toán: Đã đăng nhập; giỏ không rỗng; nhập mã.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Khách hàng cung cấp mã ưu đãi khi xem trước hoặc đặt đơn.
2. Hệ thống chuẩn hóa mã và, tại điểm tính tổng cuối, áp dụng LUNEA10, GIAM50K hoặc FREESHIP nếu mã phù hợp.
3. Hệ thống cập nhật số tiền giảm/phí giao hàng và tổng cuối; khi tạo đơn các số tiền được lưu trên đơn.

**Luồng lựa chọn / ngoại lệ**

- Áp dụng mã ưu đãi khi thanh toán: Mã khác hoặc rỗng: discount 0; không có validation mã/expiry/quota.; Tổng tiền không âm dù discount > subtotal.

**Postconditions theo nhánh**

- Áp dụng mã ưu đãi khi thanh toán: Tổng tiền thay đổi và discountAmount được lưu khi đặt đơn.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- ROLE_CUSTOMER theo SecurityConfig.

**Extend:** UC033 → UC032 và UC033 → UC034, khi khách cung cấp một trong ba mã phù hợp; extension point Tính tổng cuối.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:37](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L37) | OrderRestController · previewOrder | POST /api/orders/preview |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:249](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L249) | OrderServiceImpl · previewOrder | — |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:64](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L64) | OrderServiceImpl · placeOrder | POST /api/orders |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/resources/templates/checkout.html:295](../../../../src/main/resources/templates/checkout.html#L295) | UI | — |
| [src/main/resources/templates/checkout.html:302](../../../../src/main/resources/templates/checkout.html#L302) | UI | — |
| [src/main/resources/templates/checkout.html:311](../../../../src/main/resources/templates/checkout.html#L311) | UI | — |

**UI và giới hạn triển khai**

- Không có module quản trị voucher thật; không có lưu usage hay hoàn lượt sử dụng.
- Không tự tạo extend: root cần extension point UI đọc đầy đủ; backend có thể nhận voucher ngay trong placeOrder.
- UI: Payload only voucherCode. Uses preview.discountAmount/shippingFee. On every API error, hardcoded client discount for LUNEA10/GIAM50K/FREESHIP claims success regardless server validity.

<a id="uc034"></a>

## UC034 — Đặt hàng thanh toán khi nhận

**Primary Actor:** Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đặt hàng và vận hành đơn  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng, Nhân viên: đặt hàng thanh toán khi nhận.

**Trigger:** Actor lựa chọn đặt hàng thanh toán khi nhận.

**Preconditions theo nhánh**

- Đặt hàng thanh toán khi nhận: API /api/orders và /api/orders/checkout yêu cầu authenticated(); trang checkout ROLE_CUSTOMER. Customer/cart tồn tại và có dòng; paymentMethod COD (@NotNull).REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Actor cung cấp người nhận, số điện thoại, địa chỉ và lựa chọn COD cho giỏ khách hàng cần đặt.
2. Hệ thống tính tổng tiền hàng; tại điểm mở rộng tính tổng cuối có thể áp dụng mã ưu đãi.
3. Hệ thống xác định cửa hàng phục vụ, tạo đơn PENDING và lưu giá/tên biến thể tại thời điểm đặt.
4. Hệ thống giữ số lượng cho đơn, xóa các dòng giỏ và trả mã đơn cùng kết quả đặt hàng.

**Luồng lựa chọn / ngoại lệ**

- Đặt hàng thanh toán khi nhận: Customer không có: 404; giỏ rỗng: 400.; Không store nào đủ tồn: code vẫn chọn store đầu; không trả NO_STORE_CAN_FULFILL.; Không store nào tồn tại: assignedStore=null mâu thuẫn @NotNull/DB.; OrderCode collision không retry; persistence có thể DATA_CONFLICT.; CustomerAddressId không kiểm thuộc khách; deliveryMethod bỏ qua.

**Postconditions theo nhánh**

- Đặt hàng thanh toán khi nhận: Đơn PENDING, dòng snapshot và stock holds được tạo; giỏ đã xóa trong transaction.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- authenticated() cho POST /api/orders và /api/orders/checkout; trang /checkout ROLE_CUSTOMER.

**Extension Point**

- Tính tổng cuối: sau tổng tiền hàng và trước khi trả/lưu tổng phải trả.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:46](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L46) | OrderRestController · placeOrder | POST /api/orders; POST /api/orders/checkout |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:64](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L64) | OrderServiceImpl · placeOrder | — |
| [src/main/java/com/thinh/cosmetic/domain/dto/request/order/OrderRequest.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/order/OrderRequest.java#L1) | OrderRequest · entity/DTO field | — |
| [src/main/java/com/thinh/cosmetic/domain/entity/sales/OrderEntity.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/entity/sales/OrderEntity.java#L1) | OrderEntity · entity/DTO field | — |
| [src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java:12](../../../../src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java#L12) | InventoryRepository · findByStoreIdAndSkuId | — |
| [src/main/java/com/thinh/cosmetic/repository/order/OrderReservationRepository.java:1](../../../../src/main/java/com/thinh/cosmetic/repository/order/OrderReservationRepository.java#L1) | OrderReservationRepository · save | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java:26](../../../../src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java#L26) | XRequestedWithFilter · doFilterInternal | — |
| [src/main/resources/templates/checkout.html:333](../../../../src/main/resources/templates/checkout.html#L333) | UI | — |
| [src/main/resources/templates/checkout.html:344](../../../../src/main/resources/templates/checkout.html#L344) | UI | — |
| [src/main/resources/templates/checkout.html:355](../../../../src/main/resources/templates/checkout.html#L355) | UI | — |

**UI và giới hạn triển khai**

- Không có hỗ trợ thanh toán online hoặc external payment actor: enum chỉ COD.
- deliveryMethod STORE_PICKUP trong DTO bị bỏ qua; order luôn mặc định HOME_DELIVERY.
- Không recheck/lock tồn khi giữ; store được chọn trực tiếp có thể thiếu tồn; có thể vượt actualStock.
- Không lọc inactive store/SKU/product.
- API placeOrder cho employee authenticated chỉ định customerId dù UI dành cho Customer; không dùng ORDER_PROCESS/ORDER_VIEW permission.
- UI: Sends recipientName/Phone, province/district/ward, shippingAddress, note, COD, voucherCode, customerAddressId. Does not send storeId/deliveryMethod/expectedTotal. Backend assigned store, response orderCode -> success page.
- UI không có chọn fulfillmentStoreId; chỉ API mới nhận cửa hàng chỉ định. Không có endpoint nhân viên gán lại chi nhánh.

<a id="uc035"></a>

## UC035 — Tra cứu đơn hàng của tôi

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đặt hàng và vận hành đơn  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: tra cứu đơn hàng của tôi.

**Trigger:** Actor lựa chọn tra cứu đơn hàng của tôi.

**Preconditions theo nhánh**

- Xem lịch sử đơn hàng: Đã đăng nhập ROLE_CUSTOMER.
- Theo dõi chi tiết đơn hàng: Đã đăng nhập ROLE_CUSTOMER; đơn tồn tại.

**Main Success Scenario**

1. Khách hàng yêu cầu danh sách đơn hoặc chọn một đơn cần theo dõi.
2. Hệ thống trả các đơn mới nhất trước; chi tiết gồm dòng hàng, tiền, trạng thái, địa chỉ và thời điểm xử lý.
3. Khách hàng xem tiến trình của đơn; các đường tra cứu chưa kiểm chủ đơn được ghi ở báo cáo giới hạn.

**Luồng lựa chọn / ngoại lệ**

- Xem lịch sử đơn hàng: Chưa có đơn: danh sách rỗng.; Fallback customerId query/1 khi AuthPrincipal không có customerId.
- Theo dõi chi tiết đơn hàng: Đơn không có: ORDER_NOT_FOUND 404; page ghi log.; Sai owner: REST ACCESS_DENIED 403; account page redirect /account/orders.; Trang /order-success?orderCode=... gọi getByOrderCode không kiểm owner, gây lộ đơn theo mã.

**Postconditions theo nhánh**

- Xem lịch sử đơn hàng: Lịch sử đơn được hiển thị.
- Theo dõi chi tiết đơn hàng: Chi tiết đơn được đọc.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- /my-orders: ROLE_CUSTOMER; /customer/{customerId}: authenticated(), không ownership; page /account/orders: ROLE_CUSTOMER.
- REST /api/orders/{id} và /code/{code}: authenticated(); page /account/orders/{id}, /order-success: ROLE_CUSTOMER.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:90](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L90) | OrderRestController · getMyOrders | GET /api/orders/my-orders |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:342](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L342) | OrderServiceImpl · getByCustomer | — |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:52](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L52) | AccountPageController · orders | GET /account/orders |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:55](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L55) | AccountPageController · implementation | — |
| [src/main/resources/templates/account/orders.html:198](../../../../src/main/resources/templates/account/orders.html#L198) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:85](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L85) | OrderRestController · getByCustomer | GET /api/orders/customer/{customerId} |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:55](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L55) | OrderRestController · getById | GET /api/orders/{id} |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:71](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L71) | OrderRestController · getByOrderCode | GET /api/orders/code/{orderCode} |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:313](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L313) | OrderServiceImpl · getById | — |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:334](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L334) | OrderServiceImpl · getByOrderCode | — |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:69](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L69) | AccountPageController · orderDetail | GET /account/orders/{id} |
| [src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java:46](../../../../src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java#L46) | CheckoutPageController · orderSuccess | GET /order-success |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:72](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L72) | AccountPageController · implementation | — |
| [src/main/resources/templates/account/order-detail.html:22](../../../../src/main/resources/templates/account/order-detail.html#L22) | UI | — |
| [src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java:52](../../../../src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java#L52) | CheckoutPageController · implementation | — |
| [src/main/resources/templates/order-success.html:44](../../../../src/main/resources/templates/order-success.html#L44) | UI | — |

**UI và giới hạn triển khai**

- REST /api/orders/customer/{customerId} chỉ yêu cầu authenticated(), không kiểm owner; ghi như lỗ hổng, không UC xem dữ liệu khách khác.
- UI: SSR getByCustomer; status filtering local DOM including PENDING/CONFIRMED/PREPARING/SHIPPING/COMPLETED/CANCELLED.
- Employee gọi REST by-id có StoreScope; by-code không có StoreScope employee.
- UI: SSR getById+getByOrder; checks customer owner only when principal and non-null customerId/order.customerId. Shows snapshot, delivery, totals, stepper, return records.
- UI: SSR getByOrderCode; page displays orderCode/store/recipient/total. Status text fixed Chờ xác nhận and fallback totals when no order. Controller has no owner check here (commerce/security root cross-check).

<a id="uc036"></a>

## UC036 — Hủy đơn chờ xác nhận

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đặt hàng và vận hành đơn  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: hủy đơn chờ xác nhận.

**Trigger:** Actor lựa chọn hủy đơn chờ xác nhận.

**Preconditions theo nhánh**

- Hủy đơn đang chờ xác nhận: Đã đăng nhập ROLE_CUSTOMER; đơn thuộc customer; status PENDING. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Khách hàng yêu cầu hủy một đơn của mình.
2. Hệ thống kiểm tra quyền sở hữu và đơn đang PENDING.
3. Hệ thống đổi sang CANCELLED, giải phóng lượng còn HELD và ghi thời điểm hủy.

**Luồng lựa chọn / ngoại lệ**

- Hủy đơn đang chờ xác nhận: Không đúng owner: ACCESS_DENIED 403.; Không PENDING: ORDER_NOT_CANCELLABLE 409.; Không order: ORDER_NOT_FOUND 404.

**Postconditions theo nhánh**

- Hủy đơn đang chờ xác nhận: Đơn CANCELLED và tồn giữ HELD được giải phóng.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- ROLE_CUSTOMER theo SecurityConfig.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:115](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L115) | OrderRestController · cancelOrder | PUT/POST /api/orders/{id}/cancel |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:460](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L460) | OrderServiceImpl · cancelOrder | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/resources/templates/account/orders.html:172](../../../../src/main/resources/templates/account/orders.html#L172) | UI | — |
| [src/main/resources/templates/account/orders.html:247](../../../../src/main/resources/templates/account/orders.html#L247) | UI | — |
| [src/main/resources/templates/account/order-detail.html:247](../../../../src/main/resources/templates/account/order-detail.html#L247) | UI | — |
| [src/main/resources/templates/account/order-detail.html:338](../../../../src/main/resources/templates/account/order-detail.html#L338) | UI | — |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:459](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L459) | OrderServiceImpl · implementation | — |

**UI và giới hạn triển khai**

- Không hoàn lượt dùng voucher vì không có usage ledger.
- UI: UI and OrderServiceImpl.cancelOrder both allow cancellation only PENDING. Confirmation and reload; staff cancellation differs (PENDING/CONFIRMED/PREPARING status workflow).

<a id="uc037"></a>

## UC037 — Tra cứu đơn hàng cửa hàng

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đặt hàng và vận hành đơn  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: tra cứu đơn hàng cửa hàng.

**Trigger:** Actor lựa chọn tra cứu đơn hàng cửa hàng.

**Preconditions theo nhánh**

- Tra cứu đơn hàng trong phạm vi cửa hàng: Đã đăng nhập ROLE_EMPLOYEE; phạm vi từ EmployeeStore hoặc ALL_STORES/ROLE_ADMIN/ADMIN.
- Xem chi tiết đơn của cửa hàng: Đã đăng nhập ROLE_EMPLOYEE; order thuộc cửa hàng accessible.

**Main Success Scenario**

1. Nhân viên yêu cầu danh sách theo cửa hàng hoặc chọn một đơn.
2. Hệ thống áp dụng phạm vi cửa hàng được phân công; ADMIN/ALL_STORES có thể xem mọi cửa hàng.
3. Hệ thống trả danh sách/chi tiết đơn trong phạm vi được phép.

**Luồng lựa chọn / ngoại lệ**

- Tra cứu đơn hàng trong phạm vi cửa hàng: Không có store assignment: danh sách rỗng.; storeId ngoài scope: danh sách rỗng.; Principal null trong service overload: trả getAll; normal secured JWT flow phải có principal.
- Xem chi tiết đơn của cửa hàng: Ngoài scope: ACCESS_DENIED 403.; Không tìm thấy: ORDER_NOT_FOUND 404.; Order assignedStore null thì scope check bị bỏ qua, nhưng entity bình thường cấm null.

**Postconditions theo nhánh**

- Tra cứu đơn hàng trong phạm vi cửa hàng: Danh sách trong scope được đọc.
- Xem chi tiết đơn của cửa hàng: Chi tiết đơn trong scope được đọc.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:22](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L22) | AdminOrderRestController · getOrders | GET /api/admin/orders |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:355](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L355) | OrderServiceImpl · getOrders | — |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | StoreScope · getAccessibleStoreIds | — |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | StoreScope · canAccessStore | — |
| [src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java:10](../../../../src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java#L10) | OrderRepository · findByAssignedStoreIdOrderByCreatedAtDesc | — |
| [src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java:11](../../../../src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java#L11) | OrderRepository · findByAssignedStoreIdInOrderByCreatedAtDesc | — |
| [src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java:12](../../../../src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java#L12) | OrderRepository · findAllByOrderByCreatedAtDesc | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:98](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L98) | OrderRestController · getAll | GET /api/orders?storeId=... |
| [src/main/java/com/thinh/cosmetic/controller/AdminPageController.java:25](../../../../src/main/java/com/thinh/cosmetic/controller/AdminPageController.java#L25) | AdminPageController · implementation | — |
| [src/main/resources/templates/admin/orders.html:113](../../../../src/main/resources/templates/admin/orders.html#L113) | UI | — |
| [src/main/resources/templates/admin/orders.html:172](../../../../src/main/resources/templates/admin/orders.html#L172) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:30](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L30) | AdminOrderRestController · getById | GET /api/admin/orders/{id} |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:321](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L321) | OrderServiceImpl · getById(AuthPrincipal principal, ...) | — |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:55](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L55) | OrderRestController · getById | GET /api/orders/{id} |
| [src/main/resources/templates/admin/order-detail.html:212](../../../../src/main/resources/templates/admin/order-detail.html#L212) | UI | — |

**UI và giới hạn triển khai**

- Controller getOrders delegates service method getOrders; REST method name is getOrders in Admin and getAll in Order.
- Các quyền ORDER_VIEW/ORDER_PROCESS không được dùng để gate endpoint.
- UI: Branch selector from StoreScope; local status/code/name/phone filters. Admin any/all branches, other employee accessibleStores only.
- Đường /api/orders/code/{orderCode} không áp dụng scope employee.
- UI: AJAX render snapshots/prices/address/stepper; data-order-id from page Model.

<a id="uc038"></a>

## UC038 — Cập nhật tiến trình đơn hàng

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đặt hàng và vận hành đơn  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: cập nhật tiến trình đơn hàng.

**Trigger:** Actor lựa chọn cập nhật tiến trình đơn hàng.

**Preconditions theo nhánh**

- Xác nhận đơn hàng: Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Chuyển đơn sang chuẩn bị hàng: Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Bàn giao đơn để giao hàng: Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Ghi nhận đơn giao thành công: Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Nhân viên lựa chọn xác nhận đơn, chuẩn bị hàng, bắt đầu giao hoặc ghi nhận giao thành công.
2. Hệ thống kiểm tra phạm vi cửa hàng và không cho đổi một đơn terminal sang trạng thái khác.
3. Hệ thống cập nhật CONFIRMED/PREPARING/SHIPPING/COMPLETED tương ứng với công việc được chọn.
4. Khi SHIPPING hoặc COMPLETED, lượng còn HELD được chuyển COMMITTED và giảm tồn thực tế/lượng giữ.

**Luồng lựa chọn / ngoại lệ**

- Xác nhận đơn hàng: Ngoài scope: ACCESS_DENIED 403.; Sửa terminal order: INVALID_STATUS_TRANSITION 409.; Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.
- Chuyển đơn sang chuẩn bị hàng: Ngoài scope: ACCESS_DENIED 403.; Sửa terminal order: INVALID_STATUS_TRANSITION 409.; Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.
- Bàn giao đơn để giao hàng: Ngoài scope: ACCESS_DENIED 403.; Sửa terminal order: INVALID_STATUS_TRANSITION 409.; Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.
- Ghi nhận đơn giao thành công: Ngoài scope: ACCESS_DENIED 403.; Sửa terminal order: INVALID_STATUS_TRANSITION 409.; Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.

**Postconditions theo nhánh**

- Xác nhận đơn hàng: Status=CONFIRMED và các effects được lưu.
- Chuyển đơn sang chuẩn bị hàng: Status=PREPARING và các effects được lưu.
- Bàn giao đơn để giao hàng: Status=SHIPPING và các effects được lưu.
- Ghi nhận đơn giao thành công: Status=COMPLETED và các effects được lưu.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | AdminOrderRestController · updateStatus | PUT/POST /api/admin/orders/{id}/status?status=CONFIRMED |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:393](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L393) | OrderServiceImpl · updateStatus(AuthPrincipal principal, ...) | — |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | StoreScope · getAccessibleStoreIds | — |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | StoreScope · canAccessStore | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | OrderRestController · updateStatus | PUT/POST /api/orders/{id}/status?status=CONFIRMED |
| [src/main/resources/templates/admin/order-detail.html:457](../../../../src/main/resources/templates/admin/order-detail.html#L457) | UI | — |
| [src/main/resources/templates/admin/order-detail.html:473](../../../../src/main/resources/templates/admin/order-detail.html#L473) | UI | — |
| [src/main/resources/templates/admin/orders.html:243](../../../../src/main/resources/templates/admin/orders.html#L243) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | AdminOrderRestController · updateStatus | PUT/POST /api/admin/orders/{id}/status?status=PREPARING |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | OrderRestController · updateStatus | PUT/POST /api/orders/{id}/status?status=PREPARING |
| [src/main/resources/templates/admin/order-detail.html:393](../../../../src/main/resources/templates/admin/order-detail.html#L393) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | AdminOrderRestController · updateStatus | PUT/POST /api/admin/orders/{id}/status?status=SHIPPING |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | OrderRestController · updateStatus | PUT/POST /api/orders/{id}/status?status=SHIPPING |
| [src/main/resources/templates/admin/order-detail.html:417](../../../../src/main/resources/templates/admin/order-detail.html#L417) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | AdminOrderRestController · updateStatus | PUT/POST /api/admin/orders/{id}/status?status=COMPLETED |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | OrderRestController · updateStatus | PUT/POST /api/orders/{id}/status?status=COMPLETED |
| [src/main/resources/templates/admin/order-detail.html:435](../../../../src/main/resources/templates/admin/order-detail.html#L435) | UI | — |

**UI và giới hạn triển khai**

- Các hold HELD chỉ commit/release một lần theo trạng thái reservation.
- Không có payment collection integration, courier API hoặc xác nhận giao vận ngoài hệ thống.
- UI có thể giới hạn nút theo trạng thái nhưng backend không enforced predecessor; root merge UI exact.
- UI: State-dependent action; quick confirm in orders table; confirmation for detail. Header wrapper correct. No role/permission-specific hide beyond status; actual backend authorization must decide.

<a id="uc039"></a>

## UC039 — Hủy đơn hàng cửa hàng

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đặt hàng và vận hành đơn  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: hủy đơn hàng cửa hàng.

**Trigger:** Actor lựa chọn hủy đơn hàng cửa hàng.

**Preconditions theo nhánh**

- Hủy đơn trong phạm vi cửa hàng: Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Nhân viên yêu cầu hủy một đơn trong phạm vi cửa hàng.
2. Hệ thống kiểm tra phạm vi và không đổi đơn đã terminal sang một trạng thái khác.
3. Hệ thống cập nhật CANCELLED và giải phóng lượng còn HELD; không hoàn lại lượng đã COMMITTED.

**Luồng lựa chọn / ngoại lệ**

- Hủy đơn trong phạm vi cửa hàng: Ngoài scope: ACCESS_DENIED 403.; Sửa terminal order: INVALID_STATUS_TRANSITION 409.; Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.

**Postconditions theo nhánh**

- Hủy đơn trong phạm vi cửa hàng: Status=CANCELLED và các effects được lưu.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L47) | AdminOrderRestController · cancelOrder | PUT/POST /api/admin/orders/{id}/cancel |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:393](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L393) | OrderServiceImpl · updateStatus(AuthPrincipal principal, ...) | — |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | StoreScope · getAccessibleStoreIds | — |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | StoreScope · canAccessStore | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | AdminOrderRestController · updateStatus | PUT/POST /api/admin/orders/{id}/status?status=CANCELLED |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | OrderRestController · updateStatus | PUT/POST /api/orders/{id}/status?status=CANCELLED |
| [src/main/resources/templates/admin/order-detail.html:372](../../../../src/main/resources/templates/admin/order-detail.html#L372) | UI | — |
| [src/main/resources/templates/admin/order-detail.html:457](../../../../src/main/resources/templates/admin/order-detail.html#L457) | UI | — |

**UI và giới hạn triển khai**

- Các hold HELD chỉ commit/release một lần theo trạng thái reservation.
- Không có payment collection integration, courier API hoặc xác nhận giao vận ngoài hệ thống.
- UI có thể giới hạn nút theo trạng thái nhưng backend không enforced predecessor; root merge UI exact.
- UI: State-dependent action; quick confirm in orders table; confirmation for detail. Header wrapper correct. No role/permission-specific hide beyond status; actual backend authorization must decide.

<a id="uc040"></a>

## UC040 — Gửi yêu cầu trả hàng

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Yêu cầu trả hàng  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: gửi yêu cầu trả hàng.

**Trigger:** Actor lựa chọn gửi yêu cầu trả hàng.

**Preconditions theo nhánh**

- Gửi yêu cầu trả hàng: API công khai; orderId tồn tại, reason không trống, items không rỗng. UI chỉ hiện với order COMPLETED chưa có return. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Khách hàng chọn đơn, các dòng cần trả, số lượng và lý do.
2. Hệ thống tìm đơn và các dòng được chọn, chuẩn bị yêu cầu REQUESTED cùng chi tiết trả.
3. Nhánh lưu hiện thiếu customer bắt buộc, nên chưa có kết quả tạo thành công được xác minh trên persistence thật.

**Luồng lựa chọn / ngoại lệ**

- Gửi yêu cầu trả hàng: Đường persistence thật không đáp ứng @NotNull/nullable=false cho customer: không có main success được chứng minh.; Items DTO không có @Valid cascade, quantity nested chưa validated ở controller.; Không kiểm orderItem thuộc order, quantity <= ordered, order status, owner hoặc duplicate request.; Order/orderItem không có: generic Exception 500.

**Postconditions theo nhánh**

- Gửi yêu cầu trả hàng: Không thể khẳng định request được tạo thành công; entity customer bắt buộc đang thiếu.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:22](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L22) | ReturnRestController · create | POST /api/returns |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:34](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L34) | ReturnServiceImpl · create | — |
| [src/main/java/com/thinh/cosmetic/domain/entity/returns/ReturnRequestEntity.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/entity/returns/ReturnRequestEntity.java#L1) | ReturnRequestEntity · entity/DTO field | — |
| [src/main/java/com/thinh/cosmetic/domain/dto/request/returns/ReturnRequestDto.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/returns/ReturnRequestDto.java#L1) | ReturnRequestDto · entity/DTO field | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/resources/templates/account/order-detail.html:255](../../../../src/main/resources/templates/account/order-detail.html#L255) | UI | — |
| [src/main/resources/templates/account/order-detail.html:290](../../../../src/main/resources/templates/account/order-detail.html#L290) | UI | — |
| [src/main/resources/templates/account/order-detail.html:365](../../../../src/main/resources/templates/account/order-detail.html#L365) | UI | — |
| [src/main/resources/templates/account/order-detail.html:392](../../../../src/main/resources/templates/account/order-detail.html#L392) | UI | — |

**UI và giới hạn triển khai**

- ReturnServiceTest mocks save, không validate entity/DB, nên success test không chứng minh persist thật.
- Không có cơ chế refund hoặc nhập lại inventory.
- UI: UI visible only COMPLETED and no prior request. Check subset order items; quantity hidden full purchased qty, no partial-quantity editing. Sends orderId/reason/description/items. Backend customer required association missing per commerce analysis.

<a id="uc041"></a>

## UC041 — Theo dõi yêu cầu trả hàng

**Primary Actor:** Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Yêu cầu trả hàng  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng, Nhân viên: theo dõi yêu cầu trả hàng.

**Trigger:** Actor lựa chọn theo dõi yêu cầu trả hàng.

**Preconditions theo nhánh**

- Theo dõi yêu cầu trả hàng: API không cần đăng nhập; record/order/customer dùng để tra cứu.

**Main Success Scenario**

1. Khách hàng hoặc nhân viên yêu cầu xem các yêu cầu trả hàng liên quan đến đơn.
2. Hệ thống trả lý do, các dòng/số lượng, trạng thái, người xử lý và thời điểm xử lý.

**Luồng lựa chọn / ngoại lệ**

- Theo dõi yêu cầu trả hàng: Không request với ID: generic Exception 500.; Không record trong query list: danh sách rỗng.; Không kiểm owner/customer/store scope.

**Postconditions theo nhánh**

- Theo dõi yêu cầu trả hàng: Request records được đọc.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:27](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L27) | ReturnRestController · getById | GET /api/returns/{id} |
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:32](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L32) | ReturnRestController · getAll | GET /api/returns |
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:37](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L37) | ReturnRestController · getByCustomer | GET /api/returns/customer/{customerId} |
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:42](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L42) | ReturnRestController · getByOrder | GET /api/returns/order/{orderId} |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:65](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L65) | ReturnServiceImpl · getById | — |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:73](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L73) | ReturnServiceImpl · getAll | — |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:79](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L79) | ReturnServiceImpl · getByCustomer | — |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:85](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L85) | ReturnServiceImpl · getByOrder | — |
| [src/main/java/com/thinh/cosmetic/repository/returns/ReturnRequestRepository.java:10](../../../../src/main/java/com/thinh/cosmetic/repository/returns/ReturnRequestRepository.java#L10) | ReturnRequestRepository · findByOrderId | — |
| [src/main/java/com/thinh/cosmetic/repository/returns/ReturnRequestRepository.java:13](../../../../src/main/java/com/thinh/cosmetic/repository/returns/ReturnRequestRepository.java#L13) | ReturnRequestRepository · findByCustomerId | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:88](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L88) | AccountPageController · implementation | — |
| [src/main/resources/templates/account/order-detail.html:220](../../../../src/main/resources/templates/account/order-detail.html#L220) | UI | — |
| [src/main/resources/templates/admin/order-detail.html:224](../../../../src/main/resources/templates/admin/order-detail.html#L224) | UI | — |
| [src/main/resources/templates/admin/order-detail.html:235](../../../../src/main/resources/templates/admin/order-detail.html#L235) | UI | — |

**UI và giới hạn triển khai**

- Public API operational access là thiếu auth; UI customer/admin access có ROLE gates ở page level.
- Description không có trong response dù entity có field.
- UI: SSR returnService.getByOrder presents statuses/reason on same detail; no cancel/withdraw return UI.
- UI: Displays first return only (returns[0]), hidden card if none.

<a id="uc042"></a>

## UC042 — Cập nhật xử lý trả hàng

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Yêu cầu trả hàng  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: cập nhật xử lý trả hàng.

**Trigger:** Actor lựa chọn cập nhật xử lý trả hàng.

**Preconditions theo nhánh**

- Duyệt yêu cầu trả hàng: API công khai; ReturnRequest tồn tại; employeeId request/default 1. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Từ chối yêu cầu trả hàng: API công khai; ReturnRequest tồn tại; employeeId request/default 1. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Ghi nhận đang xử lý trả hàng: API công khai; ReturnRequest tồn tại; employeeId request/default 1. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.
- Ghi nhận hoàn tất trả hàng: API công khai; ReturnRequest tồn tại; employeeId request/default 1. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Nhân viên chọn chấp thuận, từ chối, đang xử lý hoặc đánh dấu hoàn tất yêu cầu trả hàng.
2. Hệ thống tìm yêu cầu, đổi sang APPROVED/REJECTED/PROCESSING/COMPLETED tương ứng.
3. Hệ thống ghi người và thời điểm xử lý; thao tác hiện chưa hoàn tiền hoặc nhập hàng trả lại vào kho.

**Luồng lựa chọn / ngoại lệ**

- Duyệt yêu cầu trả hàng: Không request: generic Exception 500.; Không employee: vẫn cập nhật, không lỗi.; Guest có thể gọi endpoint; không kiểm employee identity, StoreScope hoặc owner.; Có thể cập nhật từ trạng thái nào sang trạng thái nào.
- Từ chối yêu cầu trả hàng: Không request: generic Exception 500.; Không employee: vẫn cập nhật, không lỗi.; Guest có thể gọi endpoint; không kiểm employee identity, StoreScope hoặc owner.; Có thể cập nhật từ trạng thái nào sang trạng thái nào.
- Ghi nhận đang xử lý trả hàng: Không request: generic Exception 500.; Không employee: vẫn cập nhật, không lỗi.; Guest có thể gọi endpoint; không kiểm employee identity, StoreScope hoặc owner.; Có thể cập nhật từ trạng thái nào sang trạng thái nào.
- Ghi nhận hoàn tất trả hàng: Không request: generic Exception 500.; Không employee: vẫn cập nhật, không lỗi.; Guest có thể gọi endpoint; không kiểm employee identity, StoreScope hoặc owner.; Có thể cập nhật từ trạng thái nào sang trạng thái nào.

**Postconditions theo nhánh**

- Duyệt yêu cầu trả hàng: Request status=APPROVED, processedAt cập nhật; không phát sinh hoàn kho/hoàn tiền.
- Từ chối yêu cầu trả hàng: Request status=REJECTED, processedAt cập nhật; không phát sinh hoàn kho/hoàn tiền.
- Ghi nhận đang xử lý trả hàng: Request status=PROCESSING, processedAt cập nhật; không phát sinh hoàn kho/hoàn tiền.
- Ghi nhận hoàn tất trả hàng: Request status=COMPLETED, processedAt cập nhật; không phát sinh hoàn kho/hoàn tiền.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L47) | ReturnRestController · updateStatus | PUT /api/returns/{id}/status?status=APPROVED&employeeId=... |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:90](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L90) | ReturnServiceImpl · updateStatus | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/resources/templates/admin/order-detail.html:253](../../../../src/main/resources/templates/admin/order-detail.html#L253) | UI | — |
| [src/main/resources/templates/admin/order-detail.html:285](../../../../src/main/resources/templates/admin/order-detail.html#L285) | UI | — |
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L47) | ReturnRestController · updateStatus | PUT /api/returns/{id}/status?status=REJECTED&employeeId=... |
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L47) | ReturnRestController · updateStatus | PUT /api/returns/{id}/status?status=PROCESSING&employeeId=... |
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L47) | ReturnRestController · updateStatus | PUT /api/returns/{id}/status?status=COMPLETED&employeeId=... |

**UI và giới hạn triển khai**

- UI admin chỉ có APPROVED/REJECTED khi REQUESTED; PROCESSING/COMPLETED là backend-only.
- Actor UI intended là employee, access enforcement actual là PUBLIC.
- UI: Buttons only for REQUESTED first return; no complete/restock button.

<a id="uc043"></a>

## UC043 — Xem đánh giá sản phẩm

**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đánh giá sản phẩm  
**Implementation Status:** Backend Only

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách vãng lai, Khách hàng, Nhân viên: xem đánh giá sản phẩm.

**Trigger:** Actor lựa chọn xem đánh giá sản phẩm.

**Preconditions theo nhánh**

- Xem đánh giá sản phẩm: Product ID được chỉ định; API công khai.

**Main Success Scenario**

1. Người dùng yêu cầu các đánh giá đang VISIBLE của một sản phẩm.
2. Hệ thống trả điểm, nội dung, tên khách và thời điểm đánh giá; tab đánh giá storefront hiện chưa dùng dữ liệu này.

**Luồng lựa chọn / ngoại lệ**

- Xem đánh giá sản phẩm: Không có review hoặc chỉ HIDDEN/PENDING: danh sách rỗng.; Không kiểm product tồn tại riêng.

**Postconditions theo nhánh**

- Xem đánh giá sản phẩm: Visible reviews được đọc.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java:30](../../../../src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java#L30) | ReviewRestController · getByProduct | GET /api/reviews/product/{productId} |
| [src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java:44](../../../../src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java#L44) | ReviewServiceImpl · getByProduct | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |

**UI và giới hạn triển khai**

- Frontend tab review tĩnh, không gọi API => Backend Only sau merge UI.

<a id="uc044"></a>

## UC044 — Viết đánh giá sản phẩm

**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đánh giá sản phẩm  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Khách hàng: viết đánh giá sản phẩm.

**Trigger:** Actor lựa chọn viết đánh giá sản phẩm.

**Preconditions theo nhánh**

- Viết đánh giá sản phẩm: API công khai; customerId query/default 1 và product tồn tại; DTO orderId bắt buộc, rating 1..5. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Khách hàng cung cấp sản phẩm, đơn tham chiếu, điểm từ 1 đến 5 và nội dung đánh giá.
2. Hệ thống tìm sản phẩm/khách, lưu đánh giá trạng thái VISIBLE và trả kết quả.
3. Các kiểm tra đã mua hàng, đơn thuộc khách, đơn hoàn tất và đánh giá trùng chưa được thực hiện.

**Luồng lựa chọn / ngoại lệ**

- Viết đánh giá sản phẩm: Product/customer không có: generic Exception 500.; orderId không có: vẫn tạo review với order=null.; Không kiểm customer sở hữu order, order COMPLETED hay product nằm trong order; không chống duplicate.; Review xuất hiện VISIBLE ngay dù entity default là PENDING.

**Postconditions theo nhánh**

- Viết đánh giá sản phẩm: Review được lưu VISIBLE nếu product/customer/validation hợp lệ.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java:22](../../../../src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java#L22) | ReviewRestController · create | POST /api/reviews?customerId=... |
| [src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java:28](../../../../src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java#L28) | ReviewServiceImpl · create | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |

**UI và giới hạn triển khai**

- Không có UI viết review; API hiện không yêu cầu xác thực, customerId không được lấy từ AuthPrincipal.

<a id="uc045"></a>

## UC045 — Quản lý hiển thị đánh giá

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đánh giá sản phẩm  
**Implementation Status:** Partially Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: quản lý hiển thị đánh giá.

**Trigger:** Actor lựa chọn quản lý hiển thị đánh giá.

**Preconditions theo nhánh**

- Kiểm tra danh sách đánh giá: API công khai.
- Ẩn hoặc hiển thị đánh giá: API công khai; reviewId tồn tại; ReviewStatus enum hợp lệ. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Nhân viên yêu cầu danh sách đánh giá hoặc chọn đánh giá cần đổi trạng thái hiển thị.
2. Hệ thống trả danh sách hoặc cập nhật moderationStatus đã chọn.
3. Hệ thống trả kết quả; chưa ghi người kiểm duyệt hoặc kiểm quyền kiểm duyệt.

**Luồng lựa chọn / ngoại lệ**

- Kiểm tra danh sách đánh giá: Không reviews: danh sách rỗng.
- Ẩn hoặc hiển thị đánh giá: Không có review: generic Exception 500.; Guest hoặc customer đều có thể update vì permitAll.; ReviewStatus enum không hợp lệ: framework reject request.

**Postconditions theo nhánh**

- Kiểm tra danh sách đánh giá: Toàn bộ reviews được trả.
- Ẩn hoặc hiển thị đánh giá: Status review thay đổi và ảnh hưởng product visible list.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- API permitAll; không yêu cầu xác thực.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java:35](../../../../src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java#L35) | ReviewRestController · getAll | GET /api/reviews |
| [src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java:52](../../../../src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java#L52) | ReviewServiceImpl · getAll | — |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | SecurityConfig · securityFilterChain | — |
| [src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java:40](../../../../src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java#L40) | ReviewRestController · updateModerationStatus | PUT /api/reviews/{id}/status?status=... |
| [src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java:57](../../../../src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java#L57) | ReviewServiceImpl · updateModerationStatus | — |

**UI và giới hạn triển khai**

- Không có UI quản trị review hoặc kiểm quyền REVIEW_MODERATE.
- Không có UI moderation; không ghi moderator/audit timestamp.

<a id="uc046"></a>

## UC046 — Xem tổng quan vận hành

**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có hệ thống ngoài được triển khai.  
**Module:** Đặt hàng và vận hành đơn  
**Implementation Status:** Implemented

**Mục tiêu và hoàn cảnh:** Mục tiêu của Nhân viên: xem tổng quan vận hành.

**Trigger:** Actor lựa chọn xem tổng quan vận hành.

**Preconditions theo nhánh**

- Xem tổng quan vận hành: ROLE_EMPLOYEE; đơn được lọc theo StoreScope ở API /api/admin/orders.

**Main Success Scenario**

1. Nhân viên yêu cầu tổng quan đơn trong phạm vi cửa hàng được phép.
2. Hệ thống tổng hợp doanh thu từ đơn COMPLETED, đếm các trạng thái và hiển thị các đơn gần nhất.
3. Hệ thống hiển thị tồn thấp của một cửa hàng; chưa có báo cáo theo kỳ hoặc xuất báo cáo.

**Luồng lựa chọn / ngoại lệ**

- Xem tổng quan vận hành: ADMIN có thể tổng hợp đơn toàn chuỗi; tồn thấp vẫn dùng một cửa hàng đầu.; Không có đơn: KPI bằng 0; không có báo cáo theo ngày, biểu đồ chuỗi thời gian hoặc xuất báo cáo.

**Postconditions theo nhánh**

- Xem tổng quan vận hành: Dashboard hiển thị dữ liệu và phép tổng hợp; không đổi DB.

**Failed End Condition:** Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.

**Quyền được thực thi hiện tại**

- ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Nguồn code / class / method / endpoint**

| File | Class / method / field | Endpoint |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/controller/AdminPageController.java:55](../../../../src/main/java/com/thinh/cosmetic/controller/AdminPageController.java#L55) | AdminPageController · dashboard | GET /admin |
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:22](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L22) | AdminOrderRestController · getOrders | GET /api/admin/orders |
| [src/main/resources/templates/admin/dashboard.html:200](../../../../src/main/resources/templates/admin/dashboard.html#L200) | UI | — |
| [src/main/resources/templates/admin/dashboard.html:220](../../../../src/main/resources/templates/admin/dashboard.html#L220) | UI | — |

**UI và giới hạn triển khai**

- UI: KPI computed in browser from live order list: sum totalAmount of COMPLETED, pending, preparing+shipping, recent first six orders. No date filter/time series/top-selling. Low stock fetched separately.
