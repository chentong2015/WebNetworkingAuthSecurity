package webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import webflux.model.Performance;
import webflux.service.PerformanceService;

import java.time.Duration;

@RestController
@RequestMapping("/sse/flux")
public class PeriodicEventsStreamController {

    private static final String[] WORDS = "The quick brown fox jumps".split(" ");

    // TODO: 1.处理短暂的周期性事件流
    @GetMapping(path = "/words", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getWords() {
        return Flux.zip(Flux.just(WORDS), Flux.interval(Duration.ofSeconds(1)))
                // combine them together by zip method to type Flux<Tuple2<String,Long>>
                // extract the first element of the tuple by map(Tuple2::getT1) of type Flux<String>
                .map(Tuple2::getT1);
    }

    // TODO: 2. 处理长期的周期性事件流
    @GetMapping(path = "/performance", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Performance> getPerformance() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> PerformanceService.getPerformance());
    }

    // TODO: 3. 处理非周期性事件流
    // @GetMapping(path = "/folder-watch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    // Flux<FolderChangeEvent.Event> getFolderWatch() {
    //     return Flux.create(sink -> {
    //         MessageHandler handler = message ->
    //                 sink.next(FolderChangeEvent.class.cast(message.getPayload()).getEvent());
    //         sink.onCancel(() -> subscribableChannel.unsubscribe(handler));
    //         subscribableChannel.subscribe(handler);
    //     }, FluxSink.OverflowStrategy.LATEST);
    // }
}
