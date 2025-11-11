#!/bin/bash
# shellcheck disable=SC1090 disable=SC2086 disable=SC2155 disable=SC2128 disable=SC2028 disable=SC2164
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd) && cd "$SHELL_FOLDER"
[ -z $ROOT_URI ] && source <(curl -sSL https://gitlab.com/iprt/shell-basic/-/raw/main/build-project/basic.sh)

source <(curl -sSL $ROOT_URI/func/log.sh)
source <(curl -sSL $ROOT_URI/func/ostype.sh)

if is_windows;then
  log_info "build" "build in windows"
  export MSYS_NO_PATHCONV=1
fi

sub_project_path="$1"
sub_project_jar_name="$2"
sub_project_image_name="$3"

log_info "step 1" "gradle build jar"

if [ -d "$sub_project_path" ] && [ -f "$sub_project_path/build.gradle.kts" ]; then
    log_info "validate" "gradle build in $sub_project_path"
else
  log_error "validate" "gradle build in $sub_project_path 失败，目录不存在或缺少 build.gradle.kts 文件，退出"
fi

sub_project_gradle_path=$(echo "$sub_project_path" | sed 's/\//:/g')
bash <(curl -sSL $ROOT_URI/gradle/build.sh) \
  -i "registry.cn-shanghai.aliyuncs.com/iproute/gradle:9.2-jdk25" \
  -c "gradle-cache" \
  -x "gradle clean $sub_project_gradle_path:build -x test --info"

jar_name="$sub_project_jar_name"

if [ ! -f "$sub_project_path/build/libs/$jar_name" ]; then
  log_error "validate" "$jar_name 不存在，打包失败，退出"
  exit 1
fi

log_info "step 2" "docker build and push"

registry="registry.cn-shanghai.aliyuncs.com"

#timestamp_tag=$(date +"%Y-%m-%d_%H-%M-%S")

version="latest"
#version="$(date '+%Y%m%d')_$(git rev-parse --short HEAD)"

bash <(curl -sSL $ROOT_URI/docker/build.sh) \
  -f "$sub_project_path/Dockerfile" \
  -i "$registry/$sub_project_image_name" \
  -v "$version" \
  -r "false" \
  -p "true"
