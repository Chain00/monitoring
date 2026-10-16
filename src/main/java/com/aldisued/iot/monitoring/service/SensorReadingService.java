package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.exception.SensorNotFoundException;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository,
            SensorRepository sensorRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorRepository = sensorRepository;
    }

    public SensorReadingDto saveSensorReading(SensorReadingDto sensorReadingDto) {
        Sensor sensor = sensorRepository.findById(sensorReadingDto.sensorId()).orElseThrow(() ->
                new SensorNotFoundException(
                        "Sensor with ID " + sensorReadingDto.sensorId() + " not found")
        );

        SensorReading savedReading = sensorReadingRepository.save(
                new SensorReading(sensorReadingDto.value(), sensorReadingDto.timestamp(), sensor));

        return new SensorReadingDto(savedReading.getSensor().getId(), savedReading.getValue(), savedReading.getTimestamp());
    }

}
