package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.project2.objects.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class RewardsActivity extends AppCompatActivity {

    Player player;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        listView = findViewById(R.id.listView);

        Bundle bundle = getIntent().getExtras();
        String playerName = (String) bundle.get("playerName");
        int playerDamage = (int) bundle.get("playerDamage");
        int playerMax_hp = (int) bundle.get("playerMax_hp");
        int playerCurrent_hp = (int) bundle.get("playerCurrent_hp");
        int playerDamage_resist = (int) bundle.get("playerDamage_resist");
        int playerFoes_defeated = (int) bundle.get("playerFoes_defeated");
        player = new Player(playerName, playerDamage, playerMax_hp,
                playerCurrent_hp, playerDamage_resist, playerFoes_defeated);

        DatabasePopulator db_handler = new DatabasePopulator(this);
        ArrayList<HashMap<String,String>> itemList;
        itemList = db_handler.GetItems();
        ListAdapter adapter = new SimpleAdapter(RewardsActivity.this,
                itemList,
                R.layout.reward_list_row,
                new String[]{"name", "level", "weight", "stat"},
                new int[]{R.id.i_name, R.id.i_level, R.id.i_weight, R.id.i_stat});
        listView.setAdapter(adapter);
    }
}