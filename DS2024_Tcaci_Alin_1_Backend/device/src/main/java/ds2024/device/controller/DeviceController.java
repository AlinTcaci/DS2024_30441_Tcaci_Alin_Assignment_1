package ds2024.device.controller;

import ds2024.device.dtos.DeviceDTO;
import ds2024.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/getAllDevices")
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        List<DeviceDTO> devices = deviceService.getAllDevices();
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @GetMapping("/getDeviceById/{id}")
    public ResponseEntity<DeviceDTO> getDeviceById(@PathVariable UUID id) {
        DeviceDTO device = deviceService.getDeviceById(id);
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    @GetMapping("/getAllDevicesByUserId/{userId}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByUserId(@PathVariable UUID userId) {
        List<DeviceDTO> devices = deviceService.getDevicesByUserId(userId);
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @PostMapping("/createDevice")
    public ResponseEntity<String> createDevice(@RequestBody DeviceDTO deviceDTO) {
        deviceService.createDevice(deviceDTO);
        return new ResponseEntity<>("Device created successfully.", HttpStatus.CREATED);
    }

    @PutMapping("/updateDeviceById/{id}")
    public ResponseEntity<String> updateDeviceById(@PathVariable UUID id, @RequestBody DeviceDTO deviceDTO) {
        deviceService.updateDeviceById(id, deviceDTO);
        return new ResponseEntity<>("Device updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/deleteDeviceById/{id}")
    public ResponseEntity<String> deleteDeviceById(@PathVariable UUID id) {
        deviceService.deleteDeviceById(id);
        return new ResponseEntity<>("Device deleted successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllDevicesByUserId/{userId}")
    public ResponseEntity<String> deleteAllDevicesByUserId(@PathVariable UUID userId) {
        deviceService.deleteAllDevicesByUserId(userId);
        return new ResponseEntity<>("All devices deleted successfully.", HttpStatus.OK);
    }
}
