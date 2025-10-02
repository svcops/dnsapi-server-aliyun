#!/bin/bash
# shellcheck disable=SC2164
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd)
cd "$SHELL_FOLDER"

bash ../build_tpl.sh "dnsapi-server" "dnsapi-server-1.0.0-SNAPSHOT.jar" "iproute/dnsapi-server-aliyun"
