package com.spring.sse.sample.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// 重写Spring MVC SSE传输逻辑: 发送，中断，完成
// 创建SseEmitter增加连接数，完成时减少连接数
public class MySseEmitter extends SseEmitter {

    // 数据传输过程中的参数配置
    private static final Long SSE_EMITTER_TIMEOUT = -1L;
    private static final Long SSE_PULSE_DELAY = 30000L;
    private static final int PULSE_SCHEDULER_DEFAULT_THREAD_POOL_SIZE = 1;

    private static final String COMPLETE_EVENT_NAME = "complete";
    private static final String PULSE_EVENT_NAME = "pulse";
    private static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    private static final String X_PID_HEADER = "X-PID";

    private final AtomicInteger sentEventsCount;
    private final ScheduledExecutorService pulseScheduler;
    private final Logger logger = LoggerFactory.getLogger(MySseEmitter.class);
    private final SseConnectionCounter sseConnectionCounter = SseConnectionCounter.getInstance();
    private String clientHost;
    private String clientPid;

    public MySseEmitter(SseEmitterContext context) {
        this(Executors.newScheduledThreadPool(PULSE_SCHEDULER_DEFAULT_THREAD_POOL_SIZE), SSE_PULSE_DELAY);
        Objects.requireNonNull(context);
        if (context.getRequest() != null) {
            HttpServletRequest httpRequest = context.getRequest();
            this.clientHost = StringUtils.hasText(httpRequest.getHeader(X_FORWARDED_FOR_HEADER)) ?
                    httpRequest.getHeader(X_FORWARDED_FOR_HEADER) : httpRequest.getRemoteAddr();
            this.clientPid = httpRequest.getHeader(X_PID_HEADER);
        }
        sseConnectionCounter.incrementConnections();
    }

    public MySseEmitter(Long pulseDelay) {
        this(Executors.newScheduledThreadPool(PULSE_SCHEDULER_DEFAULT_THREAD_POOL_SIZE), pulseDelay);
    }

    MySseEmitter(ScheduledExecutorService pulseScheduler, Long pulseDelay) {
        super(SSE_EMITTER_TIMEOUT);
        sentEventsCount = new AtomicInteger();
        this.pulseScheduler = pulseScheduler;
        this.pulseScheduler.scheduleWithFixedDelay(this::pulse, pulseDelay, pulseDelay, MILLISECONDS);
    }

    @Override
    public void send(SseEventBuilder builder) throws IOException {
        super.send(builder);
        sentEventsCount.incrementAndGet();
    }

    void pulse() {
        try {
            send(SseEmitter.event().name(PULSE_EVENT_NAME));
        } catch (IOException e) {
            logger.warn("Failed to send heartbeat event", e);
        }
    }

    @Override
    public synchronized void complete() {
        try {
            send(SseEmitter.event().name(COMPLETE_EVENT_NAME).data(sentEventsCount.get()));
        } catch (IOException e) {
            logger.warn("Failed to send completion event", e);
            return;
        } finally {
            this.pulseScheduler.shutdown();
            sseConnectionCounter.decrementConnections();
        }
        super.complete();
    }


    @Override
    public synchronized void completeWithError(Throwable ex) {
        try {
            send(SseEmitter.event().name(COMPLETE_EVENT_NAME).data(sentEventsCount.get()));
        } catch (IOException e) {
            logger.warn("Failed to send completion event", e);
            return;
        } finally {
            this.pulseScheduler.shutdown();
            sseConnectionCounter.decrementConnections();
        }
        super.completeWithError(ex);
    }
}
