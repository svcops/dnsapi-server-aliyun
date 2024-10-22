FROM registry.cn-shanghai.aliyuncs.com/iproute/openjdk:21-bookworm

MAINTAINER "tech@intellij.io"

LABEL email="tech@intellij.io" \
      author="devops"

WORKDIR /opt/app

# We make four distinct layers so if there are application changes the library layers can be re-used
COPY build/quarkus-app/lib/ /opt/app/lib/
COPY build/quarkus-app/*.jar /opt/app/
COPY build/quarkus-app/app/ /opt/app/app/
COPY build/quarkus-app/quarkus/ /opt/app/quarkus/

EXPOSE 8080

CMD java $JAVA_OPTIONS -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -jar quarkus-run.jar
