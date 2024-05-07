package springmvc.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springmvc.emitter.SseEmitters;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/sse/mvc")
public class FolderWatchController {

    // private final FolderWatchService folderWatchService;
    private final SseEmitters emitters = new SseEmitters();

    @PostConstruct
    void init() {
        // folderWatchService.start(System.getProperty("user.home"));
    }

    @GetMapping(path = "/folder-watch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter getFolderWatch() {
        return emitters.register(new SseEmitter());
    }

    // @Override
    // public void onApplicationEvent(FolderChangeEvent event) {
    //     emitters.send(event.getEvent());
    // }
}
