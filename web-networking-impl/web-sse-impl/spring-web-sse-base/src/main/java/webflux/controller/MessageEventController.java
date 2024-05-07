package webflux.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import webflux.model.Event;
import webflux.service.MessageService;

import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/flux")
public class MessageEventController {

    private final MessageService messageService;

    // MediaType.TEXT_EVENT_STREAM_VALUE represents plain text event sent that follows SSE format
    // responses starts with "data:" 只发送data filed给client端, 并且只发送一次
    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> createConnectionAndSendEvents() {
        return Flux.just("Alpha", "Omega");
    }

    @GetMapping(path = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> index() {
        return messageService.stream();
    }

    // 直接通过ServerSentEvent构建, 不用再声明“text/event-stream” media type
    // 根据SSE network protocol网络协议约束event的发送
    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence))  // unique event identifier 用于连接端口后的自动重连
                        .event("periodic-event")       // event 事件的类型
                        .data("SSE - " + LocalTime.now().toString()) // data 事件对应的具体数据
                        .retry(Duration.ofSeconds(2))  // 如果断开，client自动重连的时间间隔，默认3s
                        .comment("some comments ...")  // 心跳机制，发送client会忽略的信息，避免断连
                        .build());
    }
    // 测试效果，会不停的推送事件，增加在UI界面的显示
    // data:{"nickname":"josdem","text":"Hola","timestamp":"2019-04-25T12:51:48.693987Z"}
    // data:{"nickname":"josdem","text":"Bonjour","timestamp":"2019-04-25T12:51:49.692895Z"}
    // data:{"nickname":"josdem","text":"Zdravstvuyte","timestamp":"2019-04-25T12:51:50.693260Z"}
    // ...
}
