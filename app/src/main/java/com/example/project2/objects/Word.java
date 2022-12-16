package com.example.project2.objects;

import com.example.project2.DatabasePopulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Word {

    String[] letters;
    String answer;
    int max_letters;

    public Word(String answer) {
        this.answer = answer;
        setMax_letters(answer);
        setLetters(answer);
        shuffle();
    }

    public String[] getLetters() {return letters;}

    public void setLetters(String ans) {this.letters = ans.split("");}

    public String getAnswer() {return answer;}

    public void setAnswer(String answer) {this.answer = answer;}

    public int getMax_letters() {return max_letters;}

    public void setMax_letters(String answer) {this.max_letters = answer.length();}

    public void shuffle(){
        Random rnd = new Random();
        String[] ar = this.letters;
        for (int i = this.max_letters - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        this.letters = ar;
    }
}
