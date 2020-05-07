package com.example.marcelkawskiuves;

import android.location.Location;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ListView;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;


public class BikeStationDetails extends AppCompatActivity {

    private Intent intent;
    private BikeStation bikeStation;
    private int stationIndex;
    private Location deviceLocation;
    private String dist;
    private String distanceUnit;
    private TextView number;
    private TextView address;
    private TextView total;
    private TextView available;
    private TextView free;
    private TextView coordinates;
    private TextView distance;
    private ListView reportsListView;
    private static DBHelper dbHelper;

    public static DBHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bikestationdetails);

        dbHelper = new DBHelper(this);
        intent = getIntent();
        //stationId = intent.getIntExtra("bikeStationId", -1);
        //stationIndex = intent.getIntExtra("listViewPosition", -1);
        //deviceLocation = intent.getExtras().getParcelable("deviceLocation");
        //dist = intent.getStringExtra("distanceString");
        //distanceUnit = intent.getStringExtra("distanceUnit");
        //System.out.println(distanceUnit);
        //double dist = intent.getDoubleExtra("distance", -1);
        //String distanceUnit = intent.getStringExtra("distanceUnit");
        //bikeStation = new AdapterBikeStations(this, deviceLocation).getItem(stationIndex);
        bikeStation = (BikeStation) intent.getSerializableExtra("bikeStation");
        System.out.println("BIKE STATIOOOOOOOOON");
        System.out.println(bikeStation.getNumber());

        number = findViewById(R.id.numberTV);
        address = findViewById(R.id.addressTV);
        total = findViewById(R.id.totalTV);
        available = findViewById(R.id.availableTV);
        free = findViewById(R.id.freeTV);
        coordinates = findViewById(R.id.coordinatesTV);
        distance = findViewById(R.id.distanceTV);

        reportsListView = findViewById(R.id.reportsLV);

        String name = bikeStation.getName();
        name = name.substring(name.indexOf("_")+1).replaceAll("_", " ");

        //dist = dist + " " + distanceUnit;

        setTitle(name);
        number.setText(String.valueOf(bikeStation.getNumber()));
        address.setText(bikeStation.getAddress());
        total.setText(String.valueOf(bikeStation.getTotal()));
        available.setText(String.valueOf(bikeStation.getAvailable()));
        free.setText(String.valueOf(bikeStation.getFree()));
        String strCoordinate = bikeStation.getCoordinate1() + ", " + bikeStation.getCoordinate2();
        coordinates.setText(strCoordinate);
        distance.setText(String.valueOf(bikeStation.getDistance()));

        reportsListView.setAdapter(new CAdapter(this, dbHelper.findReportByBikeStation(bikeStation.getNumber()), 0));

        reportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BikeStationDetails.this, BikeStationReport.class);
                intent.putExtra("stationId", bikeStation.getNumber());
                intent.putExtra("reportId", Integer.parseInt(((TextView) view.findViewById(R.id.reportId)).getText().toString()));
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_report:
                Intent intent = new Intent(BikeStationDetails.this, BikeStationReport.class);
                intent.putExtra("stationId", bikeStation.getNumber());
                intent.putExtra("id", -1);
                startActivityForResult(intent, 1);
                return true;
            case R.id.location:
                showOnMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showOnMap() {
        double latitude = bikeStation.getCoordinate1();
        double longitude = bikeStation.getCoordinate2();
        String stationName = bikeStation.getName();

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + latitude + "," + longitude + "(" + stationName + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            reportsListView.setAdapter(new CAdapter(this, dbHelper.findReportByBikeStation(bikeStation.getNumber()), 0));
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
