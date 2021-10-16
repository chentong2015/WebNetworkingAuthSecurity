package main;

public class BaseContainer {

    // 容器的基本认识：
    // 1. 容器是一个软件单元，包含应用程序及其完整的依赖和配置
    // 2. 容器可以部署到指定的宿主操作系统，而不需要进一步的配置即可运行
    // 3. 在这个"容器"能够运行，也能在生产环境中运行
    //    操作系统是确保容器在部署后能够工作的唯一共同点
    
    // 容器和虚拟机的区别
    // 1. 虚拟机是在某种"虚拟化的硬件"之上运行，运行着自己的操作系统副本
    //    虚拟机必须具有在这种场景下必要的所以二进制文件和因应用程序，几GB，启功慢
    // 2. 容器则运行在指定的"物理计算机"上，使用计算机上安装的操作系统，几MB，启动快
    //    多个容器可以共享宿主计算机的操作系统
    //     | 容器(应用程序，库)   容器(应用程序，库)   |
    //     |          Docker引擎                 |
    //     |           操作系统                   |

    // 从容器到微服务架构: 构建一次，到处运行
    // 是使用一种特殊的格式(容器镜像)打包应用程序及其配置，部署到启用了容器的服务器
    // 容器镜像能够轻松的移植到一个兼容的，启用了容器的基础架构上(共有云，私有云，本地无论计算机...)
    // 容器化将整体式的应用程序分解成独立的部分，每个部分可以部署到不同的容器中，以便每个部署部分的扩展和更新
    //   容器1：SQL数据库
    //   容器2：Web API
    //   容器3：Redis缓存

}