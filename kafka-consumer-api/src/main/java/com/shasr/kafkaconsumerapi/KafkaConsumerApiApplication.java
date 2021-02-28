package com.shasr.kafkaconsuerapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
@SpringBootApplication
public class KafkaConsumerApiApplication implements CommandLineRunner {

    @Value("${kafka.brokers:192.168.99.100:29092}")
    private String kafkaBrokers;

    @Value("${kafka.topic}")
    private String kafkaTopic;

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Properties kafkaProperties = getKafkaProperties();

        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties);
        consumer.subscribe(Collections.singletonList(kafkaTopic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                records.forEach(record -> {
                    // print the offset,key and value for the consumer records.
                    log.info("");
                    log.info("----------------------------- Kafka Headers & Payload Information -----------------------------");
                    log.info("RECEIVED_TOPIC : {} ", record.topic());
                    log.info("RECEIVED_PARTITION_ID : {} ", record.partition());
                    log.info("OFFSET : {} ", record.offset());
                    log.info("KEY : {} ", record.key());
                    log.info("VALUE: {} ", record.value());
                    log.info("TIME: {}", record.timestamp());
                });
            }
        } finally {
            consumer.close();
        }
    }

    private Properties getKafkaProperties() {

        // Configure the consumer
        Properties kafkaProperties = new Properties();

        // Point it to the brokers
        kafkaProperties.setProperty("bootstrap.servers", kafkaBrokers);

        // Set the consumer group (all consumers must belong to a group).
        kafkaProperties.setProperty("group.id", "SHASR-NUMBERS");

        // Set how to serialize key/value pairs
        kafkaProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProperties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // Read messages from the start.
        kafkaProperties.setProperty("auto.offset.reset", "earliest");
        return kafkaProperties;
    }
}
