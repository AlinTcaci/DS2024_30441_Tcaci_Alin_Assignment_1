package ds2024.device.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDTO {
    private UUID id;
    private UUID userId;
    private String description;
    private String address;
    private float maxEnergyHourly;
}
