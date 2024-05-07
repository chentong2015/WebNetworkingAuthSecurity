package com.spring.sse.sample.controller;

import com.spring.sse.sample.model.Job;
import com.spring.sse.sample.model.JobRequest;
import com.spring.sse.sample.model.JsonLikeObject;
import com.spring.sse.sample.service.JobService;
import com.spring.sse.sample.util.SseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/v1/examples/sse")
public class SseJobController {

    private final ExecutorService nonBlockingService = Executors.newCachedThreadPool();
    private final Logger logger = LoggerFactory.getLogger(SseJobController.class);

    private final JobService jobService;

    SseJobController(JobService jobService) {
        super();
        this.jobService = jobService;
    }

    @GetMapping("/jobs-service")
    public SseEmitter streamEventsFromJobsService() {
        return sendSSEevents("Without body");
    }

    @GetMapping("/jobs-service/{param}")
    public SseEmitter streamEventsFromJobsServiceWithPathVariable(@PathVariable String param) {
        return sendSSEevents(param);
    }

    @GetMapping(value = "/jobs-service", params = "param")
    public SseEmitter streamEventsFromJobsServiceWithRequestParams(@RequestParam String param) {
        return sendSSEevents(param);
    }

    @GetMapping("/jobs-service-with-requestbody")
    public SseEmitter streamEventsFromJobsServiceWithRequestBody(@RequestBody String body) {
        return sendSSEevents(body);
    }

    @GetMapping("/jobs-service-with-json-requestbody")
    public SseEmitter streamEventsFromJobsServiceWithJsonRequestBody(@RequestBody JsonLikeObject body) {
        return sendSSEevents("\nField1= " + body.field1 + "\nField2= " + body.field2 + "\nField3= " + body.field3);
    }

    private SseEmitter sendSSEevents(String body) {
        logger.info("Emitting chunks");
        SseEmitter emitter = SseHelper.createSseEmitter();
        nonBlockingService.execute(() -> {
            try {
                emitter.send(SseEmitter.event().id("param").data(body));
                List<Job> jobs = jobService.getJobs();
                int i = 0;
                for (Job job : jobs) {
                    emitter.send(SseEmitter.event().id(Integer.toString(i++)).data(job));
                }
                emitter.complete();
            } catch (Exception ex) {
                logger.error("Error occurred while sending events", ex);
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    @PostMapping("/jobs-service-with-job-request")
    public SseEmitter streamEventsFromJobsServiceWithJobRequestBody(@RequestBody JobRequest jobRequest) {
        logger.info("Emitting {} jobs of size {}, with {} ms delay", jobRequest.getChunk(), jobRequest.getSize(), jobRequest.getDelay());
        SseEmitter emitter = SseHelper.createSseEmitter();
        nonBlockingService.execute(() -> {
            try {
                int delay = jobRequest.getDelay();
                List<Job> jobs = new JobService(jobRequest.getChunk(), jobRequest.getSize()).getJobs();
                int i = 0;
                for (Job job : jobs) {
                    emitter.send(SseEmitter.event().id(Integer.toString(i++)).data(job));
                    Thread.sleep(delay);
                }
                emitter.complete();
            } catch (Exception ex) {
                logger.error("Error occurred while sending events", ex);
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

    @GetMapping("/get-jobs-service-with-job-request/{chunk}/{size}/{delay}")
    public SseEmitter streamEventsFromJobsServiceWithJobRequestBody(@PathVariable int chunk, @PathVariable int size, @PathVariable int delay) {
        logger.info("Emitting {} jobs of size {}, with {} ms delay", chunk, size, delay);
        SseEmitter emitter = SseHelper.createSseEmitter();
        nonBlockingService.execute(() -> {
            try {
                List<Job> jobs = new JobService(chunk, size).getJobs();
                int i = 0;
                for (Job job : jobs) {
                    emitter.send(SseEmitter.event().id(Integer.toString(i++)).data(job));
                    Thread.sleep(delay);
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
