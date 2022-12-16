package com.example.project2;

import androidx.annotation.Nullable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

//todo implement word difficulty levels and sort alphabetically for maintenance
//todo improve data structure from string[] to hashmap maybe? to store data, messy as is
//todo store images for monsters
//todo add more rewards with % drop rate for higher levels
//todo add images for rewards
//todo add more monsters, maybe a boss monster every 5 fights
//todo sort all fields that include levels for maintenance + updating purposes
//todo add super rare rewards, named after lore

public class DatabasePopulator extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "game_files";

    private static final String TABLE_Rewards = "rewards_table";
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_NAME = "item_name";
    private static final String KEY_LEVEL = "item_level";
    private static final String KEY_STAT = "item_stat";
    private static final String KEY_WEIGHT = "item_weight";

    private static final String TABLE_Words = "word_table";
    private static final String KEY_WORDS_ID = "word_id";
    private static final String KEY_WORD = "word_name";

    private static final String TABLE_Monsters = "monster_table";
    private static final String KEY_MONSTER_ID = "monster_id";
    private static final String KEY_MONSTER_NAME = "monster_name";
    private static final String KEY_MONSTER_DAMAGE = "monster_damage";
    private static final String KEY_MONSTER_MAX_HP = "monster_max_hp";
    private static final String KEY_MONSTER_DAMAGE_INTERVAL = "monster_damage_interval";
    private static final String KEY_MONSTER_LEVEL = "monster_level";

    public DatabasePopulator(@Nullable Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createItemTable(sqLiteDatabase);
        createWordTable(sqLiteDatabase);
        createMonsterTable(sqLiteDatabase);
    }

    void createItemTable(SQLiteDatabase sqLiteDatabase){
        String CREATE_ITEM_TABLE = "CREATE TABLE "
                + TABLE_Rewards + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_LEVEL + " TEXT,"
                + KEY_STAT + " TEXT,"
                + KEY_WEIGHT + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }

    void createWordTable(SQLiteDatabase sqLiteDatabase){
        String CREATE_WORD_TABLE = "CREATE TABLE "
                + TABLE_Words + "("
                + KEY_WORDS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_WORD + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_WORD_TABLE);
    }

    void createMonsterTable(SQLiteDatabase sqLiteDatabase){
        String CREATE_ITEM_TABLE = "CREATE TABLE "
                + TABLE_Monsters + "("
                + KEY_MONSTER_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MONSTER_NAME + " TEXT,"
                + KEY_MONSTER_DAMAGE + " TEXT,"
                + KEY_MONSTER_MAX_HP + " TEXT,"
                + KEY_MONSTER_DAMAGE_INTERVAL + " TEXT,"
                + KEY_MONSTER_LEVEL + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Rewards);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Words);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Monsters);
        // Create tables again
        onCreate(db);
    }

    // Adding item Details
    void insertItemDetails(){
        deleteRewards();
        String[] item_names = {"flimsy sword", "priest's blessing", "flimsy iron mail", "sturdy sword", "goddesses anointment"};
        String[] item_levels = {"Level: 1", "Level: 1", "Level: 1", "level: 2", "level: 3"};
        String[] item_stats = {"damage", "max_hp", "damage_resist", "damage", "max_hp"};
        String[] item_weight = {"+1", "+5", "+1", "+2", "+9"};
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        for(int i = 0; i < item_names.length; i++){
            cValues.put(KEY_NAME, item_names[i]);
            cValues.put(KEY_LEVEL, item_levels[i]);
            cValues.put(KEY_STAT, item_stats[i]);
            cValues.put(KEY_WEIGHT, item_weight[i]);
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TABLE_Rewards,null, cValues);
        }
        db.close();
    }

    // Get Item Details
    public ArrayList<HashMap<String, String>> GetItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
        String query = "SELECT item_name, item_level, item_stat, item_weight FROM "
                + TABLE_Rewards + " ORDER BY RANDOM() LIMIT 3";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> item = new HashMap<>();
            item.put("name", cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
            item.put("level",cursor.getString(cursor.getColumnIndexOrThrow(KEY_LEVEL)));
            item.put("stat",cursor.getString(cursor.getColumnIndexOrThrow(KEY_STAT)));
            item.put("weight",cursor.getString(cursor.getColumnIndexOrThrow(KEY_WEIGHT)));
            itemList.add(item);
        }
        return itemList;
    }

    // Add words
    void insertWordDetails(){
        deleteWords();
        //try for 7 letters maximum, must be all caps
        String[] words = {"DOG", "CAT", "DEER", "MOUSE", "EAGLE", "MOOSE", "RAT", "SNAKE", "CANARY",
        "ROBIN", "COW", "FERRET", "CROW", "ZEBRA", "GOAT", "FELINE", "PIG", "HORSE"};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        for(int i = 0; i < words.length; i++){
            cValues.put(KEY_WORD, words[i]);
            long newRowId = db.insert(TABLE_Words,null, cValues);
        }
        db.close();
    }

    public String getWord() {
        SQLiteDatabase db = this.getWritableDatabase();
        String w = "";
        String query = "SELECT word_name FROM " + TABLE_Words + " ORDER BY RANDOM() LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            w = cursor.getString(cursor.getColumnIndexOrThrow(KEY_WORD));
        }
        return w;
    }

    // add monsters
    void insertMonsterDetails(){
        deleteMonsters();
        String[] monsters_name = {"goblin", "imp", "orc", "wight", "dragon"};
        String[] monsters_damage = {"1", "2", "3", "4", "6"};
        String[] monsters_max_hp = {"5", "10", "15", "25", "50"};
        String[] monsters_damage_interval = {"5000", "4000", "3000", "2000", "1000"};
        String[] monsters_level = {"1", "2", "3", "4", "5"};
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        for(int i = 0; i < monsters_name.length; i++){
            cValues.put(KEY_MONSTER_NAME, monsters_name[i]);
            cValues.put(KEY_MONSTER_DAMAGE, monsters_damage[i]);
            cValues.put(KEY_MONSTER_MAX_HP, monsters_max_hp[i]);
            cValues.put(KEY_MONSTER_DAMAGE_INTERVAL, monsters_damage_interval[i]);
            cValues.put(KEY_MONSTER_LEVEL, monsters_level[i]);
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TABLE_Monsters,null, cValues);
        }
        db.close();
    }

    //return a hashmap of a monster
    public HashMap<String, String> spawnMonster(int level){
        SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, String> monsterSpawn = new HashMap<>();
        String query = "SELECT monster_name, monster_damage, monster_max_hp, monster_damage_interval FROM "
                + TABLE_Monsters + " WHERE " + KEY_MONSTER_LEVEL + " = " + "\"" + level + "\" ORDER BY RANDOM() LIMIT 1";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            monsterSpawn.put("name", cursor.getString(cursor.getColumnIndexOrThrow(KEY_MONSTER_NAME)));
            monsterSpawn.put("damage",cursor.getString(cursor.getColumnIndexOrThrow(KEY_MONSTER_DAMAGE)));
            monsterSpawn.put("max_hp",cursor.getString(cursor.getColumnIndexOrThrow(KEY_MONSTER_MAX_HP)));
            monsterSpawn.put("damage_interval",cursor.getString(cursor.getColumnIndexOrThrow(KEY_MONSTER_DAMAGE_INTERVAL)));
        }
        return monsterSpawn;

    }

    public void deleteRewards() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_Rewards);
        db.close();
    }

    public void deleteWords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_Words);
        db.close();
    }

    public void deleteMonsters(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_Monsters);
        db.close();
    }



}