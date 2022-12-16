package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project2.objects.Player;

//todo make monster class
//todo construct database and pre populate with Words, Monster and Rewards
//todo get appropriate monster from monster database
//todo get 3 rewards from rewards database each monster slain
//todo implement leaderboard database and display on main activity
//todo construct end game page
//todo fix tables in database not clearing, many duplicating items

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


        DatabasePopulator dbP = new DatabasePopulator(MainActivity.this);
        dbP.insertItemDetails();
        dbP.insertWordDetails();


        //Bundle extras = getIntent().getExtras();
        btnStart.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Let's go!", Toast.LENGTH_LONG).show();
            Intent send = new Intent(this, BattleActivity.class);

            Bundle bundle = new Bundle();
            //player class variables to send
            bundle.putString("playerName", "Hero");
            bundle.putInt("playerDamage", 2);
            bundle.putInt("playerMax_hp", 5);
            bundle.putInt("playerCurrent_hp", 5);
            bundle.putInt("playerDamage_resist", 0);
            bundle.putInt("playerFoes_defeated", 0);

            //monster class variables to send
            bundle.putString("monsterName", "Goblin");
            bundle.putInt("monsterDamage", 1);
            bundle.putInt("monsterMax_hp", 4);
            bundle.putInt("monsterDamage_interval", 4000);

            send.putExtras(bundle);
            startActivity(send);
        });
    }

}