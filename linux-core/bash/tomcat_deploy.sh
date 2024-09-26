#!/usr/bin/env bash

# TODO. 通过自定义的脚本自动部署相关的应用
killTomcat() {
    pid=`ps -ef | grep tomcat | grep java | awk '{print $2}'`
    echo "tomcat Id list :$pid"
    if [ "$pid" = "" ]
    then
      echo "no tomcat pid alive"
    else
      kill -9 $pid
    fi
}
cd $PROJ_PATH/order
mvn clean install

# 调用自定义的函数，停止指定的服务
killTomcat

# 删除原有工程 TOMCAT_APP_PATH=tomcat在部署机器上的路径
rm -rf $TOMCAT_APP_PATH/webapps/ROOT
rm -f $TOMCAT_APP_PATH/webapps/ROOT.war
rm -f $TOMCAT_APP_PATH/webapps/order.war

# 复制新的工程
cp $PROJ_PATH/order/target/order.war $TOMCAT_APP_PATH/webapps/

cd $TOMCAT_APP_PATH/webapps/
mv order.war ROOT.war

# 启动Tomcat
cd $TOMCAT_APP_PATH/
sh bin/startup.sh