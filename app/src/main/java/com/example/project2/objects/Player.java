package com.example.project2.objects;

import android.util.Log;

public class Player {

    String name;
    int damage, max_hp, current_hp, damage_resist, foes_defeated;

    public Player(String name, int damage, int max_hp, int current_hp, int damage_resist) {
        this.name = name;
        this.damage = damage;
        this.max_hp = max_hp;
        this.current_hp = current_hp;
        this.damage_resist = damage_resist;
        this.foes_defeated = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMax_hp() {
        return max_hp;
    }

    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }

    public int getCurrent_hp() {return current_hp;}

    public void setCurrent_hp(int current_hp) {
        this.current_hp = current_hp;
    }

    public int getDamage_resist() {
        return damage_resist;
    }

    public void foe_defeated(){this.foes_defeated ++;}

    public void setDamage_resist(int damage_resist) {
        this.damage_resist = damage_resist;
    }

    public void heal(int amount){
        this.current_hp = current_hp + amount;
        if(this.current_hp > this.max_hp){
            this.current_hp = this.max_hp;
        }
    }

    public void take_damage(int dmg_taken){
        int injury = dmg_taken - this.damage_resist;
        if(injury > 0){
            this.current_hp = (this.current_hp - injury);
        }
    }

}//player
