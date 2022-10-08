#!/usr/bin/env bash

# bash网络请求操作
curl --request GET \
  --url 'http://www.ctong.com/v1/testing/example' \
  --header "Authorization: Bearer $JWT_TOKEN" \
  --insecure
echo
# shell请求能否判断返回的Status来abort中断
# response=sh(script: "curl -k -X POST -L --user ${USERNAME}:${API_TOKEN} $url",,,)
