package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project2.objects.Player;

//todo track monsters defeated
//todo make monster class
//todo make rewards class
//todo construct database and pre populate with Words, Monster and Rewards
//todo get random word from word database
//todo get appropriate monster from monster database
//todo get 3 rewards from rewards database each monster slain
//todo implement leaderboard database and display on main activity
//todo get monster damage working, end game with player hp at 0
//todo construct rewards page
//todo construct end game page2

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = findViewById(R.id.button_start);
        splashImage = findViewById(R.id.splashImage);
        splashImage.setImageResource(R.drawable.splashart);

        //Bundle extras = getIntent().getExtras();
        btnStart.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Let's go!", Toast.LENGTH_LONG).show();
            Intent send = new Intent(this, BattleActivity.class);

            Bundle bundle = new Bundle();
            //player class variables to send
            bundle.putString("name", "Hero");
            bundle.putInt("damage", 2);
            bundle.putInt("max_hp", 10);
            bundle.putInt("current_hp", 10);
            bundle.putInt("damage_resist", 0);

            send.putExtras(bundle);
            startActivity(send);
        });
    }

}