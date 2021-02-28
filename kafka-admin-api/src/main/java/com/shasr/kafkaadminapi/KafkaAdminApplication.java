package com.shasr.kafkaadminapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@Slf4j
@SpringBootApplication
public class KafkaAdminApplication implements CommandLineRunner {

    private static final String TOPIC_NAME = "shasr-new-topic";

    public static void main(String[] args) {
        SpringApplication.run(KafkaAdminApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //Create Admin Client
        AdminClient adminClient = getAdminClient();

        // Topic information
        deleteTopic(adminClient);
        createNewTopic(adminClient);
        listTopics(adminClient);
        describeTopics(adminClient);

        // Cluster information
        fetchClusterId(adminClient);
        fetchClusterController(adminClient);
        fetchClusterNodes(adminClient);
    }

    private AdminClient getAdminClient() {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.99.100:29092");
        AdminClient adminClient = AdminClient.create(kafkaProperties);
        return adminClient;
    }

    private void describeTopics(AdminClient adminClient) throws Exception {

        log.info("------------- Describe topics ---------------");
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singleton(TOPIC_NAME));
        Map<String, TopicDescription> topicDescriptionMap = describeTopicsResult.all().get();

        List<TopicPartitionInfo> topicPartitionInfoList = topicDescriptionMap.get(TOPIC_NAME).partitions();
        topicPartitionInfoList.stream().forEach(topicInfo -> {
            log.info("Leader : {}", topicInfo.leader());
            log.info("Partitions : {}", topicInfo.partition());
            log.info("Replicas : {}", topicInfo.replicas());
            log.info("ISR : {}", topicInfo.isr());
        });
    }

    private void createNewTopic(AdminClient adminClient) throws Exception {

        log.info("------------- Create new topic ---------------");
        NewTopic createTopic = new NewTopic(TOPIC_NAME, 3, (short) 2);
        adminClient.createTopics(Collections.singleton(createTopic));
    }

    private void deleteTopic(AdminClient adminClient) throws Exception {

        Collection<TopicListing> topicList = adminClient.listTopics().listings().get();
        log.info("------------- Delete topic ---------------");
        if (topicList.contains(TOPIC_NAME)) {
            adminClient.deleteTopics(Collections.singleton(TOPIC_NAME));
        }
    }

    private void listTopics(AdminClient adminClient) throws Exception {

        log.info("------------- Topics List ---------------");
        Collection<TopicListing> topicList = adminClient.listTopics().listings().get();
        topicList.stream().forEach(topic -> {
            log.info("Topic Name : {} ", topic.name());
            log.info("Is-internal Topic : {} ", topic.isInternal());
        });
    }

    private void fetchClusterId(AdminClient adminClient) throws Exception {

        log.info("------------- Cluster-Id ---------------");
        String clusterId = adminClient.describeCluster().clusterId().get();
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
