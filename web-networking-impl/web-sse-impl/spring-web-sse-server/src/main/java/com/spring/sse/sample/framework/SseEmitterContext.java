package com.spring.sse.sample.framework;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// SseEmitter Context包含SSE传输数据的请求和回复
public class SseEmitterContext {

    private HttpServletRequest request;
    private HttpServletResponse response;

    private SseEmitterContext() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            this.request = ((ServletRequestAttributes) requestAttributes).getRequest();
            this.response = ((ServletRequestAttributes) requestAttributes).getResponse();
        }
    }

    public static SseEmitterContext create() {
        return new SseEmitterContext();
    }

    public SseEmitterContext withRequest(HttpServletRequest request) {
        this.request = request;
        return this;
    }

    public SseEmitterContext withResponse(HttpServletResponse response) {
        this.response = response;
        return this;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
