#!/bin/bash
# shellcheck disable=SC2164 disable=SC2086 disable=SC1090
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd)
cd "$SHELL_FOLDER"

source <(curl -sSL https://gitlab.com/iprt/shell-basic/-/raw/main/build-project/basic.sh)
export ROOT_URI=$ROOT_URI

source <(curl -sSL $ROOT_URI/func/log.sh)
source <(curl -sSL $ROOT_URI/func/ostype.sh)

if is_windows;then
  log_info "build" "build in windows"
  export MSYS_NO_PATHCONV=1
fi

log_info "step 1" "build project with gradle"

bash <(curl -sSL $ROOT_URI/gradle/build.sh) \
  -i "registry.cn-shanghai.aliyuncs.com/iproute/gradle:8.10.2-jdk21-jammy" \
  -c "gradle-cache" \
  -x "gradle clean build -x test --info"

jar_name="dnsapi-server-aliyun-1.0.0-SNAPSHOT.jar"

if [ ! -f "build/libs/$jar_name" ]; then
  log_error "validate" "$jar_name 不存在，打包失败，退出"
  exit 1
fi

log_info "step 2" "build docker image and push to registry"

# timestamp_tag=$(date +"%Y-%m-%d_%H-%M-%S")
# version="$(date '+%Y%m%d')_$(git rev-parse --short HEAD)"
version="latest"

bash <(curl -sSL $ROOT_URI/docker/build.sh) \
  -i "registry.cn-shanghai.aliyuncs.com/iproute/dnsapi-server-aliyun" \
  -v "$version" \
  -r "false" \
  -p "true"
