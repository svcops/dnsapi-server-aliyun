#!/bin/bash
# shellcheck disable=SC2164 disable=SC2086 disable=SC1090 disable=SC2155
set -euo pipefail

# 1) 从固定路径加载环境变量（若存在）
# 支持 /etc/ddns/ddns.env 或 /opt/ddns/ddns.env（按需调整路径）
load_env_file() {
  for f in /etc/ddns/ddns.env /opt/ddns/ddns.env; do
    if [ -f "$f" ]; then
      # 仅接受简单 KEY=VALUE 行
      set -a
      . "$f"
      set +a
      echo "$(date '+%F %T') - [ info ] loaded env file: $f" >> /var/log/ddns/ddns.log 2>&1 || true
      break
    fi
  done
}
load_env_file

# 2) 读取变量，必要时设置默认值或报错
: "${DNSAPI_ROOT_URI:=}"
: "${DNSAPI_ACCESS_TOKEN_KEY:=Authorization}"
: "${DNSAPI_ACCESS_TOKEN_VALUE:=}"
: "${DOMAIN_NAME:=}"
: "${RR:=@}"

log_file="/var/log/ddns/ddns.log"
mkdir -p "$(dirname "$log_file")" || true

log() {
  local remark="$1"
  local msg="$2"
  local now
  now=$(date +"%Y-%m-%d %H:%M:%S")
  echo -e "$now - [ $remark ] $msg" | tee -a "$log_file" >/dev/null
}

# 3) 校验必需变量
missing=0
[ -z "$DNSAPI_ROOT_URI" ] && log "error" "DNSAPI_ROOT_URI 未设置" && missing=1
[ -z "$DNSAPI_ACCESS_TOKEN_VALUE" ] && log "error" "DNSAPI_ACCESS_TOKEN_VALUE 未设置" && missing=1
[ -z "$DOMAIN_NAME" ] && log "error" "DOMAIN_NAME 未设置" && missing=1
# RR 已有默认值 @，可不强制
if [ "$missing" -eq 1 ]; then
  log "fatal" "必需环境变量缺失，退出"
  exit 1
fi

VERSION="1.1"
DEFAULT_TIMEOUT=20
DEFAULT_RETRIES=1

# 支持 --help / --version（保持原有逻辑）
for arg in "$@"; do
  case $arg in
    -h|--help)
      echo "Usage: ./ddns_curl.sh (env driven) [--timeout <seconds>] [--retries <count>]"
      echo "Env: DNSAPI_ROOT_URI, DNSAPI_ACCESS_TOKEN_KEY, DNSAPI_ACCESS_TOKEN_VALUE, DOMAIN_NAME, RR"
      exit 0
      ;;
    -v|--version)
      echo "ddns_curl.sh version $VERSION"
      exit 0
      ;;
  esac
done

api_root_uri="$DNSAPI_ROOT_URI"
token_key="$DNSAPI_ACCESS_TOKEN_KEY"
token_value="$DNSAPI_ACCESS_TOKEN_VALUE"
domainName="$DOMAIN_NAME"
rr="$RR"

timeout="$DEFAULT_TIMEOUT"
retries="$DEFAULT_RETRIES"

# 解析可选参数 --timeout/--retries
while [ $# -gt 0 ]; do
  case "$1" in
    --timeout)
      timeout="${2:-$DEFAULT_TIMEOUT}"
      shift 2
      ;;
    --retries)
      retries="${2:-$DEFAULT_RETRIES}"
      shift 2
      ;;
    *)
      # 忽略未知参数，或在此处报错
      shift
      ;;
  esac
done

json_body="{\"domainName\":\"$domainName\",\"rr\":\"$rr\"}"
ddns_api_url="$api_root_uri/ddns/invokeGetIpAutomatic"

attempt=1
while [[ $attempt -le $retries ]]; do
  log "info" "Attempt $attempt: Requesting $ddns_api_url"
  request_result=$(curl --connect-timeout 10 -m "$timeout" \
    -X POST \
    -H "Content-Type: application/json" \
    -H "$token_key: $token_value" \
    -d "$json_body" \
    -w "HTTPSTATUS:%{http_code}" \
    -s "$ddns_api_url" || true)

  body="${request_result//HTTPSTATUS:*/}"
  status=$(echo "$request_result" | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

  if [[ "$status" == "200" ]]; then
    log "ddns" "$body"
    exit 0
  else
    log "error" "Request failed with status ${status:-N/A}. Response: $body"
    ((attempt++))
    if [[ $attempt -le $retries ]]; then
      log "info" "Retrying..."
      sleep 2
    fi
  fi
done

log "fatal" "All $retries attempts failed."
exit 2
