package springmvc.emitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

// 使用SseEmitter在定时的周期内发送event事件
public class MySseEmitter extends SseEmitter {

    private final ScheduledExecutorService pulseScheduler;
    private final Logger logger = LoggerFactory.getLogger(MySseEmitter.class);

    MySseEmitter(Long timeout) {
        this(timeout, Executors.newScheduledThreadPool(1));
    }

    MySseEmitter(Long timeout, ScheduledExecutorService pulseScheduler) {
        super(timeout);
        this.pulseScheduler = pulseScheduler;
        this.pulseScheduler.scheduleWithFixedDelay(this::pulse, 30L, 30L, SECONDS);
    }

    // 模拟心跳，周期，定时发送指定的事件
    void pulse() {
        try {
            logger.debug("Sending a heart beat");
            send(SseEmitter.event().name("PULSE_EVENT_NAME"));
        } catch (IOException e) {
            logger.warn("Failed to send heart beat event", e);
        }
    }

    @Override
    public synchronized void complete() {
        try {
            send(SseEmitter.event().name("COMPLETE_EVENT_NAME"));
        } catch (IOException e) {
            logger.warn("Failed to send completion event", e);
            return;
        }
        super.complete();
    }
}
