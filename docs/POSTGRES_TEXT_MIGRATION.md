# Chuyển các trường văn bản từ OID sang TEXT

Bản sửa này bỏ `@Lob` trên bảy trường trong năm entity và dùng
`@Column(columnDefinition = "text")` cho cả local và Railway.
Theo [hướng dẫn Hibernate](https://docs.jboss.org/hibernate/orm/6.4/introduction/html_single/Hibernate_Introduction.html),
`@Lob` trên PostgreSQL dùng API large object/OID, không phù hợp với cột TEXT.

| Entity | Cột PostgreSQL |
|---|---|
| AuditLogEntity | `audit_logs.content` |
| BrandEntity | `brands.description` |
| ProductEntity | `products.description`, `products.main_ingredients`, `products.benefits` |
| ReturnRequestEntity | `return_requests.description` |
| ReviewEntity | `reviews.content` |

## Kiểm tra trước khi deploy

Chạy truy vấn chỉ đọc này trên database cần triển khai:

```sql
SELECT t.table_name, t.column_name, c.data_type
FROM (VALUES
    ('audit_logs', 'content'),
    ('brands', 'description'),
    ('products', 'description'),
    ('products', 'main_ingredients'),
    ('products', 'benefits'),
    ('return_requests', 'description'),
    ('reviews', 'content')
) AS t(table_name, column_name)
LEFT JOIN information_schema.columns c
  ON c.table_schema = 'public'
 AND c.table_name = t.table_name AND c.column_name = t.column_name;
```

Nếu cả bảy cột là `text`, không cần migration. Database local đã kiểm tra ở lần sửa trước
thuộc trường hợp này. Chưa kiểm tra trực tiếp schema của Railway.

Nếu có cột `oid`, thực hiện theo thứ tự:

1. Sao lưu database kèm large objects, dừng các instance ứng dụng cũ đang ghi/đọc các cột này.
2. Chạy toàn bộ [convert-lob-oid-to-text.sql](sql/convert-lob-oid-to-text.sql) bằng SQL editor
   đang kết nối đúng database. Với `psql`, dùng `-v ON_ERROR_STOP=1 -f docs/sql/convert-lob-oid-to-text.sql`.
3. Chạy lại truy vấn trên, xác nhận bảy cột là `text`, kiểm tra nội dung mô tả tiếng Việt.
4. Deploy bản mới với `SPRING_PROFILES_ACTIVE=prod`, giữ `ddl-auto=validate`.

Script chỉ áp dụng cho schema `public`. Nó chuyển OID bằng
`convert_from(lo_get(column), 'UTF8')`, giữ NULL và chuỗi rỗng, bỏ qua cột TEXT,
và có thể chạy lại. Nếu thiếu cột, gặp kiểu không hỗ trợ, không có quyền đọc large object,
hoặc large object đã mất, giao dịch thất bại và rollback toàn bộ thay đổi.
Các large object gốc được giữ nguyên vì có thể còn nơi khác tham chiếu.

Không dùng `oid::text` hoặc để Hibernate tự đổi kiểu: kết quả có thể là mã OID dưới dạng chuỗi.
Không chạy migration đồng thời với ứng dụng cũ, và không rollback riêng code về bản dùng
`@Lob` sau migration vì schema TEXT không còn tương thích với cách đọc OID cũ.

Database mới hoàn toàn cần được khởi tạo schema trước khi dùng profile `prod` vì profile này
chỉ xác thực schema. Profile `local` với `ddl-auto=update` sẽ tạo cột TEXT ngay từ đầu.
