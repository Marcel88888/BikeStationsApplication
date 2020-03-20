package com.example.marcelkawskiuves;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class BikeStationReport extends AppCompatActivity {

    Intent intent;
    Spinner status, type;
    EditText title, description;
    DBHelper dbHelper;
    int stationId, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bikestationreport);

        intent = getIntent();
        stationId = intent.getIntExtra("stationId", -1);
        id = intent.getIntExtra("id", -1);

        title = findViewById(R.id.titleET);
        description = findViewById(R.id.descriptionET);
        status = findViewById(R.id.statusSp);
        type = findViewById(R.id.typeSp);

        dbHelper = BikeStationDetails.getDbHelper();

        String[] statuses = getResources().getStringArray(R.array.report_statuses);
        String[] types = getResources().getStringArray(R.array.report_types);

        ArrayAdapter<String> statusSAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        statusSAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(statusSAA);

        ArrayAdapter<String> typeSAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeSAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeSAA);

        if (id == -1)
            setTitle("New report");
        else {
            Cursor cursor = dbHelper.findReportById(id);
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.REPORT_NAME);
                int descriptionIndex = cursor.getColumnIndex(DBHelper.REPORT_DESCRIPTION);
                int statusIndex = cursor.getColumnIndex(DBHelper.REPORT_STATUS);
                int typeIndex = cursor.getColumnIndex(DBHelper.REPORT_TYPE);

                setTitle("Edit " + cursor.getString(nameIndex));
                title.setText(cursor.getString(nameIndex));
                description.setText(cursor.getString(descriptionIndex));
                status.setSelection(getIndex(status, cursor.getString(statusIndex)));
                type.setSelection(getIndex(type, cursor.getString(typeIndex)));
            }
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_report:
                saveReport();
                return true;
            case R.id.delete_report:
                deleteReport();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteReport() {
        if (id == -1)
            setResult(RESULT_CANCELED);
        else {
            dbHelper.deleteReport(id);
            setResult(RESULT_OK);
        }
        finish();
    }

    public void saveReport() {
        if (id == -1) {
            dbHelper.insertReport(stationId,
                    title.getText().toString(),
                    description.getText().toString(),
                    status.getSelectedItem().toString(),
                    type.getSelectedItem().toString());
        }
        else {
            dbHelper.updateReport(stationId,
                    id,
                    title.getText().toString(),
                    description.getText().toString(),
                    status.getSelectedItem().toString(),
                    type.getSelectedItem().toString());
        }
        setResult(RESULT_OK);
        finish();
    }


    private int getIndex(Spinner spinner, String str) {
        for (int i=0; i<spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(str)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
