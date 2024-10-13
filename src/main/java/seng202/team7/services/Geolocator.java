package seng202.team7.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import seng202.team7.models.Position;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class to handle requesting location from Nominatim Geolocation API
 * @author Morgan English
 */
public class Geolocator {

    /**
     * Default constructor for the Geolocator class
     */
    public Geolocator() {

    }

    /**
     * Runs a query with the address given and finds the most applicable lat, lng co-ordinates
     * @param address address to find lat, lng for
     * @return position of the address as a Position model
     */
    public Position queryAddress(String address) {
        String logMessage = String.format("Requesting geolocation from Nominatim for address: %s, New Zealand", address);
        address = address.replace(' ', '+');
        try {
            // Creating the http request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                    URI.create("https://nominatim.openstreetmap.org/search?q=" + address + ",+New+Zealand&format=json")
            ).build();
            // Getting the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Parsing the json response to get the latitude and longitude co-ordinates
            JSONParser parser = new JSONParser();
            JSONArray results = (JSONArray)  parser.parse(response.body());
            if (results.size() == 0) {
                return new Position(-1000, -1000);
            }
            JSONObject bestResult = (JSONObject) results.get(0);
            double lat = Double.parseDouble((String) bestResult.get("lat"));
            double lng = Double.parseDouble((String) bestResult.get("lon"));
            return new Position(lat, lng);
        } catch (IOException | ParseException e) {
            System.err.println(e);
        } catch (InterruptedException ie) {
            System.err.println(ie);
            Thread.currentThread().interrupt();
        }
        return new Position(-1000, -1000);
    }
}
