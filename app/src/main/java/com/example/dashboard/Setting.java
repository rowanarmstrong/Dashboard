package com.example.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {

    public static final String number_to_warning_alarm = "111";
    public static final String weight_to_system_service = "60";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void Complete(View paramView) {
        Intent intent = new Intent((Context) this, MainActivity.class);
        String str1 = ((EditText) findViewById(R.id.weight)).getText().toString();
        String str2 = ((EditText) findViewById(R.id.number)).getText().toString();
        intent.putExtra(weight_to_system_service, str1);
        intent.putExtra(number_to_warning_alarm, str2);
        startActivity(intent);
    }
}