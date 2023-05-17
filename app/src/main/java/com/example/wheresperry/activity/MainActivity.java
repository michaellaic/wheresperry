package com.example.wheresperry.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wheresperry.R;
import com.example.wheresperry.init.MyGPS;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    MaterialButton game_BTN_start,game_BTN_top_10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        initGPS();
    }
    private void initGPS(){
           MyGPS.getInstance().checkPermissions(this);
    }

    private void findViews() {
        game_BTN_top_10=findViewById(R.id.game_BTN_top_10);
        game_BTN_start=findViewById(R.id.game_BTN_start);
    }

    private void initViews() {

        game_BTN_top_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(intent);
            }
        });
        game_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, ModeActivity.class);
                startActivity(intent2);
            }
        });


    }
}