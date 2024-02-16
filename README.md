# How to run this project

## Requirements

- You need Docker in your machine to run this project
- This project is written in Java, because KStreams is a library only available for Java and Scala. Use IntelliJ or other IDE to run the Java project.

## Kafka containers

You have to run the following command, to run the containers:

    docker-compose up -d

Now you should have Kafka, Zookeepeer and KafDrop (UI for Kafka) running.

Open in your browser, the next URL:

    http://localhost:9000

Now you are in Kafdrop, you have to create the following topics:

- fruits
- fruitsCounter

## How to run this project

- Run the containers
- Run the Java project
- Send events to **fruits** topic

Use this command for sending events:

    docker-compose exec kafka kafka-console-producer.sh --topic fruits --bootstrap-server localhost:9092

- In Kafdrop, you can see the topic **fruitsCounter** is receiving messages, but the counter is not visible.

You can run the following command to watch this events as a consumer in the terminal:

    docker-compose exec kafka kafka-console-consumer.sh --bootstrap-server localhost:9092 \             
    --topic fruitsCounter \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
