#!/usr/bin/env bash

# 变量赋值操作, 使用$符号表示对变量的引用
value="test value"
result=$value
echo "The result is $result"

# 按照指定的字符分割，提取最后段的字符串
class_name=`echo $result | awk -F"/" '{print $NF}' | awk -F "." '{print $1}'`

find . -name /folder  # 找到指定目录下包含指定名称的文件
find ./test01/ -type f -name *.java | wc -l # 找到指定的目录下java文件的数量

grep -r 'string_to_find' .  # 查找指定目录下包含指定字符串的文件(递归遍历文件的内容)

