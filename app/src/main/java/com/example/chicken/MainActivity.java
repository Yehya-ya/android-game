package com.example.chicken;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    MainLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        mainLayout = new MainLayout(this, point.x, point.y);
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