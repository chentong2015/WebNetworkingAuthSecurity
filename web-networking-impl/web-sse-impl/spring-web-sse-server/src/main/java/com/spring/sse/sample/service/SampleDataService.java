package com.spring.sse.sample.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SampleDataService {

    private static final Integer DEFAULT_DATASET_SIZE = 10000;
    private final List<String> datasetList = new ArrayList<>();

    public SampleDataService() {
        createDataSet(DEFAULT_DATASET_SIZE);
    }

    public SampleDataService(int size) {
        createDataSet(size);
    }

    public List<String> findAll() {
        return Collections.unmodifiableList(datasetList);
    }

    public List<String> find(int n) {
        return Collections.unmodifiableList(datasetList.subList(0, n));
    }

    private Iterable<String> createDataSet(int n) {
        String name = "dummy_";

        for (int i = 0; i < n; i++) {
            this.datasetList.add(name + i);
        }
        return datasetList;
    }

}
