package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class SmartMeterSimulator {

    private static final int BATCH_SIZE = 6;
    private static final int SLEEP_INTERVAL_MS = 10 * 1000;
    private static final String FILE_DATA = "src/main/resources/SensorData.csv";

    public static void main(String[] args) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        if (args.length < 1) {
            System.err.println("Usage: java SmartMeterSimulator <device_id>");
            System.exit(1);
        }

        String DEVICE_ID = args[0];

        try {
            UUID.fromString(DEVICE_ID);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: The provided device_id is not a valid UUID.");
            System.exit(1);
        }

        int index = 0;
        float sum = 0;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqps://cytyonyt:M7ohtiamA92yXCQC8QXcPSbshNqjcMDm@kangaroo.rmq.cloudamqp.com/cytyonyt");
//        factory.setHost("localhost");
//        factory.setPort(5672);
//        factory.setUsername("guest");
//        factory.setPassword("guest");

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_DATA));
             Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare("devices-rmq", true, false, false, null);

            String line;
            while ((line = br.readLine()) != null) {
                float value = Float.parseFloat(line);
                index++;

                if (index % BATCH_SIZE == 0) {
                    String timestamp = LocalDateTime.now().toString();
                    String jsonObject = String.format(
                            "{\"timestamp\": \"%s\", " +
                            "\"device_id\": \"%s\", " +
                            "\"measurement_value\": %.2f}",
                            timestamp,
                            DEVICE_ID,
                            Math.max(sum,value) - Math.min(sum,value)
                    );

                    channel.basicPublish("", "devices-rmq", null, jsonObject.getBytes());
                    System.out.println(" [x] Sent '" + jsonObject + "'");

                    sum = value;

                    Thread.sleep(SLEEP_INTERVAL_MS);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading sensor data from " + FILE_DATA + ": " + e.getMessage());
        } catch (TimeoutException e) {
            System.err.println("Timeout connecting to RabbitMQ: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            System.err.println("Thread was interrupted: " + e.getMessage());
        }

    }

}
