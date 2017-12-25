package com.renj.imageselect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private int[] images = {R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, R.mipmap.a1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
