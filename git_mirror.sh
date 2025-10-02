#!/bin/bash
# shellcheck disable=SC2164,SC2086,SC1090
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd) && cd "$SHELL_FOLDER"
source <(curl -sSL https://dev.kubectl.net/git/mirrors.sh)

mirror_to_gitlab "svcops/dnsapi-server-aliyun"
