package webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import webflux.dao.PostRepository;
import webflux.model.Post;

import java.time.Duration;

@RestController
@RequestMapping(value = "/posts")
public class DemoEventController {

    private final PostRepository posts;

    @Autowired
    public DemoEventController(PostRepository posts) {
        this.posts = posts;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Post> all() {
        return posts.findAll();
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Post> sse() {
        return Flux.zip(Flux.interval(Duration.ofSeconds(1)), posts.findAll().repeat())
                .map(Tuple2::getT2);
    }

    @GetMapping(value = "/latest", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Post> trackLatestPost() {
        return posts.latestPost();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Post> create(@RequestBody Post post) {
        return posts.save(post);
    }
}
