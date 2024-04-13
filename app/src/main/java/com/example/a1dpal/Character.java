package com.example.a1dpal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;


public class Character extends AppCompatActivity {
    MaterialButton homeBtn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Character.this,Login.class));
            }
        });

    }
    public void onGanyuClick(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("CharChosen", "Ganyu");
        startActivity(intent);
        finish();
    }
    public void onZhongliClick(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("CharChosen", "Zhongli");
        startActivity(intent);
        finish();
    }
}