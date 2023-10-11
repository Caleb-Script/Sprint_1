package com.solarForecast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

public class SolarForecastClient {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClients.createDefault();
        String apiKey = "Ihr-API-Schlüssel"; // Ersetzen Sie dies durch Ihren eigenen API-Schlüssel
        String location = "Karlsruhe"; // Die gewünschte Standortadresse

        try {
            // Senden Sie eine GET-Anfrage an die API
            String apiUrl = "https://api.forecast.solar/estimate/" + location;
            HttpGet httpGet = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(httpGet);

            // Verarbeiten Sie die API-Antwort
            String json = EntityUtils.toString(response.getEntity());

            // Verwenden Sie Gson, um JSON in ein Java-Objekt zu parsen
            Gson gson = new Gson();
            SolarForecast forecast = gson.fromJson(json, SolarForecast.class);

            // Verarbeiten und anzeigen Sie die Vorhersage
            System.out.println("Vorhersage für " + location);
            System.out.println("Leistung der Solaranlage: " + forecast.getSolarPower() + " W");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
