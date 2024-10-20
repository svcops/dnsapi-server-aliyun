FROM openjdk:17-jdk

MAINTAINER "devops@kubectl.net"

LABEL email="devops@kubectl.net" \
      author="zhuzhenjie"

WORKDIR /opt/app

ADD build/quarkus-app /opt/app

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar quarkus-run.jar


