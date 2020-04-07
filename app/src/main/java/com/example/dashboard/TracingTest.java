package com.example.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class TracingTest extends AppCompatActivity {
    Button Start, Main;
    TextView Info;
    long startTime, endTime, currTime, bestTime = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing_test);

        Start = (Button)findViewById(R.id.b_start);
        Main = (Button)findViewById(R.id.b_main);
        Info = (TextView)findViewById(R.id.tv_info1);

        Start.setEnabled(true);
        Main.setEnabled(false);

        Info.setText("BEST TIME: " + bestTime + "ms");

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Start.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startTime = System.currentTimeMillis();
                        Main.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(),R.color.blue));
                        Main.setText("PRESS");
                        Main.setEnabled(true);
                    }
                },3000);
            }
        });

        Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime = System.currentTimeMillis();
                currTime = endTime - startTime;
                Main.setBackgroundColor(
                        ContextCompat.getColor(getApplicationContext(),R.color.red));
                Main.setText(currTime + "ms");
                Start.setEnabled(true);
                Main.setEnabled(false);

                if(currTime < bestTime){
                    bestTime = currTime;
                    Info.setText("BEST TIME" + bestTime + "ms");
                }
            }
        });
    }
}
