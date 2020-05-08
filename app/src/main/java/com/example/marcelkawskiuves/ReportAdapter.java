package com.example.marcelkawskiuves;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportAdapter extends CursorAdapter {

    ReportAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.report_row, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int statusIndex = cursor.getColumnIndex(DBHelper.REPORT_STATUS);
        int nameIndex = cursor.getColumnIndex(DBHelper.REPORT_NAME);

        ImageView image = view.findViewById(R.id.reportDot);
        TextView title = view.findViewById(R.id.reportTitle);

        switch(cursor.getString(statusIndex)) {
            case "Open":
                image.setImageResource(R.drawable.red_dot);
                break;
            case "Processing":
                image.setImageResource(R.drawable.yellow_dot);
                break;
            case "Closed":
                image.setImageResource(R.drawable.green_dot);
                break;
        }
        title.setText(cursor.getString(nameIndex));
    }
}
