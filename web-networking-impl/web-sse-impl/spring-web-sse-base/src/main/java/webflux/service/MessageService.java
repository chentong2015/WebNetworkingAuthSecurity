package webflux.service;

import reactor.core.publisher.Flux;
import webflux.model.Event;

public interface MessageService {

    Flux<Event> stream();
}
