package com.example.dashboard;

/* This is the main_activity of fall detection. on the interface you can see three button.
* First is called start, which can initial the fall detection.
* Second is called stop to turn this function off.
* The last one is setting. Users can set phone number and weight here.*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import static com.example.dashboard.Setting.number_to_warning_alarm;

public class FallMain extends Fragment implements View.OnClickListener {
    private Button mBtnStart; //start button
    private Button mBtnStop;  //stop button
    private Button mBtnSetting;  //setting button
    private static Context mContext = null;  //use context here to start a service
    private String number_text="";
    private String weight_text="";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { //get access from dashboard
        return inflater.inflate(R.layout.fall_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBtnStart = (Button) getView().findViewById(R.id.button1);  //connect with layout
        mBtnStop = (Button) getView().findViewById(R.id.button2);
        mBtnSetting = (Button) getView().findViewById(R.id.button3);
        mBtnStart.setOnClickListener(this);  //start Click Listener to monitor if the button is clicked
        mBtnStop.setOnClickListener(this);
        mBtnSetting.setOnClickListener(this);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnStart) {   //start fall detection
            //get phone number from setting
            Intent intent = getActivity().getIntent();
            number_text = intent.getStringExtra(number_to_warning_alarm);
            //transform the phone number to system_service
            Intent intent2 = new Intent(getActivity(), System_Service.class);
            intent2.putExtra(number_to_warning_alarm, number_text);
            getActivity().startService(intent2);
        }
        if (v == mBtnStop) {    //stop fall detection
            Intent intent3 = new Intent(getActivity(), System_Service.class);
            getActivity().stopService(intent3);
            Intent intent4 = new Intent(getActivity(), WarningAlarm.class);
            getActivity().stopService(intent4);
        }
        if (v == mBtnSetting) {     //get access to setting
            Intent intent = new Intent(getActivity(),Setting.class);
            startActivity(intent);
        }
    }
}
