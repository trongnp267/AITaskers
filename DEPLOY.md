# Hướng dẫn deploy AITasker lên cloud (miễn phí, chạy 24/7)

Sau khi làm xong 3 phần dưới đây, web có địa chỉ cố định trên internet,
**máy bạn tắt vẫn chạy**. Tất cả đều dùng gói miễn phí, đăng nhập bằng GitHub,
không cần thẻ ngân hàng.

Sơ đồ: **Vercel** (frontend) → **Render** (backend) → **Neon** (database Postgres)

---

## Phần 1 — Tạo database (Neon, ~3 phút)

1. Vào https://neon.tech → **Sign up** bằng GitHub
2. **Create project** → đặt tên `aitasker` → Create
3. Ở màn hình Dashboard, bấm **Connection string** → chọn **Java** →
   copy chuỗi dạng:
   `jdbc:postgresql://ep-xxx.neon.tech/neondb?user=...&password=...&sslmode=require`
4. Tách chuỗi đó thành 3 phần và ghi lại:
   - `DATABASE_URL` = `jdbc:postgresql://ep-xxx.neon.tech/neondb?sslmode=require`
     (bỏ phần user/password ra khỏi URL)
   - `DATABASE_USER` = giá trị sau `user=`
   - `DATABASE_PASSWORD` = giá trị sau `password=`

## Phần 2 — Deploy backend (Render, ~5 phút)

1. Vào https://render.com → **Sign up** bằng GitHub
2. **New → Web Service** → **Connect** repo `trongnp267/AITaskers`
   (chọn branch `vothanhhoang`)
3. Cấu hình:
   - **Language/Runtime**: Docker (Render tự nhận Dockerfile ở gốc repo)
   - **Instance type**: Free
4. Mục **Environment Variables**, thêm:
   | Tên | Giá trị |
   |---|---|
   | `SPRING_PROFILES_ACTIVE` | `cloud` |
   | `DATABASE_URL` | (từ Phần 1) |
   | `DATABASE_USER` | (từ Phần 1) |
   | `DATABASE_PASSWORD` | (từ Phần 1) |
   | `JWT_SECRET` | chuỗi ngẫu nhiên dài ~64 ký tự tự đặt |
   | `AI_GEMINI_API_KEY` | key Gemini của bạn (bỏ trống nếu không dùng AI) |
5. **Deploy** → đợi build xong (~5-10 phút lần đầu) → ghi lại địa chỉ backend,
   dạng `https://aitaskers-xxxx.onrender.com`
6. Kiểm tra: mở `https://aitaskers-xxxx.onrender.com/api/jobs` — thấy `[]`
   hoặc danh sách JSON là backend sống. Tài khoản `admin/123456` được tự tạo.

## Phần 3 — Deploy frontend (Vercel, ~3 phút)

1. Vào https://vercel.com → **Sign up** bằng GitHub
2. **Add New → Project** → Import repo `trongnp267/AITaskers`
3. Cấu hình:
   - **Root Directory**: `frontend`
   - **Framework**: Next.js (tự nhận)
   - **Environment Variables**: thêm `BACKEND_URL` = địa chỉ backend Phần 2
     (ví dụ `https://aitaskers-xxxx.onrender.com`)
4. **Deploy** → xong sẽ có link dạng `https://aitaskers.vercel.app`

**Link Vercel đó chính là web chính thức** — gửi cho bất kỳ ai, chạy 24/7.

---

## Lưu ý gói miễn phí

- **Render free**: backend "ngủ" sau 15 phút không ai dùng; người đầu tiên
  mở web sẽ đợi ~30-60 giây cho nó thức dậy. Bình thường với demo đồ án.
- **Neon free**: database ngủ tương tự, tự thức khi có truy vấn.
- Dữ liệu trên cloud là MỚI (trống) — dữ liệu cũ nằm trong SQL Server trên
  máy bạn không tự chuyển lên. Đăng ký/đăng tin lại trên web cloud, hoặc
  nhờ Claude chạy lại script seed dữ liệu mẫu trỏ vào backend cloud.
- Đổi mật khẩu admin sau khi deploy (mặc định 123456).
