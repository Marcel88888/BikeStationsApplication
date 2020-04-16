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
        TextView distance;
        TextView reports;
        TextView available;
    }

    public AdapterBikeStations(Context c) {

        this.context = c;
        this.dbHelper = new DBHelper(c);
        this.dataCollector = new DataCollector();
        Init();
        for (int i=0; i<bikeStations.size();i++)
            System.out.println(bikeStations.get(i).getNumber());
    }

    public void Init() {

        try {
            bikeStations = dataCollector.execute().get();
            Collections.sort(bikeStations, new StationsComparator());
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
            holder.distance = v.findViewById(R.id.bikestationviewdistance);
            holder.reports = v.findViewById(R.id.bikestationviewreports);
            holder.available = v.findViewById(R.id.bikestationviewavailable);
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
        holder.distance.setText(String.valueOf(bikeStations.get(position).getDistance()));
        holder.reports.setText(String.valueOf(dbHelper.countReports(bikeStations.get(position).getNumber())));
        holder.available.setText(String.valueOf(bikeStations.get(position).getAvailable()));

        return v;
    }

    class StationsComparator implements Comparator<BikeStation> {

        @Override
        public int compare(BikeStation bs1, BikeStation bs2) {
            bs1.calculateDistance();
            bs2.calculateDistance();
            double distance1 = bs1.getDistance();
            double distance2 = bs2.getDistance();
            if (distance1 < distance2) {
                return 1;
            } else if (distance1 > distance2) {
                return -1;
            }
            return 0;
        }
    }

}
