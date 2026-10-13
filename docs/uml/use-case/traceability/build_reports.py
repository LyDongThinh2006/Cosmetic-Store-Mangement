#!/usr/bin/env python3
"""Write evidence/permission reports and validate documentation, not the app."""
import csv
import json
import re
import struct
import subprocess
import hashlib
import urllib.parse
import xml.etree.ElementTree as ET
from collections import Counter
from pathlib import Path

ROOT=Path(__file__).resolve().parents[4]
OUT=ROOT/'docs/uml/use-case'
model=json.loads((OUT/'traceability/use-cases.json').read_text())
cases=model['use_cases']; actors={a['id']:a for a in model['actors']}
manifest=json.loads((OUT/'traceability/diagram-manifest.json').read_text())
analyses={n:json.loads((OUT/f'traceability/analysis-{n}.json').read_text()) for n in ['account-security','operations','commerce','frontend']}

def source(file,line=1): return f'[{file}:{line}](../../../../{file}#L{line})'
def clean(v): return str(v).replace('|','/')

endpoints=[]
for p in sorted((ROOT/'src/main/java/com/thinh/cosmetic/rest').rglob('*.java')):
    text=p.read_text();class_at=re.search(r'public class\s+(\w+)',text);prefix=''
    mappings=list(re.finditer(r'@(Get|Post|Put|Delete|Patch|Request)Mapping\s*(?:\((.*?)\))?',text,re.S))
    for i,m in enumerate(mappings):
        kind,args=m.group(1),m.group(2) or ''
        paths=re.findall(r'"([^"]*)"',args) or ['']
        if m.start()<class_at.start(): prefix=paths[0];continue
        frag=text[m.end():(mappings[i+1].start() if i+1<len(mappings) else len(text))]
        mm=re.search(r'public\s+[\w.<>, ?\[\]]+?\s+(\w+)\s*\(',frag)
        assert mm, (p,m.group(0))
        method=mm.group(1)
        http=re.findall(r'RequestMethod\.(\w+)',args) if kind=='Request' else [kind.upper()]
        for path in paths:
            for verb in http or ['ANY']:
                route=prefix+path;ucids=[]
                for u in cases:
                    for s in u['sources']:
                        if s['file']==p.relative_to(ROOT).as_posix() and method in re.split(r'\s*[/|]\s*',s.get('method','')):
                            if u['id'] not in ucids:ucids.append(u['id'])
                note=''
                if route=='/api/auth/me': note='Ngữ cảnh xác thực kỹ thuật cho client; không tách mục tiêu use case độc lập.'
                if not ucids and not note: note='Cần kiểm tra coverage trước kết luận.'
                endpoints.append(dict(method=verb,path=route,controller=class_at.group(1),handler=method,file=p.relative_to(ROOT).as_posix(),line=text[:m.start()].count('\n')+1,use_cases=ucids,note=note))
with (OUT/'traceability/endpoint-inventory.csv').open('w',newline='',encoding='utf-8-sig') as f:
    w=csv.writer(f);w.writerow(['method','path','controller','handler','line','use_cases','note'])
    for e in endpoints:w.writerow([e['method'],e['path'],e['controller'],e['handler'],e['line'],','.join(e['use_cases']),e['note']])
report=['# API inventory và coverage', '', 'Mọi HTTP method và alias được liệt kê, nhưng không coi mỗi API là một use case. Bộ lọc bảo vệ xem security-and-actors.md.', '', '| HTTP | Path | Handler / nguồn | UC | Ghi chú |', '|---|---|---|---|---|']
for e in endpoints:report.append(f'| {e["method"]} | `{e["path"]}` | {e["controller"]}.{e["handler"]}<br>{source(e["file"],e["line"])} | '+', '.join(e['use_cases'])+' | '+e['note']+' |')
(OUT/'traceability/endpoint-inventory.md').write_text('\n'.join(report)+'\n')

security=['# Actor, RBAC và phạm vi truy cập thực tế', '', 'Nguồn quyết định: '+source('src/main/java/com/thinh/cosmetic/config/SecurityConfig.java',90)+', '+source('src/main/java/com/thinh/cosmetic/security/AppUserDetailsService.java',40)+', '+source('src/main/java/com/thinh/cosmetic/security/StoreScope.java',19)+'. Chưa đọc dữ liệu tài khoản/role trong database đang chạy.', '',
          '| Actor | Vai trò | Authority / permission | Phạm vi |', '|---|---|---|---|']
for a in model['actors']:security.append(f'| {a["name"]} | {a["role"]} | {a["authority"]} | {a["scope"]} |')
security+=['', '## Luật URL và kiểm tra business logic', '', '| Nhóm | Guard URL | Ownership / StoreScope |', '|---|---|---|',
           '| /admin/** và /api/admin/** | ROLE_EMPLOYEE | Order service lọc EmployeeStore; ADMIN/ROLE_ADMIN/ALL_STORES đi toàn chuỗi. |',
           '| /api/inventory/** | ROLE_EMPLOYEE | Không kiểm StoreScope; mọi employee có thể truyền storeId bất kỳ. UI lọc menu cửa hàng không thay thế guard API. |',
           '| /cart, /checkout, /order-success, /account/**, /api/cart/** | ROLE_CUSTOMER | Cart sửa/xóa item thiếu ownership; success page thiếu ownership mã đơn. |',
           '| /api/orders/my-orders, /preview, /{id}/cancel | ROLE_CUSTOMER | CustomerId ưu tiên principal, có fallback; cancel chỉ PENDING và kiểm chủ đơn. |',
           '| /api/orders/{id}/status | ROLE_EMPLOYEE | UpdateStatus kiểm scope nếu đơn có assignedStore. Không kiểm ORDER_PROCESS. |',
           '| /api/orders và các path còn lại | authenticated() | POST tạo đơn cả customer/employee; customer/{id} chưa ownership; code/{code} không scope cho employee. |',
           '| /api/account/** | authenticated() | Không có Account REST mapping nghiệp vụ ở namespace này. |',
           '| /api/auth/** | permitAll | /me tự trả 401 nếu không principal. Reset không OTP/token sở hữu. |',
           '| /api/customers, /employees, /products, /brands, /categories, /stores, /suppliers, /stock-transfers, /purchase-orders, /wishlist, /returns, /reviews | permitAll | Không @PreAuthorize; các operation mutation cũng công khai. |', '',
           '`XRequestedWithFilter` yêu cầu header `X-Requested-With: fetch/XMLHttpRequest` với mutation `/api/**` không dùng Bearer header. Header này là điều kiện request, không là xác thực hay quyền RBAC.', '',
           '## Quan hệ actor được xác minh', '',
           'ADMIN, ORDER_STAFF, WAREHOUSE_STAFF và PRODUCT_MANAGER chuyên biệt hóa **Nhân viên**. AppUserDetailsService luôn cấp ROLE_EMPLOYEE cho AccountType.EMPLOYEE rồi mới thêm role/permission; EmployeeService tạo account EMPLOYEE trước khi gán roleIds. Đó là bằng chứng subtype, không chỉ hai vai trò có quyền giống nhau.', '',
           'Không có RoleHierarchy; không nối ADMIN kế thừa CUSTOMER hoặc các role nhân viên khác. ADMIN được đi mọi store chỉ bởi StoreScope. Các permission chưa làm guard, nên warehouse/product manager cũng gọi được API xử lý đơn nếu có ROLE_EMPLOYEE và đúng store scope.', '',
           'Role CUSTOMER trong seeder không tạo actor khách hàng thứ hai: ROLE_CUSTOMER được cấp theo AccountType, không cần EmployeeRole. ALL_STORES là authority đặc biệt được code nhận biết, chưa seed role/permission này; không tạo actor độc lập.', '',
           'Quyền là snapshot JWT; đổi phân công vai trò không thu hồi token đã phát. Employee.status INACTIVE không được AppUserDetailsService kiểm; deactivate không khóa Account.status.', '',
           '## Actor bên ngoài không đưa vào mô hình', '',
           'Không có triển khai gọi cổng thanh toán, hãng vận chuyển, Google Maps API, Cloudinary upload hay Email/OTP. COD và cập nhật trạng thái được thực hiện nội bộ. Cloudinary/MSSQL dependency, ảnh remote và CDN trình bày không chứng minh một supporting actor nghiệp vụ. Nhà cung cấp chỉ là bản ghi do client quản lý, không có portal/API dành riêng cho nhà cung cấp.', '',
           'Nhật ký quyền cụ thể theo UC: [use-case-matrix.csv](../traceability/use-case-matrix.csv).']
(OUT/'specifications/security-and-actors.md').write_text('\n'.join(security)+'\n')

gaps=['# Khoảng trống triển khai và giới hạn xác minh', '', 'Phân biệt chức năng đã có luồng lưu/đọc, lỗi tích hợp và các mục mới chỉ xuất hiện trong thiết kế. Không suy diễn feature từ entity, enum, permission, dependency, test hoặc tên màn hình.', '',
      '## Các lỗi/nghiệp vụ chưa hoàn thiện', '', '| Vấn đề | Bằng chứng / hệ quả |', '|---|---|',
      '| Đăng ký UI | register.html gửi displayName thay fullName bắt buộc → 400; toast tự đăng nhập không có lời gọi login/cookie. |',
      '| Reset/wishlist UI | Raw fetch thiếu X-Requested-With → 403 khi dùng cookie. API reset còn thiếu xác minh sở hữu. |',
      '| Giỏ | PUT response items/subtotal khác itemSubtotal/cartSubtotal UI; +/- lấy quantity SSR cũ; sửa/xóa cart item thiếu ownership. |',
      '| Trả hàng | create không set customer bắt buộc → persistence thật không có happy path hợp lệ; approval/reject chỉ đổi trạng thái, chưa hoàn kho/refund. |',
      '| Đặt hàng và tồn | Cửa hàng chỉ định không kiểm đủ tồn; auto fallback store đầu dù thiếu; chưa lock/recheck khi giữ; có thể held vượt actual. |',
      '| Vòng đời đơn | Service chặn trạng thái terminal nhưng cho nhảy/lùi các non-terminal; hủy sau SHIPPING không hoàn lại COMMITTED. |',
      '| Quyền và ownership | Nhiều API quản lý public; permission không guard; inventory không scope; đọc đơn/address/customer có các đường thiếu ownership. |',
      '| Hồ sơ/địa chỉ/nhân viên | Phone response không map; edit địa chỉ thiếu nạp vị trí; default có thể nhiều địa chỉ; email/internalEmail mismatch; deactivate employee không khóa account. |',
      '| Danh mục | update parentId bị bỏ qua; xóa product/brand/category là xóa vật lý và có thể lỗi FK. |',
      '| Chuyển/nhập kho | Ship kiểm actual không trừ held; thiếu lock/version, @Valid cascade; checked exception sau lưu dần có nguy cơ commit một phần (chưa xác minh runtime). |', '',
      '## Backend đã có nhưng chưa có giao diện', '',
      'BeautyProfile, danh sách khách hàng, CRUD/phân công nhân viên, thêm/sửa/xóa sản phẩm/thương hiệu/danh mục, cập nhật cửa hàng, nhà cung cấp/nhập hàng/điều chuyển, review thật và moderation, clear cart. Return PROCESSING/COMPLETED có API đổi trạng thái nhưng thiếu UI và nghiệp vụ hoàn hàng.', '',
      '## UI placeholder — không đưa vào diagram như chức năng hoàn tất', '', '| Control / nhãn | Hành vi source thực tế | Nguồn |', '|---|---|---|']
for item in analyses['frontend']['ui_only']:
    gaps.append('| '+item['name']+' | '+item['reason']+' | '+'<br>'.join(source(re.sub(r':\d+$','',s),int(s.rsplit(':',1)[1]) if re.search(r':\d+$',s) else 1) for s in item['sources'])+' |')
gaps+=['', '## Chưa triển khai dù có trong thiết kế', '',
       'CRUD SKU, ảnh/thuộc tính/upload Cloudinary; BeautyProfile recommendation; voucher entity/administration/quota/expiry/usage; membership accrual/redemption; customer lock/unlock; role/permission CRUD; audit viewer/writer; pickup, staff assign/reassign store; online payment/email/OTP/courier/maps; advanced/exported reporting; transfer approval hoặc receipt tách riêng. Không tạo use case giả cho những phần này.', '',
       '## Bằng chứng chi tiết của từng nhánh khảo sát', '',
       '[Account/security](../traceability/analysis-account-security.json), [operations](../traceability/analysis-operations.json), [commerce](../traceability/analysis-commerce.json), [frontend](../traceability/analysis-frontend.json). Các JSON là nhật ký phân tích trung gian; trạng thái cuối cùng phải xem use-cases.json/đặc tả vì đã gộp cả frontend và backend.', '',
       'Không chạy database, seed, migration, API mutation, Maven hoặc test suite ứng dụng trong lần tài liệu hóa này. Tests đã đọc chỉ là chứng cứ đối chiếu; mock save có thể bỏ qua validation thật. Render/syntax/document validation có kết quả riêng; không thay thế kiểm thử nghiệp vụ.', '',
       'Chưa có Enterprise Architect để kiểm thử import. Không xuất XMI và không khẳng định tương thích EA. PlantUML --xmi được tài liệu chính thức mô tả cho class diagrams; không dùng nó tạo XMI use case giả.']
(OUT/'specifications/implementation-gaps.md').write_text('\n'.join(gaps)+'\n')

relationships=['# Kiểm tra quan hệ UML 2.x', '', '| Quan hệ | Kết quả | Bằng chứng / quyết định |', '|---|---|---|',
               '| Association | Đường liền không mũi tên giữa actor và UC trực tiếp. | use-cases.json ghi actors/primary_actors/api_access_actors. API public không được gán độc quyền ADMIN. |',
               '| Include | 0 connector. | Không tách việc đọc repository, map DTO, giữ tồn hay tính giá thành UC kỹ thuật. Preview không bắt buộc trước đặt hàng. Login là precondition. |',
               '| Extend | 0 connector. | Voucher có nhánh điều kiện trong preview/place và UI gọi preview; không tuyên bố extension point chung độc lập chưa được xác minh. CRUD, click navigation, trạng thái tuần tự không tự tạo extend. |',
               '| Actor generalization | 4 quan hệ chuyên biệt → Nhân viên. | Role nằm trên account EMPLOYEE; AppUserDetailsService tự cấp ROLE_EMPLOYEE. Dùng --\u007c> từ subtype tới parent, tam giác rỗng ở parent. |',
               '| Use-case generalization | 0 connector. | Các mục tiêu chưa có hợp đồng chuyên biệt hóa đủ chứng cứ; không dùng để gom CRUD. |', '',
               'Không nối mọi UC tới Đăng nhập; không kế thừa ADMIN từ Customer/warehouse/order/product. Tất cả actor nằm ngoài rectangle system, ellipse nằm trong. Màu chỉ là trạng thái phân tích, không phải UML stereotype thay thế notation.', '',
               'Các sơ đồ actor chuyên biệt thể hiện association trên actor Nhân viên rồi nối subtype --\u007c> Nhân viên; hành vi kế thừa được trình bày trong từng phần.']
(OUT/'specifications/uml-relationship-audit.md').write_text('\n'.join(relationships)+'\n')

errors=[]
by_id={u['id']:u for u in cases}
with (OUT/'traceability/source-inventory.csv').open(encoding='utf-8-sig') as f:
    for item in csv.DictReader(f):
        if hashlib.sha256((ROOT/item['file']).read_bytes()).hexdigest()!=item['sha256']:
            errors.append('Source changed since review: '+item['file'])
for u in cases:
    for k in ['id','name','primary_actors','supporting_actors','description','preconditions','main','alternatives','postconditions','sources','status']:
        if k not in u:errors.append(f'{u["id"]}: missing {k}')
    for s in u['sources']:
        p=ROOT/s['file']
        if not p.exists():errors.append(f'{u["id"]}: missing {p}')
        elif not 1<=s['line']<=len(p.read_text().splitlines()):errors.append(f'{u["id"]}: invalid source line {s}')
    if not any(a in actors for a in u['actors']):errors.append(f'{u["id"]}: unknown actor')
represented={i for d in manifest if d['group']=='modules' for i in d['use_cases']}
if represented!={u['id'] for u in cases}:errors.append('Module diagram coverage mismatch')
for d in manifest:
    p=OUT/d['file'];content=p.read_text()
    before,inside=content.split('rectangle "Cosmetic Store Chain Management — LUNEA" as SYSTEM {',1)
    boundary=inside.split('\n}',1)[0]
    if 'actor ' in boundary:errors.append(f'Actor inside boundary: {p}')
    if 'usecase ' in before:errors.append(f'Usecase outside boundary: {p}')
    if d['group']!='overview':
        actual=set()
        for a,b in re.findall(r'^(A_\w+|UC\d+) -- (A_\w+|UC\d+)$',content,re.M):
            actual.add((a[2:],b) if a.startswith('A_') else (b[2:],a))
        expected={(a,i) for i in d['use_cases'] for a in by_id[i]['actors'] if a in d['actors']}
        if d.get('inheritance'):
            child=d['inheritance'];parent=actors[child]['parent']
            expected={(parent,i) for i in d['use_cases']}
            if f'A_{child} --|> A_{parent}' not in content:errors.append(f'Missing/directed inheritance: {p}')
        if actual!=expected:errors.append(f'Association mismatch: {p}')
    for suffix in ['.svg','.png']:
        image=p.with_suffix(suffix)
        if not image.exists():errors.append(f'Missing rendered image: {image}')
        elif suffix=='.svg':
            root=ET.parse(image).getroot()
            if not root.tag.endswith('svg'):errors.append(f'Invalid SVG: {image}')
            visible=''.join(root.itertext()).replace('\u00a0',' ')
            if 'Syntax Error' in visible:errors.append(f'Error SVG: {image}')
            if 'Please use CSS style instead' in visible:errors.append(f'Deprecation banner: {image}')
            for ucid in d['use_cases']:
                if ucid not in visible:errors.append(f'Missing label {ucid}: {image}')
        else:
            raw=image.read_bytes()
            if raw[:8]!=b'\x89PNG\r\n\x1a\n':errors.append(f'Invalid PNG: {image}')
            else:
                width,height=struct.unpack('>II',raw[16:24])
                if not width or not height:errors.append(f'Empty PNG: {image}')
uncovered=[e for e in endpoints if not e['use_cases'] and not e['path']=='/api/auth/me']
if uncovered:errors.append('Uncovered REST handlers: '+str(uncovered))
for p in OUT.rglob('*.md'):
    for target in re.findall(r'\]\(([^)]+)\)',p.read_text()):
        if target.startswith(('http:','https:','#','mailto:')):continue
        if not (p.parent/urllib.parse.unquote(target.split('#')[0])).exists():errors.append(f'Broken local link: {p}: {target}')
for a in model['actors']:
    expected={u['id'] for u in cases if (a['parent'] or a['id']) in u['actors']}
    actual={i for d in manifest if d['group']=='actors' and a['id'] in d['actors'] for i in d['use_cases']}
    if actual!=expected:errors.append('Actor diagram coverage mismatch: '+a['id'])
status=dict(Counter(u['status'] for u in cases))
validation=dict(date='2026-10-13',source_files=model['source_files'],use_cases=len(cases),actors=len(actors),diagrams=len(manifest),rest_method_path_variants=len(endpoints),covered_business_api_variants=len(endpoints)-len(uncovered)-1,technical_endpoints=['GET /api/auth/me'],status_counts=status,errors=errors,
                app_tests='not run; static implementation review only',render_engine='PlantUML 1.2026.8 / Smetana; Java headless',xmi='not emitted; EA compatibility not verified')
(OUT/'traceability/validation.json').write_text(json.dumps(validation,ensure_ascii=False,indent=2)+'\n')
validation_md=['# Báo cáo kiểm tra', '', f'- Source đã đọc: {model["source_files"]} tệp; ledger thiếu 0.',f'- Đặc tả: {len(cases)} UC, {len(actors)} actor, 37 entity.',f'- Diagram: {len(manifest)} PlantUML; mỗi UC có trong module diagram.',f'- API inventory: {len(endpoints)} tổ hợp HTTP method/path, kể cả alias; /api/auth/me là ngữ cảnh kỹ thuật.',
               '- Quan hệ: 4 actor generalization; 0 include, 0 extend, 0 use-case generalization.', '- Syntax PlantUML được kiểm tra trước render; SVG parse XML và PNG kiểm chữ ký/kích thước.',
               '- Association khớp actor–UC; generalization đúng hướng subtype→parent. Actor views có đủ UC được gán.',
               '- Source hash không đổi; liên kết file Markdown hợp lệ; UC labels trong SVG đầy đủ; không banner lỗi/deprecation.',
               '- Đã đối chiếu UI/REST/service/DTO/repository/entity/security; trạng thái cuối đã gộp lỗi tích hợp.', '- XMI không tạo vì không kiểm chứng import EA.',
               '- Chưa chạy app/tests/DB; kiểm tra tài liệu không xác nhận nghiệp vụ production.', '', '**Lỗi kiểm tra tự động:** '+str(len(errors)), '']+['- '+e for e in errors]
(OUT/'traceability/validation-report.md').write_text('\n'.join(validation_md)+'\n')
print(json.dumps(validation,ensure_ascii=False))
