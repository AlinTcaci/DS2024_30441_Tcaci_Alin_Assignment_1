package ds2024.monitoring.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringDTO {
    private UUID id;

    @JsonProperty("device_id")
    private UUID deviceId;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("measurement_value")
    private float measurementValue;

    @Override
    public String toString() {
        return "MonitoringDTO{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", timestamp=" + timestamp +
                ", measurementValue=" + measurementValue +
                '}';
    }
}
