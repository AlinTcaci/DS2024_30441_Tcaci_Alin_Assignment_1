package ds2024.monitoring.repository;

import ds2024.monitoring.entity.Limits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LimitsRepository extends JpaRepository<Limits, UUID> {
    @Query("SELECT l.maxEnergyHourly FROM Limits l WHERE l.deviceId = :deviceId")
    float findMaxEnergyHourlyByDeviceId(@Param("deviceId") UUID deviceId);

    @Query("SELECT l.userId FROM Limits l WHERE l.deviceId = :deviceId")
    UUID findUserIdByDeviceId(UUID deviceId);
}
