package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.objects.Monster;
import com.example.project2.objects.Player;
import com.example.project2.objects.Word;

import java.util.ArrayList;
import java.util.HashMap;

public class BattleActivity extends AppCompatActivity {
    /*
        A round is every new word
        A fight is an encounter per monster
     */

    TextView playerHP, monsterHP, foesDefeated;
    ImageView playerImage, monsterImage;
    Player player;
    Monster monster;

    int letter_count = 0;
    Word word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        foesDefeated = findViewById(R.id.amount_defeated);
        playerHP = findViewById(R.id.playerHP);
        monsterHP = findViewById(R.id.monsterHP);
        playerImage = findViewById(R.id.playerImage);
        monsterImage = findViewById(R.id.monsterImage);
        monsterImage.setImageResource(R.drawable.mindflayer);
        playerImage.setImageResource(R.drawable.hero);

        //getting bundle to create/update player class
        Bundle bundle = getIntent().getExtras();
        String playerName = (String) bundle.get("playerName");
        int playerDamage = (int) bundle.get("playerDamage");
        int playerMax_hp = (int) bundle.get("playerMax_hp");
        int playerCurrent_hp = (int) bundle.get("playerCurrent_hp");
        int playerDamage_resist = (int) bundle.get("playerDamage_resist");
        int playerFoes_defeated = (int) bundle.get("playerFoes_defeated");
        player = new Player(playerName, playerDamage, playerMax_hp,
                playerCurrent_hp, playerDamage_resist, playerFoes_defeated);
        playerHP.setText("HP: " + player.getCurrent_hp() + " / " + player.getMax_hp());
        foesDefeated.setText("Defeated: " + player.getFoes_defeated());

        String monsterName = (String) bundle.get("monsterName");
        int monsterDamage = (int) bundle.get("monsterDamage");
        int monsterMax_hp = (int) bundle.get("monsterMax_hp");
        int monsterDamage_interval = (int) bundle.get("monsterDamage_interval");
        monster = new Monster(monsterName, monsterDamage, monsterMax_hp, monsterDamage_interval);
        monsterHP.setText("HP: " + monster.getCurrent_hp() + " / " + monster.getMax_hp());

        //background thread for monster damage interval
        MonsterDamageThread runnable = new MonsterDamageThread();
        new Thread(runnable).start();

        //initialises fight
        //round_letters = shuffle_letters(round_letters);
        DatabasePopulator db_handler = new DatabasePopulator(this);
        this.word = new Word(db_handler.getWord());
        EditText editText = findViewById(R.id.guess_answer);
        LinearLayout linearLayout = findViewById(R.id.letter_layout);
        for (String l : word.getLetters()) {
            addGuess( linearLayout, editText, l );
        }
    }//instance

    public void game_is_over(){
        Intent send = new Intent(BattleActivity.this, GameOverActivity.class);
        Bundle bundle = new Bundle();
        //player class variables to send
        bundle.putString("playerName", player.getName());
        bundle.putInt("playerDamage", player.getDamage());
        bundle.putInt("playerMax_hp", player.getMax_hp());
        bundle.putInt("playerCurrent_hp", player.getCurrent_hp());
        bundle.putInt("playerDamage_resist", player.getDamage_resist());
        bundle.putInt("playerFoes_defeated", player.getFoes_defeated());
        send.putExtras(bundle);
        startActivity(send);
    }

    class MonsterDamageThread extends Thread {
        boolean running = true;
        MonsterDamageThread() {}
        @Override
        public void run() {
            while(player.getCurrent_hp() > 0 && monster.getCurrent_hp() > 0) {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(() ->
                        playerHP.setText("HP: " + player.getCurrent_hp() + " / " + player.getMax_hp()));
                try {
                    Thread.sleep(monster.getDamage_interval());
                    damagePlayer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }//while
            if (player.getCurrent_hp() < 1){
                game_is_over();
            }
        }//run
    }//thread

    //populating guess game fields
    public void addGuess(LinearLayout ll, EditText et, String txt){
        //System.out.println("in add guess");

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
            if(this.monster.getCurrent_hp() > 0){
                Toast.makeText(BattleActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                editText.setText("");
                //generating a new word from the database into the word class
                DatabasePopulator db_handler = new DatabasePopulator(this);
                this.word = new Word(db_handler.getWord());
            }else{
                Toast.makeText(BattleActivity.this, "VICTORY", Toast.LENGTH_LONG).show();
                player.foe_defeated();
                Intent send = new Intent(this, RewardsActivity.class);
                Bundle bundle = new Bundle();
                //player class variables to send
                bundle.putString("playerName", this.player.getName());
                bundle.putInt("playerDamage", this.player.getDamage());
                bundle.putInt("playerMax_hp", this.player.getMax_hp());
                bundle.putInt("playerCurrent_hp", this.player.getCurrent_hp());
                bundle.putInt("playerDamage_resist", this.player.getDamage_resist());
                bundle.putInt("playerFoes_defeated", this.player.getFoes_defeated());
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
        this.monster.take_damage(this.player.getDamage());
        monsterHP.setText("HP: " + monster.getCurrent_hp() + " / " + monster.getMax_hp());
    }//damageMonster

    public void damagePlayer(){
        this.player.take_damage(1);
    }//damagePlayer



}//class