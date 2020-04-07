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

public class ReactionMain extends Fragment implements View.OnClickListener{

    private Button mBtnSpeed;
    private Button mBtnReaction;

    private static Context mContext = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reaction_layout, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        mBtnSpeed = (Button) getView().findViewById(R.id.Speed);
        mBtnReaction = (Button) getView().findViewById(R.id.Reaction);

        mBtnSpeed.setOnClickListener(this);
        mBtnReaction.setOnClickListener(this);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View v) {

        if (v == mBtnSpeed) {
            Intent intent = new Intent(getActivity(), TapTest.class);
            startActivity(intent);
        }
        if (v == mBtnReaction) {
            Intent intent = new Intent(getActivity(), TracingTest.class);
            startActivity(intent);
        }


    }


}
