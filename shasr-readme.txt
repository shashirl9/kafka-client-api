
## CREATE TOPIC - NUMBERS
docker exec kafka-1 kafka-topics --bootstrap-server 192.168.99.100:29092 --create --topic numbers --partitions 3 --replication-factor 2

## TOPICS TOPICS FROM CURRENT CLUSTER
docker exec kafka-1 kafka-topics --bootstrap-server 192.168.99.100:9092 --list

## DESCRIVE NUMBERS TOPIC
docker exec kafka-1 kafka-topics --bootstrap-server 192.168.99.100:29091 --topic  numbers --describe

docker exec kafka-1 kafka-console-consumer --bootstrap-server 192.168.99.100:9091 --topic numbers --from-beginning

docker exec kafka-1 kafka-console-producer --broker-list 192.168.99.100:29091 --topic numbers