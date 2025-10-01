#!/bin/bash
# shellcheck disable=SC2164,SC1090,SC2086
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd) && cd "$SHELL_FOLDER"
[ -z $ROOT_URI ] && source <(curl -sSL https://gitlab.com/iprt/shell-basic/-/raw/main/build-project/basic.sh)
echo -e "\033[0;32mROOT_URI=$ROOT_URI\033[0m"
# export ROOT_URI=https://dev.kubectl.net

source <(curl -sSL $ROOT_URI/func/log.sh)
source <(curl -sSL $ROOT_URI/func/ostype.sh)

if is_windows;then
  log_info "build" "build in windows"
  export MSYS_NO_PATHCONV=1
fi

log "build" "build ddns docker image"

bash <(curl -sSL $ROOT_URI/docker/build.sh) \
  -i "registry.cn-shanghai.aliyuncs.com/iproute/ddns-cron" \
  -d "." \
  -v "latest" \
  -r "false" \
  -p "true"
