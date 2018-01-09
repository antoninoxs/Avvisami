package com.example.antonino.avvisami;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Antonino on 07/01/2018.
 */
//        Map<String, String> map = new HashMap<>();
//                map.put("Lombardia", "1");
//                map.put("Liguria", "2");
//                map.put("Piemonte", "3");
//                map.put("Valle d'Aosta", "4");
//                map.put("Lazio", "5");
//                map.put("Umbria", "6");
//                map.put("Emilia ", "8");
//                map.put("na", "9");
//                map.put("Friuli-Venezia Giulia", "10");
//                map.put("Marche", "11");
//                map.put("Veneto", "12");
//                map.put("Toscana", "13");
//                map.put("Sicilia", "14");
//                map.put("Basilicata", "15");
//                map.put("Puglia", "16");
//                map.put("Calabria", "17");
//                map.put("Campania", "18");
//                map.put("Abruzzo", "19");
//                map.put("Sardegna", "20");
//                map.put("Trentino Alto Adige", "22");

public class Stazione {

    private String id;
    private String nome;
    private String lat;
    private String lon;

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    private String regID;

    public Stazione(){
    }

    public Stazione(String id, String nome, String lat, String lon){
        this.id = id;
        this.nome = nome;

        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }


    public void print() {
        Log.d("Stazione", id + " - " + nome + " - " + regID + " lat: " + lat + " lon: "+ lon);
    }

    public String getStringFermate(){
       return id + " - " + nome + " - " + regID + " lat: " + lat + " lon: "+ lon;
    }
}
