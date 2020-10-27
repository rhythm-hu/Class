package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.text.DecimalFormat;

public class MainActivity2 extends AppCompatActivity {

    EditText d,e,w;
    final DecimalFormat df   = new DecimalFormat("######0.0000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        d=findViewById(R.id.text4);
        e=findViewById(R.id.text5);
        w=findViewById(R.id.text6);

        Intent intent = getIntent();
        double d2 = intent.getDoubleExtra("drk",0.0f);
        double e2 = intent.getDoubleExtra("erk",0.0f);
        double w2 = intent.getDoubleExtra("wrk",0.0f);

        d.setText("USD:"+df.format(d2));
        e.setText("EUR:"+df.format(e2));
        w.setText("KRW:"+df.format(w2));

    }
    public void btn6(View v){
        Intent intent = getIntent();
        Bundle bdl = new Bundle();

        bdl.putDouble("k_d",Double.valueOf(d.getText().toString()));
        bdl.putDouble("k_e",Double.valueOf(e.getText().toString()));
        bdl.putDouble("k_w",Double.valueOf(w.getText().toString()));

        intent.putExtras(bdl);
        setResult(2,intent);

        finish();
    }
}