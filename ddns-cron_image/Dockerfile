FROM ubuntu:22.04

LABEL org.opencontainers.image.authors="tech@intellij.io"

LABEL email="tech@intellij.io" \
      author="devops"

RUN sed -i 's@//.*archive.ubuntu.com@//mirrors.aliyun.com@g' /etc/apt/sources.list && \
    sed -i 's@//security.ubuntu.com@//mirrors.aliyun.com@g' /etc/apt/sources.list && \
    sed -i 's@//ports.ubuntu.com@//mirrors.aliyun.com@g' /etc/apt/sources.list && \
    apt-get update && \
    export DEBIAN_FRONTEND=noninteractive && \
    apt-get install -y --no-install-recommends curl wget cron jq ca-certificates && \
    update-ca-certificates && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /opt

# 添加定时任务
COPY ddns_cron /opt/ddns_cron
RUN chmod 0644 /opt/ddns_cron && crontab /opt/ddns_cron

# 添加脚本
COPY ddns_curl.sh /usr/local/bin/ddns_curl.sh
RUN chmod +x /usr/local/bin/ddns_curl.sh && mkdir -p /var/log/ddns/

CMD ["cron", "-f"]
