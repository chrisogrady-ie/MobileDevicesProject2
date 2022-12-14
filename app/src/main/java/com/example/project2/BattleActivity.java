package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.objects.Player;
import com.example.project2.objects.Word;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

public class BattleActivity extends AppCompatActivity {
    /*
        A round is every new word
        A fight is an encounter per monster
     */

    TextView playerHP, monsterHP;
    ImageView playerImage, monsterImage;
    Player player;
    int monster_max_hp = 4;
    int monster_hp = 4;

    int letter_count = 0;
    Word word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        playerHP = findViewById(R.id.playerHP);
        monsterHP = findViewById(R.id.monsterHP);
        playerImage = findViewById(R.id.playerImage);
        monsterImage = findViewById(R.id.monsterImage);
        monsterImage.setImageResource(R.drawable.mindflayer);
        playerImage.setImageResource(R.drawable.hero);

        //getting bundle to create/update player class
        Bundle playerExtras = getIntent().getExtras();
        String name = (String) playerExtras.get("name");
        int damage = (int) playerExtras.get("damage");
        int max_hp = (int) playerExtras.get("max_hp");
        int current_hp = (int) playerExtras.get("current_hp");
        int damage_resist = (int) playerExtras.get("damage_resist");
        player = new Player(name, damage, max_hp, current_hp, damage_resist);
        playerHP.setText("HP: " + player.getCurrent_hp() + " / " + player.getMax_hp());
        monsterHP.setText("HP: " + monster_hp + " / " + monster_max_hp);

/*
        //setting up the monster to damage at intervals
        Timer monster_damage_timer = new Timer();
        TimerTask monster_damage_timer_task = new TimerTask() {
            @Override
            public void run() {
                while((monster_hp >= 1) && (player.getCurrent_hp() >= 1)){
                    //System.out.println("Player Damaged!");
                    damagePlayer();
                }
                //monster_damage_timer_task.cancel();
            }//run
        };//tt
        //delay is first execution, period in the interval after
        monster_damage_timer.scheduleAtFixedRate(monster_damage_timer_task,10,50000);
 */

        //initialises fight
        //round_letters = shuffle_letters(round_letters);
        this.word = new Word("CAT");
        EditText editText = findViewById(R.id.guess_answer);
        LinearLayout linearLayout = findViewById(R.id.letter_layout);
        for (String l : word.getLetters()) {
            addGuess( linearLayout, editText, l );
        }
    }//instance



    //populating guess game fields
    public void addGuess(LinearLayout ll, EditText et, String txt){
        System.out.println("in add guess");

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutParams.leftMargin = 90;

        TextView textView = new TextView(this);

        textView.setLayoutParams(linearLayoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setText(txt);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(28);

        textView.setOnClickListener(v1 -> {
            if(letter_count < (word).getMax_letters()) {
                //checking if check_answer was called
                if (letter_count == 0) {
                    et.setText("");
                }
                et.setText(et.getText().toString() + txt);
                letter_count++;
                if (letter_count == word.getMax_letters()) {
                    check_answer();
                }
            }//if
        });
        ll.addView(textView);
    }//add guess

    private void check_answer() {
        EditText editText = findViewById(R.id.guess_answer);

        if(editText.getText().toString().equals(word.getAnswer())){
            damageMonster();
            if(monster_hp > 0){
                Toast.makeText(BattleActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                editText.setText("");
                //generating a new word from the database into the word class
                this.word = new Word("DOGS");
            }else{
                Toast.makeText(BattleActivity.this, "VICTORY", Toast.LENGTH_LONG).show();
                Intent send = new Intent(this, RewardsActivity.class);
                Bundle bundle = new Bundle();
                //player class variables to send
                bundle.putString("name", this.player.getName());
                bundle.putInt("damage", this.player.getDamage());
                bundle.putInt("max_hp", this.player.getMax_hp());
                bundle.putInt("current_hp", this.player.getCurrent_hp());
                bundle.putInt("damage_resist", this.player.getDamage_resist());
                send.putExtras(bundle);
                startActivity(send);
            }
        }else {
            Toast.makeText(BattleActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
            editText.setText("");
        }

        letter_count = 0;
        LinearLayout linearLayout = findViewById(R.id.letter_layout);
        linearLayout.removeAllViews();
        for (String l : word.getLetters()) {
            addGuess(linearLayout, editText, l);
        }
    }//check_answer

    public void damageMonster(){
        this.monster_hp = (this.monster_hp - this.player.getDamage() );
        monsterHP.setText("HP: " + monster_hp + " / " + monster_max_hp);
    }//damageMonster

    public void damagePlayer(){
        this.player.take_damage(1);
        playerHP.setText("HP: " + player.getCurrent_hp() + " / " + player.getMax_hp());
    }//damagePlayer

}//class