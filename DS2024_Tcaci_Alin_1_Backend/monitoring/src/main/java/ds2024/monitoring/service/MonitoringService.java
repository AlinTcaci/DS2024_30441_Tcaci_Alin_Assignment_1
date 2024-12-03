package ds2024.monitoring.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ds2024.monitoring.dtos.LimitsDTO;
import ds2024.monitoring.dtos.MonitoringDTO;
import ds2024.monitoring.dtos.builder.LimitsBuilder;
import ds2024.monitoring.dtos.builder.MonitoringBuilder;
import ds2024.monitoring.entity.Limits;
import ds2024.monitoring.entity.Monitoring;
import ds2024.monitoring.repository.MonitoringRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MonitoringService {

    private final MonitoringRepository monitoringRepository;

    private final LimitsService limitsService;

    private final NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MonitoringService(MonitoringRepository monitoringRepository, LimitsService limitsService, NotificationService notificationService) {
        this.monitoringRepository = monitoringRepository;
        this.limitsService = limitsService;
        this.notificationService = notificationService;
    }

//    @RabbitListener(queues = "${rabbitmq.queue.name}", containerFactory = "localListenerFactory")
    @RabbitListener(queues = "${rabbitmq.queue1.name}", containerFactory = "queue1ListenerFactory")
    public void receiveMessage(String message) {
        try {
            MonitoringDTO monitoringDTO = objectMapper.readValue(message, MonitoringDTO.class);
//            System.out.println("Received monitoring data: " + monitoringDTO);
            processMonitoringData(monitoringDTO);
            System.out.println("Received and processed monitoring data: " + monitoringDTO.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to process message: " + message);
        }
    }

    public void processMonitoringData(MonitoringDTO monitoringDTO) {
        Monitoring monitoring = MonitoringBuilder.toEntity(monitoringDTO);
        verifyIfLimitSucceedMaxEnergyHourly(monitoringDTO);
        monitoringRepository.save(monitoring);
    }

    public void verifyIfLimitSucceedMaxEnergyHourly(MonitoringDTO monitoringDTO) {
        float maxEnergyHourlyByDeviceId = limitsService.getMaxEnergyHourlyByDeviceId(monitoringDTO.getDeviceId());
        if (monitoringDTO.getMeasurementValue() > maxEnergyHourlyByDeviceId) {
            UUID userId = limitsService.getUserIdByDeviceId(monitoringDTO.getDeviceId());
            String jsonMessage = String.format(
                    "{\"device_id\": \"%s\", \"user_id\": \"%s\"}",
                    monitoringDTO.getDeviceId(),
                    userId
            );
            notificationService.sendExceedNotification(jsonMessage);
            System.out.println("The energy for deviceId " + monitoringDTO.getDeviceId() + " has exceeded the limit!");
        }
    }

    public List<MonitoringDTO> getAllMonitoringsByDeviceId(UUID deviceId) {
        List<Monitoring> monitoring = monitoringRepository.findAllByDeviceId(deviceId);
        return monitoring.stream()
                .map(MonitoringBuilder::toMonitoringDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<MonitoringDTO> getEnergyConsumptionByDay(UUID deviceId, String date) {
        // Query the repository for data
        List<Monitoring> data = monitoringRepository.findByDeviceIdAndDate(deviceId, date);
        return data.stream()
                .map(MonitoringBuilder::toMonitoringDTO)
                .collect(Collectors.toList());
    }

    public List<MonitoringDTO> getAllMonitorings() {
        List<Monitoring> monitoring = monitoringRepository.findAll();
        return monitoring.stream()
                .map(MonitoringBuilder::toMonitoringDTO)
                .collect(Collectors.toList());
    }

}
