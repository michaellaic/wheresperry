package com.example.wheresperry.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.wheresperry.R;
import com.example.wheresperry.data.GameManager;
import com.google.android.material.button.MaterialButton;

public class ModeActivity extends AppCompatActivity {

    private MaterialButton mode_BTN_buttons;
    private MaterialButton mode_BTN_sensor;
    boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        findViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {

        final int BTN_SENSOR_MODE = R.id.mode_BTN_sensor;
        final int BTN_BUTTON_MODE = R.id.mode_BTN_buttons;
        View.OnClickListener listener = v -> {
            Intent intent;
            switch (v.getId()) {
                case BTN_SENSOR_MODE:
                    GameManager.getInstance().setSensorMode(true);
                    GameManager.getInstance().setButtonMode(false);
                    intent = new Intent(ModeActivity.this, GameActivity.class);
                    break;
                case BTN_BUTTON_MODE:
                    GameManager.getInstance().setfastMode(checked);
                    GameManager.getInstance().setSensorMode(false);
                    GameManager.getInstance().setButtonMode(true);
                    intent = new Intent(ModeActivity.this, GameActivity.class);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid view id");
            }
            finish();
            startActivity(intent);
        };

        mode_BTN_sensor.setOnClickListener(listener);
        mode_BTN_buttons.setOnClickListener(listener);
    }

    private void findViews() {
        mode_BTN_buttons = findViewById(R.id.mode_BTN_buttons);
        mode_BTN_sensor = findViewById(R.id.mode_BTN_sensor);
    }

}