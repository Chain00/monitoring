package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import org.springframework.stereotype.Service;

@Service
public class MeasurementService {

    private final SensorReadingRepository sensorReadingRepository;

    public MeasurementService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public List<Double> getMeasurementValuesBySensorType(SensorType sensorType, LocalDateTime from,
            LocalDateTime to) {
        return sensorReadingRepository.findByTimestampBetweenAndSensor_typeOrderByTimestamp(
                from, to, sensorType).stream().map(SensorReading::getValue).toList();
    }

    public Optional<Double> getAverageTemperature(LocalDateTime from, LocalDateTime to) {
        List<SensorReading> sensorReadings = sensorReadingRepository.findByTimestampBetweenAndSensor_typeOrderByTimestamp(
                from, to, SensorType.TEMPERATURE);

        OptionalDouble average = sensorReadings.stream().mapToDouble(SensorReading::getValue).average();

        return average.isPresent() ? Optional.of(average.getAsDouble()) : Optional.empty();
    }

}
