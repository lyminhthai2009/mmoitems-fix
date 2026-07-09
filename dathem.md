# Các thay đổi đã được thêm vào MMOItems để đồng bộ hóa vật phẩm

Tệp này ghi nhận chi tiết toàn bộ các lớp và đoạn mã nguồn đã được tạo mới hoặc chỉnh sửa để thực hiện tính năng tự động đồng bộ chỉ số, lore của vật phẩm mà không làm lag máy chủ.

---

## 1. Các tệp tin được tạo mới

### [NEW] `MMOItems-API/src/main/java/net/Indyuce/mmoitems/api/util/TemplateAutoUpdater.java`
Lớp này chịu trách nhiệm quản lý tệp cấu hình trung gian `plugins/MMOItems/template-hashes.yml`.
* **Chức năng**:
  * Đọc/ghi mã băm (hash) và Revision ID của từng mẫu vật phẩm (template).
  * So sánh mã băm hiện tại của cấu hình template với mã băm cũ đã lưu.
  * Nếu phát hiện chỉ số thay đổi (mã băm khác nhau), tự động tăng `revision-id` của template đó lên 1 đơn vị và lưu lại.
  * Nếu không thay đổi, giữ nguyên Revision ID lớn nhất giữa cấu hình gốc và tệp hash.

---

## 2. Các tệp tin được chỉnh sửa

### [MODIFY] `MMOItems-API/src/main/java/net/Indyuce/mmoitems/api/item/template/MMOItemTemplate.java`
* **Thay đổi**: Tại constructor nạp cấu hình template từ file YAML:
  * Gọi sang lớp `TemplateAutoUpdater` để gán giá trị Revision ID tự động tăng cho biến `this.revId` thay vì đọc giá trị tĩnh cố định trong file cấu hình gốc.
  * Đoạn mã được thêm vào cuối constructor:
    ```java
    int originalRevId = config.getInt("base.revision-id", 1);
    this.revId = net.Indyuce.mmoitems.api.util.TemplateAutoUpdater.getAndUpdateRevisionId(type.getId(), id, hash, originalRevId);
    ```

### [MODIFY] `MMOItems-Dist/src/main/java/net/Indyuce/mmoitems/listener/ItemListener.java`
* **Thay đổi 1: Mở rộng quét toàn bộ 41 ô túi đồ (`updateInventory`)**
  * Thay vì chỉ quét hotbar (9 ô) và giáp khi người chơi kết nối, hàm này đã được nâng cấp để duyệt qua toàn bộ 36 ô túi đồ chính + 4 ô giáp + 1 ô tay trái (Off-hand).
* **Thay đổi 2: Trì hoãn đồng bộ khi Join Game**
  * Trì hoãn việc gọi hàm `updateInventory` đi `20 ticks` (1 giây) sau khi người chơi join để đảm bảo các plugin quản lý túi đồ khác (như AuthMe, Multiverse-Inventories) đã tải xong dữ liệu của người chơi, tránh cập nhật nhầm túi đồ trống.
* **Thay đổi 3: Đồng bộ JIT (Just-In-Time) khi mở rương, khối chứa đồ**
  * Lắng nghe sự kiện `InventoryOpenEvent` để tự động duyệt và đồng bộ hóa các vật phẩm trong hòm chứa đồ (Chest, Barrel, Shulker Box đặt dưới đất, Dispenser...) ngay khi người chơi mở giao diện.
* **Thay đổi 4: Bỏ giới hạn tương tác Click**
  * Sửa sự kiện `InventoryClickEvent` để tự động đồng bộ hóa bất kỳ vật phẩm nào khi người chơi click tương tác trong bất kỳ loại giao diện hòm đồ nào.
* **Thay đổi 5: Chống Spike Lag khi reload plugin (`/mi reload`)**
  * Lắng nghe sự kiện `MMOItemsReloadEvent`. Khi reload, sử dụng một `BukkitRunnable` để chia nhỏ danh sách người chơi online, chỉ cập nhật cho **5 người chơi mỗi tick** để dàn đều tải trọng xử lý, triệt tiêu Spike Lag cho server đông người.
