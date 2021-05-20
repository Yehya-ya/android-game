package com.example.chicken;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MainLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = new MainLayout(this);
        setContentView(mainLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainLayout.Pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainLayout.Resume();
    }
}