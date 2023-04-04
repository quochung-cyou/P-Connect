 Kế hoạch:
 1. Hệ thống đăng nhập (Xong)
 2. Trang Profile (Xong)
 3. Trang Tin Tức (Xong)
 4. Nhắn tin và kết nối qua mã QR (Xong)
 5. Tinder kết nối về nhắn tin 
 6. Review quán ăn Ao Sen
 
 
 Công nghệ và các vấn đề khó khăn đã làm:
 - Thiết kế giao diện theo guideline Material, căn lề tự động responsive trên đa số thiết bị, đã kiểm tra trên máy thật từ Android 9
 - Bubble Navigation + View Pager để di chuyển trên MainActivity và activity qr mượt mà, ngoài ra có tinh chỉnh lại ViewPager Transform để tạo hiệu ứng vuốt mượt hơn.
 - MotionToast để tạo thông báo có hình ảnh đa chiều và trực quan hơn thay vì Toast mặc định của Android
 - Particle để tạo hiệu ứng hình ảnh particle ở phần login/register
 - ProgressWheelView để tạo ui ảnh đang tải lên tại phần tải avatar
 - Lottie để tạo nhiều hiệu ứng chờ khi đang load dữ liệu 
 - Glide sử dụng cùng cache để cache lại ảnh và tải ảnh nhanh hơn, một số chỗ dùng để khởi về Bitmap
 - Retrofit2 để lấy dữ liệu bài viết
 - Pretty Time để format lại thời gian mạch lạc, theo locale tiếng việt
 - Jsoup để cào dữ liệu ảnh từ og:image qua html do api mới không có ảnh mặc định thumbnail cho một số bài viết. Do tốc độ cào chậm nên sử dụng thêm đa luồng và gọi lại về cập nhật ui
 - Firebase Realtime + Storage để tải ảnh avatar, đồng thời lưu trữ dữ liệu user, chat, quan hệ bạn bè. Sử dụng security rule đầy đủ để tránh việc truy cập bất hợp pháp vào vùng dữ liệu user khác
 - AXEmojiView cung cấp EditText chứa emoji, sử dụng Fluent 3d emoji của Microsoft để gửi emoji trong chat
 - Sử dụng flag window tại activity để làm màn hình tràn viền trên các máy có tai thỏ, nốt ruồi, ...
 - Sử dụng cache vào bộ nhớ để lưu trữ dữ liệu bài viết trong 10 phút
 - Khi ấn vào profile image sẽ mở ảnh full, sử dụng intent truyền extra thêm user uid
 - Data được preload trên màn hình logo/splash
 - Thông báo kết bạn sẽ ở notification activity, khi có lời mời kết bạn mới sẽ có ở nút báo đỏ ở fragment Home. 
 - Quét mã qr sử dụng Google ML Kit với tốc độ xử lí khá cao, chuyển sang profile đối phương khi quét hoàn thành
 - Mã qr bản thân được sinh từ useruid của firebase, sử dụng thêm canva để thêm 1 lớp overlay avatar của user ở giữa với độ đảm bảo sai sót 90% (H), mã qr được sinh từ zxing
 - Gửi tin nhắn được cập nhật realtime từ firebase, hơi khó phần xử lí lưu trữ ở backend do cần xử lí quyền truy cập nhưng cũng đã xong
 
 
 
 
 # 7/3/2023: Bắt đầu dự án
 - Tạo xong giao diện login, register theo thiên hướng material design
 - Thiết kế hệ thống login mvc cơ bản
 - Thay đổi animation vào ứng dụng và background đăng nhập

# 9/3/2023:
- Thêm các animation cơ bản bằng xml cổ (fade in fade out)
- Thay đổi font một chút


# 10/3/2023:
- Thêm hệ thống toast thư viện ngoài (MotionToast) để thông báo lỗi
- logic đăng nhập đăng ký sử dụng Firebase Authencation và TextInputLayout của Material chứ không dùng EditText nữa


# 14/3/2023:
- Tinh chỉnh giao diện đăng nhập
- Tạo giao diện profile cơ bản

# 15/3/2023:
- Viết lại giao diện profile, làm lại theo format hiện đại hơn và có ảnh avatar


# 16/3/2023:
- Viết lại hệ thống chuyển trang ViewPager tăng độ mượt, preload hình ảnh về drawable xxxhdpi
- Làm xong sửa profile với hệ thống Firebase Realtime

# 17/3/2023:
- Hệ thống quên mật khẩu, gửi email về bằng Firebase
- Upload ảnh avatar, ui upload bằng Firebase Storage

# 20/3/2023:
- Hệ thống trang tin tức

# 23/3/2023:
- Sử dụng nguồn api mới với trang tin tức tiếng việt, thêm các chuyên mục
- Thêm chức năng cache để tải api cực nhanh, thêm animation recyclerview
- Thêm cache cho webview
- Tắt javascript webview để chặn quảng cáo và đọc bài nhanh hơn
- Animation đọc bài

# 24/3/2023:
- Activity xem ảnh lớn khi ấn avatar
- Tạo t.o.s
- Tinh chỉnh giao diện trang chủ và profile

# 25/3/2023:
- Tạo doc
- Bắt đầu làm ui cho chat

# 28/3/2023:
- API mới cho bài viết tiếng việt
- tinh chỉnh ui cho đọc bài viết
- preload và cache tốc độ cao cho đọc bài viết
- Sử dụng đa luồng để lấy ảnh icon từ og:image qua jsoup

# 30/3/2023:
- Xử lí các cách code cũ đã deprecated

# 1/4/2023:
- Cập nhật gradle
- Fix một số bug gây crash app

# 2/4/2023:
- UI cho quét mã qr, tạo mã qr sử dụng zwing, qr có ảnh avatar của user ở chính giữa nhưng vẫn đảm bảo độ sai sót H
- Tạm thời tạo mock data để show post của user, nếu còn thời gian sẽ làm đăng bài viết

# 3/4/2023:
- Vẽ xong sơ đồ dự thảo cho mối quan hệ database để làm chat
- Xong phần kết bạn giữa các user, giao diện thông báo để xác nhận kết bạn
- Sử dụng Google Machine Learning Kit native cho quét mã qr
- Tạo khả năng chat cơ bản, logic bảo mật trên firebase cho phép user truy cập hợp lí
- Tự động tạo dữ liệu mặc định để sửa một số lỗi crash

# 4/4/2023:
- Tinh chỉnh ui chat và một số phần khác để căn lề hợp lí
- Chat realtime
- Thêm hệ thống emoji trong chat
