#!/bin/bash
# shellcheck disable=SC2164 disable=SC2086 disable=SC1090 disable=SC2155
set -euo pipefail

VERSION="1.1"
DEFAULT_TIMEOUT=20
DEFAULT_RETRIES=1

function usage() {
  echo "Usage: ./ddns_curl.sh <api_root_uri> <token_key> <token_value> <domainName> <rr> [--timeout <seconds>] [--retries <count>]"
  echo "Options:"
  echo "  -h, --help        Show this help message"
  echo "  -v, --version     Show version"
  echo "  --timeout <sec>   Set curl timeout (default: $DEFAULT_TIMEOUT)"
  echo "  --retries <cnt>   Set curl retry count (default: $DEFAULT_RETRIES)"
  exit 1
}

function version() {
  echo "ddns_curl.sh version $VERSION"
  exit 0
}

function log() {
  local remark="$1"
  local msg="$2"
  local now=$(date +"%Y-%m-%d %H:%M:%S")
  echo -e "$now - [ $remark ] $msg"
}

# 参数校验和解析
if [[ $# -eq 0 ]]; then
  usage
fi

# 处理帮助和版本参数
for arg in "$@"; do
  case $arg in
    -h|--help)
      usage
      ;;
    -v|--version)
      version
      ;;
  esac
  # 不 shift，后面统一处理
done

# 解析主参数
if [[ $# -lt 5 ]]; then
  usage
fi

api_root_uri="$1"
token_key="$2"
token_value="$3"
domainName="$4"
rr="$5"
shift 5

# 解析可选参数
timeout="$DEFAULT_TIMEOUT"
retries="$DEFAULT_RETRIES"
while [[ $# -gt 0 ]]; do
  case $1 in
    --timeout)
      timeout="$2"
      shift 2
      ;;
    --retries)
      retries="$2"
      shift 2
      ;;
    *)
      log "warn" "Unknown option: $1"
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
    -s "$ddns_api_url")

  body=$(echo "$request_result" | sed -e 's/HTTPSTATUS:.*//g')
  status=$(echo "$request_result" | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

  if [[ "$status" -eq 200 ]]; then
    log "ddns" "$body"
    exit 0
  else
    log "error" "Request failed with status $status. Response: $body"
    ((attempt++))
    if [[ $attempt -le $retries ]]; then
      log "info" "Retrying..."
      sleep 2
    fi
  fi
done

log "fatal" "All $retries attempts failed."
exit 2
