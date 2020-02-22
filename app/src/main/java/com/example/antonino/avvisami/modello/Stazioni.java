package com.example.antonino.avvisami.modello;

import java.util.ArrayList;

/**
 * Created by Antonino on 11/01/2018.
 */

public class Stazioni extends Stazione {


    private ArrayList<Stazione> stazioni;

    public void stazioni(){
        stazioni = new ArrayList<>();
    }

    public void add(Stazione s){
        stazioni.add(s);
    }

    public ArrayList<Stazione> getStazioni() {
        return stazioni;
    }

    public void setStazioni(ArrayList<Stazione> stazioni) {
        this.stazioni = stazioni;
    }
}
