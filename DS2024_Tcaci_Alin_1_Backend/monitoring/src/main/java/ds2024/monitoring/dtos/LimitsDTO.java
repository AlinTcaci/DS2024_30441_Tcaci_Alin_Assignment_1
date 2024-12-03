package ds2024.monitoring.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LimitsDTO {
    private UUID id;

    @JsonProperty("device_id")
    private UUID deviceId;

    @JsonProperty("max_energy_hourly")
    private float maxEnergyHourly;

    @JsonProperty("user_id")
    private UUID userId;

    @Override
    public String toString() {
        return "LimitsDTO{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", maxEnergyHourly=" + maxEnergyHourly +
                ", userId=" + userId +
                '}';
    }
}
