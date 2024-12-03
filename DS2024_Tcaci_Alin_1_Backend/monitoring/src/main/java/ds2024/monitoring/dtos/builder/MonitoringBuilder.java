package ds2024.monitoring.dtos.builder;

import ds2024.monitoring.dtos.MonitoringDTO;
import ds2024.monitoring.entity.Monitoring;

public class MonitoringBuilder {
    public static MonitoringDTO toMonitoringDTO(Monitoring monitoring) {
        return MonitoringDTO.builder()
                .id(monitoring.getId())
                .deviceId(monitoring.getDeviceId())
                .timestamp(monitoring.getTimestamp())
                .measurementValue(monitoring.getMeasurementValue())
                .build();
    }

    public static Monitoring toEntity(MonitoringDTO monitoringDTO) {
        return Monitoring.builder()
                .id(monitoringDTO.getId())
                .deviceId(monitoringDTO.getDeviceId())
                .timestamp(monitoringDTO.getTimestamp())
                .measurementValue(monitoringDTO.getMeasurementValue())
                .build();
    }
}