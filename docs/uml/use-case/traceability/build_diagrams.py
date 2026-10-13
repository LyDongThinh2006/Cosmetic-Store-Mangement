#!/usr/bin/env python3
"""Emit editable PlantUML after build_catalog confirms source coverage."""
import json
import textwrap
from pathlib import Path

OUT = Path(__file__).resolve().parents[1]
model = json.loads((OUT/'traceability/use-cases.json').read_text())
check = json.loads((OUT/'traceability/catalog-validation.json').read_text())
assert not check['missing_source_files']
actors = {a['id']:a for a in model['actors']}
cases = model['use_cases']
modules = model['modules']
manifest = []
colors = {'Implemented':'#FFF8DB','Partially Implemented':'#FFE7CE','Backend Only':'#E5EEF9','UI Only':'#EDE5F4','Not Verified':'#EEEEEE'}

def wrap(value, width=28):
    return '\\n'.join(textwrap.wrap(value,width=width,break_long_words=False,break_on_hyphens=False))

def start(title):
    return ['@startuml', '!pragma layout smetana', 'left to right direction',
            'skinparam backgroundColor #FFFFFF', 'skinparam shadowing false',
            'skinparam defaultFontName SansSerif', 'skinparam defaultFontSize 13',
            'skinparam ArrowColor #4A4A4A', 'skinparam ArrowThickness 1',
            'skinparam actorStyle stickman', 'skinparam actorBackgroundColor #FFFFFF',
            'skinparam actorBorderColor #333333', 'skinparam usecaseBorderColor #5C5544',
            'skinparam usecaseFontColor #252525', 'skinparam rectangleBorderColor #565656',
            'skinparam rectangleBackgroundColor #FFFFFF', 'skinparam nodesep 32',
            'skinparam ranksep 70', f'title {title}']

def legend():
    return ['legend bottom left', '|= Màu |= Implementation Status |',
            '|<#FFF8DB> | Implemented |', '|<#FFE7CE> | Partially Implemented |',
            '|<#E5EEF9> | Backend Only |', '|<#EDE5F4> | UI Only |',
            'Nguồn: source review 13/10/2026. Chưa chạy nghiệm thu ứng dụng.',
            'API công khai phản ánh permitAll hiện tại; xem báo cáo quyền.', 'endlegend']

def emit(file,title,selected,actor_ids=None,inherit=None,group='modules'):
    ids=actor_ids or list(dict.fromkeys(a for u in selected for a in u['actors']))
    data=start(title)
    for a in ids: data.append(f'actor "{wrap(actors[a]["name"],22)}" as A_{a}')
    if inherit:
        parent=actors[inherit]['parent']
        data.append(f'actor "{actors[parent]["name"]}" as A_{parent}')
        data.append(f'A_{inherit} --|> A_{parent}')
    data.append('rectangle "Cosmetic Store Chain Management — LUNEA" as SYSTEM {')
    for u in selected:
        label=u['id']+'\\n'+wrap(u['name'])
        data.append(f'  usecase "{label}" as {u["id"]} {colors[u["status"]]}')
    data.append('}')
    for u in selected:
        for a in ids:
            if a in u['actors']:
                endpoint=actors[inherit]['parent'] if inherit and a==inherit else a
                if group=='modules' and endpoint=='PUBLIC' and len(ids)>1:
                    data.append(f'{u["id"]} -- A_{endpoint}')
                else:
                    data.append(f'A_{endpoint} -- {u["id"]}')
    if inherit:
        data += ['note right of A_'+inherit, wrap(actors[inherit]['scope'],50).replace('\\n','\n'),
                 'Các association ở Nhân viên được kế thừa.', 'end note']
    data+=legend()+['@enduml','']
    (OUT/file).write_text('\n'.join(data))
    manifest.append(dict(file=file,title=title,group=group,use_cases=[u['id'] for u in selected],actors=ids,inheritance=inherit))

# Module partitions retain every use case and its direct associations.
for module,title in modules.items():
    selected=[u for u in cases if u['module']==module]
    groups=[selected]
    if module=='order':
        groups=[[u for u in selected if not u['key'].startswith('order.staff') and u['key'] not in {'order.confirm','order.prepare','order.ship','order.complete','order.cancel_staff'}],
                [u for u in selected if u['key'].startswith('order.staff') or u['key'] in {'order.confirm','order.prepare','order.ship','order.complete','order.cancel_staff'}]]
    for i,part in enumerate(groups,1):
        suffix=f'-{i}' if len(groups)>1 else ''
        emit(f'modules/{module}{suffix}.puml',title+(f' — phần {i}' if suffix else ''),part)

# Specialized employee actors inherit the concrete EMPLOYEE access, not assumed
# powers of other organizational roles. Split large actor views for readability.
for actor,a in actors.items():
    inherited=bool(a['parent'])
    selected=[u for u in cases if (a['parent'] if inherited else actor) in u['actors']]
    for i in range(0,len(selected),12):
        part=selected[i:i+12]
        number=i//12+1
        suffix=f'-{number:02d}' if len(selected)>12 else ''
        if inherited:
            part=[dict(u,actors=[actor]) for u in part]
        emit(f'actors/{actor.lower()}{suffix}.puml',a['name']+(f' — phần {number}' if suffix else ''),part,[actor],actor if inherited else None,'actors')

# The overview deliberately summarizes existing case sets, not new detailed UCs.
summary = [
    ('S01','Sử dụng tài khoản', ['GUEST','CUSTOMER','EMPLOYEE'], lambda u:u['module']=='authentication'),
    ('S02','Tra cứu mỹ phẩm và cửa hàng', ['GUEST','CUSTOMER','EMPLOYEE','PUBLIC'], lambda u:u['key'] in {'catalog_browse','catalog_search','catalog_detail','brand_browse','category_browse','store_browse','stock_public'}),
    ('S03','Quản lý hồ sơ và địa chỉ', ['CUSTOMER','PUBLIC'], lambda u:u['module']=='customers' and u['key']!='CUSTOMER_LIST'),
    ('S04','Quản lý giỏ và yêu thích', ['CUSTOMER','PUBLIC'], lambda u:u['module'] in {'cart','wishlist'}),
    ('S05','Đặt và theo dõi đơn hàng', ['CUSTOMER','EMPLOYEE'], lambda u:u['module']=='order' and 'CUSTOMER' in u['actors']),
    ('S06','Xử lý đơn tại cửa hàng', ['EMPLOYEE'], lambda u:u['module']=='order' and u['key']!='order.place' and 'EMPLOYEE' in u['actors']),
    ('S07','Tra cứu và điều chỉnh tồn', ['EMPLOYEE'], lambda u:u['module']=='inventory' and u['key']!='stock_public'),
    ('S08','Xem tổng quan vận hành', ['EMPLOYEE'], lambda u:u['module']=='dashboard'),
    ('S09','Quản lý danh mục, nhập và chuyển kho', ['PUBLIC'], lambda u:u['module'] in {'catalog','brand_category','store','purchase','stock_transfer'} and 'PUBLIC' in u['actors']),
    ('S10','Tra cứu và cập nhật khách, nhân viên', ['PUBLIC'], lambda u:u['module'] in {'customers','employees'} and 'PUBLIC' in u['actors']),
    ('S11','Yêu cầu và xử lý trả hàng', ['CUSTOMER','EMPLOYEE','PUBLIC'], lambda u:u['module']=='returns'),
    ('S12','Tra cứu và ghi nhận đánh giá', ['GUEST','PUBLIC'], lambda u:u['module']=='review'),
]
data=start('Tổng quan Use Case — hệ thống hiện tại')
for a in actors.values(): data.append(f'actor "{wrap(a["name"],22)}" as A_{a["id"]}')
for a in actors.values():
    if a['parent']: data.append(f'A_{a["id"]} --|> A_{a["parent"]}')
data.append('rectangle "Cosmetic Store Chain Management — LUNEA" as SYSTEM {')
for key,name,ids,predicate in summary: data.append(f'  usecase "{wrap(name)}" as {key} #FFF8DB')
data.append('}')
for key,name,ids,predicate in summary:
    for a in ids: data.append(f'{key} -- A_{a}' if a=='PUBLIC' else f'A_{a} -- {key}')
data+=['legend bottom left', 'Các ellipse là mục tiêu tổng hợp; danh sách UC chi tiết ở overview-map.md.',
       'Actor ngoài boundary; UC trong boundary. Bốn role nhân viên chuyên biệt hóa Nhân viên.',
       'API công khai là hành vi hiện tại do thiếu kiểm tra quyền.',
       'Màu tổng quan không biểu thị trạng thái; xem sơ đồ module và đặc tả.', 'endlegend','@enduml','']
(OUT/'overview/overall-system.puml').write_text('\n'.join(data))
manifest.insert(0,dict(file='overview/overall-system.puml',title='Tổng quan hệ thống',group='overview',use_cases=[],actors=list(actors),summary=True))
overview=['# Truy vết các mục tiêu tổng hợp ở sơ đồ tổng quan', '', 'Ellipse tổng quan là các mục tiêu gom nhóm để giữ hình dễ đọc; không bổ sung use case hoặc yêu cầu mới.', '', '| Nhãn | Mục tiêu tổng hợp | UC chi tiết |', '|---|---|---|']
for key,name,ids,predicate in summary:
    overview.append(f'| {key} | {name} | '+', '.join(u['id'] for u in cases if predicate(u))+' |')
overview += ['', 'UC đổi theme là tiện ích local; xem module ui_preferences. BeautyProfile chỉ có backend được giữ trong nhóm hồ sơ với status riêng.']
(OUT/'overview/overview-map.md').write_text('\n'.join(overview)+'\n')
(OUT/'traceability/diagram-manifest.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n')
print(f'Generated {len(manifest)} PlantUML diagrams; all {len(cases)} use cases represented.')
