package com.spring.sse.sample.util;

import com.spring.sse.sample.framework.MySseEmitter;
import com.spring.sse.sample.framework.SseEmitterContext;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseHelper {

    private SseHelper() {
    }

    public static SseEmitter createSseEmitter() {
        return createSseEmitter(SseEmitterContext.create());
    }

    public static SseEmitter createSseEmitter(SseEmitterContext sseContext) {
        return new MySseEmitter(sseContext);
    }

    static SseEmitter createSseEmitter(long pulseDelay) {
        return new MySseEmitter(pulseDelay);
    }
}
