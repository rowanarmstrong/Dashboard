package com.example.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FallMain extends Fragment implements View.OnClickListener {
    private Button mBtnStart;
    private Button mBtnStop;
    private Button mBtnSetting;
    private static Context mContext = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fall_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mBtnStart = (Button) getView().findViewById(R.id.button1);
        mBtnStop = (Button) getView().findViewById(R.id.button2);
        mBtnSetting = (Button) getView().findViewById(R.id.button3);
        mBtnStart.setOnClickListener(this);
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
        if (v == mBtnStart) {
            this.getActivity().startService(new Intent(getActivity(),System_Service.class));
        }
        if (v == mBtnStop) {
            this.getActivity().stopService(new Intent(getActivity(),System_Service.class));
        }
        if (v == mBtnSetting) {
            Intent intent = new Intent(getActivity(),Setting.class);
            startActivity(intent);
        }
    }
}
