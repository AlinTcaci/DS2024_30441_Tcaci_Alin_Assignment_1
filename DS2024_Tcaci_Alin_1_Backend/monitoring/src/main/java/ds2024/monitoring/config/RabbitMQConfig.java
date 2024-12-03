package ds2024.monitoring.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

//@Configuration
//public class RabbitMQConfig {
//
//    @Value("${rabbitmq.queue.host}")
//    private String localHost;
//
//    @Value("${rabbitmq.queue.port}")
//    private int localPort;
//
//    @Value("${rabbitmq.queue.username}")
//    private String localUsername;
//
//    @Value("${rabbitmq.queue.password}")
//    private String localPassword;
//
//    @Value("${spring.rabbitmq.host}")
//    private String cloudHost;
//
//    @Value("${spring.rabbitmq.port}")
//    private int cloudPort;
//
//    @Value("${spring.rabbitmq.username}")
//    private String cloudUsername;
//
//    @Value("${spring.rabbitmq.password}")
//    private String cloudPassword;
//
//    @Value("${spring.rabbitmq.virtual-host}")
//    private String cloudVirtualHost;
//
//    @Value("${spring.rabbitmq.ssl.enabled}")
//    private boolean cloudSslEnabled;
//
////    @Bean
////    public Queue simulatorQueue() {
////        return new Queue("simulator", true); // Durable queue
////    }
//
//
//    // Local RabbitMQ Connection Factory
//    @Bean("localConnectionFactory")
//    @Primary
//    public CachingConnectionFactory localConnectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(localHost, localPort);
//        connectionFactory.setUsername(localUsername);
//        connectionFactory.setPassword(localPassword);
//        return connectionFactory;
//    }
//
//    // Cloud RabbitMQ Connection Factory
//    @Bean("cloudConnectionFactory")
//    public CachingConnectionFactory cloudConnectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(cloudHost, cloudPort);
//        connectionFactory.setUsername(cloudUsername);
//        connectionFactory.setPassword(cloudPassword);
//        connectionFactory.setVirtualHost(cloudVirtualHost);
//        if (cloudSslEnabled) {
//            try {
//                connectionFactory.getRabbitConnectionFactory().useSslProtocol();
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to set SSL protocol for CloudAMQP", e);
//            }
//        }
//        return connectionFactory;
//    }
//
//    // Listener Factory for Local Queue
//    @Bean("localListenerFactory")
//    @Primary
//    public SimpleRabbitListenerContainerFactory localListenerFactory(
//            @Qualifier("localConnectionFactory") CachingConnectionFactory localConnectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(localConnectionFactory);
//        return factory;
//    }
//
//
//    // Listener Factory for Cloud Queue
//    @Bean("cloudListenerFactory")
//    public SimpleRabbitListenerContainerFactory cloudListenerFactory(
//            @Qualifier("cloudConnectionFactory") CachingConnectionFactory cloudConnectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(cloudConnectionFactory);
//        return factory;
//    }
//}

@Configuration
public class RabbitMQConfig {

    // Properties for Queue 1
    @Value("${rabbitmq.queue1.host}")
    private String queue1Host;

    @Value("${rabbitmq.queue1.port}")
    private int queue1Port;

    @Value("${rabbitmq.queue1.username}")
    private String queue1Username;

    @Value("${rabbitmq.queue1.password}")
    private String queue1Password;

    @Value("${rabbitmq.queue1.virtual-host}")
    private String queue1VirtualHost;

    @Value("${rabbitmq.queue1.ssl.enabled}")
    private boolean queue1SslEnabled;

    @Value("${rabbitmq.queue1.name}")
    private String queue1Name;

    // Properties for Queue 2
    @Value("${spring.rabbitmq.host}")
    private String queue2Host;

    @Value("${spring.rabbitmq.port}")
    private int queue2Port;

    @Value("${spring.rabbitmq.username}")
    private String queue2Username;

    @Value("${spring.rabbitmq.password}")
    private String queue2Password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String queue2VirtualHost;

    @Value("${spring.rabbitmq.ssl.enabled}")
    private boolean queue2SslEnabled;

    @Value("${spring.rabbitmq.name}")
    private String queue2Name;

    // Connection Factory for Queue 1
    @Bean("queue1ConnectionFactory")
    @Primary
    public CachingConnectionFactory queue1ConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(queue1Host, queue1Port);
        connectionFactory.setUsername(queue1Username);
        connectionFactory.setPassword(queue1Password);
        connectionFactory.setVirtualHost(queue1VirtualHost);
        if (queue1SslEnabled) {
            try {
                connectionFactory.getRabbitConnectionFactory().useSslProtocol();
            } catch (Exception e) {
                throw new RuntimeException("Failed to set SSL protocol for Queue 1", e);
            }
        }
        return connectionFactory;
    }

    // Connection Factory for Queue 2
    @Bean("monitoringConnectionFactory")
    public CachingConnectionFactory queue2ConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(queue2Host, queue2Port);
        connectionFactory.setUsername(queue2Username);
        connectionFactory.setPassword(queue2Password);
        connectionFactory.setVirtualHost(queue2VirtualHost);
        if (queue2SslEnabled) {
            try {
                connectionFactory.getRabbitConnectionFactory().useSslProtocol();
            } catch (Exception e) {
                throw new RuntimeException("Failed to set SSL protocol for Queue 2", e);
            }
        }
        return connectionFactory;
    }

    // Listener Factory for Queue 1
    @Bean("queue1ListenerFactory")
    @Primary
    public SimpleRabbitListenerContainerFactory queue1ListenerFactory(
            @Qualifier("queue1ConnectionFactory") CachingConnectionFactory queue1ConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(queue1ConnectionFactory);
        return factory;
    }

    // Listener Factory for Queue 2
    @Bean("monitoringListenerFactory")
    public SimpleRabbitListenerContainerFactory queue2ListenerFactory(
            @Qualifier("monitoringConnectionFactory") CachingConnectionFactory queue2ConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(queue2ConnectionFactory);
        return factory;
    }

//    // Queue limits
//    @Bean
//    public Queue queue1() {
//        return new Queue(queue1Name, true); // Durable queue
//    }
//
//    // Queue monitoring
//    @Bean
//    public Queue queue2() {
//        return new Queue(queue2Name, true); // Durable queue
//    }
}
