#!/bin/bash

# find all .c or .cpp files on linux 全局查找的命令
find . -name "*.c" -o -name "*.cpp"

# 设置程序的日志文件
logger_file=logger.txt
if [ -f "$logger_file" ]; then
	rm $logger_file
fi
touch $logger_file

# $1: get the first parameter from console
find $1 -type f -name *.hbm.xml |
while read result
do
    echo $result >> $logger_file
    while IFS= read -r line; do
	  if [[ "$line" == *"<class "* || "$line" == *"<subclass "* ]]; then
	      # https://www.shellunix.com/awk.html awk参考文档
	      # 截取指定区间内的字符串
	      class_name=`echo $line | awk -F"name=\"" '{print $2}' | awk -F "\"" '{print $1}'`

		    # 正则匹配指定的行，替换(插入)指定的字符串
		    # 这里必须使用双引号，才能替换变量
        # “&”字符表示前面正则匹配的字符串
        # 只需要匹配第一个空格结束的位置
		    entity_tag="entity-name=\"$class_name\" "
		    sed -i "s/<class.*name=\"$class_name\" /&$entity_tag/g" $result
	  fi

	  # 这里的文件路径不能包含空格，否则会报错
	  # $result: ambiguous redirect
    done < $result
done