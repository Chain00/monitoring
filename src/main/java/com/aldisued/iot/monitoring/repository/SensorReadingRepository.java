package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorReadingRepository extends JpaRepository<SensorReading, String> {
    List<SensorReading> findByTimestampBetweenAndSensor_typeOrderByTimestamp(LocalDateTime from, LocalDateTime to,
            SensorType temperature);
}
