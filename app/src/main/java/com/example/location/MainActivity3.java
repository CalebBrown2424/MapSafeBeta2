package com.example.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {
    EditText passcode;
    int passcodenum;
    private static final String SAVED_PASSCODE = "saved_passcode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        firstActivityData();
    }
    private void firstActivityData()
    {
        Button finish = (Button) findViewById(R.id.finishbutton);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passcode = (EditText)findViewById(R.id.editTextNumber) ;
                String value= passcode.getText().toString();
                passcodenum =Integer.parseInt(value);
                //editor.putBoolean(FIRST_OPEN, false);
                SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(SAVED_PASSCODE, passcodenum);
                editor.apply();
                finish();
            }
        });
    }
}