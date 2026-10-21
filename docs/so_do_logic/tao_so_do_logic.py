#!/usr/bin/env python3
"""Sinh sơ đồ PK/FK từ JPA entity, xuất SVG/PNG/PDF/draw.io và bản PDF báo cáo.

Chạy từ thư mục gốc: python3 docs/so_do_logic/tao_so_do_logic.py
Yêu cầu: PyMuPDF (fitz). Không sửa entity hoặc PDF báo cáo gốc.
"""

from __future__ import annotations

import hashlib
import heapq
import html
import json
import math
import re
from collections import Counter, defaultdict
from pathlib import Path
import xml.etree.ElementTree as ET

import fitz


ROOT = Path(__file__).resolve().parents[2]
OUT = Path(__file__).resolve().parent
ENTITY_DIR = ROOT / 'src/main/java/com/thinh/cosmetic/domain/entity'
REPORT = ROOT / 'docs/Nhom01_BaoCaoCuoiKy_review.pdf'
PATCHED = ROOT / 'docs/Nhom01_BaoCaoCuoiKy_review_so_do_logic_da_sua.pdf'
W, H, GRID = 1960, 1320, 10
FONT_PATHS = [
    '/System/Library/Fonts/Supplemental/Arial.ttf',
    '/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf',
    'C:/Windows/Fonts/arial.ttf',
]
FONT_FILE = next((p for p in FONT_PATHS if Path(p).exists()), None)
if not FONT_FILE:
    raise RuntimeError('Cần phông Unicode Arial hoặc DejaVu Sans.')
FONT = fitz.Font(fontfile=FONT_FILE)

NAMES = {
    'accounts': 'Tài khoản', 'customers': 'Khách hàng',
    'customer_addresses': 'Địa chỉ khách hàng', 'beauty_profiles': 'Hồ sơ làm đẹp',
    'password_reset_otps': 'OTP khôi phục mật khẩu',
    'registration_otps': 'OTP đăng ký', 'employees': 'Nhân viên',
    'employee_roles': 'Nhân viên – Vai trò', 'employee_stores': 'Nhân viên – Cửa hàng',
    'roles': 'Vai trò', 'permissions': 'Quyền', 'role_permissions': 'Vai trò – Quyền',
    'audit_logs': 'Nhật ký thao tác', 'brands': 'Thương hiệu',
    'categories': 'Danh mục', 'products': 'Sản phẩm', 'skus': 'Biến thể sản phẩm',
    'attributes': 'Thuộc tính', 'product_attribute_values': 'Giá trị thuộc tính sản phẩm',
    'sku_attribute_values': 'Giá trị thuộc tính SKU', 'product_images': 'Hình ảnh sản phẩm',
    'wishlists': 'Danh sách yêu thích', 'wishlist_items': 'Chi tiết yêu thích',
    'carts': 'Giỏ hàng', 'cart_items': 'Chi tiết giỏ hàng', 'orders': 'Đơn hàng',
    'order_items': 'Chi tiết đơn hàng', 'order_reservations': 'Giữ tồn đơn hàng',
    'return_requests': 'Yêu cầu trả hàng', 'return_items': 'Chi tiết trả hàng',
    'reviews': 'Đánh giá', 'stores': 'Cửa hàng', 'suppliers': 'Nhà cung cấp',
    'inventories': 'Tồn kho', 'inventory_adjustments': 'Điều chỉnh tồn',
    'purchase_orders': 'Phiếu nhập hàng', 'purchase_order_items': 'Chi tiết phiếu nhập',
    'stock_transfers': 'Phiếu điều chuyển', 'stock_transfer_items': 'Chi tiết điều chuyển',
}

PAGE1 = {
    'accounts': (0, 0), 'customers': (1, 0), 'brands': (2, 0),
    'categories': (3, 0), 'attributes': (4, 0),
    'password_reset_otps': (0, 1), 'customer_addresses': (1, 1),
    'products': (3, 1), 'product_attribute_values': (4, 1),
    'registration_otps': (0, 2), 'beauty_profiles': (1, 2), 'orders': (2, 2),
    'product_images': (4, 2), 'wishlists': (0, 3), 'carts': (1, 3),
    'cart_items': (2, 3), 'skus': (3, 3), 'sku_attribute_values': (4, 3),
    'wishlist_items': (0, 4), 'return_requests': (1, 4), 'order_items': (2, 4),
    'order_reservations': (3, 4), 'employees': (0, 5), 'return_items': (1, 5),
    'reviews': (2, 5), 'stores': (3, 5),
}
PAGE2 = {
    'accounts': (0, 0), 'employees': (1, 0), 'employee_roles': (2, 0),
    'roles': (3, 0), 'permissions': (4, 0), 'audit_logs': (0, 1),
    'employee_stores': (1, 1), 'stores': (2, 1), 'role_permissions': (3, 1),
    'skus': (4, 1), 'suppliers': (0, 2), 'purchase_orders': (1, 2),
    'inventories': (2, 2), 'inventory_adjustments': (3, 2),
    'stock_transfers': (4, 2), 'purchase_order_items': (1, 3),
    'stock_transfer_items': (3, 3),
}
SPECS = [
    dict(stem='01_khach_hang_san_pham_don_hang', title='NHÓM KHÁCH HÀNG, SẢN PHẨM VÀ ĐƠN HÀNG',
         layout=PAGE1, refs={'employees', 'stores'}, pitch=180),
    dict(stem='02_van_hanh_chuoi_phan_quyen', title='NHÓM VẬN HÀNH CHUỖI VÀ PHÂN QUYỀN',
         layout=PAGE2, refs={'accounts', 'skus'}, pitch=260),
]


def snake(s):
    return re.sub(r'(?<!^)(?=[A-Z])', '_', s).lower()


def value(annotation, key, default=None):
    m = re.search(r'\b' + key + r'\s*=\s*("(?:\\.|[^"\\])*"|true|false|\d+)', annotation)
    if not m:
        return default
    token = m.group(1)
    if token.startswith('"'):
        return json.loads(token).strip('"')
    return {'true': True, 'false': False}.get(token, token)


def extract_schema():
    tables, by_class = {}, {}
    raw_joins = 0
    for f in sorted(ENTITY_DIR.rglob('*.java')):
        text = f.read_text()
        if not re.search(r'@Entity\b', text):
            continue
        table_name = re.search(r'@Table\s*\(\s*name\s*=\s*"([^"]+)"', text).group(1)
        class_name = re.search(r'public class (\w+)', text).group(1)
        table = dict(name=table_name, label=NAMES[table_name], entity=class_name,
                     source=str(f.relative_to(ROOT)), columns=[], fks=[])
        pending = []
        for line_no, line in enumerate(text.splitlines(), 1):
            s = line.strip()
            if s.startswith('public class '):
                pending = []
            elif s.startswith('@'):
                pending.append(s)
            else:
                m = re.match(r'private\s+([\w<>]+)\s+(\w+)\s*(?:=|;)', s)
                if not m:
                    continue
                java_type, field = m.groups()
                ann = '\n'.join(pending)
                pending = []
                if '@OneToMany' in ann or '@Transient' in ann:
                    continue
                join = next((a for a in ann.splitlines() if a.startswith('@JoinColumn')), None)
                column = next((a for a in ann.splitlines() if a.startswith('@Column')), '')
                pk = bool(re.search(r'@Id\b', ann))
                one = '@OneToOne' in ann
                record = dict(field=field, name=value(join or column, 'name', snake(field)),
                              java_type=java_type, pk=pk, fk=bool(join),
                              nullable=False if pk else value(join or column, 'nullable', True),
                              unique=bool(one or value(join or column, 'unique', False)), line=line_no)
                table['columns'].append(record)
                if pk:
                    table['pk'] = record['name']
                if join:
                    raw_joins += 1
                    record['target_entity'] = java_type
                    record['target_column'] = value(join, 'referencedColumnName', 'id')
                    record['one_to_one'] = one
                    table['fks'].append(record)
        assert 'pk' in table, class_name
        assert table_name not in tables
        tables[table_name] = table
        by_class[class_name] = table_name
    edges = []
    for table in tables.values():
        for fk in table['fks']:
            target = by_class[fk['target_entity']]
            fk['target_table'] = target
            assert fk['target_column'] == tables[target]['pk']
            edges.append(dict(id=f"{table['name']}.{fk['name']}", source=table['name'],
                              target=target, column=fk['name'], target_column=fk['target_column'],
                              nullable=fk['nullable'], unique=fk['unique'],
                              source_range='0..1' if fk['unique'] else '0..N',
                              target_range='0..1' if fk['nullable'] else '1',
                              file=table['source'], line=fk['line']))
    assert len(edges) == raw_joins
    assert len(tables) == 39, 'Số bảng đã thay đổi; cần rà soát lại bố cục.'
    assert len(edges) == 59, 'Số quan hệ đã thay đổi; cần rà soát lại bố cục.'
    return tables, edges


def make_boxes(spec, tables):
    boxes = {}
    for name, (c, r) in spec['layout'].items():
        fk_fields = [] if name in spec['refs'] else tables[name]['fks']
        boxes[name] = dict(x=60+c*380, y=120+r*spec['pitch'], w=300,
                           h=70+20*len(fk_fields), fks=fk_fields, ref=name in spec['refs'])
    return boxes


def color(table):
    if table in {'accounts', 'customers', 'customer_addresses', 'beauty_profiles',
                 'password_reset_otps', 'registration_otps', 'employees', 'employee_roles',
                 'roles', 'permissions', 'role_permissions', 'employee_stores', 'audit_logs'}:
        return '#eaf0ff', '#345486'
    if table in {'brands', 'categories', 'products', 'skus', 'attributes',
                 'product_images', 'product_attribute_values', 'sku_attribute_values'}:
        return '#f0eafb', '#6b428b'
    return '#eaf5ef', '#38634d'


def inside(p, box, pad=0):
    return box['x']-pad < p[0] < box['x']+box['w']+pad and box['y']-pad < p[1] < box['y']+box['h']+pad


def simplify(points):
    out = []
    for p in points:
        if out and p == out[-1]:
            continue
        if len(out) > 1:
            a, b = out[-2:]
            if (a[0] == b[0] == p[0]) or (a[1] == b[1] == p[1]):
                out[-1] = p
                continue
        out.append(p)
    return out


def route(spec, boxes, edges):
    blocked = set()
    for box in boxes.values():
        for gx in range(math.floor((box['x']-8)/GRID), math.ceil((box['x']+box['w']+8)/GRID)+1):
            for gy in range(math.floor((box['y']-8)/GRID), math.ceil((box['y']+box['h']+8)/GRID)+1):
                blocked.add((gx, gy))
    used = Counter()
    routes = []
    mine = [e for e in edges if e['source'] in boxes and e['source'] not in spec['refs']]
    assert all(e['target'] in boxes for e in mine)

    def path(start, end):
        a, z = (int(start[0]/GRID), int(start[1]/GRID)), (int(end[0]/GRID), int(end[1]/GRID))
        heap = [(0, 0, a[0], a[1], -1)]
        costs, prev = {(a[0], a[1], -1): 0}, {}
        found = None
        while heap:
            _, cost, x, y, direction = heapq.heappop(heap)
            state = x, y, direction
            if cost > costs.get(state, float('inf')):
                continue
            if (x, y) == z:
                found = state
                break
            for nd, (dx, dy) in enumerate([(1,0),(0,1),(-1,0),(0,-1)]):
                xx, yy = x+dx, y+dy
                if not (2 <= xx <= W/GRID-2 and 10 <= yy <= (H-125)/GRID):
                    continue
                if (xx, yy) in blocked and (xx, yy) not in (a, z):
                    continue
                segment = tuple(sorted(((x,y),(xx,yy))))
                extra = 1 + (1.8 if direction >= 0 and direction != nd else 0) + used[segment]*3.0
                nc = cost+extra
                ns = xx, yy, nd
                if nc >= costs.get(ns, float('inf')):
                    continue
                costs[ns], prev[ns] = nc, state
                heuristic = abs(xx-z[0])+abs(yy-z[1])
                heapq.heappush(heap, (nc+heuristic,nc,xx,yy,nd))
        if found is None:
            raise RuntimeError(f'Không tìm được đường nối {start} -> {end}')
        states = [found]
        while states[-1] in prev:
            states.append(prev[states[-1]])
        states.reverse()
        for p, q in zip(states, states[1:]):
            used[tuple(sorted((p[:2],q[:2])))] += 1
        return [(s[0]*GRID,s[1]*GRID) for s in states]

    # Ưu tiên các quan hệ ngắn để tránh đường nối dài chiếm hành lang cục bộ.
    for e in sorted(mine, key=lambda e: (abs(boxes[e['source']]['x']-boxes[e['target']]['x']) +
                                         abs(boxes[e['source']]['y']-boxes[e['target']]['y']), e['id'])):
        s, t = boxes[e['source']], boxes[e['target']]
        fk_idx = [f['name'] for f in s['fks']].index(e['column'])
        sy, ty = s['y']+80+fk_idx*20, t['y']+60
        if s['x'] != t['x']:
            sr = t['x'] > s['x']
            tr = not sr
        else:
            sr = tr = (s['x'] < W/2)
        sx = s['x']+s['w'] if sr else s['x']
        tx = t['x']+t['w'] if tr else t['x']
        a, z = (sx+(20 if sr else -20),sy), (tx+(20 if tr else -20),ty)
        points = simplify([(sx,sy),*path(a,z),(tx,ty)])
        routes.append(dict(**e, points=points, source_right=sr, target_right=tr))
    return routes


def validate_routes(boxes, routes):
    for e in routes:
        assert e['points'][0][1] > boxes[e['source']]['y']+70
        assert e['points'][-1][1] == boxes[e['target']]['y']+60
        for a, b in zip(e['points'], e['points'][1:]):
            assert a[0] == b[0] or a[1] == b[1], e['id']
            count = max(1, int(max(abs(a[0]-b[0]),abs(a[1]-b[1]))/2))
            for k in range(1,count):
                p = a[0]+(b[0]-a[0])*k/count, a[1]+(b[1]-a[1])*k/count
                assert not any(inside(p,box) for box in boxes.values()), (e['id'],p)


class Canvas:
    def __init__(self, page):
        self.page = page
        self.svg = [f'<svg xmlns="http://www.w3.org/2000/svg" width="{W}" height="{H}" viewBox="0 0 {W} {H}">',
                    '<rect width="100%" height="100%" fill="white"/>']
        self.page.insert_font(fontname='LuneaUnicode', fontfile=FONT_FILE)

    @staticmethod
    def rgb(c):
        return tuple(int(c[i:i+2],16)/255 for i in (1,3,5))

    def rect(self,x,y,w,h,fill='#ffffff',stroke='#7f8b96',dash=False):
        self.svg.append(f'<rect x="{x}" y="{y}" width="{w}" height="{h}" fill="{fill}" stroke="{stroke}" stroke-width="1.5"'+(' stroke-dasharray="7 4"' if dash else '')+'/>')
        shape=self.page.new_shape();shape.draw_rect(fitz.Rect(x,y,x+w,y+h))
        shape.finish(color=self.rgb(stroke),fill=self.rgb(fill),width=1.5,dashes='[7 4] 0' if dash else None);shape.commit()

    def text(self,x,y,txt,size=16,color='#172b3a',bold=False,center=False):
        width=FONT.text_length(txt,fontsize=size)
        if center:x-=width/2
        self.svg.append(f'<text x="{x}" y="{y}" font-family="Arial,DejaVu Sans,sans-serif" font-size="{size}" fill="{color}"'+(' font-weight="bold"' if bold else '')+f'>{html.escape(txt)}</text>')
        self.page.insert_text((x,y),txt,fontname='LuneaUnicode',fontsize=size,color=self.rgb(color))

    def line(self,pts,color='#53616c',width=1.5,dash=False,arrow=False,halo=False):
        path=' '.join(f'{x},{y}' for x,y in pts)
        if halo:
            self.svg.append(f'<polyline points="{path}" fill="none" stroke="white" stroke-width="5"/>')
            self.line_pdf(pts,'#ffffff',5,False)
        self.svg.append(f'<polyline points="{path}" fill="none" stroke="{color}" stroke-width="{width}"'+(' stroke-dasharray="6 4"' if dash else '')+'/>')
        self.line_pdf(pts,color,width,dash)
        if arrow:
            x,y=pts[-1];px,py=pts[-2];dx,dy=x-px,y-py;length=math.hypot(dx,dy);ux,uy=dx/length,dy/length
            tri=[(x,y),(x-ux*10-uy*4.5,y-uy*10+ux*4.5),(x-ux*10+uy*4.5,y-uy*10-ux*4.5)]
            self.svg.append('<polygon points="'+' '.join(f'{a},{b}' for a,b in tri)+f'" fill="{color}"/>')
            sh=self.page.new_shape();sh.draw_polyline([fitz.Point(*p) for p in tri]);sh.finish(color=self.rgb(color),fill=self.rgb(color),closePath=True);sh.commit()

    def line_pdf(self,pts,color,width,dash):
        sh=self.page.new_shape();sh.draw_polyline([fitz.Point(*p) for p in pts]);sh.finish(color=self.rgb(color),width=width,closePath=False,dashes='[6 4] 0' if dash else None);sh.commit()


def draw_page(pdf,spec,boxes,routes,tables):
    page=pdf.new_page(width=W,height=H);c=Canvas(page)
    c.text(60,44,spec['title'],27,bold=True)
    c.text(60,77,'Đối chiếu JPA entity • Đầu mũi tên đặt tại PK được tham chiếu • Chỉ hiển thị cột khóa',16)
    # Vẽ đường nối trước các khối, điểm nối luôn kết thúc đúng hàng id của bảng đích.
    for e in routes:
        _,stroke=color(e['target'])
        c.line(e['points'],color=stroke,width=1.6,dash=e['nullable'],arrow=True,halo=True)
    for name,b in boxes.items():
        fill,stroke=color(name)
        if b['ref']:fill,stroke='#f2f3f4','#7b858c'
        c.rect(b['x'],b['y'],b['w'],b['h'],stroke=stroke,dash=b['ref'])
        c.rect(b['x'],b['y'],b['w'],50,fill=fill,stroke=stroke,dash=b['ref'])
        label=tables[name]['label']+(' [TC]' if b['ref'] else '')
        size=min(18,276/FONT.text_length(label,fontsize=1))
        c.text(b['x']+12,b['y']+23,label,size,bold=True)
        c.text(b['x']+12,b['y']+42,name,14,color='#4b5863')
        c.text(b['x']+12,b['y']+65,'PK  '+tables[name]['pk'],15,bold=True)
        for n,fk in enumerate(b['fks']):
            label='FK  '+fk['name']+(' [UQ]' if fk['unique'] else '')+(' ?' if fk['nullable'] else '')
            assert FONT.text_length(label,fontsize=14) < b['w']-20, (name,label)
            c.text(b['x']+12,b['y']+85+n*20,label,14)
    # Đặt một nhãn bội số cho từng FK; tránh nhãn chồng nhau khi nhiều FK cùng tới một PK.
    for e in routes:
        pt=e['points'][0]
        label=e['source_range']+' : '+e['target_range']
        size=10;tw=FONT.text_length(label,fontsize=size)
        x=pt[0]+5 if e['source_right'] else pt[0]-tw-5
        y=pt[1]-6
        c.rect(x-2,y-10,tw+4,13,fill='#ffffff',stroke='#ffffff')
        c.text(x,y,label,size,color='#263746')
    c.line([(60,1210),(1900,1210)],color='#a9b0b6',width=1)
    c.text(60,1241,'Quy ước: FK → PK. Nét đứt / dấu ?: FK cho phép NULL. UQ: duy nhất (gồm ánh xạ @OneToOne).',17)
    c.text(60,1271,'Nhãn cạnh FK = số bản ghi con mỗi cha : số cha mỗi con. Ví dụ 0..N : 1; 0..1 : 1.',16)
    owned=len(boxes)-len(spec['refs'])
    c.text(60,1301,f'[TC]: cùng bảng được vẽ lại để tham chiếu; không tạo bảng mới. {owned} bảng chính, {len(routes)} quan hệ. Giao cắt không phải điểm nối.',16)
    (OUT/(spec['stem']+'.svg')).write_text('\n'.join(c.svg+['</svg>']))
    page.get_pixmap(matrix=fitz.Matrix(1.5,1.5),alpha=False).save(OUT/(spec['stem']+'.png'))


def add_cell(container,**kw):
    return ET.SubElement(container,'mxCell', {k:str(v) for k,v in kw.items()})


def make_drawio(all_pages,tables):
    root=ET.Element('mxfile',host='app.diagrams.net',version='24.7.17')
    for idx,(spec,boxes,routes) in enumerate(all_pages,1):
        dia=ET.SubElement(root,'diagram',id=f'logic-{idx}',name=spec['title'])
        model=ET.SubElement(dia,'mxGraphModel',dx=str(W),dy=str(H),grid='1',gridSize='10',page='1',pageWidth=str(W),pageHeight=str(H))
        rt=ET.SubElement(model,'root');add_cell(rt,id='0');add_cell(rt,id='1',parent='0')
        def text_cell(identifier,txt,x,y,w,h,size=16):
            cell=add_cell(rt,id=identifier,value=txt,style=f'text;html=1;align=left;verticalAlign=middle;fontFamily=Arial;fontSize={size};',vertex='1',parent='1')
            ET.SubElement(cell,'mxGeometry',x=str(x),y=str(y),width=str(w),height=str(h),**{'as':'geometry'})
        text_cell('title',html.escape(spec['title']),60,10,1840,45,27)
        text_cell('subtitle','Mũi tên: FK → PK. Cột khóa lấy từ entity hiện tại.',60,52,1840,35)
        for name,b in boxes.items():
            fill,stroke=color(name)
            if b['ref']:fill,stroke='#f2f3f4','#7b858c'
            keys=['<b>PK '+html.escape(tables[name]['pk'])+'</b>']
            for f in b['fks']:
                keys.append('FK '+html.escape(f['name'])+(' [UQ]' if f['unique'] else '')+(' ?' if f['nullable'] else ''))
            title=html.escape(tables[name]['label']+(' [TC]' if b['ref'] else ''))
            body=f'<div style="font-size:18px"><b>{title}</b></div><div style="font-size:14px">{name}</div><hr style="margin:6px 0"/>'+'<br/>'.join(keys)
            cell=add_cell(rt,id=name,value=body,style=f'rounded=0;whiteSpace=wrap;html=1;align=left;verticalAlign=top;spacing=10;fillColor={fill};strokeColor={stroke};fontFamily=Arial;fontSize=14;'+('dashed=1;' if b['ref'] else ''),vertex='1',parent='1')
            ET.SubElement(cell,'mxGeometry',x=str(b['x']),y=str(b['y']),width=str(b['w']),height=str(b['h']),**{'as':'geometry'})
        for n,e in enumerate(routes):
            s,t=boxes[e['source']],boxes[e['target']]
            sy=(e['points'][0][1]-s['y'])/s['h'];ty=(e['points'][-1][1]-t['y'])/t['h']
            _,stroke=color(e['target'])
            style=f'edgeStyle=none;html=1;rounded=0;startArrow=none;endArrow=block;endFill=1;strokeColor={stroke};strokeWidth=1.6;exitX={1 if e["source_right"] else 0};exitY={sy};entryX={1 if e["target_right"] else 0};entryY={ty};exitPerimeter=0;entryPerimeter=0;'+('dashed=1;' if e['nullable'] else '')
            edge=add_cell(rt,id=f'edge-{n}',value='',style=style,edge='1',source=e['source'],target=e['target'],parent='1')
            geometry=ET.SubElement(edge,'mxGeometry',relative='1',**{'as':'geometry'})
            array=ET.SubElement(geometry,'Array',**{'as':'points'})
            for x,y in e['points'][1:-1]:ET.SubElement(array,'mxPoint',x=str(x),y=str(y))
            for suffix,val,relx in [('from',e['source_range']+' : '+e['target_range'],-.85)]:
                lab=add_cell(rt,id=f'edge-{n}-{suffix}',value=val,style='edgeLabel;html=1;align=center;verticalAlign=middle;fontFamily=Arial;fontSize=11;labelBackgroundColor=#ffffff;',vertex='1',connectable='0',parent=f'edge-{n}')
                g=ET.SubElement(lab,'mxGeometry',x=str(relx),y='-10',relative='1',**{'as':'geometry'});ET.SubElement(g,'mxPoint',**{'as':'offset'})
        text_cell('legend','FK → PK; nét đứt: FK có thể NULL; UQ: duy nhất; [TC]: bảng tham chiếu từ hình còn lại.',60,1220,1840,35,17)
        text_cell('legend-2','Nhãn cạnh FK = số con mỗi cha : số cha mỗi con. Giao cắt không phải điểm nối.',60,1260,1840,35)
    path=OUT/'LUNEA_so_do_logic.drawio';ET.indent(root);ET.ElementTree(root).write(path,encoding='utf-8',xml_declaration=True)
    check=ET.parse(path)
    actual=[]
    for model in check.findall('.//mxGraphModel'):
        cells={c.get('id'):c for c in model.findall('./root/mxCell')}
        for c in cells.values():
            if c.get('edge')=='1':
                assert 'startArrow=none;' in c.get('style') and 'endArrow=block;' in c.get('style')
                actual.append((c.get('source'),c.get('target')))
    expected=[(e['source'],e['target']) for _,_,rs in all_pages for e in rs]
    assert Counter(actual)==Counter(expected)


def write_notes(tables,edges):
    lines=['# Sơ đồ logic LUNEA đã đối chiếu mã nguồn', '',
           'Cập nhật ngày 20/10/2026 theo entity hiện tại trong workspace. Sơ đồ thể hiện 39 bảng và 59 khóa ngoại; không suy ra cấu trúc từ tên property hoặc từ DTO.', '',
           '## Tệp kết quả', '',
           '- `LUNEA_so_do_logic.pdf`: hai sơ đồ vector, phóng to vẫn đọc được chữ.',
           '- `LUNEA_so_do_logic.drawio`: hai trang chỉnh sửa được trong diagrams.net.',
           '- `01_*.svg`, `02_*.svg`: ảnh vector; các tệp PNG tương ứng dùng để chèn Word.',
           '- `../Nhom01_BaoCaoCuoiKy_review_so_do_logic_da_sua.pdf`: bản sao báo cáo đã thay hai hình tại trang PDF 174 và 175.',
           '- `schema_from_entities.json`: bảng, cột và FK lấy từ entity; gồm cả cột thông thường không vẽ trong sơ đồ.', '',
           '## Hướng mũi tên và bội số', '',
           '**FK → PK**: đầu mũi tên nằm tại hàng `id` của bảng được tham chiếu. Ví dụ `orders.fulfillment_store_id → stores.id`.', '',
           'Nét liền: FK không cho NULL. Nét đứt và dấu `?`: FK cho NULL. `UQ`: duy nhất, bao gồm quan hệ được ánh xạ `@OneToOne`. Mỗi FK có nhãn `số con mỗi cha : số cha mỗi con`, ví dụ `0..N : 1` hoặc `0..1 : 1`. Nhãn được đặt cạnh nguồn FK để các quan hệ cùng tham chiếu một PK không chồng chữ.', '',
           'Khối `[TC]` chỉ lặp lại cùng bảng để thể hiện quan hệ giữa hai nhóm, không phải bảng mới. Đường giao nhau không có nghĩa là kết nối. Sơ đồ chỉ hiển thị PK/FK để đọc được quan hệ; JSON chứa danh sách đầy đủ các cột.', '',
           '## Những điểm đã sửa so với hình cũ', '',
           '1. Thêm hướng tham chiếu và cột PK/FK cho mọi đường nối; không dùng đường liên kết vô hướng.',
           '2. Bổ sung `accounts`, `password_reset_otps`, `registration_otps` và `product_attribute_values` theo cài đặt hiện tại. `registration_otps.email` là dữ liệu xác thực trước đăng ký, không phải FK tới `accounts.email`.',
           '3. Không vẽ bảng Voucher hay Chương trình khuyến mãi như bảng đã cài đặt: source chưa có entity tương ứng. Ba mã ưu đãi hiện được xử lý trong service; `voucherCode` trong DTO không tạo FK.',
           '4. `order_reservations` tham chiếu `orders`, `skus`, `stores`; không tham chiếu trực tiếp `inventories`.',
           '5. `return_items.order_item_id` tham chiếu `order_items`; `return_requests` còn tham chiếu khách hàng và nhân viên xử lý.',
           '6. `reviews` có FK tới khách hàng, sản phẩm, đơn và chi tiết đơn; `order_item_id` được ánh xạ `@OneToOne`, còn `order_id` và `order_item_id` cho phép NULL ở mức mapping.',
           '7. Phiếu điều chuyển có hai FK riêng tới cửa hàng: `from_store_id` và `to_store_id`; đồng thời có `employee_id` tới người lập.',
           '8. Thêm quan hệ tự tham chiếu `categories.parent_id → categories.id`; thêm liên kết SKU tùy chọn của `product_images`.',
           '9. Các bảng `employee_roles`, `role_permissions`, `employee_stores` dùng PK `id` theo entity; không gán nhầm khóa chính ghép chỉ vì có các lớp `*Id` chưa được ánh xạ.',
           '10. `orders` lưu snapshot địa chỉ và dữ liệu nhận hàng; không có FK `customer_address_id` trong entity. Giá, tên sản phẩm trong chi tiết đơn cũng là snapshot.', '',
           '## Phân biệt mapping và quy tắc nghiệp vụ', '',
           'Sơ đồ bám mapping JPA hiện tại. Không tự bổ sung UNIQUE(store_id, sku_id) cho tồn kho hoặc UNIQUE cho các cặp ở bảng nối vì entity chưa khai báo các ràng buộc đó. Quy tắc một tài khoản chỉ thuộc một loại khách/nhân viên, chống trả vượt số lượng và giữ tồn đúng chi nhánh là quy tắc cần kiểm tra thêm ở tầng nghiệp vụ; không thể biểu diễn đầy đủ bằng riêng các FK.', '',
           'Bản PDF báo cáo mới chỉ thay hai hình; phần mô tả chi tiết 37 bảng ở các trang sau vẫn là thiết kế cũ và cần đồng bộ riêng nếu báo cáo được chuyển hoàn toàn sang mô tả 39 bảng đã cài đặt.', '',
           '## Danh sách khóa ngoại đã đối chiếu', '',
           '| STT | Cột FK (nguồn mũi tên) | PK được tham chiếu (đầu mũi tên) | Số con mỗi cha | Số cha mỗi con | Entity |',
           '|---|---|---|---|---|---|']
    for i,e in enumerate(sorted(edges,key=lambda e:e['id']),1):
        lines.append(f"| {i} | `{e['id']}` | `{e['target']}.{e['target_column']}` | {e['source_range']} | {e['target_range']} | `{Path(e['file']).name}:{e['line']}` |")
    lines += ['', '## Tái tạo', '', '```bash', 'python3 docs/so_do_logic/tao_so_do_logic.py', '```', '',
              'Script đọc lại entity, kiểm tra đủ bảng/FK và đúng hướng source/target, kiểm tra đường nối không xuyên khối bảng, sau đó xuất lại các tệp. Khi schema thay đổi, cần rà soát bố cục và số lượng mong đợi trong script.']
    (OUT/'README.md').write_text('\n'.join(lines)+'\n')


def patch_report(diagrams):
    original_hash=hashlib.sha256(REPORT.read_bytes()).hexdigest()
    src=fitz.open(REPORT)
    for idx,pno in enumerate([173,174]):
        page=src[pno]
        images=page.get_images(full=True)
        assert len(images)==1
        for rect in page.get_image_rects(images[0][0]):page.add_redact_annot(rect,fill=(1,1,1))
        page.apply_redactions(images=fitz.PDF_REDACT_IMAGE_REMOVE,graphics=0,text=0)
        rect=fitz.Rect(85,278.10,565,604.70) if idx==0 else fitz.Rect(85,70.90,565,380.60)
        page.show_pdf_page(rect,diagrams,idx,keep_proportion=True)
    src.save(PATCHED,garbage=4,deflate=True)
    check=fitz.open(PATCHED);original=fitz.open(REPORT)
    assert len(check)==len(original)==204
    for i in range(len(check)):
        if i not in [173,174]:assert check[i].get_text()==original[i].get_text(),i
    assert hashlib.sha256(REPORT.read_bytes()).hexdigest()==original_hash


def main():
    OUT.mkdir(exist_ok=True)
    tables,edges=extract_schema()
    (OUT/'schema_from_entities.json').write_text(json.dumps(dict(tables=tables,foreign_keys=edges),ensure_ascii=False,indent=2)+'\n')
    pdf=fitz.open();all_pages=[];represented=[]
    for spec in SPECS:
        boxes=make_boxes(spec,tables);routes=route(spec,boxes,edges)
        validate_routes(boxes,routes)
        draw_page(pdf,spec,boxes,routes,tables)
        represented.extend(e['id'] for e in routes)
        all_pages.append((spec,boxes,routes))
        print(spec['stem'],len(boxes),'blocks',len(routes),'FK arrows')
    assert Counter(represented)==Counter(e['id'] for e in edges)
    assert set().union(*(set(s['layout']) for s in SPECS))==set(tables)
    pdf.save(OUT/'LUNEA_so_do_logic.pdf',garbage=4,deflate=True)
    make_drawio(all_pages,tables)
    write_notes(tables,edges)
    patch_report(pdf)
    summary=dict(tables=len(tables),foreign_keys=len(edges),pages=2,
                 arrow_convention='FK -> referenced PK',every_fk_exactly_once=True,
                 drawio_source_target_verified=True,no_edges_through_table_interiors=True,
                 original_report_preserved=True,unchanged_report_pages=202)
    (OUT/'validation.json').write_text(json.dumps(summary,ensure_ascii=False,indent=2)+'\n')
    print(json.dumps(summary,ensure_ascii=False))
    print('Updated report:',PATCHED)


if __name__=='__main__':
    main()
