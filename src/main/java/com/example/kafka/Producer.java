package com.example.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {
    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(Producer.class);

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        //Producer
        final KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        String topic = "meine_topics";
        String message = "Hello,zu Kafka!";

        //ProducerRecord
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);

        //send Data Asynchron
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e==null){
                    logger.info(
                            "hat geklappt \n"
                                    + "Topic: " + recordMetadata.topic()
                                    + ", Partition: " + recordMetadata.partition() + ", "
                                    + "Offset: " + recordMetadata.offset() 
                                    + " @ Timestamp: " + recordMetadata.timestamp() + "\n"
                            );
                } else{
                    logger.error("Error: ", e);
                }
            }
        });

        //flush and close
        producer.flush();
        producer.close();
    }
}
