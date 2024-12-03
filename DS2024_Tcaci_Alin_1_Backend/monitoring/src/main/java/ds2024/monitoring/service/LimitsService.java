package ds2024.monitoring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ds2024.monitoring.dtos.builder.LimitsBuilder;
import ds2024.monitoring.entity.Limits;
import ds2024.monitoring.repository.LimitsRepository;
import ds2024.monitoring.dtos.LimitsDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LimitsService {

    private final LimitsRepository limitsRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LimitsService(LimitsRepository limitsRepository) {
        this.limitsRepository = limitsRepository;
    }

//    @RabbitListener(queues = "${spring.rabbitmq.name}", containerFactory = "cloudListenerFactory")
    @RabbitListener(queues = "${spring.rabbitmq.name}", containerFactory = "monitoringListenerFactory")
    public void receiveMessage(String message) {
        try{
            LimitsDTO limitsDTO = objectMapper.readValue(message, LimitsDTO.class);
//            System.out.println("Received limits data: " + limitsDTO.toString());
            processLimitsData(limitsDTO);
            System.out.println("Received and processed limits data: " + limitsDTO.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to process limits message: " + message);
        }
    }

    public void processLimitsData(LimitsDTO limitsDTO) {
        Limits limits = LimitsBuilder.toEntity(limitsDTO);
        limitsRepository.save(limits);
    }

    public float getMaxEnergyHourlyByDeviceId(UUID deviceId) {
        return limitsRepository.findMaxEnergyHourlyByDeviceId(deviceId);
    }

    public List<LimitsDTO> getAllLimits() {
        List<Limits> limits = limitsRepository.findAll();
        return limits.stream()
                .map(LimitsBuilder::toLimitsDTO)
                .collect(Collectors.toList());
    }


    public UUID getUserIdByDeviceId(UUID deviceId) {
        return limitsRepository.findUserIdByDeviceId(deviceId);
    }
}
