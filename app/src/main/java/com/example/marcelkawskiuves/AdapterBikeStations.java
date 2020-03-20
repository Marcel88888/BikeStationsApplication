package com.example.marcelkawskiuves;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdapterBikeStations extends android.widget.BaseAdapter{

    private ArrayList<BikeStation> bikeStations;
    private DBHelper dbHelper;
    Context context;

    static class ViewHolder {
        TextView number;
        TextView name;
        TextView available;
    }

    public AdapterBikeStations(Context c) {
        this.context = c;
        dbHelper = new DBHelper(c);
        Init();
    }

    public void Init() {

        bikeStations = new ArrayList<BikeStation>();

        InputStream is = context.getResources().openRawResource(R.raw.bikestationsvalenbici);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        JSONObject jsonObject;
        JSONArray jsonArray = null;

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

                    bikeStations.add(new BikeStation(
                            obj.getJSONObject("properties").getString("name"),
                            obj.getJSONObject("properties").getInt("number"),
                            obj.getJSONObject("properties").getString("address"),
                            obj.getJSONObject("properties").getInt("total"),
                            obj.getJSONObject("properties").getInt("available"),
                            obj.getJSONObject("properties").getInt("free"),
                            obj.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0),
                            obj.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public int getCount() {
        return bikeStations.size();
    }

    @Override
    public BikeStation getItem(int position){
        return bikeStations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bikeStations.get(position).getNumber();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;
        if (v == null) {
            LayoutInflater li =
                    (LayoutInflater)context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.bikestationview, null) ;
            holder = new ViewHolder();

            holder.number = v.findViewById(R.id.bikestationviewnumber);
            holder.name = v.findViewById(R.id.bikestationviewname);
            holder.available = v.findViewById(R.id.bikestationviewavailable);
            v.setTag(holder);
        } else {
            holder = (ViewHolder)v.getTag();
        }

        if (bikeStations.get(position).getAvailable() > 10)
            v.setBackgroundColor(Color.GREEN);
        else if (bikeStations.get(position).getAvailable() > 4)
            v.setBackgroundColor(Color.YELLOW);
        else v.setBackgroundColor(Color.RED);

        holder.number.setText(String.valueOf(bikeStations.get(position).getNumber()));
        holder.name.setText(bikeStations.get(position).getName());
        holder.available.setText(String.valueOf(bikeStations.get(position).getAvailable()));

        return v;
    }
}
