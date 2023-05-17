package com.example.wheresperry.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.wheresperry.R;
import com.example.wheresperry.data.GameManager;
import com.google.android.material.button.MaterialButton;

public class GameOverActivity extends AppCompatActivity {
    private AppCompatImageView gameOver_IMG_back;
    private MaterialButton gameOver_BTN_enter;
    private EditText gameOver_EDT_name;
    private GameManager gameManager;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        findViews();
        initViews();
    }
    private void findViews() {
        gameOver_EDT_name = findViewById(R.id.gameover_EDT_name);
        gameOver_BTN_enter = findViewById(R.id.gameover_BTN_enter);


    }

    private void initViews() {
        gameOver_BTN_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = gameOver_EDT_name.getText().toString();
                if (!name.equals("")){
                    Intent intent = new Intent(GameOverActivity.this, ScoreActivity.class);
                    startActivity(intent);
                    saveScoreRecord();
                }
            }
        });

    }

    public void saveScoreRecord() {
        gameManager.getInstance().saveNewScoreRecord(name);
    }

}