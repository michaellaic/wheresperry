package com.example.wheresperry.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wheresperry.R;
import com.example.wheresperry.data.GameManager;
import com.example.wheresperry.data.Item;
import com.example.wheresperry.init.MyGPS;
import com.example.wheresperry.init.MySignal;
import com.example.wheresperry.utilities.SensorDetector;
import com.example.wheresperry.utilities.TypeItem;
import com.example.wheresperry.utilities.TypeVisibility;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

        private ArrayList<AppCompatImageView> game_IMG_hearts;
        private ArrayList<ArrayList<AppCompatImageView>> game_IMG_Characters;
        private ArrayList<AppCompatImageView> game_IMG_Perrys;
        private MaterialButton game_BTN_right;
        private MaterialButton game_BTN_left;
        private MaterialButton game_BTN_restart;
        private MaterialButton game_BTN_pause;
        private final int HEARTS = 3;
        private int lives = HEARTS;
        private Timer timerUpdateUI;
        private boolean isActiveController = true;
        private final int ROWS = 8, COLS = 5;
        private GameManager gameManager;
        private boolean pause = false;
        private SensorDetector sensorDetector;
        private TextView game_LBL_score;
        private TextView game_LBL_scoreNum;
        private int roundsToDoof = 0;
        private int num=1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);
            findViews();
            init();
            initView();
            updateUI();
        }

        private void Move(String direction) {
            if (isActiveController) {
                gameManager.move(direction);
                renderPerry();
            }
        }

        public void updateUI() {
            if (gameManager.isBallCollision()) {
                renderCollision("boom", "busted", true);
            } else if (gameManager.isGoodCollision()) {
                renderCollision("success", "good job perry", false);
            }
            renderScore();
            renderCharactersTable();
            gameManager.setBallCollision(false);
            gameManager.setGoodCollision(false);
        }

        private void makeSound(String type) {

        }

        @Override
        protected void onPause() {
            super.onPause();
            if(pause){
                onResume();
            }
            else {
                stopTimer();
                pause = true;
                if (gameManager.isSensorMode()) sensorDetector.stop();
            }
        }


        protected void onStop() {
            super.onStop();
            MyGPS.getInstance().stop();
        }

        @Override
        protected void onResume() {
            super.onResume();
            pause = false;
            startTimer();
            if (gameManager.isSensorMode()) {
                sensorDetector.start();
            }
            MyGPS.getInstance().start();
        }

        protected void onRestart() {
            super.onRestart();
            isActiveController = false;
            stopTimer();
            init();
            initView();
            updateUI();
        }

        private void startTimer() {
            timerUpdateUI = new Timer();
            TimerTask callback_updateBalls = new TimerTask() {
                @Override
                public void run() {
                    boolean insertPokemon;
                    if (roundsToDoof == 0) {
                        insertPokemon = true;
                        roundsToDoof = 3;
                    } else {
                        insertPokemon = false;
                        roundsToDoof--;
                    }
                    runOnUiThread(() -> updateUIonTime(insertPokemon));
                }
            };
            timerUpdateUI.scheduleAtFixedRate(callback_updateBalls, gameManager.getDELAY(), gameManager.getDELAY());
        }


        private void initButtons() {
            game_BTN_left.setOnClickListener(v -> Move("left"));
            game_BTN_right.setOnClickListener(v -> Move("right"));
          //  game_BTN_restart.setOnClickListener(v -> onRestart());
            game_BTN_pause.setOnClickListener(v -> onPause());
        }


        public void updateUIonTime(boolean insertPokemon) {
            gameManager.updateTable(insertPokemon);
            if (gameManager.isGameOver()) {
                updateUIGameOver();
            } else {
                updateUI();
            }
        }

        private void updateUIGameOver() {
            renderGameOver();
            isActiveController = false;
            stopTimer();
        }


        public void renderPerry() {
            ArrayList<Item> perryItems = gameManager.getPerryItems();
            for (int i = 0; i < COLS; i++) {
                AppCompatImageView game_IMG_perry = game_IMG_Perrys.get(i);
                TypeVisibility typeVisibility = perryItems.get(i).getTypeVisibility();
                game_IMG_perry.setVisibility(typeVisibility == TypeVisibility.VISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        }

        public void renderHearts() {
            ArrayList<Item> heartItems = gameManager.getHeartItems();
            for (int i = 0; i < HEARTS; i++) {
                AppCompatImageView game_IMG_heart = game_IMG_hearts.get(i);
                TypeVisibility typeVisibility = heartItems.get(i).getTypeVisibility();
                game_IMG_heart.setVisibility(typeVisibility == TypeVisibility.VISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        }


        private void renderCollision(String musicType, String toastMassage, boolean doVibrate) {
            renderHearts();
            renderToast(toastMassage);
            if (doVibrate) vibrate();
            makeSound(musicType);
        }

        private void renderScore() {
            game_LBL_scoreNum.setText(Integer.toString((gameManager.getScore())));
        }

        private void initControls() {
            if (gameManager.isButtonMode()) initButtons();
            else initSensor();
        }

        private void initSensor() {
            sensorDetector = new SensorDetector(this, callback_movement);
        }

        private final SensorDetector.CallBack_Movement callback_movement = new SensorDetector.CallBack_Movement() {
            @Override
            public void moveRight() {
                Move("right");
            }

            @Override
            public void moveLeft() {
                Move("left");
            }
        };

        private void initGameManager() {
            gameManager = gameManager.getInstance()
                    .setNumOfHearts(HEARTS)
                    .setNumOfRows(ROWS)
                    .setNumOfColumns(COLS);
            gameManager.initItems(game_IMG_hearts, "hearts");
            gameManager.initItems(game_IMG_Perrys, "perry");
            int imageDoof = R.drawable.doofenshmirz;
            gameManager.initCharactersMat(game_IMG_Characters, imageDoof);
        }

        private void init() {
            initGameManager();
            initControls();
        }


        private void renderGameOver() {
            renderHearts();
            renderScore();
            renderCharactersTable();
            gameOver();
        }

        private void renderCharactersTable() {
            ArrayList<ArrayList<Item>> matCharactersItems = gameManager.getMatrixItems();
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    Item characters = matCharactersItems.get(row).get(col);
                    TypeVisibility typeVisibility = characters.getTypeVisibility();
                    AppCompatImageView imgCharacter = game_IMG_Characters.get(row).get(col);
                    if (typeVisibility == TypeVisibility.VISIBLE) {
                        int imageResource;

                        if (characters.getTypeItem() == TypeItem.BAD_CHARACTER) {
                            if(num==1) {
                                imageResource = R.drawable.ferb;
                                num =2;
                            }
                            else if(num==2) {
                                imageResource = R.drawable.phineas;
                                num=3;
                            }
                            else {
                                imageResource = R.drawable.candace;
                                num =1;
                            }
                        }
                        else{ imageResource = R.drawable.doofenshmirz;}
                        imgCharacter.setImageResource(imageResource);
                        imgCharacter.setVisibility(View.VISIBLE);
                    } else {
                        imgCharacter.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

        private void initView() {
            renderHearts();
            renderCharactersTable();
            renderPerry();
        }

        private void stopTimer() {
            timerUpdateUI.cancel();
        }

        private void findButtons() {
            game_BTN_left = findViewById(R.id.game_BTN_left);
            game_BTN_right = findViewById(R.id.game_BTN_right);
            game_BTN_pause = findViewById(R.id.game_BTN_pause);
           // game_BTN_restart = findViewById(R.id.game_BTN_restsrt);
            game_BTN_left.setVisibility(View.VISIBLE);
            game_BTN_right.setVisibility(View.VISIBLE);
            game_BTN_pause.setVisibility(View.VISIBLE);
         //   game_BTN_restart.setVisibility(View.VISIBLE);
        }




        private void findPerrys() {


        }







        // todo on create
        private void findViews() {
            game_IMG_hearts = new ArrayList<>(HEARTS);
            for (int i = 1; i <= HEARTS; i++) {
                int heartID = getResources().getIdentifier("game_IMG_heart" + i, "id", getPackageName());
                AppCompatImageView currentHeart = findViewById(heartID);
                game_IMG_hearts.add(currentHeart);
            }
            game_LBL_score = findViewById(R.id.game_LBL_score);
            game_LBL_scoreNum = findViewById(R.id.game_LBL_score_num);
            if (gameManager.getInstance().isButtonMode())
            {
                findButtons();}
            game_IMG_Perrys = new ArrayList<>(COLS);
            game_IMG_Perrys.add(findViewById(R.id.game_IMG_perry1));
            game_IMG_Perrys.add(findViewById(R.id.game_IMG_perry2));
            game_IMG_Perrys.add(findViewById(R.id.game_IMG_perry3));
            game_IMG_Perrys.add(findViewById(R.id.game_IMG_perry4));
            game_IMG_Perrys.add(findViewById(R.id.game_IMG_perry5));
            game_IMG_Characters = new ArrayList<>(ROWS);
            for (int i = 0; i < ROWS; i++) {
                game_IMG_Characters.add(new ArrayList<>(COLS));
                for (int j = 0; j < COLS; j++) {
                    int currentPlace = i * COLS + j + 1;
                    int ballID = getResources().getIdentifier("game_IMG_Charcter" + currentPlace, "id", getPackageName());
                    AppCompatImageView currentBall = findViewById(ballID);
                    game_IMG_Characters.get(i).add(currentBall);
                }
            }
        }


        private void gameOver() {
            //stopTimer();
            pause = true;
            Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
            startActivity(intent);
            gameManager.gameOver();
            finish();


        }

        private void vibrate() {
            MySignal.getInstance().vibrate();
        }


        public void renderToast(String msg) {
            MySignal.getInstance().toast(msg);
        }



}