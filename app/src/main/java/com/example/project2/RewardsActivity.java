package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.project2.objects.Player;

import java.util.ArrayList;
import java.util.HashMap;

//todo display how many foes the player had defeated
//todo display the players stats so they make an informed decision on a reward to pick
//todo game hangs/crashes after defeating 3 dragons

public class RewardsActivity extends AppCompatActivity {

    /*
        Here the player chooses a reward listed from the database
        The reward is added to their stats
        Then sent back to the battle page
     */

    Player player;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        listView = findViewById(R.id.listView);

        Bundle receive = getIntent().getExtras();
        String playerName = (String) receive.get("playerName");
        int playerDamage = (int) receive.get("playerDamage");
        int playerMax_hp = (int) receive.get("playerMax_hp");
        int playerCurrent_hp = (int) receive.get("playerCurrent_hp");
        int playerDamage_resist = (int) receive.get("playerDamage_resist");
        int playerFoes_defeated = (int) receive.get("playerFoes_defeated");

        player = new Player(playerName, playerDamage, playerMax_hp,
                playerCurrent_hp, playerDamage_resist, playerFoes_defeated);

        DatabasePopulator dbP = new DatabasePopulator(this);
        ArrayList<HashMap<String,String>> itemList;
        itemList = dbP.GetItems();
        ListAdapter adapter = new SimpleAdapter(RewardsActivity.this,
                itemList,
                R.layout.reward_list_row,
                new String[]{"name", "level", "weight", "stat"},
                new int[]{R.id.i_name, R.id.i_level, R.id.i_weight, R.id.i_stat});
        listView.setAdapter(adapter);

        //stat, level, name, weight
        listView.setOnItemClickListener((adapterView, view, pos, id) -> {
            HashMap<String, String> des = (itemList.get(pos));
            if (des.get("stat").equals("damage")){
                String[] tempArr = des.get("weight").split("");
                int increase = Integer.parseInt(tempArr[1]);
                player.increaseDamage(increase);
            }else if (des.get("stat").equals("max_hp")){
                String[] tempArr = des.get("weight").split("");
                int increase = Integer.parseInt(tempArr[1]);
                player.increaseMax_hp(increase);
            }else if (des.get("stat").equals("damage_resist")){
                String[] tempArr = des.get("weight").split("");
                int increase = Integer.parseInt(tempArr[1]);
                player.increaseDamageResist(increase);
            }

            player.heal(player.getFoes_defeated());
            Intent send = new Intent(RewardsActivity.this, BattleActivity.class);
            Bundle bundle = new Bundle();
            //player class variables to send
            bundle.putString("playerName", player.getName());
            bundle.putInt("playerDamage", player.getDamage());
            bundle.putInt("playerMax_hp", player.getMax_hp());
            bundle.putInt("playerCurrent_hp", player.getCurrent_hp());
            bundle.putInt("playerDamage_resist", player.getDamage_resist());
            bundle.putInt("playerFoes_defeated", player.getFoes_defeated());

            //fetching an appropriate monster from the database
            HashMap<String, String> mon = dbP.spawnMonster((int) Math.ceil((int)player.getFoes_defeated() / 3) + 1);
            //monster class variables to send
            bundle.putString("monsterName", mon.get("name"));
            bundle.putInt("monsterDamage", Integer.parseInt(mon.get("damage")));
            bundle.putInt("monsterMax_hp", Integer.parseInt(mon.get("max_hp")));
            bundle.putInt("monsterDamage_interval", Integer.parseInt(mon.get("damage_interval")));

            send.putExtras(bundle);
            startActivity(send);
        });

    }
}