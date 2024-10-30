package ds2024.device.dtos.builder;

import ds2024.device.dtos.DeviceDTO;
import ds2024.device.entity.Device;

public class DeviceBuilder {
    public static DeviceDTO toDeviceDTO(Device device){
        return DeviceDTO.builder()
                .id(device.getId())
                .userId(device.getUserId())
                .description(device.getDescription())
                .address(device.getAddress())
                .maxEnergyHourly(device.getMaxEnergyHourly())
                .build();
    }

    public static Device toEntity(DeviceDTO deviceDTO){
        return Device.builder()
                .id(deviceDTO.getId())
                .userId(deviceDTO.getUserId())
                .description(deviceDTO.getDescription())
                .address(deviceDTO.getAddress())
                .maxEnergyHourly(deviceDTO.getMaxEnergyHourly())
                .build();

    }
}
