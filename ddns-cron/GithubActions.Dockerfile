FROM gradle:9.2-jdk25 AS builder
WORKDIR /opt/build/
COPY . .
RUN gradle clean ddns-cron:build -x test --info

FROM eclipse-temurin:21-jdk-jammy
RUN apt-get update && \
    export DEBIAN_FRONTEND=noninteractive && \
    apt-get install -y --no-install-recommends curl wget ca-certificates vim && \
    update-ca-certificates && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

LABEL org.opencontainers.image.authors="tech@intellij.io"\
  email="tech@intellij.io" \
  author="zhenjie zhu"

WORKDIR /opt/app

COPY --from=builder /opt/build/ddns-cron/build/libs/ddns-cron-1.0.0-SNAPSHOT.jar ddns-cron-1.0.0-SNAPSHOT.jar

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar ddns-cron-1.0.0-SNAPSHOT.jar
