package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.objects.Player;

import java.util.HashMap;

public class GameOverActivity extends AppCompatActivity {

    TextView playerScoreText, playerScoreDisplay;
    Button retryButton;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Toast.makeText(this, "DEFEAT", Toast.LENGTH_LONG).show();

        playerScoreText = findViewById(R.id.player_score_text);
        playerScoreDisplay = findViewById(R.id.player_score_result);
        retryButton = findViewById(R.id.home_button);

        Bundle receive = getIntent().getExtras();
        String playerName = (String) receive.get("playerName");
        int playerDamage = (int) receive.get("playerDamage");
        int playerMax_hp = (int) receive.get("playerMax_hp");
        int playerCurrent_hp = (int) receive.get("playerCurrent_hp");
        int playerDamage_resist = (int) receive.get("playerDamage_resist");
        int playerFoes_defeated = (int) receive.get("playerFoes_defeated");

        player = new Player(playerName, playerDamage, playerMax_hp,
                playerCurrent_hp, playerDamage_resist, playerFoes_defeated);
        playerScoreDisplay.setText(String.valueOf(player.getFoes_defeated()));

        retryButton.setOnClickListener(v -> {
            Toast.makeText(GameOverActivity.this, "Yeah!", Toast.LENGTH_SHORT).show();
            Intent send = new Intent(this, MainActivity.class);
            startActivity(send);
        });
    }
}