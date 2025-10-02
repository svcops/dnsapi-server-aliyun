FROM ubuntu:22.04

LABEL org.opencontainers.image.authors="tech@intellij.io"

LABEL email="tech@intellij.io" \
      author="devops"

RUN sed -i 's@//.*archive.ubuntu.com@//mirrors.aliyun.com@g' /etc/apt/sources.list && \
    sed -i 's@//security.ubuntu.com@//mirrors.aliyun.com@g' /etc/apt/sources.list && \
    sed -i 's@//ports.ubuntu.com@//mirrors.aliyun.com@g' /etc/apt/sources.list && \
    apt-get update && \
    export DEBIAN_FRONTEND=noninteractive && \
    apt-get install -y --no-install-recommends curl wget cron jq ca-certificates vim dos2unix && \
    update-ca-certificates && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /opt

# 添加定时任务
COPY ddns_cron /etc/cron.d/ddns_cron
RUN chmod 0644 /etc/cron.d/ddns_cron &&  dos2unix /etc/cron.d/ddns_cron && crontab /etc/cron.d/ddns_cron

# 添加脚本
COPY ddns_curl.sh /opt/ddns_curl.sh
RUN chmod +x /opt/ddns_curl.sh && mkdir -p /var/log/ddns/

CMD ["cron", "-f"]
