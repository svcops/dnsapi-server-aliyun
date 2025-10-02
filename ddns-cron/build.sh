#!/bin/bash
# shellcheck disable=SC2164
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd)
cd "$SHELL_FOLDER"

bash ../build_tpl.sh "ddns-cron" "ddns-cron-1.0.0-SNAPSHOT.jar" "iproute/ddns-cron"
