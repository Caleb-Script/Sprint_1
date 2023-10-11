package kafkaAdresse;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class adresseProducer {
    public static void main(String[] args) {



        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        final Producer<String, String> producer = new KafkaProducer<String, String>(prop);

        String topic = "anfrage-topic"; // Name des Kafka-Themas für Anfragen
        String address = "D, Stuttgart, Namurstraße, 4 ";
        int solarPower = 5000; // Leistung der Solaranlage in Watt

        String message = address + "," + solarPower;

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);

        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Anfrage gesendet erfolgreich an " + metadata.topic());

            } else {
                System.err.println("Fehler beim Senden der Anfrage: " + exception.getMessage());
            }
        });

        producer.close();
    }


}
