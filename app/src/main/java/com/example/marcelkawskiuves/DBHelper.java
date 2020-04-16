package com.example.marcelkawskiuves;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "BikeStationsReports";
    public static final String TABLE_NAME = "Reports";

    public static final String REPORT_ID = "_id";
    public static final String STATION_ID = "station_id";
    public static final String REPORT_NAME = "name";
    public static final String REPORT_DESCRIPTION = "description";
    public static final String REPORT_STATUS = "status";
    public static final String REPORT_TYPE = "type";

    private SQLiteDatabase dataBase;
    private ContentValues contentValues;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        dataBase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists " + TABLE_NAME + "(" +
                        REPORT_ID + " integer primary key autoincrement," +
                        STATION_ID + " integer," +
                        REPORT_NAME + " text," +
                        REPORT_DESCRIPTION + " text," +
                        REPORT_STATUS + " text," +
                        REPORT_TYPE + " text" + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public int countReports(int bikeStationId) {
        SQLiteStatement sqLiteStatement = dataBase.compileStatement("select count(*) from " +
                TABLE_NAME + " where " + STATION_ID + " == " + bikeStationId);
        return (int)sqLiteStatement.simpleQueryForLong();
    }

    public void insertReport(Integer stationId, String name, String description, String status, String type) {
        contentValues = new ContentValues();

        contentValues.put(DBHelper.STATION_ID, stationId);
        contentValues.put(DBHelper.REPORT_NAME, name);
        contentValues.put(DBHelper.REPORT_DESCRIPTION, description);
        contentValues.put(DBHelper.REPORT_STATUS, status);
        contentValues.put(DBHelper.REPORT_TYPE, type);

        dataBase.insert(DBHelper.TABLE_NAME, null, contentValues);
    }

    public void updateReport(Integer stationId, Integer reportId, String name, String description, String status, String type) {
        contentValues = new ContentValues();

        contentValues.put(DBHelper.STATION_ID, stationId);
        contentValues.put(DBHelper.REPORT_NAME, name);
        contentValues.put(DBHelper.REPORT_DESCRIPTION, description);
        contentValues.put(DBHelper.REPORT_STATUS, status);
        contentValues.put(DBHelper.REPORT_TYPE, type);

        dataBase.update(DBHelper.TABLE_NAME, contentValues, DBHelper.REPORT_ID + "=" + reportId, null);
    }

    public void deleteReport(Integer reportId) {
        dataBase.delete(DBHelper.TABLE_NAME, DBHelper.REPORT_ID + "=" + reportId, null);
    }

    public Cursor findReportByBikeStation(Integer stationId) {
        return dataBase.query(DBHelper.TABLE_NAME,
                new String[]{DBHelper.REPORT_ID, DBHelper.REPORT_STATUS, DBHelper.REPORT_NAME},
                DBHelper.STATION_ID + "=" + stationId,
                null, null, null, null);
    }

    public Cursor findReportById(Integer reportId) {
        return dataBase.query(DBHelper.TABLE_NAME,
                new String[] {DBHelper.REPORT_NAME, DBHelper.REPORT_DESCRIPTION, DBHelper.REPORT_STATUS, DBHelper.REPORT_TYPE},
                DBHelper.REPORT_ID + "=" + reportId, null, null, null, null);
    }
}
