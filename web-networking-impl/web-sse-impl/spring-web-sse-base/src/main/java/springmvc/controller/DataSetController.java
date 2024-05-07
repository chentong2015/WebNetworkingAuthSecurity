package springmvc.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class DataSetController {

    private final List<String> dataSets;

    public DataSetController() {
        this.dataSets = new ArrayList<>();
        for (int index = 1; index < 10; index++) {
            dataSets.add("item " + index);
        }
    }

    // 最终返回到界面的信息
    // data:item1
    //
    // data:item1
    // event:dataSet-created
    // id:100525950
    @GetMapping(path = "/emit-data-sets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter fetchData() {
        SseEmitter emitter = new SseEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // 使用另外一个线程发送event消息
        executor.execute(() -> {
            try {
                for (String dataSet : dataSets) {
                    randomDelay();
                    // 1. 发送简单的event
                    emitter.send(dataSet);

                    // 2. 通过SseEventBuilder来发送完整的event
                    //    emitter.send(SseEmitter.event().id("1").name("name item"));
                    SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event();
                    emitter.send(eventBuilder
                            .data(dataSet)
                            .name("dataSet-created")
                            .id(String.valueOf(dataSet.hashCode()))
                    );
                }
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        executor.shutdown();
        return emitter;
    }

    private void randomDelay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
