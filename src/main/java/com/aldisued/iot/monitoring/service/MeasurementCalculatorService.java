package com.aldisued.iot.monitoring.service;


import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

    /**
     * Filter out "noisy" values using a deviation value
     * @param values values to filter
     * @param deviation the deviation to calculate with
     * @return accepted values
     */
    public List<Double> filterByAverageDeviation(List<Double> values, Double deviation) {
        if (deviation > 1 || deviation < 0) {
            throw new IllegalArgumentException("Deviation must be between 0 and 1");
        }

        if (values.isEmpty()) {
            return List.of();
        }

        double average = values.stream().mapToDouble(x -> x).average().orElse(0);
        double from = average - average * deviation;
        double to = average + average * deviation;

        return values.stream().filter(x -> x > from && x < to).toList();
    }

    public List<Double> getMovingAverage(List<Double> data, int windowSize) {
        // TODO: Task 10
        return List.of();
    }

}
