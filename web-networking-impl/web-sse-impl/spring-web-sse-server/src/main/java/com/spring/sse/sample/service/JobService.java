package com.spring.sse.sample.service;

import com.spring.sse.sample.model.Job;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    private static final int DEFAULT_CHUNK_SIZE = 10000;
    private static final int DEFAULT_JOB_SIZE = 1000;
    private List<Job> jobs;

    public JobService() {
        this.jobs = buildJobs(DEFAULT_CHUNK_SIZE, DEFAULT_JOB_SIZE);
    }

    public JobService(int chunkSize, int jobSize) {
        this.jobs = buildJobs(chunkSize, jobSize);
    }

    public List<Job> buildJobs(int chunkSize, int jobSize) {
        List<Job> jobList = new ArrayList<>();
        for (int i = 0; i < chunkSize; ++i) {
            Job job = new Job(jobSize);
            jobList.add(job);
        }
        return jobList;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
