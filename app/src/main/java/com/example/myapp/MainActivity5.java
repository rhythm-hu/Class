package com.example.myapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

public class MainActivity5 extends ListActivity implements Runnable {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main5);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    List<HashMap<String, String>> list = (List<HashMap<String, String>>) msg.obj;
                    ListAdapter adapter = new SimpleAdapter(com.example.myapp.MainActivity5.this,list,R.layout.list_item,new String[]{"Country","Rate"},new int[]{R.id.Country,R.id.Rate});
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }

        };

        getListView().setOnItemClickListener((AdapterView.OnItemClickListener) com.example.myapp.MainActivity5.this);
    }
    

    @Override
    public void run() {

    }
}