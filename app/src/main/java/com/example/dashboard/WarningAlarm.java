package com.example.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.dashboard.Setting.number_to_warning_alarm;



public class WarningAlarm extends Activity implements OnClickListener {

    private TextView tv_timer;
    private ImageView img_start;
    private boolean isStopCount = false;
    private boolean isPause = true;
    private Handler mHandler = new Handler();
    private int counter = 0;
    private String currenttime = "";
    private SmsManager smsManager;
    private String number_text="";
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_alarm);
        findViews();
        countTimer();
        smsManager = SmsManager.getDefault();
    }

    private void findViews() {
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        img_start = (ImageView) findViewById(R.id.img_start);
        img_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_start:
                if (!isPause) {
                    isPause = true;
                    isStopCount = false;
                    img_start.setImageResource(R.drawable.pause);
                } else {
                    isPause = false;
                    isStopCount = true;
                    img_start.setImageResource(R.drawable.start);
                }
                break;
            default:
                break;
        }
    }

    private Runnable TimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isStopCount) {
                counter += 1;
                if (counter == 1800) //30s  1s = 60
                {
                    Toast.makeText(getApplicationContext(), "send message", Toast.LENGTH_LONG).show(); //long 2s
                    Intent intent = getIntent();
                    number_text = intent.getStringExtra(number_to_warning_alarm);
                    if(TextUtils.isEmpty(number_text)){//判断是否为空
                        number = "+44 7421271993";
                    }else{
                        number = number_text;
                    }
                    smsManager.sendTextMessage(number, null, "Fall Detection", null, null);
                }
                currenttime = TimeUtil.getFormatTime(counter);
                tv_timer.setText(currenttime);
            }
            countTimer();
        }
    };

    private void countTimer() {
        mHandler.postDelayed(TimerRunnable, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(TimerRunnable);
    }

}