package ds2024.device.service;

import ds2024.device.dtos.DeviceDTO;
import ds2024.device.dtos.builder.DeviceBuilder;
import ds2024.device.entity.Device;
import ds2024.device.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<DeviceDTO> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO getDeviceById(UUID id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));
        return DeviceBuilder.toDeviceDTO(device);
    }

    public List<DeviceDTO> getDevicesByUserId(UUID userId) {
        List<Device> devices = deviceRepository.findAllByUserId(userId);
        return devices.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public void createDevice(DeviceDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        Device savedDevice = deviceRepository.save(device);
        DeviceBuilder.toDeviceDTO(savedDevice);
    }

    public void updateDeviceById(UUID id, DeviceDTO deviceDTO) {
        Device existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        existingDevice.setUserId(deviceDTO.getUserId());
        existingDevice.setDescription(deviceDTO.getDescription());
        existingDevice.setAddress(deviceDTO.getAddress());
        existingDevice.setMaxEnergyHourly(deviceDTO.getMaxEnergyHourly());

        Device updatedDevice = deviceRepository.save(existingDevice);
        DeviceBuilder.toDeviceDTO(updatedDevice);
    }

    public void deleteDeviceById(UUID id) {
        if(!deviceRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found");
        }
        deviceRepository.deleteById(id);
    }

    public void deleteAllDevicesByUserId(UUID userId) {
        List<Device> devices = deviceRepository.findAllByUserId(userId);
        for(Device device : devices){
            deviceRepository.deleteById(device.getId());
        }
    }

}
