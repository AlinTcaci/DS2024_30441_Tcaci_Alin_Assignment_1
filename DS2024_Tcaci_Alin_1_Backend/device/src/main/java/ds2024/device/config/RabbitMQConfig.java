package ds2024.device.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.name}")
    private String limitsQueueName;

    @Bean
    public Queue limitsQueue() {
        return new Queue(limitsQueueName, false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }


}
