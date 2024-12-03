package ds2024.monitoring.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "limits")
public class Limits {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "max_energy_hourly", nullable = false)
    private float maxEnergyHourly;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
