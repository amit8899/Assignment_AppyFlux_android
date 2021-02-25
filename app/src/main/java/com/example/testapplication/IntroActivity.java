package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button button = findViewById(R.id.btn_get);

        if(opened()){
            startActivity(new Intent(this, MainActivity.class));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    SharedPreferences.Editor preferences = getSharedPreferences("database", MODE_PRIVATE).edit();
                    preferences.putBoolean("opened", true);
                    preferences.apply();
            }
        });
    }

    private boolean opened(){
        boolean result ;
        SharedPreferences preferences = getSharedPreferences("database", MODE_PRIVATE);
        result = preferences.getBoolean("opened", false);
        return result;
    }
}