FROM adoptopenjdk/openjdk11:alpine-jre
ENV APP_HOME=/usr/app
ADD ./build/libs/potato-admin.jar /app.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]
