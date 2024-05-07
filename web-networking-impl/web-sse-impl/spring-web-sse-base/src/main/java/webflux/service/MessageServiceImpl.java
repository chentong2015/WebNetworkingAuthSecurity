package webflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import webflux.model.Event;
import webflux.model.EventGenerator;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor // 在创建Service bean到IoC中的时候，通过构造器自动注入属性
public class MessageServiceImpl implements MessageService {

    private final EventGenerator eventGenerator;

    // Flux流量：在指定的时间间隔里面，server指定的事件Event
    public Flux<Event> stream() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(it -> new Event("josdem", eventGenerator.generate(), Instant.now()));
    }
}
