#!/bin/bash
# shellcheck disable=SC2164 disable=SC2086 disable=SC1090 disable=SC2155
function log() {
  local remark="$1"
  local msg="$2"
  local now=$(date +"%Y-%m-%d %H:%M:%S")
  echo -e "$now - [ $remark ] $msg"
}

dnsapi_server_addr=$1
dnsapi_server_token_key=$2
dnsapi_server_token_value=$3

domainName=$4
rr=$5

function validate_param() {
  local input="$1"
  if [ -z $input ]; then
    exit 1
  fi
}
validate_param $dnsapi_server_addr
validate_param $dnsapi_server_token_key
validate_param $dnsapi_server_token_value
validate_param $domainName
validate_param $rr

# notice: use: /ddns/invokeGetIpAutomatic
json_body="{\"domainName\":\"$domainName\",\"rr\":\"$rr\"}"

request_result=$(curl --connect-timeout 10 -m 20 \
  -X POST \
  -H "Content-Type: application/json" \
  -H "$dnsapi_server_token_key: $dnsapi_server_token_value" \
  -d "$json_body" \
  $dnsapi_server_addr)

log "ddns" "$request_result"
