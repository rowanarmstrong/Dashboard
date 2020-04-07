package com.example.dashboard;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TapTest extends AppCompatActivity {

    ImageView Tap;
    TextView Result, Info;
    int currTaps = 0;
    boolean gameStarted = false;
    CountDownTimer timer;
    int bestResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_test);
        Tap = findViewById(R.id.iv_tap);
        Result = findViewById(R.id.tv_result);
        Result.setTextSize(30);
        Info = findViewById(R.id.tv_info);
        Info.setTextSize(20);
        Result.setText("Best Result: " + bestResult);


        Tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameStarted){
                    //Count the tap if game is started
                    currTaps++;
                } else {
                    //Start the game if it not started
                    Info.setText("Tap!");
                    gameStarted = true;
                    timer.start();
                }
            }
        });

        //Timer for 10 seconds with interval 1 second
        timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Display the remaining time
                long timeTillEnd = (millisUntilFinished / 1000) + 1;
                Result.setText("Time Remaining: " + timeTillEnd);
            }

            @Override
            public void onFinish() {
                //The game is over
                Tap.setEnabled(false);
                gameStarted = false;
                Info.setText("Game" +  "\n Over!");
                //Check the high scope and save the new result if better
                if(currTaps > bestResult) {
                    bestResult = currTaps;
                }
                    //Display the best result and current one
                    Result.setText("Best Result: " + bestResult + "\nCurrent Taps:" + currTaps);

                //Prepare the new game after 3 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tap.setEnabled(true);
                        Info.setText("   Start" + "\nTapping");
                        currTaps = 0;
                    }
                },3000);
            }
        };
    }
}
