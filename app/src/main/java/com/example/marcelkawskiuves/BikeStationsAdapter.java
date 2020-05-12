package com.example.marcelkawskiuves;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.concurrent.ExecutionException;


public class BikeStationsAdapter extends android.widget.BaseAdapter {

    private ArrayList<BikeStation> bikeStations;
    private DBHelper dbHelper;
    private DataCollector dataCollector;
    private Location deviceLocation;
    private Context context;

    static class ViewHolder {
        TextView number;
        TextView name;
        TextView distance;
        TextView reports;
        TextView available;
    }

    public BikeStationsAdapter(Context c, Location location, BikeStationsListActivity bikeStationsListActivity) {

        this.context = c;
        this.dbHelper = new DBHelper(c);
        this.dataCollector = new DataCollector();
        this.deviceLocation = location;
        final BikeStationsListActivity BSActivity = bikeStationsListActivity;
        bikeStationsListActivity.getStationsList().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent act2 = new Intent(BSActivity, DetailsActivity.class);
                BikeStation bikeStation = getItem(position);
                act2.putExtra("bikeStation", bikeStation);
                BSActivity.startActivity(act2);
            }
        });
        try {
            this.bikeStations = dataCollector.execute().get();
            for (BikeStation bikeStation: bikeStations) {
                bikeStation.calculateDistance(deviceLocation);
                double distance = bikeStation.getDistance();
                if (distance >= 1000) {
                    distance /= 1000;
                    bikeStation.setDistanceString(String.format("%.1f", distance));
                    bikeStation.setDistanceUnit("km");
                }
                else {
                    bikeStation.setDistanceString(String.format("%.0f", distance));
                    bikeStation.setDistanceUnit("m");
                }
            }
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
    public BikeStation getItem(int index){
        return bikeStations.get(index);
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
            v = li.inflate(R.layout.bike_station, null) ;
            holder = new ViewHolder();

            holder.number = v.findViewById(R.id.bikeStationViewNumber);
            holder.name = v.findViewById(R.id.bikeStationViewName);
            holder.distance = v.findViewById(R.id.bikeStationViewDistance);
            holder.reports = v.findViewById(R.id.bikeStationViewReports);
            holder.available = v.findViewById(R.id.bikeStationViewAvailable);
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

        BikeStation bikeStation = bikeStations.get(position);
        String name = bikeStation.getName();
        name = name.substring(name.indexOf("_")+1).replaceAll("_", " ");

        holder.number.setText(String.valueOf(bikeStation.getNumber()));
        holder.name.setText(name);
        String distanceDisplayString = bikeStation.getDistanceString() + " " + bikeStation.getDistanceUnit();
        holder.distance.setText(distanceDisplayString);
        holder.reports.setText(String.valueOf(dbHelper.countReports(bikeStation.getNumber())));
        holder.available.setText(String.valueOf(bikeStation.getAvailable()));

        return v;
    }


    public class StationsComparator implements Comparator<BikeStation> {

        @Override
        public int compare(BikeStation bs1, BikeStation bs2) {
            double distance1 = bs1.getDistance();
            double distance2 = bs2.getDistance();
            if (distance1 > distance2) {
                return 1;
            } else if (distance1 < distance2) {
                return -1;
            }
            return 0;
        }
    }

}
