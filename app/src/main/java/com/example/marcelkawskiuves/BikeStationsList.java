package com.example.marcelkawskiuves;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.SyncFailedException;
import java.util.Collections;


public class BikeStationsList extends AppCompatActivity implements LocationListener {

    private ListView stationsList;
    private Location deviceLocation;
    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bikestationslistview);

        stationsList = findViewById(R.id.stationsList);
        this.setTitle("ValenBisi");

        final AdapterBikeStations adapterBikeStations = new AdapterBikeStations(this, deviceLocation);
        stationsList.setAdapter(adapterBikeStations);

        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        //boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //if(network_enabled){
        //deviceLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //}

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent act2 = new Intent(BikeStationsList.this, BikeStationDetails.class);
                /*
                System.out.println("WAZNEEEEEEEEEEEEE");
                for (BikeStation bikeStation: adapterBikeStations.getBikeStations()) {
                    System.out.println(bikeStation.getNumber());
                }
                */
                act2.putExtra("bikeStation", adapterBikeStations.getItem(position));
                //act2.putExtra("listViewPosition", position);
                //act2.putExtra("deviceLocation", deviceLocation);
                //act2.putExtra("distanceString", adapterBikeStations.getDistanceString());
                //act2.putExtra("distanceUnit", adapterBikeStations.getDistanceUnit());
                //act2.putExtra("distance", Double.valueOf(((TextView) view.findViewById(R.id.bikestationviewdistance)).getText().toString()));
                //act2.putExtra("distanceUnit", (((TextView) view.findViewById(R.id.bikestationviewdistanceunit)).getText().toString()));
                startActivity(act2);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                final AdapterBikeStations newAdapterBikeStations = new AdapterBikeStations(this, deviceLocation);
                stationsList.setAdapter(newAdapterBikeStations);
                stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent act2 = new Intent(BikeStationsList.this, BikeStationDetails.class);
                        System.out.println("WAZNEEEEEEEEEEEEE");
                        for (BikeStation bikeStation: newAdapterBikeStations.getBikeStations()) {
                            System.out.println(bikeStation.getNumber());
                        }
                        act2.putExtra("bikeStation", newAdapterBikeStations.getItem(position));
                        startActivity(act2);
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stationsList.setAdapter(new AdapterBikeStations(this, deviceLocation));
    }

    @Override
    public void onLocationChanged(Location location) {
        deviceLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, "location fail", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
