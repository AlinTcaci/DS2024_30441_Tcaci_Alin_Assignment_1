package ds2024.monitoring;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableRabbit
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class MonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringApplication.class, args);
	}

}
