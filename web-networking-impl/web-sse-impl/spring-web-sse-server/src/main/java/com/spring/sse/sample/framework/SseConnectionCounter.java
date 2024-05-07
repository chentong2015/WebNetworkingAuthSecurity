package com.spring.sse.sample.framework;

import java.util.concurrent.atomic.AtomicInteger;

// 单列设计模式，原子性操作，线程安全
public class SseConnectionCounter {

    private final AtomicInteger liveConnections;
    private static final SseConnectionCounter INSTANCE = new SseConnectionCounter();

    private SseConnectionCounter() {
        this.liveConnections = new AtomicInteger();
    }

    public static SseConnectionCounter getInstance() {
        return INSTANCE;
    }

    public void incrementConnections() {
        liveConnections.incrementAndGet();
    }

    public void decrementConnections() {
        liveConnections.decrementAndGet();
    }

    public int getConnections() {
        return liveConnections.get();
    }
}
