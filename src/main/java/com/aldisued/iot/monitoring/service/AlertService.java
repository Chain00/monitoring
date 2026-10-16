package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.exception.AlertNotFoundException;
import com.aldisued.iot.monitoring.exception.SensorNotFoundException;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);
    private final AlertRepository alertRepository;
    private final SensorRepository sensorRepository;
    private final KafkaTemplate<String, AlertDto> kafkaTemplate;

    public AlertService(AlertRepository alertRepository, SensorRepository sensorRepository,
            KafkaTemplate<String, AlertDto> kafkaTemplate) {
        this.alertRepository = alertRepository;
        this.sensorRepository = sensorRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Save the alert and publish to kafka
     * @param alertDto the alert to persist
     * @return the saved alert
     */
    public Alert saveAlert(AlertDto alertDto) {
        Sensor sensor = sensorRepository.findById(alertDto.sensorId()).orElseThrow(
                () -> new SensorNotFoundException("Sensor " + alertDto.sensorId() + " not found"));

        Alert savedAlert = alertRepository.save(new Alert(alertDto.message(), alertDto.timestamp(), sensor));
        kafkaTemplate.send("alerts", alertDto);
//        .whenComplete((result, exception) -> {
//            if (exception != null) {
//                log.error("Failed to publish alert {} from sensor {}. {}", alertDto.message(),
//                        alertDto.sensorId(), exception.getMessage());
//            }
//        });
        return savedAlert;
    }

    public AlertDto findLastAlertBySensorId(UUID sensorId) {
        Alert latestAlert = alertRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId)
                .orElseThrow(() -> new AlertNotFoundException(
                        "Alert for " + sensorId + " sensor not found"));

        return new AlertDto(latestAlert.getSensor().getId(), latestAlert.getMessage(),
                latestAlert.getTimestamp());
    }
}
