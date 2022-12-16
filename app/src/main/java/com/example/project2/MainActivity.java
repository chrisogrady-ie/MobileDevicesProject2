package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

//Christopher O'Grady
//R00067022
//christopher.ogrady@mycit.ie

//todo implement leaderboard database and display on main activity, use an online database
//todo construct end game page
//todo fix tables in database not clearing, many duplicating items
//todo allow player to select avatar
//todo allow player to enter name

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

        System.out.println();

        //making an instance of database that holds rewards, words and monsters
        DatabasePopulator dbP = new DatabasePopulator(MainActivity.this);
        dbP.insertItemDetails();
        dbP.insertWordDetails();
        dbP.insertMonsterDetails();


        //Bundle extras = getIntent().getExtras();
        btnStart.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Let's go!", Toast.LENGTH_SHORT).show();
            Intent send = new Intent(this, BattleActivity.class);

            Bundle bundle = new Bundle();
            //player class variables to send
            bundle.putString("playerName", "Hero");
            bundle.putInt("playerDamage", 2);
            bundle.putInt("playerMax_hp", 5);
            bundle.putInt("playerCurrent_hp", 5);
            bundle.putInt("playerDamage_resist", 0);
            bundle.putInt("playerFoes_defeated", 0);

            //fetching a level 1 monster from the database
            HashMap<String, String> mon = dbP.spawnMonster(1);
            //monster class variables to send
            bundle.putString("monsterName", mon.get("name"));
            bundle.putInt("monsterDamage", Integer.parseInt(mon.get("damage")));
            bundle.putInt("monsterMax_hp", Integer.parseInt(mon.get("max_hp")));
            bundle.putInt("monsterDamage_interval", Integer.parseInt(mon.get("damage_interval")));

            //starts battle activity
            send.putExtras(bundle);
            startActivity(send);
        });
    }

}