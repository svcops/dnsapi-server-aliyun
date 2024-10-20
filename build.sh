#!/bin/bash
# shellcheck disable=SC2164 disable=SC2086 disable=SC1090
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd)
cd "$SHELL_FOLDER"

source <(curl -SL https://gitlab.com/iprt/shell-basic/-/raw/main/build-project/basic.sh)
source <(curl -sSL $ROOT_URI/func/log.sh)

log_info "build" "build native file"

#bash <(curl $ROOT_URI/gradle/build.sh) \
#  -i "registry.cn-shanghai.aliyuncs.com/iproute/gradle:8.9-jdk21-graal-jammy" \
#  -c "8.9-jdk21-graal-jammy-jammy_cache" \
#  -x "gradle clean build -x test -Dquarkus.package.jar.enabled=false -Dquarkus.native.enabled=true --info"

# https://quarkus.io/guides/config#package-and-run-the-application
bash <(curl $ROOT_URI/gradle/build.sh) \
  -i "registry.cn-shanghai.aliyuncs.com/iproute/gradle:8.9-jdk21-graal-jammy" \
  -c "8.9-jdk21-graal-jammy-jammy_cache" \
  -x "gradle clean build -x test -Dquarkus.native.enabled=true --info"
