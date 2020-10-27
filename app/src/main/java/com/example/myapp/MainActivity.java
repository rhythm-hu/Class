package com.example.myapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "ERC";
    EditText inp ;
    TextView out ;
    Handler handler;


    final DecimalFormat  df   = new DecimalFormat("######0.0000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inp = findViewById(R.id.text2);
        out = findViewById(R.id.text1);

        handler = new Handler(){
            public void handleMessage(Message msg) {
                if(msg.what == 2){
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: msg =" + str);
                }
                super.handleMessage(msg);
            }
        };

        Thread t=new Thread(this);
        t.start();

    }

    public void btn1(View v) {
        if (inp.getText().toString().length() == 0) {
            Toast.makeText(this, "Input The Amount", Toast.LENGTH_SHORT).show();
        } else {
            double F = Double.valueOf(inp.getText().toString());
            F = F * 0.1473;
            out.setText(df.format(F));
        }
    }

    public void btn2(View v) {
        if (inp.getText().toString().length() == 0) {
            Toast.makeText(this, "Input The Amount", Toast.LENGTH_SHORT).show();
        } else {
            double F = Double.valueOf(inp.getText().toString());
            F = F * 0.1252;
            out.setText(df.format(F));
        }
    }

    public void btn3(View v){
        if (inp.getText().toString().length() == 0) {
            Toast.makeText(this, "Input The Amount", Toast.LENGTH_SHORT).show();
        } else {
            double F = Double.valueOf(inp.getText().toString());
            F = F * 171.4292;
            out.setText(df.format(F));
        }
    }

    public void btn4(View v) {
        if (inp.getText().toString().length() == 0) {
            Toast.makeText(this, "Input The Amount", Toast.LENGTH_SHORT).show();
        } else {
            inp.setText("");
            out.setText(R.string.text1);
        }
    }

    public void btn5(View v){
        if (inp.getText().toString().length() == 0) {
            Toast.makeText(this, "Input The Amount", Toast.LENGTH_SHORT).show();
        } else {
            Intent config = new Intent(this, MainActivity2.class);

            float F = Float.valueOf(inp.getText().toString());
            float DR = (float) (F * 0.1473);
            float ER = (float) (F * 0.1252);
            float WR = (float) (F * 171.4292);

            config.putExtra("drk", DR);
            config.putExtra("erk", ER);
            config.putExtra("wrk", WR);

            Log.i(TAG, "openOne:dollarRate = " + DR);
            Log.i(TAG, "openOne:euroRate = " + ER);
            Log.i(TAG, "openOne:wonRate = " + WR);

            SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("d_rate",DR);
            editor.putFloat("e_rate",ER);
            editor.putFloat("w_rate",WR);
            editor.apply();

            startActivity(config);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void run() {
        URL url = null;
        String html = "";
        try{
            url = new URL("https://www.usd-cny.com/bankofchina.htm");
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            html = inputStream2String(in);
            Log.i(TAG, "run: html:" + html);
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage(1);
        msg.obj = html;
        handler.sendMessage(msg);

        String url1 = "https://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        try {
            doc = Jsoup.connect(url1).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG,"run:"+doc.title());
        Elements tables = doc.getElementsByTag("table");

        Element table6 = tables.get(0);

        Elements tds = table6.getElementsByTag("td");
        for(int i=0;i<tds.size();i+=6){
            Element td1 = tds.get(i);
            Element td2 = tds.get(i+5);

            String str1 = td1.text();
            String val = td2.text();
            Log.i(TAG,"run:"+ str1 + "==>" + val);

            float v = 100f/Float.parseFloat(val);
        }


    }

    private String inputStream2String(InputStream inputStream) throws IOException{
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"utf8");
        while(true){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz < 0) break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}