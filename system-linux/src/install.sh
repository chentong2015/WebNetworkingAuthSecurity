#!/usr/bin/env bash

# Bash shell脚本测试
# https://leetcode.com/tag/shell/

echo Linux平台shell请求能否判断返回的Status, 能否abort中断
# response=sh(script: "curl -k -X POST -L --user ${USERNAME}:${API_TOKEN} $url",,,)

curl --request GET \
  --url 'http://www.ctong.com/v1/testing/example' \
  --header "Authorization: Bearer $JWT_TOKEN" \
  --insecure
echo

if [[ -d output ]]
then
    rm -r output
fi
mkdir -p output/primary
mkdir -p output/secondary

echo Copy指定的目录
cp -r ./packaging/resources/target/resources*/* ./output/primary