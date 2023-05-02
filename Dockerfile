FROM eclipse-temurin:20-jdk

COPY target/api_gateway-1.0-SNAPSHOT-spring-boot.jar /app/app.jar

EXPOSE 8080

WORKDIR /app

ENTRYPOINT ["java", "--enable-preview", "--add-modules", "jdk.incubator.concurrent", "-jar", "app.jar"]
