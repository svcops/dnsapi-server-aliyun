FROM registry.cn-shanghai.aliyuncs.com/iproute/openjdk:21-bookworm

MAINTAINER "tech@intellij.io"

LABEL email="tech@intellij.io" \
      author="devops"

WORKDIR /opt/app

ADD build/quarkus-app /opt/app

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar quarkus-run.jar


