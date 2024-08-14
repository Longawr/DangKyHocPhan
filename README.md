# DKHP_BE_Project
- Backend ứng dụng đăng ký học phần
- Công nghệ: Java, Spring boot, Security, Validation, Lombok,...
- Chức năng:
    - jwt và authorize.
    - CRUD cơ bản.
    - Global Exception Handler.
    - Validation.
    - Cache.

## Hướng dẫn cài đặt

- Cần `sql database` bất kỳ, JDK 17 (Java SE 17) để sử dụng.
- Tạo trước database với `tên database`.
- Mở project bằng maven: `file -> import -> exist maven project`.
- Build trước khi khởi chạy:
    - `chuột phải project -> Show in Local Terminal -> Terminal`, hoặc nhấn Tổ hợp phím `Ctrl + Alt + T`.
    - Nhập: `mvnw clean install` hoặc `mvnw.cmd clean install`. Với `mvnw` hoặc `mvnw.cmd` là tên 2 file trong thư mục gốc của project của bạn (sử dụng `eclipse`, có thể thử `mvn` thay thế).

### Đổi dependency cho database nếu dùng database khác ngoài sqlserver như sau:
- Đổi dòng sau thành `dependency` của database bạn dùng (có thể tìm trên [Maven Repository](https://mvnrepository.com)):
```xml
<dependency>
	<groupId>com.microsoft.sqlserver</groupId>
	<artifactId>mssql-jdbc</artifactId>
	<scope>runtime</scope>
</dependency>
```

### Sửa file `application.yaml` tại `src/main/java/resources` như sau:
- `default.admin.username`: là username admin mặc định khởi tạo
- `default.admin.password`: là password admin mặc định khi khởi tạo

- `jwt.signerKey`: là khoá mã hoá json web token
- `jwt.refresh-duration`: là khoảng thời gian có hiệu lực của refresh token (s)
- `jwt.access-duration`: là khoảng thời gian có hiệu lực của access token (s)

- `spring.datasource.url`: là url dẫn đến database của bạn, trong này bao gồm host, port và tên database
- `spring.datasource.username`: là username có quyền truy cập vào database
- `spring.datasource.password`: là password cho username trên
- `spring.datasource.driver-class-name`: là tên driver của database tuỳ theo loại database sử dụng (cái này search gg)

