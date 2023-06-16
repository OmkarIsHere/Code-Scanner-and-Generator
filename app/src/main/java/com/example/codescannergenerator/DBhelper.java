package com.example.codescannergenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "DB_EncodeData";
    private static final int DB_VERSION = 1;
    private static final String TABLE_EncodeData = "EncodeData";
    private static final String EID = "EId";
    private static final String ENCODE = "EncodeText";
    private static final String DATE = "Date";

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table tablename (id integer primary key autoincrement, encode text, date text);
        db.execSQL("create table " + TABLE_EncodeData +" ( "+ EID +" integer primary key autoincrement,  " + ENCODE + " text, "+ DATE + " text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_EncodeData);
        onCreate(db);
    }

    public void addData(String text, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ENCODE, text);
        values.put(DATE, date);

        db.insert(TABLE_EncodeData, null, values);
    }

    public ArrayList<EncodeData> getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from "+TABLE_EncodeData + " order by " + EID +" desc limit 20", null);
        ArrayList<EncodeData> arrayList = new ArrayList<>();
        while(cursor.moveToNext()){
            EncodeData encodeData = new EncodeData();
            encodeData.id = cursor.getInt(0);
            encodeData.text = cursor.getString(1);
            encodeData.date = cursor.getString(2);
            arrayList.add(encodeData);
        }

        return arrayList;
    }
}
