package springmvc.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 通过Spring MVC的配置器自定义配置Async Thread Pool异步线程池
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("mvc-task-");
        configurer.setTaskExecutor(taskExecutor);
        configurer.setDefaultTimeout(30_000);
    }
}
