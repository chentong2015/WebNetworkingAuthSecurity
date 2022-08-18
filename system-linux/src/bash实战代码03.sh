#!/bin/bash

# 创建方法，$1即为方法的第一个参数
get_greeting() {
  echo "Hello, $1!"
  # 方法没有直接的途径可以返回 ?
}

# 传递参数，获取方法输出的值
STRING_VAR=$(get_greeting "chen victor")
echo $STRING_VAR

# --------------------------------------------

# 截取指定字符之间的字符串
class_name=""
get_class_name() {
   class_name=`echo $1 | awk -F"name=\"" '{print $2}' | awk -F "\"" '{print $1}'`
   if [[ "$class_name" == "" ]]; then
      echo "Error: can not find class name for the line $1"
   fi
}

line="<class name=\"mx.test.MyClass\" >"
echo "input line: $line"

# 传递字符串变量
get_class_name "${line}"
echo $class_name

# if 条件判断regular expression正则条件
while read result
do
    while IFS= read -r line; do
        if [[ $line =~ entity-name=.*entity-name= ]]; then
           echo "Find entity-name twice"
		       echo $line
        fi
        if [[ $line =~ entity-name=\".*\"\" ]]; then
           echo "Find entity-name error"
        fi
    done < $result
done