package springmvc.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springmvc.emitter.SseEmitters;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/sse/mvc")
public class PerformanceController {

    private final AtomicInteger id = new AtomicInteger();
    private final SseEmitters emitters = new SseEmitters();
    // TODO. 定期，周期性的执行一定的任务
    // Schedule commands to run after a given delay, or to execute periodically.
    private final ScheduledExecutorService scheduledThreadPool =
            Executors.newScheduledThreadPool(1);

    // Submits a periodic action that becomes enabled first after the given initial delay,
    // and subsequently with the given period 在给定的周期频率下发送指定的event
    @PostConstruct
    public void init() {
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            emitters.send("performanceService.getPerformance()");
        }, 0, 1, TimeUnit.SECONDS);
    }

    @GetMapping(path = "/performance", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getPerformance() {
        return emitters.register(new SseEmitter());
    }
}
