package com.example.marcelkawskiuves;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class ReportActivity extends AppCompatActivity {

    private Intent intent;
    private Spinner status, type;
    private EditText title, description;
    private ImageView photo;
    private DBHelper dbHelper;
    private int stationId, id;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        intent = getIntent();
        stationId = intent.getIntExtra("stationId", -1);
        id = intent.getIntExtra("reportId", -1);

        title = findViewById(R.id.titleET);
        description = findViewById(R.id.descriptionET);
        status = findViewById(R.id.statusSp);
        type = findViewById(R.id.typeSp);
        photo = findViewById(R.id.photo);

        dbHelper = DetailsActivity.getDbHelper();

        String[] statuses = getResources().getStringArray(R.array.report_statuses);
        String[] types = getResources().getStringArray(R.array.report_types);

        ArrayAdapter<String> statusSAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        statusSAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(statusSAA);

        ArrayAdapter<String> typeSAA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeSAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeSAA);

        if (id == -1)
            this.setTitle("New report");
        else {
            Cursor cursor = dbHelper.findReportById(id);
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBHelper.REPORT_NAME);
                int descriptionIndex = cursor.getColumnIndex(DBHelper.REPORT_DESCRIPTION);
                int statusIndex = cursor.getColumnIndex(DBHelper.REPORT_STATUS);
                int typeIndex = cursor.getColumnIndex(DBHelper.REPORT_TYPE);
                int photoIndex = cursor.getColumnIndex(DBHelper.REPORT_PHOTO);

                this.setTitle("Edit " + cursor.getString(nameIndex));
                title.setText(cursor.getString(nameIndex));
                description.setText(cursor.getString(descriptionIndex));
                status.setSelection(getIndex(status, cursor.getString(statusIndex)));
                type.setSelection(getIndex(type, cursor.getString(typeIndex)));
                photo.setImageBitmap(BitmapFactory.decodeByteArray(cursor.getBlob(photoIndex), 0, cursor.getBlob(photoIndex).length));
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
            case R.id.addPhoto:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                return true;
            case R.id.saveReport:
                saveReport();
                return true;
            case R.id.deleteReport:
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
                    type.getSelectedItem().toString(),
                    getPhotoBytesArray(imageViewToBitmap(photo)));
        }
        else {
            dbHelper.updateReport(stationId,
                    id,
                    title.getText().toString(),
                    description.getText().toString(),
                    status.getSelectedItem().toString(),
                    type.getSelectedItem().toString(),
                    getPhotoBytesArray(imageViewToBitmap(photo)));
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photoBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(photoBitmap);
        }
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

    public static byte[] getPhotoBytesArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private Bitmap imageViewToBitmap(ImageView imageView){
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return drawable.getBitmap();
    }
}
