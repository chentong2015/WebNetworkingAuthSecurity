package springmvc.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sse/mvc")
public class EmitterController {

    private static final String[] WORDS = "The quick brown fox test check is ok.".split(" ");
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    // Each client connection submits a separate task to the thread pool
    // 使用emitter发送简单的data field数据
    @GetMapping(path = "/words", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getWords() {
        SseEmitter emitter = new SseEmitter(5 * 1000L);
        cachedThreadPool.execute(() -> {
            try {
                for (String word : WORDS) {
                    emitter.send(word);
                    TimeUnit.SECONDS.sleep(1);
                }
                // TODO. 在Emitter完成信息的发送之后，是否需要做相关的清除的操作 !!
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    // 使用SseEventBuilder来发送多种fields属性的event
    @GetMapping("/stream-sse-mvc")
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data("SSE MVC - " + LocalTime.now().toString())
                            .id(String.valueOf(i))
                            .name("sse event - mvc");
                    emitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}


