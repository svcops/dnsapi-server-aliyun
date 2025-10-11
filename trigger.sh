#!/bin/bash
# shellcheck disable=SC2164
SHELL_FOLDER=$(cd "$(dirname "$0")" && pwd)
cd "$SHELL_FOLDER"

branch="main"

key_word_list=(
  "build_ddns_cron"
  "build_dnsapi_server"
)

key_word="$1"

[[ -z "$key_word" ]] && {
  key_word="build_ddns_cron"
}

[[ -n $Key_word ]] && {
  match=false
  for tmp in "${key_word_list[@]}"; do
    if [[ "$key_word" == "$tmp" ]]; then
      key_word="$tmp"
      match=true
    fi
  done

  [[ $match == false ]] && {
    echo "Invalid keyword. Valid keywords are: ${key_word_list[*]}"
    exit 1
  }
}

# 切到你要触发的分支
git checkout $branch
# 创建一个空提交，提交信息包含触发关键字 "build_geo_data"
git commit --allow-empty -m "GitHub Action Trigger: $key_word"
# 推送到远端j
git push origin $branch
