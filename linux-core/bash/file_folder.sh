#!/usr/bin/env bash

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