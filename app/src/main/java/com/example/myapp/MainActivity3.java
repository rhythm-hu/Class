package com.example.myapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity3 extends ListActivity implements Runnable {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main3);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    List<HashMap<String, String>> list = (List<HashMap<String, String>>) msg.obj;
                    ListAdapter adapter = new SimpleAdapter(com.example.myapp.MainActivity3.this,list,R.layout.list_item,new String[]{"Country","Rate"},new int[]{R.id.Country,R.id.Rate});
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }

        };

        getListView().setOnItemClickListener((AdapterView.OnItemClickListener) com.example.myapp.MainActivity3.this);
    }

    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        HashMap<String,String> map = (HashMap<String, String>)getListView().getItemAtPosition(position);
        String country = map.get("Country");
        String rate  = map.get("Rate");

        String title = String.valueOf(((TextView)view.findViewById(R.id.Country)).getText());
        String detail = String.valueOf(((TextView)view.findViewById(R.id.Rate)).getText());

        Intent rateCalc = new Intent(this, com.example.myapp.RateCalcAct.class);
        rateCalc.putExtra("country",title);
        rateCalc.putExtra("rate",Float.parseFloat(detail));
        startActivity(rateCalc);
    }

        @Override
        public void run () {
            String url = "https://www.usd-cny.com/bankofchina.htm";
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<HashMap<String, String>> message = getMessage(doc);
            Message msg = handler.obtainMessage(2);
            msg.obj = message;
            handler.sendMessage(msg);
        }
        private List<HashMap<String, String>> getMessage (Document doc){
            Elements tables = doc.getElementsByTag("table");
            Element table = tables.get(0);
            List<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
            Elements trs = table.getElementsByTag("tr");
            Element e = null;
            for (int i = 1; i < trs.size(); i++) {
                e = trs.get(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Country", "  "+e.getElementsByTag("td").get(0).text());
                map.put("Rate", e.getElementsByTag("td").get(5).text()+"  ");
                listItems.add(map);
            }

            return listItems;
        }
    }

