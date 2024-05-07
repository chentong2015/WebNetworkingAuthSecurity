package com.spring.sse.sample.controller;

import com.spring.sse.sample.service.SampleDataService;
import com.spring.sse.sample.util.SseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PreDestroy;
import java.io.*;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/v1/examples/sse")
public class SseSampleController {

    private final ExecutorService nonBlockingService = Executors.newCachedThreadPool();
    private final Logger logger = LoggerFactory.getLogger(SseSampleController.class);

    private final SampleDataService sampleDataService;

    SseSampleController(SampleDataService sampleDataService) {
        super();
        this.sampleDataService = sampleDataService;
    }

    @GetMapping
    public String index() {
        return "sse-sample :: OK";
    }

    @PreDestroy
    public void tearDown() {
        nonBlockingService.shutdown();
    }

    @GetMapping("/heavy")
    public ResponseEntity<InputStreamResource> longRunningHeavy() {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, 1024).forEach(i -> sb.append(getLongString(1024)).append("\n"));
        String chunk = sb.toString();
        logger.info("Writing chunks");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter pw = new PrintWriter(out)) {
            for (int i = 0; i < 1024; i++) {
                pw.write(chunk);
            }
            pw.flush();
            logger.info("Constructing inputStream");
            InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            logger.info("Sending inputStream");
            return new ResponseEntity<>(inputStreamResource, null, HttpStatus.OK);
        } catch (IOException ex) {
            logger.error("Error while closing output stream", ex);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private String getLongString(int size) {
        return IntStream.range(0, size).mapToObj(i -> "x").collect(Collectors.joining());
    }

    @GetMapping("/events")
    public SseEmitter streamEvents() {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, 1024).forEach(i -> sb.append(getLongString(1024)).append("\n"));
        String chunk = sb.toString();
        logger.info("Emitting chunks");
        SseEmitter emitter = SseHelper.createSseEmitter();
        nonBlockingService.execute(() -> {
            try {
                for (int i = 0; i < 1024; i++) {
                    logger.info("emitting event {}", i);
                    emitter.send(SseEmitter.event().id(Integer.toString(i)).data(chunk));
                }
                emitter.send(SseEmitter.event().name("done"));
                emitter.complete();
                logger.info("Done!");
            } catch (Exception ex) {
                logger.error("Error occurred while sending events", ex);
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    @GetMapping("/events-service")
    public SseEmitter streamEventsFromService() {
        logger.info("Emitting chunks");
        SseEmitter emitter = SseHelper.createSseEmitter();
        nonBlockingService.execute(() -> {
            try {
                List<String> dataSets = sampleDataService.findAll();
                int i = 0;
                for (String chunk : dataSets) {
                    emitter.send(SseEmitter.event().id(Integer.toString(i++)).data(chunk));
                }
                emitter.complete();
            } catch (Exception ex) {
                logger.error("Error occurred while sending events", ex);
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    @GetMapping("/stream-events-service/{n}/{delay}")
    public SseEmitter streamDelayedEventsFromService(@PathVariable Integer n, @PathVariable Long delay) {
        logger.info("Emitting {} chunks - with delay={}ms", n, delay);
        SseEmitter emitter = SseHelper.createSseEmitter();
        nonBlockingService.execute(() -> {
            try {
                List<String> dataSets = sampleDataService.find(n);
                int i = 0;
                for (String chunk : dataSets) {
                    Thread.sleep(delay);
                    emitter.send(SseEmitter.event().id(Integer.toString(i++)).data(chunk));
                }
                emitter.complete();
            } catch (Exception ex) {
                logger.error("Error occurred while sending events", ex);
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    @GetMapping("/stream-events-service/{n}/{delay}/{size}")
    public SseEmitter streamDelayedEventsFromService(@PathVariable Integer n, @PathVariable Long delay, @PathVariable int size) {
        logger.info("Emitting {} chunks - with delay={}ms, each one of a size {} bytes", n, delay, size);
        SseEmitter emitter = SseHelper.createSseEmitter();
        nonBlockingService.execute(() -> {
            try {
                String chunk = "x".repeat(size);
                int i = 0;
                while (i < n) {
                    Thread.sleep(delay);
                    logger.info("Sending message of size {}", size);
                    String data = "{ \"time\":\"" + Instant.now() + "\", \"payload\":\"" + chunk + "\"}";
                    emitter.send(SseEmitter.event().id(Integer.toString(i++)).data(data));
                }
                emitter.complete();
            } catch (Exception ex) {
                logger.error("Error occurred while sending events", ex);
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}
