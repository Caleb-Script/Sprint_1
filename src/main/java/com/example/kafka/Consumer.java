package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class Consumer {
        public static void main(String[] args) {

            //create Logger
            final Logger logger = LoggerFactory.getLogger(Consumer.class);

            final String bootstrapServers = "localhost:9092";
            final String consumerGroupID = "Java-group-consumer";

            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupID);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

            String topic = "meine-topics";
            consumer.subscribe(Arrays.asList(topic));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("neue Nachricht: \n" +
                            "Key: " + record.key() + ", " +
                            "Value: " + record.value() + ", " +
                            "Topic: " + record.topic() + ", " +
                            "Partition" + record.partition() + ", " +
                            "Offset" + record.partition() + "\n");
                }
            }
        }
    }
