package com.example.antonino.avvisami;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.antonino.avvisami.modello.Stazione;

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
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    EditText etTreno;
    ListView lvFermate;
    Button btnRicerca;
    Context ctx;

    Timer timer;

    ArrayList<Stazione> stazioniGlobal;

    private String providerId = LocationManager.NETWORK_PROVIDER;
    private static final int MIN_DIST = 1;
    private static final int MIN_PERIOD = 30000;


    LocationManager locationManager;
    MyLocationListener locationListener;

    StazioniAdapter stazioniAdapter;

    SoundPool sp;

    PowerManager powerManager;

    Camera cam = null;

    String numTreno;

    Boolean trenoTrovato;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = MainActivity.this;

         window = this.getWindow();

        locationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener(ctx);


        stazioniGlobal = new ArrayList<>();

        trenoTrovato = true;
        stazioniAdapter = new StazioniAdapter(ctx, android.R.layout.simple_list_item_1, stazioniGlobal);


        final String result = "";
        etTreno = (EditText) findViewById(R.id.etTreno);
        lvFermate = (ListView) findViewById(R.id.lvFermate);

        btnRicerca = (Button) findViewById(R.id.btnRicerca);

        timer = new Timer();

        powerManager = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);



        btnRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numTreno = etTreno.getText().toString();
                if (numTreno.length() != 0) {
                    new DownloadFilesTask(ctx).execute(numTreno, "", "");

                    timer.scheduleAtFixedRate( new TimerTask() {
                        public void run() {
                            try{
                                new CalculateDistance(ctx).execute(stazioniGlobal);

                                for (int i = 0; i < stazioniGlobal.size(); i++){
                                    Stazione s = stazioniGlobal.get(i);

                                    if (s.isPassato() && !s.isNotificato()) {
                                        Log.d("VIBRAZIONE", "VIBRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                        // Vibrate for 500 milliseconds
                                        v.vibrate(3000);
                                        v.vibrate(500);
                                        v.vibrate(3000);

                                        // emetti il suono!
                                        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
                                        int soundId = sp.load(ctx, R.raw.squillotelefonocampanello, 1); // in 2nd param u have to pass your desire ringtone
                                        sp.play(soundId, 100, 100, 0, 1, 1);

                                        // Accendi lo schermo
                                        Log.d("SCHERMMOOOOOOOOOOOOO", "ACCENSIONEEEEEEEEEEEEEEEEEE");
                                        PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
                                        wl.acquire();

                                        // Accendo la torcia
                                        cam = Camera.open();
                                        Camera.Parameters p = cam.getParameters();
                                        p = cam.getParameters();
                                        Log.d("TORCIAAAAAAAAAAA", "ACCENSIONEEEEEEEEEEEEEEEEEE");
                                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                        cam.setParameters(p);
                                        cam.startPreview();
                                        cam.release();

                                        s.setNotificato(true);

                                        stazioniGlobal.set(i, s);
                                    }

                                    if (s.isPassato2() && !s.isNotificato2()) {
                                        Log.d("VIBRAZIONE", "VIBRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                        // Vibrate for 500 milliseconds
                                        v.vibrate(3000);
                                        v.vibrate(500);
                                        v.vibrate(3000);

                                        // emetti il suono!
                                        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
                                        int soundId = sp.load(ctx, R.raw.squillotelefonocampanello, 1); // in 2nd param u have to pass your desire ringtone
                                        sp.play(soundId, 100, 100, 0, 1, 1);

                                        // Accendi lo schermo
                                        Log.d("SCHERMMOOOOOOOOOOOOO", "ACCENSIONEEEEEEEEEEEEEEEEEE");
                                        PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
                                        wl.acquire();

                                        // Accendo la torcia
                                        cam = Camera.open();
                                        Camera.Parameters p = cam.getParameters();
                                        p = cam.getParameters();
                                        Log.d("TORCIAAAAAAAAAAA", "ACCENSIONEEEEEEEEEEEEEEEEEE");
                                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                        cam.setParameters(p);
                                        cam.startPreview();
                                        cam.release();

                                        s.setNotificato2(true);

                                        stazioniGlobal.set(i, s);
                                    }
                                }
                            }
                            catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }, 0, 10000);
                } else {
                    trenoTrovato = false;
                    Log.d("treno.lenght", "ZERO");
                }
            }
        });


    }

    public class CalculateDistance extends AsyncTask<ArrayList<Stazione>, String, String> {

        Context context = null;
        public CalculateDistance(Context c){
            this.context = c;
        }

        @Override
        protected String doInBackground(ArrayList<Stazione>[] staz) {

            ArrayList<Stazione> stazioni = staz[0];

            for (int i = 0; i < stazioni.size(); i++) {
                if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return "";
                }
                Location currLoc = locationManager.getLastKnownLocation(providerId);

                if(currLoc != null || stazioni != null || stazioni.size()!=0){
                    float distanceKM = (currLoc.distanceTo(stazioni.get(i).getLoc()))/1000;
                    Log.d("CALCULATE DISTANCE" , "DISTANCE to " + stazioni.get(i).getNome() + "   " + distanceKM + "km");
                    Stazione s = stazioni.get(i);
                    s.setDistance(distanceKM);


                    if(distanceKM < 9){
                        Log.d("IFPASSATO", distanceKM +"  to " + stazioni.get(i).getNome());
                        s.setPassato(true);
                    }

                    if(distanceKM < 4){
                        Log.d("IFPASSATO", distanceKM +"  to " + stazioni.get(i).getNome());
                        s.setPassato2(true);
                    }
                    stazioni.set(i, s);
                }
                else
                    Log.d("CALCULATE DISTANCE" , "CURR LOC == null");

            }
            Log.d("CALCULATE DISTANCE" , " ");
            Log.d("CALCULATE DISTANCE" , " ");
            return null;
        }

        @Override
        protected void onPostExecute(String stazioni){
            stazioniAdapter.notifyDataSetChanged();
        }

    }

    public class DownloadFilesTask extends AsyncTask<String, String, ArrayList<Stazione>> {

        Context context = null;

        public DownloadFilesTask(Context c){
            this.context = c;
        }

        protected ArrayList<Stazione> doInBackground(String... strings) {

            Log.d("STRINGS", strings[0]);
            ArrayList<Stazione> stazioni = new ArrayList<>();

            // http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/cercaNumeroTrenoTrenoAutocomplete/numeroTreno
            String urlGetIdStazioneFromNumTreno = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/cercaNumeroTrenoTrenoAutocomplete/";

            // http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/andamentoTreno/IDStazionePartenza/numeroTreno
            String urlGetFermateFromIdStazioneAndNumTreno = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/andamentoTreno/";

            // http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/regione/IDStazione
            String urlGetIdRegioneFromIdStazione = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/regione/";

            // http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/dettaglioStazione/IDStazione/CodiceRegione
            String urlGetDettaglioStazione = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/dettaglioStazione/";

            HttpClient request = new DefaultHttpClient();
            String url = urlGetIdStazioneFromNumTreno + strings[0];
            HttpGet get = new HttpGet(url);
            HttpResponse response = null;
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

//                        Log.d("SUBSTRING", sb.toString());

                        JSONObject jsonObject = new JSONObject(sb.toString());
//                        Log.d("JSON", jsonObject.toString(4));

                        JSONArray jaFermate = (JSONArray) jsonObject.get("fermate");

//                        Log.d("JSONFERMATE", jaFermate.toString(4));
//                        Log.d("JSONFERMATE NUM", jaFermate.length() + "");
                        int fermateLeng = jaFermate.length();

                        for (int i = 0; i < fermateLeng; i++) {
                            JSONObject joFermata = (JSONObject) jaFermate.get(i);

                            String idStaz = joFermata.get("id").toString();
                            String nomeStaz = joFermata.get("stazione").toString();

//                            Log.d("JSONFERMATA", nomeStaz + " -*- " + idStaz);

                            Stazione stazione = new Stazione();
                            stazione.setId(idStaz);
                            stazione.setNome(nomeStaz);
                            stazione.setPassato(false);

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

                    Stazione stazione = stazioni.get(i);

                    try {
                        response = request.execute(get);
                        int responseCode = response.getStatusLine().getStatusCode();

                        Log.d("STAZIONE ID", stazioni.get(i).getId());
                        if(stazioni.get(i).getId().equals("S09988")){
                            Log.d("GET 3 IFF", stazioni.get(i).getId());
                            stazione.setRegID("18");
                            stazioni.set(i, stazione);
                        }

                        if (responseCode == 200) {
                            InputStream istream = response.getEntity().getContent();
                            BufferedReader r = new BufferedReader(new InputStreamReader(istream));
                            String s = null;
                            StringBuffer sb = new StringBuffer();
                            while ((s = r.readLine()) != null) {
                                sb.append(s);
                            }

                            Log.d("SUBSTRING", sb.toString());

                            stazione.setRegID(sb.toString());
                            stazioni.set(i, stazione);
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


                    Stazione reg = stazioni.get(i);

                    try {
                        response = request.execute(get);
                        int responseCode = response.getStatusLine().getStatusCode();

                        double lat;
                        double lon;



                        Log.d("STAZIONE ID", stazioni.get(i).getId());
                        if(stazioni.get(i).getId().equals("S09988")){
                            lat = 40.931822;
                            lon = 14.331447;

                            Location loc = new Location(providerId);
                            loc.setLatitude(lat);
                            loc.setLongitude(lon);
                            reg.setLoc(loc);
                            stazioni.set(i, reg);
                        }

                        if (responseCode == 200) {
                            InputStream istream = response.getEntity().getContent();
                            BufferedReader r = new BufferedReader(new InputStreamReader(istream));
                            String s = null;
                            StringBuffer sb = new StringBuffer();
                            while ((s = r.readLine()) != null) {
                                sb.append(s);
                            }

//                            Log.d("SUBSTRING", sb.toString());

                            JSONObject jsonObject = new JSONObject(sb.toString());
//                            Log.d("JSON", jsonObject.toString(4));

                            lat = (double) jsonObject.get("lat");
                            lon = (double) jsonObject.get("lon");

//                            Log.d("LAT", lat + "");
//                            Log.d("LON", lon + "");


                            reg.setLat(lat + "");
                            reg.setLon(lon + "");
                            Location loc = new Location(providerId);
                            loc.setLatitude(lat);
                            loc.setLongitude(lon);
                            reg.setLoc(loc);
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
            }
            return stazioni;
        }

        @Override
        protected void onPostExecute(ArrayList<Stazione> stazioni) {
            super.onPostExecute(stazioni);

            ArrayList<String> stringFermate = new ArrayList<>();
            for (int i = 0; i < stazioni.size(); i++) {
                stazioni.get(i).print();
                stringFermate.add(stazioni.get(i).getStringFermate());
                stazioniGlobal.add(stazioni.get(i));
            }

//            for (int i = 0; i< stringFermate.size(); i++){
//                Log.d("ONPOST", stringFermate.get(i));
//            }

            lvFermate.setAdapter(stazioniAdapter);
            stazioniAdapter.notifyDataSetChanged();


            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(providerId)) {
                Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(gpsOptionsIntent);
            } else if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(providerId, MIN_PERIOD, MIN_DIST, locationListener);

            if(trenoTrovato == false){
                Toast.makeText(context, "TRENO NON TROVATO", Toast.LENGTH_LONG).show();
            }
        }
    }


}
