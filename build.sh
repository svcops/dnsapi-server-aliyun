#!/bin/bash
# shellcheck disable=SC2164 disable=SC2086 disable=SC1090
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd)
cd "$SHELL_FOLDER"

source <(curl -SL https://gitlab.com/iprt/shell-basic/-/raw/main/build-project/basic.sh)
source <(curl -sSL $ROOT_URI/func/log.sh)

log_info "step 1" "gradle build jar"

bash <(curl $ROOT_URI/gradle/build.sh) \
  -i "gradle:8.10.2-jdk21-jammy" \
  -c "gradle_8.10.2-jdk21-jammy_cache" \
  -x "gradle clean build -x test"

jar_name="ddns-server-aliyun-1.0.0-SNAPSHOT.jar"

if [ ! -f "build/libs/$jar_name" ]; then
  log "validate" "$jar_name 不存在，打包失败，退出"
  exit 1
fi

log "step 2" "docker build and push"

registry="registry.cn-shanghai.aliyuncs.com"

#timestamp_tag=$(date +"%Y-%m-%d_%H-%M-%S")

version="latest"
#version="$(date '+%Y%m%d')_$(git rev-parse --short HEAD)"

bash <(curl $ROOT_URI/docker/build.sh) \
  -i "$registry/iproute/ddns-server-aliyun" \
  -v "$version" \
  -r "false" \
  -p "true"
