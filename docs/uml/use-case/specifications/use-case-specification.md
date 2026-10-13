# Đặc tả Use Case theo implementation

**Ngày khảo sát: 13/10/2026.** Mọi scenario dưới đây mô tả code đã đọc; `Implemented` là mức xác minh tĩnh, không khẳng định đã chạy thử production.

Actor chính là người dùng giao diện hoặc client API có mục tiêu. Association với **Người gọi API công khai** ghi truy cập hiện tại do `permitAll`, không trao quyền nghiệp vụ mới. Supporting actor không có vì chưa thấy tích hợp hệ thống ngoài.

Đăng nhập là precondition ở chức năng được bảo vệ. Không tự tạo include/extend từ CRUD, navigation hay các bước trạng thái. Mã voucher là nhánh điều kiện trong preview/place; chưa tách thành lời gọi hành vi tái sử dụng đủ rõ để khẳng định một connector UML.

| ID | Use case | Module | Status |
|---|---|---|---|
| [UC001](#uc001) | Đăng ký tài khoản khách hàng | Xác thực và phiên đăng nhập | Partially Implemented |
| [UC002](#uc002) | Đăng nhập | Xác thực và phiên đăng nhập | Partially Implemented |
| [UC003](#uc003) | Đăng xuất | Xác thực và phiên đăng nhập | Implemented |
| [UC004](#uc004) | Đặt lại mật khẩu | Xác thực và phiên đăng nhập | Partially Implemented |
| [UC005](#uc005) | Xem hồ sơ khách hàng | Hồ sơ khách hàng và địa chỉ | Implemented |
| [UC006](#uc006) | Cập nhật hồ sơ khách hàng | Hồ sơ khách hàng và địa chỉ | Partially Implemented |
| [UC007](#uc007) | Xem sổ địa chỉ | Hồ sơ khách hàng và địa chỉ | Implemented |
| [UC008](#uc008) | Thêm địa chỉ nhận hàng | Hồ sơ khách hàng và địa chỉ | Implemented |
| [UC009](#uc009) | Cập nhật địa chỉ nhận hàng | Hồ sơ khách hàng và địa chỉ | Partially Implemented |
| [UC010](#uc010) | Xóa địa chỉ nhận hàng | Hồ sơ khách hàng và địa chỉ | Implemented |
| [UC011](#uc011) | Xem hồ sơ làm đẹp | Hồ sơ khách hàng và địa chỉ | Backend Only |
| [UC012](#uc012) | Cập nhật hồ sơ làm đẹp | Hồ sơ khách hàng và địa chỉ | Backend Only |
| [UC013](#uc013) | Tra cứu danh sách khách hàng | Hồ sơ khách hàng và địa chỉ | Backend Only |
| [UC014](#uc014) | Đặt địa chỉ nhận hàng mặc định | Hồ sơ khách hàng và địa chỉ | Partially Implemented |
| [UC015](#uc015) | Tra cứu nhân viên | Nhân viên và phân công | Backend Only |
| [UC016](#uc016) | Tạo tài khoản nhân viên | Nhân viên và phân công | Partially Implemented |
| [UC017](#uc017) | Cập nhật nhân viên và phân công | Nhân viên và phân công | Partially Implemented |
| [UC018](#uc018) | Ngừng hoạt động nhân viên | Nhân viên và phân công | Partially Implemented |
| [UC019](#uc019) | Xem danh sách sản phẩm | Sản phẩm và tra cứu mỹ phẩm | Implemented |
| [UC020](#uc020) | Tìm kiếm và lọc sản phẩm | Sản phẩm và tra cứu mỹ phẩm | Implemented |
| [UC021](#uc021) | Xem chi tiết sản phẩm | Sản phẩm và tra cứu mỹ phẩm | Implemented |
| [UC022](#uc022) | Thêm sản phẩm | Sản phẩm và tra cứu mỹ phẩm | Backend Only |
| [UC023](#uc023) | Cập nhật sản phẩm | Sản phẩm và tra cứu mỹ phẩm | Backend Only |
| [UC024](#uc024) | Xóa sản phẩm | Sản phẩm và tra cứu mỹ phẩm | Backend Only |
| [UC025](#uc025) | Xem thương hiệu | Thương hiệu và danh mục | Implemented |
| [UC026](#uc026) | Thêm thương hiệu | Thương hiệu và danh mục | Backend Only |
| [UC027](#uc027) | Cập nhật thương hiệu | Thương hiệu và danh mục | Backend Only |
| [UC028](#uc028) | Xóa thương hiệu | Thương hiệu và danh mục | Backend Only |
| [UC029](#uc029) | Xem danh mục sản phẩm | Thương hiệu và danh mục | Implemented |
| [UC030](#uc030) | Thêm danh mục sản phẩm | Thương hiệu và danh mục | Backend Only |
| [UC031](#uc031) | Cập nhật danh mục sản phẩm | Thương hiệu và danh mục | Partially Implemented |
| [UC032](#uc032) | Xóa danh mục sản phẩm | Thương hiệu và danh mục | Backend Only |
| [UC033](#uc033) | Xem hệ thống cửa hàng | Cửa hàng | Implemented |
| [UC034](#uc034) | Thêm cửa hàng | Cửa hàng | Backend Only |
| [UC035](#uc035) | Cập nhật cửa hàng | Cửa hàng | Backend Only |
| [UC036](#uc036) | Ngừng hoạt động cửa hàng | Cửa hàng | Backend Only |
| [UC037](#uc037) | Tra cứu tồn sản phẩm theo cửa hàng | Tồn kho | Implemented |
| [UC038](#uc038) | Xem tồn kho cửa hàng | Tồn kho | Implemented |
| [UC039](#uc039) | Xem cảnh báo tồn thấp | Tồn kho | Implemented |
| [UC040](#uc040) | Điều chỉnh tồn kho | Tồn kho | Implemented |
| [UC041](#uc041) | Lập phiếu chuyển kho | Điều chuyển kho | Backend Only |
| [UC042](#uc042) | Xem phiếu chuyển kho | Điều chuyển kho | Backend Only |
| [UC043](#uc043) | Xác nhận xuất chuyển kho | Điều chuyển kho | Backend Only |
| [UC044](#uc044) | Xác nhận nhận chuyển kho | Điều chuyển kho | Backend Only |
| [UC045](#uc045) | Xem nhà cung cấp | Nhà cung cấp và nhập hàng | Backend Only |
| [UC046](#uc046) | Thêm nhà cung cấp | Nhà cung cấp và nhập hàng | Backend Only |
| [UC047](#uc047) | Cập nhật nhà cung cấp | Nhà cung cấp và nhập hàng | Backend Only |
| [UC048](#uc048) | Ngừng hợp tác nhà cung cấp | Nhà cung cấp và nhập hàng | Backend Only |
| [UC049](#uc049) | Lập đơn nhập hàng | Nhà cung cấp và nhập hàng | Backend Only |
| [UC050](#uc050) | Xem đơn nhập hàng | Nhà cung cấp và nhập hàng | Backend Only |
| [UC051](#uc051) | Xác nhận nhập hàng vào kho | Nhà cung cấp và nhập hàng | Backend Only |
| [UC052](#uc052) | Xem giỏ hàng | Giỏ hàng | Implemented |
| [UC053](#uc053) | Thêm sản phẩm vào giỏ | Giỏ hàng | Implemented |
| [UC054](#uc054) | Điều chỉnh số lượng trong giỏ | Giỏ hàng | Partially Implemented |
| [UC055](#uc055) | Xóa sản phẩm khỏi giỏ | Giỏ hàng | Partially Implemented |
| [UC056](#uc056) | Làm trống giỏ hàng | Giỏ hàng | Backend Only |
| [UC057](#uc057) | Xem sản phẩm yêu thích | Yêu thích | Implemented |
| [UC058](#uc058) | Lưu sản phẩm yêu thích | Yêu thích | Partially Implemented |
| [UC059](#uc059) | Bỏ sản phẩm yêu thích | Yêu thích | Partially Implemented |
| [UC060](#uc060) | Xem tổng tiền trước khi đặt hàng | Đặt hàng và xử lý đơn | Implemented |
| [UC061](#uc061) | Áp dụng mã ưu đãi khi thanh toán | Đặt hàng và xử lý đơn | Partially Implemented |
| [UC062](#uc062) | Đặt hàng thanh toán khi nhận | Đặt hàng và xử lý đơn | Partially Implemented |
| [UC063](#uc063) | Xem lịch sử đơn hàng | Đặt hàng và xử lý đơn | Implemented |
| [UC064](#uc064) | Theo dõi chi tiết đơn hàng | Đặt hàng và xử lý đơn | Partially Implemented |
| [UC065](#uc065) | Hủy đơn đang chờ xác nhận | Đặt hàng và xử lý đơn | Implemented |
| [UC066](#uc066) | Tra cứu đơn hàng trong phạm vi cửa hàng | Đặt hàng và xử lý đơn | Implemented |
| [UC067](#uc067) | Xem chi tiết đơn của cửa hàng | Đặt hàng và xử lý đơn | Implemented |
| [UC068](#uc068) | Xác nhận đơn hàng | Đặt hàng và xử lý đơn | Partially Implemented |
| [UC069](#uc069) | Chuyển đơn sang chuẩn bị hàng | Đặt hàng và xử lý đơn | Partially Implemented |
| [UC070](#uc070) | Bàn giao đơn để giao hàng | Đặt hàng và xử lý đơn | Partially Implemented |
| [UC071](#uc071) | Ghi nhận đơn giao thành công | Đặt hàng và xử lý đơn | Partially Implemented |
| [UC072](#uc072) | Hủy đơn trong phạm vi cửa hàng | Đặt hàng và xử lý đơn | Partially Implemented |
| [UC073](#uc073) | Gửi yêu cầu trả hàng | Yêu cầu trả hàng | Partially Implemented |
| [UC074](#uc074) | Theo dõi yêu cầu trả hàng | Yêu cầu trả hàng | Implemented |
| [UC075](#uc075) | Duyệt yêu cầu trả hàng | Yêu cầu trả hàng | Partially Implemented |
| [UC076](#uc076) | Từ chối yêu cầu trả hàng | Yêu cầu trả hàng | Partially Implemented |
| [UC077](#uc077) | Ghi nhận đang xử lý trả hàng | Yêu cầu trả hàng | Partially Implemented |
| [UC078](#uc078) | Ghi nhận hoàn tất trả hàng | Yêu cầu trả hàng | Partially Implemented |
| [UC079](#uc079) | Xem đánh giá sản phẩm | Đánh giá sản phẩm | Backend Only |
| [UC080](#uc080) | Viết đánh giá sản phẩm | Đánh giá sản phẩm | Partially Implemented |
| [UC081](#uc081) | Kiểm tra danh sách đánh giá | Đánh giá sản phẩm | Backend Only |
| [UC082](#uc082) | Ẩn hoặc hiển thị đánh giá | Đánh giá sản phẩm | Partially Implemented |
| [UC083](#uc083) | Xem tổng quan vận hành | Tổng quan vận hành | Implemented |
| [UC084](#uc084) | Đổi giao diện sáng tối | Tùy chọn giao diện | UI Only |

<a id="uc001"></a>

## UC001 — Đăng ký tài khoản khách hàng

**Module:** Xác thực và phiên đăng nhập  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách vãng lai  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /register  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Tạo tài khoản CUSTOMER và hồ sơ khách hàng với mật khẩu băm BCrypt.

**Preconditions**

- Email/SĐT chưa tồn tại; dữ liệu theo RegisterRequest; không cần xác thực.

**Main Success Scenario**

1. Khách nhập họ tên, email, SĐT và mật khẩu tối thiểu 6 ký tự.
2. Service chuẩn hóa email, SĐT và họ tên; kiểm tra trùng email/SĐT.
3. Lưu account ACTIVE, customer có loyaltyPoints=0 và joinDate hiện tại.
4. API trả hồ sơ HTTP 201 nếu request chứa fullName đúng; UI hiện tại gửi displayName nên không đến nhánh thành công.

**Alternative / Exception Flows**

- Trùng email hoặc SĐT: 409 DUPLICATE_EMAIL/DUPLICATE_PHONE.
- Dữ liệu sai @Valid: 400; không gửi email/OTP xác minh.
- register.html gửi displayName thay vì fullName bắt buộc -> 400 validation; không tạo tài khoản từ form hiện tại.
- Toast hứa tự động đăng nhập nhưng không có cookie/JWT hoặc lời gọi login.

**Postconditions**

- Gọi API đúng payload tạo account/customer; form hiện tại chưa hoàn tất đăng ký.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:32](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L32) | `AuthRestController` · `register` | `POST /api/auth/register` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java:47](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java#L47) | `AccountServiceImpl` · `register` | `—` |
| [src/main/resources/templates/auth/register.html:102](../../../../src/main/resources/templates/auth/register.html#L102) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L31) | `AuthRestController` · `implementation` | `—` |
| [src/main/java/com/thinh/cosmetic/domain/dto/request/account/RegisterRequest.java:16](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/account/RegisterRequest.java#L16) | `RegisterRequest` · `implementation` | `—` |

**Giới hạn và đối chiếu UI**

- UI: UI sends displayName rather than required RegisterRequest.fullName -> @Valid returns 400 before account creation. Even with corrected field, register responds 201 without JWT cookie and UI falsely says auto login without invoking login.

<a id="uc002"></a>

## UC002 — Đăng nhập

**Module:** Xác thực và phiên đăng nhập  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /login  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Xác thực email/SĐT/username, cấp JWT HS256 qua cookie HttpOnly LUNEA_AT.

**Preconditions**

- Có AccountEntity ACTIVE; mật khẩu đúng.

**Main Success Scenario**

1. Nhập identifier và password.
2. AuthenticationManager gọi AppUserDetailsService và kiểm mật khẩu.
3. Nạp account type, role và permission vào authorities; phát JWT ttl 7200 giây mặc định.
4. Trả cookie và redirectUrl /admin cho EMPLOYEE, / cho CUSTOMER.

**Alternative / Exception Flows**

- Identifier rỗng hoặc sai mật khẩu/account INACTIVE: INVALID_CREDENTIALS.
- Employee.status INACTIVE không được kiểm tại đăng nhập.
- UI đọc res.isEmployee (không có trong LoginResponse), bỏ response.redirectUrl; employee đăng nhập thành công từ /login mặc định chuyển về / thay vì /admin.

**Postconditions**

- Cookie/JWT đã cấp khi credentials hợp lệ; backend trả redirectUrl nhưng UI chưa xử lý đúng hướng chuyển cho employee.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L38) | `AuthRestController` · `login` | `POST /api/auth/login` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java:83](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java#L83) | `AccountServiceImpl` · `login` | `—` |
| [src/main/resources/templates/auth/login.html:268](../../../../src/main/resources/templates/auth/login.html#L268) | `UI` | `—` |
| [src/main/resources/static/js/api.js:65](../../../../src/main/resources/static/js/api.js#L65) | `UI` | `—` |
| [src/main/resources/templates/auth/login.html:295](../../../../src/main/resources/templates/auth/login.html#L295) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/domain/dto/response/account/LoginResponse.java:15](../../../../src/main/java/com/thinh/cosmetic/domain/dto/response/account/LoginResponse.java#L15) | `LoginResponse` · `implementation` | `—` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java:107](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java#L107) | `AccountServiceImpl` · `implementation` | `—` |

**Giới hạn và đối chiếu UI**

- Checkbox Ghi nhớ đăng nhập không được đọc; không có refresh token.
- UI: Identifier/password; apiFetch attaches X-Requested-With and backend login issues JWT cookie. UI checks res.isEmployee, but LoginResponse has accountType/redirectUrl and no isEmployee; UI ignores backend redirectUrl, so default employee login navigates / rather than /admin (unless request redirect parameter itself /admin). Remember checkbox unread.

<a id="uc003"></a>

## UC003 — Đăng xuất

**Module:** Xác thực và phiên đăng nhập  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** all authenticated pages  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Xóa cookie xác thực trên trình duyệt.

**Preconditions**

- Client có cookie (API cũng chấp nhận chưa đăng nhập).

**Main Success Scenario**

1. Người dùng chọn đăng xuất.
2. Client POST /api/auth/logout có X-Requested-With.
3. Controller gửi Set-Cookie với Max-Age=0; UI chuyển login.

**Alternative / Exception Flows**

- Bearer/JWT đã cấp không bị thu hồi phía server.

**Postconditions**

- Cookie client bị xóa; token đã sao chép có hiệu lực tới exp.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:48](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L48) | `AuthRestController` · `logout` | `POST /api/auth/logout` |
| [src/main/java/com/thinh/cosmetic/security/AuthCookieFactory.java:31](../../../../src/main/java/com/thinh/cosmetic/security/AuthCookieFactory.java#L31) | `AuthCookieFactory` · `createLogoutCookie` | `—` |
| [src/main/resources/static/js/api.js:512](../../../../src/main/resources/static/js/api.js#L512) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/AuthPageController.java:30](../../../../src/main/java/com/thinh/cosmetic/controller/AuthPageController.java#L30) | `AuthPageController` · `implementation` | `—` |

**Giới hạn và đối chiếu UI**

- Hành vi ở AuthRestController.logout/AuthCookieFactory.createLogoutCookie, không có AccountServiceImpl.logout. Spring Security cũng có /logout.
- UI: LUNEA.logout POST with credentials/include header then full GET /logout; AuthPageController evicts JWT cookie/clears context/session.

<a id="uc004"></a>

## UC004 — Đặt lại mật khẩu

**Module:** Xác thực và phiên đăng nhập  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách vãng lai  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /login  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Đổi mật khẩu theo email/SĐT/username đã nhập.

**Preconditions**

- Account tồn tại; newPassword tối thiểu 6 ký tự; API public.

**Main Success Scenario**

1. Nhập emailOrPhone và newPassword.
2. Service tìm account, băm mật khẩu mới và lưu.
3. API trả thông báo thành công.

**Alternative / Exception Flows**

- Không tìm thấy tài khoản: 404; identifier trống: 400.
- UI fetch thiếu X-Requested-With nên filter trả 403 trước controller.
- Không yêu cầu OTP, reset token hay chứng minh quyền sở hữu.

**Postconditions**

- Nếu gọi API đúng header, hash mật khẩu đổi; UI hiện tại không hoàn thành.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java:56](../../../../src/main/java/com/thinh/cosmetic/rest/account/AuthRestController.java#L56) | `AuthRestController` · `resetPassword` | `POST /api/auth/reset-password` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java:123](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/AccountServiceImpl.java#L123) | `AccountServiceImpl` · `resetPassword` | `—` |
| [src/main/resources/templates/auth/login.html:213](../../../../src/main/resources/templates/auth/login.html#L213) | `UI` | `—` |
| [src/main/resources/templates/auth/login.html:239](../../../../src/main/resources/templates/auth/login.html#L239) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java:36](../../../../src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java#L36) | `XRequestedWithFilter` · `implementation` | `—` |

**Giới hạn và đối chiếu UI**

- UI: Modal sends emailOrPhone/newPassword, confirm local. Raw fetch omits X-Requested-With, filter rejects 403. Backend does not use Email/OTP actor.
- Đổi mật khẩu không thu hồi JWT hiện có; token tiếp tục tới exp.

<a id="uc005"></a>

## UC005 — Xem hồ sơ khách hàng

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/profile  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Xem tên, thông tin cá nhân và điểm tích lũy của một customerId.

**Preconditions**

- Customer id tồn tại; trang /account/profile yêu cầu ROLE_CUSTOMER nhưng API public.

**Main Success Scenario**

1. Trang /account/profile lấy principal.customerId và gọi CustomerService.getProfile trực tiếp (SSR).
2. Service tìm CustomerEntity và map hồ sơ vào model.
3. Template hiển thị thông tin; GET /api/customers/{id} là đường API đọc thay thế.

**Alternative / Exception Flows**

- Id không tồn tại: generic Exception được trả INTERNAL_ERROR 500.
- API không kiểm ownership, cho phép đọc customer bất kỳ.

**Postconditions**

- Không đổi dữ liệu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:30](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L30) | `CustomerRestController` · `getById` | `GET /api/customers/{id}` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:125](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L125) | `CustomerServiceImpl` · `getCustomerById` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:35](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L35) | `AccountPageController` · `implementation` | `—` |
| [src/main/resources/templates/account/profile.html:80](../../../../src/main/resources/templates/account/profile.html#L80) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:38](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L38) | `CustomerServiceImpl` · `getProfile` | `—` |

**Giới hạn và đối chiếu UI**

- UI: SSR calls customerService.getProfile and getAddresses; displays loyaltyPoints only (no redemption/accrual UI). Controller falls back customerId=1 when principal ID missing.

<a id="uc006"></a>

## UC006 — Cập nhật hồ sơ khách hàng

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/profile  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Cập nhật họ tên, ngày sinh, giới tính và số điện thoại.

**Preconditions**

- Customer tồn tại; fullName hợp lệ theo CustomerProfileRequest.

**Main Success Scenario**

1. Nhập hồ sơ và gửi PUT.
2. Service đổi các field khác null; phone cập nhật trên AccountEntity.
3. Lưu và trả CustomerResponse.

**Alternative / Exception Flows**

- API public, không xác minh customerId thuộc principal.
- Phone trùng chỉ bị ràng buộc DB; CustomerMapper không map Account.phone vào CustomerResponse.phone.

**Postconditions**

- Thông tin đã lưu; phone có thể không xuất hiện trong response.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:35](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L35) | `CustomerRestController` · `updateProfile` | `PUT /api/customers/{id}/profile` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:42](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L42) | `CustomerServiceImpl` · `updateProfile` | `—` |
| [src/main/resources/templates/account/profile.html:324](../../../../src/main/resources/templates/account/profile.html#L324) | `UI` | `—` |
| [src/main/resources/templates/account/profile.html:334](../../../../src/main/resources/templates/account/profile.html#L334) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- CustomerMapper chỉ mapping account.id và account.email; CustomerEntity không có phone -> CustomerResponse.phone không map.
- UI: Full name/phone/dob/gender; email disabled, no password-change form.

<a id="uc007"></a>

## UC007 — Xem sổ địa chỉ

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/profile  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Liệt kê địa chỉ theo customerId.

**Preconditions**

- Trang hồ sơ cần ROLE_CUSTOMER; API public.

**Main Success Scenario**

1. UI chọn sổ địa chỉ.
2. Repository tìm customerEntityId.
3. Trả danh sách địa chỉ và cờ mặc định.

**Alternative / Exception Flows**

- Id không có địa chỉ trả danh sách rỗng.
- API không kiểm ownership.

**Postconditions**

- Không đổi dữ liệu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:43](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L43) | `CustomerRestController` · `getAddresses` | `GET /api/customers/{id}/addresses` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:55](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L55) | `CustomerServiceImpl` · `getAddresses` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:43](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L43) | `AccountPageController` · `implementation` | `—` |
| [src/main/resources/templates/account/profile.html:402](../../../../src/main/resources/templates/account/profile.html#L402) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- UI: Initial SSR addresses; edit/default actions fetch actual list.

<a id="uc008"></a>

## UC008 — Thêm địa chỉ nhận hàng

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/profile  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Lưu địa chỉ nhận hàng và tùy chọn đặt mặc định.

**Preconditions**

- Customer tồn tại; recipientName/phone/province/ward/addressDetail hợp lệ.

**Main Success Scenario**

1. Nhập địa chỉ và cờ mặc định.
2. Nếu isDefault=true, đặt các địa chỉ đang có về false.
3. Map request, gán customer, lưu và trả 201.

**Alternative / Exception Flows**

- Không có customer: 500; trường bắt buộc sai: 400.
- isDefault bị bỏ qua/null có thể vi phạm Entity @NotNull; UI gửi boolean.
- Không kiểm ownership.

**Postconditions**

- Địa chỉ mới được lưu; khi thêm mặc định, các địa chỉ cũ bị bỏ mặc định.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:48](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L48) | `CustomerRestController` · `addAddress` | `POST /api/customers/{id}/addresses` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:61](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L61) | `CustomerServiceImpl` · `addAddress` | `—` |
| [src/main/resources/templates/account/profile.html:384](../../../../src/main/resources/templates/account/profile.html#L384) | `UI` | `—` |
| [src/main/resources/templates/account/profile.html:448](../../../../src/main/resources/templates/account/profile.html#L448) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- UI: Recipient/phone/province/district/ward/detail/default from modal. Static locations includes 5 provinces but UI select only HCM/HN/DN.

<a id="uc009"></a>

## UC009 — Cập nhật địa chỉ nhận hàng

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/profile  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Sửa các thông tin trên địa chỉ đã có.

**Preconditions**

- Address id tồn tại; request hợp lệ.

**Main Success Scenario**

1. Gửi PUT customerId/addressId.
2. Service tìm theo addressId rồi mapper cập nhật field khác null.
3. Lưu địa chỉ và trả response.

**Alternative / Exception Flows**

- Không so sánh address.customer với customerId; không kiểm principal.
- Cập nhật isDefault=true không bỏ mặc định các địa chỉ khác.
- UI không nạp lại tỉnh/quận/phường khi mở edit, có thể gửi sai.

**Postconditions**

- Địa chỉ thay đổi; tính duy nhất địa chỉ mặc định chưa bảo đảm.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:56](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L56) | `CustomerRestController` · `updateAddress` | `PUT /api/customers/{id}/addresses/{addressId}` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:73](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L73) | `CustomerServiceImpl` · `updateAddress` | `—` |
| [src/main/resources/templates/account/profile.html:398](../../../../src/main/resources/templates/account/profile.html#L398) | `UI` | `—` |
| [src/main/resources/templates/account/profile.html:441](../../../../src/main/resources/templates/account/profile.html#L441) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- UI: Edit fetch only fills recipient/phone/detail/default; province/district/ward not prefilled from existing record, can save stale controls.

<a id="uc010"></a>

## UC010 — Xóa địa chỉ nhận hàng

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/profile  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Xóa địa chỉ theo addressId.

**Preconditions**

- Address id được chọn; API public.

**Main Success Scenario**

1. Người dùng chọn xóa.
2. Service gọi deleteById(addressId) và trả 204.

**Alternative / Exception Flows**

- customerId bị bỏ qua, không kiểm ownership.
- Xóa địa chỉ mặc định không chọn một địa chỉ khác làm mặc định.

**Postconditions**

- Địa chỉ bị xóa nếu tồn tại.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:65](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L65) | `CustomerRestController` · `deleteAddress` | `DELETE /api/customers/{id}/addresses/{addressId}` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:81](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L81) | `CustomerServiceImpl` · `deleteAddress` | `—` |
| [src/main/resources/templates/account/profile.html:186](../../../../src/main/resources/templates/account/profile.html#L186) | `UI` | `—` |
| [src/main/resources/templates/account/profile.html:482](../../../../src/main/resources/templates/account/profile.html#L482) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- UI: Confirmation; UI hides delete for default address, non-default card removed on success.

<a id="uc011"></a>

## UC011 — Xem hồ sơ làm đẹp

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Backend Only  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Xem thông tin loại da, vấn đề da, nhu cầu và khoảng giá quan tâm.

**Preconditions**

- Trang cần customer; API public.

**Main Success Scenario**

1. UI mở hồ sơ làm đẹp.
2. Service tìm BeautyProfile theo customerId.
3. Trả profile hoặc null.

**Alternative / Exception Flows**

- Chưa có profile: response null, UI cho nhập mới.
- Không xác minh chủ thể sở hữu.

**Postconditions**

- Không đổi dữ liệu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:71](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L71) | `CustomerRestController` · `getBeautyProfile` | `GET /api/customers/{id}/beauty-profile` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:87](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L87) | `CustomerServiceImpl` · `getBeautyProfile` | `—` |

<a id="uc012"></a>

## UC012 — Cập nhật hồ sơ làm đẹp

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Backend Only  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Tạo hoặc cập nhật hồ sơ làm đẹp; không tự gợi ý sản phẩm.

**Preconditions**

- Customer tồn tại; API public.

**Main Success Scenario**

1. Người dùng nhập loại da, nhu cầu và sở thích.
2. Tìm profile hiện có hoặc tạo mới gắn customer.
3. Gán toàn bộ field request, updatedAt rồi lưu.

**Alternative / Exception Flows**

- Customer không tồn tại: 500.
- DTO không có Bean Validation; entity có giới hạn kích thước, có thể lỗi khi lưu.
- Field null ghi đè dữ liệu cũ; không kiểm ownership.

**Postconditions**

- BeautyProfile được lưu; không có recommendation service.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:76](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L76) | `CustomerRestController` · `updateBeautyProfile` | `PUT /api/customers/{id}/beauty-profile` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:99](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L99) | `CustomerServiceImpl` · `updateBeautyProfile` | `—` |

<a id="uc013"></a>

## UC013 — Tra cứu danh sách khách hàng

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Đọc toàn bộ hồ sơ khách hàng qua API, chưa có màn hình quản trị khách hàng.

**Preconditions**

- API public, không cần CUSTOMER_MANAGE.

**Main Success Scenario**

1. Client gọi GET /api/customers.
2. Repository findAll, map từng hồ sơ.
3. Trả danh sách không phân trang.

**Alternative / Exception Flows**

- Có thể rỗng; không filter phạm vi và không kiểm quyền.

**Postconditions**

- Không đổi dữ liệu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:25](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L25) | `CustomerRestController` · `getAll` | `GET /api/customers` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:119](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L119) | `CustomerServiceImpl` · `getAllCustomers` | `—` |

<a id="uc014"></a>

## UC014 — Đặt địa chỉ nhận hàng mặc định

**Module:** Hồ sơ khách hàng và địa chỉ  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/profile  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Chọn một địa chỉ có sẵn làm địa chỉ mặc định.

**Preconditions**

- Address id tồn tại; request hợp lệ.

**Main Success Scenario**

1. UI tải danh sách địa chỉ và tìm địa chỉ được chọn.
2. Gán isDefault=true, gửi toàn bộ địa chỉ bằng PUT.
3. Mapper lưu cờ trên địa chỉ này; không xóa cờ mặc định của địa chỉ khác.

**Alternative / Exception Flows**

- Không so sánh địa chỉ với customerId hoặc principal; API công khai.
- Có thể tồn tại nhiều địa chỉ isDefault=true vì update không reset các địa chỉ còn lại.
- Địa chỉ không tồn tại: UI không tìm được target hoặc API trả lỗi.

**Postconditions**

- Địa chỉ được chọn có isDefault=true; có thể tồn tại nhiều địa chỉ mặc định.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java:56](../../../../src/main/java/com/thinh/cosmetic/rest/account/CustomerRestController.java#L56) | `CustomerRestController` · `updateAddress` | `PUT /api/customers/{id}/addresses/{addressId}` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java:73](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/CustomerServiceImpl.java#L73) | `CustomerServiceImpl` · `updateAddress` | `—` |
| [src/main/resources/templates/account/profile.html:464](../../../../src/main/resources/templates/account/profile.html#L464) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- UI: Fetch list, set target.isDefault=true, submit whole target. No dedicated endpoint.

<a id="uc015"></a>

## UC015 — Tra cứu nhân viên

**Module:** Nhân viên và phân công  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Xem danh sách hoặc chi tiết nhân viên kèm tên vai trò và chi nhánh.

**Preconditions**

- API public; id tồn tại đối với chi tiết.

**Main Success Scenario**

1. Client gọi danh sách/chi tiết.
2. Tìm employee; query employeeRole và employeeStore.
3. Trả response tên vai trò/chi nhánh, trạng thái và tài khoản.

**Alternative / Exception Flows**

- Id không tồn tại: 500 generic Exception.
- Không EMPLOYEE_MANAGE; không phân trang.

**Postconditions**

- Không đổi dữ liệu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java#L31) | `EmployeeRestController` · `getAll / getById` | `GET /api/employees; GET /api/employees/{id}` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java:92](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java#L92) | `EmployeeServiceImpl` · `getAll|getById` | `—` |

<a id="uc016"></a>

## UC016 — Tạo tài khoản nhân viên

**Module:** Nhân viên và phân công  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Tạo account EMPLOYEE, employee và tùy chọn gán vai trò/chi nhánh.

**Preconditions**

- email,password,fullName hợp lệ DTO; internalEmail cần cung cấp để service tạo username không rỗng.

**Main Success Scenario**

1. Client gửi thông tin employee với email và internalEmail.
2. Service dùng internalEmail cho account.email và username, kiểm trùng rồi băm password.
3. Lưu account và employee ACTIVE.
4. Gán từng roleId/storeId nếu tồn tại, trả 201.

**Alternative / Exception Flows**

- DTO bắt buộc email nhưng service bỏ qua email; internalEmail không bắt buộc nên có thể lỗi DB/validation.
- Role/store id không tồn tại bị bỏ qua im lặng.
- Fallback password 123456 trong service không đi qua REST @Valid nếu password trống.
- API public cho phép gán ADMIN nếu biết roleId.

**Postconditions**

- Nếu dữ liệu đáp ứng cả DTO/entity, tạo employee/account và các liên kết.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java#L21) | `EmployeeRestController` · `create` | `POST /api/employees` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java:32](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java#L32) | `EmployeeServiceImpl` · `create` | `—` |

<a id="uc017"></a>

## UC017 — Cập nhật nhân viên và phân công

**Module:** Nhân viên và phân công  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Đổi thông tin nhân viên, mật khẩu, danh sách vai trò và chi nhánh.

**Preconditions**

- Employee tồn tại; REST dùng EmployeeRequest bắt buộc email/password/fullName ngay cả update.

**Main Success Scenario**

1. Client gửi thông tin cập nhật.
2. Service đổi fullName/phone và hash password account nếu có.
3. Nếu roleIds hoặc storeIds khác null, xóa liên kết cũ rồi tạo liên kết cho id hợp lệ.
4. Lưu employee và trả response.

**Alternative / Exception Flows**

- Không đổi internalEmail/email/username dù DTO nhận.
- Employee.phone đổi nhưng Account.phone không đồng bộ.
- ID không tồn tại bị bỏ qua; danh sách rỗng xóa toàn bộ phân công.
- API public; JWT đã phát giữ authorities cũ tới hết hạn.

**Postconditions**

- Thông tin/phân công được lưu; quyền mới chỉ nạp khi đăng nhập lại.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java:36](../../../../src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java#L36) | `EmployeeRestController` · `update` | `PUT /api/employees/{id}` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java:97](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java#L97) | `EmployeeServiceImpl` · `update` | `—` |

<a id="uc018"></a>

## UC018 — Ngừng hoạt động nhân viên

**Module:** Nhân viên và phân công  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Đặt Employee.status=INACTIVE.

**Preconditions**

- Employee tồn tại; API public.

**Main Success Scenario**

1. Client yêu cầu DELETE employee.
2. Service tìm employee, đặt trạng thái INACTIVE và lưu.
3. Trả 204.

**Alternative / Exception Flows**

- Không đặt Account.status INACTIVE; AppUserDetailsService chỉ kiểm account, nhân viên vẫn đăng nhập được.
- Không thu hồi JWT hiện hành.

**Postconditions**

- Employee INACTIVE; không bảo đảm khóa tài khoản đăng nhập.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java:44](../../../../src/main/java/com/thinh/cosmetic/rest/account/EmployeeRestController.java#L44) | `EmployeeRestController` · `deactivate` | `DELETE /api/employees/{id}` |
| [src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java:133](../../../../src/main/java/com/thinh/cosmetic/service/account/impl/EmployeeServiceImpl.java#L133) | `EmployeeServiceImpl` · `deactivate` | `—` |

<a id="uc019"></a>

## UC019 — Xem danh sách sản phẩm

**Module:** Sản phẩm và tra cứu mỹ phẩm  
**Implementation Status:** Implemented  
**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /, /products, /product, /admin/products  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xem sản phẩm kèm thương hiệu, danh mục, ảnh, SKU, giá và tồn khả dụng trên toàn chuỗi.

**Preconditions**

- Không yêu cầu đăng nhập.

**Main Success Scenario**

1. Mở trang / hoặc /products, hoặc GET /api/products.
2. Hệ thống lấy toàn bộ sản phẩm, bổ sung SKU, ảnh, giá min/max, thuộc tính và tồn khả dụng.
3. Hiển thị kết quả sản phẩm.

**Alternative / Exception Flows**

- Không có ảnh: dùng ảnh chính, ảnh đầu theo sortOrder hoặc URL Unsplash mặc định.
- Không có SKU: giá min/max bằng 0, danh sách SKU rỗng.
- Không có tồn: tồn khả dụng bằng 0.

**Postconditions**

- Dữ liệu không thay đổi.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:42](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L42) | `ProductRestController` · `getAll` | `GET /api/products` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:88](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L88) | `ProductServiceImpl` · `getAll / enrich` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:27](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L27) | `HomeController` · `home` | `GET /` |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:29](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L29) | `ProductPageController` · `productList` | `GET /products; GET /product` |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:27](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L27) | `ProductPageController` · `implementation` | `—` |
| [src/main/resources/templates/product-list.html:37](../../../../src/main/resources/templates/product-list.html#L37) | `UI` | `—` |
| [src/main/resources/templates/fragments/header.html:67](../../../../src/main/resources/templates/fragments/header.html#L67) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:120](../../../../src/main/resources/templates/admin/products.html#L120) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:170](../../../../src/main/resources/templates/admin/products.html#L170) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:194](../../../../src/main/resources/templates/admin/products.html#L194) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Không lọc ACTIVE cho sản phẩm, SKU, thương hiệu, danh mục hoặc cửa hàng.
- Không phân trang backend; tham số page của page controller không được áp dụng.
- UI: SSR: ProductPageController.productList calls productService.searchAndFilter(keyword, brandId, categoryId, skinType, concern, priceRange, sortBy). Không fetch API public catalog trên storefront. page parameter chỉ đưa vào Model, không paginate.
- UI: Fetch public reads, client filter name/brand/category/origin, KPI total products/SKUs/featured/ACTIVE, show prices/stock/status/link storefront. No editing actions/forms/modal.

<a id="uc020"></a>

## UC020 — Tìm kiếm và lọc sản phẩm

**Module:** Sản phẩm và tra cứu mỹ phẩm  
**Implementation Status:** Implemented  
**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /, /products, /product, /admin/products  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Tìm sản phẩm theo từ khóa, thương hiệu, danh mục, loại da, nhu cầu và khoảng giá; sắp xếp kết quả.

**Preconditions**

- Không yêu cầu đăng nhập.

**Main Success Scenario**

1. Cung cấp keyword, brandId, categoryId, skinType, concern, priceRange hoặc sortBy.
2. Hệ thống enrich toàn bộ sản phẩm trước rồi lọc trong bộ nhớ.
3. Từ khóa so khớp có/không dấu trên tên, brand, category, description, ingredients, uses, origin, skuCode và variantName.
4. Lọc brand/category theo ID chính xác; loại da/nhu cầu theo tên thuộc tính; khoảng giá theo minPrice.
5. Sắp xếp theo price-asc, price-desc, featured hoặc ID giảm dần.

**Alternative / Exception Flows**

- Không có kết quả: trả danh sách rỗng.
- priceRange không nhận diện: không lọc giá.
- sortBy không nhận diện: sắp xếp ID giảm dần.

**Postconditions**

- Kết quả truy vấn được hiển thị; dữ liệu không thay đổi.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:42](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L42) | `ProductRestController` · `getAll` | `GET /api/products?keyword=&brandId=&categoryId=&skinType=&concern=&priceRange=&sortBy=` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:97](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L97) | `ProductServiceImpl` · `searchAndFilter / matchesKeyword / matchesSkinType / matchesConcern / matchesPriceRange / getComparator` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:29](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L29) | `ProductPageController` · `productList` | `GET /products` |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:27](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L27) | `ProductPageController` · `implementation` | `—` |
| [src/main/resources/templates/product-list.html:37](../../../../src/main/resources/templates/product-list.html#L37) | `UI` | `—` |
| [src/main/resources/templates/fragments/header.html:67](../../../../src/main/resources/templates/fragments/header.html#L67) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:120](../../../../src/main/resources/templates/admin/products.html#L120) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:170](../../../../src/main/resources/templates/admin/products.html#L170) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:194](../../../../src/main/resources/templates/admin/products.html#L194) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- under300 và under500 cùng nghĩa giá < 500.000; 300to600 và 500to1000 cùng nghĩa 500.000–1.000.000; above1000 > 1.000.000.
- Category filter không mở rộng xuống các danh mục con.
- Tên thuộc tính được dùng cho skinTypes/concerns; value của ProductAttributeValue không dùng; các nhóm ngoài SKIN_TYPE đều vào concerns.
- UI: SSR: ProductPageController.productList calls productService.searchAndFilter(keyword, brandId, categoryId, skinType, concern, priceRange, sortBy). Không fetch API public catalog trên storefront. page parameter chỉ đưa vào Model, không paginate.
- UI: Fetch public reads, client filter name/brand/category/origin, KPI total products/SKUs/featured/ACTIVE, show prices/stock/status/link storefront. No editing actions/forms/modal.

<a id="uc021"></a>

## UC021 — Xem chi tiết sản phẩm

**Module:** Sản phẩm và tra cứu mỹ phẩm  
**Implementation Status:** Implemented  
**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /products/{id}, /product/{id}  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xem mô tả, biến thể/SKU, giá, ảnh, loại da/nhu cầu, tồn khả dụng và sản phẩm liên quan.

**Preconditions**

- ID sản phẩm tồn tại; không yêu cầu đăng nhập.

**Main Success Scenario**

1. Mở /products/{id} hoặc GET /api/products/{id}.
2. Hệ thống tìm sản phẩm và enrich SKU, ảnh, giá, stock và thuộc tính.
3. Page controller chọn tối đa 4 sản phẩm cùng brand hoặc category, loại sản phẩm đang xem.
4. Hiển thị chi tiết sản phẩm.

**Alternative / Exception Flows**

- Sản phẩm không tồn tại: API ném checked Exception và được trả HTTP 500; page chuyển /error/404.
- Không có ảnh/SKU/stock: dùng các giá trị fallback như danh sách.

**Postconditions**

- Thông tin chi tiết được hiển thị.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:33](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L33) | `ProductRestController` · `getById` | `GET /api/products/{id}` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:77](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L77) | `ProductServiceImpl` · `getById / enrich` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:66](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L66) | `ProductPageController` · `productDetail` | `GET /products/{id}; GET /product/{id}` |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:63](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L63) | `ProductPageController` · `implementation` | `—` |
| [src/main/resources/templates/product-detail.html:23](../../../../src/main/resources/templates/product-detail.html#L23) | `UI` | `—` |
| [src/main/resources/templates/product-detail.html:295](../../../../src/main/resources/templates/product-detail.html#L295) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Related products chỉ theo brand/category, không có tư vấn AI hay cá nhân hóa; không suy diễn thêm actor external.
- UI: SSR productService.getById; selected SKU updates price and code locally. Related products actually same category OR brand, limit 4; no BeautyProfile personalization.

<a id="uc022"></a>

## UC022 — Thêm sản phẩm

**Module:** Sản phẩm và tra cứu mỹ phẩm  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Tạo bản ghi sản phẩm với thương hiệu, danh mục, mô tả, xuất xứ, nguyên liệu, công dụng, featured và status.

**Preconditions**

- Payload có brandId, categoryId; tên không trống ≤ 200 ký tự; origin ≤ 100; brand/category tồn tại.
- Mutation API có header X-Requested-With phù hợp hoặc dùng Bearer token.

**Main Success Scenario**

1. Gửi ProductRequest tới POST /api/products.
2. Hệ thống map trường cơ bản, tìm brand/category theo ID rồi gán quan hệ.
3. Lưu ProductEntity và trả DTO được enrich.

**Alternative / Exception Flows**

- Request không hợp lệ: HTTP 400.
- Brand/category không tồn tại: checked Exception, HTTP 500.
- Nếu status hoặc isFeatured thành null sau mapping: entity @NotNull ngăn lưu; cần đối chiếu generated mapper/runtime.

**Postconditions**

- Một sản phẩm mới được lưu; không tạo SKU, ảnh, tồn kho hay thuộc tính.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:24](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L24) | `ProductRestController` · `create` | `POST /api/products` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:49](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L49) | `ProductServiceImpl` · `create` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java:26](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java#L26) | `ProductMapper` · `toEntity` | `—` |

**Giới hạn và đối chiếu UI**

- Không có xác thực/permission cho endpoint; PUBLIC mô tả truy cập thật.
- ProductRequest không chứa SKU, ảnh hoặc attribute values; không xem những phần này là được quản lý qua form sản phẩm.

<a id="uc023"></a>

## UC023 — Cập nhật sản phẩm

**Module:** Sản phẩm và tra cứu mỹ phẩm  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Sửa thông tin cơ bản và trạng thái kinh doanh của sản phẩm; thay brand/category.

**Preconditions**

- Sản phẩm và brand/category tồn tại; validation như thêm sản phẩm.
- Header mutation phù hợp.

**Main Success Scenario**

1. Gửi PUT /api/products/{id} với ProductRequest.
2. Hệ thống đọc sản phẩm và map các trường cơ bản.
3. Tìm và gán brand/category nếu ID có trong request.
4. Lưu sản phẩm và trả DTO cơ bản.

**Alternative / Exception Flows**

- Không có sản phẩm, brand hoặc category: checked Exception, HTTP 500.
- Request không hợp lệ hoặc entity sau mapping vi phạm @NotNull: HTTP 400.

**Postconditions**

- Thông tin sản phẩm được cập nhật.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:62](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L62) | `ProductRestController` · `update` | `PUT /api/products/{id}` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:263](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L263) | `ProductServiceImpl` · `update` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java:34](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/ProductMapper.java#L34) | `ProductMapper` · `updateEntity` | `—` |

**Giới hạn và đối chiếu UI**

- status=INACTIVE là khả năng trong update; không có endpoint riêng ngừng kinh doanh.
- Response của update không gọi enrich nên thiếu SKU/ảnh/giá/stock/attribute fields cho đến khi GET lại.
- NullValuePropertyMappingStrategy.IGNORE không được đặt cho ProductMapper; null có thể xóa optional fields.

<a id="uc024"></a>

## UC024 — Xóa sản phẩm

**Module:** Sản phẩm và tra cứu mỹ phẩm  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xóa vật lý bản ghi sản phẩm.

**Preconditions**

- Sản phẩm tồn tại; header mutation phù hợp.

**Main Success Scenario**

1. Gửi DELETE /api/products/{id}.
2. Hệ thống kiểm tra tồn tại và deleteById.
3. Trả HTTP 204 khi xóa thành công.

**Alternative / Exception Flows**

- Sản phẩm không tồn tại: checked Exception, HTTP 500.
- Quan hệ SKU, ảnh, attribute hoặc các dữ liệu khác còn tham chiếu có thể gây lỗi ràng buộc dữ liệu HTTP 409.

**Postconditions**

- Bản ghi sản phẩm bị xóa khi không bị ràng buộc cản trở.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:72](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L72) | `ProductRestController` · `delete` | `DELETE /api/products/{id}` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java:296](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/ProductServiceImpl.java#L296) | `ProductServiceImpl` · `delete` | `—` |

**Giới hạn và đối chiếu UI**

- Xóa vật lý khác với update status=INACTIVE; không có cascade cleanup trong implementation.

<a id="uc025"></a>

## UC025 — Xem thương hiệu

**Module:** Thương hiệu và danh mục  
**Implementation Status:** Implemented  
**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /brands, /admin/products  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xem danh sách hoặc thông tin một thương hiệu.

**Preconditions**

- Không yêu cầu đăng nhập.

**Main Success Scenario**

1. Mở /brands hoặc GET /api/brands; có thể yêu cầu /api/brands/{id}.
2. Hệ thống findAll hoặc findById, map BrandResponse.
3. Hiển thị tên, mô tả và status.

**Alternative / Exception Flows**

- ID không tồn tại: checked Exception, HTTP 500.
- Không có thương hiệu: danh sách rỗng.

**Postconditions**

- Thông tin thương hiệu được hiển thị.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java:37](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java#L37) | `BrandRestController` · `getAll / getById` | `GET /api/brands; GET /api/brands/{id}` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java:45](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java#L45) | `BrandServiceImpl` · `getAll / getById` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:51](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L51) | `HomeController` · `brands` | `GET /brands` |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:49](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L49) | `HomeController` · `implementation` | `—` |
| [src/main/resources/templates/brands.html:29](../../../../src/main/resources/templates/brands.html#L29) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:120](../../../../src/main/resources/templates/admin/products.html#L120) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:170](../../../../src/main/resources/templates/admin/products.html#L170) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:194](../../../../src/main/resources/templates/admin/products.html#L194) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Không lọc ACTIVE.
- UI: HomeController loads brandService.getAll; links filter catalog by brandId.
- UI: Fetch public reads, client filter name/brand/category/origin, KPI total products/SKUs/featured/ACTIVE, show prices/stock/status/link storefront. No editing actions/forms/modal.

<a id="uc026"></a>

## UC026 — Thêm thương hiệu

**Module:** Thương hiệu và danh mục  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Thêm tên, mô tả và status thương hiệu.

**Preconditions**

- Tên không trống ≤ 150; tên phải thỏa unique của Entity/DB.
- Header mutation phù hợp.

**Main Success Scenario**

1. Gửi BrandRequest tới POST /api/brands.
2. Hệ thống map request, save BrandEntity rồi trả BrandResponse.

**Alternative / Exception Flows**

- Tên không hợp lệ: HTTP 400.
- Tên trùng: ràng buộc unique, HTTP 409.
- status null được DTO cho phép nhưng entity bắt buộc; kết quả phụ thuộc null mapping, không có default trong service.

**Postconditions**

- Thương hiệu mới được lưu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java#L21) | `BrandRestController` · `create` | `POST /api/brands` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java:23](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java#L23) | `BrandServiceImpl` · `create` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/BrandMapper.java:14](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/BrandMapper.java#L14) | `BrandMapper` · `toEntity` | `—` |

**Giới hạn và đối chiếu UI**

- Không gọi kiểm tra tồn tại/tên trùng trong service; không có RBAC.

<a id="uc027"></a>

## UC027 — Cập nhật thương hiệu

**Module:** Thương hiệu và danh mục  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Sửa tên, mô tả hoặc status của thương hiệu.

**Preconditions**

- Thương hiệu tồn tại; tên không trống ≤ 150; header mutation phù hợp.

**Main Success Scenario**

1. Gửi PUT /api/brands/{id}.
2. Tìm thương hiệu, map request với null bị bỏ qua.
3. Lưu và trả thông tin cập nhật.

**Alternative / Exception Flows**

- ID không tồn tại: checked Exception, HTTP 500.
- Tên trùng hoặc sai validation: HTTP 409/400.

**Postconditions**

- Thương hiệu được cập nhật.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java:42](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java#L42) | `BrandRestController` · `update` | `PUT /api/brands/{id}` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java:53](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java#L53) | `BrandServiceImpl` · `update` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/BrandMapper.java:22](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/BrandMapper.java#L22) | `BrandMapper` · `updateEntity` | `—` |

**Giới hạn và đối chiếu UI**

- Có thể đặt status=INACTIVE hoặc ACTIVE; description null không xóa giá trị cũ.

<a id="uc028"></a>

## UC028 — Xóa thương hiệu

**Module:** Thương hiệu và danh mục  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xóa vật lý một thương hiệu.

**Preconditions**

- Thương hiệu tồn tại; header mutation phù hợp.

**Main Success Scenario**

1. Gửi DELETE /api/brands/{id}.
2. Kiểm tra tồn tại, deleteById, trả HTTP 204.

**Alternative / Exception Flows**

- ID không tồn tại: checked Exception, HTTP 500.
- Sản phẩm đang tham chiếu có thể ngăn xóa: HTTP 409.

**Postconditions**

- Thương hiệu bị xóa nếu không bị FK cản trở.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java:50](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/BrandRestController.java#L50) | `BrandRestController` · `delete` | `DELETE /api/brands/{id}` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java:67](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/BrandServiceImpl.java#L67) | `BrandServiceImpl` · `delete` | `—` |

**Giới hạn và đối chiếu UI**

- Không là soft delete.

<a id="uc029"></a>

## UC029 — Xem danh mục sản phẩm

**Module:** Thương hiệu và danh mục  
**Implementation Status:** Implemented  
**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /, /products, /product, /admin/products  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xem danh sách danh mục hoặc một danh mục, có parentId.

**Preconditions**

- Không yêu cầu đăng nhập.

**Main Success Scenario**

1. GET /api/categories hoặc /api/categories/{id}; page sản phẩm/home dùng CategoryService.getAll.
2. Hệ thống tìm category và map parent.id vào parentId.
3. Hiển thị thông tin danh mục.

**Alternative / Exception Flows**

- ID không tồn tại: checked Exception, HTTP 500.
- Không có danh mục: danh sách rỗng.

**Postconditions**

- Thông tin danh mục được hiển thị.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java:39](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java#L39) | `CategoryRestController` · `getAll / getById` | `GET /api/categories; GET /api/categories/{id}` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java:53](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java#L53) | `CategoryServiceImpl` · `getAll / getById` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java:18](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java#L18) | `CategoryMapper` · `toResponse` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/ProductPageController.java:27](../../../../src/main/java/com/thinh/cosmetic/controller/ProductPageController.java#L27) | `ProductPageController` · `implementation` | `—` |
| [src/main/resources/templates/product-list.html:37](../../../../src/main/resources/templates/product-list.html#L37) | `UI` | `—` |
| [src/main/resources/templates/fragments/header.html:67](../../../../src/main/resources/templates/fragments/header.html#L67) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:120](../../../../src/main/resources/templates/admin/products.html#L120) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:170](../../../../src/main/resources/templates/admin/products.html#L170) | `UI` | `—` |
| [src/main/resources/templates/admin/products.html:194](../../../../src/main/resources/templates/admin/products.html#L194) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Response là danh sách phẳng, không trả cây descendants; không lọc ACTIVE.
- UI: SSR: ProductPageController.productList calls productService.searchAndFilter(keyword, brandId, categoryId, skinType, concern, priceRange, sortBy). Không fetch API public catalog trên storefront. page parameter chỉ đưa vào Model, không paginate.
- UI: Fetch public reads, client filter name/brand/category/origin, KPI total products/SKUs/featured/ACTIVE, show prices/stock/status/link storefront. No editing actions/forms/modal.

<a id="uc030"></a>

## UC030 — Thêm danh mục sản phẩm

**Module:** Thương hiệu và danh mục  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Tạo danh mục gốc hoặc con của một danh mục có sẵn.

**Preconditions**

- Tên không trống ≤ 150; description ≤ 500; parentId nếu có phải tồn tại.
- Header mutation phù hợp.

**Main Success Scenario**

1. Gửi CategoryRequest tới POST /api/categories.
2. Map trường cơ bản; nếu parentId có giá trị, tìm category cha rồi gán parent.
3. Save và trả CategoryResponse cùng parentId.

**Alternative / Exception Flows**

- Cha không tồn tại: RuntimeException, HTTP 500.
- Validation không đạt: HTTP 400.
- status null được request cho phép nhưng entity yêu cầu; service không tự default.

**Postconditions**

- Danh mục mới gốc/con được lưu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java#L21) | `CategoryRestController` · `create` | `POST /api/categories` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java:23](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java#L23) | `CategoryServiceImpl` · `create` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java:15](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java#L15) | `CategoryMapper` · `toEntity` | `—` |

**Giới hạn và đối chiếu UI**

- Không có nghiệp vụ quản lý cây đầy đủ hay endpoint di chuyển cây; chỉ gán cha khi create.

<a id="uc031"></a>

## UC031 — Cập nhật danh mục sản phẩm

**Module:** Thương hiệu và danh mục  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Sửa tên, mô tả và status danh mục; request parentId chưa được xử lý.

**Preconditions**

- Danh mục tồn tại; tên không trống ≤ 150; description ≤ 500.
- Header mutation phù hợp.

**Main Success Scenario**

1. Gửi PUT /api/categories/{id}.
2. Hệ thống tìm danh mục, CategoryMapper.updateEntity bỏ qua null và bỏ qua parent.
3. Lưu tên/mô tả/status và trả danh mục.

**Alternative / Exception Flows**

- Không có category: checked Exception, HTTP 500.
- Validation không đạt: HTTP 400.
- Gửi parentId mới hoặc null: quan hệ cha vẫn giữ nguyên.

**Postconditions**

- Tên/mô tả/status được cập nhật; parent không đổi.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java:46](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java#L46) | `CategoryRestController` · `update` | `PUT /api/categories/{id}` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java:61](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java#L61) | `CategoryServiceImpl` · `update` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java:25](../../../../src/main/java/com/thinh/cosmetic/mapper/catalog/CategoryMapper.java#L25) | `CategoryMapper` · `updateEntity` | `—` |

**Giới hạn và đối chiếu UI**

- Khoảng trống thực tế: chưa hỗ trợ chuyển danh mục cha hoặc tách thành danh mục gốc; không đánh dấu quản lý hierarchy đầy đủ.

<a id="uc032"></a>

## UC032 — Xóa danh mục sản phẩm

**Module:** Thương hiệu và danh mục  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xóa vật lý danh mục theo ID.

**Preconditions**

- Header mutation phù hợp; danh mục không bị FK cản trở.

**Main Success Scenario**

1. Gửi DELETE /api/categories/{id}.
2. Service gọi deleteById trực tiếp.
3. Trả HTTP 204 nếu không có lỗi.

**Alternative / Exception Flows**

- Danh mục cha có con hoặc danh mục có sản phẩm tham chiếu có thể bị FK ngăn xóa: HTTP 409.
- Không kiểm tra tồn tại riêng trước deleteById.

**Postconditions**

- Danh mục được xóa khi thao tác thành công.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java:56](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/CategoryRestController.java#L56) | `CategoryRestController` · `delete` | `DELETE /api/categories/{id}` |
| [src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java:75](../../../../src/main/java/com/thinh/cosmetic/service/catalog/impl/CategoryServiceImpl.java#L75) | `CategoryServiceImpl` · `delete` | `—` |

**Giới hạn và đối chiếu UI**

- Không cascade chuyển sản phẩm/danh mục con hoặc soft deactivate.

<a id="uc033"></a>

## UC033 — Xem hệ thống cửa hàng

**Module:** Cửa hàng  
**Implementation Status:** Implemented  
**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /stores  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xem thông tin cửa hàng gồm địa chỉ, tỉnh, số điện thoại, giờ mở/đóng, tọa độ và status.

**Preconditions**

- Không yêu cầu đăng nhập.

**Main Success Scenario**

1. Mở /stores hoặc GET /api/stores; có thể xem /api/stores/{id}.
2. Hệ thống lấy StoreEntity, map StoreResponse.
3. Hiển thị các cửa hàng.

**Alternative / Exception Flows**

- ID không tồn tại: checked Exception, HTTP 500.
- Không có cửa hàng: danh sách rỗng.

**Postconditions**

- Thông tin cửa hàng được hiển thị.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java#L31) | `StoreRestController` · `getAll / getById` | `GET /api/stores; GET /api/stores/{id}` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java:39](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java#L39) | `StoreServiceImpl` · `getAll / getById` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:40](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L40) | `HomeController` · `stores` | `GET /stores` |
| [src/main/java/com/thinh/cosmetic/controller/HomeController.java:38](../../../../src/main/java/com/thinh/cosmetic/controller/HomeController.java#L38) | `HomeController` · `implementation` | `—` |
| [src/main/resources/templates/stores.html:29](../../../../src/main/resources/templates/stores.html#L29) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- findAll trả cả INACTIVE; không có tìm kiếm backend theo tỉnh/khoảng cách.
- operatingHours trong Response không có nguồn field Entity.
- UI: HomeController loads storeService.getAll; cards show province/name/address/phone/hours. Không map/booking.

<a id="uc034"></a>

## UC034 — Thêm cửa hàng

**Module:** Cửa hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Tạo cửa hàng và thông tin liên lạc, giờ mở/đóng, tọa độ, status.

**Preconditions**

- Tên ≤ 150, địa chỉ ≤ 255, tỉnh ≤ 100 đều không trống; phone ≤ 20.
- Header mutation phù hợp.

**Main Success Scenario**

1. Gửi StoreRequest tới POST /api/stores.
2. Map request vào StoreEntity; status null được service đặt ACTIVE.
3. Save và trả StoreResponse.

**Alternative / Exception Flows**

- Request sai validation/định dạng thời gian: HTTP 400.

**Postconditions**

- Cửa hàng mới được lưu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java#L21) | `StoreRestController` · `create` | `POST /api/stores` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java:24](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java#L24) | `StoreServiceImpl` · `create` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/store/StoreMapper.java:11](../../../../src/main/java/com/thinh/cosmetic/mapper/store/StoreMapper.java#L11) | `StoreMapper` · `toEntity` | `—` |

**Giới hạn và đối chiếu UI**

- operatingHours là field DTO nhưng Entity không có; chỉ openTime/closeTime được lưu.
- Không tạo tồn kho hay nhân viên gán cửa hàng tự động.

<a id="uc035"></a>

## UC035 — Cập nhật cửa hàng

**Module:** Cửa hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Sửa địa chỉ, liên lạc, tỉnh, tên, giờ mở/đóng, tọa độ và status.

**Preconditions**

- Cửa hàng tồn tại; validation giống thêm cửa hàng; header mutation phù hợp.

**Main Success Scenario**

1. Gửi PUT /api/stores/{id}.
2. Tìm cửa hàng và map trường khác null vào entity.
3. Save và trả StoreResponse.

**Alternative / Exception Flows**

- Không có cửa hàng: checked Exception, HTTP 500.
- Request sai validation: HTTP 400.
- operatingHours được gửi nhưng không có nơi lưu.

**Postconditions**

- Thông tin cửa hàng được cập nhật.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java:36](../../../../src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java#L36) | `StoreRestController` · `update` | `PUT /api/stores/{id}` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java:44](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java#L44) | `StoreServiceImpl` · `update` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/store/StoreMapper.java:17](../../../../src/main/java/com/thinh/cosmetic/mapper/store/StoreMapper.java#L17) | `StoreMapper` · `updateEntity` | `—` |

**Giới hạn và đối chiếu UI**

- Có thể kích hoạt lại qua status=ACTIVE.
- Không kiểm tra openTime/closeTime hợp lệ theo nghiệp vụ hoặc lat/long range.

<a id="uc036"></a>

## UC036 — Ngừng hoạt động cửa hàng

**Module:** Cửa hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Đặt trạng thái cửa hàng INACTIVE, giữ nguyên dữ liệu.

**Preconditions**

- Cửa hàng tồn tại; header mutation phù hợp.

**Main Success Scenario**

1. Gửi DELETE /api/stores/{id}.
2. Service tìm entity, setStatus(INACTIVE), save.
3. Trả HTTP 204.

**Alternative / Exception Flows**

- Không có cửa hàng: checked Exception, HTTP 500.

**Postconditions**

- Cửa hàng có status INACTIVE.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java:44](../../../../src/main/java/com/thinh/cosmetic/rest/store/StoreRestController.java#L44) | `StoreRestController` · `deactivate` | `DELETE /api/stores/{id}` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java:52](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StoreServiceImpl.java#L52) | `StoreServiceImpl` · `deactivate` | `—` |

**Giới hạn và đối chiếu UI**

- Endpoint DELETE nhưng không xóa vật lý.
- Không tự ẩn trong StoreService.getAll, public stock, ProductService.enrich; không xử lý đơn/tồn/nhân viên hiện có.

<a id="uc037"></a>

## UC037 — Tra cứu tồn sản phẩm theo cửa hàng

**Module:** Tồn kho  
**Implementation Status:** Implemented  
**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /products/{id}  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Tra tồn khả dụng và thông tin liên lạc tại từng cửa hàng cho một SKU.

**Preconditions**

- Không yêu cầu đăng nhập; skuId được truyền trên URL.

**Main Success Scenario**

1. GET /api/products/sku/{skuId}/stores.
2. Hệ thống lấy toàn bộ cửa hàng và tồn kho theo skuId.
3. Mỗi cửa hàng trả actualStock, heldQuantity, availableStock=max(0,actual-held), inStock và địa chỉ/liên lạc.
4. Không có inventory ở cửa hàng: trả tất cả số lượng bằng 0.

**Alternative / Exception Flows**

- skuId không tồn tại: không kiểm tra SKU, vẫn trả mọi cửa hàng với số lượng 0.

**Postconditions**

- Stock snapshot theo cửa hàng được hiển thị.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java:83](../../../../src/main/java/com/thinh/cosmetic/rest/catalog/ProductRestController.java#L83) | `ProductRestController` · `getSkuStoreStock` | `GET /api/products/sku/{skuId}/stores` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java:87](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java#L87) | `InventoryServiceImpl` · `getStoreStockBySku` | `—` |
| [src/main/resources/templates/product-detail.html:357](../../../../src/main/resources/templates/product-detail.html#L357) | `UI` | `—` |
| [src/main/resources/templates/product-detail.html:383](../../../../src/main/resources/templates/product-detail.html#L383) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Trả cả cửa hàng INACTIVE và actual/held nội bộ; không chỉ available.
- Inventory trùng store-SKU chọn bản ghi đầu qua merge (a,b)->a.
- UI: Modal per selectedSkuId displays storeName/address/phone/inStock/availableStock. Không giữ hàng.

<a id="uc038"></a>

## UC038 — Xem tồn kho cửa hàng

**Module:** Tồn kho  
**Implementation Status:** Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin/inventory  
**Quyền:** /api/inventory/** requires ROLE_EMPLOYEE

**Mô tả nghiệp vụ:** Nhân viên xem tồn thực tế, lượng giữ, khả dụng và ngưỡng tối thiểu của cửa hàng/SKU.

**Preconditions**

- Token hợp lệ có ROLE_EMPLOYEE theo SecurityConfig.
- API không yêu cầu cửa hàng thuộc phạm vi của nhân viên.

**Main Success Scenario**

1. GET /api/inventory/store/{storeId} hoặc /store/{storeId}/sku/{skuId}; có thể lấy riêng /available.
2. Hệ thống tìm inventory theo store hoặc store-SKU.
3. Trả actualStock, heldQuantity, available=max(0,actual-held), minimumStock và nhận diện sản phẩm.

**Alternative / Exception Flows**

- Store hoặc SKU không có inventory: list rỗng hoặc /available trả 0; detail ném checked Exception HTTP 500.
- Chưa xác thực/không ROLE_EMPLOYEE: HTTP 401/403.

**Postconditions**

- Stock snapshot được hiển thị.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java:20](../../../../src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java#L20) | `InventoryRestController` · `getByStore / getByStoreAndSku / getAvailableStock` | `GET /api/inventory/store/{storeId}; GET /api/inventory/store/{storeId}/sku/{skuId}; GET /api/inventory/store/{storeId}/sku/{skuId}/available` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java:34](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java#L34) | `InventoryServiceImpl` · `getByStore / getByStoreAndSku / getAvailableStock` | `—` |
| [src/main/resources/templates/admin/inventory.html:19](../../../../src/main/resources/templates/admin/inventory.html#L19) | `UI` | `—` |
| [src/main/resources/templates/admin/inventory.html:182](../../../../src/main/resources/templates/admin/inventory.html#L182) | `UI` | `—` |
| [src/main/resources/templates/admin/inventory.html:219](../../../../src/main/resources/templates/admin/inventory.html#L219) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- StoreScope chỉ phục vụ danh sách cửa hàng ở AdminPageController; API/service này không sử dụng nên nhân viên gọi ID cửa hàng khác được.
- Không có permission riêng INVENTORY_VIEW/INVENTORY_ADJUST trong code.
- UI: SKU/name local filter; actual/held/available/minimum/status + sums KPI. Accessible branch options; query storeId in dashboard link ignored in initialization.

<a id="uc039"></a>

## UC039 — Xem cảnh báo tồn thấp

**Module:** Tồn kho  
**Implementation Status:** Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin  
**Quyền:** /api/inventory/** requires ROLE_EMPLOYEE

**Mô tả nghiệp vụ:** Tra các SKU có tồn thực tế không vượt ngưỡng minimumStock của cửa hàng.

**Preconditions**

- Token hợp lệ có ROLE_EMPLOYEE.

**Main Success Scenario**

1. GET /api/inventory/store/{storeId}/low-stock.
2. Repository chọn store.id và actualStock <= minimumStock.
3. Hệ thống trả danh sách InventoryResponse.

**Alternative / Exception Flows**

- Không có bản ghi thỏa: danh sách rỗng.
- Chưa xác thực/không ROLE_EMPLOYEE: HTTP 401/403.

**Postconditions**

- Danh sách SKU tồn thấp được trả.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java:33](../../../../src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java#L33) | `InventoryRestController` · `getLowStockItems` | `GET /api/inventory/store/{storeId}/low-stock` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java:72](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java#L72) | `InventoryServiceImpl` · `getLowStockItems` | `—` |
| [src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java:16](../../../../src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java#L16) | `InventoryRepository` · `findLowStock` | `—` |
| [src/main/resources/templates/admin/dashboard.html:211](../../../../src/main/resources/templates/admin/dashboard.html#L211) | `UI` | `—` |
| [src/main/resources/templates/admin/dashboard.html:274](../../../../src/main/resources/templates/admin/dashboard.html#L274) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Dùng actualStock, không dùng khả dụng sau giữ hàng; ngưỡng bao gồm trường hợp bằng.
- Không có workflow đặt ngưỡng/đặt mua tự động, thông báo gửi bên ngoài hay scheduler.
- UI: Shows count/all and first five. With all-store dashboard, chooses options[1] (first store), no chain aggregation.

<a id="uc040"></a>

## UC040 — Điều chỉnh tồn kho

**Module:** Tồn kho  
**Implementation Status:** Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin/inventory  
**Quyền:** /api/inventory/** requires ROLE_EMPLOYEE

**Mô tả nghiệp vụ:** Cập nhật tồn thực tế sau kiểm đếm và lưu lịch sử trước/sau, lý do, người thực hiện, thời gian.

**Preconditions**

- Token có ROLE_EMPLOYEE; inventory store-SKU tồn tại.
- newQuantity không null, >=0; lý do không trống ≤255; newQuantity >= heldQuantity.
- Header mutation phù hợp.

**Main Success Scenario**

1. POST /api/inventory/adjust với storeId, skuId, newQuantity, reason.
2. Controller ưu tiên principal.employeeId; thiếu thì dùng employeeId parameter hoặc 1.
3. Service kiểm tra tồn mới không nhỏ hơn lượng giữ cho khách.
4. Ghi InventoryAdjustmentEntity với quantityBefore, quantityAfter, reason, performedBy và adjustedAt.
5. Set actualStock=newQuantity, save và trả InventoryResponse.

**Alternative / Exception Flows**

- Không có inventory: checked Exception HTTP 500.
- Tồn mới dưới lượng giữ: checked Exception HTTP 500.
- Request không hợp lệ: HTTP 400.
- Employee lookup không có kết quả: performedBy=null; Entity bắt buộc employee nên validation/constraint ngăn lưu.

**Postconditions**

- Tồn thực tế và audit adjustment được lưu; heldQuantity không đổi.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/store/InventoryRestController.java#L38) | `InventoryRestController` · `adjustStock` | `POST /api/inventory/adjust` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java:48](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/InventoryServiceImpl.java#L48) | `InventoryServiceImpl` · `adjustStock` | `—` |
| [src/main/java/com/thinh/cosmetic/domain/entity/inventory/InventoryAdjustmentEntity.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/entity/inventory/InventoryAdjustmentEntity.java#L1) | `InventoryAdjustmentEntity` · `entity field: performedBy / adjustedAt` | `—` |
| [src/main/resources/templates/admin/inventory.html:163](../../../../src/main/resources/templates/admin/inventory.html#L163) | `UI` | `—` |
| [src/main/resources/templates/admin/inventory.html:318](../../../../src/main/resources/templates/admin/inventory.html#L318) | `UI` | `—` |
| [src/main/resources/templates/admin/inventory.html:344](../../../../src/main/resources/templates/admin/inventory.html#L344) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Không kiểm tra StoreScope.
- Có lịch sử trong DB nhưng không có REST/page xem lịch sử điều chỉnh; không tạo UC tra audit hoàn chỉnh.
- Không có pessimistic lock/@Version; chưa bảo đảm cạnh tranh với giữ hàng trong đơn.
- UI: NewQuantity >= held UI enforced; required reason preset/custom. Sends storeId/skuId/newQuantity/reason. Reasons mentioning purchase/transfer only update one stock record, not actual purchase/transfer processes.

<a id="uc041"></a>

## UC041 — Lập phiếu chuyển kho

**Module:** Điều chuyển kho  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Lập phiếu gồm cửa hàng nguồn, đích và các dòng SKU/số lượng ở trạng thái PENDING.

**Preconditions**

- sourceStoreId, destinationStoreId không null; items không rỗng; store/SKU và employeeId phải tồn tại để lưu hợp lệ.
- Header mutation phù hợp.

**Main Success Scenario**

1. POST /api/stock-transfers với request và employeeId query mặc định 1.
2. Tìm nguồn/đích, tạo StockTransferEntity status=PENDING và employee theo ID query.
3. Lưu từng StockTransferItem với SKU và quantity.
4. Trả phiếu và dòng hàng.

**Alternative / Exception Flows**

- Nguồn/đích/SKU không tồn tại: checked Exception HTTP 500.
- employeeId không tồn tại: null createdBy vi phạm Entity @NotNull.
- items rỗng/null: HTTP 400; thiếu @Valid trên list nên nested validation DTO không được cascade, entity validation là lớp sau.

**Postconditions**

- Phiếu PENDING và các dòng hàng được lưu; chưa trừ/giữ tồn.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java#L21) | `StockTransferRestController` · `create` | `POST /api/stock-transfers?employeeId=1` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:32](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java#L32) | `StockTransferServiceImpl` · `create` | `—` |

**Giới hạn và đối chiếu UI**

- Không RBAC, không StoreScope; có thể giả danh createdBy qua query.
- Không cấm cùng nguồn/đích, SKU lặp, store/SKU INACTIVE; không kiểm tra tồn khi lập.
- checked Exception sau save có rủi ro không rollback toàn bộ do @Transactional không rollbackFor checked exceptions.

<a id="uc042"></a>

## UC042 — Xem phiếu chuyển kho

**Module:** Điều chuyển kho  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xem danh sách hoặc chi tiết phiếu chuyển kho cùng trạng thái, thời gian và dòng SKU.

**Preconditions**

- Không yêu cầu đăng nhập.

**Main Success Scenario**

1. GET /api/stock-transfers hoặc /{id}.
2. Hệ thống findAll hoặc findById, trả store names, status, employee name, created/shipped/received times và items.

**Alternative / Exception Flows**

- ID không tồn tại: checked Exception HTTP 500.
- Không có phiếu: danh sách rỗng.

**Postconditions**

- Thông tin phiếu chuyển được hiển thị.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java:34](../../../../src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java#L34) | `StockTransferRestController` · `getAll / getById` | `GET /api/stock-transfers; GET /api/stock-transfers/{id}` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:123](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java#L123) | `StockTransferServiceImpl` · `getAll / getById / toResponse` | `—` |

**Giới hạn và đối chiếu UI**

- Không lọc theo store/status/người; repository có finder sourceOrDestination nhưng service không dùng.
- Response không có source/destination IDs; chỉ tên.

<a id="uc043"></a>

## UC043 — Xác nhận xuất chuyển kho

**Module:** Điều chuyển kho  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xuất hàng khỏi cửa hàng nguồn và chuyển phiếu từ PENDING sang IN_TRANSIT.

**Preconditions**

- Phiếu tồn tại và status=PENDING.
- Mỗi dòng có inventory tại nguồn; actualStock >= quantity.
- Header mutation phù hợp.

**Main Success Scenario**

1. PUT /api/stock-transfers/{id}/ship.
2. Tìm phiếu, kiểm tra PENDING.
3. Lấy dòng hàng, từng dòng kiểm tra actualStock rồi trừ quantity và save tồn nguồn.
4. Set IN_TRANSIT, shippedAt=now, save phiếu và trả kết quả.

**Alternative / Exception Flows**

- Phiếu không tồn tại/không PENDING: checked Exception HTTP 500.
- Thiếu inventory hoặc không đủ actualStock: checked Exception HTTP 500.
- Không đủ tồn ở dòng sau khi dòng trước đã save: có nguy cơ commit một phần do checked Exception không thuộc rollback mặc định.

**Postconditions**

- Khi thành công, tồn nguồn giảm theo quantity; phiếu IN_TRANSIT có shippedAt.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java:39](../../../../src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java#L39) | `StockTransferRestController` · `confirmShipment` | `PUT /api/stock-transfers/{id}/ship` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:63](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java#L63) | `StockTransferServiceImpl` · `confirmShipment` | `—` |

**Giới hạn và đối chiếu UI**

- Không có bước phê duyệt riêng; enum PENDING_EXPORT không được service sử dụng.
- Kiểm tra actualStock, bỏ qua heldQuantity; có thể xuất phần đã giữ cho khách.
- Không khóa/Version; hai lần ship cạnh tranh có rủi ro trừ kho sai; không ghi InventoryAdjustment audit.

<a id="uc044"></a>

## UC044 — Xác nhận nhận chuyển kho

**Module:** Điều chuyển kho  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Nhận toàn bộ dòng chuyển kho tại cửa hàng đích và chuyển phiếu IN_TRANSIT thành RECEIVED.

**Preconditions**

- Phiếu tồn tại và status=IN_TRANSIT; header mutation phù hợp.

**Main Success Scenario**

1. PUT /api/stock-transfers/{id}/receive.
2. Tìm phiếu và kiểm tra IN_TRANSIT.
3. Từng dòng tìm tồn đích; nếu chưa có tạo inventory actual=0, held=0, minimum=5.
4. Cộng quantity vào actualStock, save inventory.
5. Set RECEIVED và receivedAt=now, save phiếu và trả kết quả.

**Alternative / Exception Flows**

- Phiếu không tồn tại/không IN_TRANSIT: checked Exception HTTP 500.
- Lỗi persistence/validation khi lưu inventory: request thất bại.

**Postconditions**

- Khi thành công, tồn đích tăng; phiếu RECEIVED có receivedAt.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java:44](../../../../src/main/java/com/thinh/cosmetic/rest/store/StockTransferRestController.java#L44) | `StockTransferRestController` · `confirmReceipt` | `PUT /api/stock-transfers/{id}/receive` |
| [src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java:87](../../../../src/main/java/com/thinh/cosmetic/service/store/impl/StockTransferServiceImpl.java#L87) | `StockTransferServiceImpl` · `confirmReceipt` | `—` |

**Giới hạn và đối chiếu UI**

- Nhận toàn bộ theo quantity gốc; không hỗ trợ nhận thiếu/hư hỏng/hủy.
- Không có approval hoặc completion status riêng; RECEIVED là terminal implementation.
- Không RBAC/StoreScope, lock, Version hoặc adjustment audit.

<a id="uc045"></a>

## UC045 — Xem nhà cung cấp

**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xem danh sách hoặc chi tiết nhà cung cấp với thông tin liên hệ và status.

**Preconditions**

- Không yêu cầu đăng nhập.

**Main Success Scenario**

1. GET /api/suppliers hoặc /{id}.
2. Hệ thống findAll/findById, map SupplierResponse.

**Alternative / Exception Flows**

- ID không tồn tại: checked Exception HTTP 500.
- Không có supplier: danh sách rỗng.

**Postconditions**

- Thông tin nhà cung cấp được trả.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java#L31) | `SupplierRestController` · `getAll / getById` | `GET /api/suppliers; GET /api/suppliers/{id}` |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java:39](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java#L39) | `SupplierServiceImpl` · `getAll / getById` | `—` |

**Giới hạn và đối chiếu UI**

- Không lọc ACTIVE và không phân trang/tìm kiếm.

<a id="uc046"></a>

## UC046 — Thêm nhà cung cấp

**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Tạo nhà cung cấp, người liên hệ, địa chỉ, điện thoại, email và status.

**Preconditions**

- Tên không trống ≤200; contact ≤150; address ≤255; phone ≤20; email hợp lệ ≤150.
- Header mutation phù hợp.

**Main Success Scenario**

1. POST /api/suppliers với SupplierRequest.
2. Map request, đặt status=ACTIVE nếu null.
3. Save và trả SupplierResponse.

**Alternative / Exception Flows**

- Sai validation/JSON: HTTP 400.

**Postconditions**

- Nhà cung cấp được lưu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java#L21) | `SupplierRestController` · `create` | `POST /api/suppliers` |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java:24](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java#L24) | `SupplierServiceImpl` · `create` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/purchase/SupplierMapper.java:11](../../../../src/main/java/com/thinh/cosmetic/mapper/purchase/SupplierMapper.java#L11) | `SupplierMapper` · `toEntity` | `—` |

**Giới hạn và đối chiếu UI**

- existsByName trong repository không được service sử dụng; Entity name không unique.

<a id="uc047"></a>

## UC047 — Cập nhật nhà cung cấp

**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Sửa thông tin liên hệ hoặc status nhà cung cấp.

**Preconditions**

- Supplier tồn tại; validation như create; header mutation phù hợp.

**Main Success Scenario**

1. PUT /api/suppliers/{id}.
2. Tìm supplier, map non-null fields.
3. Save và trả dữ liệu mới.

**Alternative / Exception Flows**

- Không có supplier: checked Exception HTTP 500.
- Request không hợp lệ: HTTP 400.

**Postconditions**

- Nhà cung cấp được cập nhật.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java:36](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java#L36) | `SupplierRestController` · `update` | `PUT /api/suppliers/{id}` |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java:44](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java#L44) | `SupplierServiceImpl` · `update` | `—` |
| [src/main/java/com/thinh/cosmetic/mapper/purchase/SupplierMapper.java:17](../../../../src/main/java/com/thinh/cosmetic/mapper/purchase/SupplierMapper.java#L17) | `SupplierMapper` · `updateEntity` | `—` |

**Giới hạn và đối chiếu UI**

- Có thể ACTIVE lại qua status; null không xóa dữ liệu optional cũ.

<a id="uc048"></a>

## UC048 — Ngừng hợp tác nhà cung cấp

**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Đặt supplier INACTIVE thay vì xóa vật lý.

**Preconditions**

- Supplier tồn tại; header mutation phù hợp.

**Main Success Scenario**

1. DELETE /api/suppliers/{id}.
2. Tìm supplier, setStatus(INACTIVE), save và trả HTTP 204.

**Alternative / Exception Flows**

- ID không tồn tại: checked Exception HTTP 500.

**Postconditions**

- Nhà cung cấp có status INACTIVE.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java:44](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/SupplierRestController.java#L44) | `SupplierRestController` · `deactivate` | `DELETE /api/suppliers/{id}` |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java:52](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/SupplierServiceImpl.java#L52) | `SupplierServiceImpl` · `deactivate` | `—` |

**Giới hạn và đối chiếu UI**

- Không chặn tạo PO từ supplier INACTIVE; không tự lọc khỏi danh sách.

<a id="uc049"></a>

## UC049 — Lập đơn nhập hàng

**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Lập đơn mua từ supplier đến một cửa hàng với các dòng SKU, quantity, unitPrice và tổng tiền ở trạng thái DRAFT.

**Preconditions**

- supplierId/receivingStoreId không null; items không rỗng; supplier/store/SKU và employeeId phải tồn tại.
- Header mutation phù hợp.

**Main Success Scenario**

1. POST /api/purchase-orders?employeeId=1.
2. Tìm supplier/cửa hàng; tạo PO DRAFT, createdBy từ query, total=0.
3. Từng dòng tìm SKU và tính subtotal=unitPrice*quantity.
4. Lưu PurchaseOrderItem, cộng totalAmount, gán items và save PO.
5. Trả PO và items.

**Alternative / Exception Flows**

- Supplier/store/SKU không tồn tại: checked Exception HTTP 500.
- Không có employee: createdBy=null vi phạm Entity bắt buộc.
- items rỗng/null: HTTP 400; nested item validation không cascade do thiếu @Valid trên list; entity constraint là lớp sau.
- unitPrice/quantity null: phép tính có thể ném exception trước entity validation.

**Postconditions**

- PO DRAFT và dòng hàng được lưu; chưa tăng tồn.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java:21](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java#L21) | `PurchaseOrderRestController` · `create` | `POST /api/purchase-orders?employeeId=1` |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java:38](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java#L38) | `PurchaseOrderServiceImpl` · `create` | `—` |

**Giới hạn và đối chiếu UI**

- Không RBAC/StoreScope; employeeId query cho phép gán người tạo, không lấy principal.
- Không kiểm tra status ACTIVE, giá supplier hay trùng SKU; giá nhập do request quyết định.
- Checked Exception sau lưu PO/items có nguy cơ commit một phần.

<a id="uc050"></a>

## UC050 — Xem đơn nhập hàng

**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xem danh sách hoặc chi tiết PO với nhà cung cấp, cửa hàng nhận, người tạo, trạng thái, tổng tiền và dòng hàng.

**Preconditions**

- Không yêu cầu đăng nhập.

**Main Success Scenario**

1. GET /api/purchase-orders hoặc /{id}.
2. Hệ thống findAll/findById và map PO cùng items.

**Alternative / Exception Flows**

- Không có PO ID: checked Exception HTTP 500.
- Không có đơn: danh sách rỗng.

**Postconditions**

- Thông tin đơn nhập được trả.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java:34](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java#L34) | `PurchaseOrderRestController` · `getAll / getById` | `GET /api/purchase-orders; GET /api/purchase-orders/{id}` |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java:113](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java#L113) | `PurchaseOrderServiceImpl` · `getAll / getById / toResponse` | `—` |

**Giới hạn và đối chiếu UI**

- Không lọc supplier/store/status; repository findBySupplierId/findByReceivingStoreId không dùng.
- Response chỉ names, không supplier/store IDs.

<a id="uc051"></a>

## UC051 — Xác nhận nhập hàng vào kho

**Module:** Nhà cung cấp và nhập hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** anyRequest().permitAll()

**Mô tả nghiệp vụ:** Xác nhận PO DRAFT và nhập toàn bộ hàng vào tồn kho cửa hàng nhận trong cùng thao tác.

**Preconditions**

- PO tồn tại và status=DRAFT; header mutation phù hợp.

**Main Success Scenario**

1. PUT /api/purchase-orders/{id}/confirm.
2. Tìm PO và kiểm tra DRAFT.
3. Mỗi dòng tìm inventory store-SKU; nếu thiếu tạo actual=0, held=0, minimum=5.
4. Cộng quantity vào actualStock rồi save inventory.
5. Set status=CONFIRMED, confirmedAt=now và save PO.

**Alternative / Exception Flows**

- ID không tồn tại/không DRAFT: checked Exception HTTP 500.
- Persistence/validation error khi lưu inventory hoặc PO: request thất bại.

**Postconditions**

- Khi thành công, tồn kho nhận tăng toàn bộ lượng trong PO; PO CONFIRMED có confirmedAt.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java:39](../../../../src/main/java/com/thinh/cosmetic/rest/purchase/PurchaseOrderRestController.java#L39) | `PurchaseOrderRestController` · `confirm` | `PUT /api/purchase-orders/{id}/confirm` |
| [src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java:77](../../../../src/main/java/com/thinh/cosmetic/service/purchase/impl/PurchaseOrderServiceImpl.java#L77) | `PurchaseOrderServiceImpl` · `confirm` | `—` |

**Giới hạn và đối chiếu UI**

- Không có approve rồi receipt tách biệt; confirm đồng thời tăng kho nên tên UC phản ánh hành vi nhập hàng thật.
- Không có phiếu nhận riêng, nhận từng phần, sửa/hủy PO hay đối chiếu hàng thiếu.
- Không RBAC/StoreScope, lock/Version hay adjustment audit.

<a id="uc052"></a>

## UC052 — Xem giỏ hàng

**Module:** Giỏ hàng  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /cart  
**Quyền:** ROLE_CUSTOMER theo SecurityConfig.

**Mô tả nghiệp vụ:** Xem sản phẩm SKU, số lượng, giá hiện tại và tổng tiền trong giỏ.

**Preconditions**

- Đã đăng nhập ROLE_CUSTOMER.

**Main Success Scenario**

1. Gọi GET /api/cart hoặc mở trang /cart.
2. Hệ thống tìm giỏ theo customerId của principal; nếu chưa có thì tạo giỏ.
3. Đọc cart items, giá SKU hiện tại và ảnh chính sản phẩm.
4. Trả các dòng, subtotal và tổng số lượng.

**Alternative / Exception Flows**

- Giỏ chưa có sản phẩm: trả danh sách rỗng và tổng 0.
- Khách không tồn tại nhưng chưa có giỏ: code tạo customer=null rồi persistence không đáp ứng constraint.

**Postconditions**

- Giỏ được hiển thị; lần đọc đầu tiên có thể tạo giỏ.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:31](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L31) | `CartRestController` · `getCart` | `GET /api/cart` |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:41](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L41) | `CartServiceImpl` · `getCart` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/CartPageController.java:24](../../../../src/main/java/com/thinh/cosmetic/controller/CartPageController.java#L24) | `CartPageController` · `implementation` | `—` |
| [src/main/resources/static/js/api.js:134](../../../../src/main/resources/static/js/api.js#L134) | `UI` | `—` |
| [src/main/resources/templates/cart.html:35](../../../../src/main/resources/templates/cart.html#L35) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Query customerId chỉ được dùng khi AuthPrincipal không chứa customerId; fallback 1L.
- UI: SSR cartService.getCart; JS cart badge separately fetches /api/cart.

<a id="uc053"></a>

## UC053 — Thêm sản phẩm vào giỏ

**Module:** Giỏ hàng  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /products/{id}  
**Quyền:** ROLE_CUSTOMER theo SecurityConfig.

**Mô tả nghiệp vụ:** Thêm một SKU hoặc tăng số lượng SKU đã có trong giỏ.

**Preconditions**

- Đã đăng nhập; SKU tồn tại; quantity >= 1.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Gửi skuId và quantity; @Valid kiểm tra dữ liệu.
2. Tìm hoặc tạo giỏ của khách, tìm SKU và dòng SKU đã có.
3. Cộng tồn khả dụng max(0, actualStock-heldQuantity) trên mọi cửa hàng.
4. Nếu tổng số lượng sau thêm không vượt tồn khả dụng, lưu dòng mới hoặc cập nhật dòng cũ.
5. Trả lại giỏ và tổng tiền.

**Alternative / Exception Flows**

- SKU không tồn tại: generic Exception -> INTERNAL_ERROR 500.
- Số lượng vượt tổng tồn khả dụng: INSUFFICIENT_STOCK 409.
- DTO thiếu skuId/quantity hoặc quantity < 1: VALIDATION_ERROR 400.

**Postconditions**

- Giỏ chứa SKU với số lượng tăng; chưa giữ tồn.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:39](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L39) | `CartRestController` · `addItem` | `POST /api/cart/items` |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:47](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L47) | `CartServiceImpl` · `addItem` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/templates/product-detail.html:317](../../../../src/main/resources/templates/product-detail.html#L317) | `UI` | `—` |
| [src/main/resources/static/js/api.js:157](../../../../src/main/resources/static/js/api.js#L157) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Không kiểm activeStatus SKU/product/store.
- Tồn cộng toàn hệ thống không bảo đảm một cửa hàng đáp ứng toàn bộ giỏ.
- UI: Requires SKU selection; quantity 1..99; JSON skuId/quantity; updates badge. Backend actual stock checks delegated commerce.

<a id="uc054"></a>

## UC054 — Điều chỉnh số lượng trong giỏ

**Module:** Giỏ hàng  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /cart  
**Quyền:** ROLE_CUSTOMER theo SecurityConfig.

**Mô tả nghiệp vụ:** Đổi số lượng của dòng giỏ hàng.

**Preconditions**

- Đã đăng nhập; cartItemId tồn tại.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Tìm hoặc tạo giỏ của customer resolved.
2. Tìm cartItem theo ID.
3. Quantity <= 0: xóa dòng; nếu > 0 kiểm tổng tồn khả dụng trên các cửa hàng.
4. Lưu số lượng mới nếu đủ tồn và trả giỏ resolved.

**Alternative / Exception Flows**

- Không đủ tồn: INSUFFICIENT_STOCK 409.
- Không có item: generic Exception -> 500.
- CartItem của khách khác vẫn được xử lý vì thiếu kiểm item.cart.id == cart.id.

**Postconditions**

- Số lượng dòng được đổi hoặc dòng bị xóa.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:48](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L48) | `CartRestController` · `updateItemQuantity` | `PUT /api/cart/items/{cartItemId}?quantity=...` |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:82](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L82) | `CartServiceImpl` · `updateItemQuantity` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/templates/cart.html:70](../../../../src/main/resources/templates/cart.html#L70) | `UI` | `—` |
| [src/main/resources/templates/cart.html:169](../../../../src/main/resources/templates/cart.html#L169) | `UI` | `—` |
| [src/main/resources/templates/cart.html:182](../../../../src/main/resources/templates/cart.html#L182) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/domain/dto/response/cart/CartResponse.java:14](../../../../src/main/java/com/thinh/cosmetic/domain/dto/response/cart/CartResponse.java#L14) | `CartResponse` · `implementation` | `—` |

**Giới hạn và đối chiếu UI**

- Không chứng minh quyền sở hữu dòng; đây là lỗi triển khai, không phải chức năng quản lý giỏ người khác.
- Frontend agent xác minh cart.html dùng số lượng SSR cố định ở nút +/-; AJAX đọc itemSubtotal/cartSubtotal không có trong CartResponse nên tổng tiền stale và click lặp không tăng tiếp.
- UI: updateQty calls API; response code expects itemSubtotal/cartSubtotal although CartResponse has items/subtotal/totalItems, therefore money stale until reload. +/- onclick embeds SSR quantity and repeats same target across clicks.

<a id="uc055"></a>

## UC055 — Xóa sản phẩm khỏi giỏ

**Module:** Giỏ hàng  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /cart  
**Quyền:** ROLE_CUSTOMER theo SecurityConfig.

**Mô tả nghiệp vụ:** Xóa một dòng khỏi giỏ.

**Preconditions**

- Đã đăng nhập.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Tìm hoặc tạo giỏ của customer resolved.
2. Xóa cartItem bằng ID.
3. Trả lại giỏ resolved với tổng tiền tính lại.

**Alternative / Exception Flows**

- CartItem thuộc giỏ người khác cũng bị xóa vì không kiểm quyền sở hữu.
- Item không tồn tại: không có business validation riêng; hành vi deleteById do repository.

**Postconditions**

- Dòng giỏ theo ID bị xóa.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:58](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L58) | `CartRestController` · `removeItem` | `DELETE /api/cart/items/{cartItemId}` |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:106](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L106) | `CartServiceImpl` · `removeItem` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/templates/cart.html:201](../../../../src/main/resources/templates/cart.html#L201) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Không gán mục tiêu quản trị giỏ khách khác dù lỗ hổng cho phép.
- UI: Confirm then reload. newQty<1 also routes to remove.

<a id="uc056"></a>

## UC056 — Làm trống giỏ hàng

**Module:** Giỏ hàng  
**Implementation Status:** Backend Only  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** Chưa có.  
**Quyền:** ROLE_CUSTOMER theo SecurityConfig.

**Mô tả nghiệp vụ:** Xóa mọi dòng trong giỏ của khách.

**Preconditions**

- Đã đăng nhập.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Tìm hoặc tạo giỏ của khách.
2. Xóa các cartItem có cartId tương ứng.
3. Trả HTTP 204.

**Alternative / Exception Flows**

- Giỏ đã rỗng: vẫn trả 204.

**Postconditions**

- Giỏ vẫn tồn tại và không còn dòng.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java:67](../../../../src/main/java/com/thinh/cosmetic/rest/cart/CartRestController.java#L67) | `CartRestController` · `clearCart` | `DELETE /api/cart` |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java:113](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/CartServiceImpl.java#L113) | `CartServiceImpl` · `clearCart` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |

<a id="uc057"></a>

## UC057 — Xem sản phẩm yêu thích

**Module:** Yêu thích  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/wishlist  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Xem sản phẩm đã lưu trong wishlist của customer resolved.

**Preconditions**

- API không yêu cầu đăng nhập; customerId nếu được chỉ định phải tồn tại để tạo wishlist mới.

**Main Success Scenario**

1. Tìm wishlist theo customerId của AuthPrincipal, query hoặc fallback 1.
2. Nếu chưa có, tạo wishlist.
3. Đọc sản phẩm, ảnh chính, giá thấp nhất trong các SKU.
4. Trả wishlist.

**Alternative / Exception Flows**

- Không có sản phẩm: danh sách rỗng.
- Customer không tồn tại và chưa có wishlist: customer=null vi phạm entity constraint.

**Postconditions**

- Wishlist được đọc hoặc tạo mới.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java:28](../../../../src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java#L28) | `WishListRestController` · `getWishList` | `GET /api/wishlist` |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java:38](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java#L38) | `WishListServiceImpl` · `getWishList` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:96](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L96) | `AccountPageController` · `implementation` | `—` |
| [src/main/resources/templates/account/wishlist.html:102](../../../../src/main/resources/templates/account/wishlist.html#L102) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Trang /account/wishlist yêu cầu ROLE_CUSTOMER; API rơi vào permitAll.
- Giá min gồm cả SKU inactive; không có lọc active.
- UI: SSR wishListService.getWishList; card links to product detail, no move-to-cart operation.

<a id="uc058"></a>

## UC058 — Lưu sản phẩm yêu thích

**Module:** Yêu thích  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /, /products, /products/{id}  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Thêm sản phẩm vào danh sách yêu thích.

**Preconditions**

- Product tồn tại; API không yêu cầu đăng nhập.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Resolve customerId và tìm/tạo wishlist.
2. Tìm product theo ID.
3. Nếu product chưa có trong wishlist thì lưu wishlist item.
4. Trả wishlist.

**Alternative / Exception Flows**

- Product đã tồn tại: không tạo dòng trùng.
- Product không tồn tại: generic Exception -> 500.
- UI raw fetch thiếu X-Requested-With nên request cookie JWT bị chặn 403 trước controller.

**Postconditions**

- Backend có thể lưu wishlist item nếu request hợp lệ.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java:36](../../../../src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java#L36) | `WishListRestController` · `addProduct` | `POST /api/wishlist/products/{productId}` |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java:44](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java#L44) | `WishListServiceImpl` · `addProduct` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/static/js/api.js:167](../../../../src/main/resources/static/js/api.js#L167) | `UI` | `—` |
| [src/main/resources/static/js/api.js:176](../../../../src/main/resources/static/js/api.js#L176) | `UI` | `—` |
| [src/main/resources/templates/home.html:248](../../../../src/main/resources/templates/home.html#L248) | `UI` | `—` |
| [src/main/resources/templates/product-list.html:152](../../../../src/main/resources/templates/product-list.html#L152) | `UI` | `—` |
| [src/main/resources/templates/product-detail.html:105](../../../../src/main/resources/templates/product-detail.html#L105) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java:36](../../../../src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java#L36) | `XRequestedWithFilter` · `implementation` | `—` |

**Giới hạn và đối chiếu UI**

- Tích hợp UI phải Partially Implemented: frontend agent xác minh header thiếu.
- API cho phép guest sửa wishlist qua customerId/default 1; không coi đây là độc quyền Customer.
- UI: toggleWishlist raw fetch only Content-Type, lacks X-Requested-With. Blocked by filter with cookie JWT. Hearts not initialized from server wishlist state.

<a id="uc059"></a>

## UC059 — Bỏ sản phẩm yêu thích

**Module:** Yêu thích  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/wishlist, /, /products, /products/{id}  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Xóa một sản phẩm khỏi wishlist.

**Preconditions**

- API không yêu cầu đăng nhập.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Resolve customerId và tìm/tạo wishlist.
2. Xóa item bằng wishlistId + productId.
3. Trả wishlist.

**Alternative / Exception Flows**

- Product không ở wishlist: không có business exception riêng.
- UI raw fetch thiếu X-Requested-With: 403 trước controller.

**Postconditions**

- Wishlist không còn sản phẩm chỉ định.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java:45](../../../../src/main/java/com/thinh/cosmetic/rest/cart/WishListRestController.java#L45) | `WishListRestController` · `removeProduct` | `DELETE /api/wishlist/products/{productId}` |
| [src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java:60](../../../../src/main/java/com/thinh/cosmetic/service/cart/impl/WishListServiceImpl.java#L60) | `WishListServiceImpl` · `removeProduct` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/static/js/api.js:167](../../../../src/main/resources/static/js/api.js#L167) | `UI` | `—` |
| [src/main/resources/templates/account/wishlist.html:165](../../../../src/main/resources/templates/account/wishlist.html#L165) | `UI` | `—` |
| [src/main/resources/templates/account/wishlist.html:172](../../../../src/main/resources/templates/account/wishlist.html#L172) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java:36](../../../../src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java#L36) | `XRequestedWithFilter` · `implementation` | `—` |

**Giới hạn và đối chiếu UI**

- Tích hợp UI phải Partially Implemented do header thiếu.
- UI: Both toggleWishlist and removeWishlistItem raw fetch omit X-Requested-With -> 403.

<a id="uc060"></a>

## UC060 — Xem tổng tiền trước khi đặt hàng

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /checkout  
**Quyền:** ROLE_CUSTOMER theo SecurityConfig.

**Mô tả nghiệp vụ:** Tính tổng giỏ, phí giao hàng và giảm giá mà chưa tạo đơn.

**Preconditions**

- Đã đăng nhập ROLE_CUSTOMER; customer và giỏ có sản phẩm tồn tại.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Gửi OrderRequest tới /api/orders/preview; controller không @Valid ở đường preview.
2. Đọc khách và giỏ, snapshot giá hiện tại vào response.
3. Tính subtotal; shipping 0 nếu subtotal >= 500000, ngược lại 30000.
4. Áp dụng mã hardcode và tính max(0, subtotal-discount+shipping).
5. Trả tổng tiền và dòng sản phẩm; không lưu order/reservation.

**Alternative / Exception Flows**

- Giỏ rỗng: EMPTY_CART 400.
- Customer không tồn tại: CUSTOMER_NOT_FOUND 404.
- Mã lạ: không báo lỗi, discount 0; shipping theo threshold.

**Postconditions**

- Khách nhận tổng tiền dự kiến; DB không tạo đơn.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:37](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L37) | `OrderRestController` · `previewOrder` | `POST /api/orders/preview` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:249](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L249) | `OrderServiceImpl` · `previewOrder` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java:30](../../../../src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java#L30) | `CheckoutPageController` · `implementation` | `—` |
| [src/main/resources/templates/checkout.html:39](../../../../src/main/resources/templates/checkout.html#L39) | `UI` | `—` |
| [src/main/resources/templates/checkout.html:263](../../../../src/main/resources/templates/checkout.html#L263) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Không kiểm tồn; không bắt buộc gọi preview trước placeOrder.
- UI: SSR cart+saved addresses+stores loaded; UI selects existing address or new. Only standard delivery and COD. No store selection or deliveryMethod payload.
- SSR /checkout hiển thị giá giỏ; preview API được UI dùng khi áp mã. Chưa tạo đơn hoặc giữ tồn ở bước xem tổng.

<a id="uc061"></a>

## UC061 — Áp dụng mã ưu đãi khi thanh toán

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /checkout  
**Quyền:** ROLE_CUSTOMER theo SecurityConfig.

**Mô tả nghiệp vụ:** Áp dụng một trong ba mã hardcode trong preview và đặt hàng.

**Preconditions**

- Đã đăng nhập; giỏ không rỗng; nhập mã.REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Chuẩn hóa voucherCode trim và uppercase.
2. LUNEA10: giảm 10% subtotal, tối đa 100000; GIAM50K: giảm 50000; FREESHIP: phí ship 0.
3. Trả tổng dự kiến qua preview; khi đặt hàng tính lại cùng rule và lưu discountAmount/shippingFee.

**Alternative / Exception Flows**

- Mã khác hoặc rỗng: discount 0; không có validation mã/expiry/quota.
- Tổng tiền không âm dù discount > subtotal.

**Postconditions**

- Tổng tiền thay đổi và discountAmount được lưu khi đặt đơn.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:37](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L37) | `OrderRestController` · `previewOrder` | `POST /api/orders/preview` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:249](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L249) | `OrderServiceImpl` · `previewOrder` | `—` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:64](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L64) | `OrderServiceImpl` · `placeOrder` | `POST /api/orders` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/templates/checkout.html:295](../../../../src/main/resources/templates/checkout.html#L295) | `UI` | `—` |
| [src/main/resources/templates/checkout.html:302](../../../../src/main/resources/templates/checkout.html#L302) | `UI` | `—` |
| [src/main/resources/templates/checkout.html:311](../../../../src/main/resources/templates/checkout.html#L311) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Không có module quản trị voucher thật; không có lưu usage hay hoàn lượt sử dụng.
- Không tự tạo extend: root cần extension point UI đọc đầy đủ; backend có thể nhận voucher ngay trong placeOrder.
- UI: Payload only voucherCode. Uses preview.discountAmount/shippingFee. On every API error, hardcoded client discount for LUNEA10/GIAM50K/FREESHIP claims success regardless server validity.

<a id="uc062"></a>

## UC062 — Đặt hàng thanh toán khi nhận

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /checkout  
**Quyền:** authenticated() cho POST /api/orders và /api/orders/checkout; trang /checkout ROLE_CUSTOMER.

**Mô tả nghiệp vụ:** Tạo đơn COD từ toàn bộ giỏ, xác định cửa hàng phục vụ, giữ tồn và xóa giỏ.

**Preconditions**

- API /api/orders và /api/orders/checkout yêu cầu authenticated(); trang checkout ROLE_CUSTOMER. Customer/cart tồn tại và có dòng; paymentMethod COD (@NotNull).REST mutation: dùng Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Resolve customerId từ AuthPrincipal, query hoặc fallback 1; đọc khách và cart items.
2. Lấy địa chỉ đã lưu nếu ID tồn tại; nếu không dùng request/customer/fallback và thử lưu địa chỉ mới.
3. Tính subtotal, shipping, discount và total.
4. Nếu fulfillmentStoreId tồn tại thì chọn store đó; nếu không tìm store đủ mọi SKU, ưu tiên province, fallback capable đầu hoặc store đầu.
5. Tạo orderCode LUN- ngẫu nhiên 6 số, order PENDING và các dòng snapshot.
6. Tạo HELD reservation cho từng SKU và tăng heldQuantity nếu inventory record tồn tại.
7. Xóa cart items, trả đơn HTTP 201.

**Alternative / Exception Flows**

- Customer không có: 404; giỏ rỗng: 400.
- Không store nào đủ tồn: code vẫn chọn store đầu; không trả NO_STORE_CAN_FULFILL.
- Không store nào tồn tại: assignedStore=null mâu thuẫn @NotNull/DB.
- OrderCode collision không retry; persistence có thể DATA_CONFLICT.
- CustomerAddressId không kiểm thuộc khách; deliveryMethod bỏ qua.

**Postconditions**

- Đơn PENDING, dòng snapshot và stock holds được tạo; giỏ đã xóa trong transaction.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:46](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L46) | `OrderRestController` · `placeOrder` | `POST /api/orders; POST /api/orders/checkout` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:64](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L64) | `OrderServiceImpl` · `placeOrder` | `—` |
| [src/main/java/com/thinh/cosmetic/domain/dto/request/order/OrderRequest.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/order/OrderRequest.java#L1) | `OrderRequest` · `entity/DTO field` | `—` |
| [src/main/java/com/thinh/cosmetic/domain/entity/sales/OrderEntity.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/entity/sales/OrderEntity.java#L1) | `OrderEntity` · `entity/DTO field` | `—` |
| [src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java:12](../../../../src/main/java/com/thinh/cosmetic/repository/store/InventoryRepository.java#L12) | `InventoryRepository` · `findByStoreIdAndSkuId` | `—` |
| [src/main/java/com/thinh/cosmetic/repository/order/OrderReservationRepository.java:1](../../../../src/main/java/com/thinh/cosmetic/repository/order/OrderReservationRepository.java#L1) | `OrderReservationRepository` · `save` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java:26](../../../../src/main/java/com/thinh/cosmetic/security/XRequestedWithFilter.java#L26) | `XRequestedWithFilter` · `doFilterInternal` | `—` |
| [src/main/resources/templates/checkout.html:333](../../../../src/main/resources/templates/checkout.html#L333) | `UI` | `—` |
| [src/main/resources/templates/checkout.html:344](../../../../src/main/resources/templates/checkout.html#L344) | `UI` | `—` |
| [src/main/resources/templates/checkout.html:355](../../../../src/main/resources/templates/checkout.html#L355) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Không có hỗ trợ thanh toán online hoặc external payment actor: enum chỉ COD.
- deliveryMethod STORE_PICKUP trong DTO bị bỏ qua; order luôn mặc định HOME_DELIVERY.
- Không recheck/lock tồn khi giữ; store được chọn trực tiếp có thể thiếu tồn; có thể vượt actualStock.
- Không lọc inactive store/SKU/product.
- API placeOrder cho employee authenticated chỉ định customerId dù UI dành cho Customer; không dùng ORDER_PROCESS/ORDER_VIEW permission.
- UI: Sends recipientName/Phone, province/district/ward, shippingAddress, note, COD, voucherCode, customerAddressId. Does not send storeId/deliveryMethod/expectedTotal. Backend assigned store, response orderCode -> success page.
- UI không có chọn fulfillmentStoreId; chỉ API mới nhận cửa hàng chỉ định. Không có endpoint nhân viên gán lại chi nhánh.

<a id="uc063"></a>

## UC063 — Xem lịch sử đơn hàng

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /account/orders  
**Quyền:** /my-orders: ROLE_CUSTOMER; /customer/{customerId}: authenticated(), không ownership; page /account/orders: ROLE_CUSTOMER.

**Mô tả nghiệp vụ:** Xem các đơn của khách hiện tại theo thời gian giảm dần.

**Preconditions**

- Đã đăng nhập ROLE_CUSTOMER.

**Main Success Scenario**

1. Mở /account/orders hoặc GET /api/orders/my-orders.
2. Lấy customerId từ AuthPrincipal.
3. Truy vấn các đơn customerId, createdAt giảm dần.
4. Trả danh sách cùng trạng thái, tổng tiền, dòng đơn.

**Alternative / Exception Flows**

- Chưa có đơn: danh sách rỗng.
- Fallback customerId query/1 khi AuthPrincipal không có customerId.

**Postconditions**

- Lịch sử đơn được hiển thị.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:90](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L90) | `OrderRestController` · `getMyOrders` | `GET /api/orders/my-orders` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:342](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L342) | `OrderServiceImpl` · `getByCustomer` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:52](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L52) | `AccountPageController` · `orders` | `GET /account/orders` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:55](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L55) | `AccountPageController` · `implementation` | `—` |
| [src/main/resources/templates/account/orders.html:198](../../../../src/main/resources/templates/account/orders.html#L198) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:85](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L85) | `OrderRestController` · `getByCustomer` | `GET /api/orders/customer/{customerId}` |

**Giới hạn và đối chiếu UI**

- REST /api/orders/customer/{customerId} chỉ yêu cầu authenticated(), không kiểm owner; ghi như lỗ hổng, không UC xem dữ liệu khách khác.
- UI: SSR getByCustomer; status filtering local DOM including PENDING/CONFIRMED/PREPARING/SHIPPING/COMPLETED/CANCELLED.

<a id="uc064"></a>

## UC064 — Theo dõi chi tiết đơn hàng

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /account/orders/{id}, /order-success  
**Quyền:** REST /api/orders/{id} và /code/{code}: authenticated(); page /account/orders/{id}, /order-success: ROLE_CUSTOMER.

**Mô tả nghiệp vụ:** Xem đơn bằng ID hoặc orderCode và các trạng thái xử lý.

**Preconditions**

- Đã đăng nhập ROLE_CUSTOMER; đơn tồn tại.

**Main Success Scenario**

1. Mở /account/orders/{id} hoặc GET /api/orders/{id} hay /code/{orderCode}.
2. Đọc đơn và dòng snapshot.
3. Nếu AuthPrincipal customerId khác owner, REST hoặc account page từ chối/redirect.
4. Trả recipient/address, trạng thái, timestamps, items và totals.

**Alternative / Exception Flows**

- Đơn không có: ORDER_NOT_FOUND 404; page ghi log.
- Sai owner: REST ACCESS_DENIED 403; account page redirect /account/orders.
- Trang /order-success?orderCode=... gọi getByOrderCode không kiểm owner, gây lộ đơn theo mã.

**Postconditions**

- Chi tiết đơn được đọc.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:55](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L55) | `OrderRestController` · `getById` | `GET /api/orders/{id}` |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:71](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L71) | `OrderRestController` · `getByOrderCode` | `GET /api/orders/code/{orderCode}` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:313](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L313) | `OrderServiceImpl` · `getById` | `—` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:334](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L334) | `OrderServiceImpl` · `getByOrderCode` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:69](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L69) | `AccountPageController` · `orderDetail` | `GET /account/orders/{id}` |
| [src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java:46](../../../../src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java#L46) | `CheckoutPageController` · `orderSuccess` | `GET /order-success` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:72](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L72) | `AccountPageController` · `implementation` | `—` |
| [src/main/resources/templates/account/order-detail.html:22](../../../../src/main/resources/templates/account/order-detail.html#L22) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java:52](../../../../src/main/java/com/thinh/cosmetic/controller/CheckoutPageController.java#L52) | `CheckoutPageController` · `implementation` | `—` |
| [src/main/resources/templates/order-success.html:44](../../../../src/main/resources/templates/order-success.html#L44) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Employee gọi REST by-id có StoreScope; by-code không có StoreScope employee.
- UI: SSR getById+getByOrder; checks customer owner only when principal and non-null customerId/order.customerId. Shows snapshot, delivery, totals, stepper, return records.
- UI: SSR getByOrderCode; page displays orderCode/store/recipient/total. Status text fixed Chờ xác nhận and fallback totals when no order. Controller has no owner check here (commerce/security root cross-check).

<a id="uc065"></a>

## UC065 — Hủy đơn đang chờ xác nhận

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /account/orders, /account/orders/{id}  
**Quyền:** ROLE_CUSTOMER theo SecurityConfig.

**Mô tả nghiệp vụ:** Khách hủy đơn của mình khi PENDING và giải phóng stock holds.

**Preconditions**

- Đã đăng nhập ROLE_CUSTOMER; đơn thuộc customer; status PENDING. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Gửi POST/PUT /api/orders/{id}/cancel.
2. Kiểm order owner và status PENDING.
3. Chuyển CANCELLED, ghi cancelledAt.
4. Với mỗi HELD, chuyển RELEASED và giảm heldQuantity tối thiểu 0.
5. Lưu order và trả response.

**Alternative / Exception Flows**

- Không đúng owner: ACCESS_DENIED 403.
- Không PENDING: ORDER_NOT_CANCELLABLE 409.
- Không order: ORDER_NOT_FOUND 404.

**Postconditions**

- Đơn CANCELLED và tồn giữ HELD được giải phóng.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:115](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L115) | `OrderRestController` · `cancelOrder` | `PUT/POST /api/orders/{id}/cancel` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:460](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L460) | `OrderServiceImpl` · `cancelOrder` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/templates/account/orders.html:172](../../../../src/main/resources/templates/account/orders.html#L172) | `UI` | `—` |
| [src/main/resources/templates/account/orders.html:247](../../../../src/main/resources/templates/account/orders.html#L247) | `UI` | `—` |
| [src/main/resources/templates/account/order-detail.html:247](../../../../src/main/resources/templates/account/order-detail.html#L247) | `UI` | `—` |
| [src/main/resources/templates/account/order-detail.html:338](../../../../src/main/resources/templates/account/order-detail.html#L338) | `UI` | `—` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:459](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L459) | `OrderServiceImpl` · `implementation` | `—` |

**Giới hạn và đối chiếu UI**

- Không hoàn lượt dùng voucher vì không có usage ledger.
- UI: UI and OrderServiceImpl.cancelOrder both allow cancellation only PENDING. Confirmation and reload; staff cancellation differs (PENDING/CONFIRMED/PREPARING status workflow).

<a id="uc066"></a>

## UC066 — Tra cứu đơn hàng trong phạm vi cửa hàng

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin/orders  
**Quyền:** ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Mô tả nghiệp vụ:** Nhân viên xem và lọc đơn theo cửa hàng được phân công.

**Preconditions**

- Đã đăng nhập ROLE_EMPLOYEE; phạm vi từ EmployeeStore hoặc ALL_STORES/ROLE_ADMIN/ADMIN.

**Main Success Scenario**

1. Gọi /api/admin/orders hoặc /api/orders, tùy chọn storeId.
2. StoreScope trả null cho quyền toàn chuỗi; ngược lại danh sách assigned store ids.
3. Nếu toàn chuỗi, lấy tất cả hoặc store chỉ định; nếu được phân công, chỉ truy vấn assigned store ids.
4. Sắp xếp createdAt giảm dần và trả danh sách.

**Alternative / Exception Flows**

- Không có store assignment: danh sách rỗng.
- storeId ngoài scope: danh sách rỗng.
- Principal null trong service overload: trả getAll; normal secured JWT flow phải có principal.

**Postconditions**

- Danh sách trong scope được đọc.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:22](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L22) | `AdminOrderRestController` · `getOrders` | `GET /api/admin/orders` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:355](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L355) | `OrderServiceImpl` · `getOrders` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | `StoreScope` · `getAccessibleStoreIds` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | `StoreScope` · `canAccessStore` | `—` |
| [src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java:10](../../../../src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java#L10) | `OrderRepository` · `findByAssignedStoreIdOrderByCreatedAtDesc` | `—` |
| [src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java:11](../../../../src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java#L11) | `OrderRepository` · `findByAssignedStoreIdInOrderByCreatedAtDesc` | `—` |
| [src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java:12](../../../../src/main/java/com/thinh/cosmetic/repository/order/OrderRepository.java#L12) | `OrderRepository` · `findAllByOrderByCreatedAtDesc` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:98](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L98) | `OrderRestController` · `getAll` | `GET /api/orders?storeId=...` |
| [src/main/java/com/thinh/cosmetic/controller/AdminPageController.java:25](../../../../src/main/java/com/thinh/cosmetic/controller/AdminPageController.java#L25) | `AdminPageController` · `implementation` | `—` |
| [src/main/resources/templates/admin/orders.html:113](../../../../src/main/resources/templates/admin/orders.html#L113) | `UI` | `—` |
| [src/main/resources/templates/admin/orders.html:172](../../../../src/main/resources/templates/admin/orders.html#L172) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Controller getOrders delegates service method getOrders; REST method name is getOrders in Admin and getAll in Order.
- Các quyền ORDER_VIEW/ORDER_PROCESS không được dùng để gate endpoint.
- UI: Branch selector from StoreScope; local status/code/name/phone filters. Admin any/all branches, other employee accessibleStores only.

<a id="uc067"></a>

## UC067 — Xem chi tiết đơn của cửa hàng

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin/orders/{id}  
**Quyền:** ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Mô tả nghiệp vụ:** Nhân viên đọc chi tiết đơn thuộc chi nhánh trong scope.

**Preconditions**

- Đã đăng nhập ROLE_EMPLOYEE; order thuộc cửa hàng accessible.

**Main Success Scenario**

1. Gọi /api/admin/orders/{id} hoặc /api/orders/{id}.
2. Tìm order.
3. Nếu assignedStore có giá trị, kiểm StoreScope.canAccessStore.
4. Trả order và dòng snapshot.

**Alternative / Exception Flows**

- Ngoài scope: ACCESS_DENIED 403.
- Không tìm thấy: ORDER_NOT_FOUND 404.
- Order assignedStore null thì scope check bị bỏ qua, nhưng entity bình thường cấm null.

**Postconditions**

- Chi tiết đơn trong scope được đọc.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:30](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L30) | `AdminOrderRestController` · `getById` | `GET /api/admin/orders/{id}` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:321](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L321) | `OrderServiceImpl` · `getById(AuthPrincipal principal, ...)` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | `StoreScope` · `getAccessibleStoreIds` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | `StoreScope` · `canAccessStore` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:55](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L55) | `OrderRestController` · `getById` | `GET /api/orders/{id}` |
| [src/main/resources/templates/admin/order-detail.html:212](../../../../src/main/resources/templates/admin/order-detail.html#L212) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Đường /api/orders/code/{orderCode} không áp dụng scope employee.
- UI: AJAX render snapshots/prices/address/stepper; data-order-id from page Model.

<a id="uc068"></a>

## UC068 — Xác nhận đơn hàng

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin/orders, /admin/orders/{id}  
**Quyền:** ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Mô tả nghiệp vụ:** Nhân viên cập nhật tiến trình đơn sang CONFIRMED.

**Preconditions**

- Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Chọn thao tác tương ứng và gọi status endpoint (hoặc cancel endpoint).
2. Tìm order và kiểm StoreScope.
3. Nếu cùng status, trả ngay; nếu hiện tại CANCELLED/COMPLETED và khác status thì từ chối.
4. Gán CONFIRMED. Ghi confirmedAt, giữ HELD reservation.
5. Lưu order và trả response.

**Alternative / Exception Flows**

- Ngoài scope: ACCESS_DENIED 403.
- Sửa terminal order: INVALID_STATUS_TRANSITION 409.
- Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.

**Postconditions**

- Status=CONFIRMED và các effects được lưu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | `AdminOrderRestController` · `updateStatus` | `PUT/POST /api/admin/orders/{id}/status?status=CONFIRMED` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:393](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L393) | `OrderServiceImpl` · `updateStatus(AuthPrincipal principal, ...)` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | `StoreScope` · `getAccessibleStoreIds` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | `StoreScope` · `canAccessStore` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | `OrderRestController` · `updateStatus` | `PUT/POST /api/orders/{id}/status?status=CONFIRMED` |
| [src/main/resources/templates/admin/order-detail.html:457](../../../../src/main/resources/templates/admin/order-detail.html#L457) | `UI` | `—` |
| [src/main/resources/templates/admin/order-detail.html:473](../../../../src/main/resources/templates/admin/order-detail.html#L473) | `UI` | `—` |
| [src/main/resources/templates/admin/orders.html:243](../../../../src/main/resources/templates/admin/orders.html#L243) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Các hold HELD chỉ commit/release một lần theo trạng thái reservation.
- Không có payment collection integration, courier API hoặc xác nhận giao vận ngoài hệ thống.
- UI có thể giới hạn nút theo trạng thái nhưng backend không enforced predecessor; root merge UI exact.
- UI: State-dependent action; quick confirm in orders table; confirmation for detail. Header wrapper correct. No role/permission-specific hide beyond status; actual backend authorization must decide.

<a id="uc069"></a>

## UC069 — Chuyển đơn sang chuẩn bị hàng

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin/orders/{id}  
**Quyền:** ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Mô tả nghiệp vụ:** Nhân viên cập nhật tiến trình đơn sang PREPARING.

**Preconditions**

- Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Chọn thao tác tương ứng và gọi status endpoint (hoặc cancel endpoint).
2. Tìm order và kiểm StoreScope.
3. Nếu cùng status, trả ngay; nếu hiện tại CANCELLED/COMPLETED và khác status thì từ chối.
4. Gán PREPARING. Đổi trạng thái, không ghi timestamp hoặc thay đổi tồn.
5. Lưu order và trả response.

**Alternative / Exception Flows**

- Ngoài scope: ACCESS_DENIED 403.
- Sửa terminal order: INVALID_STATUS_TRANSITION 409.
- Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.

**Postconditions**

- Status=PREPARING và các effects được lưu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | `AdminOrderRestController` · `updateStatus` | `PUT/POST /api/admin/orders/{id}/status?status=PREPARING` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:393](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L393) | `OrderServiceImpl` · `updateStatus(AuthPrincipal principal, ...)` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | `StoreScope` · `getAccessibleStoreIds` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | `StoreScope` · `canAccessStore` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | `OrderRestController` · `updateStatus` | `PUT/POST /api/orders/{id}/status?status=PREPARING` |
| [src/main/resources/templates/admin/order-detail.html:393](../../../../src/main/resources/templates/admin/order-detail.html#L393) | `UI` | `—` |
| [src/main/resources/templates/admin/order-detail.html:457](../../../../src/main/resources/templates/admin/order-detail.html#L457) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Các hold HELD chỉ commit/release một lần theo trạng thái reservation.
- Không có payment collection integration, courier API hoặc xác nhận giao vận ngoài hệ thống.
- UI có thể giới hạn nút theo trạng thái nhưng backend không enforced predecessor; root merge UI exact.
- UI: State-dependent action; quick confirm in orders table; confirmation for detail. Header wrapper correct. No role/permission-specific hide beyond status; actual backend authorization must decide.

<a id="uc070"></a>

## UC070 — Bàn giao đơn để giao hàng

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin/orders/{id}  
**Quyền:** ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Mô tả nghiệp vụ:** Nhân viên cập nhật tiến trình đơn sang SHIPPING.

**Preconditions**

- Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Chọn thao tác tương ứng và gọi status endpoint (hoặc cancel endpoint).
2. Tìm order và kiểm StoreScope.
3. Nếu cùng status, trả ngay; nếu hiện tại CANCELLED/COMPLETED và khác status thì từ chối.
4. Gán SHIPPING. Với HELD reservation, chuyển COMMITTED và giảm actualStock/heldQuantity tối thiểu 0.
5. Lưu order và trả response.

**Alternative / Exception Flows**

- Ngoài scope: ACCESS_DENIED 403.
- Sửa terminal order: INVALID_STATUS_TRANSITION 409.
- Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.

**Postconditions**

- Status=SHIPPING và các effects được lưu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | `AdminOrderRestController` · `updateStatus` | `PUT/POST /api/admin/orders/{id}/status?status=SHIPPING` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:393](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L393) | `OrderServiceImpl` · `updateStatus(AuthPrincipal principal, ...)` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | `StoreScope` · `getAccessibleStoreIds` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | `StoreScope` · `canAccessStore` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | `OrderRestController` · `updateStatus` | `PUT/POST /api/orders/{id}/status?status=SHIPPING` |
| [src/main/resources/templates/admin/order-detail.html:417](../../../../src/main/resources/templates/admin/order-detail.html#L417) | `UI` | `—` |
| [src/main/resources/templates/admin/order-detail.html:457](../../../../src/main/resources/templates/admin/order-detail.html#L457) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Các hold HELD chỉ commit/release một lần theo trạng thái reservation.
- Không có payment collection integration, courier API hoặc xác nhận giao vận ngoài hệ thống.
- UI có thể giới hạn nút theo trạng thái nhưng backend không enforced predecessor; root merge UI exact.
- UI: State-dependent action; quick confirm in orders table; confirmation for detail. Header wrapper correct. No role/permission-specific hide beyond status; actual backend authorization must decide.

<a id="uc071"></a>

## UC071 — Ghi nhận đơn giao thành công

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin/orders/{id}  
**Quyền:** ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Mô tả nghiệp vụ:** Nhân viên cập nhật tiến trình đơn sang COMPLETED.

**Preconditions**

- Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Chọn thao tác tương ứng và gọi status endpoint (hoặc cancel endpoint).
2. Tìm order và kiểm StoreScope.
3. Nếu cùng status, trả ngay; nếu hiện tại CANCELLED/COMPLETED và khác status thì từ chối.
4. Gán COMPLETED. Ghi completedAt; commit HELD nếu chưa commit; đơn trở thành terminal.
5. Lưu order và trả response.

**Alternative / Exception Flows**

- Ngoài scope: ACCESS_DENIED 403.
- Sửa terminal order: INVALID_STATUS_TRANSITION 409.
- Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.

**Postconditions**

- Status=COMPLETED và các effects được lưu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | `AdminOrderRestController` · `updateStatus` | `PUT/POST /api/admin/orders/{id}/status?status=COMPLETED` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:393](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L393) | `OrderServiceImpl` · `updateStatus(AuthPrincipal principal, ...)` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | `StoreScope` · `getAccessibleStoreIds` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | `StoreScope` · `canAccessStore` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | `OrderRestController` · `updateStatus` | `PUT/POST /api/orders/{id}/status?status=COMPLETED` |
| [src/main/resources/templates/admin/order-detail.html:435](../../../../src/main/resources/templates/admin/order-detail.html#L435) | `UI` | `—` |
| [src/main/resources/templates/admin/order-detail.html:457](../../../../src/main/resources/templates/admin/order-detail.html#L457) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Các hold HELD chỉ commit/release một lần theo trạng thái reservation.
- Không có payment collection integration, courier API hoặc xác nhận giao vận ngoài hệ thống.
- UI có thể giới hạn nút theo trạng thái nhưng backend không enforced predecessor; root merge UI exact.
- UI: State-dependent action; quick confirm in orders table; confirmation for detail. Header wrapper correct. No role/permission-specific hide beyond status; actual backend authorization must decide.

<a id="uc072"></a>

## UC072 — Hủy đơn trong phạm vi cửa hàng

**Module:** Đặt hàng và xử lý đơn  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin/orders/{id}  
**Quyền:** ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Mô tả nghiệp vụ:** Nhân viên cập nhật tiến trình đơn sang CANCELLED.

**Preconditions**

- Đã đăng nhập ROLE_EMPLOYEE; order trong StoreScope; trạng thái hiện tại chưa CANCELLED/COMPLETED hoặc đã bằng đích. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Chọn thao tác tương ứng và gọi status endpoint (hoặc cancel endpoint).
2. Tìm order và kiểm StoreScope.
3. Nếu cùng status, trả ngay; nếu hiện tại CANCELLED/COMPLETED và khác status thì từ chối.
4. Gán CANCELLED. Ghi cancelledAt; HELD chuyển RELEASED và giảm heldQuantity; COMMITTED không phục hồi.
5. Lưu order và trả response.

**Alternative / Exception Flows**

- Ngoài scope: ACCESS_DENIED 403.
- Sửa terminal order: INVALID_STATUS_TRANSITION 409.
- Các trạng thái non-terminal được nhảy tự do hoặc đi lùi vì không có bảng transition.

**Postconditions**

- Status=CANCELLED và các effects được lưu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L47) | `AdminOrderRestController` · `cancelOrder` | `PUT/POST /api/admin/orders/{id}/cancel` |
| [src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java:393](../../../../src/main/java/com/thinh/cosmetic/service/order/impl/OrderServiceImpl.java#L393) | `OrderServiceImpl` · `updateStatus(AuthPrincipal principal, ...)` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:19](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L19) | `StoreScope` · `getAccessibleStoreIds` | `—` |
| [src/main/java/com/thinh/cosmetic/security/StoreScope.java:43](../../../../src/main/java/com/thinh/cosmetic/security/StoreScope.java#L43) | `StoreScope` · `canAccessStore` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:38](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L38) | `AdminOrderRestController` · `updateStatus` | `PUT/POST /api/admin/orders/{id}/status?status=CANCELLED` |
| [src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java:106](../../../../src/main/java/com/thinh/cosmetic/rest/order/OrderRestController.java#L106) | `OrderRestController` · `updateStatus` | `PUT/POST /api/orders/{id}/status?status=CANCELLED` |
| [src/main/resources/templates/admin/order-detail.html:372](../../../../src/main/resources/templates/admin/order-detail.html#L372) | `UI` | `—` |
| [src/main/resources/templates/admin/order-detail.html:457](../../../../src/main/resources/templates/admin/order-detail.html#L457) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Các hold HELD chỉ commit/release một lần theo trạng thái reservation.
- Không có payment collection integration, courier API hoặc xác nhận giao vận ngoài hệ thống.
- UI có thể giới hạn nút theo trạng thái nhưng backend không enforced predecessor; root merge UI exact.
- UI: State-dependent action; quick confirm in orders table; confirmation for detail. Header wrapper correct. No role/permission-specific hide beyond status; actual backend authorization must decide.

<a id="uc073"></a>

## UC073 — Gửi yêu cầu trả hàng

**Module:** Yêu cầu trả hàng  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Khách hàng  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/orders/{id}  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Khách gửi yêu cầu trả hàng có lý do và các dòng đơn.

**Preconditions**

- API công khai; orderId tồn tại, reason không trống, items không rỗng. UI chỉ hiện với order COMPLETED chưa có return. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Nhận DTO và @Valid kiểm top-level orderId/reason/items.
2. Tìm order.
3. Build ReturnRequest với order/reason/REQUESTED nhưng không gán customer hoặc description.
4. Gọi repository.save.
5. Nếu có thể qua save, tìm các orderItem theo ID và lưu return items rồi trả response.

**Alternative / Exception Flows**

- Đường persistence thật không đáp ứng @NotNull/nullable=false cho customer: không có main success được chứng minh.
- Items DTO không có @Valid cascade, quantity nested chưa validated ở controller.
- Không kiểm orderItem thuộc order, quantity <= ordered, order status, owner hoặc duplicate request.
- Order/orderItem không có: generic Exception 500.

**Postconditions**

- Không thể khẳng định request được tạo thành công; entity customer bắt buộc đang thiếu.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:22](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L22) | `ReturnRestController` · `create` | `POST /api/returns` |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:34](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L34) | `ReturnServiceImpl` · `create` | `—` |
| [src/main/java/com/thinh/cosmetic/domain/entity/returns/ReturnRequestEntity.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/entity/returns/ReturnRequestEntity.java#L1) | `ReturnRequestEntity` · `entity/DTO field` | `—` |
| [src/main/java/com/thinh/cosmetic/domain/dto/request/returns/ReturnRequestDto.java:1](../../../../src/main/java/com/thinh/cosmetic/domain/dto/request/returns/ReturnRequestDto.java#L1) | `ReturnRequestDto` · `entity/DTO field` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/templates/account/order-detail.html:255](../../../../src/main/resources/templates/account/order-detail.html#L255) | `UI` | `—` |
| [src/main/resources/templates/account/order-detail.html:290](../../../../src/main/resources/templates/account/order-detail.html#L290) | `UI` | `—` |
| [src/main/resources/templates/account/order-detail.html:365](../../../../src/main/resources/templates/account/order-detail.html#L365) | `UI` | `—` |
| [src/main/resources/templates/account/order-detail.html:392](../../../../src/main/resources/templates/account/order-detail.html#L392) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- ReturnServiceTest mocks save, không validate entity/DB, nên success test không chứng minh persist thật.
- Không có cơ chế refund hoặc nhập lại inventory.
- UI: UI visible only COMPLETED and no prior request. Check subset order items; quantity hidden full purchased qty, no partial-quantity editing. Sends orderId/reason/description/items. Backend customer required association missing per commerce analysis.

<a id="uc074"></a>

## UC074 — Theo dõi yêu cầu trả hàng

**Module:** Yêu cầu trả hàng  
**Implementation Status:** Implemented  
**Primary Actor:** Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /account/orders/{id}, /admin/orders/{id}  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Xem yêu cầu trả hàng bằng ID hoặc theo order/customer; API còn cho xem toàn bộ.

**Preconditions**

- API không cần đăng nhập; record/order/customer dùng để tra cứu.

**Main Success Scenario**

1. Gọi GET /api/returns/{id}, /order/{orderId}, /customer/{customerId}, hoặc /api/returns.
2. Repository đọc request, order và các return items.
3. Map trạng thái, lý do, processedByName, thời điểm và các dòng.
4. Trả DTO/list; UI customer order detail và admin order detail hiển thị theo orderId.

**Alternative / Exception Flows**

- Không request với ID: generic Exception 500.
- Không record trong query list: danh sách rỗng.
- Không kiểm owner/customer/store scope.

**Postconditions**

- Request records được đọc.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:27](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L27) | `ReturnRestController` · `getById` | `GET /api/returns/{id}` |
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:32](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L32) | `ReturnRestController` · `getAll` | `GET /api/returns` |
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:37](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L37) | `ReturnRestController` · `getByCustomer` | `GET /api/returns/customer/{customerId}` |
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:42](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L42) | `ReturnRestController` · `getByOrder` | `GET /api/returns/order/{orderId}` |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:65](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L65) | `ReturnServiceImpl` · `getById` | `—` |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:73](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L73) | `ReturnServiceImpl` · `getAll` | `—` |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:79](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L79) | `ReturnServiceImpl` · `getByCustomer` | `—` |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:85](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L85) | `ReturnServiceImpl` · `getByOrder` | `—` |
| [src/main/java/com/thinh/cosmetic/repository/returns/ReturnRequestRepository.java:10](../../../../src/main/java/com/thinh/cosmetic/repository/returns/ReturnRequestRepository.java#L10) | `ReturnRequestRepository` · `findByOrderId` | `—` |
| [src/main/java/com/thinh/cosmetic/repository/returns/ReturnRequestRepository.java:13](../../../../src/main/java/com/thinh/cosmetic/repository/returns/ReturnRequestRepository.java#L13) | `ReturnRequestRepository` · `findByCustomerId` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/java/com/thinh/cosmetic/controller/AccountPageController.java:88](../../../../src/main/java/com/thinh/cosmetic/controller/AccountPageController.java#L88) | `AccountPageController` · `implementation` | `—` |
| [src/main/resources/templates/account/order-detail.html:220](../../../../src/main/resources/templates/account/order-detail.html#L220) | `UI` | `—` |
| [src/main/resources/templates/admin/order-detail.html:224](../../../../src/main/resources/templates/admin/order-detail.html#L224) | `UI` | `—` |
| [src/main/resources/templates/admin/order-detail.html:235](../../../../src/main/resources/templates/admin/order-detail.html#L235) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- Public API operational access là thiếu auth; UI customer/admin access có ROLE gates ở page level.
- Description không có trong response dù entity có field.
- UI: SSR returnService.getByOrder presents statuses/reason on same detail; no cancel/withdraw return UI.
- UI: Displays first return only (returns[0]), hidden card if none.

<a id="uc075"></a>

## UC075 — Duyệt yêu cầu trả hàng

**Module:** Yêu cầu trả hàng  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /admin/orders/{id}  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Cập nhật trạng thái lưu trong yêu cầu trả hàng sang APPROVED.

**Preconditions**

- API công khai; ReturnRequest tồn tại; employeeId request/default 1. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Gửi PUT /api/returns/{id}/status?status=APPROVED.
2. Tìm request và gán trạng thái; không kiểm predecessor.
3. Tìm employeeId; nếu không có, lưu processedBy=null.
4. Ghi processedAt now và lưu request.

**Alternative / Exception Flows**

- Không request: generic Exception 500.
- Không employee: vẫn cập nhật, không lỗi.
- Guest có thể gọi endpoint; không kiểm employee identity, StoreScope hoặc owner.
- Có thể cập nhật từ trạng thái nào sang trạng thái nào.

**Postconditions**

- Request status=APPROVED, processedAt cập nhật; không phát sinh hoàn kho/hoàn tiền.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L47) | `ReturnRestController` · `updateStatus` | `PUT /api/returns/{id}/status?status=APPROVED&employeeId=...` |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:90](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L90) | `ReturnServiceImpl` · `updateStatus` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/templates/admin/order-detail.html:253](../../../../src/main/resources/templates/admin/order-detail.html#L253) | `UI` | `—` |
| [src/main/resources/templates/admin/order-detail.html:285](../../../../src/main/resources/templates/admin/order-detail.html#L285) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- UI admin chỉ có APPROVED/REJECTED khi REQUESTED; PROCESSING/COMPLETED là backend-only.
- Actor UI intended là employee, access enforcement actual là PUBLIC.
- UI: Buttons only for REQUESTED first return; no complete/restock button.

<a id="uc076"></a>

## UC076 — Từ chối yêu cầu trả hàng

**Module:** Yêu cầu trả hàng  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** /admin/orders/{id}  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Cập nhật trạng thái lưu trong yêu cầu trả hàng sang REJECTED.

**Preconditions**

- API công khai; ReturnRequest tồn tại; employeeId request/default 1. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Gửi PUT /api/returns/{id}/status?status=REJECTED.
2. Tìm request và gán trạng thái; không kiểm predecessor.
3. Tìm employeeId; nếu không có, lưu processedBy=null.
4. Ghi processedAt now và lưu request.

**Alternative / Exception Flows**

- Không request: generic Exception 500.
- Không employee: vẫn cập nhật, không lỗi.
- Guest có thể gọi endpoint; không kiểm employee identity, StoreScope hoặc owner.
- Có thể cập nhật từ trạng thái nào sang trạng thái nào.

**Postconditions**

- Request status=REJECTED, processedAt cập nhật; không phát sinh hoàn kho/hoàn tiền.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L47) | `ReturnRestController` · `updateStatus` | `PUT /api/returns/{id}/status?status=REJECTED&employeeId=...` |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:90](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L90) | `ReturnServiceImpl` · `updateStatus` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |
| [src/main/resources/templates/admin/order-detail.html:253](../../../../src/main/resources/templates/admin/order-detail.html#L253) | `UI` | `—` |
| [src/main/resources/templates/admin/order-detail.html:285](../../../../src/main/resources/templates/admin/order-detail.html#L285) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- UI admin chỉ có APPROVED/REJECTED khi REQUESTED; PROCESSING/COMPLETED là backend-only.
- Actor UI intended là employee, access enforcement actual là PUBLIC.
- UI: Buttons only for REQUESTED first return; no complete/restock button.

<a id="uc077"></a>

## UC077 — Ghi nhận đang xử lý trả hàng

**Module:** Yêu cầu trả hàng  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Cập nhật trạng thái lưu trong yêu cầu trả hàng sang PROCESSING.

**Preconditions**

- API công khai; ReturnRequest tồn tại; employeeId request/default 1. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Gửi PUT /api/returns/{id}/status?status=PROCESSING.
2. Tìm request và gán trạng thái; không kiểm predecessor.
3. Tìm employeeId; nếu không có, lưu processedBy=null.
4. Ghi processedAt now và lưu request.

**Alternative / Exception Flows**

- Không request: generic Exception 500.
- Không employee: vẫn cập nhật, không lỗi.
- Guest có thể gọi endpoint; không kiểm employee identity, StoreScope hoặc owner.
- Có thể cập nhật từ trạng thái nào sang trạng thái nào.

**Postconditions**

- Request status=PROCESSING, processedAt cập nhật; không phát sinh hoàn kho/hoàn tiền.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L47) | `ReturnRestController` · `updateStatus` | `PUT /api/returns/{id}/status?status=PROCESSING&employeeId=...` |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:90](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L90) | `ReturnServiceImpl` · `updateStatus` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |

**Giới hạn và đối chiếu UI**

- UI admin chỉ có APPROVED/REJECTED khi REQUESTED; PROCESSING/COMPLETED là backend-only.
- Actor UI intended là employee, access enforcement actual là PUBLIC.

<a id="uc078"></a>

## UC078 — Ghi nhận hoàn tất trả hàng

**Module:** Yêu cầu trả hàng  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Cập nhật trạng thái lưu trong yêu cầu trả hàng sang COMPLETED.

**Preconditions**

- API công khai; ReturnRequest tồn tại; employeeId request/default 1. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Gửi PUT /api/returns/{id}/status?status=COMPLETED.
2. Tìm request và gán trạng thái; không kiểm predecessor.
3. Tìm employeeId; nếu không có, lưu processedBy=null.
4. Ghi processedAt now và lưu request.

**Alternative / Exception Flows**

- Không request: generic Exception 500.
- Không employee: vẫn cập nhật, không lỗi.
- Guest có thể gọi endpoint; không kiểm employee identity, StoreScope hoặc owner.
- Có thể cập nhật từ trạng thái nào sang trạng thái nào.

**Postconditions**

- Request status=COMPLETED, processedAt cập nhật; không phát sinh hoàn kho/hoàn tiền.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java:47](../../../../src/main/java/com/thinh/cosmetic/rest/returns/ReturnRestController.java#L47) | `ReturnRestController` · `updateStatus` | `PUT /api/returns/{id}/status?status=COMPLETED&employeeId=...` |
| [src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java:90](../../../../src/main/java/com/thinh/cosmetic/service/returns/impl/ReturnServiceImpl.java#L90) | `ReturnServiceImpl` · `updateStatus` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |

**Giới hạn và đối chiếu UI**

- UI admin chỉ có APPROVED/REJECTED khi REQUESTED; PROCESSING/COMPLETED là backend-only.
- Actor UI intended là employee, access enforcement actual là PUBLIC.

<a id="uc079"></a>

## UC079 — Xem đánh giá sản phẩm

**Module:** Đánh giá sản phẩm  
**Implementation Status:** Backend Only  
**Primary Actor:** Khách vãng lai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Tra cứu các đánh giá sản phẩm có moderationStatus VISIBLE.

**Preconditions**

- Product ID được chỉ định; API công khai.

**Main Success Scenario**

1. Gọi GET /api/reviews/product/{productId}.
2. Repository tìm reviews theo productId.
3. Service lọc VISIBLE.
4. Trả rating/comment/customerName/productName.

**Alternative / Exception Flows**

- Không có review hoặc chỉ HIDDEN/PENDING: danh sách rỗng.
- Không kiểm product tồn tại riêng.

**Postconditions**

- Visible reviews được đọc.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java:30](../../../../src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java#L30) | `ReviewRestController` · `getByProduct` | `GET /api/reviews/product/{productId}` |
| [src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java:44](../../../../src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java#L44) | `ReviewServiceImpl` · `getByProduct` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |

**Giới hạn và đối chiếu UI**

- Frontend tab review tĩnh, không gọi API => Backend Only sau merge UI.

<a id="uc080"></a>

## UC080 — Viết đánh giá sản phẩm

**Module:** Đánh giá sản phẩm  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Lưu rating và nội dung review gắn product/customer.

**Preconditions**

- API công khai; customerId query/default 1 và product tồn tại; DTO orderId bắt buộc, rating 1..5. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. @Valid kiểm productId/orderId/rating.
2. Tìm product và customer theo IDs.
3. Tìm orderId; nếu không có thì order=null và tiếp tục.
4. Build review với rating/comment và status VISIBLE, không set orderItem.
5. Lưu và trả DTO.

**Alternative / Exception Flows**

- Product/customer không có: generic Exception 500.
- orderId không có: vẫn tạo review với order=null.
- Không kiểm customer sở hữu order, order COMPLETED hay product nằm trong order; không chống duplicate.
- Review xuất hiện VISIBLE ngay dù entity default là PENDING.

**Postconditions**

- Review được lưu VISIBLE nếu product/customer/validation hợp lệ.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java:22](../../../../src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java#L22) | `ReviewRestController` · `create` | `POST /api/reviews?customerId=...` |
| [src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java:28](../../../../src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java#L28) | `ReviewServiceImpl` · `create` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |

**Giới hạn và đối chiếu UI**

- Không có UI viết review; actor actual PUBLIC, customerId không được lấy từ AuthPrincipal.

<a id="uc081"></a>

## UC081 — Kiểm tra danh sách đánh giá

**Module:** Đánh giá sản phẩm  
**Implementation Status:** Backend Only  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Đọc toàn bộ review, kể cả hidden/pending.

**Preconditions**

- API công khai.

**Main Success Scenario**

1. Gọi GET /api/reviews.
2. Đọc findAll.
3. Map tất cả DTO, không lọc status.

**Alternative / Exception Flows**

- Không reviews: danh sách rỗng.

**Postconditions**

- Toàn bộ reviews được trả.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java:35](../../../../src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java#L35) | `ReviewRestController` · `getAll` | `GET /api/reviews` |
| [src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java:52](../../../../src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java#L52) | `ReviewServiceImpl` · `getAll` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |

**Giới hạn và đối chiếu UI**

- Không có UI quản trị review hoặc kiểm quyền REVIEW_MODERATE.

<a id="uc082"></a>

## UC082 — Ẩn hoặc hiển thị đánh giá

**Module:** Đánh giá sản phẩm  
**Implementation Status:** Partially Implemented  
**Primary Actor:** Người gọi API công khai  
**Supporting Actor:** Không có.  
**Association API public:** Người gọi API công khai  
**UI:** Chưa có.  
**Quyền:** API permitAll; không yêu cầu xác thực.

**Mô tả nghiệp vụ:** Thay đổi moderationStatus của review sang VISIBLE/HIDDEN/PENDING.

**Preconditions**

- API công khai; reviewId tồn tại; ReviewStatus enum hợp lệ. REST mutation cần Bearer header hoặc X-Requested-With: fetch/XMLHttpRequest.

**Main Success Scenario**

1. Gửi PUT /api/reviews/{id}/status?status=....
2. Tìm review theo ID.
3. Gán status không kiểm trạng thái trước.
4. Lưu và trả DTO; VISIBLE mới được public product list trả.

**Alternative / Exception Flows**

- Không có review: generic Exception 500.
- Guest hoặc customer đều có thể update vì permitAll.
- ReviewStatus enum không hợp lệ: framework reject request.

**Postconditions**

- Status review thay đổi và ảnh hưởng product visible list.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java:40](../../../../src/main/java/com/thinh/cosmetic/rest/review/ReviewRestController.java#L40) | `ReviewRestController` · `updateModerationStatus` | `PUT /api/reviews/{id}/status?status=...` |
| [src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java:57](../../../../src/main/java/com/thinh/cosmetic/service/review/impl/ReviewServiceImpl.java#L57) | `ReviewServiceImpl` · `updateModerationStatus` | `—` |
| [src/main/java/com/thinh/cosmetic/config/SecurityConfig.java:85](../../../../src/main/java/com/thinh/cosmetic/config/SecurityConfig.java#L85) | `SecurityConfig` · `securityFilterChain` | `—` |

**Giới hạn và đối chiếu UI**

- Không có UI moderation; không ghi moderator/audit timestamp.

<a id="uc083"></a>

## UC083 — Xem tổng quan vận hành

**Module:** Tổng quan vận hành  
**Implementation Status:** Implemented  
**Primary Actor:** Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** /admin  
**Quyền:** ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Mô tả nghiệp vụ:** Tổng hợp KPI từ danh sách đơn thực và cảnh báo tồn thấp; tính toán trong trình duyệt.

**Preconditions**

- ROLE_EMPLOYEE; đơn được lọc theo StoreScope ở API /api/admin/orders.

**Main Success Scenario**

1. Mở /admin; page controller tải cửa hàng theo phân công.
2. JS lấy danh sách đơn qua API admin và cộng totalAmount của đơn COMPLETED.
3. Đếm PENDING, PREPARING/SHIPPING, lấy sáu đơn gần nhất.
4. Lấy tồn thấp riêng cho một cửa hàng; hiển thị KPI và bảng.

**Alternative / Exception Flows**

- ADMIN có thể tổng hợp đơn toàn chuỗi; tồn thấp vẫn dùng một cửa hàng đầu.
- Không có đơn: KPI bằng 0; không có báo cáo theo ngày, biểu đồ chuỗi thời gian hoặc xuất báo cáo.

**Postconditions**

- Dashboard hiển thị dữ liệu và phép tổng hợp; không đổi DB.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/java/com/thinh/cosmetic/controller/AdminPageController.java:55](../../../../src/main/java/com/thinh/cosmetic/controller/AdminPageController.java#L55) | `AdminPageController` · `dashboard` | `GET /admin` |
| [src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java:22](../../../../src/main/java/com/thinh/cosmetic/rest/order/AdminOrderRestController.java#L22) | `AdminOrderRestController` · `getOrders` | `GET /api/admin/orders` |
| [src/main/resources/templates/admin/dashboard.html:200](../../../../src/main/resources/templates/admin/dashboard.html#L200) | `UI` | `—` |
| [src/main/resources/templates/admin/dashboard.html:220](../../../../src/main/resources/templates/admin/dashboard.html#L220) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- UI: KPI computed in browser from live order list: sum totalAmount of COMPLETED, pending, preparing+shipping, recent first six orders. No date filter/time series/top-selling. Low stock fetched separately.

<a id="uc084"></a>

## UC084 — Đổi giao diện sáng tối

**Module:** Tùy chọn giao diện  
**Implementation Status:** UI Only  
**Primary Actor:** Khách vãng lai, Khách hàng, Nhân viên  
**Supporting Actor:** Không có.  
**Association API public:** Không có.  
**UI:** all pages  
**Quyền:** ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.

**Mô tả nghiệp vụ:** Chọn dark/light và nhớ lựa chọn trong localStorage của trình duyệt; chức năng client hoàn chỉnh, không cần backend.

**Preconditions**

- Trình duyệt tải api.js và nút đổi theme.

**Main Success Scenario**

1. Người dùng nhấn nút đổi giao diện.
2. JS đổi data-theme và cập nhật nhãn nút.
3. Ghi lunea-theme vào localStorage; lần tải trang sau đọc lựa chọn.

**Alternative / Exception Flows**

- localStorage không khả dụng: code bỏ qua lỗi lưu, thay theme phiên hiện tại vẫn hoạt động.

**Postconditions**

- Giao diện thay đổi trên client; không lưu server.

**Nguồn source / class / method / endpoint**

| Nguồn | Class / method hoặc field | Endpoint / route |
|---|---|---|
| [src/main/resources/static/js/api.js:288](../../../../src/main/resources/static/js/api.js#L288) | `toggleTheme` | `—` |
| [src/main/resources/static/js/api.js:269](../../../../src/main/resources/static/js/api.js#L269) | `UI` | `—` |
| [src/main/resources/templates/fragments/header.html:78](../../../../src/main/resources/templates/fragments/header.html#L78) | `UI` | `—` |

**Giới hạn và đối chiếu UI**

- UI Only ở đây mô tả nơi thực hiện; không là placeholder.
- UI: localStorage lunea-theme + DOM data-theme. UI convenience, exclude main business use cases.
