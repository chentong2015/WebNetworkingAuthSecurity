package com.spring.sse.sample.model;

public class Job {

    private byte[] payload;

    public Job() {
        this(1024 * 10);
    }

    public Job(int size) {
        this.payload = new byte[size];
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

}
