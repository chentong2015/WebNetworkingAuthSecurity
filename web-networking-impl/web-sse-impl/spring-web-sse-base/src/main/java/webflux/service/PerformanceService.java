package webflux.service;

import webflux.model.Performance;

public class PerformanceService {

    public static Performance getPerformance() {
        return new Performance();
    }
}
