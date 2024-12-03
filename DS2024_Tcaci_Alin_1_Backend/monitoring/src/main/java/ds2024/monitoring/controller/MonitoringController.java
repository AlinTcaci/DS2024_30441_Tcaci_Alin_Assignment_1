package ds2024.monitoring.controller;

import ds2024.monitoring.dtos.MonitoringDTO;
import ds2024.monitoring.dtos.builder.MonitoringBuilder;
import ds2024.monitoring.entity.Monitoring;
import ds2024.monitoring.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/monitor")
public class MonitoringController {

    private final MonitoringService monitoringService;

    @Autowired
    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }


    @GetMapping("/getAllMonitoringsByDeviceId/{deviceId}")
    public ResponseEntity<List<MonitoringDTO>> getMonitoringsByDeviceId(@PathVariable UUID deviceId) {
        List<MonitoringDTO> monitorings = monitoringService.getAllMonitoringsByDeviceId(deviceId);
        return new ResponseEntity<>(monitorings, HttpStatus.OK);
    }

    @GetMapping("/getEnergyConsumptionByDay/{deviceId}/{date}")
    public ResponseEntity<List<MonitoringDTO>> getEnergyConsumptionByDay(
            @PathVariable UUID deviceId,
            @PathVariable String date) {
        List<MonitoringDTO> consumptionData = monitoringService.getEnergyConsumptionByDay(deviceId, date);
        return new ResponseEntity<>(consumptionData, HttpStatus.OK);
    }

    @GetMapping("/getAllMonitorings")
    public ResponseEntity<List<MonitoringDTO>> getAllMonitorings() {
        List<MonitoringDTO> monitorings = monitoringService.getAllMonitorings();
        return new ResponseEntity<>(monitorings, HttpStatus.OK);
    }
}
