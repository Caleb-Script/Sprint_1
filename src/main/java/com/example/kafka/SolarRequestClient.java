package com.example.kafka;

import org.apache.kafka.clients.producer.*;
import java.util.Properties;

public class SolarRequestClient {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);

        String topic = "solar-requests";
        String address = "Land, Stadt, Straße, Hausnummer";
        double solarPower = 5000.0; // Leistung der Solaranlage in Watt

        // Nachricht erstellen (z. B. als JSON)
        String requestMessage = "{\"address\": \"" + address + "\", \"solarPower\": " + solarPower + "}";

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, requestMessage);

        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Anfrage erfolgreich gesendet an " + metadata.topic());
            } else {
                System.err.println("Fehler beim Senden der Anfrage: " + exception.getMessage());
            }
        });

        producer.close();
    }
}

