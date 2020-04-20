package com.example.dashboard;
/* This file allows the user to type the phone number and their weight*/
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {

    public static final String number_to_warning_alarm = "+44 7421271993";
    public static final String weight_to_system_service = "60";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void Complete(View paramView) {
        Intent intent = new Intent((Context) this, MainActivity.class);
        String str1 = ((EditText) findViewById(R.id.weight)).getText().toString();  //get the number that user types through the layout and set it as string
        String str2 = ((EditText) findViewById(R.id.number)).getText().toString();
        intent.putExtra(weight_to_system_service, str1);    //Transfer number
        intent.putExtra(number_to_warning_alarm, str2);
        startActivity(intent);
    }
}