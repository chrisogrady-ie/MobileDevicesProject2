package com.example.project2.objects;

public class Monster {

    String name;
    int damage, max_hp, damage_interval, current_hp;

    public Monster(String name, int damage, int max_hp, int damage_interval) {
        this.name = name;
        this.damage = damage;
        this.max_hp = max_hp;
        this.damage_interval = damage_interval;
        this.current_hp = max_hp;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public int getDamage() {return damage;}

    public void setDamage(int damage) {this.damage = damage;}

    public int getMax_hp() {return max_hp;}

    public void setMax_hp(int max_hp) {this.max_hp = max_hp;}

    public int getCurrent_hp(){return current_hp;}

    public int getDamage_interval() {return damage_interval;}

    public void setDamage_interval(int damage_interval) {this.damage_interval = damage_interval;}

    public void take_damage(int damage_received){
        this.current_hp = (this.current_hp - damage_received);
    }
}
