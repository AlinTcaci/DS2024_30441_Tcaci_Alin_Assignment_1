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
@Table(name = "monitoring")
public class Monitoring {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    @Column(name="id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "measurement_value", nullable = false)
    private float measurementValue;
}
