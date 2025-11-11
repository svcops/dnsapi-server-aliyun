FROM registry.cn-shanghai.aliyuncs.com/iproute/eclipse-temurin:25-jdk-jammy

LABEL org.opencontainers.image.authors="tech@intellij.io"

LABEL email="tech@intellij.io" \
      author="zhenjie zhu"

WORKDIR /opt/app

ADD build/libs/dnsapi-server-1.0.0-SNAPSHOT.jar dnsapi-server-1.0.0-SNAPSHOT.jar

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar dnsapi-server-1.0.0-SNAPSHOT.jar
