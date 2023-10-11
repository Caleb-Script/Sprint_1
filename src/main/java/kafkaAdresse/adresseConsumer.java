package kafkaAdresse;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Arrays;

public class adresseConsumer {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Consumer.class);

        String URL = "localhost:9092";
        String groupID = "anfrage-group";

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,URL );
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupID ); // Verwenden Sie eine eindeutige Gruppen-ID
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        String topic = "anfrage-topic"; // Name des Kafka-Themas für Anfragen
        consumer.subscribe(Arrays.asList(topic));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                // Hier können Sie die Anfrage verarbeiten

                String[] parts = record.value().split(",");
                String address = parts[0];
                for(int i =1;i<4;i++){
                    address += " " + parts[i];
                }
                int solarPower = Integer.parseInt(parts[4]);

                System.out.println("Anfrage empfangen: Adresse=" + address + ", Solarleistung=" + solarPower + "\n");

                logger.info("neue Nachricht: \n" +
                        "Key: " + record.key() + ", " +
                        "Value: " + record.value() + ", " +
                        "Topic: " + record.topic() + ", " +
                        "Partition: " + record.partition() + ", " +
                        "Offset: " + record.offset() +
                        "\n");

            }
        }
    }
}