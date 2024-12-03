package ds2024.monitoring.repository;

import ds2024.monitoring.entity.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring, UUID> {
    List<Monitoring> findAllByDeviceId(UUID deviceId);

    @Query("SELECT m FROM Monitoring m WHERE m.deviceId = :deviceId AND DATE(m.timestamp) = :date")
    List<Monitoring> findByDeviceIdAndDate(@Param("deviceId") UUID deviceId, @Param("date") String date);

}
