package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.exception.ResourceAlreadyExistsException;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * Saves the sensor
     * @param sensor the sensor to save
     * @return the saved sensor
     */
    public Sensor saveSensor(SensorDto sensor) {

        if (sensorRepository.existsByName(sensor.name())) {
            throw new ResourceAlreadyExistsException("Sensor " + sensor.name() + " already exists.");
        }

        return sensorRepository.save(new Sensor(
                sensor.name(),
                sensor.type()
        ));
    }
}
