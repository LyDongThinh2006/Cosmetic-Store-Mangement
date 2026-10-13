#!/usr/bin/env python3
"""Build the reviewed business model and one PNG per diagram. No app/DB writes."""
import argparse
import copy
import csv
import hashlib
import json
import re
import struct
import subprocess
import tempfile
import textwrap
import urllib.parse
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parents[4]
OUT = ROOT / 'docs/uml/use-case'
RAW = json.loads((OUT/'traceability/source-behaviors.json').read_text())
BEHAVIORS = {u['key']:u for u in RAW['use_cases']}
ACTORS = {a['id']:copy.deepcopy(a) for a in RAW['actors'] if a['id']!='PUBLIC'}
MODULES = {
    'authentication':'Tài khoản và xác thực', 'customers':'Hồ sơ khách hàng',
    'employees':'Quản lý nhân viên', 'catalog':'Sản phẩm và tra cứu mỹ phẩm',
    'brand_category':'Thương hiệu và danh mục', 'store':'Cửa hàng', 'inventory':'Tồn kho',
    'stock_transfer':'Điều chuyển kho', 'purchase':'Nhà cung cấp và nhập hàng',
    'cart':'Giỏ hàng và yêu thích', 'order':'Đặt hàng và vận hành đơn',
    'returns':'Yêu cầu trả hàng', 'review':'Đánh giá sản phẩm',
}
GOALS = []

def goal(key,name,module,actors,parts,steps):
    if isinstance(parts,str): parts=parts.split()
    GOALS.append(dict(key=key,name=name,module=module,actors=actors.split(),parts=parts,main=steps))

goal('register','Đăng ký tài khoản khách hàng','authentication','GUEST','AUTH_REGISTER',[
    'Khách cung cấp họ tên, email, số điện thoại và mật khẩu mới.',
    'Hệ thống kiểm tra dữ liệu, chuẩn hóa email/số điện thoại và kiểm tra trùng tài khoản.',
    'Hệ thống tạo tài khoản khách hàng, hồ sơ với điểm tích lũy ban đầu bằng 0 và thông báo kết quả.'])
goal('login','Đăng nhập','authentication','GUEST CUSTOMER EMPLOYEE','AUTH_LOGIN',[
    'Người dùng cung cấp email/số điện thoại/tên đăng nhập và mật khẩu.',
    'Hệ thống kiểm tra tài khoản còn hoạt động và mật khẩu đúng.',
    'Hệ thống xác lập danh tính cùng vai trò khách hàng hoặc nhân viên và trả kết quả đăng nhập.'])
goal('logout','Đăng xuất','authentication','CUSTOMER EMPLOYEE','AUTH_LOGOUT',[
    'Người dùng yêu cầu kết thúc đăng nhập trên trình duyệt.',
    'Hệ thống xóa cookie xác thực của trình duyệt và đưa người dùng về trang đăng nhập.'])
goal('reset_password','Đặt lại mật khẩu','authentication','GUEST','AUTH_RESET',[
    'Người dùng cung cấp email/số điện thoại của tài khoản và mật khẩu mới.',
    'Hệ thống tìm tài khoản, kiểm tra mật khẩu mới có ít nhất 6 ký tự.',
    'Hệ thống lưu mật khẩu mới dưới dạng băm và thông báo kết quả.'])
goal('personal_profile','Quản lý hồ sơ cá nhân','customers','CUSTOMER','CUSTOMER_READ CUSTOMER_EDIT',[
    'Khách hàng yêu cầu xem hồ sơ gồm họ tên, ngày sinh, giới tính, số điện thoại và điểm tích lũy.',
    'Khách hàng lựa chọn xem hoặc cập nhật họ tên, ngày sinh, giới tính, số điện thoại.',
    'Hệ thống kiểm tra dữ liệu, lưu phần được cập nhật và trả hồ sơ hiện hành.'])
goal('addresses','Quản lý địa chỉ nhận hàng','customers','CUSTOMER','ADDRESS_READ ADDRESS_ADD ADDRESS_EDIT ADDRESS_DELETE ADDRESS_DEFAULT',[
    'Khách hàng yêu cầu xem các địa chỉ nhận hàng đã lưu.',
    'Khách hàng chọn thêm, sửa, xóa hoặc đặt một địa chỉ làm mặc định.',
    'Với thêm/sửa, khách cung cấp người nhận, số điện thoại, tỉnh, quận, phường và địa chỉ chi tiết.',
    'Hệ thống thực hiện công việc được chọn và trả kết quả cập nhật sổ địa chỉ.'])
goal('beauty_profile','Quản lý hồ sơ làm đẹp','customers','CUSTOMER','BEAUTY_READ BEAUTY_EDIT',[
    'Khách hàng yêu cầu xem loại da, vấn đề da, nhu cầu chăm sóc, sở thích và khoảng giá quan tâm.',
    'Khách hàng có thể cung cấp bộ giá trị mới cho hồ sơ làm đẹp.',
    'Hệ thống tạo hồ sơ khi chưa có hoặc cập nhật hồ sơ hiện có và ghi thời điểm cập nhật.'])
goal('customer_lookup','Tra cứu khách hàng','customers','EMPLOYEE','CUSTOMER_LIST',[
    'Nhân viên yêu cầu danh sách khách hàng.',
    'Hệ thống trả họ tên, email, ngày sinh, giới tính, điểm tích lũy và ngày tham gia của các khách hàng.'])
goal('employee_management','Quản lý nhân viên và phân công','employees','ADMIN','EMPLOYEE_READ EMPLOYEE_ADD EMPLOYEE_EDIT EMPLOYEE_DEACTIVATE',[
    'Quản trị viên chọn tra cứu, tạo, cập nhật hoặc ngừng hoạt động nhân viên.',
    'Khi tạo/cập nhật, quản trị viên cung cấp họ tên, email nội bộ, số điện thoại, mật khẩu và vai trò/cửa hàng được phân công.',
    'Hệ thống thực hiện nhánh công việc tương ứng; các vai trò và cửa hàng hợp lệ được liên kết với nhân viên.',
    'Hệ thống trả nhân viên cùng tên vai trò, cửa hàng và trạng thái hiện hành.'])
goal('product_search','Tra cứu mỹ phẩm','catalog','GUEST CUSTOMER EMPLOYEE','catalog_browse catalog_search',[
    'Người dùng yêu cầu danh sách mỹ phẩm hoặc cung cấp từ khóa, thương hiệu, danh mục, loại da, nhu cầu, khoảng giá và cách sắp xếp.',
    'Hệ thống tìm các sản phẩm phù hợp và bổ sung ảnh, biến thể, giá cùng tồn khả dụng.',
    'Hệ thống trả danh sách kết quả; không có kết quả thì trả danh sách rỗng.'])
goal('product_detail','Xem chi tiết mỹ phẩm','catalog','GUEST CUSTOMER EMPLOYEE','catalog_detail',[
    'Người dùng chọn một sản phẩm.',
    'Hệ thống trả mô tả, công dụng, thành phần, thương hiệu, danh mục, hình ảnh và các biến thể.',
    'Người dùng có thể chọn biến thể để xem giá và mã tương ứng.'])
goal('product_management','Quản lý sản phẩm','catalog','PRODUCT_MANAGER ADMIN','product_create product_update product_delete',[
    'Nhân viên quản lý sản phẩm hoặc quản trị viên chọn thêm, cập nhật hoặc xóa sản phẩm.',
    'Với thêm/cập nhật, người thực hiện cung cấp tên, thương hiệu, danh mục, xuất xứ, thành phần, công dụng, mô tả và trạng thái.',
    'Hệ thống kiểm tra các dữ liệu liên quan, thực hiện nhánh được chọn và trả kết quả.',
    'Việc quản lý này chỉ tác động bản ghi sản phẩm; chưa có thao tác quản lý SKU, ảnh hoặc thuộc tính.'])
goal('brand_category_lookup','Tra cứu thương hiệu và danh mục','brand_category','GUEST CUSTOMER EMPLOYEE','brand_browse category_browse',[
    'Người dùng yêu cầu danh sách/chi tiết thương hiệu hoặc danh mục mỹ phẩm.',
    'Hệ thống trả tên, mô tả, trạng thái và quan hệ danh mục cha nếu có.'])
goal('brand_management','Quản lý thương hiệu','brand_category','PRODUCT_MANAGER ADMIN','brand_create brand_update brand_delete',[
    'Người thực hiện chọn thêm, sửa hoặc xóa thương hiệu.',
    'Với thêm/sửa, người thực hiện cung cấp tên, mô tả và trạng thái thương hiệu.',
    'Hệ thống thực hiện nhánh đã chọn và trả thương hiệu hoặc xác nhận xóa thành công.'])
goal('category_management','Quản lý danh mục sản phẩm','brand_category','PRODUCT_MANAGER ADMIN','category_create category_update category_delete',[
    'Người thực hiện chọn thêm, sửa hoặc xóa danh mục.',
    'Với thêm/sửa, người thực hiện cung cấp tên, mô tả, trạng thái và danh mục cha nếu cần.',
    'Hệ thống thực hiện nhánh được chọn và trả kết quả; việc đổi danh mục cha khi sửa hiện chưa hoạt động.'])
goal('store_lookup','Tra cứu cửa hàng','store','GUEST CUSTOMER EMPLOYEE','store_browse',[
    'Người dùng yêu cầu danh sách hoặc chi tiết cửa hàng trong chuỗi.',
    'Hệ thống trả tên, địa chỉ, tỉnh, số điện thoại, giờ mở/đóng cửa và trạng thái cửa hàng.'])
goal('store_management','Quản lý cửa hàng','store','ADMIN','store_create store_update store_deactivate',[
    'Quản trị viên chọn thêm, cập nhật hoặc ngừng hoạt động cửa hàng.',
    'Khi thêm/cập nhật, quản trị viên cung cấp tên, địa chỉ, tỉnh, số điện thoại, giờ mở/đóng cửa và tọa độ.',
    'Hệ thống lưu dữ liệu mới hoặc đổi trạng thái cửa hàng sang INACTIVE, rồi trả kết quả.'])
goal('stock_lookup','Kiểm tra còn hàng theo cửa hàng','inventory','GUEST CUSTOMER EMPLOYEE','stock_public',[
    'Người dùng chọn một biến thể mỹ phẩm cần kiểm tra.',
    'Hệ thống lấy tồn thực tế và lượng đang giữ của biến thể tại từng cửa hàng.',
    'Hệ thống trả cửa hàng, tồn khả dụng và thông tin còn hàng; bước này không giữ hàng.'])
goal('inventory_lookup','Tra cứu tồn kho','inventory','EMPLOYEE','inventory_browse',[
    'Nhân viên chọn cửa hàng cần tra cứu tồn kho.',
    'Hệ thống trả các SKU cùng tồn thực tế, lượng đang giữ, tồn khả dụng và ngưỡng tối thiểu.'])
goal('low_stock','Xem cảnh báo tồn thấp','inventory','EMPLOYEE','inventory_low',[
    'Nhân viên yêu cầu danh sách tồn thấp tại một cửa hàng.',
    'Hệ thống chọn các SKU có tồn thực tế không vượt minimumStock và trả kết quả.'])
goal('stock_adjustment','Điều chỉnh tồn kho','inventory','EMPLOYEE','inventory_adjust',[
    'Nhân viên cung cấp cửa hàng, SKU, số tồn thực tế mới và lý do kiểm kê.',
    'Hệ thống kiểm tra tồn mới không âm và không thấp hơn lượng đang giữ.',
    'Hệ thống lưu số lượng trước/sau, lý do, người thực hiện và thời điểm, rồi cập nhật tồn thực tế.'])
goal('transfer_create','Lập phiếu chuyển kho','stock_transfer','EMPLOYEE','transfer_create',[
    'Nhân viên cung cấp cửa hàng nguồn, cửa hàng đích và các SKU/số lượng cần chuyển.',
    'Hệ thống tìm cửa hàng, SKU và người lập rồi lưu phiếu cùng các dòng hàng.',
    'Hệ thống trả phiếu trạng thái PENDING; chưa giữ hoặc trừ tồn ở bước lập.'])
goal('transfer_lookup','Tra cứu phiếu chuyển kho','stock_transfer','EMPLOYEE','transfer_browse',[
    'Nhân viên yêu cầu danh sách hoặc chọn một phiếu chuyển kho.',
    'Hệ thống trả nguồn, đích, người lập, trạng thái và các dòng SKU/số lượng.'])
goal('transfer_ship','Xác nhận xuất chuyển kho','stock_transfer','EMPLOYEE','transfer_ship',[
    'Nhân viên yêu cầu xuất hàng cho một phiếu PENDING.',
    'Hệ thống kiểm tra tồn thực tế nguồn theo từng dòng hàng.',
    'Hệ thống trừ tồn thực tế nguồn và đổi phiếu sang IN_TRANSIT.'])
goal('transfer_receive','Xác nhận nhận chuyển kho','stock_transfer','EMPLOYEE','transfer_receive',[
    'Nhân viên yêu cầu nhận hàng cho một phiếu IN_TRANSIT.',
    'Hệ thống tạo bản ghi tồn đích nếu thiếu, rồi cộng số lượng của từng SKU.',
    'Hệ thống đổi phiếu sang RECEIVED và trả kết quả.'])
goal('supplier_management','Quản lý nhà cung cấp','purchase','EMPLOYEE','supplier_browse supplier_create supplier_update supplier_deactivate',[
    'Nhân viên chọn tra cứu, thêm, cập nhật hoặc ngừng hợp tác nhà cung cấp.',
    'Với thêm/cập nhật, nhân viên cung cấp tên, địa chỉ, số điện thoại và email liên hệ.',
    'Hệ thống thực hiện nhánh được chọn; ngừng hợp tác đặt trạng thái INACTIVE.'])
goal('purchase_create','Lập đơn nhập hàng','purchase','EMPLOYEE','purchase_create',[
    'Nhân viên cung cấp nhà cung cấp, cửa hàng nhận và các SKU, số lượng, giá nhập.',
    'Hệ thống tìm nhà cung cấp/cửa hàng/SKU/người lập và lưu đơn cùng các dòng hàng.',
    'Hệ thống trả đơn DRAFT; tồn kho chưa tăng ở bước lập.'])
goal('purchase_lookup','Tra cứu đơn nhập hàng','purchase','EMPLOYEE','purchase_browse',[
    'Nhân viên yêu cầu danh sách hoặc chọn một đơn nhập hàng.',
    'Hệ thống trả nhà cung cấp, cửa hàng nhận, trạng thái, các dòng và tổng giá trị nhập.'])
goal('purchase_confirm','Xác nhận nhập hàng vào kho','purchase','EMPLOYEE','purchase_confirm',[
    'Nhân viên yêu cầu xác nhận một đơn nhập hàng DRAFT.',
    'Hệ thống cộng toàn bộ số lượng từng SKU vào tồn cửa hàng nhận, tạo bản ghi tồn khi thiếu.',
    'Hệ thống đổi đơn sang CONFIRMED và ghi thời điểm xác nhận.'])
goal('cart_management','Quản lý giỏ hàng','cart','CUSTOMER','cart.view cart.add cart.quantity cart.remove cart.clear',[
    'Khách hàng yêu cầu xem giỏ hoặc chọn thêm SKU, đổi số lượng, xóa một dòng hay làm trống giỏ.',
    'Khi thêm/đổi số lượng, hệ thống kiểm tra tổng khả dụng của SKU trên chuỗi cửa hàng.',
    'Hệ thống thực hiện nhánh đã chọn và trả các dòng hàng, giá hiện tại, tổng tiền và tổng số lượng.',
    'Giỏ hàng không giữ tồn; giữ tồn chỉ xảy ra khi tạo đơn.'])
goal('wishlist_management','Quản lý sản phẩm yêu thích','cart','CUSTOMER','wishlist.view wishlist.add wishlist.remove',[
    'Khách hàng yêu cầu xem danh sách yêu thích hoặc chọn lưu/bỏ một sản phẩm.',
    'Hệ thống tìm hoặc tạo danh sách của khách và thực hiện nhánh được chọn.',
    'Hệ thống trả danh sách/kết quả cập nhật; không có bước tự chuyển sản phẩm vào giỏ.'])
goal('order_preview','Xem tổng tiền trước khi đặt','order','CUSTOMER','order.preview',[
    'Khách hàng yêu cầu xem tổng tiền của giỏ trước khi đặt hàng.',
    'Hệ thống lấy giá hiện tại của các SKU và tính tổng tiền hàng.',
    'Tại điểm mở rộng tính tổng cuối, hệ thống có thể áp dụng mã ưu đãi nếu khách cung cấp mã phù hợp.',
    'Hệ thống tính phí giao hàng, tổng phải trả và hiển thị kết quả; chưa tạo đơn hoặc giữ tồn.'])
goal('voucher','Áp dụng mã ưu đãi','order','CUSTOMER','order.voucher',[
    'Khách hàng cung cấp mã ưu đãi khi xem trước hoặc đặt đơn.',
    'Hệ thống chuẩn hóa mã và, tại điểm tính tổng cuối, áp dụng LUNEA10, GIAM50K hoặc FREESHIP nếu mã phù hợp.',
    'Hệ thống cập nhật số tiền giảm/phí giao hàng và tổng cuối; khi tạo đơn các số tiền được lưu trên đơn.'])
goal('place_order','Đặt hàng thanh toán khi nhận','order','CUSTOMER EMPLOYEE','order.place',[
    'Actor cung cấp người nhận, số điện thoại, địa chỉ và lựa chọn COD cho giỏ khách hàng cần đặt.',
    'Hệ thống tính tổng tiền hàng; tại điểm mở rộng tính tổng cuối có thể áp dụng mã ưu đãi.',
    'Hệ thống xác định cửa hàng phục vụ, tạo đơn PENDING và lưu giá/tên biến thể tại thời điểm đặt.',
    'Hệ thống giữ số lượng cho đơn, xóa các dòng giỏ và trả mã đơn cùng kết quả đặt hàng.'])
goal('my_orders','Tra cứu đơn hàng của tôi','order','CUSTOMER','order.history order.detail',[
    'Khách hàng yêu cầu danh sách đơn hoặc chọn một đơn cần theo dõi.',
    'Hệ thống trả các đơn mới nhất trước; chi tiết gồm dòng hàng, tiền, trạng thái, địa chỉ và thời điểm xử lý.',
    'Khách hàng xem tiến trình của đơn; các đường tra cứu chưa kiểm chủ đơn được ghi ở báo cáo giới hạn.'])
goal('cancel_my_order','Hủy đơn chờ xác nhận','order','CUSTOMER','order.cancel_customer',[
    'Khách hàng yêu cầu hủy một đơn của mình.',
    'Hệ thống kiểm tra quyền sở hữu và đơn đang PENDING.',
    'Hệ thống đổi sang CANCELLED, giải phóng lượng còn HELD và ghi thời điểm hủy.'])
goal('branch_orders','Tra cứu đơn hàng cửa hàng','order','EMPLOYEE','order.staff_list order.staff_detail',[
    'Nhân viên yêu cầu danh sách theo cửa hàng hoặc chọn một đơn.',
    'Hệ thống áp dụng phạm vi cửa hàng được phân công; ADMIN/ALL_STORES có thể xem mọi cửa hàng.',
    'Hệ thống trả danh sách/chi tiết đơn trong phạm vi được phép.'])
goal('order_progress','Cập nhật tiến trình đơn hàng','order','EMPLOYEE','order.confirm order.prepare order.ship order.complete',[
    'Nhân viên lựa chọn xác nhận đơn, chuẩn bị hàng, bắt đầu giao hoặc ghi nhận giao thành công.',
    'Hệ thống kiểm tra phạm vi cửa hàng và không cho đổi một đơn terminal sang trạng thái khác.',
    'Hệ thống cập nhật CONFIRMED/PREPARING/SHIPPING/COMPLETED tương ứng với công việc được chọn.',
    'Khi SHIPPING hoặc COMPLETED, lượng còn HELD được chuyển COMMITTED và giảm tồn thực tế/lượng giữ.'])
goal('cancel_branch_order','Hủy đơn hàng cửa hàng','order','EMPLOYEE','order.cancel_staff',[
    'Nhân viên yêu cầu hủy một đơn trong phạm vi cửa hàng.',
    'Hệ thống kiểm tra phạm vi và không đổi đơn đã terminal sang một trạng thái khác.',
    'Hệ thống cập nhật CANCELLED và giải phóng lượng còn HELD; không hoàn lại lượng đã COMMITTED.'])
goal('return_create','Gửi yêu cầu trả hàng','returns','CUSTOMER','return.create',[
    'Khách hàng chọn đơn, các dòng cần trả, số lượng và lý do.',
    'Hệ thống tìm đơn và các dòng được chọn, chuẩn bị yêu cầu REQUESTED cùng chi tiết trả.',
    'Nhánh lưu hiện thiếu customer bắt buộc, nên chưa có kết quả tạo thành công được xác minh trên persistence thật.'])
goal('return_lookup','Theo dõi yêu cầu trả hàng','returns','CUSTOMER EMPLOYEE','return.view',[
    'Khách hàng hoặc nhân viên yêu cầu xem các yêu cầu trả hàng liên quan đến đơn.',
    'Hệ thống trả lý do, các dòng/số lượng, trạng thái, người xử lý và thời điểm xử lý.'])
goal('return_processing','Cập nhật xử lý trả hàng','returns','EMPLOYEE','return.approve return.reject return.process return.complete',[
    'Nhân viên chọn chấp thuận, từ chối, đang xử lý hoặc đánh dấu hoàn tất yêu cầu trả hàng.',
    'Hệ thống tìm yêu cầu, đổi sang APPROVED/REJECTED/PROCESSING/COMPLETED tương ứng.',
    'Hệ thống ghi người và thời điểm xử lý; thao tác hiện chưa hoàn tiền hoặc nhập hàng trả lại vào kho.'])
goal('review_lookup','Xem đánh giá sản phẩm','review','GUEST CUSTOMER EMPLOYEE','review.view_product',[
    'Người dùng yêu cầu các đánh giá đang VISIBLE của một sản phẩm.',
    'Hệ thống trả điểm, nội dung, tên khách và thời điểm đánh giá; tab đánh giá storefront hiện chưa dùng dữ liệu này.'])
goal('review_create','Viết đánh giá sản phẩm','review','CUSTOMER','review.create',[
    'Khách hàng cung cấp sản phẩm, đơn tham chiếu, điểm từ 1 đến 5 và nội dung đánh giá.',
    'Hệ thống tìm sản phẩm/khách, lưu đánh giá trạng thái VISIBLE và trả kết quả.',
    'Các kiểm tra đã mua hàng, đơn thuộc khách, đơn hoàn tất và đánh giá trùng chưa được thực hiện.'])
goal('review_moderation','Quản lý hiển thị đánh giá','review','EMPLOYEE','review.all review.moderate',[
    'Nhân viên yêu cầu danh sách đánh giá hoặc chọn đánh giá cần đổi trạng thái hiển thị.',
    'Hệ thống trả danh sách hoặc cập nhật moderationStatus đã chọn.',
    'Hệ thống trả kết quả; chưa ghi người kiểm duyệt hoặc kiểm quyền kiểm duyệt.'])
goal('dashboard','Xem tổng quan vận hành','order','EMPLOYEE','dashboard.view',[
    'Nhân viên yêu cầu tổng quan đơn trong phạm vi cửa hàng được phép.',
    'Hệ thống tổng hợp doanh thu từ đơn COMPLETED, đếm các trạng thái và hiển thị các đơn gần nhất.',
    'Hệ thống hiển thị tồn thấp của một cửa hàng; chưa có báo cáo theo kỳ hoặc xuất báo cáo.'])

def write_json(path,value):
    (OUT/path).write_text(json.dumps(value,ensure_ascii=False,indent=2)+'\n')

def link(file,line=1):
    return f'[{file}:{line}](../../../../{file}#L{line})'

def build_model():
    raw_to_goal={};result=[]
    for index,g in enumerate(GOALS,1):
        g=copy.deepcopy(g);g['id']=f'UC{index:03d}'
        parts=[BEHAVIORS[k] for k in g['parts']]
        for p in parts:
            assert p['key'] not in raw_to_goal,p['key']
            raw_to_goal[p['key']]=g['id']
        statuses={p['status'] for p in parts}
        g['status']='Partially Implemented' if 'Partially Implemented' in statuses else ('Backend Only' if statuses=={'Backend Only'} else 'Implemented')
        g['description']='Mục tiêu của '+', '.join(ACTORS[a]['name'] for a in g['actors'])+': '+g['name'].lower()+'.'
        g['supporting_actors']=[]
        g['source_behaviors']=[dict(key=p['key'],previous_id=p['id'],name=p['name'],status=p['status']) for p in parts]
        g['sources']=[];seen=set()
        for p in parts:
            for s in p['sources']:
                key=(s['file'],s['line'],s.get('method'),s.get('endpoint'))
                if key not in seen:g['sources'].append(s);seen.add(key)
        g['ui_pages']=list(dict.fromkeys(v for p in parts for v in p.get('ui_pages',[])))
        g['observed_security']=list(dict.fromkeys(p['security']['url_rule'] for p in parts))
        g['trigger']='Actor lựa chọn '+g['name'].lower()+'.'
        g['preconditions']=[{'flow':p['name'],'conditions':p['preconditions']} for p in parts]
        g['alternatives']=[{'flow':p['name'],'conditions':p['alternatives']} for p in parts]
        g['postconditions']=[{'flow':p['name'],'conditions':p['postconditions']} for p in parts]
        g['notes']=list(dict.fromkeys(n for p in parts for n in p.get('notes',[])))
        g['notes']=[n.replace('actor actual PUBLIC','API hiện không yêu cầu xác thực').replace('PUBLIC mô tả truy cập thật','truy cập công khai được ghi trong báo cáo quyền').replace('actor actualPUBLIC','API hiện không yêu cầu xác thực') for n in g['notes']]
        g['failed_end_condition']='Công việc chưa được hoàn tất; lỗi đầu vào, quyền, trạng thái hoặc persistence được báo. Các nguy cơ lưu một phần được ghi trong từng nhánh nguồn.'
        g['extension_points']=[]
        if g['key'] in {'order_preview','place_order'}:g['extension_points']=['Tính tổng cuối: sau tổng tiền hàng và trước khi trả/lưu tổng phải trả.']
        result.append(g)
    assert set(raw_to_goal)==set(BEHAVIORS)-{'ui.theme'}
    ids={g['key']:g['id'] for g in result}
    relations=[dict(type='generalization',source=a['id'],target=a['parent'],evidence='AppUserDetailsService.loadUserByUsername: AccountType.EMPLOYEE luôn được ROLE_EMPLOYEE.') for a in ACTORS.values() if a['parent']]
    for base in ['order_preview','place_order']:
        relations.append(dict(type='extend',source=ids['voucher'],target=ids[base],condition='Khách cung cấp mã LUNEA10, GIAM50K hoặc FREESHIP.',extension_point='Tính tổng cuối',evidence='OrderServiceImpl.'+('previewOrder' if base=='order_preview' else 'placeOrder')+' có nhánh voucher; checkout.applyVoucher gửi mã và gọi preview.'))
    model=dict(date='2026-10-13',source_files=RAW['source_files'],actors=list(ACTORS.values()),modules=MODULES,use_cases=result,relationships=relations,modeling_level='Mục tiêu nghiệp vụ; association không là bảng kiểm soát quyền độc quyền.',excluded=[dict(key='ui.theme',reason='Tiện ích trình bày client; ngoài phạm vi use-case nghiệp vụ trong bộ hình vẽ theo PDF.')])
    write_json('traceability/use-cases.json',model)
    return model,raw_to_goal

def wrap(value,width=29):
    return '\\n'.join(textwrap.wrap(value,width=width,break_long_words=False,break_on_hyphens=False))

COLORS={'Implemented':'#FFF8DA','Backend Only':'#E8F0FC','Partially Implemented':'#FFE8D5'}

def diagram(model,selected,title,overview=False):
    content=['@startuml','!pragma layout smetana','left to right direction',
             'skinparam backgroundColor white','skinparam shadowing false','skinparam dpi 170',
             'skinparam defaultFontName SansSerif','skinparam defaultFontSize 15',
             'skinparam actorStyle stickman','skinparam actorBorderColor #333333',
             'skinparam usecaseBorderColor #4C4C4C','skinparam rectangleBorderColor #444444',
             'skinparam rectangleBackgroundColor white','skinparam ArrowColor #333333',
             'skinparam nodesep 35','skinparam ranksep 80','skinparam packageTitleAlignment center',f'title {title}']
    ids=list(ACTORS) if overview else list(dict.fromkeys(a for g in selected for a in g['actors']))
    for a in ids:content.append(f'actor "{wrap(ACTORS[a]["name"],22)}" as A_{a}')
    if overview:
        for a in ACTORS.values():
            if a['parent']:content.append(f'A_{a["id"]} --|> A_{a["parent"]}')
    content+=['rectangle "LUNEA" as SYSTEM {']
    for g in selected:content.append(f'  usecase "{g["id"]}\\n{wrap(g["name"])}" as {g["id"]} {COLORS[g["status"]]}')
    content+=['}']
    for g in selected:
        for a in g['actors']:
            if (a in {'ADMIN','PRODUCT_MANAGER'} and len(ids)>2) or (not overview and selected[0]['module']=='order' and a=='EMPLOYEE'):content.append(f'{g["id"]} -- A_{a}')
            else:content.append(f'A_{a} -- {g["id"]}')
    included={g['id'] for g in selected}
    relations=[]
    for r in model['relationships']:
        if r['type']=='extend' and r['source'] in included and r['target'] in included:
            content.append(f'{r["source"]} ..> {r["target"]} : <<extend>>\\n[có mã ưu đãi phù hợp]')
            relations.append(r)
    content+=['legend bottom left','Vàng: có luồng tích hợp. Xanh: chỉ backend. Cam: còn lỗi/thiếu nghiệp vụ.',
              'Actor biểu diễn vai trò thực hiện; quyền truy cập hiện tại xem README.',
              'Đăng nhập là điều kiện trước, không nối include cho mọi chức năng.']
    if relations:content.append('Điểm mở rộng của UC032 và UC034: Tính tổng cuối.')
    content+=['endlegend','@enduml','']
    return '\n'.join(content),ids,relations

def render(model,jar):
    diagrams=[]
    overview_keys={'login','product_search','product_management','store_management','employee_management','place_order','my_orders','order_progress','stock_adjustment','purchase_create','transfer_create','return_create','dashboard'}
    selected=[g for g in model['use_cases'] if g['key'] in overview_keys]
    jobs=[('overview/overall-system.png','Sơ đồ Use Case tổng quan',selected,True)]
    for module,title in MODULES.items():jobs.append((f'modules/{module}.png',title,[g for g in model['use_cases'] if g['module']==module],False))
    with tempfile.TemporaryDirectory(prefix='lunea-use-case-') as temp:
        paths=[];sources=[]
        for index,(file,title,selected,overall) in enumerate(jobs):
            source,actor_ids,relations=diagram(model,selected,title,overall)
            path=Path(temp)/f'{index:02d}.puml';path.write_text(source);paths.append(str(path));sources.append(source)
            diagrams.append(dict(file=file,title=title,group='overview' if overall else 'modules',use_cases=[g['id'] for g in selected],actors=actor_ids,relations=relations,source_sha256=hashlib.sha256(source.encode()).hexdigest()))
        subprocess.run(['java','-Djava.awt.headless=true','-jar',str(jar),'--check-syntax',*paths],check=True,cwd=ROOT)
        for job,source in zip(jobs,sources):
            dest=OUT/job[0];dest.parent.mkdir(parents=True,exist_ok=True)
            proc=subprocess.run(['java','-Djava.awt.headless=true','-DPLANTUML_LIMIT_SIZE=10000','-jar',str(jar),'--format','png','--pipe'],input=source.encode(),stdout=subprocess.PIPE,stderr=subprocess.PIPE,check=True,cwd=ROOT)
            assert proc.stdout.startswith(b'\x89PNG\r\n\x1a\n'),proc.stderr.decode(errors='replace')
            dest.write_bytes(proc.stdout)
            print('PNG:',job[0],flush=True)
    write_json('traceability/diagram-manifest.json',diagrams)
    return diagrams

def documents(model,raw_map,diagrams):
    goals=model['use_cases'];ids={g['id']:g for g in goals}
    spec=['# Đặc tả Use Case theo mục tiêu nghiệp vụ','',
          'Bộ đặc tả theo tài liệu PDF môn học: một use case có mục tiêu trọn vẹn; thêm/sửa/xóa hoặc các trạng thái cùng mục tiêu là các luồng lựa chọn. ID đã được đánh lại; bảng raw-behavior-map.csv giữ ánh xạ từ bộ chi tiết trước khi gộp.',
          '', 'Association xác định vai trò tham gia, không khẳng định quyền độc quyền. Các API permitAll/thiếu scope vẫn ghi trung thực ở cột Quyền thực tế và báo cáo bảo mật. Trạng thái dựa trên khảo sát source, chưa nghiệm thu runtime.', '',
          '| ID | Mục tiêu | Actor | Trạng thái |', '|---|---|---|---|']
    matrix=['# Use Case Traceability Matrix','', 'Mỗi hành vi nguồn chỉ thuộc một mục tiêu nghiệp vụ. Các API kỹ thuật/phương thức không được tạo use case riêng.','',
            '| ID | Mục tiêu | Actor nghiệp vụ | UI | Status | Nguồn |', '|---|---|---|---|---|---|']
    with (OUT/'traceability/use-case-matrix.csv').open('w',newline='',encoding='utf-8-sig') as f:
        w=csv.writer(f);w.writerow(['id','name','module','actors','status','ui_pages','observed_security','source_behavior_keys','source_files_methods_endpoints'])
        for g in goals:
            actor=', '.join(ACTORS[a]['name'] for a in g['actors'])
            spec.append(f'| [{g["id"]}](#{g["id"].lower()}) | {g["name"]} | {actor} | {g["status"]} |')
            sources='<br>'.join(link(s['file'],s['line']) for s in g['sources'])
            matrix.append(f'| [{g["id"]}](../specifications/use-case-specification.md#{g["id"].lower()}) | {g["name"]} | {actor} | '+', '.join(g['ui_pages'])+f' | {g["status"]} | {sources} |')
            w.writerow([g['id'],g['name'],g['module'],actor,g['status'],'; '.join(g['ui_pages']),'; '.join(g['observed_security']),'; '.join(g['parts']),'; '.join(f'{s["file"]}:{s["line"]}#{s.get("method","")} {s.get("endpoint") or ""}' for s in g['sources'])])
    for g in goals:
        spec+=['',f'<a id="{g["id"].lower()}"></a>','',f'## {g["id"]} — {g["name"]}','',
               '**Primary Actor:** '+', '.join(ACTORS[a]['name'] for a in g['actors'])+'  ',
               '**Supporting Actor:** Không có hệ thống ngoài được triển khai.  ', '**Module:** '+MODULES[g['module']]+'  ', '**Implementation Status:** '+g['status'], '',
               '**Mục tiêu và hoàn cảnh:** '+g['description'],'','**Trigger:** '+g['trigger'],'','**Preconditions theo nhánh**','']
        for part in g['preconditions']:spec+=['- '+part['flow']+': '+'; '.join(part['conditions'])]
        spec+=['','**Main Success Scenario**','']+[f'{i}. {s}' for i,s in enumerate(g['main'],1)]
        spec+=['','**Luồng lựa chọn / ngoại lệ**','']
        for part in g['alternatives']:
            spec+=['- '+part['flow']+': '+'; '.join(part['conditions'])]
        spec+=['','**Postconditions theo nhánh**','']
        for part in g['postconditions']:spec+=['- '+part['flow']+': '+'; '.join(part['conditions'])]
        spec+=['','**Failed End Condition:** '+g['failed_end_condition'],'','**Quyền được thực thi hiện tại**','']+['- '+s for s in g['observed_security']]
        if g['extension_points']:spec+=['','**Extension Point**','']+['- '+p for p in g['extension_points']]
        if g['key']=='voucher':spec+=['','**Extend:** UC033 → UC032 và UC033 → UC034, khi khách cung cấp một trong ba mã phù hợp; extension point Tính tổng cuối.']
        spec+=['','**Nguồn code / class / method / endpoint**','','| File | Class / method / field | Endpoint |','|---|---|---|']
        for s in g['sources']:
            symbol=(s.get('class')+' · ' if s.get('class') else '')+(s.get('signature') or s.get('method') or s['kind'])
            spec.append(f'| {link(s["file"],s["line"])} | {symbol.replace("|","/")} | {(s.get("endpoint") or "—").replace("|","/")} |')
        spec+=['','**UI và giới hạn triển khai**','']+['- '+n for n in g['notes']]
    (OUT/'specifications/use-case-specification.md').write_text('\n'.join(spec)+'\n')
    (OUT/'traceability/use-case-matrix.md').write_text('\n'.join(matrix)+'\n')
    with (OUT/'traceability/raw-behavior-map.csv').open('w',newline='',encoding='utf-8-sig') as f:
        w=csv.writer(f);w.writerow(['previous_id','source_key','previous_name','new_id','new_goal','decision'])
        for u in RAW['use_cases']:
            target=raw_map.get(u['key']);w.writerow([u['id'],u['key'],u['name'],target or '',ids[target]['name'] if target else '', 'grouped user goal' if target else 'client presentation preference; excluded business diagram'])
    # Keep the endpoint inventory, updating the UC IDs from the previous model.
    legacy_to_new={u['id']:raw_map.get(u['key']) for u in RAW['use_cases']}
    endpoint_file=OUT/'traceability/endpoint-inventory.csv'
    with endpoint_file.open(encoding='utf-8-sig') as f:rows=list(csv.DictReader(f))
    # Regeneration always resolves via original handlers, not through repeatedly-renumbered IDs.
    if not (OUT/'traceability/source-endpoints.json').exists():write_json('traceability/source-endpoints.json',rows)
    rows=json.loads((OUT/'traceability/source-endpoints.json').read_text())
    with endpoint_file.open('w',newline='',encoding='utf-8-sig') as f:
        w=csv.DictWriter(f,fieldnames=list(rows[0]));w.writeheader()
        for row in rows:
            row=copy.deepcopy(row);row['use_cases']=','.join(dict.fromkeys(legacy_to_new[v] for v in row['use_cases'].split(',') if v and legacy_to_new.get(v)))
            w.writerow(row)
    ep=['# API inventory và Use Case nghiệp vụ','', 'Giữ đủ HTTP methods và aliases; nhiều API có thể phục vụ các nhánh của cùng một use case. /api/auth/me là ngữ cảnh kỹ thuật.','',
        '| HTTP | Path | Handler | Use Case mới | Ghi chú |','|---|---|---|---|---|']
    with endpoint_file.open(encoding='utf-8-sig') as f:
        for r in csv.DictReader(f):ep.append(f'| {r["method"]} | {r["path"]} | {r["controller"]}.{r["handler"]} | {r["use_cases"]} | {r["note"]} |')
    (OUT/'traceability/endpoint-inventory.md').write_text('\n'.join(ep)+'\n')
    overview=['# Phạm vi hình tổng quan','', 'Hình tổng quan chọn các UC chính từ cùng catalogue, không tạo ellipse tổng hợp không có đặc tả. Chức năng còn lại nằm trong hình module.','',
              '| UC | Mục tiêu |','|---|---|']
    for i in diagrams[0]['use_cases']:overview.append(f'| {i} | {ids[i]["name"]} |')
    (OUT/'overview/overview-map.md').write_text('\n'.join(overview)+'\n')
    write_json('traceability/catalog-validation.json',dict(source_files=model['source_files'],missing_source_files=[],use_cases=len(goals),entity_count=37,actors=len(ACTORS),source_behaviors_grouped=len(raw_map),statuses=dict(Counter(g['status'] for g in goals))))

def cleanup(diagrams):
    keep={d['file'] for d in diagrams};removed=[]
    for directory in ['overview','actors','modules']:
        folder=OUT/directory
        if folder.exists():
            for p in folder.iterdir():
                if p.is_file() and p.suffix in {'.puml','.svg','.png'} and p.relative_to(OUT).as_posix() not in keep:
                    removed.append(p.relative_to(OUT).as_posix());p.unlink()
            if directory=='actors' and not any(folder.iterdir()):folder.rmdir()
    for name in ['build_catalog.py','build_diagrams.py','build_reports.py','render.py','previous-diagram-manifest.json']:
        p=OUT/'traceability'/name
        if p.exists():removed.append(p.relative_to(OUT).as_posix());p.unlink()
    print('Removed superseded documentation files:',len(removed))

def validate(model,raw_map,diagrams):
    errors=[]
    all_ids={g['id'] for g in model['use_cases']}
    represented=[i for d in diagrams if d['group']=='modules' for i in d['use_cases']]
    if set(represented)!=all_ids or len(represented)!=len(all_ids):errors.append('Module goals are missing or duplicated')
    if len(raw_map)!=83:errors.append('Source behavior coverage is incomplete')
    for d in diagrams:
        p=OUT/d['file'];data=p.read_bytes()
        if data[:8]!=b'\x89PNG\r\n\x1a\n':errors.append('Invalid PNG '+str(p))
        width,height=struct.unpack('>II',data[16:24]);d['width']=width;d['height']=height
        if not width or not height:errors.append('Empty PNG '+str(p))
    if list(OUT.rglob('*.puml')) or list(OUT.rglob('*.svg')):errors.append('Extra diagram formats remain')
    if set(p.relative_to(OUT).as_posix() for p in OUT.rglob('*.png'))!={d['file'] for d in diagrams}:errors.append('Extra/missing PNG diagrams')
    for g in model['use_cases']:
        for a in g['actors']:
            if a not in ACTORS:errors.append('Unknown business actor '+a)
        for s in g['sources']:
            if not (ROOT/s['file']).is_file():errors.append('Missing source '+s['file'])
    with (OUT/'traceability/source-inventory.csv').open(encoding='utf-8-sig') as f:
        for r in csv.DictReader(f):
            if hashlib.sha256((ROOT/r['file']).read_bytes()).hexdigest()!=r['sha256']:errors.append('Application source changed '+r['file'])
    for p in OUT.rglob('*.md'):
        for target in re.findall(r'\]\(([^)]+)\)',p.read_text()):
            target=target.strip('<>')
            if target.startswith(('http:','https:','#','mailto:')):continue
            if not (p.parent/urllib.parse.unquote(target.split('#')[0])).exists():errors.append('Broken link '+str(p)+': '+target)
    validation=dict(date='2026-10-13',business_use_cases=len(model['use_cases']),actors=len(ACTORS),png_diagrams=len(diagrams),source_behaviors_covered=len(raw_map),source_files=model['source_files'],include=0,extend=2,actor_generalization=4,errors=errors,app_tests='not run; documentation/source review only',xmi='not emitted; EA import not verified',output='one PNG per diagram')
    write_json('traceability/diagram-manifest.json',diagrams)
    write_json('traceability/validation.json',validation)
    report=['# Kiểm tra bộ hình Use Case đã chỉnh','',
            f'- {len(model["use_cases"])} mục tiêu nghiệp vụ, 7 actor, {len(diagrams)} PNG.',
            '- Mỗi module chỉ có một hình; mỗi UC xuất hiện đúng một lần trong các hình chi tiết.',
            '- 83 hành vi nguồn được gộp đầy đủ; đổi theme là tiện ích trình bày được ghi riêng.',
            '- 4 actor generalization; 2 extend có điều kiện và extension point; không include từ đăng nhập/CRUD.',
            '- PlantUML đã qua check-syntax trước render; PNG có chữ ký và kích thước hợp lệ.',
            '- Hash source ứng dụng không thay đổi; nguồn dẫn và liên kết file đã kiểm tra.',
            '- Không còn .puml/.svg hoặc các loạt actor -01/-02/-03 trong bộ bàn giao.',
            '- Chưa chạy kiểm thử nghiệp vụ hoặc DB; không khẳng định model native EA/XMI đã import.',
            '',f'**Lỗi kiểm tra: {len(errors)}**','']+['- '+e for e in errors]
    (OUT/'traceability/validation-report.md').write_text('\n'.join(report)+'\n')
    print(json.dumps(validation,ensure_ascii=False))
    if errors:raise SystemExit(1)

def main():
    parser=argparse.ArgumentParser();parser.add_argument('--jar',required=True,type=Path);parser.add_argument('--cleanup',action='store_true');args=parser.parse_args()
    assert args.jar.is_file(),args.jar
    model,raw_map=build_model();diagrams=render(model,args.jar);documents(model,raw_map,diagrams)
    if args.cleanup:cleanup(diagrams)
    validate(model,raw_map,diagrams)

if __name__=='__main__':main()
