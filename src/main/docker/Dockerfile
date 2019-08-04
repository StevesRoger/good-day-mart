FROM openjdk:8-slim
MAINTAINER kimchheng <kimchheng101@gmail.com>
COPY @project.build.finalName@.jar @project.build.finalName@.jar
ENV TZ=Asia/Phnom_Penh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
LABEL release-date="@timestamp@"
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/@project.build.finalName@.jar"]