package com.example.marcelkawskiuves;

import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.me.jstott.jcoord.UTMRef;

// TODO POBIERANIE OSTATNIO ZNANEJ LOKALIZACJI
// TODO POMYSLEC NAD DODANIEM KLASY REPORT ZEBY NIE CZYTAC Z BAZY DANYCH

public class DataCollector extends AsyncTask<String, Void, ArrayList>{

    @Override
    protected ArrayList<BikeStation> doInBackground(String... params) {

        ArrayList<BikeStation> bikeStations = new ArrayList<>();
        String jsonString;
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        String url = "https://drive.google.com/uc?export=download&id=10bsaBPZ-5QoAF9ludW0Lxt8svrHFyMH6";
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            con.setRequestProperty("accept", "application/json;");
            con.setRequestProperty("accept-language", "es");
            con.connect();
            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("ERROR!!! - HTTP error code: " + responseCode);
            }
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            int n;
            while ((n = in.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            in.close();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        jsonString = writer.toString();


        try {
            jsonObject = new JSONObject(jsonString);
            jsonArray = new JSONArray(jsonObject.get("features").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    JSONObject geometry = obj.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    JSONObject properties = obj.getJSONObject("properties");

                    double coordinate1 = coordinates.getDouble(0);
                    double coordinate2 = coordinates.getDouble(1);

                    UTMRef utm = new UTMRef(coordinate1, coordinate2, 'N', 30);
                    coordinate1 = utm.toLatLng().getLat();
                    coordinate2 = utm.toLatLng().getLng();

                    bikeStations.add(new BikeStation(
                            properties.getString("name"),
                            properties.getInt("number"),
                            properties.getString("address"),
                            properties.getInt("total"),
                            properties.getInt("available"),
                            properties.getInt("free"),
                            coordinate1,
                            coordinate2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return bikeStations;
    }

}

