FROM gradle:9.2-jdk25 AS builder

WORKDIR /opt/build/
COPY . .

RUN gradle clean dnsapi-server:build -x test --info

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

COPY --from=builder /opt/build/dnsapi-server/build/libs/dnsapi-server-1.0.0-SNAPSHOT.jar dnsapi-server-1.0.0-SNAPSHOT.jar

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar dnsapi-server-1.0.0-SNAPSHOT.jar
