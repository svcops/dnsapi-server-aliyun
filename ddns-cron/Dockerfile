FROM registry.cn-shanghai.aliyuncs.com/iproute/eclipse-temurin:21-jdk-jammy

RUN sed -i 's@//.*archive.ubuntu.com@//mirrors.aliyun.com@g' /etc/apt/sources.list && \
    sed -i 's@//security.ubuntu.com@//mirrors.aliyun.com@g' /etc/apt/sources.list && \
    sed -i 's@//ports.ubuntu.com@//mirrors.aliyun.com@g' /etc/apt/sources.list && \
    apt-get update && \
    export DEBIAN_FRONTEND=noninteractive && \
    apt-get install -y --no-install-recommends curl wget ca-certificates vim && \
    update-ca-certificates && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

LABEL org.opencontainers.image.authors="tech@intellij.io"\
  email="tech@intellij.io" \
  author="zhenjie zhu"

WORKDIR /opt/app

ADD build/libs/ddns-cron-1.0.0-SNAPSHOT.jar ddns-cron-1.0.0-SNAPSHOT.jar

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar ddns-cron-1.0.0-SNAPSHOT.jar
