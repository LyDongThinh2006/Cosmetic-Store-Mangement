# Báo cáo kiểm tra

- Source đã đọc: 277 tệp; ledger thiếu 0.
- Đặc tả: 84 UC, 8 actor, 37 entity.
- Diagram: 42 PlantUML; mỗi UC có trong module diagram.
- API inventory: 95 tổ hợp HTTP method/path, kể cả alias; /api/auth/me là ngữ cảnh kỹ thuật.
- Quan hệ: 4 actor generalization; 0 include, 0 extend, 0 use-case generalization.
- Syntax PlantUML được kiểm tra trước render; SVG parse XML và PNG kiểm chữ ký/kích thước.
- Association khớp actor–UC; generalization đúng hướng subtype→parent. Actor views có đủ UC được gán.
- Source hash không đổi; liên kết file Markdown hợp lệ; UC labels trong SVG đầy đủ; không banner lỗi/deprecation.
- Đã đối chiếu UI/REST/service/DTO/repository/entity/security; trạng thái cuối đã gộp lỗi tích hợp.
- XMI không tạo vì không kiểm chứng import EA.
- Chưa chạy app/tests/DB; kiểm tra tài liệu không xác nhận nghiệp vụ production.

**Lỗi kiểm tra tự động:** 0

