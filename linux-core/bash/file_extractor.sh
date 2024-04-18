# 统计文件中有多少不服条件的field
grep -c '<record ' file.xml

# 提取大文件的前两行
head -2 file.xml > result.xml

# 找到首次指定的匹配条件并截取1000行的数据到结果文件
grep -m 1 -A 1000 'uid="11235"' file.xml >> result.xml

# 最后补充文件的结尾标签
tail -1 file.xml >> result.xml



