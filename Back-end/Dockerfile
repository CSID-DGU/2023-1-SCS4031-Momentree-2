FROM openjdk:17
ARG JAR_FILE=Back-end/build/libs/app.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "./app.jar"]