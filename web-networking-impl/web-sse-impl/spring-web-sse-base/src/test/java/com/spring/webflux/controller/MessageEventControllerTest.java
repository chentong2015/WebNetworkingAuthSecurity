package com.spring.webflux.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import webflux.model.Event;

import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MessageEventControllerTest {

    // 使用reactor-test依赖中WebClient测试
    private final WebTestClient webClient;

    // 指定测试方法显示名称
    // @Test
    // @DisplayName("Should get five events")
    void shouldConsumeServerSentEvents(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());

        // 从事件流中获取指定的事件序列
        List<Event> events = webClient.get().uri("/")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .expectStatus().isOk()
                .returnResult(Event.class)
                .getResponseBody()
                .take(5)
                .collectList()
                .block();

        assert events != null;
        events.forEach(event -> log.info("event: {}", event));
        Assertions.assertEquals(5, events.size());
    }
}