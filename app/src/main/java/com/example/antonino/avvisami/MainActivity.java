package com.example.antonino.avvisami;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText etTreno;
    ListView lvFermate;
    Button btnRicerca;
    Context ctx;

    String numTreno;

    Boolean trenoTrovato;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = MainActivity.this;

        trenoTrovato = true;


        final String result = "";
        etTreno = (EditText) findViewById(R.id.etTreno);
        lvFermate = (ListView) findViewById(R.id.lvFermate);

        btnRicerca = (Button) findViewById(R.id.btnRicerca);

        btnRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numTreno = etTreno.getText().toString();
                if(numTreno.length() != 0){
                    new DownloadFilesTask(ctx).execute(numTreno, "", "");
                }
                else {
                    trenoTrovato = false;
                    Log.d("treno.lenght", "ZERO");
                }
            }
        });


        Log.d("ANTONINOOOOOOOOOOOOOO", "INIZIO");
    }



    public class DownloadFilesTask extends AsyncTask<String, String, ArrayList<String>> {

        Context context = null;

        public DownloadFilesTask(Context c){
            this.context = c;
        }

        protected ArrayList<String> doInBackground(String... strings) {
            int count = strings.length;

            Log.d("STRINGS", strings[0]);
            ArrayList<Stazione> stazioni = new ArrayList<>();
            ArrayList<String> stringFermate = new ArrayList<>();

            // http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/cercaNumeroTrenoTrenoAutocomplete/numeroTreno
            String urlGetIdStazioneFromNumTreno = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/cercaNumeroTrenoTrenoAutocomplete/";

            // http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/andamentoTreno/IDStazionePartenza/numeroTreno
            String urlGetFermateFromIdStazioneAndNumTreno = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/andamentoTreno/";

            // http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/regione/IDStazione
            String urlGetIdRegioneFromIdStazione = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/regione/";

            // http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/dettaglioStazione/IDStazione/CodiceRegione
            String urlGetDettaglioStazione = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/dettaglioStazione/";

            String[] persone = null; // conterrà i risultati
            HttpClient request = new DefaultHttpClient();
            String url = urlGetIdStazioneFromNumTreno + strings[0];
            HttpGet get = new HttpGet(url);
            HttpResponse response = null;
            String nome = "";
            String idStazPart = "";


            Log.d("GET1", "");
            try {
                response = request.execute(get);

                int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode == 200) {
                    InputStream istream = response.getEntity().getContent();
                    BufferedReader r = new BufferedReader(new InputStreamReader(istream));
                    String s = null;
                    StringBuffer sb = new StringBuffer();
                    while ((s = r.readLine()) != null) {
                        sb.append(s);
                    }

                    if(sb.length() != 0){
                        idStazPart = sb.substring(sb.length()-6);
                        Log.d("SUBSTRING", idStazPart);
                        trenoTrovato = true;

                    }
                    else{
                        trenoTrovato = false;
                    }

                }
                else {
                    Log.d("GET1", responseCode+"");
                }
            } catch (IOException e) {
                e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
            }

            if (trenoTrovato) {
                Log.d("GET2", "");
                urlGetFermateFromIdStazioneAndNumTreno = urlGetFermateFromIdStazioneAndNumTreno + idStazPart + "/" + strings[0];
                get = new HttpGet(urlGetFermateFromIdStazioneAndNumTreno);

                try {
                    response = request.execute(get);

                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == 200) {
                        InputStream istream = response.getEntity().getContent();
                        BufferedReader r = new BufferedReader(new InputStreamReader(istream));
                        String s = null;
                        StringBuffer sb = new StringBuffer();
                        while ((s = r.readLine()) != null) {
                            sb.append(s);
                        }

                        Log.d("SUBSTRING", sb.toString());

                        JSONObject jsonObject = new JSONObject(sb.toString());
                        Log.d("JSON", jsonObject.toString(4));

                        JSONArray jaFermate = (JSONArray) jsonObject.get("fermate");

                        Log.d("JSONFERMATE", jaFermate.toString(4));
                        Log.d("JSONFERMATE NUM", jaFermate.length() + "");
                        int fermateLeng = jaFermate.length();

                        for (int i = 0; i < fermateLeng; i++) {
                            JSONObject joFermata = (JSONObject) jaFermate.get(i);

                            String idStaz = joFermata.get("id").toString();
                            String nomeStaz = joFermata.get("stazione").toString();

                            Log.d("JSONFERMATA", nomeStaz + " -*- " + idStaz);

                            Stazione stazione = new Stazione();
                            stazione.setId(idStaz);
                            stazione.setNome(nomeStaz);

                            stazioni.add(stazione);

                            Log.d("STAZIONI", stazioni.get(i).getId() + " -*- " + stazioni.get(i).getNome());
                        }
                    } else {
                        Log.d("GET2", responseCode + "");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("GET3", " ");
                Log.d("GET3", " ");

                for (int i = 0; i < stazioni.size(); i++) {

                    String tempUrlGetIdRegioneFromIdStazione = urlGetIdRegioneFromIdStazione + stazioni.get(i).getId();
                    Log.d("GET3", tempUrlGetIdRegioneFromIdStazione);
                    get = new HttpGet(tempUrlGetIdRegioneFromIdStazione);

                    try {
                        response = request.execute(get);

                        int responseCode = response.getStatusLine().getStatusCode();
                        if (responseCode == 200) {
                            InputStream istream = response.getEntity().getContent();
                            BufferedReader r = new BufferedReader(new InputStreamReader(istream));
                            String s = null;
                            StringBuffer sb = new StringBuffer();
                            while ((s = r.readLine()) != null) {
                                sb.append(s);
                            }

                            Log.d("SUBSTRING", sb.toString());

                            Stazione reg = stazioni.get(i);
                            reg.setRegID(sb.toString());
                            stazioni.set(i, reg);
                        } else {
                            Log.d("GET3", responseCode + "");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


                Log.d("GET4", " ");
                Log.d("GET4", " ");

                for (int i = 0; i < stazioni.size(); i++) {

                    String tempUrlGetDettaglioStazione = urlGetDettaglioStazione + stazioni.get(i).getId() + "/" + stazioni.get(i).getRegID();
                    Log.d("GET4", tempUrlGetDettaglioStazione);
                    get = new HttpGet(tempUrlGetDettaglioStazione);

                    try {
                        response = request.execute(get);

                        int responseCode = response.getStatusLine().getStatusCode();
                        if (responseCode == 200) {
                            InputStream istream = response.getEntity().getContent();
                            BufferedReader r = new BufferedReader(new InputStreamReader(istream));
                            String s = null;
                            StringBuffer sb = new StringBuffer();
                            while ((s = r.readLine()) != null) {
                                sb.append(s);
                            }

                            Log.d("SUBSTRING", sb.toString());

                            JSONObject jsonObject = new JSONObject(sb.toString());
                            Log.d("JSON", jsonObject.toString(4));

                            double lat = (double) jsonObject.get("lat");
                            double lon = (double) jsonObject.get("lon");

                            Log.d("LAT", lat + "");
                            Log.d("LON", lon + "");

                            Stazione reg = stazioni.get(i);
                            reg.setLat(lat + "");
                            reg.setLon(lon + "");
                            stazioni.set(i, reg);
                        } else {
                            Log.d("GET4", responseCode + "");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                for (int i = 0; i < stazioni.size(); i++) {
                    stazioni.get(i).print();

                    stringFermate.add(stazioni.get(i).getStringFermate());
                }

            }

            return stringFermate;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);

            for (int i = 0; i< s.size(); i++){
                Log.d("ONPOST", s.get(i));
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, s);
            lvFermate.setAdapter(arrayAdapter);

            if(trenoTrovato == false){
                Toast.makeText(context, "TRENO NON TROVATO", Toast.LENGTH_LONG).show();
            }
        }
    }
}
