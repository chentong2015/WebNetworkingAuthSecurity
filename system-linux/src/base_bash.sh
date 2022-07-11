#!/usr/bin/env bash

# 变量赋值操作, 使用$符号表示对变量的引用
value="test value"
result=$value
echo "The result is $result"

# 按照指定的字符分割，提取最后段的字符串
class_name=`echo $result | awk -F"/" '{print $NF}' | awk -F "." '{print $1}'`

# 找到指定的目录下java文件的数量
find ./test01/ -type f -name *.java | wc -l

# bash网络请求操作
curl --request GET \
  --url 'http://www.ctong.com/v1/testing/example' \
  --header "Authorization: Bearer $JWT_TOKEN" \
  --insecure
echo
# shell请求能否判断返回的Status来abort中断
# response=sh(script: "curl -k -X POST -L --user ${USERNAME}:${API_TOKEN} $url",,,)

# 判断文件是否是目录
if [[ -d output ]]
then
    rm -r outputdxq
fi
mkdir -p output/primary
mkdir -p output/secondary
cp -r ./packaging/resources/target/resources*/* ./output/primary

# for file in $root_path/*; 递归遍历文件
for f in $root_path
do
    if [ -d "$f" ]; then
        for ff in $f/*
        do
            echo "Processing $ff"
        done
    else
        echo "Processing $f"
    fi
done