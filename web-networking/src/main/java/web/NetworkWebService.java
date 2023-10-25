package web;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

// Host: a machine for protocol
// Ethernet: private network for communicating
// Public IP: what is my ip 找到个人电脑外网IP
// localhost: 127.0.0.1 定位本机host上的Server APP
public class NetworkWebService {

    // Web Service:
    // 1. 跨编程语言和跨操作系统平台的远程调用技术
    // 2. 采用HTTP协议传输数据，采用XML格式封装数据

    // Web Service标准:
    // 1. SOAP(XML Web Service)通信协议 = HTTP协议 + XML数据格式
    //    特定的HTTP消息头 + 发送的请求内容和结果内容都采用XML格式封装
    // 2. WSDL: Web Services Description Language
    //    通过一个WSDL文件来说明服务是什么以及那些可以对外调用
    //    基于XML的, 客户端和服务器端都能理解的标准格式, 用于描述Web Service及其函数、参数和返回值

    // CDN(Content Delivery Network) 内容分发网络
    // 一种互联网连接的电脑网络系统，利用"最靠近"每位用户的服务器，更快更可靠地发送给用户
    // 提供高性能，可扩展性及低成本的网络内容传递给用户

    // Network Interface: Systems often run with multiple active network connections
    // 显示系统上同时运行的不同网络连接
    public void testNetworkInterfaces() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netIf : Collections.list(nets)) {
            System.out.println("Display name: " + netIf.getDisplayName());
            System.out.println("Name: " + netIf.getName());
            for (NetworkInterface subIf : Collections.list(netIf.getSubInterfaces())) {
                System.out.println("Sub Interface Display name: " + subIf.getDisplayName());
                System.out.println("Sub Interface Name: " + subIf.getName());
            }
        }
    }
}







