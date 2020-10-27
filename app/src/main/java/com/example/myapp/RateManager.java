package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RateManager {

    private com.example.myapp.DBHelper dbHelper;
    private String TBNAME;



    public RateManager(Context context){
        dbHelper = new com.example.myapp.DBHelper(context);
        TBNAME = com.example.myapp.DBHelper.TB_NAME;
    }

    public  void add(RateItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("country",item.getCurName());
        values.put("country",item.getCurName());

        db.insert(TBNAME,null,values);
        db.close();
    }

    public void addAll(List<RateItem> list){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

            for(RateItem item : list){
                ContentValues values = new ContentValues();
                values.put("curName", item.getCurName());
                values.put("curRate", item.getCurRate());
                db.insert(TBNAME, null, values);
            }
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, null, null);
        db.close();
    }

    public void update(RateItem item){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("curName", item.getCurName());
        values.put("curRate", item.getCurRate());
        db.update(TBNAME, values, "id=?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public List<RateItem> listAll(){

        List<RateItem> rateList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if (cursor!=null){
            rateList = new ArrayList<RateItem>();
            while (cursor.moveToNext()){
                RateItem item = new RateItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("id")));
                item.setCurName(cursor.getString(cursor.getColumnIndex("curName")));
                item.setCurRate(cursor.getString(cursor.getColumnIndex("curRate")));

                rateList.add(item);
            }
            cursor.close();
        }
        db.close();
        return rateList;

    }

    public RateItem findById(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, "id=?" ,new String[]{String.valueOf(id)},null, null, null);

        RateItem item = null;
        if(cursor!=null && cursor.moveToFirst()){
            item = new RateItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("id")));
            item.setCurName(cursor.getString(cursor.getColumnIndex("curName")));
            item.setCurRate(cursor.getString(cursor.getColumnIndex("curRate")));
            cursor.close();
        }

        db.close();
        return item;
    }
}
