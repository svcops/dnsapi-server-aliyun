#!/bin/bash
# shellcheck disable=SC2164 disable=SC2086 disable=SC1090
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd)
cd "$SHELL_FOLDER"

source <(curl -SL https://gitlab.com/iprt/shell-basic/-/raw/main/build-project/basic.sh)
source <(curl -sSL $ROOT_URI/func/log.sh)

log_info "build" "build quarkus run file"

bash <(curl $ROOT_URI/gradle/build.sh) \
  -i "registry.cn-shanghai.aliyuncs.com/iproute/gradle:8.9-jdk21-graal-jammy" \
  -c "8.9-jdk21-graal-jammy_cache" \
  -x "gradle clean build -x test --info"

#  -x "gradle clean build -Dquarkus.package.jar.enabled=false -Dquarkus.native.enabled=true --info -x test"
# 阿里云SDK有问题，构建不了native文件
# https://quarkus.io/guides/config#package-and-run-the-application

run_file="quarkus-run.jar"

if [ ! -f build/quarkus-app/$run_file ]; then
  log_error "build app" "build quarkus-app file ($run_file) failed"
  exit 1
fi

log "step 2" "docker build and push"

registry="registry.cn-shanghai.aliyuncs.com"
version="quarkus-app"

bash <(curl $ROOT_URI/docker/build.sh) \
  -f "Dockerfile" \
  -i "$registry/iproute/dnsapi-server-aliyun" \
  -v "$version" \
  -r "false" \
  -p "true"
