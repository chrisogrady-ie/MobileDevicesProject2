package com.example.project2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;



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

    public DatabasePopulator(@Nullable Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createItemTable(sqLiteDatabase);
        createWordTable(sqLiteDatabase);
    }

    void createItemTable(SQLiteDatabase sqLiteDatabase){
        String CREATE_ITEM_TABLE = "CREATE TABLE "
                + TABLE_Rewards + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_LEVEL + " TEXT,"
                + KEY_STAT + " TEXT,"
                + KEY_WEIGHT + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }

    void createWordTable(SQLiteDatabase sqLiteDatabase){
        String CREATE_WORD_TABLE = "CREATE TABLE "
                + TABLE_Words + "("
                + KEY_WORDS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_WORD + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_WORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Rewards);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Words);
        // Create tables again
        onCreate(db);
    }

    // Adding item Details
    void insertItemDetails(){
        deleteRewards();
        String[] item_names = {"flimsy sword", "priest's blessing", "flimsy iron mail"};
        String[] item_levels = {"Level: 1", "Level: 1", "Level: 1"};
        String[] item_stats = {"damage", "max_hp", "damage_resist"};
        String[] item_weight = {"+1", "+5", "+1"};
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
        String[] words = {"DOG", "CAT", "DEER", "MOUSE", "EAGLE", "MOOSE", "RAT", "SNAKE", "CANARY",
        "SQUIRREL", "COW", "FERRET", "CROW"};
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



}