package com.shasr.kafkaproducerapi;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class KafkaProducerApiApplication implements CommandLineRunner {

	@Value("${kafka.brokers : 192.168.99.100:29091}")
	private String kafkaBrokers;

	@Value("${kafka.topic}")
	private String kafkaTopic;

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Properties kafkaProperties = new Properties();

		// Set the brokers (bootstrap servers)
		kafkaProperties.setProperty("bootstrap.servers", kafkaBrokers);

		// Set how to serialize key/value pairs
		kafkaProperties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaProperties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// Create Kafka Producer
		Producer<String, String> producer = new KafkaProducer<>(kafkaProperties);

		for (int i = 100; i < 200; i++) {

			//Create Producer Record with topic-name, key(optional), value
			ProducerRecord producerRecord = new ProducerRecord(kafkaTopic, "shasr", Integer.toString(i));

			// Send producer record to kafka
			producer.send(producerRecord);
		}
		producer.close();
	}
}
