package webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Spring WebFlux 网络流量, 响应式编程，事件驱动
// reactive programming, non-blocking asynchronous applications and event-driven.
// 2. Spring WebFlux supports fully non-blocking reactive streams. 响应式流
// 3. Spring WebFlux uses "Netty" as inbuilt server to run reactive applications. 通知机制

// based on Reactive Streams API
// uses the event-loop computing model to implement asynchronous Java web applications
@SpringBootApplication
public class BaseSpringWebFlux {

    public static void main(String[] args) {
        SpringApplication.run(BaseSpringWebFlux.class, args);
    }
}
