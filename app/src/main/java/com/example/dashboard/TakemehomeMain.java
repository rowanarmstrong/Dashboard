package com.example.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class TakemehomeMain extends Fragment implements View.OnClickListener{
    private Button mBtnSave;
    private Button mBtnRead;
    private Button mBtnFind;

    public final static String EXTRA_MESSAGE ="com.s1920393.backhome1";
    TextView out;
    //Declare Sharedpreferenced object
    SharedPreferences mSpf;
    EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.takemehome_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        out = (TextView) getView().findViewById(R.id.showtext);
        //Carry data from editTExt by the ID UserHome
        editText = (EditText)getView().findViewById(R.id.UserHome);
        //Get SharedPreferenced object
        mSpf = getActivity().getSharedPreferences("postcode", Context.MODE_PRIVATE);

        mBtnSave = (Button) getView().findViewById(R.id.SavePostcode);
        mBtnRead = (Button) getView().findViewById(R.id.ReadPostcode);
        mBtnFind = (Button) getView().findViewById(R.id.FindHome);
        mBtnSave.setOnClickListener(this);
        mBtnRead.setOnClickListener(this);
        mBtnFind.setOnClickListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onClick(View v) {

        if (v == mBtnSave) {
            //Get edit object
            SharedPreferences.Editor editor = mSpf.edit();
            //Write data through the editor object
            editor.putString("addr", editText.getText().toString());
            //Submit data to xml file
            editor.commit();
        }
        if (v == mBtnFind) {
            //Respond to the button
            Intent intent = new Intent(getActivity(), stratMap.class);
            //Get the string from text
            String message = editText.getText().toString();
            //Put the data to intent
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
        if (v == mBtnRead) {
            //To fetch the data, the first parameter is the key to be written,
            //and the second parameter is the value returned by default if no data is obtained.
            String info = mSpf.getString("addr", "Null");
            out.setText(info);
        }

    }
}
