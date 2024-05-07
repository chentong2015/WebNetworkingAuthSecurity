package webflux.dao;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import webflux.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

// 模拟Repository层的操作, 在本地的实现
@Component
public class PostRepository {

    private static final List<Post> DATA = new ArrayList<>();
    private final Sinks.Many<Post> sinks = Sinks.many().replay().latest();

    static {
        DATA.add(Post.builder().id(1L).title("post one").content("content of post one").build());
        DATA.add(Post.builder().id(2L).title("post two").content("content of post two").build());
    }

    public Flux<Post> findAll() {
        return Flux.fromIterable(DATA);
    }

    public Mono<Post> findById(Long id) {
        return findAll().filter(p -> Objects.equals(p.getId(), id)).single();
    }

    public Mono<Post> save(Post post) {
        long id = DATA.size() + 1;
        Post saved = Post.builder().id(id).title(post.getTitle()).content(post.getContent()).build();
        DATA.add(saved);
        sinks.emitNext(saved, FAIL_FAST);
        return Mono.just(saved);
    }

    public Flux<Post> latestPost() {
        return sinks.asFlux();
    }
}
