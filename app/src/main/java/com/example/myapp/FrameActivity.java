package com.example.myapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FrameActivity extends FragmentActivity {

    private Fragment mFragements[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton rbtHome, rbtFunc, rbtSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        mFragements = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragements[0] = fragmentManager.findFragmentById(R.id.fragment_main);
        mFragements[1] = fragmentManager.findFragmentById(R.id.fragment_func);
        mFragements[2] = fragmentManager.findFragmentById(R.id.fragment_setting);
        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragements[0]).hide(mFragements[1]).hide(mFragements[2]);
        fragmentTransaction.show(mFragements[0]).commit();

        rbtHome = (RadioButton)findViewById(R.id.radioHome);
        rbtFunc = (RadioButton)findViewById(R.id.radioFunc);
        rbtSetting = (RadioButton)findViewById(R.id.radioSetting);

        radioGroup = (RadioGroup)findViewById(R.id.bottomGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("radioGroup", "checkId=" + checkedId);

                fragmentTransaction = fragmentManager.beginTransaction().hide(mFragements[0]).hide(mFragements[1]).hide(mFragements[2]);
                switch (checkedId){
                    case R.id.radioHome:
                        fragmentTransaction.show(mFragements[0]).commit();
                        break;
                    case R.id.radioFunc:
                        fragmentTransaction.show(mFragements[1]).commit();
                        break;
                    case R.id.radioSetting:
                        fragmentTransaction.show(mFragements[2]).commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }


}