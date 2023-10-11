package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class Consumer {
        public static void main(String[] args) {

            //create Logger
            final Logger logger = LoggerFactory.getLogger(Consumer.class);

            final String bootstrapServers = "localhost:9092";
            final String consumerGroupID = "cr";

            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty("group.id", consumerGroupID);

            final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

            String topic = "meine-topics";
            consumer.subscribe(Arrays.asList(topic));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("neue Nachricht: \n" +
                            "Key: " + record.key() + ", " +
                            "Value: " + record.value() + ", " +
                            "Topic: " + record.topic() + ", " +
                            "Partition: " + record.partition() + ", " +
                            "Offset: " + record.offset() +
                            "\n");
                    System.out.println("ok!");
                }

            }
        }
    }
