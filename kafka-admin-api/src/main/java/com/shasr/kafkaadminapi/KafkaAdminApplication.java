package com.shasr.kafkaadminapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
@SpringBootApplication
public class KafkaAdminApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KafkaAdminApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Properties kafkaProperties = new Properties();
        kafkaProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.99.100:29092");

        AdminClient adminClient = AdminClient.create(kafkaProperties);

        fetchClusterId(adminClient);
        fetchClusterController(adminClient);
        fetchClusterNodes(adminClient);

        createAndListTopics(adminClient);
    }

    private void createAndListTopics(AdminClient adminClient) throws Exception {

        log.info("------------- Create new topic ---------------");

        NewTopic createTopic = new NewTopic("shasr-topic", 3, (short) 2);
        adminClient.createTopics(Collections.singleton(createTopic));

        log.info("------------- Topics List ---------------");

        Collection<TopicListing> topicList = adminClient.listTopics().listings().get();

        topicList.stream().forEach(topic -> {
            log.info("Topic Name : {} ", topic.name());
            log.info("Is-internal Topic : {} ", topic.isInternal());
        });
    }

    private void fetchClusterId(AdminClient adminClient) throws Exception {

        String clusterId = adminClient.describeCluster().clusterId().get();

        log.info("------------- Cluster-Id ---------------");
        log.info("Cluster-Id : {}", clusterId);
    }

    private void fetchClusterController(AdminClient adminClient) throws Exception {

        Node controllerNode = adminClient.describeCluster().controller().get();

        log.info("--------------- Controller Node Details ---------------");
        log.info("Node-Id: {}", controllerNode.id());
        log.info("Node-String: {}", controllerNode.idString());
        log.info("Node-host:{}", controllerNode.host());
        log.info("Node-Port: {}", controllerNode.port());
        log.info("Node-hasRack: {}", controllerNode.hasRack());
    }

    private void fetchClusterNodes(AdminClient adminClient) throws Exception {

        Collection<Node> clusterNodes = adminClient.describeCluster().nodes().get();

        clusterNodes.stream().forEach(node -> {
            log.info("------------- Node Details ---------------");
            log.info("Node-Id: {}", node.id());
            log.info("Node-String: {}", node.idString());
            log.info("Node-host:{}", node.host());
            log.info("Node-Port: {}", node.port());
            log.info("Node-hasRack: {}", node.hasRack());
        });
    }
}
