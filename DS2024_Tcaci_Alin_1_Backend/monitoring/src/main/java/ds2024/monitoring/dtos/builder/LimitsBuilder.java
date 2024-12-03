package ds2024.monitoring.dtos.builder;

import ds2024.monitoring.dtos.LimitsDTO;
import ds2024.monitoring.entity.Limits;

public class LimitsBuilder {
    public static LimitsDTO toLimitsDTO(Limits limits) {
        return LimitsDTO.builder()
                .id(limits.getId())
                .deviceId(limits.getDeviceId())
                .maxEnergyHourly(limits.getMaxEnergyHourly())
                .userId(limits.getUserId())
                .build();
    }

    public static Limits toEntity(LimitsDTO limitsDTO) {
        return Limits.builder()
                .id(limitsDTO.getId())
                .deviceId(limitsDTO.getDeviceId())
                .maxEnergyHourly(limitsDTO.getMaxEnergyHourly())
                .userId(limitsDTO.getUserId())
                .build();
    }
}
