package com.example.antonino.avvisami;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Antonino on 10/01/2018.
 */

class MyLocationListener implements LocationListener {

    protected LocationManager locationManager;
    Location location;
    Context ctx;

    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

    public MyLocationListener(Context context) {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        ctx = context;
        Toast.makeText(ctx, "MyLocationListener", Toast.LENGTH_LONG).show();

    }

    public Location getLocation(String provider) {
        Toast.makeText(ctx, "GetLocation", Toast.LENGTH_LONG).show();
        if (locationManager.isProviderEnabled(provider)) {
            Toast.makeText(ctx, "IF1", Toast.LENGTH_LONG).show();
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(ctx, "IF2", Toast.LENGTH_LONG).show();
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(provider, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(provider);


                double latitude=location.getLatitude();
                double longitude=location.getLongitude();

                Toast.makeText(ctx, latitude + " "+ longitude, Toast.LENGTH_LONG).show();
                return location;
            }
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        Toast.makeText(ctx, "ON LOCATION: " + latitude + " "+ longitude, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
