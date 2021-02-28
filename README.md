# kafka-client-api

https://medium.com/nerd-for-tech/how-client-application-interact-with-kafka-cluster-made-easy-with-java-apis-58f29229d992

- AdminClient API
  --Create New Topic
  -- Delete Topic
  -- Describe Topic to fetch Partition, ISR, Leader and Replica details
  -- List Topics
- Producer API
- Consumer API
- Streams API (In-progress)
- Connect API (In-progress

**Maven/Gradle dependency:**

**_MAVEN_**:

`
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>2.7.0</version>
</dependency>
`


**_GRADLE_**:

`implementation group: 'org.apache.kafka', name: 'kafka-clients', version: '2.7.0'`
