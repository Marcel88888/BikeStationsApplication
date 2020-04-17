package com.example.marcelkawskiuves;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class BikeStationsList extends AppCompatActivity {

    ListView stationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bikestationslistview);

        stationsList = findViewById(R.id.stationsList);
        this.setTitle("ValenBisi");

        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent act2 = new Intent(BikeStationsList.this, BikeStationDetails.class);
                act2.putExtra("bikeStationId", Integer.parseInt(((TextView) view.findViewById(R.id.bikestationviewnumber)).getText().toString()));
                startActivity(act2);
            }
        });
        stationsList.setAdapter((new AdapterBikeStations(this)));
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
                stationsList.setAdapter(new AdapterBikeStations(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stationsList.setAdapter(new AdapterBikeStations(this));
    }
}
