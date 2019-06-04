package com.myapplicationdev.android.p07_smsretriever;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment fragment1 = new FragmentFirst();
        ft.replace(R.id.frame1,fragment1);

        Fragment fragment2 = new FragmentSecond();
        ft.replace(R.id.frame2,fragment2);

        ft.commit();
    }
}
