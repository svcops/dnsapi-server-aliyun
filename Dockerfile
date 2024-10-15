FROM registry.cn-shanghai.aliyuncs.com/iproute/openjdk:21-bookworm

MAINTAINER "tech@intellij.io"

LABEL email="tech@intellij.io" \
      author="devops"

WORKDIR /opt/app

ADD build/libs/dnsapi-server-aliyun-1.0.0-SNAPSHOT.jar dnsapi-server-aliyun-1.0.0-SNAPSHOT.jar

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar dnsapi-server-aliyun-1.0.0-SNAPSHOT.jar
