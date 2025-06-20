#!/bin/bash
# shellcheck disable=SC2164 disable=SC2086 disable=SC1090 disable=SC2155
set -euo pipefail

function usage() {
  echo "Usage: $0 <dnsapi_server_root_uri> <token_key> <token_value> <domainName> <rr>"
  exit 1
}

function log() {
  local remark="$1"
  local msg="$2"
  local now=$(date +"%Y-%m-%d %H:%M:%S")
  echo -e "$now - [ $remark ] $msg"
}

# 参数校验
if [[ $# -ne 5 ]]; then
  usage
fi

dnsapi_server_root_uri="$1"
dnsapi_server_token_key="$2"
dnsapi_server_token_value="$3"
domainName="$4"
rr="$5"

# notice: use: /ddns/invokeGetIpAutomatic
json_body="{\"domainName\":\"$domainName\",\"rr\":\"$rr\"}"

ddns_api_url="$dnsapi_server_root_uri/ddns/invokeGetIpAutomatic"

request_result=$(curl --connect-timeout 10 -m 20 \
  -X POST \
  -H "Content-Type: application/json" \
  -H "$dnsapi_server_token_key: $dnsapi_server_token_value" \
  -d "$json_body" \
  -w "HTTPSTATUS:%{http_code}" \
  -s "$ddns_api_url")

body=$(echo "$request_result" | sed -e 's/HTTPSTATUS:.*//g')
status=$(echo "$request_result" | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

if [[ "$status" -ne 200 ]]; then
  log "error" "Request failed with status $status. Response: $body"
  exit 2
fi

log "ddns" "$body"
