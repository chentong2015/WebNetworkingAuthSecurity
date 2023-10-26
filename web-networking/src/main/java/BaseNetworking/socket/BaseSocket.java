package com.network.base.socket;

import java.io.IOException;
import java.net.Socket;

// TODO: Socket本质上是底层TCP连接暴露到上层的接口(实现)
// 由于上层是无法直接调用OS级别的核心方法(TCP的连接..)，于是暴露给上层(应用程序)进行调用

// Socket连接:
// 1. 双向连接，在client端和server端都有的end-point
// 2. Socket缓冲
public class BaseSocket {

    // Socket.createImpl()
    //    AbstractPlainSocketImpl.socketCreate()
    //       class PlainSocketImpl
    //       static native int socket0(boolean stream) throws IOException;
    //       最终会调用到本地方法，OpenJdk中实现的底层方法
    //
    // jdk8u-jdk-master\jdk8u-jdk-master\src\windows\native\java\net
    //    DualStackPlainSocketImpl.c
    //    net_util_md.c
    //       int NET_Socket(int domain, int type 协议的类型, int protocol) {
    //          SOCKET sock;
    //          该方法调用OS底层暴露出来的接口
    //          sock = socket(domain, type, protocol);
    //          if (sock != INVALID_SOCKET) {
    //              SetHandleInformation((HANDLE)(uintptr_t)sock, HANDLE_FLAG_INHERIT, FALSE);
    //          }
    //          return (int)sock;
    //       }
    //
    // TODO: Windows内核源码并不开源，无法查看方法的具体实现
    // C:\Program Files (x86)\Windows Kits\10\Include\10.0.18362.0\um
    //     WinSock2.h OS操作系统提供SDK中的头文件，其中声明方法
    //        #if INCL_WINSOCK_API_TYPEDEFS
    //        typedef
    //        _Must_inspect_result_
    //        SOCKET
    //        (WSAAPI * LPFN_SOCKET)(
    //            _In_ int af,
    //            _In_ int type,
    //            _In_ int protocol
    //            );
    //        #endif /* INCL_WINSOCK_API_TYPEDEFS */
    private void testJavaSocketTcp()  throws IOException{
        Socket socket = new Socket("localhost", 8080);
    }
}
