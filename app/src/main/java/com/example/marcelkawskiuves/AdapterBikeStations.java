package com.example.marcelkawskiuves;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.concurrent.ExecutionException;


public class AdapterBikeStations extends android.widget.BaseAdapter{

    private ArrayList<BikeStation> bikeStations;
    private DBHelper dbHelper;
    private DataCollector dataCollector;
    Context context;

    static class ViewHolder {
        TextView number;
        TextView name;
        TextView available;
        ImageView bike;
    }

    public AdapterBikeStations(Context c) {

        this.context = c;
        dbHelper = new DBHelper(c);
        dataCollector = new DataCollector();
        Init();
    }

    public void Init() {

        try {
            bikeStations = dataCollector.execute().get();
            //Collections.sort(bikeStations, new StationsComparator());
            // TODO sortowanie
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getCount() {
        return bikeStations.size();
    }

    @Override
    public BikeStation getItem(int number){
        for (BikeStation bikeStation: bikeStations) {
            if (bikeStation.getNumber() == number)
                return bikeStation;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return bikeStations.get(position).getNumber();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater li =
                    (LayoutInflater)context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.bikestationview, null) ;
            holder = new ViewHolder();

            holder.number = v.findViewById(R.id.bikestationviewnumber);
            holder.name = v.findViewById(R.id.bikestationviewname);
            holder.available = v.findViewById(R.id.bikestationviewavailable);
            holder.bike = v.findViewById(R.id.bike);
            v.setTag(holder);
        } else {
            holder = (ViewHolder)v.getTag();
        }

        if (bikeStations.get(position).getAvailable() > 10) {
            v.setBackgroundColor(Color.GREEN);
        }
        else if (bikeStations.get(position).getAvailable() > 4)
            v.setBackgroundColor(Color.YELLOW);
        else v.setBackgroundColor(Color.RED);

        holder.number.setText(String.valueOf(bikeStations.get(position).getNumber()));
        holder.name.setText(bikeStations.get(position).getName());
        holder.available.setText(String.valueOf(bikeStations.get(position).getAvailable()));

        return v;
    }

    class StationsComparator implements Comparator<BikeStation> {

        @Override
        public int compare(BikeStation s1, BikeStation s2) {
            int n1 = s1.getNumber();
            int n2 = s2.getNumber();
            if (n1 > n2) {
                return 1;
            } else if (n2 > n1) {
                return -1;
            }
            return 0;
        }
    }

}
