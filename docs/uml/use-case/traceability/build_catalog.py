#!/usr/bin/env python3
"""Build documentation from reviewed evidence; never write application files."""
import copy
import csv
import hashlib
import json
import re
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parents[4]
OUT = ROOT / 'docs/uml/use-case'
BASE = 'src/main/java/com/thinh/cosmetic/'

def read(name):
    return json.loads((OUT / 'traceability' / name).read_text())

def save(path, value):
    (OUT / path).write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n')

def lines(value):
    return value if isinstance(value, list) else [value]

def link(file, line=1):
    return f'[{file}:{line}](../../../../{file}#L{line})'

analyses = {n: read(f'analysis-{n}.json') for n in ['account-security', 'operations', 'commerce', 'frontend']}
readers = {}
for name, data in analyses.items():
    for entry in data['files_read']:
        file = entry if isinstance(entry, str) else entry['path']
        readers.setdefault(file, []).append(name)
suffixes = {'.java', '.html', '.js', '.css', '.json', '.properties', '.txt', '.webmanifest'}
source_files = sorted(p.relative_to(ROOT).as_posix() for p in (ROOT / 'src').rglob('*') if p.is_file() and p.suffix in suffixes)
missing = sorted(set(source_files) - readers.keys())
assert not missing, f'Read all source before generating diagrams: {missing}'
with (OUT / 'traceability/source-inventory.csv').open('w', newline='', encoding='utf-8-sig') as f:
    w = csv.writer(f)
    w.writerow(['file', 'lines', 'sha256', 'reviewed_by', 'purpose'])
    for file in sorted(set(source_files) | readers.keys()):
        p = ROOT / file
        raw = p.read_bytes()
        w.writerow([file, len(raw.splitlines()), hashlib.sha256(raw).hexdigest(), ', '.join(readers.get(file, [])), 'test evidence only' if '/test/' in file else 'implementation/configuration'])

actors = [
    dict(id='GUEST', name='Khách vãng lai', role='Người tra cứu chưa đăng nhập.', authority='Không cần JWT.', scope='Tra cứu công khai; đăng ký/đăng nhập/đặt lại mật khẩu.', parent=None),
    dict(id='CUSTOMER', name='Khách hàng', role='AccountType.CUSTOMER, customerId trong JWT.', authority='ROLE_CUSTOMER tự cấp theo account type.', scope='Trang account/cart/checkout; đơn riêng ở đường có ownership. API khách hàng/yêu thích/đổi trả có các ngoại lệ công khai.', parent=None),
    dict(id='EMPLOYEE', name='Nhân viên', role='AccountType.EMPLOYEE, không bắt buộc có role cụ thể.', authority='ROLE_EMPLOYEE tự cấp; authorities vai trò/permission là snapshot JWT.', scope='Mọi trang admin, kho ở mọi storeId qua API; đơn theo EmployeeStore.', parent=None),
    dict(id='ADMIN', name='Quản trị viên', role='Nhân viên được gán role ADMIN.', authority='ROLE_EMPLOYEE + ADMIN + ROLE_ADMIN; seeder gán 12 permission.', scope='StoreScope trả null: đơn mọi cửa hàng; không có quyền kế thừa CUSTOMER.', parent='EMPLOYEE'),
    dict(id='ORDER_STAFF', name='Nhân viên xử lý đơn', role='Nhân viên được gán ORDER_STAFF.', authority='ROLE_EMPLOYEE; seed ORDER_VIEW, ORDER_PROCESS, INVENTORY_VIEW.', scope='Đơn tại cửa hàng được phân công; inventory API không scope. Permission không được dùng làm guard.', parent='EMPLOYEE'),
    dict(id='WAREHOUSE_STAFF', name='Nhân viên kho', role='Nhân viên được gán WAREHOUSE_STAFF.', authority='ROLE_EMPLOYEE; seed INVENTORY_VIEW, INVENTORY_ADJUST.', scope='Vẫn có thể xử lý đơn tại cửa hàng phân công như mọi EMPLOYEE; kho API không scope.', parent='EMPLOYEE'),
    dict(id='PRODUCT_MANAGER', name='Nhân viên quản lý sản phẩm', role='Nhân viên được gán PRODUCT_MANAGER; role có trong seeder.', authority='ROLE_EMPLOYEE; seed CATALOG_MANAGE, INVENTORY_VIEW.', scope='Có quyền EMPLOYEE thực tế; catalog mutation public. Không tạo độc quyền quản lý sản phẩm từ permission.', parent='EMPLOYEE'),
    dict(id='PUBLIC', name='Người gọi API công khai', role='Bất kỳ client HTTP, kể cả chưa đăng nhập; actor giao tiếp API.', authority='permitAll; mutation vẫn cần X-Requested-With: fetch/XMLHttpRequest nếu không có Bearer header.', scope='API public thực tế về khách/nhân viên/catalog/cửa hàng/nhập/chuyển/đổi trả/review. Đây là truy cập hiện hành do thiếu guard, không phải yêu cầu nghiệp vụ mới.', parent=None),
]
actor_map = {a['id']: a for a in actors}
modules = {
    'authentication': 'Xác thực và phiên đăng nhập', 'customers': 'Hồ sơ khách hàng và địa chỉ',
    'employees': 'Nhân viên và phân công', 'catalog': 'Sản phẩm và tra cứu mỹ phẩm',
    'brand_category': 'Thương hiệu và danh mục', 'store': 'Cửa hàng',
    'inventory': 'Tồn kho', 'stock_transfer': 'Điều chuyển kho', 'purchase': 'Nhà cung cấp và nhập hàng',
    'cart': 'Giỏ hàng', 'wishlist': 'Yêu thích', 'order': 'Đặt hàng và xử lý đơn',
    'returns': 'Yêu cầu trả hàng', 'review': 'Đánh giá sản phẩm', 'dashboard': 'Tổng quan vận hành',
    'ui_preferences': 'Tùy chọn giao diện',
}
ui = {a['key']: a for a in analyses['frontend']['ui_actions']}
ui_map = {
    'AUTH_REGISTER': ['register'], 'AUTH_LOGIN': ['login'], 'AUTH_LOGOUT': ['logout'], 'AUTH_RESET': ['reset_password'],
    'CUSTOMER_READ': ['view_profile'], 'CUSTOMER_EDIT': ['update_profile'], 'ADDRESS_READ': ['view_addresses'],
    'ADDRESS_ADD': ['add_address'], 'ADDRESS_EDIT': ['update_address'], 'ADDRESS_DELETE': ['delete_address'],
    'catalog_browse': ['browse_catalog', 'admin_view_catalog'], 'catalog_search': ['browse_catalog', 'admin_view_catalog'],
    'catalog_detail': ['view_product'], 'brand_browse': ['view_brands', 'admin_view_catalog'],
    'category_browse': ['browse_catalog', 'admin_view_catalog'], 'store_browse': ['view_stores'],
    'stock_public': ['view_sku_store_stock'], 'inventory_browse': ['view_inventory'],
    'inventory_low': ['low_stock'], 'inventory_adjust': ['adjust_inventory'],
    'cart.view': ['view_cart'], 'cart.add': ['add_cart'], 'cart.quantity': ['update_cart'], 'cart.remove': ['remove_cart'],
    'wishlist.view': ['view_wishlist'], 'wishlist.add': ['add_wishlist'], 'wishlist.remove': ['remove_wishlist'],
    'order.preview': ['checkout_form'], 'order.voucher': ['apply_voucher'], 'order.place': ['place_order'],
    'order.history': ['view_my_orders'], 'order.detail': ['view_order', 'order_success'],
    'order.cancel_customer': ['cancel_my_order'], 'order.staff_list': ['staff_orders'], 'order.staff_detail': ['staff_order_detail'],
    'order.confirm': ['confirm_order'], 'order.prepare': ['prepare_order'], 'order.ship': ['ship_order'],
    'order.complete': ['complete_order'], 'order.cancel_staff': ['staff_cancel_order'],
    'return.create': ['request_return'], 'return.view': ['view_return', 'staff_view_returns'],
    'return.approve': ['approve_return'], 'return.reject': ['reject_return'],
}
cases = []
for name in ['account-security', 'operations', 'commerce']:
    cases += copy.deepcopy(analyses[name]['use_cases'])
# A concrete default-address control has a distinct user goal, while sharing the update endpoint.
default = copy.deepcopy(next(u for u in cases if u['key'] == 'ADDRESS_EDIT'))
default.update(key='ADDRESS_DEFAULT', name='Đặt địa chỉ nhận hàng mặc định', description='Chọn một địa chỉ có sẵn làm địa chỉ mặc định.',
               main=['UI tải danh sách địa chỉ và tìm địa chỉ được chọn.', 'Gán isDefault=true, gửi toàn bộ địa chỉ bằng PUT.', 'Mapper lưu cờ trên địa chỉ này; không xóa cờ mặc định của địa chỉ khác.'],
               alternatives=['Không so sánh địa chỉ với customerId hoặc principal; API công khai.', 'Có thể tồn tại nhiều địa chỉ isDefault=true vì update không reset các địa chỉ còn lại.', 'Địa chỉ không tồn tại: UI không tìm được target hoặc API trả lỗi.'],
               postconditions='Địa chỉ được chọn có isDefault=true; có thể tồn tại nhiều địa chỉ mặc định.')
cases.append(default)
ui_map['ADDRESS_DEFAULT'] = ['default_address']
cases.append(dict(key='dashboard.view', name='Xem tổng quan vận hành', module='dashboard', actors=['EMPLOYEE'], status_backend='Implemented',
                  description='Tổng hợp KPI từ danh sách đơn thực và cảnh báo tồn thấp; tính toán trong trình duyệt.',
                  preconditions='ROLE_EMPLOYEE; đơn được lọc theo StoreScope ở API /api/admin/orders.',
                  main=['Mở /admin; page controller tải cửa hàng theo phân công.', 'JS lấy danh sách đơn qua API admin và cộng totalAmount của đơn COMPLETED.', 'Đếm PENDING, PREPARING/SHIPPING, lấy sáu đơn gần nhất.', 'Lấy tồn thấp riêng cho một cửa hàng; hiển thị KPI và bảng.'],
                  alternatives=['ADMIN có thể tổng hợp đơn toàn chuỗi; tồn thấp vẫn dùng một cửa hàng đầu.', 'Không có đơn: KPI bằng 0; không có báo cáo theo ngày, biểu đồ chuỗi thời gian hoặc xuất báo cáo.'],
                  postconditions='Dashboard hiển thị dữ liệu và phép tổng hợp; không đổi DB.',
                  sources=[dict(file=BASE+'controller/AdminPageController.java', method='dashboard', endpoint='GET /admin'), dict(file=BASE+'rest/order/AdminOrderRestController.java', method='getOrders', endpoint='GET /api/admin/orders')], notes=[]))
ui_map['dashboard.view'] = ['dashboard']
cases.append(dict(key='ui.theme', name='Đổi giao diện sáng tối', module='ui_preferences', actors=['GUEST', 'CUSTOMER', 'EMPLOYEE'], status_backend='UI Only',
                  description='Chọn dark/light và nhớ lựa chọn trong localStorage của trình duyệt; chức năng client hoàn chỉnh, không cần backend.',
                  preconditions='Trình duyệt tải api.js và nút đổi theme.',
                  main=['Người dùng nhấn nút đổi giao diện.', 'JS đổi data-theme và cập nhật nhãn nút.', 'Ghi lunea-theme vào localStorage; lần tải trang sau đọc lựa chọn.'],
                  alternatives=['localStorage không khả dụng: code bỏ qua lỗi lưu, thay theme phiên hiện tại vẫn hoạt động.'],
                  postconditions='Giao diện thay đổi trên client; không lưu server.', sources=[dict(file='src/main/resources/static/js/api.js', method='toggleTheme', endpoint='')], notes=['UI Only ở đây mô tả nơi thực hiện; không là placeholder.']))
ui_map['ui.theme'] = ['theme']

controller_methods = {'CUSTOMER_READ': 'getById', 'CUSTOMER_LIST': 'getAll', 'EMPLOYEE_READ': 'getAll / getById'}
for u in cases:
    u['backend_assessment'] = u.pop('status_backend')
    u['actors'] = list(dict.fromkeys({'ROLE_CUSTOMER': 'CUSTOMER', 'ROLE_EMPLOYEE': 'EMPLOYEE'}.get(a, a) for a in u['actors']))
    u['supporting_actors'] = []
    u['ui_actions'] = ui_map.get(u['key'], [])
    u['ui_pages'] = []
    for key in u['ui_actions']:
        a = ui[key]
        for page in lines(a['page']):
            if page not in u['ui_pages']: u['ui_pages'].append(page)
        for actor in a.get('actors', []):
            actor = {'Guest': 'GUEST', 'Customer': 'CUSTOMER', 'Employee': 'EMPLOYEE'}[actor]
            if actor not in u['actors']: u['actors'].append(actor)
        for source in a['sources']:
            match = re.match(r'^(.*?):(\d+)$', source)
            file, line = (match.group(1), int(match.group(2))) if match else (source, 1)
            kind='implementation' if file.endswith('.java') else 'UI'
            u['sources'].append(dict(file=file, line=line, method='', endpoint='', kind=kind))
    # PUBLIC alone denotes API access; public browsing also has the storefront actor.
    if u['key'] in {'catalog_browse','catalog_search','catalog_detail','brand_browse','category_browse','store_browse','stock_public','review.view_product'}:
        if 'GUEST' not in u['actors']: u['actors'].append('GUEST')
    u['primary_actors'] = [a for a in u['actors'] if a != 'PUBLIC'] or ['PUBLIC']
    u['api_access_actors'] = ['PUBLIC'] if 'PUBLIC' in u['actors'] else []
    partial_ui = any(ui[k]['integration_status'] == 'Partially Implemented' for k in u['ui_actions'])
    if u['backend_assessment'] == 'UI Only': status = 'UI Only'
    elif u['backend_assessment'] == 'Partially Implemented' or partial_ui: status = 'Partially Implemented'
    elif u['ui_actions']: status = 'Implemented'
    else: status = 'Backend Only'
    u['status'] = status
    u['ui_integration'] = 'Có UI: ' + ', '.join(u['ui_actions']) if u['ui_actions'] else 'Chưa tích hợp UI'
    # These customer profile fields have no form/fetch/page in this snapshot.
    if u['key'] in {'BEAUTY_READ', 'BEAUTY_EDIT'}:
        u['status'] = 'Backend Only'; u['ui_pages'] = []; u['ui_actions'] = []; u['ui_integration'] = 'Chưa tích hợp UI'
    if not u.get('security'):
        if 'PUBLIC' in u['actors'] or u['module'] == 'authentication': rule = 'API permitAll; không yêu cầu xác thực.'
        elif u['key'] == 'order.place': rule = '/api/orders và /checkout alias: authenticated(); UI /checkout: ROLE_CUSTOMER.'
        elif 'EMPLOYEE' in u['actors']: rule = 'ROLE_EMPLOYEE theo SecurityConfig; không guard theo permission.'
        elif 'CUSTOMER' in u['actors']: rule = 'ROLE_CUSTOMER theo SecurityConfig.'
        else: rule = 'Client only; không API.'
        u['security'] = dict(url_rule=rule, preauthorize=None, store_scope=u['key'].startswith('order.staff') or u['key'] in {'order.confirm','order.prepare','order.ship','order.complete','order.cancel_staff','dashboard.view'})
    if u['key'] == 'order.place': u['security']['url_rule'] = 'authenticated() cho POST /api/orders và /api/orders/checkout; trang /checkout ROLE_CUSTOMER.'
    if u['key'] == 'order.detail': u['security']['url_rule'] = 'REST /api/orders/{id} và /code/{code}: authenticated(); page /account/orders/{id}, /order-success: ROLE_CUSTOMER.'
    if u['key'] == 'order.history': u['security']['url_rule'] = '/my-orders: ROLE_CUSTOMER; /customer/{customerId}: authenticated(), không ownership; page /account/orders: ROLE_CUSTOMER.'
    # Fix controller versus service method naming in the account draft.
    for s in u['sources']:
        if '/rest/account/' in s['file'] and u['key'] in controller_methods: s['method'] = controller_methods[u['key']]
        if s['file'].endswith('EmployeeRestController.java') and u['key'] == 'EMPLOYEE_EDIT': s['method'] = 'update'
        if s['file'].endswith('CustomerRestController.java') and u['key'] == 'ADDRESS_DEFAULT': s['method'] = 'updateAddress'
        s['class'] = s.pop('class_name', s.get('class', Path(s['file']).stem)) if s['file'].endswith('.java') else ''
        content = (ROOT / s['file']).read_text()
        method = s.get('method', '')
        if 'line' not in s:
            token = re.split(r'\s*[/|]\s*', method)[0]
            match = re.search(r'\b' + re.escape(token) + r'\s*\(', content) if token else None
            if match: s['line'] = content[:match.start()].count('\n') + 1
            else: s['line'] = 1
        s['kind'] = s.get('kind', 'entity/DTO field' if '/domain/' in s['file'] else ('inherited repository method' if method in {'save','findAll','deleteById'} and '/repository/' in s['file'] else 'implementation'))
        if s['file'].endswith('InventoryAdjustmentEntity.java'):
            s['symbol']=s['method']; s['method']=''; s['kind']='entity field: '+s['symbol']
        if s['file'].endswith('OrderServiceImpl.java') and u['key'] in {'order.staff_detail','order.confirm','order.prepare','order.ship','order.complete','order.cancel_staff'}:
            token='getById' if u['key']=='order.staff_detail' else 'updateStatus'
            match=re.search(r'public OrderResponse '+token+r'\(AuthPrincipal principal,',content)
            assert match
            s['line']=content[:match.start()].count('\n')+1
            s['signature']=token+'(AuthPrincipal principal, ...)'
    u['notes'] = lines(u.get('notes', []))
    for key in u['ui_actions']: u['notes'].append('UI: ' + ui[key]['notes'])
    u['preconditions'] = lines(u['preconditions']); u['postconditions'] = lines(u['postconditions'])

# Add direct profile/page behavior evidence and retain distinct API variants.
profile = next(u for u in cases if u['key'] == 'CUSTOMER_READ')
profile['sources'].append({'file':BASE+'service/account/impl/CustomerServiceImpl.java', 'class':'CustomerServiceImpl', 'method':'getProfile', 'line':38, 'endpoint':'', 'kind':'implementation'})
for u in cases:
    if u['key']=='CUSTOMER_READ':
        u['main']=['Trang /account/profile lấy principal.customerId và gọi CustomerService.getProfile trực tiếp (SSR).', 'Service tìm CustomerEntity và map hồ sơ vào model.', 'Template hiển thị thông tin; GET /api/customers/{id} là đường API đọc thay thế.']
    if u['key']=='AUTH_LOGIN':
        u['alternatives'].append('UI đọc res.isEmployee (không có trong LoginResponse), bỏ response.redirectUrl; employee đăng nhập thành công từ /login mặc định chuyển về / thay vì /admin.')
        u['postconditions']=['Cookie/JWT đã cấp khi credentials hợp lệ; backend trả redirectUrl nhưng UI chưa xử lý đúng hướng chuyển cho employee.']
    if u['key']=='order.history':
        u['sources'].append({'file':BASE+'rest/order/OrderRestController.java','class':'OrderRestController','method':'getByCustomer','line':85,'endpoint':'GET /api/orders/customer/{customerId}','kind':'implementation'})
    if u['key']=='AUTH_RESET':
        u['notes'].append('Đổi mật khẩu không thu hồi JWT hiện có; token tiếp tục tới exp.')
    if u['key'] == 'order.preview':
        u['notes'].append('SSR /checkout hiển thị giá giỏ; preview API được UI dùng khi áp mã. Chưa tạo đơn hoặc giữ tồn ở bước xem tổng.')
    if u['key'] == 'order.place':
        u['notes'].append('UI không có chọn fulfillmentStoreId; chỉ API mới nhận cửa hàng chỉ định. Không có endpoint nhân viên gán lại chi nhánh.')

order = {m:i for i,m in enumerate(modules)}
cases.sort(key=lambda u: order[u['module']])
for u in cases:
    if u['module'] == 'returns':
        u['name'] = u['name'].replace('đổi trả', 'trả hàng')
        u['description'] = u['description'].replace('đổi trả', 'trả hàng')
assert len({u['key'] for u in cases}) == len(cases)
for i,u in enumerate(cases, 1): u['id'] = f'UC{i:03d}'
save('traceability/use-cases.json', dict(date='2026-10-13', scope='static source review; no app/database mutation or runtime claim', source_files=len(source_files), actors=actors, modules=modules, use_cases=cases,
                                     relationships=[dict(type='generalization', source=a['id'], target=a['parent'], evidence=BASE+'security/AppUserDetailsService.java#loadUserByUsername') for a in actors if a['parent']]))

# Inventory all 37 annotated entities, including model parts that have no use-case implementation.
entity_lines = ['# Entity và quan hệ dữ liệu từ source', '', 'Danh sách lấy từ các entity đã đọc, không phải schema introspection của database đang chạy. DTO/id helper không có `@Entity` không được tính.', '', '| Entity / bảng | Quan hệ được khai báo | Nguồn |', '|---|---|---|']
entity_count = 0
for p in sorted((ROOT / (BASE+'domain/entity')).rglob('*.java')):
    text = p.read_text()
    if not re.search(r'@Entity\b',text): continue
    entity_count += 1
    table = re.search(r'@Table\(name\s*=\s*"([^"]+)"',text)
    relations=[]
    for m in re.finditer(r'@(OneToOne|OneToMany|ManyToOne|ManyToMany)([\s\S]*?)private\s+([\w<>?, ]+)\s+(\w+)\s*;',text):
        kind, metadata, typ, field = m.groups()
        if 'private' in metadata: continue
        nullable = 'bắt buộc' if ('optional = false' in metadata or 'nullable = false' in metadata) else 'xem annotation'
        relations.append(f'{field}: {kind} → {typ} ({nullable})')
    file=p.relative_to(ROOT).as_posix()
    entity_lines.append(f'| {p.stem} / `{table.group(1) if table else "unknown"}` | ' + '<br>'.join(relations or ['Không có relation entity']) + f' | {link(file)} |')
entity_lines += ['', 'Các bảng role/permission/audit và entity SKU/ảnh/thuộc tính không chứng minh có UI/API quản lý. Xem báo cáo khoảng trống triển khai.', '', f'Tổng: **{entity_count} entity**.']
(OUT/'specifications/entity-inventory.md').write_text('\n'.join(entity_lines)+'\n')

spec = ['# Đặc tả Use Case theo implementation', '', '**Ngày khảo sát: 13/10/2026.** Mọi scenario dưới đây mô tả code đã đọc; `Implemented` là mức xác minh tĩnh, không khẳng định đã chạy thử production.', '',
        'Actor chính là người dùng giao diện hoặc client API có mục tiêu. Association với **Người gọi API công khai** ghi truy cập hiện tại do `permitAll`, không trao quyền nghiệp vụ mới. Supporting actor không có vì chưa thấy tích hợp hệ thống ngoài.', '',
        'Đăng nhập là precondition ở chức năng được bảo vệ. Không tự tạo include/extend từ CRUD, navigation hay các bước trạng thái. Mã voucher là nhánh điều kiện trong preview/place; chưa tách thành lời gọi hành vi tái sử dụng đủ rõ để khẳng định một connector UML.', '',
        '| ID | Use case | Module | Status |', '|---|---|---|---|']
for u in cases: spec.append(f'| [{u["id"]}](#{u["id"].lower()}) | {u["name"]} | {modules[u["module"]]} | {u["status"]} |')
for u in cases:
    spec += ['', f'<a id="{u["id"].lower()}"></a>', '', f'## {u["id"]} — {u["name"]}', '',
             f'**Module:** {modules[u["module"]]}  ', f'**Implementation Status:** {u["status"]}  ',
             '**Primary Actor:** '+', '.join(actor_map[a]['name'] for a in u['primary_actors'])+'  ', '**Supporting Actor:** Không có.  ',
             '**Association API public:** '+(', '.join(actor_map[a]['name'] for a in u['api_access_actors']) or 'Không có.')+'  ',
             '**UI:** '+(', '.join(u['ui_pages']) or 'Chưa có.')+'  ', '**Quyền:** '+u['security']['url_rule'], '',
             '**Mô tả nghiệp vụ:** '+u['description'], '', '**Preconditions**', ''] + ['- '+p for p in u['preconditions']]
    spec += ['', '**Main Success Scenario**', ''] + [f'{i}. {p}' for i,p in enumerate(u['main'],1)]
    spec += ['', '**Alternative / Exception Flows**', ''] + ['- '+p for p in u['alternatives']]
    spec += ['', '**Postconditions**', ''] + ['- '+p for p in u['postconditions']]
    spec += ['', '**Nguồn source / class / method / endpoint**', '', '| Nguồn | Class / method hoặc field | Endpoint / route |', '|---|---|---|']
    for s in u['sources']:
        symbol=(('`'+s['class']+'` · ') if s['class'] else '')+'`'+(s.get('signature') or s.get('method') or s['kind'])+'`'
        spec.append(f'| {link(s["file"],s["line"])} | {symbol} | `{(s.get("endpoint") or "—").replace("|", "/")}` |')
    if u['notes']: spec += ['', '**Giới hạn và đối chiếu UI**', ''] + ['- '+n for n in dict.fromkeys(u['notes'])]
(OUT/'specifications/use-case-specification.md').write_text('\n'.join(spec)+'\n')

matrix=['# Use Case Traceability Matrix', '', 'Một use case có thể dùng nhiều endpoint hoặc cùng endpoint với use case khác. UI và backend được đối chiếu riêng; các source link chỉ về repository hiện tại.', '',
        '| ID | Mục tiêu / module | Actor trực tiếp | Quyền thực tế | UI | Status | Nguồn |', '|---|---|---|---|---|---|---|']
with (OUT/'traceability/use-case-matrix.csv').open('w',newline='',encoding='utf-8-sig') as f:
    w=csv.writer(f);w.writerow(['id','name','module','primary_actors','all_associations','supporting_actors','status','backend_assessment','security','store_scope','ui_pages','api_endpoints','sources'])
    for u in cases:
        sources='<br>'.join(link(s['file'],s['line'])+' · '+s.get('method','').replace('|','/') for s in u['sources'])
        matrix.append(f'| [{u["id"]}](../specifications/use-case-specification.md#{u["id"].lower()}) | {u["name"]}<br>{modules[u["module"]]} | '+', '.join(actor_map[a]['name'] for a in u['actors'])+' | '+u['security']['url_rule'].replace('|','/')+' | '+', '.join(u['ui_pages'])+f' | {u["status"]} | {sources} |')
        w.writerow([u['id'],u['name'],u['module'],','.join(u['primary_actors']),','.join(u['actors']),'',u['status'],u['backend_assessment'],u['security']['url_rule'],u['security'].get('store_scope',False),'; '.join(u['ui_pages']),'; '.join(dict.fromkeys(s.get('endpoint','') or '' for s in u['sources'] if s.get('endpoint'))),'; '.join(f'{s["file"]}:{s["line"]}#{s.get("method","")}' for s in u['sources'])])
(OUT/'traceability/use-case-matrix.md').write_text('\n'.join(matrix)+'\n')
save('traceability/catalog-validation.json',dict(source_files=len(source_files),missing_source_files=missing,use_cases=len(cases),entity_count=entity_count,actors=len(actors),statuses=dict(Counter(u['status'] for u in cases))))
print(json.dumps(read('catalog-validation.json'),ensure_ascii=False))
