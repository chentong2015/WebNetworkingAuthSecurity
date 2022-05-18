#!/usr/bin/env bash

# Bash shell脚本测试
# https://leetcode.com/tag/shell/

# 基本的变量赋值操作, 使用$符号表示对变量的引用
value="test value"
result=$value
echo "The result is $result"

echo shell请求能否判断返回的Status来abort中断
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