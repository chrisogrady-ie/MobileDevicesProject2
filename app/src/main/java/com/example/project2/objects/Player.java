package com.example.project2.objects;

import android.util.Log;

public class Player {

    String name;
    int damage, max_hp, current_hp, damage_resist, foes_defeated;

    public Player(String name, int damage, int max_hp, int current_hp, int damage_resist, int foes_defeated) {
        this.name = name;
        this.damage = damage;
        this.max_hp = max_hp;
        this.current_hp = current_hp;
        this.damage_resist = damage_resist;
        this.foes_defeated = foes_defeated;
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

    public void increaseDamage(int damage_increase){
        this.damage = this.damage + damage_increase;
    }

    public int getMax_hp() {
        return max_hp;
    }

    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }

    public void increaseMax_hp(int hp_increase){
        this.max_hp = this.max_hp + hp_increase;
        heal(hp_increase);
    }

    public int getCurrent_hp() {return current_hp;}

    public void setCurrent_hp(int current_hp) {
        this.current_hp = current_hp;
    }

    public void setDamage_resist(int damage_resist) {
        this.damage_resist = damage_resist;
    }

    public int getDamage_resist() {
        return damage_resist;
    }

    public void increaseDamageResist(int amount){
        this.damage_resist = this.damage_resist + amount;
    }

    public void foe_defeated(){this.foes_defeated ++;}

    public int getFoes_defeated(){return foes_defeated;}

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
