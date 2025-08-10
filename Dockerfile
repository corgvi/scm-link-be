# Giai đoạn Build (Build Stage)
# Sử dụng một image Maven với JDK 21 để build ứng dụng của bạn
# maven:3.9.11-eclipse-temurin-21 là một lựa chọn tốt
FROM maven:3.9.11-eclipse-temurin-21 AS build

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Copy file pom.xml và các thư mục src/main/resources, src/main/java
# để Maven có thể tải dependencies và build project
COPY pom.xml .
COPY src ./src

# Build ứng dụng. Sử dụng -DskipTests để bỏ qua test khi build Docker image
# Hoặc bỏ -DskipTests nếu bạn muốn chạy test trong quá trình build image
RUN mvn package -DskipTests

# Giai đoạn Runtime (Runtime Stage)
# Sử dụng một image JDK 21 nhỏ gọn để chạy ứng dụng của bạn
# eclipse-temurin:21-jre-alpine là một lựa chọn rất tốt cho kích thước nhỏ
FROM eclipse-temurin:21-jre-alpine

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Copy file JAR đã build từ giai đoạn 'build' vào thư mục /app trong container
COPY --from=build /app/target/*.jar app.jar

# Lộ cổng mà ứng dụng Spring Boot sẽ lắng nghe (mặc định là 8080)
EXPOSE 8080

# Lệnh để chạy ứng dụng Spring Boot khi container khởi động
# Sử dụng java -jar để chạy file JAR.
# Có thể thêm các tham số JVM tối ưu hóa nếu cần (ví dụ: -Xmx, -XX:MaxRAMPercentage)
ENTRYPOINT ["java", "-jar", "app.jar"]