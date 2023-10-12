package com.Solar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class SolarPredictionServer {

    public static void main(String[] args) {
        // Konfiguration für den Kafka-Producer
        Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", "localhost:9092");
        kafkaProperties.put("key.serializer", StringSerializer.class.getName());
        kafkaProperties.put("value.serializer", StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(kafkaProperties);



        try {
            // Adresse und Leistung der Solaranlage
            String address = "Namurstraße 4 70374 Stuttgart";
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            int solarPower = 5000;

            // Erstellen Sie die API-Anfrage-URL mit Adresse und Leistung
            String apiUrl = "https://forecast.solar/api/v1/forecast?address=" + encodedAddress + "&power=" + solarPower;

            // HTTP-Client erstellen
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiUrl);

            // API-Anfrage senden und Antwort abrufen
            String response = EntityUtils.toString(httpClient.execute(httpGet).getEntity());

            // Extrahieren Sie die Solarprognosewerte aus der Antwort
            String solarPrediction = extractSolarPrediction(response);

            // Nachricht an Kafka senden
            ProducerRecord<String, String> record = new ProducerRecord<>("solar-prediction-topic", solarPrediction);
            producer.send(record);

            producer.close();
        }  catch (UnsupportedEncodingException e) {
            // Hier können Sie den Fehler behandeln oder die Ausnahme auslösen, falls erforderlich
            System.out.println("Error: "+e); // Hier wird die Ausnahme einfach gedruckt
        } catch (Exception e) {
            System.out.println("Error: "+e);
        }
    }

    private static String extractSolarPrediction(String response) {
        try {
        // Verwenden Sie eine JSON-Verarbeitungsbibliothek, um das JSON-Objekt aus der API-Antwort zu extrahieren.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);


        // Nehmen wir an, die Solarprognose wird im JSON-Objekt "solar" unter dem Feld "prediction" gespeichert.
        JsonNode solarPredictionNode = rootNode.get("solar").get("prediction");

        if (solarPredictionNode != null && solarPredictionNode.isTextual()) {
            // Wenn das Feld existiert und ein Textfeld ist, extrahieren Sie den Wert.
            String solarPrediction = solarPredictionNode.asText();
            return "Solarprognose: " + solarPrediction;
        } else {
            // Wenn das Feld nicht gefunden wurde oder nicht den erwarteten Datentyp hat, geben Sie einen Fehler zurück.
            return "Fehler: Solarprognose nicht gefunden";
        }
    } catch (Exception e) {
        // Hier können Sie Fehlerbehandlung hinzufügen, wenn beim Extrahieren der Prognose ein Fehler auftritt.
            System.out.println("Error: "+e);
        System.out.println("API-Antwort: " + response);
        return "Fehler: Fehler bei der Extraktion der Solarprognose";
    }
}
}
