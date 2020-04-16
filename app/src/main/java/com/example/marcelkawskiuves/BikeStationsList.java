package com.example.marcelkawskiuves;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.TextView;


public class BikeStationsList extends Activity {

    ListView stationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bikestationslistview);

        stationsList = findViewById(R.id.stationsList);

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
    protected void onResume() {
        super.onResume();
        stationsList.setAdapter(new AdapterBikeStations(this));
    }
}
